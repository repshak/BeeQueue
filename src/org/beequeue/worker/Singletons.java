package org.beequeue.worker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.beequeue.GlobalConfig;
import org.beequeue.coordinator.Coordiantor;
import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.template.ContentReference;
import org.beequeue.template.ContentSource;
import org.beequeue.util.BeeException;
import org.beequeue.util.ToStringUtil;

public class Singletons {
	private static ConcurrentMap<String,SingletonReference<?>> JSON_SINGLETONS = new ConcurrentHashMap<String, SingletonReference<?>>();

	@SuppressWarnings("unchecked")
	public static<T> T singleton(String path, Class<T> singletonType) {
		try {
			SingletonReference<T>  c  = null ;
			while( (c = (SingletonReference<T>) JSON_SINGLETONS.get(path)) == null){
				SingletonReference<T> reference = new SingletonReference<T>(path,singletonType);
				JSON_SINGLETONS.putIfAbsent(path, reference);
			}
			return c.instance;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static class SingletonReference<T> extends ContentReference {
		public SingletonReference(String s, Class<T> singletonType) {
			initThat(s, ContentSource.file, this);
			this.singletonType = singletonType;
			refresh();
		}
		
		public T refresh() {
			try {
				String resolve = resolveReference(BeeQueueHome.instance.getHomeVariables());
				return instance = ToStringUtil.toObject(resolve, singletonType);
			} catch (Exception e) {
				throw BeeException.cast(e);
			}
			
		}
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


}
