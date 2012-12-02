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

import java.io.File;

public interface HashStore {
	
	
	/**
	 * push new content into named content tree in 
	 * datastore
	 * @param dir source dir
	 * @param content name of content tree
	 * @return content tree 
	 */
	ContentTree push(File source, String content);
	
	/**
	 * Synchronize destination with datastore 
	 * @param content
	 * @param destination
	 * @return new ContentTree if changes are detected, or <code>null</code> otherwise 
	 */
	ContentTree sync(ContentTree contentTree, File destination);
	
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
