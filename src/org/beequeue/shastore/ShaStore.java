package org.beequeue.shastore;

import java.io.File;

public interface ShaStore {
	/**
	 * Store file or directory 
	 * @param file
	 * @return return 
	 */
	ShaCode push(File file);
	
	/**
	 * Pull file or directory
	 * @param code
	 * @param destination
	 */
	void pull(ShaCode code, File destination);
	
	/**
	 * update sweept_on timestamp on  all files and directories that 
	 * used in any tables and
	 */
	void sweep();
}
