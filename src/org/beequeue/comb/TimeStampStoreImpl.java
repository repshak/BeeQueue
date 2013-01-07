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
package org.beequeue.comb;

import java.io.File;
import java.util.Date;

import org.beequeue.util.ToStringUtil;

import com.fasterxml.jackson.core.type.TypeReference;

public class TimeStampStoreImpl implements TimeStampStore {

	private final class Reader<T> implements TimeStampStreamReader<T> {
		private final File file;
		private final Class<T> cls;
		private final TypeReference<T> ref;
		private Reader(TypeReference<T>ref,File file) {
			this.cls = null;
			this.ref = ref;
			this.file = file;
		}

		private Reader(Class<T> cls,File file) {
			this.cls = cls;
			this.ref = null;
			this.file = file;
		}

		private T factory(String s){
			if(cls!=null){
				return ToStringUtil.toObject(s, cls);
			}else{
				return ToStringUtil.toObject(s, ref);
			}
		}

		@Override
		public org.beequeue.comb.TimeStampStreamReader.Iterator<T> iterate(
				Date startFrom, IterationDirection direction) {
			// TODO Auto-generated method stub
			return null;
		}
		
//		@Override
//		public TimeStampStream<T> openStream(Date startFrom, IterationDirection direction) {
//			try {
//				RandomAccessFile raf = new RandomAccessFile(file, "r");
//				return null;
//			} catch (Exception e) {
//				throw new BeeException(e);
//			}
//		}
	}

	@Override
	public <T> TimeStampStreamReader<T> reader(Class<T> type, File file) {
		return new Reader<T>(type, file);
	}

	@Override
	public <T> TimeStampStreamReader<T> reader(final TypeReference<T> type, final File file) {
		return new Reader<T>(type, file);
	}

	@Override
	public <T> void append(File file, T toAdd) {
		// TODO Auto-generated method stub

	}

}
