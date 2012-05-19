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
package org.beequeue.hash;

import java.io.File;
import java.io.IOException;

import org.beequeue.GlobalConfig;
import org.beequeue.coordinator.DbCoordinatorTest;
import org.beequeue.coordinator.db.DbCoordinator;
import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.sql.TransactionContext;
import org.beequeue.util.Dirs;
import org.beequeue.util.Files;
import org.beequeue.util.ToStringUtil;
import org.beequeue.worker.Singletons;
import org.beequeue.worker.WorkerData;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FileCollectionTest {
	private static final String SEED_TREE = "test/seed";
	
	@Test
	public void test() 
			throws JsonParseException, JsonMappingException, IOException {
		DbCoordinator c = DbCoordinatorTest.getCoordinator();
		TransactionContext.push();
		
		HashKey push = c.push(new File(SEED_TREE));
		System.out.println(push);
		c.pull(push,new File(SEED_TREE.replaceAll("test/", "test-out/")));
		
		TransactionContext.pop();
		
	}

	@Test
	public void tesTooToo() 
			throws JsonParseException, JsonMappingException, IOException {
		String all = Files.readAll(new File("test/seed/global.json"));
		TransactionContext.push();
		Dirs.deleteDirectory(BeeQueueHome.instance.getConfig());
		DbCoordinator c = DbCoordinatorTest.getCoordinator();
		System.out.println(c.sync(WorkerData.instance.config,BeeQueueHome.instance.getConfig()));
		ToStringUtil.toObject(all, GlobalConfig.class);
		Singletons.getGlobalConfig();
		TransactionContext.pop();
	}
	@Test
	public void tesToo() 
			throws JsonParseException, JsonMappingException, IOException {
		
		DbCoordinator c = DbCoordinatorTest.getCoordinator();
		TransactionContext.push();
		ContentTree push = c.push(new File(SEED_TREE),WorkerData.BEE_QUEUE_CONFIG);
		System.out.println(push);
		String pathname = SEED_TREE.replaceAll("test/", "test-out/")+"Too";
		File destination = new File(pathname);
		System.out.println(c.sync(FileCollection.buildContentTree(WorkerData.BEE_QUEUE_CONFIG,destination),destination));
		System.out.println(c.sync(FileCollection.buildContentTree(WorkerData.BEE_QUEUE_CONFIG,destination),destination));
	
		TransactionContext.pop();

	}
	

}
