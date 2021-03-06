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

import java.io.Serializable;

/**
 * 4-Tuple
 * @see Tuple
 * 
 * @param <T1>
 * @param <T2>
 * @param <T3>
 * @param <T4>
 */
public class Quadruple<T1,T2,T3,T4>  implements Serializable{
  private static final long serialVersionUID = 1L;
	public final T1 o1;
	public T2 o2;
	public T3 o3;
	public T4 o4;

	@Override public String toString() { return ToStringUtil.toString(this); }

	public Quadruple(T1 o1, T2 o2, T3 o3, T4 o4) {
	  this.o1 = o1;
	  this.o2 = o2;
	  this.o3 = o3;
	  this.o4 = o4;
	}

	public Quadruple(T1 o1) {
		this.o1 = o1;
		this.o2 = null;
		this.o3 = null;
		this.o4 = null;
	}

}
