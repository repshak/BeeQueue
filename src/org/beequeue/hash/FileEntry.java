package org.beequeue.hash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.beequeue.util.Dirs;

public class FileEntry {

	public final HashKey code;
	public final boolean executible;
	public final String path;
	
	public FileEntry(HashKey code, boolean executible, String path) {
		this.code = code;
		this.executible = executible;
		this.path = path;
	}
	
	public static FileEntry valueOf(String s){
		return new FileEntry( HashKey.valueOf( s.substring(0, 41)) , 
				s.substring(42,43).equals("x") , s.substring(44)  );
	}

	public HashInput input(File base) throws FileNotFoundException{
		File file = file(base);
		return new HashInput(this.code, new FileInputStream(file));
	}
	
	public HashOutput output(File base) throws FileNotFoundException{
		File file = file(base);
		Dirs.ensureParentDirExists(file);
		return new HashOutput(this.code, new FileOutputStream(file));
	}

	public File file(File base) {
		return new File(base,path);
	}

	@Override
	public String toString() {
		return "" + code + "," + (executible ? "x" : "-") + "," + path;
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

		
}
