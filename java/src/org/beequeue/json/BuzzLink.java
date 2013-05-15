package org.beequeue.json;

import java.util.Map;

import org.beequeue.util.BeeException;

public class BuzzLink {
	public String title;
	public String xref;
	
	public static BuzzLink buildFromMap(Map<String,String> map){
		BuzzLink buzzLink = new BuzzLink();
		buzzLink.title = BeeException.throwIfValueNull(map,"title");
		buzzLink.xref = BeeException.throwIfValueNull(map, "xref");
		return buzzLink;
	}

}
