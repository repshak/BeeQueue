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
package org.beequeue.template;

import java.util.LinkedHashMap;
import java.util.Map;

import org.beequeue.msg.BeeQueueMessage;
import org.beequeue.util.Initializable;

public class StageTemplate {
	public String stageName;
	public String[] dependOnStage;
	public String[] filters ;
	public Map<String,CommandTemplate> commands = new LinkedHashMap<String, CommandTemplate>();
	public RetryStrategy retryStrategy = new RetryStrategy();
	private JobTemplate jobTemplate;
	
	public StageTemplate command(String key, CommandTemplate cmd){
		commands.put(key, cmd);
		return this;
	}

	public void init(JobTemplate jobTemplate) {
		this.jobTemplate = jobTemplate;
		this.jobTemplate.checkPresenseOfFilters(filters);
	}

	public JobTemplate jobTemplate() {
		return jobTemplate;
	}
	
	public boolean checkFilters(BeeQueueMessage msg) {
		return jobTemplate.matchAnyOfTheseFilters(msg, filters);
	}

	
}
