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
