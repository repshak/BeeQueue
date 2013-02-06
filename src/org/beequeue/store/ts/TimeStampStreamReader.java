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
package org.beequeue.store.ts;

import java.util.Date;

import org.beequeue.store.IterationDirection;

public interface TimeStampStreamReader<T> {
	class Entry<T> {
		public final TimeSequence ts;
		public final T data;
		public Entry(TimeSequence ts, T data) {
			this.ts = ts;
			this.data = data;
		}
		
	}
	interface Iterator<T>{
		/** @return next entry or null if no more */
		Entry<T> next();
	}
	
	/**
	 * 
	 * @param startFrom date to start iteration from, if <code>null</code> could mean 
	 *    from beginning or from the end depending of <code>direction</code>: 
	 *    {@link IterationDirection#FORWARD} - means start from beginning, 
	 *    {@link IterationDirection#BACKWARD} - from from the end
	 * @param direction of iteration
	 * @return iterator
	 */
	Iterator<T> iterate(Date startFrom, IterationDirection direction );
}
