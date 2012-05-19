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
package org.beequeue.hash;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import com.fasterxml.jackson.annotation.JsonValue;

public class HashKey {
	final public HashKeyResource type;
	final public byte digest[];
	
	
	public HashKey(HashKeyResource type, byte[] digest) {
		this.type = type;
		this.digest = digest;
	}

	public HashKey(HashKeyResource valueOf, String substring) {
		this(valueOf,MessageDigestUtils.fromHexString(substring));
	}

	
	public static HashKey valueOf(String s) {
		String g = s.substring(0, 1);
		return new HashKey(HashKeyResource.valueOf(g),s.substring(1));
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
		if (obj instanceof HashKey) {
			HashKey that = (HashKey) obj;
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
	
	
	private static ThreadLocal<byte[]> BUFFER = new ThreadLocal<byte[]>();
	public static HashKey buildHashKey(HashKeyResource resource, InputStream in) throws IOException {
		byte[] buffer = BUFFER.get();
		if(buffer==null){
			BUFFER.set(new byte[32*1024]);
			buffer = BUFFER.get();
		}
		MessageDigest md = MessageDigestUtils.md();
		int countRead ;
		while((countRead = in.read(buffer))>0){
			md.update(buffer, 0, countRead);
		}
		return new HashKey(resource,md.digest());
	}

	public static HashKey buildHashKey(HashKeyResource resource, byte[] buffer) {
		MessageDigest md = MessageDigestUtils.md();
		md.update(buffer);
		return new HashKey(resource,md.digest());
	}
	

}
