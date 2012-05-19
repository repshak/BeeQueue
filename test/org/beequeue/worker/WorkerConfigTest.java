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
package org.beequeue.worker;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

public class WorkerConfigTest {

	@Test
	public void test() {
		WorkerConfig workerConfig = new WorkerConfig();
		workerConfig.randomizationInterval = 5000L;
		for (int i = 0; i < 1000; i++) {
			long t = workerConfig.callRandomizationTerm();
			Assert.assertTrue(Math.abs(t)< 2501L);
		}
				
	}

}
