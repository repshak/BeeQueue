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
import java.net.ServerSocket;
import java.util.List;
import java.util.regex.Matcher;

import org.beequeue.launcher.BeeQueueCommandLineInterface.Entry;


public class BeeQueueLauncher {
	 
	private static boolean runScheduler = true ;

	public static void main(String[] args) throws IOException, InterruptedException {
		BeeQueueHome bqh = BeeQueueHome.instance;
		
		BeeQueueCommandLineInterface cli = new BeeQueueCommandLineInterface(
			new Entry(
						"-runServer=(\\d+)",
						"run server on specific port. By default application it always run server using port 7532.",
						"In both default case and when -runServer specified. One more argument <cloud name> required."
			){
				@Override void extract(Matcher m, List<String> rest, BeeQueueCommandLineInterface cli) {
					BeeQueueHome.instance.getBuzz().setPort(Integer.parseInt(m.group(1)));
					if(rest.size()!=1){
						System.err.println("ERROR: runServer take one additional argument <cloud name>");
						cli.printHelp = true;
					}
				}
			},
			new Entry(
					"-command=(\\w+)",
					"Run command like: -command=<cmdName> [<arg0> <arg1>] " ,
					"" ,
					"For list of commands and its' arguments see:" ,
					"https://github.com/repshak/BeeQueue/tree/master/src/org/beequeue/command" 
		){
			@Override void extract(Matcher m, List<String> rest, BeeQueueCommandLineInterface cli) {
				runScheduler = false;
				BeeQueueCommand.runMain(m.group(1), rest);
			}
		}
		)
		.description(
				"",
				"  BeeQueue - event queue and workflow execution engine",
				"  more info: https://github.com/repshak/BeeQueue",
				"",
				""
				);
		
		List<String> extraArgs = cli.process(args);
		//run default action
		if(runScheduler){
			if(extraArgs.size()!=1){
				//cloud
				System.err.println("ERROR: you nee to provide only cloud name instead:"+extraArgs);
				System.err.println();
				cli.printHelp();
			}else{
				bqh.setCloudName(extraArgs.get(0));
				System.out.println("BQ_HOME:"+bqh.getHome());
				runScheduler();
			}
		}
	}

	
	public static void runScheduler() throws InterruptedException,
			IOException {
		makeSureThatPortAvailable();
		BeeQueueHome.instance.runServer();
	}

	public static void makeSureThatPortAvailable() throws InterruptedException {
		File jvmCsv = BeeQueueHome.instance.jvmCsv(BeeQueueHome.instance.getPort());
		BeeQueueJvmHelpeer jvmStatus = new BeeQueueJvmHelpeer(BeeQueueHome.instance.getPort());
		if( jvmStatus.starting != null ){
			BeeQueueHome.die(null, "It's odd. Some worker already starting. Kill all workers running and delete file:"+ jvmCsv);
		}
		boolean portAvailable = false;
		for (int i = 0; i < 6; i++) {
			jvmStatus.ensureMeInList(BeeQueueHome.instance.getPort(), BeeQueueJvmStatus.STARTING);
			jvmStatus.write(BeeQueueHome.instance.getPort());
			if(portAvailable(BeeQueueHome.instance.getPort())){
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
	}

	public static boolean portAvailable(int port) {
		ServerSocket ss = null;
	    try {
			ss = new ServerSocket(port);
	        ss.setReuseAddress(true);
	        return true;
	    } catch (IOException ignore) {
	    } finally {
            try { ss.close(); } catch (Exception ignore) {}
	    }
	    return false;
	}

	private static boolean killAllWorkersExceptItself() {
		// TODO add smart killing logic
		//return portAvailable();
		return false;
		
	}

}
