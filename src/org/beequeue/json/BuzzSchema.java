package org.beequeue.json;

import org.beequeue.piles.MapList;
import org.beequeue.util.Morph;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class BuzzSchema {
	@JsonInclude (Include.NON_NULL)
	public BuzzAttribute key;
	
	@JsonInclude (Include.NON_NULL)
	public BuzzAttribute object;
	
	public MapList<String,BuzzClass> types = new MapList<String,BuzzClass>(new Morph<BuzzClass,String>(){
		@Override public String doIt(BuzzClass input) {
			return input.className;
		}});

	@JsonInclude (Include.NON_EMPTY)
	public MapList<String,BuzzColumn> tableColumns = new MapList<String,BuzzColumn>(new Morph<BuzzColumn,String>(){
		@Override public String doIt(BuzzColumn input) {
			return input.name;
		}});
}
