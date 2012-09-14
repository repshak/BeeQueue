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

import java.io.IOException;
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
			BeeQueueCommand.runMain("RunServer", extraArgs);
		}
	}

	


}
