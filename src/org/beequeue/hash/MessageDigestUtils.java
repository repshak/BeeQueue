package org.beequeue.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.beequeue.sql.DalException;

public class MessageDigestUtils {
	private static final int HEX_LETTER_BASELINE = 'A' - 10;

	public static MessageDigest md(){
		try {
			return MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			throw new DalException(e);
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