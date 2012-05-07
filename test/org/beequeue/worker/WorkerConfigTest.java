package org.beequeue.worker;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

public class WorkerConfigTest {

	@Test
	public void test() {
		WorkerConfig workerConfig = new WorkerConfig();
		workerConfig.randomizationInterval = 5000L;
		for (int i = 0; i < 1000; i++) {
			long t = workerConfig.callRandomizationTerm();
			Assert.assertTrue(Math.abs(t)< 2501L);
		}
				
	}

}
