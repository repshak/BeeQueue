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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class BeeQueueJvmHelpeer {
	public BeeQueueJvmInfo me = null;
	public BeeQueueJvmInfo starting = null;
	public List<BeeQueueJvmInfo> list = new ArrayList<BeeQueueJvmInfo>();
	
	public BeeQueueJvmHelpeer(int port){
		FileReader r = null;
		BufferedReader br=null;
		try {
			r = new FileReader(BeeQueueHome.instance.jvmCsv(port));
			br = new BufferedReader(r);
			BeeQueueJvmInfo v;
			while (null != (v = BeeQueueJvmInfo.valueOf(br.readLine()))) {
				if(v.isMe()){
					me = v;
				}
				if(v.status == BeeQueueJvmStatus.STARTING){
					starting = v;
				}
				list.add(v);
			}
		} catch (Exception e) {}finally{
			try { r.close(); } catch (Exception ignore) {}
			try { br.close(); } catch (Exception ignore) {}
		}
	}

	public void write(int port){
		FileWriter w = null;
		try {
			w = new FileWriter(BeeQueueHome.instance.jvmCsv(port));
			for (BeeQueueJvmInfo v : list) {
				w.write(v.toString());
				w.write("\n");
			}
		} catch (Exception e) {}finally{
			try { w.close(); } catch (Exception ignore) {}
		}
	}

	public void ensureMeInList(int port, BeeQueueJvmStatus status) {
		if( this.me != null ){
			this.me.status = status;
			this.me.last_time_alive = System.currentTimeMillis();
		}else{
			this.me = BeeQueueJvmInfo.itself(status, port);
			this.list.add(me);
		}
	}
}
