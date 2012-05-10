package org.beequeue.shastore;

import static org.junit.Assert.*;

import java.security.MessageDigest;

import org.beequeue.shastore.ShaCode.Resource;
import org.junit.Test;

public class FileEntryTest {

	@Test
	public void test() {
		MessageDigest md = MessageDigestUtils.md();
		md.update((byte)10);
		FileEntry fe = new FileEntry( new ShaCode(Resource.F, md.digest()), true, "right/there.txt");
		FileEntry f2 = FileEntry.valueOf(fe.toString());
		assertEquals(fe.toString(), f2.toString());
		System.out.println(fe.toString());
		System.out.println(f2.toString());
	}

}
