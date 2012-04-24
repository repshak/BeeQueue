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
/**
 * Wakeup OR 
 * www.apache.org/licenses/LICENSE-2.0.html  
 */
package org.beequeue.template;

import groovy.text.SimpleTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.beequeue.util.Files;
import org.codehaus.groovy.control.CompilationFailedException;

import com.fasterxml.jackson.annotation.JsonValue;

public class GroovyTemplate {
	public enum Source {
		inline{
			public String getTemplateText(String template, Map<String, ?> context) throws IOException, ClassNotFoundException {
				return template;
			}
		},
		file{
			public String getTemplateText(String template, Map<String, ?> context) throws IOException, ClassNotFoundException {
				return Files.readAll(new File(resolveTemplate(context, template)));
			}
			
		},
		classPath{
			public String getTemplateText(String template,Map<String, ?> context) throws IOException, ClassNotFoundException {
				return Files.readAll(new InputStreamReader(Files.getResourceFromClasspath(resolveTemplate(context, template))));
			}
			
		};
		abstract public String getTemplateText(String template, Map<String, ?> context) throws IOException, ClassNotFoundException ;

	}
	private Source source = null;
	private String template;


	
	public GroovyTemplate(Source source, String template) {
		this.source = source;
		this.template = template;
	}

	public GroovyTemplate(String s) {
		Source[] values = Source.values();
		for (int i = 0; i < values.length; i++) {
			if( s.startsWith(values[i].name()+":") ){
				this.source = values[i];
			}
		}
		if(this.source == null){
			this.source = Source.inline ;
			this.template = s;
		}else{
			this.template = s.substring(this.source.name().length()+1);
		}
	}

	@Override @JsonValue
	public String toString() {
		return source +":"+template;
	}

	public boolean generate(Map<String, ?> context, File writeTo)
			throws IOException, CompilationFailedException,
			ClassNotFoundException {
		String content = generate(context);
		boolean same = false;
		if (writeTo.exists()) {
			same = Files.readAll(writeTo).equals(content);
		}
		if (!same) {
			Files.writeAll(writeTo, content);
		}
		return !same;
	}

	public String generate(Map<String, ?> context)
			throws CompilationFailedException, ClassNotFoundException,
			IOException {
		String templateText = source.getTemplateText(this.template, context);
		return resolveTemplate(context, templateText);
	}

	public static String resolveTemplate(Map<String, ?> context, String templateText)
			throws ClassNotFoundException, IOException {
		SimpleTemplateEngine engine = new SimpleTemplateEngine(false);
		return engine.createTemplate(templateText).make(context).toString();
	}


}
