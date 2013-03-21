package org.beequeue.piles;

public interface Lockable {
	boolean isUpdatesAllowed();
	void preventUpdates();
}
