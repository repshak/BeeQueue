package org.beequeue.msg;

import java.util.LinkedHashMap;

public class BeeQueueMessage extends LinkedHashMap<String, String> {
	private static final long serialVersionUID = 1L;

	public BeeQueueMessage set(String k, String v){
		this.put(k, v);
		return this;
	}
	

}
