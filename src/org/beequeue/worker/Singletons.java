package org.beequeue.worker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.beequeue.coordinator.Coordiantor;
import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.template.ContentReference;
import org.beequeue.template.ContentSource;
import org.beequeue.util.ToStringUtil;

public class Singletons {
	private static ConcurrentMap<String,Object> JSON_SINGLETONS = new ConcurrentHashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public static<T> T singleton(String path, Class<T> singletonType) {
		try {
			T c  = null ;
			while( (c = (T) JSON_SINGLETONS.get(path)) == null){
				String s = new SingletonReference(path).text();
				Object object = ToStringUtil.toObject(s, singletonType);
				JSON_SINGLETONS.putIfAbsent(path, object);
			}
			return c;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static class SingletonReference extends ContentReference {
		public SingletonReference(String s) {
			initThat(s, ContentSource.file, this);
		}
		
		public String text(){
			try {
				return resolveReference(BeeQueueHome.instance.getHomeVariables());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public static Coordiantor getCoordinator() {
		return Singletons.singleton("$BQ_HOME/coordinator.json", Coordiantor.class);
	}


}
