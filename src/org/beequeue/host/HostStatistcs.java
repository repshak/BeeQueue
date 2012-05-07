package org.beequeue.host;

import org.beequeue.util.Filter;

public class HostStatistcs {
	public CapacityRatio cpu;
	public CapacityRatio memory;
	
	public static final Filter<HostStatistcs> HEALTHY_HOST_CHECK =new Filter<HostStatistcs>(){
		@Override
		public Boolean doIt(HostStatistcs stat) {
			return stat.cpu.ratio() < 0.9 && stat.memory.ratio() < .95;
		}
		
	} ;
	
	
}
