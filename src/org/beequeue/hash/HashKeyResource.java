package org.beequeue.hash;

public enum HashKeyResource {
	F("File"), 
	D("Directory");
	
	public final String description;
	
	private HashKeyResource(String description){
		this.description = description;
	}
}