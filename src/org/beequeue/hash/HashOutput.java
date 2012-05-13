package org.beequeue.hash;

import java.io.OutputStream;

public class HashOutput {
	public final HashKey code;
	public final OutputStream out;
	
	public HashOutput(HashKey code, OutputStream out) {
		super();
		this.code = code;
		this.out = out;
	}
	
}
