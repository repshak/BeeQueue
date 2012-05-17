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

import java.util.ArrayList;
import java.util.List;


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
	
	public List<Object> payload = new ArrayList<Object>();
	public BeeException withPayload( Object ... payload){
		if(payload!=null){
			for (int i = 0; i < payload.length; i++) {
				this.payload.add(payload[i]);
			}
		}
		return this;
	}
	@Override
	public String getMessage() {
		if(payload.size() > 0 ){
			return super.getMessage() + "\n" +
					"payload:" +  ToStringUtil.toString(payload);
		}
		return super.getMessage();
	}
	
	
}