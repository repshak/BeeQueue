/** ==== BEGIN LICENSE =====
   Copyright 2004-2007 - Wakeup ORM

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
package org.beequeue.sql;

/**
 * projectory.kernel.dal.DalException
 */
public class DalException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4673830527692626847L;
	/**
	 * @param message
	 */
	public DalException(String message) {
		super(message);
	}
	/**
	 * @param message
	 * @param cause
	 */
	public DalException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * @param cause
	 */
	public DalException(Throwable cause) {
		super(cause);
	}
}
