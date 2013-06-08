package org.beequeue.buzz;

import static org.junit.Assert.*;

import org.junit.Test;

public class BuzzCriteriaTest {

	@Test
	public void test() {
		checkBuzzCriteria("branch/sec/ti/on//fi/le/pa/th");
		
		//dbcriteria
		checkBuzzCriteria("branch/sec/ti/ondb//index/a/b/b");
		//this shortcut for to:
		checkBuzzCriteria("branch/sec/ti/ondb//index?eq=a/b/c");

		checkBuzzCriteria("branch/sec/ti/ondb//index?gte=a1/b1/c1&lt=a2/b2/c3");
	}

	public void checkBuzzCriteria(String s) {
		assertEquals(new BuzzCriteria(s).toString(),s);
	}

}
