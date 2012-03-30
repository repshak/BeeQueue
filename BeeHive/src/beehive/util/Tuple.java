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
package beehive.util;

import java.io.Serializable;

/**
 * Tuple
 * <p>
 * From Wikipedia, the free encyclopedia
 * In mathematics, a tuple is a finite sequence (also known as an "ordered list") 
 * of objects, each of a specified type. A tuple containing n objects is known 
 * as an "n-tuple". For example the 4-tuple (or "quadruple") 
 * [ Mozart, 27, January, 1756], with components of 
 * respective types PERSON, INTEGER, MONTH and INTEGER, 
 * could be used to record that a certain person was born on a 
 * certain day of a certain month of a certain year.
 * <p>
* 
 * @see Triple 
 * @see Quadruple
 *  
 * @param <T1>
 * @param <T2>
 */
public class Tuple<T1,T2> extends ToStringUtil implements Serializable{
  private static final long serialVersionUID = 1L;
  public final T1 o1;
	public T2 o2;
	
	public Tuple(T1 t1) {
	  this.o1 = t1;
	  this.o2 = null;
	}
	public Tuple(T1 t1, T2 t2) {
		this.o1 = t1;
		this.o2 = t2;
	}

}
