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
package org.beequeue.template;

import static org.junit.Assert.*;

import org.beequeue.msg.BeeQueueEvent;
import org.junit.Test;

public class MessageFilterTest {

	@Test
	public void test() {
		assertEquals(true, new EventFilter("true").evalFilter(null));
		assertEquals(false, new EventFilter("false").evalFilter(null));
	    
		BeeQueueEvent bqm = new BeeQueueEvent();
		bqm.parameters.put("val", "5");
	    
		assertEquals(true, new EventFilter("msg.parameters.val.toInteger() > 3").evalFilter(bqm));
	    assertEquals(false, new EventFilter("Integer.valueOf(msg.parameters.val)/2 > 6").evalFilter(bqm));
	    assertEquals(false, new EventFilter("int i = msg.parameters.val.toInteger();i/2 > 6").evalFilter(bqm));
	    assertEquals(false, new EventFilter("msg.parameters.val > '6'").evalFilter(bqm));
	    assertEquals(true, new EventFilter("msg.parameters.val == '5'").evalFilter(bqm));
	    assertEquals(true, new EventFilter("msg.parameters.val != '3'").evalFilter(bqm));
	
	}

}
