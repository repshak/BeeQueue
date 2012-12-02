package org.beequeue.comb;

import org.beequeue.hash.HashKey;

public interface CombStore {
	HashKey save(ContentSource content);
	ContentSource read(HashKey key);
	KeyValueCollection get(String collectionName);
	
	

}
