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
package org.beequeue.template;

public enum AttributeType {
	/**
	 * if messages differs by such attributes, than parallel processing is allowed  
	 */
	PARALLEL(true),
	/**
	 * if all parallel attributes are the same and messages only differ by sequential attributes than such 
	 * messages will be processed sequentially in order they was received.
	 * 
	 */
	SEQUENTIAL(true),
	/**
	 * informational attributes what tag along with message and have no influence how message is processed
	 */
	INFORMATIONAL(false) ;
	
	public final boolean key;
	private AttributeType(boolean key){
		this.key = key;
	}
}
