package org.beequeue.util;

public class JsonBackedObject<T> {
	public final String json;
	public final TypeFactory<T> tf;
	private T value = null;
	
	public JsonBackedObject(String json, TypeFactory<T> tf) {
		this.json = json;
		this.tf = tf;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	
	

}
