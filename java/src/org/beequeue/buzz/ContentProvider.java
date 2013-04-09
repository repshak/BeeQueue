package org.beequeue.buzz;


abstract public class ContentProvider {
	public abstract String getRoot();
	
	public abstract BuzzContent getContent(BuzzPath relativePath);

}
