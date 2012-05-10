package org.beequeue.shastore;

import java.io.OutputStream;

public class ShaOutput {
	public final ShaCode code;
	public final OutputStream out;
	
	public ShaOutput(ShaCode code, OutputStream out) {
		super();
		this.code = code;
		this.out = out;
	}
	
}
