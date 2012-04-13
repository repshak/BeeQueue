package org.beequeue.agent;

public interface Creator<T> {
	T create() throws Exception;
	public static class IgnoreExceptions  {
		public static <T> T create (Creator<T> creator){
			try {
				return creator.create();
			} catch (Exception e) {
				return null;
			}
		}
	}

}
