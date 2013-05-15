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

import java.text.DateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import org.beequeue.util.BeeException;
import org.beequeue.util.Nulls;
import org.beequeue.util.ToStringUtil;
import org.beequeue.util.TypeFactory;

import com.fasterxml.jackson.databind.JavaType;

public enum BuiltInType implements Comparator<Object> {
	BOOLEAN(Boolean.class,Boolean.TYPE){
		@Override
		protected Object coerseIt(Object v) {
			return super.coerseIt(v);
		}
		@Override
		public Object fromString(String s) {
			return "yYtT1".indexOf(s.charAt(0)) >= 0;
		}
	},
	INTEGER(Long.class,Integer.TYPE,Integer.class,Long.TYPE,Short.TYPE,Short.class){
		@Override
		protected Object coerseIt(Object v) {
			if (v instanceof Number) {
				return ((Number) v).longValue();
			}
			return super.coerseIt(v);
		}
		
		@Override
		public Object fromString(String s) {
			return Long.parseLong(s);
		}

	},
	FLOAT(Double.class,Float.TYPE,Float.class,Double.TYPE){
		@Override protected Object coerseIt(Object v) {
			if (v instanceof Number) {
				return ((Number) v).doubleValue();
			}
			return super.coerseIt(v);
		}
		@Override public Object fromString(String s) {
			return Double.parseDouble(s);
		}
	},
	STRING(String.class){
		@Override protected Object coerseIt(Object v) {
			return v.toString();
		}
		@Override public Object fromString(String s) {
			return s;
		}
	},
	DATE(Date.class){
		@Override protected Object coerseIt(Object v) {
			if (v instanceof Number) {
				return new Date(((Number) v).longValue());
			}
			return super.coerseIt(v);
		}
		@Override public Object fromString(String s) {
			DateFormat df = (DateFormat) ToStringUtil.MAPPER.getSerializationConfig().getDateFormat().clone();
			try {
				return df.parse(s);
			} catch (Exception e) {
				throw BeeException.cast(e);
			}
		}
	},
	LINK(BuzzLink.class){
		TypeFactory<BuzzLink> TF = new TypeFactory<BuzzLink>(BuzzLink.class);
		@SuppressWarnings("unchecked")
		@Override protected Object coerseIt(Object v) {
			if (v instanceof Map ) {
				return BuzzLink.buildFromMap((Map<String,String>)v);
			}
			return super.coerseIt(v);
		}
		@Override public Object fromString(String s) {
			try {
				return TF.op_STRING_TO_OBJ.execute(s);
			} catch (Exception e) {
				throw BeeException.cast(e);
			}
		}
	},
	ENUM(String.class){
		@Override protected Object coerseIt(Object v) {
			return v.toString();
		}
		@Override public Object fromString(String s) {
			return s;
		}
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
	
	public Class<?> getBoxClass(){
		return isPrimitive() ? classes[0] : null ;
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
		return Nulls.compare((Comparable)a, (Comparable)b, true);
	}
	
	public Object fromString(String s){
		return ToStringUtil.toObject(s, Object.class);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object coerce(Object v) {
		Object r;
		if(v == null){
			r = null;
		}else if(!isPrimitive()){
			r = v ;
		}else{
			Class boxClass = getBoxClass();
			boolean alreadyAssignable = boxClass.isAssignableFrom(v.getClass());
			if( alreadyAssignable ){
				r = v;
			}else if(v instanceof String){
				r = fromString((String)v);
			}else{
				r = coerseIt(v);
			}
		}
		return r;
	}

	/**
	 * 
	 * @param v assumed that v is not null and v is not string
	 * @return
	 */
	protected Object coerseIt(Object v) {
		throw new BeeException("Dont know how to coerce")
		.memo("from", v.getClass().getName())
		.memo("to", getBoxClass() )
		.memo("v", v);
	}
	
}