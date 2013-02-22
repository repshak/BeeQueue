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
			}else if( ref !=null ){
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
		BeeException.makeSureItIsNot(ref == null, "ref == null");
		this.type = null;
		this.ref = ref;
	}
	
	public TypeFactory(Class<T> type){
		BeeException.makeSureItIsNot(type == null, "type == null");
		this.type = type;
		this.ref = null;
	}
}