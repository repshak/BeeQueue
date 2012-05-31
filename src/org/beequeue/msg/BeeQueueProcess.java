package org.beequeue.msg;

import java.sql.Timestamp;

public class BeeQueueProcess {
	public long runId;
	public String pid;
	public String ppid;
	public String processName;
	public Timestamp startTimeStamp;
	public Timestamp upTimeStamp;
	public Timestamp downTimeStamp;
	public boolean down =  false;
	@Override
	public int hashCode() {
		return (int)runId +pid.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BeeQueueProcess) {
			BeeQueueProcess that = (BeeQueueProcess) obj;
			return this.runId == that.runId && this.pid.equals(that.pid);
		}
		return false;
	}
	
}