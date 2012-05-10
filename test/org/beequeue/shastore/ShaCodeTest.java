package org.beequeue.shastore;

import static org.junit.Assert.*;

import java.security.MessageDigest;
import java.util.Random;

import org.beequeue.shastore.ShaCode.Resource;
import org.beequeue.util.ToStringUtil;
import org.junit.Test;

public class ShaCodeTest {

	@Test
	public void test() {
		Random r = new Random();
		byte[] bytes = new byte[100];
		r.nextBytes(bytes);
		MessageDigest md = MessageDigestUtils.md();
		md.update(bytes);
		ShaCode sc = new ShaCode(Resource.F, md.digest());
		System.out.println(sc);
		ShaCode sc2 = ShaCode.valueOf(sc.toString());
		assertEquals(sc.toString(), sc2.toString());
		assertEquals(sc, sc2);
		System.out.println(ToStringUtil.toString(sc2));
		ShaCode sc3 = new ShaCode(Resource.D, md.digest());
		bytes = new byte[200];
		r.nextBytes(bytes);
		MessageDigest md2 = MessageDigestUtils.md();
		md2.update(bytes);
		ShaCode sc4 = new ShaCode(Resource.F, md2.digest());
		assertFalse(sc2.equals(sc3));
		System.out.println(sc3);
		System.out.println(sc4);
		assertFalse(sc2.equals(sc4));
		
	}

}
