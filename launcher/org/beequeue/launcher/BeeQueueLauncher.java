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
package org.beequeue.launcher;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

import winstone.Launcher;

public class BeeQueueLauncher {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("BQ_HOME:"+BeeQueueHome.instance.getHome());
		File webDir = BeeQueueHome.instance.getWeb();
		System.out.println("BQ_WEB:"+webDir);
		//TODO pars args and --> BeeQueueHome.instance.setPort(port);
		File jvmCsv = BeeQueueHome.instance.jvmCsv(BeeQueueHome.instance.getPort());
		BeeQueueJvmHelpeer jvmStatus = new BeeQueueJvmHelpeer(BeeQueueHome.instance.getPort());
		if( jvmStatus.starting != null ){
			BeeQueueHome.die(null, "It's odd. Some worker already starting. Kill all workers running and delete file:"+ jvmCsv);
		}
		boolean portAvailable = false;
		for (int i = 0; i < 6; i++) {
			jvmStatus.ensureMeInList(BeeQueueHome.instance.getPort(), BeeQueueJvmStatus.STARTING);
			jvmStatus.write(BeeQueueHome.instance.getPort());
			if(portAvailable()){
				portAvailable = true;
				break;
			}
			Thread.sleep(15000L);
		}
		if(!portAvailable){
			portAvailable = killAllWorkersExceptItself();
		}
		if(!portAvailable){
			BeeQueueHome.die(null, "Gave up to get port. Kill process mannualy, also you may inspect file:"+ jvmCsv);
		}
		String newArgs[] = { "--webroot="+webDir, "--httpPort="+BeeQueueHome.instance.getPort() , "--ajp13Port=-1" };
		Launcher.main(newArgs);
	}
	
	private static boolean killAllWorkersExceptItself() {
		// TODO add smart killing logic
		//return portAvailable();
		return false;
		
	}

	public static boolean portAvailable() {
	    ServerSocket ss = null;
	    try {
	        ss = new ServerSocket(BeeQueueHome.instance.getPort());
	        ss.setReuseAddress(true);
	        return true;
	    } catch (IOException e) {
	    } finally {
	        if (ss != null) {
	            try {
	                ss.close();
	            } catch (IOException e) {
	                /* should not be thrown */
	            }
	        }
	    }
	    return false;
	}
}
