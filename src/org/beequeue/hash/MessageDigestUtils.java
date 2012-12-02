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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.beequeue.util.BeeException;

public class MessageDigestUtils {
	private static final int HEX_LETTER_BASELINE = 'A' - 10;

	public static MessageDigest md(){
		try {
			return MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			throw new BeeException(e);
		}
	}
	public static MessageDigest md512(){
		try {
			return MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			throw new BeeException(e);
		}
	}
	
	final static public String toHexString(byte[] b){
		char[] array = new char[b.length*2];
		for (int i = 0; i < b.length; i++) {
			int c = b[i];
			if (c < 0) c = 256 + c ;
			int low = c & 0xF;
			int hi = c >> 4;
		int j = i*2;
		array[j] = toHexChar(hi);
		array[j+1]= toHexChar(low);
		}
		return new String(array);
	}

	final static public byte[] fromHexString(String s){
		byte[] b = new byte[s.length()/2];
		for (int i = 0; i < b.length; i++) {
			int j = i*2;
			int low = fromHexChar(s.charAt(j+1));
			int hi = fromHexChar(s.charAt(j));
			b[i]= (byte)((hi << 4) | low);
		}
		return b;
	}
	
	
	
	final static public char toHexChar(int i){
		return (char)(i > 9 ? HEX_LETTER_BASELINE + i : '0'+i ) ;
	}

	final static public int fromHexChar(char ch){
		return ch >= '0' && ch <= '9' ? ch - '0'  : ch - HEX_LETTER_BASELINE ;
	}
	
	
}
