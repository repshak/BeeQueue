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
package org.beequeue.hash;

import static org.junit.Assert.*;

import org.beequeue.hash.MessageDigestUtils;
import org.junit.Test;

public class MessageDigestUtilsTest {

	@Test
	public void test() {
		byte[] b = MessageDigestUtils.fromHexString("01A0");
		assertEquals("01A0", MessageDigestUtils.toHexString(b));
		assertEquals(b[0],1);
		assertEquals(b[1],-96);
		assertEquals(MessageDigestUtils.fromHexChar('0'), 0);
		assertEquals(MessageDigestUtils.fromHexChar('1'), 1);
		assertEquals(MessageDigestUtils.fromHexChar('2'), 2);
		assertEquals(MessageDigestUtils.fromHexChar('3'), 3);
		assertEquals(MessageDigestUtils.fromHexChar('4'), 4);
		assertEquals(MessageDigestUtils.fromHexChar('5'), 5);
		assertEquals(MessageDigestUtils.fromHexChar('6'), 6);
		assertEquals(MessageDigestUtils.fromHexChar('7'), 7);
		assertEquals(MessageDigestUtils.fromHexChar('8'), 8);
		assertEquals(MessageDigestUtils.fromHexChar('9'), 9);
		assertEquals(MessageDigestUtils.fromHexChar('A'), 10);
		assertEquals(MessageDigestUtils.fromHexChar('B'), 11);
		assertEquals(MessageDigestUtils.fromHexChar('C'), 12);
		assertEquals(MessageDigestUtils.fromHexChar('D'), 13);
		assertEquals(MessageDigestUtils.fromHexChar('E'), 14);
		assertEquals(MessageDigestUtils.fromHexChar('F'), 15);
		assertEquals(MessageDigestUtils.toHexChar(0), '0');
		assertEquals(MessageDigestUtils.toHexChar(1), '1');
		assertEquals(MessageDigestUtils.toHexChar(2), '2');
		assertEquals(MessageDigestUtils.toHexChar(9), '9');
		assertEquals(MessageDigestUtils.toHexChar(10), 'A');
		assertEquals(MessageDigestUtils.toHexChar(11), 'B');
		assertEquals(MessageDigestUtils.toHexChar(13), 'D');
		assertEquals(MessageDigestUtils.toHexChar(14), 'E');
		assertEquals(MessageDigestUtils.toHexChar(15), 'F');
	}

}
