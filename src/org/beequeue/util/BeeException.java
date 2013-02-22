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



public class BeeException extends RuntimeException {
	private static final long serialVersionUID = -1;
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
	
	
	public BeeException memo( String memo, Object ... payload){
		if(this.extraMemos==null){
			this.extraMemos = new StringBuilder();
		}	
		extraMemos.append("\n");
		extraMemos.append(Throwables.findPreviousStack(BeeException.class));
		extraMemos.append(memo);
		if(payload!=null && payload.length > 0){
			extraMemos.append(": ");
			Object v = payload.length == 1 ? payload[0]: payload;
			extraMemos.append(ToStringUtil.toNotPrettyString(v));
		}
			
		return this;
	}
	
	@Override
	public String getMessage() {
		if(extraMemos!=null ){
			return super.getMessage() + extraMemos.toString();
		}
		return super.getMessage();
	}
	
	public static void makeSureItIsNot(boolean condition, String message){
		if(condition) {
			throw new BeeException(message);
		}
	}


}
