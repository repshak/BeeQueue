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
package org.beequeue.util;

public class Tuples {
  
  public static <I1,I2> Tuple<I1,I2> build(I1 i1,I2 i2){
    return new Tuple<I1, I2>(i1,i2); 
  }
  
  public static <I1,I2,I3> Triple<I1,I2,I3> build(I1 i1,I2 i2,I3 i3){
    return new Triple<I1, I2, I3>(i1,i2,i3); 
  }

  public static <I1,I2,I3,I4> Quadruple<I1,I2,I3,I4> build(I1 i1,I2 i2,I3 i3,I4 i4){
    return new Quadruple<I1, I2, I3,I4>(i1,i2,i3,i4); 
  }

}
