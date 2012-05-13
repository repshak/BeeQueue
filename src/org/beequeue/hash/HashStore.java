package org.beequeue.hash;

import java.io.File;

public interface HashStore {
	/**
	 * Store file or directory 
	 * @param file
	 * @return return 
	 */
	HashKey push(File file);
	
	/**
	 * Pull file or directory
	 * @param code
	 * @param destination
	 */
	void pull(HashKey code, File destination, File previousPull);
	
	/**
	 * update sweept_on timestamp on  all files and directories that 
	 * used in any tables and
	 */
	void sweep();
}
