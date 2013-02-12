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

import java.util.Map;

import org.beequeue.coordinator.Coordiantor;
import org.beequeue.msg.BeeQueueEvent;
import org.beequeue.msg.MessageState;
import org.beequeue.sql.TransactionContext;
import org.beequeue.template.DomainTemplate;
import org.beequeue.template.EventAttribute;
import org.beequeue.template.EventTemplate;
import org.beequeue.util.ToStringUtil;
import org.beequeue.worker.Singletons;
import org.beequeue.worker.WorkerData;


public class SendMessage {
	public static void main(String[] args) {
		if( args.length >= 2 ){
			try{
				TransactionContext.push();
				Coordiantor coordinator = Singletons.getCoordinator();
				WorkerData.instance.checkGlobalConfig(coordinator);
				BeeQueueEvent msg = new BeeQueueEvent();
				msg.domain = args[0];
				msg.name = args[1];
				Map<String, DomainTemplate> activeDomains = Singletons.getGlobalConfig().activeDomains();
				DomainTemplate dt = activeDomains.get(msg.domain);
				if(dt==null){
					die("Cont find domain:"+msg.domain+" here:", activeDomains.keySet());
				}
				EventTemplate mt = dt.messageTemplate(msg.name);
				if(mt==null){
					die("Can't find message:"+msg.name+" here:", dt.messages );
				}
				msg.state = MessageState.EMITTED;
				for (int i = 0; i < mt.columns.length; i++) {
					EventAttribute col = mt.columns[i];
					if(2+i < args.length ){
						msg.parameters.put(col.name, args[2+i]);
					}else{
						die(""+i+" parameter missing: ", mt.columns);
					}
				}
				coordinator.storeMessage(msg);
				System.out.println("Submited :  " + ToStringUtil.toString(msg));
				System.exit(0);
			}catch(Exception e){
				e.printStackTrace();
				die(e.toString(),null);
			}finally{
				TransactionContext.pop();
			}
		}else{
			die("SendMessage <domain> <msgName> [<msgArg1 msgArg2 .... msgArgN>]",null);
		}
 	}

	public static void die(String errText,Object o) {
		System.out.println(errText+ (o != null ? ToStringUtil.toString(o) : ""));
		System.exit(-1);
	}

}
