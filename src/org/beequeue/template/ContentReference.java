package org.beequeue.template;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public abstract class  ContentReference {
	private ContentSource source = null;
	private String template;

	protected ContentReference() {
	}

	public ContentReference(ContentSource source, String template) {
		this.source = source;
		this.template = template;
	}

	protected static void initThat(String s, ContentSource defaultSource, ContentReference that) {
		ContentSource source = null;
		ContentSource[] values = ContentSource.values();
		for (int i = 0; i < values.length; i++) {
			if( s.startsWith(values[i].name()+":") ){
				source = values[i];
				break;
			}
		}
		if(source == null){
			if(defaultSource == null){
				throw new RuntimeException("no defaultSource, cannot resove:"+s);
			}
			that.source = defaultSource ;
			that.template = s;
		}else{
			that.source = source;
			that.template = s.substring(source.name().length()+1);
		}
	}

	@Override @JsonValue
	public String toString() {
		return source +":"+template;
	}
	
	protected String resolveReference(Map<String, ?> context) throws IOException,
	ClassNotFoundException {
		return source.getTemplateText(this.template, context);
	}



}
