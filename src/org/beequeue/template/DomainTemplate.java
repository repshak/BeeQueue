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

public class DomainTemplate implements Initializable {
	public EventTemplate[] messages;
	public Map<String,String> properties = new LinkedHashMap<String, String>();
	
	private Map<String,String> addOnProperties = new LinkedHashMap<String, String>();
	public Map<String, String> addOnProperties() {
		return addOnProperties;
	}
	public Map<String, String> allProperties() {
		LinkedHashMap<String, String> all = new LinkedHashMap<String, String>();
		all.putAll(properties);
		all.putAll(addOnProperties);
		return all;
	}
	
	private Map<String,EventTemplate> messageMap = new LinkedHashMap<String, EventTemplate>();
	
	public EventTemplate messageTemplate(String name) {
		return messageMap.get(name);
	}

	@Override
	public void init() {
		for (int i = 0; i < messages.length; i++) {
			EventTemplate mt = messages[i];
			messageMap.put(mt.messageName, mt);
			mt.init();
		}
	}
}
