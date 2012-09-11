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
