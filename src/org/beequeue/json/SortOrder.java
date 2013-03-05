package org.beequeue.json;

public enum SortOrder {
	ASCENDING(1),
	DESCENDING(-1);
	
	public final int multiplier;

	private SortOrder(int multiplier) {
		this.multiplier = multiplier;
	}
	
}