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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.beequeue.util.Initializable;

public class MessageTemplate implements Initializable {
	
	public String messageName;
	public MessageAttribute [] columns;
	public JobTemplate[] jobs;
	public Map<String,MessageFilter> filters = new LinkedHashMap<String, MessageFilter>();
	public SpawnTemplate spawn;
	
	private MessageAttribute [] keyColumns;
	private Map<String,JobTemplate> jobMap = new LinkedHashMap<String, JobTemplate>();
	private Map<String,MessageAttribute> columnsMap = new LinkedHashMap<String, MessageAttribute>();
	private boolean sequential = false;
	@Override
	public void init() {
		if(jobs != null){
			for (int i = 0; i < jobs.length; i++) {
				JobTemplate jt = jobs[i];
				jobMap.put(jt.jobName, jt);
				jt.init(this);
			}
		}
		if(spawn != null){
			spawn.init(this);
		}
		if(columns != null){
			List<MessageAttribute> keyColumnsList = new ArrayList<MessageAttribute>();
			for (int i = 0; i < columns.length; i++) {
				MessageAttribute ma = columns[i];
				columnsMap.put(ma.name, ma);
				if(ma.attrType.key){
					keyColumnsList.add(ma);
				}
				if(ma.attrType == AttributeType.SEQUENTIAL ){
					sequential = true;
				}
			}
			this.keyColumns = keyColumnsList.toArray(new MessageAttribute[keyColumnsList.size()]);
		}
	}
	
	public boolean isSequential(){
		return false;
		
	}
	
	public JobTemplate jobTemplate(String name){
		return jobMap.get(name);
	}

	public MessageAttribute column(String name) {
		return columnsMap.get(name);
	}

	public MessageAttribute[] keyColumns() {
		return keyColumns;
	}
	

}
