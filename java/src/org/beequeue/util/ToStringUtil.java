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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * ToStringUtil - class used for debugging purposes. That class uses reflection
 * to introspect object properties and print out them. <br>
 * Is is pretty CPU expensive so use with caution.
 */
public class ToStringUtil {
	public static ObjectMapper MAPPER = new ObjectMapper()
	.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	
	public static TypeFactory<Object> TF =new TypeFactory<Object>(Object.class);

	public String toString() {
		return toString(this);
	}

	public static String toString(Object o) {
		return writeObject(o, MAPPER.writerWithDefaultPrettyPrinter());
	}

	private static String writeObject(Object o, ObjectWriter writer) {
		try {
			return writer.writeValueAsString(o);
		} catch (Exception e) {
			throw BeeException.cast(e);
		}
	}

	public static String toNotPrettyString(Object o) {
		return writeObject(o, MAPPER.writer());
	}
	
	public static <T> T toObject(String s, Class<T> type)  {
		try {
			T readValue = MAPPER.readValue(s, type);
			if (readValue instanceof Initializable) {
				Initializable toInit = (Initializable) readValue;
				toInit.init();
			}
			return readValue;
		} catch (Exception e) {
			throw BeeException.cast(e);
		}
	}
	
	public static <T> T toObject(String s, TypeReference<T> type){
		try {
			T readValue = MAPPER.readValue(s, type);
			if (readValue instanceof Initializable) {
				Initializable toInit = (Initializable) readValue;
				toInit.init();
			}
			return readValue;
		} catch (Exception e) {
			throw BeeException.cast(e);
		}
	}

	public static void out(Object o) {
		out("", o);
	}
	
	public static void out(String msg, Object o) {
		System.out.println(msg+toString(o));
	}
	
	public static void err(Object o) {
		err("",o);
	}
	
	public static void err(String msg, Object o) {
		System.err.println(msg+toString(o));
	}

}