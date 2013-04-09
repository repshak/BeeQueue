package org.beequeue.buzz;

import java.io.InputStream;
import java.io.Reader;

import org.beequeue.json.BuzzTable;

public abstract class BuzzContent {
	public abstract String getContentType();
	
	public abstract RetrievalMethod getMethod();
	
	public InputStream getStream() { throw new UnsupportedOperationException(); }
	public Reader getReader() { throw new UnsupportedOperationException(); }
	public BuzzTable getBuzzTable() { throw new UnsupportedOperationException(); }
}
