package org.beequeue.hash;

import org.beequeue.util.Nulls;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public abstract class ContentKey {
	public final String name;
	public final String type;
	
	@JsonCreator
	public ContentKey(String name) {
		this.type = getClass().getSimpleName();
		this.name = name;
	}

	@Override @JsonValue
	public String toString() {
		return this.name;
	}

	@Override
	public int hashCode() {
		return 31 * type.hashCode() + name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) 
			return true;
		else if (obj == null) 
			return false;
		else if (getClass() != obj.getClass())
			return false;
		else
			return doCompare((ContentKey)obj)==0;
	}
	
	protected <T extends ContentKey> int doCompare(T that){
		int compare = Nulls.compare(this.type, that.type, true);
		if(compare == 0){
			compare = Nulls.compare(this.name, that.name, true);
		}
		return compare;
	}
	
	

}
