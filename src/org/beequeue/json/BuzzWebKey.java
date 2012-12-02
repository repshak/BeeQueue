package org.beequeue.json;

import com.fasterxml.jackson.annotation.JsonValue;

public class BuzzWebKey {
	public String url;

	public BuzzWebKey(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		return url.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuzzWebKey other = (BuzzWebKey) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override @JsonValue
	public String toString() {
		return url ;
	}

	
}
