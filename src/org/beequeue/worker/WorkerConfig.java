package org.beequeue.worker;

import java.util.Random;

public class WorkerConfig {
	public static final Random  RND = new Random() ;
	public long minFireInterval = 15000L;
	public long maxFireInterval = 60000L;
	public long randomizationInterval = 5000L;
	public long maxProcessMonitoringInterval = 600000L;
	public long callRandomizationTerm (){
		return Math.abs(RND.nextLong()) % randomizationInterval - randomizationInterval/2;
	}
}
