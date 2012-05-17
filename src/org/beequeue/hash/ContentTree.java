package org.beequeue.hash;

import com.fasterxml.jackson.annotation.JsonValue;

public class ContentTree {
	public String name;
	public HashKey hashKey;
	
	@Override @JsonValue
	public String toString() {
		return hashKey+","+name;
	}
	
	public ContentTree valueOf(String s){
		ContentTree ct = new ContentTree();
		ct.name = s.substring(42);
		ct.hashKey = HashKey.valueOf(s.substring(0,41));
		return ct;
	}

}
