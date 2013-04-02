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
package org.beequeue.coordinator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.beequeue.coordinator.db.DbCoordinator;
import org.beequeue.sql.TransactionContext;
import org.beequeue.util.Files;
import org.beequeue.util.LoggingUtil;
import org.beequeue.util.ToStringUtil;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class DbCoordinatorFunctional {

	private static final String COORDINATOR_JSON = "/Users/sergeyk/git/BeeQueue/bq-home/coordinator.json";

	@Test 
	@Before
	public void create() throws JsonParseException, JsonMappingException, IOException {
		DbCoordinator coord = new DbCoordinator();
		coord.driver = "org.apache.derby.jdbc.ClientDriver";
		coord.url = "jdbc:derby://localhost:1527/bee";
		coord.user = "derbyuser";
		coord.password = "derbyuser";
		String ts = ToStringUtil.toString(coord);
//		System.out.println(ts);
		Coordiantor c2 = ToStringUtil.toObject(ts, Coordiantor.class);
		try{
			TransactionContext.push();
			((DbCoordinator)c2).connection();
		}finally{
			TransactionContext.pop();
		}
		try{
			((DbCoordinator)c2).connection();
			Assert.fail();
		}catch (Exception ignore) {}
		System.out.println(ToStringUtil.toString(c2)); 
//		Files.writeAll( new File(COORDINATOR_JSON), ts );
	}
	
	
	@Test 
	public void newId() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		ConsoleHandler ch = LoggingUtil.setLevelForConsoleHandler(java.util.logging.Level.FINEST);
		LoggingUtil.setSimpleFormatter(ch);
		final ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<String, Long>(); 
		final DbCoordinator c = getCoordinator();
		ExecutorService pool = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 50; i++) {
			pool.submit(
					new Callable<Void>() {
						@Override
						public Void call() throws Exception {
							String t1 = "T1";
							String t2 = "T2";
							try {
								TransactionContext.push();
								
								testNewId(t1);
								testNewId(t1);
								testNewId(t1);
								testNewId(t2);
								testNewId(t2);
								testNewId(t1);
								testNewId(t1);
								testNewId(t2);
								testNewId(t1);
								testNewId(t2);
								testNewId(t1);
								
								TransactionContext.pop();
								
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						}

						public void testNewId( String t) {
							long newId = c.getNewId(t);
							String k = t + ":"+newId;
							Long r = map.putIfAbsent(k, newId);
							if(r == null){
								System.out.println(k);
							}else{
								System.err.println("Dublicate:" + k);
								Assert.fail("Dublicate:" + k);
							}
						}
					});
		}
		pool.shutdown();
		pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

	}


	public static DbCoordinator getCoordinator() throws JsonParseException,
			JsonMappingException, IOException {
		final DbCoordinator c = (DbCoordinator)ToStringUtil.toObject(Files.readAll(new File(COORDINATOR_JSON)), Coordiantor.class);
		return c;
	}




}
