package org.beequeue.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PercentEncodingTest {

	private static final String P1 =       "Abc/dlkdd?dldl=zz z";
	private static final String ENCODED1 = "Abc%2Fdlkdd%3Fdldl%3Dzz+z";
	private static final String P2 =       "Abc/dlk\ndd\t?d\rldl=zz+z%";
	private static final String ENCODED2 = "Abc%2Fdlk%0Add%09%3Fd%0Dldl%3Dzz%2Bz%25";
	//UTF-8
	private static final String P3 =       "У попа";
	private static final String P4 =       "\u0423 \u043F\u043E\u043F\u0430";
	private static final String ENCODED3 = "%D0%A3+%D0%BF%D0%BE%D0%BF%D0%B0";

	@Test
	public void test() {
		scenario1(P1, ENCODED1);
		assertEquals(PercentEncoding.decode(P1), P1);
		scenario1(P2, ENCODED2);
		scenario1(P3, ENCODED3);
		scenario1(P4, ENCODED3);
	}

	public void scenario1(String p, String encoded) {
		String encodeToTest = PercentEncoding.encode(p);
		System.out.println(encodeToTest);
		assertEquals(encodeToTest, encoded);
		assertEquals(PercentEncoding.decode(encodeToTest), p);
	}

}
