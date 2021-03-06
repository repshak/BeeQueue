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
import java.util.Map;

import org.beequeue.util.Tuple;
import org.beequeue.util.Tuples;
import org.codehaus.groovy.control.CompilationFailedException;

public class FileGroovyTemplate {
	GroovyTemplate fileName;
	GroovyTemplate body;
	
	  public Tuple<File,Boolean> generate(Map<String,?> context)  {
		    String file = fileName.generate(context);
		    File fileToWriteTo = new File(file);
			boolean newContentGenerated = body.generate(context, fileToWriteTo);
			return Tuples.build(fileToWriteTo, newContentGenerated);
	  }
	
	
}
