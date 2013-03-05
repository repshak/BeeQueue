/** ==== BEGIN LICENSE =====
   Copyright 2012 - BeeQueue.org

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
 *  ===== END LICENSE ====== */
package org.beequeue.store;

import java.util.Comparator;

import org.beequeue.json.BuzzAttribute;
import org.beequeue.json.BuzzSchema;
import org.beequeue.store.node.BeeMessageKeyRange;
import org.beequeue.store.node.BeeMessageRangeTree;
import org.beequeue.util.BeeException;

public class BeeMessageFrame 
implements Comparator<BeeMessageKey> {
	public BeeMessageFrameKey id ;
	public BuzzAttribute[] keyDefinitions;
	public BuzzSchema valueSchema;
	public int keyLimit;
	public int valueInlineLimit;
	public int fragmentNodeMaxSize;
	
	public static class RetainPolicy{
	};
	public RetainPolicy retainingPolicy;
	BeeMessageRangeTree root;
	
	
	public int compare(BeeMessageKey a, BeeMessageKey b){
		BeeException.throwIfTrue(a.keys.length != b.keys.length,"a.keys.length != b.keys.length");
		BeeException.throwIfTrue(a.keys.length != keyDefinitions.length,"a.keys.length != keyDefinitions.length");
		int rc = 0;
		for (int i = 0; i < keyDefinitions.length; i++) {
			rc = keyDefinitions[i].compare(a.keys[i],b.keys[i]);
			if(rc != 0){
				return rc;
			}
		}
		if( a.time != b.time){
			if( a.time == null ){
				rc = -1 ;
			}else{
				rc = a.time.compareTo(b.time);
			}
		}
		return rc;
	}
	
	
	public int checkRange(BeeMessageKey key, BeeMessageKeyRange range){
		if ( range.start != null && compare(key, range.start) < 0 ){
			return -1;
		}
		if ( range.end != null && compare(range.end, key) <= 0 ){
			return 1;
		}
		return 0;
	}
}
