/** ==== BEGIN LICENSE =====
   Copyright 2012 - BeeQueue.org

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
 *  ===== END LICENSE ====== */
package org.beequeue.json;

import java.util.LinkedHashMap;
import java.util.Map;

import org.beequeue.piles.MapList;
import org.beequeue.util.BeeOperation;

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
	

	public MapList<String,BuzzClass> types = new MapList<String,BuzzClass>(new BeeOperation<BuzzClass,String>(){
		@Override public String doIt(BuzzClass input) {
			return input.className;
		}});

	@JsonInclude (Include.NON_EMPTY)
	public MapList<String,BuzzColumn> tableColumns = new MapList<String,BuzzColumn>(new BeeOperation<BuzzColumn,String>(){
		@Override public String doIt(BuzzColumn input) {
			return input.name;
		}});
}
