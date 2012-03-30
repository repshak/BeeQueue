/** ==== BEGIN LICENSE =====
   Copyright 2004-2007 - Wakeup ORM

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
package beehive.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;


import beehive.piles.LazyMap;

abstract public class Template {
  
  final public String template;

  public Template(Class clazz, String template) {
    this(clazz.getResourceAsStream(template));
  }
  
  public Template(Class clazz) {
    this(clazz,clazz.getSimpleName()+".template");
  }
  
  public Template(String template) {
    this.template = template;
  }

  public Template(InputStream is) {
    this(new InputStreamReader(is));
  }
  
  public Template(Reader reader) {
    StringWriter stringWriter = new StringWriter();
    try {
      Streams.copy(reader, stringWriter);
    } catch (Exception e) {
      throw Throwables.cast(RuntimeException.class, e);
    }
    this.template = stringWriter.toString();
  }
  
  public String generate(Map<String,?> context) {
    return getTemplateEngine().generate(context);
  }
  
  private ContentGenerator engine = null ;
  final public ContentGenerator getTemplateEngine(){
    if(engine == null){
	  engine = newContentGenerator();
    }
	return engine;
  }

   abstract protected ContentGenerator newContentGenerator() ;

  public boolean generate(Map<String,?> context, File writeTo) throws FileNotFoundException, IOException {
    String content = generate(context);
    boolean same = false;
    if( writeTo.exists()){
      same = Files.readAll(writeTo).equals(content);
    }
    if(!same) {
      Files.writeAll(writeTo, content);
    }
    return !same;
  }

  public static LazyMap<String, Object> map() {
    return new LazyMap<String, Object>();
  }

  public static LazyMap<String, Object> map(Object ... objects ) {
    LazyMap<String, Object> map = map();
    for (int i = 0; i < objects.length; i++) {
      map.add("o"+(i+1), objects[i]);
    }
    return map;
  }

}
