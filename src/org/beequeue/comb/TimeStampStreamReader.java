package org.beequeue.comb;

import java.util.Date;

public interface TimeStampStreamReader<T> {
	/**
	 * @param startFrom date to start iteration from, if <code>null</code> could mean 
	 *    from beginning or from the end depending of <code>direction</code>: 
	 *    {@link IterationDirection#FORWARD} - means start from beginning, 
	 *    {@link IterationDirection#BACKWARD} - from from the end
	 * @param direction of iteration
	 * @return
	 */
	TimeStampStream<T> openStream(Date startFrom, IterationDirection direction );
}
