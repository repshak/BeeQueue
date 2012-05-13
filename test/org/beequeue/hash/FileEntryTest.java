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

}
