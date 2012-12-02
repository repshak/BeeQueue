package org.beequeue.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class BuzzColumn {
	public String name;
	/** only primitive types */ 
	public BuiltInType type;
	@JsonInclude (Include.NON_NULL)
	public String displayName = null;
	@JsonInclude (Include.NON_NULL)
	public BuzzSelector selector = null;

}
