package org.beequeue.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class BuzzFilter {
	public String name;
	
	public enum Sorting { ASC, DESC };
	
	@JsonInclude (Include.NON_NULL)
	public Sorting sortingOrder = null;
	
	@JsonInclude (Include.NON_NULL)
	public String filterRule = null;
}
