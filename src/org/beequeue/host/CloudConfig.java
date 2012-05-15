package org.beequeue.host;


public class CloudConfig  {
	public String[] hosts;

	public boolean hasHost(String hostname){
		for (int i = 0; i < hosts.length; i++) {
			if(hosts[i].equals(hostname)){
				return true;
			}
		}
		return false;
	}
}
