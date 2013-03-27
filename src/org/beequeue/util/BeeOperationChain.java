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

public class BeeOperationChain<A,B,C> 
implements BeeOperation<A, C> {
	public final BeeOperation<A,B> step1;
	public final BeeOperation<? super B,C> step2;
	
	public BeeOperationChain(BeeOperation<A, B> step1, BeeOperation<? super B, C> step2) {
		this.step1 = step1;
		this.step2 = step2;
	}

	public<D> BeeOperationChain<A, C, D> chain (BeeOperation<? super C, D> nextOp) {
		return new BeeOperationChain<A, C, D>(this, nextOp);
	} 

	public static <A,B,C>  BeeOperationChain<A, B, C> chain (BeeOperation<A, B> step1, BeeOperation<? super B, C> step2) {
		return new BeeOperationChain<A ,B, C>(step1, step2);
	} 
	
	@Override
	public C execute(A input) {
		B result1 = step1.execute(input);
		C result2 = step2.execute(result1);
		return result2;
	}
}
