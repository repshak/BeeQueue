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
package org.beequeue.cache;

import static org.junit.Assert.*;

import org.beequeue.cache.ExponentialDecayWeight;
import org.junit.Test;

public class ExponentialDecayWeightTest {

	private static final long COST = 1000L;
	private static final long HL = 10000L;

	@Test
	public void test() {
		ExponentialDecayWeight entry = new ExponentialDecayWeight();
		entry.hit(HL, COST, 1000L);
		assertEquals( 950L, entry.adjustWeightToCurrentTime(2000L, HL));
		assertEquals( 475L, entry.adjustWeightToCurrentTime(12000L, HL));
	}

}
