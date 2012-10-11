package org.beequeue.json;

import org.beequeue.piles.MapList;
import org.beequeue.util.Morph;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class BuzzSchema {
	@JsonInclude (Include.NON_NULL)
	public BuzzAttribute head;
	
	public MapList<String,BuzzClass> types = new MapList<String,BuzzClass>(new Morph<BuzzClass,String>(){
		@Override public String doIt(BuzzClass input) {
			return input.className;
		}});
}
