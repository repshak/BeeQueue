package org.beequeue.store.node;

import org.beequeue.hash.HashKey;
import org.beequeue.store.ts.TimeSequence;

public class BeeMessageRangeData extends BeeMessageRangeNode {

	public static class DataEntry {
		public String key;
		public TimeSequence ts;
		public String inline;
		public HashKey offline;
	}

	public DataEntry[] entries;

}
