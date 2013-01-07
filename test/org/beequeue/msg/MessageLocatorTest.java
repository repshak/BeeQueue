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
package org.beequeue.msg;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.beequeue.util.ToStringUtil;
import org.junit.Test;

public class MessageLocatorTest {

	@Test
	public void test() {
		doTest("AA:BBB:C\\\\C\\:CC");
		doTest("AA:BBB:CCCC");
		doTest("AA:*:CCCC");
		doTest("AA:**:CCCC");
		doTest("AA:*A:CCCC");
		doTest("AA:*\\::CCCC");
		doTest("AA:\\*:CCCC");
		doMatch("AA:*:CCCC","AA:*A:CCCC",true);
		doMatch("AA:A:CCCC","AA:*A:CCCC",false);
		doMatch("AA:\\*A:CCCC","AA:*A:CCCC",true);
		doMatch("AA:A:CCCC","AA:A:CCCC",true);
		
	}

	private void doMatch(String match, String input, boolean outcome) {
		MessageLocator matchPattern = new MessageLocator(match);
		MessageLocator inputLocator = new MessageLocator(input);
		assertEquals(matchPattern.match(inputLocator), outcome);
	}

	public void doTest(String locator) {
		MessageLocator messageLocator = new MessageLocator(locator);
		String string1 = messageLocator.toString();
		MessageLocator ml2 = MessageLocator.valueOf(string1);
		String string2 = ml2.toString();
		assertEquals(string1, string2);
		assertEquals(ml2,messageLocator);
		assertEquals(messageLocator.hashCode(), ml2.hashCode());
		out(messageLocator,locator);
	}

	public void out(MessageLocator ml, Object input) {
		Map<String,Object> so = new LinkedHashMap<String, Object>();
		so.put("input", input);
		so.put("name", ml.name);
		so.put("attributes", ml.attributes);
		so.put("locator", ml.locator);
//		ToStringUtil.out(so);
	}

}
