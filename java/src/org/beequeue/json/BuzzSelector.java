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

import org.beequeue.util.BeeException;
import org.beequeue.util.Strings;

import com.fasterxml.jackson.annotation.JsonValue;
/**
 * TODO: rethink json xpath-like selector
 * Idea to have some kind selector(like xpath in xml), I did not found nice way 
 * to implement it so I deprecate it for now until I figure something better.
 */
@Deprecated
public class BuzzSelector {
	
	private String[] elements;
	private BuiltInType[] types;
	public final boolean direct;
	
	public BuzzSelector(String selector) {
		String[] split = selector.split("\\.");
		if(split.length == 0  ){
			throw new BeeException("selector cannot be empty:"+ selector);
		}
		this.types = new BuiltInType[split.length];
		boolean direct = true ;
		for (int i = 0; i < split.length; i++) {
			String s = split[i];
			this.types[i] = null ;
			if (s.endsWith("{}")) {
				this.types[i] = BuiltInType.MAP;
				split[i] = s = s.substring(0,s.length()-2);
				direct = false;
			} else if (elements[i].endsWith("[]")) {
				this.types[i] = BuiltInType.ARRAY;
				split[i] = s = s.substring(0,s.length()-2);
				direct = false;
			} 
			if( Strings.isEmpty(s) ){
				throw new BeeException("selector cannot be empty:"+ selector);
			}
		}
		this.direct = direct;
		this.elements = split;
	}

	public String elementAt(int i){
		return elements[i];
	}

	public BuiltInType elementType(int i){
		return types[i];
	}

	
	@JsonValue
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < elements.length; i++) {
			if(i > 0){
				sb.append('.');
			}
			sb.append(elements[i]);
			BuiltInType builtInType = types[i];
			if(builtInType!=null){
				sb.append(builtInType == BuiltInType.MAP ? "{}" : "[]");
			}
		}
		return sb.toString();
	}
}
