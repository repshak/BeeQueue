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
package org.beequeue.buzz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.template.GroovyTemplate;
import org.beequeue.util.BeeException;
import org.beequeue.util.BeeOperation;
import org.beequeue.util.BeeOperationChain;
import org.beequeue.util.Files;
import org.beequeue.util.TypeFactory;

import com.fasterxml.jackson.core.type.TypeReference;

public class BuzzRcConfig {

	public static TypeFactory<Map<BuzzPath,BuzzRcConfig>> tf_BUZZ_RC_CONFIG_MAP = TypeFactory.tf(new TypeReference< Map<BuzzPath,BuzzRcConfig> >() {});
	
	private static final BeeOperation<File, String> READ_AND_RESOLVE = new BeeOperation<File, String>() {
		@Override
		public String execute(File input) {
			try {
				String generate = new GroovyTemplate(Files.readAll(input))
					.generate(BeeQueueHome.instance.getVariables());
				System.out.println(generate);
				return generate;
			} catch (IOException e) {
				throw new BeeException(e);
			}
		}
	};

	public BuzzResourceController[] resourceControllers = new BuzzResourceController[0];
	public static Map<BuzzPath,BuzzRcConfig> read(File root) {
		File config = new File(root, "WEB-INF/buzz-config.json.gtemplate");
		return BeeOperationChain.chain(READ_AND_RESOLVE, tf_BUZZ_RC_CONFIG_MAP.op_STRING_TO_OBJ).execute(config);
	}

	/**
	 * add all RC entries in  beginning of <code>resourceControllers</code> array. 
	 * @param toAdd
	 */
	public void add(BuzzRcConfig toAdd) {
		ArrayList<BuzzResourceController> list = new ArrayList<BuzzResourceController>();
		list.addAll(Arrays.asList(toAdd.resourceControllers));
		list.addAll(Arrays.asList(this.resourceControllers));
		this.resourceControllers = list.toArray(new BuzzResourceController[list.size()]);
	}

}
