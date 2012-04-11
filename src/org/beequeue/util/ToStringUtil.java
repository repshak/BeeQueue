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
package org.beequeue.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * ToStringUtil - class used for debugging purposes. That class uses 
 * reflection to introspect object properties and print out them. 
 * <br>
 * Is is pretty CPU expensive so use with caution.
 */
public class ToStringUtil
{
	public static ObjectMapper MAPPER = new ObjectMapper();
	
	public String toString() {
		return toString(this);
	}


	public static String toString(Object o) {
		ObjectWriter writer = MAPPER.writerWithDefaultPrettyPrinter();
	    try {
			return writer.writeValueAsString(o);
		} catch (Exception e) {
			return e.toString();
		}
	}

    
}
