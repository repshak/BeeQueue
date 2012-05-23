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
package org.beequeue.worker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.beequeue.GlobalConfig;
import org.beequeue.coordinator.Coordiantor;
import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.launcher.VariablesProvider;
import org.beequeue.template.ContentReference;
import org.beequeue.template.ContentSource;
import org.beequeue.util.BeeException;
import org.beequeue.util.ToStringUtil;

public class Singletons {
	private static ConcurrentMap<String,SingletonReference<?>> JSON_SINGLETONS = new ConcurrentHashMap<String, SingletonReference<?>>();

	public static<T> T singleton(String path, Class<T> singletonType) {
		return singleton(path, singletonType,BeeQueueHome.instance);
	}
	
	@SuppressWarnings("unchecked")
	public static<T> T singleton(String path, Class<T> singletonType, VariablesProvider variablesProvider) {
		try {
			SingletonReference<T>  c  = null ;
			while( (c = (SingletonReference<T>) JSON_SINGLETONS.get(path)) == null){
				SingletonReference<T> reference = new SingletonReference<T>(path,singletonType,variablesProvider);
				JSON_SINGLETONS.putIfAbsent(path, reference);
			}
			return c.instance;
		} catch (Exception e) {
			throw BeeException.cast(e);
		}
	}
	
	public static class SingletonReference<T> extends ContentReference {
		public SingletonReference(String s, Class<T> singletonType, VariablesProvider variablesProvider) {
			initThat(s, ContentSource.file, this);
			this.singletonType = singletonType;
			this.variablesProvider=variablesProvider;
			refresh();
		}
		
		public T refresh() {
			String resolve = null;
			try {
				resolve = resolveReference(variablesProvider.getVariables());
				return instance = ToStringUtil.toObject(resolve, singletonType);
			} catch (Exception e) {
				throw BeeException.cast(e).addPayload(resolve,this);
			}
		}

		
		private VariablesProvider variablesProvider;
		private Class<T> singletonType ;
		private T instance;
	}
	
	public static Coordiantor getCoordinator() {
		return Singletons.singleton(Coordiantor.LOAD_FROM, Coordiantor.class);
	}
	
	public static GlobalConfig getGlobalConfig() {
		
		return Singletons.singleton(GlobalConfig.LOAD_FROM, GlobalConfig.class);
	}
	
	public static void refresh(String path) {
		SingletonReference<?> reference = JSON_SINGLETONS.get(path);
		if(reference!=null) reference.refresh();
	}

	public static void refreshAll(String startWith) {
		for (String k : JSON_SINGLETONS.keySet()) {
			if( k.startsWith(startWith) ){
				JSON_SINGLETONS.get(k).refresh();
			}
			
		}
		
	}
}
