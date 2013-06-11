package org.beequeue.hash;

import org.beequeue.util.Nulls;

public abstract class ContentKey {
	public final String name;
	public final String type;
	
	public ContentKey(String name) {
		this.type = getClass().getSimpleName();
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
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
