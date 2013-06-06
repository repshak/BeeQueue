package org.beequeue.util;

import java.io.ByteArrayOutputStream;


/**
 * main idea here: http://en.wikipedia.org/wiki/Percent-encoding
 * but {@link PercentEncoding#ENCODED_CHARS} list is smaller
 */
public class PercentEncoding {
	public final static String ENCODED_CHARS = "%+/?&=";
	
	public static String encode(String input){
		return encode(input, null).toString();
	}


	public static StringBuilder encode(String input, StringBuilder output) {
		try {
			byte[] bytes = input.getBytes("UTF-8");
			if(output == null){
				output = new StringBuilder(bytes.length);
			}
			for (int i = 0; i < bytes.length; i++) {
				int c = bytes[i];
				if (c < 0) {
					c += 256;
				}
				if (c == 32) {
					output.append('+');
					continue;
				} else if (c > 32 && c < 128) {
					int p = ENCODED_CHARS.indexOf(c);
					if (p < 0) {
						output.append((char)c);
						continue;
					}
				}
				output.append('%');
				String hexString = Integer.toHexString(c).toUpperCase();
				int ln = hexString.length();
				if (ln == 1) {
					output.append('0');
				} else {
					BeeException.throwIfTrue(ln != 2, hexString);
				}
				output.append(hexString);
			}
			return output;
		} catch (Exception e) {
			throw BeeException.cast(e).memo("input",input);
		}
	}

	public static String decode(String input){
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream(input.length()+10);
			for (int i = 0; i < input.length(); i++) {
				int c = input.charAt(i);
				if (c == '+') {
					c = ' ';
				}
				if (c == '%') {
					if (i + 2 < input.length()) {
						try {
							int charCode = Integer.parseInt(
									input.substring(i + 1, i + 3), 16);
							i += 2;
							c = charCode;
						} catch (Exception ignore) {
						}
					}
				}
				out.write(c);
			}
			return out.toString("UTF-8");
		} catch (Exception e) {
			throw BeeException.cast(e).memo("input",input);
		}
		
	}
}
