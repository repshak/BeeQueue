package org.beequeue.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class BuzzClass {
	/**
	 * className - key to lookup ClassDefiniton
	 */
	public String className;
	/** only for enums */
	@JsonInclude  (Include.NON_NULL)
	public String[] enumValues = null;
	/** only for objects */
	@JsonInclude  (Include.NON_NULL)
	public BuzzAttribute[] objectAttributes = null;
}