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
/*
 * Created on Sep 21, 2005 7:28:08 PM
 *
 */
package org.beequeue.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class BeeException extends RuntimeException {
	private static final long serialVersionUID = -1;
	
	/**
	 * @param message
	 */
	public BeeException() {
		super();
	}
	/**
	 * @param message
	 */
	public BeeException(String message) {
		super(message);
	}
	/**
	 * @param message
	 * @param cause
	 */
	public BeeException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * @param cause
	 */
	public BeeException(Throwable cause) {
		super(cause);
	}
	
	public static BeeException cast(Throwable e){
		if(e instanceof BeeException){
			return (BeeException) e;
		}
		return new BeeException(e);
	}
	
	private StringBuilder extraMemos = null;
	
	private Map<String, Object> memoValues = new LinkedHashMap<String, Object>();
	public BeeException memo( String memo, Object ... payload){
		if(this.extraMemos==null){
			this.extraMemos = new StringBuilder(Nulls.fallback(super.getMessage(),""));
		}	
		Object v = null;
		if(payload!=null && payload.length > 0){
			v = payload.length == 1 ? payload[0]: payload;
		}
		String location = Throwables.findPreviousStack(BeeException.class);
		memoValues.put(memo, v);
		extraMemos.append("\n");
		extraMemos.append(location);
		extraMemos.append(memo);
		if(v!=null){
			extraMemos.append(": ");
			extraMemos.append(ToStringUtil.toNotPrettyString(v));
		}
		return this;
	}
	
	
	
	public Map<String, Object> getMemoValues() {
		return Collections.unmodifiableMap(memoValues);
	}
	
	@Override
	public String getMessage() {
		if(this.extraMemos!=null ){
			return this.extraMemos.toString();
		}
		return super.getMessage();
	}
	
	public static void throwIfTrue(boolean condition, String... messages){
		if(condition) {
			throw new BeeException(Strings.join(" ", messages));
		}
	}
	
	public static <T> T throwIfNull(T notNull, String... messages){
		if(notNull == null) {
			BeeException e = messages.length > 0 ? 
				new BeeException(Strings.join(" ", messages)):  
				new BeeException("expected to be not null");
			throw e ;
		}
		return notNull;
	}

	public static <K,V> V throwIfValueNull(Map<K,V> map, K key){
		V notNull = map.get(key);
		if(notNull == null) {
			throw new BeeException("value for key "+key+" expected to be not null") ;
		}
		return notNull;
	}
}
