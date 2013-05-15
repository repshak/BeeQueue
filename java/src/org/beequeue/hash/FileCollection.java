/** ==== BEGIN LICENSE =====
   Copyright 2012 - BeeQueue.org

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
 *  ===== END LICENSE ====== */
package org.beequeue.hash;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.beequeue.util.BeeException;
import org.beequeue.util.Strings;

public class FileCollection {
	public static final String HASH_STORE_FILE = ".Hash_Store";
	
	private static final Logger log = Logger.getLogger(FileCollection.class.getName());
	public FileEntry[] entries;
	public byte[] entriesDataBuffer = null ;
	public HashKey entriesDataHashKey = null;
	
	
	public Set<HashKey> getAllCodes() {
		Set<HashKey> allFiles = new HashSet<HashKey>();
		for (int i = 0; i < this.entries.length; i++) {
			allFiles.add(this.entries[i].code);
		}
		return allFiles;
	}

	public static FileCollection scan(File base){
		return scan(base, 24*60);
	}

	public static FileCollection scan(File base, int maxWaitTime) {
		CopyOnWriteArrayList<FileEntry> entries = new CopyOnWriteArrayList<FileEntry>();
		CopyOnWriteArrayList<String> errors = new CopyOnWriteArrayList<String>();
		try {
			ExecutorService threadPool = Executors.newFixedThreadPool(4);
			constructEntries(base, null, threadPool, entries, errors);
			threadPool.shutdown();
			int i = 0;
			while( !threadPool.awaitTermination(1, TimeUnit.MINUTES) ) {
				if(i > maxWaitTime){
					errors.add("waiting for too long:"+ threadPool.shutdownNow() );
					break;
				}
				i += 1;
				if(i % 60 == 59 ) {
					log.warning(""+i+"hours passed by.");
				}
			}
		} catch (Exception e) {
			throw new BeeException(e)
			.memo("errors",errors.toArray());
		}
		if(errors.size() > 0){
			throw new BeeException("has errors")
			.memo("errors",errors.toArray());
		}
		FileCollection fileCollection = new FileCollection();
		FileEntry[] array = entries.toArray(new FileEntry[entries.size()]);
		Arrays.sort(array);
		fileCollection.entries = array;
		return fileCollection;
	}

	static Set<String> IGNORED_FILES = new HashSet<String>(Arrays.asList(".DS_Store", HASH_STORE_FILE));
	static Set<String> IGNORED_DIRECTORIES = new HashSet<String>(Arrays.asList("CVS", ".svn", ".git"));
	private static void constructEntries(File base, final String path, 
			ExecutorService threadPool, 
			final CopyOnWriteArrayList<FileEntry> entries, 
			final CopyOnWriteArrayList<String> errors) 
	{
		StringBuilder newPath = new StringBuilder();
		File file;
		if (Strings.isEmpty(path)) {
			file = base;
		} else {
			file = new File(base,path);
			newPath.append(path);
			newPath.append("/");
		}
		int resetNewPathBuilder = newPath.length() ;
		if(file.isDirectory()){
			String[] list = file.list();
			for (String newName : list) {
				newPath.delete(resetNewPathBuilder,Integer.MAX_VALUE);
				newPath.append(newName);
				String newPathString = newPath.toString();
				if( (IGNORED_DIRECTORIES.contains(newName)  &&  new File(base, newPathString).isDirectory())
					|| (IGNORED_FILES.contains(newName)  &&  new File(base, newPathString).isFile()) ) {
				    continue; // ignore
				}else{
					constructEntries(base, newPathString, threadPool, entries, errors);
				}
			}
		}else{
			final File fileToProcess = file;
			threadPool.execute(
			new Runnable(){
				@Override
				public void run() {
					boolean executable = fileToProcess.canExecute();
					HashKey shaCode;
					FileInputStream in = null;
					try {
						in = new FileInputStream(fileToProcess);
						shaCode = HashKey.buildHashKey( HashKeyResource.F,in);
						entries.add( new FileEntry( shaCode , executable, path ));
					} catch (IOException e) {
						errors.add( "Cannot read:" + fileToProcess+ " "+ e );
					}finally{
						try { in.close();  } catch (Exception ignore) {}
					}
					
				}
				
			});
		}
	}

	public Map<HashKey,FileEntry> selectByHashes(Set<HashKey> hashes) {
		Map<HashKey,FileEntry> selected = new LinkedHashMap<HashKey,FileEntry>();
		for (int i = 0; i < entries.length; i++) {
			FileEntry e = entries[i];
			if( hashes.contains(e.code) ){
				selected.put(e.code,e);
			}
		}
		return selected;
	}

	public boolean isFile() {
		return entries.length == 1 && entries[0].path == null;
	}

	public HashKey getFileKey() {
		return entries[0].code;
	}

	public static FileCollection read(File f){
		try {
			return read(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			throw new BeeException(e);
		}
	}

	public static FileCollection read(InputStream in){
		FileCollection fileCollection = new FileCollection();
		ObjectInputStream oin = null;
		try {
			oin = new ObjectInputStream(in);
			fileCollection.entries = new FileEntry[oin.readInt()];
			for (int i = 0; i < fileCollection.entries.length; i++) {
				String readLine = oin.readUTF();
				fileCollection.entries[i] = FileEntry.valueOf(readLine);
			}
			return fileCollection;
		} catch (IOException e) {
			throw new BeeException(e);
		}finally{
			try { oin.close(); } catch (Exception ignore) {}
		}
	}
	
	public static HashKey calcHashKey(File dir) {
		HashKey hk = null;
		if(dir.isDirectory()) {
			File hashStoreFile = new File(dir, FileCollection.HASH_STORE_FILE);
			if(hashStoreFile.exists()){
				try {
					hk = HashKey.buildHashKey(
							HashKeyResource.D, 
							new FileInputStream(hashStoreFile) );
				} catch (Exception e) {
					throw BeeException.cast(e);
				}
			}else{
				FileCollection scan = scan(dir);
				hk = scan.getEntriesDataKey();
			}
		}
		return hk;
	}

	public static ContentTree buildContentTree(String contentName, File local) {
		ContentTree contentTree = new ContentTree();
		contentTree.name = contentName;
		contentTree.hashKey = calcHashKey(local);
		return contentTree;
	}
	
	
	private byte[] ensureEntriesData(){
		if(entriesDataBuffer == null){
			ByteArrayOutputStream out = null;
			ObjectOutputStream oout = null;
			try {
				out = new ByteArrayOutputStream();
				oout = new ObjectOutputStream(out);
				oout.writeInt(entries.length);
				for (int i = 0; i < entries.length; i++) {
					oout.writeUTF(entries[i].toString());
				}
				oout.flush();
				entriesDataBuffer = out.toByteArray();
			} catch (IOException e) {
				throw new BeeException(e);
			}finally{
				try {out.close();} catch (Exception ignore) {}
				try {oout.close();} catch (Exception ignore) {}
			}
		}
		return entriesDataBuffer;
	}
	
	public HashKey getEntriesDataKey() {
		if(entriesDataHashKey == null){
			entriesDataHashKey = HashKey.buildHashKey(
					HashKeyResource.D, 
					ensureEntriesData()
					);
		}
		return entriesDataHashKey;
	}
	
	public HashInput getEntriesData(){
		ByteArrayInputStream in = new ByteArrayInputStream(ensureEntriesData());
		HashKey key = getEntriesDataKey();
		return new HashInput(key, in);
	}
	
	
	

}
