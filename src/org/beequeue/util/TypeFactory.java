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
package org.beequeue.util;

import com.fasterxml.jackson.core.type.TypeReference;

public class TypeFactory<T> {
	public final Class<T> type ; 
	public final TypeReference<T> ref ; 
	public final BeeOperation<T,String> op_OBJ_TO_PRETTY = new BeeOperation<T, String>() {
		@Override
		public String execute(T input) {
			return ToStringUtil.toString(input);
		}
	};
	public final BeeOperation<T,String> op_OBJ_TO_COMPACT = new BeeOperation<T, String>() {
		@Override
		public String execute(T input) {
			return ToStringUtil.toNotPrettyString(input);
		}
	};
	public final BeeOperation<String,T> op_STRING_TO_OBJ = new BeeOperation<String, T>() {
		@Override
		public T execute(String input) {
			if( ref !=null ){
				return ToStringUtil.toObject(input,ref);
			}else if( type !=null ){
				return ToStringUtil.toObject(input,type);
			}
			throw new BeeException("Should never happend");
		}
	};
	public static<T> TypeFactory<T> tf(TypeReference<T> ref){
		return new TypeFactory<T>(ref);
	}
	public static<T> TypeFactory<T> tf(Class<T> ref){
		return new TypeFactory<T>(ref);
	}
	public TypeFactory(TypeReference<T> ref){
		BeeException.throwIfTrue(ref == null, "ref == null");
		this.type = null;
		this.ref = ref;
	}
	
	public TypeFactory(Class<T> type){
		BeeException.throwIfTrue(type == null, "type == null");
		this.type = type;
		this.ref = null;
	}
}
