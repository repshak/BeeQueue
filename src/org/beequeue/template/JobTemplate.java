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

import org.beequeue.msg.BeeQueueEvent;
import org.beequeue.util.BeeException;

/**
 * JobTemplate has many stages, they may depend on each other, but no circular dependencies allowed. 
 * 
 * @author sergeyk
 *
 */
public class JobTemplate {
	public String jobName;
	public StageTemplate[] stages;
	public boolean responsbile = false;
	public String filters[] ;
	private Map<String,StageTemplate> stageMap = new LinkedHashMap<String, StageTemplate>();
	private EventTemplate messageTemplate ;
	
	void init(EventTemplate messageTemplate) {
		this.messageTemplate = messageTemplate;
		checkPresenseOfFilters(filters);
		if(stages!=null){
			for (int i = 0; i < stages.length; i++) {
				StageTemplate st = stages[i];
				stageMap.put(st.stageName, st);
				st.init(this);
			}
		}
	}
	
	public EventTemplate messageTemplate() {
		return messageTemplate;
	}

	public StageTemplate stageTemplate(String name){
		return stageMap.get(name);
	}

	public boolean checkFilters(BeeQueueEvent msg) {
		return matchAnyOfTheseFilters(msg, filters);
	}

	public boolean matchAnyOfTheseFilters(BeeQueueEvent msg, String[] theseFilters) {
		if(theseFilters == null){
			return true;
		}
		for (int i = 0; i < theseFilters.length; i++) {
			if(this.messageTemplate.filters.get(theseFilters[i]).evalFilter(msg)){
				return true;
			}
		}
		return false;
	}
	
	public void checkPresenseOfFilters(String[] theseFilters) {
		if(theseFilters == null) return;
		for (int i = 0; i < theseFilters.length; i++) {
			String key = theseFilters[i];
			if(! this.messageTemplate.filters.containsKey(key)){
				throw new BeeException("No filter defined:"+key).memo("messageTemplate",this.messageTemplate);
			}
		}
	}
}
