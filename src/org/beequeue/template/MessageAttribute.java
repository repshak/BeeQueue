package org.beequeue.template;

import org.beequeue.util.DataType;

public class MessageAttribute {
	public String name;
	public AttributeType attrType;
	public DataType<?> dataType;

	public MessageAttribute() {
	}

	public MessageAttribute(String name, AttributeType attrType,
			DataType<?> dataType) {
		super();
		this.name = name;
		this.attrType = attrType;
		this.dataType = dataType;
	}

	
}
