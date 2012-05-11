package org.beequeue.shastore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.beequeue.shastore.ShaCode.Resource;
import org.beequeue.sql.DalException;
import org.beequeue.sql.Select;
import org.beequeue.util.Dirs;
import org.beequeue.util.Strings;

public class FileEntry {
	private static final Logger log = Logger.getLogger(Select.class.getName());

	public final ShaCode code;
	public final boolean executible;
	public final String path;
	
	public FileEntry(ShaCode code, boolean executible, String path) {
		this.code = code;
		this.executible = executible;
		this.path = path;
	}
	
	public static FileEntry valueOf(String s){
		return new FileEntry( ShaCode.valueOf( s.substring(0, 41)) , 
				s.substring(42,43).equals("x") , s.substring(44)  );
	}

	public ShaInput input(File base) throws FileNotFoundException{
		File file = file(base);
		return new ShaInput(this.code, new FileInputStream(file));
	}
	public ShaOutput output(File base) throws FileNotFoundException{
		File file = file(base);
		Dirs.ensureParentDirExists(file);
		return new ShaOutput(this.code, new FileOutputStream(file));
	}

	public File file(File base) {
		return new File(base,path);
	}


	@Override
	public String toString() {
		return "" + code + "," + (executible ? "x" : "-") + "," + path;
	}
	
	
	static Set<String> IGNORED_SCM_DIRECTORIES = new HashSet<String>(Arrays.asList("CVS", ".svn", ".git"));
	public static List<FileEntry> readEntries(File base){
		CopyOnWriteArrayList<FileEntry> entries = new CopyOnWriteArrayList<FileEntry>();
		CopyOnWriteArrayList<String> errors = new CopyOnWriteArrayList<String>();
		try {
			ExecutorService threadPool = Executors.newFixedThreadPool(4);
			constructEntries(base, "", threadPool, entries, errors);
			threadPool.shutdown();
			for (int i = 0; !threadPool.awaitTermination(1, TimeUnit.HOURS); i++) {
				log.warning(""+i+"hours passed by.");
				if(i > 24){
					errors.add("waiting for too long");
					threadPool.shutdownNow();
					break;
				}
			}
		} catch (Exception e) {
			throw new DalException(e).withPayload(errors.toArray());
		}
		if(errors.size() > 0){
			throw new DalException("has errors:").withPayload(errors.toArray());
		}
		return entries;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + (executible ? 1231 : 1237);
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileEntry that = (FileEntry) obj;
		if (code == null) {
			if (that.code != null)
				return false;
		} else if (!code.equals(that.code))
			return false;
		if (executible != that.executible)
			return false;
		if (path == null) {
			if (that.path != null)
				return false;
		} else if (!path.equals(that.path))
			return false;
		return true;
	}

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
				if(  !IGNORED_SCM_DIRECTORIES.contains(newName) 
					||  new File(base, newPathString).isFile() ) {
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
					ShaCode shaCode;
					FileInputStream in = null;
					try {
						in = new FileInputStream(fileToProcess);
						shaCode = ShaCode.buildShaCode( Resource.F,in);
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
	
}
