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
package org.beequeue.command;

import org.beequeue.coordinator.Coordiantor;
import org.beequeue.msg.BeeQueueMessageDrilldown;
import org.beequeue.sql.TransactionContext;
import org.beequeue.util.ToStringUtil;
import org.beequeue.worker.Singletons;


public class CheckMessage {
	public static void main(String[] args) {
		if( args.length == 1 ){
			try{
				TransactionContext.push();
				Coordiantor coordinator = Singletons.getCoordinator();
				long messageId = Long.parseLong(args[0]);
				BeeQueueMessageDrilldown drilldown = coordinator.checkMessage(messageId);
				System.out.println("Drilldown :  " + ToStringUtil.toString(drilldown));
				System.exit(0);
			}catch(Exception e){
				e.printStackTrace();
				die(e.toString(),null);
			}finally{
				TransactionContext.pop();
			}
		}else{
			die("CheckMessage <msgId>",null);
		}
 	}

	public static void die(String errText,Object o) {
		System.out.println(errText+ (o != null ? ToStringUtil.toString(o) : ""));
		System.exit(-1);
	}

}
