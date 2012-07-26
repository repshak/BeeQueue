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
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.beequeue.template.DomainTemplate;
import org.beequeue.template.MessageAttribute;
import org.beequeue.template.MessageTemplate;
import org.beequeue.util.Creator;
import org.beequeue.util.Nulls;
import org.beequeue.worker.Singletons;

public class BeeQueueMessage {
	public String domain;
	public String name;
	public MessageState state;
	public MessageLocator kind;
	public MessageLocator locator;
	
	public Map<String, String> parameters = new LinkedHashMap<String, String>();
	public Map<String, Object> attributes = new Map<String, Object>(){

		@Override
		public int size() {
			return parameters.size();
		}

		@Override
		public boolean isEmpty() {
			return parameters.isEmpty();
		}

		@Override
		public boolean containsKey(Object key) {
			return parameters.containsKey(key);
		}

		@Override
		public Set<String> keySet() {
			return parameters.keySet();
		}

		@Override
		public Object get(Object key) {
			String name = (String) key;
			MessageAttribute ma = messageTemplate().column(name);
			return ma.dataType.toObject(parameters.get(key));
		}

		@Override
		public Object put(String key, Object value) {
			String name = (String) key;
			MessageAttribute ma = messageTemplate().column(name);
			String replaced = parameters.put(name,ma.dataType.toString(value));
			return replaced == null ? null : ma.dataType.toObject(replaced);
		}

		@Override
		public Object remove(Object key) {
			String name = (String) key;
			MessageAttribute ma = messageTemplate().column(name);
			String removed = parameters.remove(name);
			return removed == null ? null : ma.dataType.toObject(removed);
		}


		@Override
		public void putAll(Map<? extends String, ? extends Object> m) {
			for (String k : m.keySet()) {
				put(k,m.get(k));
			}
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Collection<Object> values() {
			throw new UnsupportedOperationException();

		}

		@Override
		public Set<java.util.Map.Entry<String, Object>> entrySet() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean containsValue(Object value) {
			throw new UnsupportedOperationException();
		}

	};
	public long id;
	public Timestamp lock;
	public Timestamp current;
	public Timestamp created;
	
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
	
	public MessageLocator buildMessageLocator(){
		MessageTemplate mt = messageTemplate();
		MessageAttribute[] keyColumns = mt.keyColumns();
		String[] values = new String[keyColumns.length];
		for (int i = 0; i < keyColumns.length; i++) {
			values[i] = Nulls.fallback(parameters.get(keyColumns[i].name),"");
		}
		return new MessageLocator(mt.messageName, values);
	}
	
	public void updateLocators(){
		this.locator = buildMessageLocator();
		this.kind = this.locator.extractMessageKind(messageTemplate());
	}
	
	


}
