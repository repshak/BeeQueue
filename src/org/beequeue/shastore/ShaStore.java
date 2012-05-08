package org.beequeue.shastore;

import java.io.File;

public interface ShaStore {
	/**
	 * Store file or directory 
	 * @param file
	 * @return return 
	 */
	ShaCode push(File file);
	void pull(ShaCode code, File destination);
	void sweep();
}
