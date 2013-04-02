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
package org.beequeue.store.node;

import org.beequeue.hash.HashKey;
import org.beequeue.hash.HashKeyResource;
import org.beequeue.store.BeeMessageFrameKey;
import org.beequeue.store.BeeMessageKey;
import org.beequeue.util.TypeFactory;

public class BeeMessageKeyRange {
	public static final TypeFactory<BeeMessageKeyRange> TF = new TypeFactory<BeeMessageKeyRange>(BeeMessageKeyRange.class);
	public BeeMessageKey start;
	public BeeMessageKey end;
	public BeeMessageFrameKey frame;
	
	public HashKey rangeKey(HashKeyResource resource) {
		return HashKey.buildHashKey(resource, TF.op_OBJ_TO_COMPACT.execute(this));
	}
}
