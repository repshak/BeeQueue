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
package org.beequeue.piles;

import java.util.ArrayList;
import java.util.Collection;

import org.beequeue.util.BeeException;

public class BoundList<T> extends ArrayList<T> implements Lockable{
	private static final long serialVersionUID = 1L;

	public static interface Listener {
		void updated();
	}

	private Listener updateListener = null;
	private boolean updatesAllowed = true;
	
	public Listener getUpdateListener() {
		return updateListener;
	}
	
	public void setUpdateListener(Listener updateListener) {
		this.updateListener = updateListener;
	}
	
	public boolean isUpdatesAllowed() {
		return updatesAllowed;
	}
	
	public void preventUpdates() {
		this.updateListener = null;
		this.updatesAllowed = false;
	}


	public BoundList() {}

	public BoundList(Listener listener) {
		this.updateListener = listener;
	}

	@Override
	public void add(int index, T element) {
		BeeException.throwIfTrue(!updatesAllowed, "!updatesAllowed");
		super.add(index, element);
		fireUpdate();
	}

	@Override
	public boolean add(T e) {
		BeeException.throwIfTrue(!updatesAllowed, "!updatesAllowed");
		boolean add = super.add(e);
		fireUpdate();
		return add;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		BeeException.throwIfTrue(!updatesAllowed, "!updatesAllowed");
		boolean addAll = super.addAll(c);
		fireUpdate();
		return addAll;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		BeeException.throwIfTrue(!updatesAllowed, "!updatesAllowed");
		boolean addAll = super.addAll(index, c);
		fireUpdate();
		return addAll;
	}

	@Override
	public void clear() {
		BeeException.throwIfTrue(!updatesAllowed, "!updatesAllowed");
		super.clear();
		fireUpdate();
	}

	@Override
	public T remove(int index) {
		BeeException.throwIfTrue(!updatesAllowed, "!updatesAllowed");
		T remove = super.remove(index);
		fireUpdate();
		return remove;
	}

	@Override
	public boolean remove(Object o) {
		BeeException.throwIfTrue(!updatesAllowed, "!updatesAllowed");
		boolean remove = super.remove(o);
		fireUpdate();
		return remove;
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		BeeException.throwIfTrue(!updatesAllowed, "!updatesAllowed");
		super.removeRange(fromIndex, toIndex);
		fireUpdate();
	}

	@Override
	public T set(int index, T element) {
		BeeException.throwIfTrue(!updatesAllowed, "!updatesAllowed");
		T t = super.set(index, element);
		fireUpdate();
		return t;
	}


	@Override
	public void trimToSize() {
		BeeException.throwIfTrue(!updatesAllowed, "!updatesAllowed");
		super.trimToSize();
		fireUpdate();
	}

	private void fireUpdate() {
		if(updateListener!=null)updateListener.updated();
	}
}
