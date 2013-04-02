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
package org.beequeue.buzz;

import java.util.Map;

import org.beequeue.piles.LazyMap;
import org.beequeue.util.Nulls;

public class BuzzMime {
	private static final String TEXT_PLAIN = "text/plain";
	public static Map<String,String> extToMime = 
			new LazyMap<String, String>()
				.add("png","image/png")
				.add("ico","image/ico")
				.add("jpg","image/jpeg")
				.add("jpeg","image/jpeg")
				.add("ico","image/gif")
			    .add("svg","image/svg+xml")
			    .add("pdf","application/pdf")
			    .add("js","application/javascript")
			    .add("css","text/css")
				.add("txt",TEXT_PLAIN)
				.add("text",TEXT_PLAIN)
				.add("log",TEXT_PLAIN)
				.add("htm","text/html")
				.add("html","text/html");		
	
	public static String get(String extension) {
		String mime = extToMime.get(extension.toLowerCase());
		return Nulls.fallback(mime,TEXT_PLAIN);
	}


}
