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
package org.beequeue.sql;


public class SqlUtil {
	
	/**
	 * builds line like this "?, ?, ?, ?" if you pass 4 as argument
	 */
	public static String buildPlaceholderList(int count){
		StringBuffer sb = new StringBuffer(count*3-2);
		for (int i = 0; i < count; i++) {
			if(i != 0){
				sb.append(", ");
			}
			sb.append("?");
		}
		return sb.toString();
	}
  

}
