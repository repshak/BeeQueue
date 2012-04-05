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
/**
 * chain of transformations   
 * 
 * @param <T> input and target types
 */
public class TransformationChain<T> implements Transformation<T> {
  private Transformation<T>[] transitionChain ;

  public TransformationChain(Transformation<T> ... chain) {
    super();
    transitionChain = chain;
  }

  public T doIt(T data) {
    for (int i = 0; i < transitionChain.length; i++) {
      data = transitionChain[i].doIt(data);
    }
    return data;
  }
}