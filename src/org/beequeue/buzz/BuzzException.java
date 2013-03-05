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
package org.beequeue.buzz;

import org.beequeue.util.BeeException;

public class BuzzException extends BeeException {
	private static final long serialVersionUID = 1L;
	public final int statusCode;

	public BuzzException(int statusCode, String message) {
		super(statusMessage(statusCode) + "\n" + message);
		this.statusCode = statusCode;
	}
	private static String statusMessage(int statusCode){
		return String.format("StatusCode: %d" , statusCode);
	}
	public BuzzException(int statusCode, Throwable cause) {
		super(statusMessage(statusCode), cause);
		this.statusCode = statusCode;
	}
	
	public BuzzException(int statusCode, String message, Throwable cause) {
		super(statusMessage(statusCode) + "\n" + message, cause);
		this.statusCode = statusCode;
	}

	
}
