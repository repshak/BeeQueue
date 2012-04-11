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
package org.beequeue.groovy;

import groovy.text.SimpleTemplateEngine;

import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import org.beequeue.util.ContentGenerator;
import org.beequeue.util.Template;
import org.beequeue.util.Throwables;


public class GroovyTemplate extends Template {
  private static final String GTEMPLATE = ".gtemplate";

  public static GroovyTemplate getInstance(Class clazz, String template, String prefix, String extension ){
    try {
      if(template == null){
        template = clazz.getSimpleName();
      }
      if(prefix == null){
        prefix = "" ;
      }
      if(extension == null){
        extension = GTEMPLATE ;
      }
      return new GroovyTemplate(clazz,template,prefix,extension);
    }catch (Exception e) {
      return null;
    }
  }  

  public GroovyTemplate(Class clazz, String template, String suffix, String extension ){
    super(clazz, template + suffix + extension);
  }
	
  public GroovyTemplate(Class clazz, String template) {
		super(clazz, template);
	}

	public GroovyTemplate(Class clazz) {
	  super(clazz,clazz.getSimpleName()+GTEMPLATE);
	}
	
	public GroovyTemplate(InputStream is) {
		super(is);
	}

	public GroovyTemplate(Reader reader) {
		super(reader);
	}

	public GroovyTemplate(String template) {
		super(template);
	}

	@Override
	protected ContentGenerator newContentGenerator() {
		return new ContentGenerator(){
			public String generate(Map<String, ?> context) {
				try {
					SimpleTemplateEngine engine = new SimpleTemplateEngine(false);
					groovy.text.Template groovyTemplate = engine.createTemplate(template);
					return groovyTemplate.make(context).toString();
				} catch (Exception e) {
					throw Throwables.cast(RuntimeException.class, e);
				}
			}
			
		};
	}
	
	

}
