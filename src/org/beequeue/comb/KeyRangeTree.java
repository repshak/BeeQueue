package org.beequeue.comb;

import org.beequeue.hash.HashKey;

public class KeyRangeTree extends KeyRangeNode{
	public static class Entry {
		public String key;
		public TimeSequence ts;
		public String inline;
		public HashKey offline;
	}
	public Entry[] entries;
	
}
