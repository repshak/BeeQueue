package org.beequeue.command;

import java.io.File;

import org.beequeue.coordinator.Coordiantor;
import org.beequeue.hash.ContentTree;
import org.beequeue.sql.TransactionContext;
import org.beequeue.util.ToStringUtil;
import org.beequeue.worker.Singletons;
import org.beequeue.worker.WorkerData;


public class PushConfig {
	public static void main(String[] args) {
		if( args.length == 1 ){
			try{
				TransactionContext.push();
				Coordiantor c = Singletons.getCoordinator();
				ContentTree push = c.push(new File(args[0]),WorkerData.BEE_QUEUE_CONFIG);
				System.out.println(push);
				System.exit(0);
			}catch(Exception e){
				e.printStackTrace();
				die(e.toString(),null);
			}finally{
				TransactionContext.pop();
			}
		}else{
			die("PushConfig <bq_config_dir>",null);
		}
 	}

	public static void die(String errText,Object o) {
		System.out.println(errText+ (o != null ? ToStringUtil.toString(o) : ""));
		System.exit(-1);
	}

}
