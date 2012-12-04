package org.beequeue.piles.lifecycle;

import static org.junit.Assert.*;

import org.junit.Test;

public class LifeCycleEntryTest {

	private static final long COST = 1000L;
	private static final long HL = 10000L;

	@Test
	public void test() {
		LifeCycleEntry<Object, Object> entry = new LifeCycleEntry<Object, Object>(new Object(), new Object());
		entry.onGet(HL, COST, 1000L);
		assertEquals( 950L, entry.adjustWeightToTime(2000L, HL));
		assertEquals( 475L, entry.adjustWeightToTime(12000L, HL));
	}

}
