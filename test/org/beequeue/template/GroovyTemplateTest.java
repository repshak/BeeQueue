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

import org.beequeue.piles.LazyMap;
import org.codehaus.groovy.control.CompilationFailedException;
import org.junit.Test;

public class GroovyTemplateTest {

	@Test
	public void test() throws CompilationFailedException, ClassNotFoundException, IOException {
		String z[] = { "Float" };
		for (int i = 0; i < z.length; i++) {
			String v = z[i];
			
			System.out.println(
					new GroovyTemplate("classPath:/org/beequeue/template/DataType.gt")
					.generate(new LazyMap<String, Object>()
							.add("T", v)
							.add("U", v.toUpperCase())
							));
			
		}
	}

}
