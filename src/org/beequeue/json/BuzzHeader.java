package org.beequeue.json;

import org.beequeue.piles.MapList;
import org.beequeue.util.TypeFactory;

public class BuzzHeader extends MapList<String, BuzzAttribute> {
	private static final long serialVersionUID = 1L;
	public static TypeFactory<BuzzHeader> TF = new TypeFactory<BuzzHeader>(BuzzHeader.class) ;
	
	public BuzzHeader() {
		super(BuzzAttribute.op_GET_NAME);
	}
}
