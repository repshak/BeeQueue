package org.beequeue.json;

import java.util.LinkedHashMap;
import java.util.Map;

import org.beequeue.piles.MapList;
import org.beequeue.util.Morph;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class BuzzSchema {
	
	@JsonInclude (Include.NON_NULL)
	public BuzzAttribute key;
	
	public static enum SortOrder {
		ASCENDING,
		DESCENDING
	};
	public static class KeySorting {
		public Map<String,SortOrder> keys = new LinkedHashMap<String, BuzzSchema.SortOrder>();
	}
	@JsonInclude (Include.NON_NULL)
	public KeySorting keySorting;

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
