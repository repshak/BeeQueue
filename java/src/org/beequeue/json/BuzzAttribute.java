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

import java.util.Comparator;

import org.beequeue.util.BeeOperation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class BuzzAttribute implements Comparator<Object>{
	public static BeeOperation<? super BuzzAttribute, ? extends String> op_GET_NAME = new BeeOperation<BuzzAttribute,String>(){
		@Override public String execute(BuzzAttribute input) {
			return input.name;
		}};

	@JsonInclude  (Include.NON_NULL)
	public String name = null;
	public BuiltInType type;
	
	/** populated only for collections  */ 
	@JsonInclude  (Include.NON_NULL)
	public BuiltInType contentType=null;
	
	/** populated only for key attributes where sort order is relevant */ 
	@JsonInclude  (Include.NON_NULL)
	public SortOrder sortOrder=null;
	
	/** populated for non primitive types only */ 
	@JsonInclude  (Include.NON_NULL)
	public String className = null;
	
	/** populated string for non primitive types only */ 
	@JsonInclude  (Include.NON_NULL)
	public String keyClassName = null;
	
	public void copyTypeId(BuzzAttribute that) {
		this.type = that.type;
		this.contentType = that.contentType;
		this.className = that.className;
		this.keyClassName = that.keyClassName;
	}

	public void copyTypeIdAsContent(BuzzAttribute that) {
		this.contentType = that.type;
		this.className = that.className;
	}

	public int compare(Object a, Object b) {
		return SortOrder.DESCENDING == sortOrder ? type.compare(b, a) : type.compare(a, b);
	}

	public Object coerce(Object v) {
		if(v != null && !type.isPrimitive() && hasTypeFactory()){
			
		}
		return type.coerce(v);
	}
	
	private boolean hasTypeFactory() {
		
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * sortcut creator method
	 * @param name
	 * @param type
	 * @param sort
	 * @return
	 */
	public static BuzzAttribute newAttr( String name, BuiltInType type, SortOrder sort){
		BuzzAttribute a = new BuzzAttribute();
		a.name = name;
		a.type = type;
		a.sortOrder = sort;
		return a;
	}
	
}
