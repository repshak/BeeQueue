package org.beequeue.launcher;



public class BeeQueueJvmInfo {
	public long pid;
	public BeeQueueJvmStatus status;
	public long last_time_alive;
	public int port;
	
	public static BeeQueueJvmInfo itself(BeeQueueJvmStatus s, int port) {
		BeeQueueJvmInfo it = new BeeQueueJvmInfo();
		it.pid = BeeQueueHome.instance.getPid();
		it.status = s;
		it.last_time_alive = System.currentTimeMillis();
		it.port = port;
		return it;
	}
	public static BeeQueueJvmInfo valueOf(String s) {
		BeeQueueJvmInfo that = new BeeQueueJvmInfo();
		try {
			String[] v = s.split(",");
			that.port = Integer.parseInt(v[3]);
			that.last_time_alive = Long.parseLong(v[2]);
			that.status = BeeQueueJvmStatus.valueOf(v[1]);
			that.pid = Long.parseLong(v[0]);
		} catch (Exception e) {
			that = null;
		}
		return that;
	}

	public boolean isMe(){
		return this.pid == BeeQueueHome.instance.getPid();

	}
	
	public boolean doNotSave() {
		return this.status.ordinal() >= BeeQueueJvmStatus.TERMINATED.ordinal() 
			&& this.last_time_alive < (System.currentTimeMillis() - 600000L);
	}
	
	@Override
	public String toString() {
		return "" + pid + "," +status+ ","+ last_time_alive+","+port;
	}
	
	
	
}