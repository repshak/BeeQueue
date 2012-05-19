/** ==== BEGIN LICENSE =====
   Copyright 2012 - BeeQueue.org

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
 *  ===== END LICENSE ====== */
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
