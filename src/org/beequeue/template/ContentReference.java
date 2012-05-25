/** ==== BEGIN LICENSE =====
   Copyright 2012 - BeeQueue.org

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
 *  ===== END LICENSE ====== */
package org.beequeue.template;

import java.io.IOException;
import java.util.Map;

import org.beequeue.util.BeeException;

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
	
	protected String resolveReference(Map<String,?> context) {
		try {
			return source.getTemplateText(this.template, context);
		} catch (Exception e) {
			throw BeeException.cast(e).addPayload(this.template,context);
		}
	}



}
