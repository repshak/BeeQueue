package org.beequeue.store.node;

import java.util.Map;

import org.beequeue.hash.HashKey;
import org.beequeue.store.ts.TimeSequence;

public class BeeMessageRangeData extends BeeMessageRangeNode {

	public static class DataEntry {
		public String key;
		public TimeSequence ts;
		public String inline;
		public Map<String,HashKey> offline;
	}

	public DataEntry[] entries;

}
