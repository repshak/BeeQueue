package org.beequeue.shastore;

import java.io.InputStream;

public class ShaInput {
	public final ShaCode code;
	public final InputStream in;

	public ShaInput(ShaCode code, InputStream in) {
		super();
		this.code = code;
		this.in = in;
	}

}
