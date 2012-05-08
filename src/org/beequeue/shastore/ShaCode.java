package org.beequeue.shastore;

import com.fasterxml.jackson.annotation.JsonValue;

public class ShaCode {
	public static enum Resource {
		F("FILE"), 
		D("DIR");
		
		public final String description;
		
		private Resource(String description){
			this.description = description;
		}
	}
	
	final public Resource type;
	final public byte digest[];
	
	
	public ShaCode(Resource type, byte[] digest) {
		this.type = type;
		this.digest = digest;
	}

	public ShaCode(Resource valueOf, String substring) {
		this(valueOf,MessageDigestUtils.fromHexString(substring));
	}

	
	public static ShaCode valueOf(String s) {
		String g = s.substring(0, 1);
		return new ShaCode(Resource.valueOf(g),s.substring(1));
	}

	@Override @JsonValue
	public String toString() {
		return type.name()+MessageDigestUtils.toHexString(digest);
	}
	
	
	

}
