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
			String resolveTemplate = GroovyTemplate.resolveTemplate(context, template);
			return Files.readAll(new File(resolveTemplate));
		}
		
	},
	classPath{
		public String getTemplateText(String template,Map<String, ?> context) throws IOException, ClassNotFoundException {
			return Files.readAll(new InputStreamReader(Files.getResourceFromClasspath(GroovyTemplate.resolveTemplate(context, template))));
		}
		
	};
	abstract public String getTemplateText(String template, Map<String, ?> context) throws IOException, ClassNotFoundException ;

}
