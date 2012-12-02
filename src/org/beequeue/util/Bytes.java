package org.beequeue.util;

import java.util.ArrayList;

public class Bytes {

	public static byte[] convergeFixedSizeArrays(ArrayList<byte[]> row, int unit) {
		
		byte[] bs = new byte[row.size()*unit];
		for (int i = 0; i < row.size(); i++) {
			for (int j = 0; j < unit; j++) {
				bs[i*unit+j] = row.get(i)[j];
			}
		}
		return bs;
	}

}
