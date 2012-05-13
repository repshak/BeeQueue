package org.beequeue.hash;

import java.io.InputStream;

public class HashInput {
	public final HashKey code;
	public final InputStream in;

	public HashInput(HashKey code, InputStream in) {
		super();
		this.code = code;
		this.in = in;
	}

}
