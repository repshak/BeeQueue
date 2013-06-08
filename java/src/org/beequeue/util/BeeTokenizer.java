package org.beequeue.util;

public final class BeeTokenizer {
	public final String delimiters;
	public final String s;
	private int i = 0 ;
	private int start = 0 ;
	
	public BeeTokenizer(String value, String delimiters) {
		super();
		this.s = value;
		this.delimiters = delimiters;
	}

	public boolean isValueChar(String s, int i){
		return this.delimiters.indexOf(s.charAt(i))<0;
	}

	public String nextValue(){
		while (i < s.length() && isValueChar(s, i) ) i++;
		String next=s.substring(start, i);
		start = i;
		return next;
	}

	public String nextDelimiter(){
		while (i < s.length() && !isValueChar(s, i) ) i++;
		String next=s.substring(start, i);
		start = i;
		return next;
	}

	public void assertDelimiter(String delimiter, String expectedDelimiter) {
		if (!delimiter.equals(expectedDelimiter)) { 
			throw new BeeException("Unexpected delimiter: \'" + delimiter + "\'");
		}
	}
	
	@Override
	public String toString() {
		return s.substring(0,i)+" <-i-> "+s.substring(i);
	}

}
