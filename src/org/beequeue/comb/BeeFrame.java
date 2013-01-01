package org.beequeue.comb;

import org.beequeue.json.BuzzSchema;

public class BeeFrame {
	public String name;
	public BuzzSchema schema;
	
	public int keyLimit;
	public int valueInlineLimit;
	public int fragmentNodeMaxSize;
	
	public static class VersioningPolicy{
		
	};
	public VersioningPolicy versioningPolicy;
	KeyRangeTree root;
	
}
