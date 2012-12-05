package org.beequeue.cache;

import static org.junit.Assert.*;

import org.beequeue.cache.ExponentialDecayWeight;
import org.junit.Test;

public class ExponentialDecayWeightTest {

	private static final long COST = 1000L;
	private static final long HL = 10000L;

	@Test
	public void test() {
		ExponentialDecayWeight entry = new ExponentialDecayWeight();
		entry.hit(HL, COST, 1000L);
		assertEquals( 950L, entry.adjustWeightToCurrentTime(2000L, HL));
		assertEquals( 475L, entry.adjustWeightToCurrentTime(12000L, HL));
	}

}
