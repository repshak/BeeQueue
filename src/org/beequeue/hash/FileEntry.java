package org.beequeue.hash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.beequeue.util.Dirs;

import com.fasterxml.jackson.annotation.JsonValue;

public class FileEntry implements Comparable<FileEntry>{

	public final HashKey code;
	public final boolean executible;
	public final String path;
	public final String s;
	
	public FileEntry(HashKey code, boolean executible, String path) {
		this.code = code;
		this.executible = executible;
		this.path = path;
		this.s = "" + code + "," + (executible ? "x" : "-") + "," + path ;
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
	
	@Override @JsonValue
	public String toString() {
		return s;
	}
	
	@Override
	public int hashCode() {
		return s.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FileEntry) {
			FileEntry that = (FileEntry) obj;
			return this.s.equals(that.s);
		}
		return false;
	}

	@Override
	public int compareTo(FileEntry that) {
		return this.s.compareTo(that.s);
	}

		
}
