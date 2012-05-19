package org.beequeue.host;


public class CloudConfig  {
	public String[] hosts;
	public String[] domains;
	
	/**
	 * Indicate if host belong to this cloud
	 * @param hostname
	 * @return <code>Booolean.TRUE</code> if belong, <br />
	 * 		   <code>Booolean.FALSE</code> if not. <br />
	 *         <code>null</code> - if undefined. Only makes sense 
	 *         in case of default cloud 
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
