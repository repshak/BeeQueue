package org.beequeue.time;

import java.sql.Timestamp;

public class LockTimestamp {
	public long lockDuration = 10000L;
	public Timestamp value;
	public Timestamp timeNow;

	public Timestamp newLock() {
		return new Timestamp(timeNow.getTime()+lockDuration);
	}

}
