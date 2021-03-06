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

import java.util.List;
import java.util.Map;

import org.beequeue.piles.MapList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class BuzzSchema {
	@JsonInclude (Include.NON_NULL)
	public BuzzAttribute key;
	
	@JsonInclude (Include.NON_NULL)
	public BuzzAttribute object;
	
	private MapList<String,BuzzClass> types = new MapList<String,BuzzClass>(BuzzClass.op_GET_NAME);
	public List<BuzzClass> getTypes() {
		return types;
	}
	
	public void setTypes(List<BuzzClass> types) {
		this.types.clear();
		this.types.addAll(types);
	}

	@JsonIgnore
	public Map<String, BuzzClass> getTypesMap() {
		return types.map();
	}
	
}
