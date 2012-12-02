package org.beequeue.comb;

import java.io.InputStream;

public interface ContentSource {
	InputStream get(ContentSourceType type);
	TimeStampStreamReader<BlobReferenceLogEntry> log();
	long length();
}
