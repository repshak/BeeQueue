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
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * ToStringUtil - de/serializes objects to JSON and YAML
 */
final public class ToStringUtil {
	public static ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory())
	.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

	public static ObjectMapper JSON_MAPPER = new ObjectMapper()
	.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	
	public static TypeFactory<Object> TF =new TypeFactory<Object>(Object.class);

	
	public static String toString(Object o) {
		return writeObject(o, JSON_MAPPER.writerWithDefaultPrettyPrinter());
	}
	
	public static String toYamlString(Object o) {
		return writeObject(o, YAML_MAPPER.writer());
	}

	private static String writeObject(Object o, ObjectWriter writer) {
		try {
			return writer.writeValueAsString(o);
		} catch (Exception e) {
			throw BeeException.cast(e);
		}
	}

	public static String toNotPrettyString(Object o) {
		return writeObject(o, JSON_MAPPER.writer());
	}
	
	public static <T> T toObject(String s, Class<T> type)  {
		return toObject(JSON_MAPPER, s, type);
	}
	
	public static <T> T toObject(String s, TypeReference<T> type){
		return toObject(JSON_MAPPER, s, type);
	}
	
	public static <T> T toYamlObject(String s, Class<T> type)  {
		return toObject(YAML_MAPPER, s, type);
	}

	public static <T> T toYamlObject(String s, TypeReference<T> type){
		return toObject(YAML_MAPPER, s, type);
	}

	public static <T> T toObject(ObjectMapper mapper, String s, Class<T> type) {
		try {
			T readValue = mapper.readValue(s, type);
			if (readValue instanceof Initializable) {
				Initializable toInit = (Initializable) readValue;
				toInit.init();
			}
			return readValue;
		} catch (Exception e) {
			throw BeeException.cast(e);
		}
	}

	public static <T> T toObject(ObjectMapper mapper, String s,
			TypeReference<T> type) {
		try {
			T readValue = mapper.readValue(s, type);
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
