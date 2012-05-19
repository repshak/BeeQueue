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