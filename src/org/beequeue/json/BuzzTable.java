package org.beequeue.json;

import java.util.ArrayList;
import java.util.List;

import org.beequeue.piles.MapList;
import org.beequeue.util.Morph;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class BuzzTable {
	public BuzzWebKey webKey;
	
	@JsonInclude (Include.NON_EMPTY)
	public MapList<String,BuzzColumn> columns = new MapList<String,BuzzColumn>(new Morph<BuzzColumn,String>(){
		@Override public String doIt(BuzzColumn input) {
			return input.name;
		}});
	
	@JsonInclude (Include.NON_EMPTY)
	public MapList<String,BuzzFilter> filters = new MapList<String,BuzzFilter>(new Morph<BuzzFilter,String>(){
		@Override public String doIt(BuzzFilter input) {
			return input.name;
		}});

	public int total;
	public int start;
	public int end;

	@JsonInclude (Include.NON_NULL)
	public List<Object> objectRows = null;
	
	@JsonInclude (Include.NON_NULL)
	public List<Object[]> arrayRows = null;
	
}
