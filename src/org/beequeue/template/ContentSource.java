package org.beequeue.template;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.beequeue.util.Files;

public enum ContentSource {
	inline{
		public String getTemplateText(String template, Map<String, ?> context) throws IOException, ClassNotFoundException {
			return template;
		}
	},
	file{
		public String getTemplateText(String template, Map<String, ?> context) throws IOException, ClassNotFoundException {
			return Files.readAll(new File(GroovyTemplate.resolveTemplate(context, template)));
		}
		
	},
	classPath{
		public String getTemplateText(String template,Map<String, ?> context) throws IOException, ClassNotFoundException {
			return Files.readAll(new InputStreamReader(Files.getResourceFromClasspath(GroovyTemplate.resolveTemplate(context, template))));
		}
		
	};
	abstract public String getTemplateText(String template, Map<String, ?> context) throws IOException, ClassNotFoundException ;

}