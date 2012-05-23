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
import java.util.LinkedHashMap;
import java.util.Map;

import org.beequeue.template.DomainTemplate;
import org.beequeue.template.MessageTemplate;
import org.beequeue.util.Creator;
import org.beequeue.worker.Singletons;

public class BeeQueueMessage {
	public String domain;
	public String name;
	public MessageState state;
	public Map<String, String> parameters = new LinkedHashMap<String, String>();
	public Map<String, String> contextSettings = null;
	public long id;
	public Timestamp lock;
	public Timestamp current;
	
	public Timestamp newLock() {
		return new Timestamp(current.getTime()+10000L);
	}

	public DomainTemplate domainTemplate(){
		return Creator.IgnoreExceptions.create(new Creator<DomainTemplate>() { 
			public DomainTemplate create() throws Exception { 
				return Singletons.getGlobalConfig().activeDomains().get(domain);
			}});
	}
	public MessageTemplate messageTemplate(){
		return Creator.IgnoreExceptions.create(new Creator<MessageTemplate>() { 
			public MessageTemplate create() throws Exception { 
				return domainTemplate().messageTemplate(name);
			}});
	}

}
