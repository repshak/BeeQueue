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
package org.beequeue;

import java.util.LinkedHashMap;
import java.util.Map;

import org.beequeue.host.CloudConfig;
import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.launcher.VariablesProvider;
import org.beequeue.template.DomainTemplate;
import org.beequeue.util.Initializable;
import org.beequeue.util.ToStringUtil;
import org.beequeue.worker.Singletons;
import org.beequeue.worker.WorkerConfig;

import com.sun.org.apache.xalan.internal.xsltc.DOM;


public class GlobalConfig implements Initializable {
	public static final String $BQ_CONFIG = "$BQ_CONFIG";
	public static final String LOAD_FROM = $BQ_CONFIG + "/global.json";
	
	public String defaultCloud;
	
	public Map<String, CloudConfig> clouds ;
	
	public WorkerConfig workerConfig = new WorkerConfig();
	
	public String findCloudForHost(String hostName) {
		if(clouds!=null){
			for (String cloud : clouds.keySet()) {
				if(Boolean.TRUE.equals(clouds.get(cloud).doesHostBelong(hostName)) ){
					return cloud;
				}
			}
		}
		return defaultCloud;
	}

	public CloudConfig cloudConfig(String cloudname) {
		if(this.clouds!=null){
			return this.clouds.get(cloudname);
		}
		return null;
	}

	@Override
	public void init() {
		if(this.clouds!=null){
			for (String cloud : this.clouds.keySet()) {
				CloudConfig cloudConfig = this.clouds.get(cloud);
				if(cloudConfig.domains != null){
					for (int i = 0; i < cloudConfig.domains.length; i++) {
						final String domainName = cloudConfig.domains[i];
						if(!activeDomains.containsKey(domainName)){
							String configReference = "$" + BeeQueueHome.BQ_CONFIG + "/domains/"+domainName+"/"+domainName+".json";
							VariablesProvider domainSpecificVariables = new VariablesProvider() {
								@Override
								public Map<String, ?> getVariables() {
									Map<String, String> homeVariables = BeeQueueHome.instance.getHomeVariables();
									String domainDir = homeVariables.get(BeeQueueHome.BQ_CONFIG)+"/domains/"+domainName;
									homeVariables.put("BQ_DOMAIN", domainDir );
									return homeVariables;
								}
							};
							DomainTemplate dt = Singletons.singleton(configReference, DomainTemplate.class,domainSpecificVariables);
							activeDomains.put(domainName, dt);
						}
					}
				}
			}
			//System.out.println(ToStringUtil.toString(activeDomains));
		}
	}
	
	private Map<String,DomainTemplate> activeDomains = new LinkedHashMap<String, DomainTemplate>();
	public Map<String, DomainTemplate> activeDomains() {
		return activeDomains;
	}

	

	
}
