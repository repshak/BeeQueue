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
import java.util.LinkedHashMap;
import java.util.Map;

import org.beequeue.util.BeeException;
import org.beequeue.util.Files;
import org.beequeue.util.ToStringUtil;

public class GroovyTemplate extends ContentReference {

	public GroovyTemplate(String s) {
		initThat(s, ContentSource.inline, this);
	}

	public boolean generate(Map<String, ?> context, File writeTo) {
		try {
			String content = generate(context);
			boolean same = false;
			if (writeTo.exists()) {
				same = Files.readAll(writeTo).equals(content);
			}
			if (!same) {
				Files.writeAll(writeTo, content);
			}
			return !same;
		} catch (Exception e) {
			throw BeeException.cast(e);
		}
	}

	public String generate(Map<String, ?> context){
		try {
			String resolveReference = resolveReference(context);
			return resolveTemplate(context, resolveReference);
		} catch (Exception e) {
			throw BeeException.cast(e);
		}
	}


	public static String resolveTemplate(Map<String, ?> context, String templateText) {
		try {
			SimpleTemplateEngine engine = new SimpleTemplateEngine(false);
			return engine.createTemplate(templateText).make(new LinkedHashMap<String, Object>(context)).toString();
		} catch (Exception e) {
			throw BeeException.cast(e).addPayload(context,templateText);
		}
	}


}
