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

import java.util.ArrayList;
import java.util.List;

import org.beequeue.piles.MapList;
import org.beequeue.util.BeeOperation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class BuzzTable {
	public BuzzWebKey webKey;
	
	@JsonInclude (Include.NON_EMPTY)
	public MapList<String,BuzzColumn> columns = new MapList<String,BuzzColumn>(new BeeOperation<BuzzColumn,String>(){
		@Override public String execute(BuzzColumn input) {
			return input.name;
		}});
	
	@JsonInclude (Include.NON_EMPTY)
	public MapList<String,BuzzFilter> filters = new MapList<String,BuzzFilter>(new BeeOperation<BuzzFilter,String>(){
		@Override public String execute(BuzzFilter input) {
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
