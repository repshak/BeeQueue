package org.beequeue.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class BuzzAttribute {
	@JsonInclude  (Include.NON_NULL)
	public String name = null;
	public BuiltInType type;
	/** populated only for collections  */ @JsonInclude  (Include.NON_NULL)
	public BuiltInType contentType=null;
	
	/** populated for non primitive types only */ @JsonInclude  (Include.NON_NULL)
	public String className = null;
	
	public void copyTypeId(BuzzAttribute that) {
		this.type = that.type;
		this.contentType = that.contentType;
		this.className = that.className;
		
	}
	public void copyTypeIdAsContent(BuzzAttribute that) {
		this.contentType = that.type;
		this.className = that.className;
		
	}
}