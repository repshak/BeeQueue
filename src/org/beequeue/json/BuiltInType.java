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
import java.util.Date;

import org.beequeue.util.BeeException;

import com.fasterxml.jackson.databind.JavaType;

public enum BuiltInType implements Comparator<Object> {
	BOOLEAN(Boolean.class,Boolean.TYPE),
	INTEGER(Long.class,Integer.TYPE,Integer.class,Long.TYPE,Short.TYPE,Short.class),
	FLOAT(Double.class,Float.TYPE,Float.class,Double.TYPE),
	STRING(String.class),
	DATE(Date.class),
	ENUM{ 
		@Override protected boolean matches(JavaType jt){ return jt.isEnumType(); }
	},
	OBJECT(){ 
		@Override protected boolean matches(JavaType jt){ return !jt.isContainerType(); }		
	},
	MAP{ 
		@Override protected boolean matches(JavaType jt){ return jt.isMapLikeType(); }		
	},
	ARRAY{ 
		@Override protected boolean matches(JavaType jt){ return jt.isContainerType(); }		
	};
	
	@SuppressWarnings("rawtypes")
	public final Class[] classes;
	
	@SuppressWarnings("rawtypes")
	private BuiltInType(Class... classes){
		this.classes = classes;
	}
	
	public boolean isPrimitive(){
		return classes != null && classes.length > 0 ;
	}
	
	@SuppressWarnings("unchecked")
	protected boolean matches(JavaType jt){
		for (int i = 0; i < this.classes.length; i++) {
			 if(this.classes[i].isAssignableFrom(jt.getRawClass()) ){
				 return true;
			 }
		}
		return false;
	}
	
	public static BuiltInType detect(JavaType jt){
		BuiltInType[] values = values();
		for (int i = 0; i < values.length; i++) {
			if( values[i].matches(jt)){
				return values[i];
			}
		}
		throw new BeeException("Cannot detect JavaType:"+jt);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int compare(Object a, Object b) {
		BeeException.throwIfTrue(this != ENUM && !isPrimitive(), "this != ENUM && !isPrimitive()");
		return ((Comparable)a).compareTo(b);
	}
	
	
	
}
