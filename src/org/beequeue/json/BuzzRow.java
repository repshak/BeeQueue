package org.beequeue.json;

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

	public void setPreviousVersion(BuzzRow previousVersion) {
		BeeException.throwIfTrue(!isUpdatesAllowed(), "!isUpdatesAllowed()");
		this.previousVersion = previousVersion;
	}

	public BuzzRow(BuzzHeader columns) {
		this.columns = columns;
		this.data = new Object[columns.size()];
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
