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
