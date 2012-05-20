package org.beequeue.command;

import java.util.Map;

import org.beequeue.coordinator.Coordiantor;
import org.beequeue.msg.BeeQueueMessage;
import org.beequeue.msg.MessageState;
import org.beequeue.sql.TransactionContext;
import org.beequeue.template.DomainTemplate;
import org.beequeue.template.MessageTemplate;
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
				BeeQueueMessage msg = new BeeQueueMessage();
				msg.domain = args[0];
				msg.name = args[1];
				Map<String, DomainTemplate> activeDomains = Singletons.getGlobalConfig().activeDomains();
				DomainTemplate dt = activeDomains.get(msg.domain);
				if(dt==null){
					die("Cont find domain:"+msg.domain+" here:", activeDomains.keySet());
				}
				MessageTemplate mt = dt.findMessageTemplate(msg.name);
				if(mt==null){
					die("Can't find message:"+msg.name+" here:", dt.messages );
				}
				msg.state = MessageState.EMITTED;
				for (int i = 0; i < mt.columns.length; i++) {
					String col = mt.columns[i];
					if(2+i < args.length ){
						msg.parameters.put(col, args[2+i]);
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
