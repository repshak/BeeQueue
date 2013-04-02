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
import java.util.List;

public class BeeQueueRun {
	public long id;
	public long stageId;
	public long workerId;
	public RunState state;
	public String pid;
	public String processName;
	public String dir;
	public String cmd;
	public Timestamp startTimeStamp;
	public Timestamp upTimeStamp;
	public Timestamp downTimeStamp;
	public BeeQueueStage stage;
	
	public boolean justUpTimeStamp = false;
	
	public List<BeeQueueProcess> children=null;
	
	public boolean isInFinalState() {
		return state == RunState.KILLED || state == RunState.FAILURE || state == RunState.SUCCESS;
	}

}
