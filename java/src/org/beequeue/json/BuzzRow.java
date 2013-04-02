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
package org.beequeue.json;

import java.util.List;

import org.beequeue.piles.Lockable;
import org.beequeue.util.BeeException;
import org.beequeue.util.ToStringUtil;

public class BuzzRow implements Lockable{
	private final BuzzHeader columns;
	private final Object[] data ;
	private BuzzRow previousVersion;

	public BuzzRow getPreviousVersion() {
		return previousVersion;
	}

	public BuzzRow(BuzzHeader columns) {
		this.columns = columns;
		this.data = new Object[columns.size()];
	}
	
	public BuzzRow(BuzzHeader columns, List<Object> rawData) {
		this(columns);					
		for (int i = 0; i < rawData.size(); i++) {
			set(i, rawData.get(i));
		}
	}

	public void setPreviousVersion(BuzzRow previousVersion) {
		BeeException.throwIfTrue(!isUpdatesAllowed(), "!isUpdatesAllowed()");
		this.previousVersion = previousVersion;
	}

	
	public Object get(String name) {
		return get(columns.colIndex(name));
	}


	public void set(String name, Object v) {
		set(columns.colIndex(name), v);
	}

	public Object get(int idx) {
		return data[idx];
	}

	public void set(int idx, Object v) {
		BeeException.throwIfTrue(!isUpdatesAllowed(), "!isUpdatesAllowed()");
		data[idx] = columns.get(idx).coerce(v);
	}


	public String toString() {
		return ToStringUtil.toNotPrettyString(data);
	}


	public BuzzHeader header() {
		return columns;
	}


	private boolean updatesAllowed = true;
	@Override
	public boolean isUpdatesAllowed() {
		return updatesAllowed;
	}


	@Override
	public void preventUpdates() {
		this.updatesAllowed = false;
	}
	
}
