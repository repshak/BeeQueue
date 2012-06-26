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
