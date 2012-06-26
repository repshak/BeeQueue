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

import org.beequeue.util.Initializable;

public class MessageTemplate implements Initializable {
	public String messageName;
	public MessageAttribute [] columns;
	public JobTemplate[] jobs;
	public Map<String,MessageFilter> filters = new LinkedHashMap<String, MessageFilter>();
	private Map<String,JobTemplate> jobMap = new LinkedHashMap<String, JobTemplate>();
	
	@Override
	public void init() {
		for (int i = 0; i < jobs.length; i++) {
			JobTemplate jt = jobs[i];
			jobMap.put(jt.jobName, jt);
			jt.init(this);
		}
	}	
	
	public JobTemplate jobTemplate(String name){
		return jobMap.get(name);
	}
	

}
