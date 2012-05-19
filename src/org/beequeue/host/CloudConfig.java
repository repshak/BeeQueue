package org.beequeue.host;


public class CloudConfig  {
	public String[] hosts;
	public String[] domains;
	
	/**
	 * Indicate if host belong to this cloud
	 * @param hostname
	 * @return <code>Booolean.TRUE</code> if belong, <code>Booolean.FALSE</code> if not. <code>null</code> - if undefined in case of default cloud 
	 */
	public Boolean doesHostBelong(String hostname){
		if(hosts==null)return null;
		for (int i = 0; i < hosts.length; i++) {
			if(hosts[i].equals(hostname)){
				return true;
			}
		}
		return false;
	}
}
