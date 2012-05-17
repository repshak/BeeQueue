package org.beequeue.hash;

import java.io.File;

public interface HashStore {
	
	/**
	 * push new content into named content tree in 
	 * datastore
	 * @param dir source dir
	 * @param content name of content tree
	 * @return hashkey
	 */
	HashKey push(File source, String content);
	
	/**
	 * Synchronize destination with datastore 
	 * @param content
	 * @param destination
	 * @return true if changes detected and downloaded 
	 */
	boolean sync(String content, File destination);
	
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
	void pull(HashKey code, File destination);
	
	/**
	 * update sweept_on timestamp on  all files and directories that 
	 * used in any tables and
	 */
	void sweep();
}
