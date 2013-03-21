package org.beequeue.json;

import org.beequeue.util.TypeFactory;

public interface BuzzRow {
	Object get(String name);
	void set(String name, Object v);
	Object get(int idx);
	void set(int idx, Object v);
}
