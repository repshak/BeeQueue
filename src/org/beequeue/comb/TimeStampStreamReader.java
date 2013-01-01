package org.beequeue.comb;

import java.util.Date;

public interface TimeStampStreamReader<T> {
	class Entry<T> {
		public final TimeSequence ts;
		public final T data;
		public Entry(TimeSequence ts, T data) {
			this.ts = ts;
			this.data = data;
		}
		
	}
	interface Iterator<T>{
		/** @return next entry or null if no more */
		Entry<T> next();
	}
	
	/**
	 * 
	 * @param startFrom date to start iteration from, if <code>null</code> could mean 
	 *    from beginning or from the end depending of <code>direction</code>: 
	 *    {@link IterationDirection#FORWARD} - means start from beginning, 
	 *    {@link IterationDirection#BACKWARD} - from from the end
	 * @param direction of iteration
	 * @return iterator
	 */
	Iterator<T> iterate(Date startFrom, IterationDirection direction );
}
