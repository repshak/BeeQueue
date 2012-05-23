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

import org.beequeue.template.StageTemplate;
import org.beequeue.util.Creator;

public class BeeQueueStage {

	public long stageId;
	public long jobId;
	public StageState state;
	public long retriesLeft;
	public String stageName;
	public BeeQueueJob job = null;
	public StageState newState;
	
	public StageTemplate stageTemplate(){
		return Creator.IgnoreExceptions.create(new Creator<StageTemplate>() { 
			public StageTemplate create() throws Exception { 
				return job.jobTemplate().stageTemplate(stageName);
			}});
	}
	
}
