package org.beequeue.hash;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import org.beequeue.util.UtfUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Hash {
	final public byte digest[];
	
	public Hash(byte[] digest) {
		this.digest = digest;
	}
	public Hash(String s) {
		this.digest = MessageDigestUtils.fromHexString(s);
	}

	@JsonCreator
	public static Hash valueOf(String s) {
		return new Hash(s);
	}
	
	@Override @JsonValue
	public String toString() {
		return MessageDigestUtils.toHexString(digest);
	}

	@Override
	public int hashCode() {
		return digest[0]+digest[1]+digest[3];
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HashKey) {
			HashKey that = (HashKey) obj;
			if( this.digest.length == that.digest.length){
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
	
	
	private static ThreadLocal<byte[]> BUFFER = new ThreadLocal<byte[]>();
	public static Hash buildHash(InputStream in) throws IOException {
		byte[] buffer = ensureBuffer();
		MessageDigest md = MessageDigestUtils.md();
		int countRead ;
		while((countRead = in.read(buffer))>0){
			md.update(buffer, 0, countRead);
		}
		return new Hash(md.digest());
	}
	public static byte[] ensureBuffer() {
		byte[] buffer = BUFFER.get();
		if(buffer==null){
			BUFFER.set(new byte[32*1024]);
			buffer = BUFFER.get();
		}
		return buffer;
	}

	public static Hash buildHashKey(byte[] buffer) {
		MessageDigest md = MessageDigestUtils.md();
		md.update(buffer);
		return new Hash(md.digest());
	}
	
	public static Hash buildHash(String buffer) {
		MessageDigest md = MessageDigestUtils.md();
		md.update(buffer.getBytes(UtfUtils.UTF8));
		return new Hash(md.digest());
	}
	
}
