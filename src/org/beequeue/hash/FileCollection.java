package org.beequeue.hash;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.beequeue.sql.DalException;
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
		CopyOnWriteArrayList<FileEntry> entries = new CopyOnWriteArrayList<FileEntry>();
		CopyOnWriteArrayList<String> errors = new CopyOnWriteArrayList<String>();
		try {
			ExecutorService threadPool = Executors.newFixedThreadPool(4);
			constructEntries(base, null, threadPool, entries, errors);
			threadPool.shutdown();
			for (int i = 0; !threadPool.awaitTermination(1, TimeUnit.HOURS); i++) {
				log.warning(""+i+"hours passed by.");
				if(i > 24){
					errors.add("waiting for too long:"+ threadPool.shutdownNow() );
					break;
				}
			}
		} catch (Exception e) {
			throw new DalException(e).withPayload(errors.toArray());
		}
		if(errors.size() > 0){
			throw new DalException("has errors:").withPayload(errors.toArray());
		}
		FileCollection fileCollection = new FileCollection();
		fileCollection.entries = entries.toArray(new FileEntry[entries.size()]);
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

	public List<FileEntry> selectByHashes(Set<HashKey> hashes) {
		List<FileEntry> selected = new ArrayList<FileEntry>();
		for (int i = 0; i < entries.length; i++) {
			FileEntry e = entries[i];
			if( hashes.contains(e.code) ){
				selected.add(e);
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
			throw new DalException(e);
		}finally{
			try { oin.close(); } catch (Exception ignore) {}
		}
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
					oout.writeUTF(entries[0].toString());
				}
				oout.flush();
				entriesDataBuffer = out.toByteArray();
			} catch (IOException e) {
				throw new DalException(e);
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
