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
