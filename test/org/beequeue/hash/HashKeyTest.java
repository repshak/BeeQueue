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

import java.security.MessageDigest;
import java.util.Random;

import org.beequeue.hash.HashKey;
import org.beequeue.hash.HashKeyResource;
import org.beequeue.hash.MessageDigestUtils;
import org.beequeue.util.ToStringUtil;
import org.junit.Test;

public class HashKeyTest {

	@Test
	public void test() {
		Random r = new Random();
		byte[] bytes = new byte[100];
		r.nextBytes(bytes);
		MessageDigest md = MessageDigestUtils.md();
		md.update(bytes);
		HashKey sc = new HashKey(HashKeyResource.F, md.digest());
		System.out.println(sc);
		HashKey sc2 = HashKey.valueOf(sc.toString());
		assertEquals(sc.toString(), sc2.toString());
		assertEquals(sc, sc2);
		System.out.println(ToStringUtil.toString(sc2));
		HashKey sc3 = new HashKey(HashKeyResource.D, md.digest());
		bytes = new byte[200];
		r.nextBytes(bytes);
		MessageDigest md2 = MessageDigestUtils.md();
		md2.update(bytes);
		HashKey sc4 = new HashKey(HashKeyResource.F, md2.digest());
		assertFalse(sc2.equals(sc3));
		System.out.println(sc3);
		System.out.println(sc4);
		assertFalse(sc2.equals(sc4));
	}

}
