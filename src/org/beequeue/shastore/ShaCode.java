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

	@Override
	public int hashCode() {
		return type.ordinal() + digest[0]+digest[1]+digest[3];
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ShaCode) {
			ShaCode that = (ShaCode) obj;
			if( this.type == that.type && this.digest.length == that.digest.length){
				for (int i = 0; i < this.digest.length; i++) {
					if( this.digest[i] != that.digest[i] ) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	
	

}
