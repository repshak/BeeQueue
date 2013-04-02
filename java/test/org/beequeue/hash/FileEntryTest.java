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

import org.beequeue.hash.FileEntry;
import org.beequeue.hash.HashKey;
import org.beequeue.hash.HashKeyResource;
import org.beequeue.hash.MessageDigestUtils;
import org.junit.Test;

public class FileEntryTest {

	@Test
	public void test() {
		MessageDigest md = MessageDigestUtils.md();
		md.update((byte)10);
		FileEntry fe = new FileEntry( new HashKey(HashKeyResource.F, md.digest()), true, "right/there.txt");
		String f = fe.toString();
		FileEntry f2 = FileEntry.valueOf(f);
		assertEquals(f, f2.toString());
		System.out.println(f);
		System.out.println(f2.toString());
	}
	@Test
	public void test512() {
		MessageDigest md = MessageDigestUtils.md512();
		md.update((byte)10);
		FileEntry fe = new FileEntry( new HashKey(HashKeyResource.F, md.digest()), true, "right/there.txt");
		String f = fe.toString();
		FileEntry f2 = FileEntry.valueOf(f);
		assertEquals(f, f2.toString());
		System.out.println(f);
		System.out.println(f2.toString());
	}

}
