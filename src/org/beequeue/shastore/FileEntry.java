package org.beequeue.shastore;

public class FileEntry {
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

	@Override
	public String toString() {
		return "" + code + "," + (executible ? "x" : "-") + "," + path;
	}
	
	
	
}
