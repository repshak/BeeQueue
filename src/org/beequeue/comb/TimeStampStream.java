package org.beequeue.comb;

public interface TimeStampStream<T> {
	/**
	 * @return next entry or null if no more
	 */
	TimeStampEntry<T> next();
}
