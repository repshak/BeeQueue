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
package org.beequeue.store.ts;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.beequeue.store.ts.TimeSequence;
import org.beequeue.util.ToStringUtil;
import org.junit.Test;

public class TimeSequenceTest {

	@Test
	public void json(){
		String notPrettyString = ToStringUtil.toNotPrettyString(TimeSequence.next());
		ToStringUtil.toObject(notPrettyString, TimeSequence.class);
	}
	
	@Test
	public void test() throws InterruptedException {
		ExecutorService pool = Executors.newFixedThreadPool(10);
		Callable[] tasks = new Callable[10];
		for (int i = 0; i < tasks.length; i++) {
			tasks[i] = new TestCallable(i);
			
		}
		for (int i = 0; i < tasks.length; i++) {
			pool.submit(tasks[i]);
		}
		pool.awaitTermination(2, TimeUnit.SECONDS);
		pool.shutdown();
	}
	public static class TestCallable implements Callable<String>{
		public int n;
		
		public TestCallable(int n) {
			super();
			this.n = n;
		}

		@Override
		public String call() throws Exception {
			StringBuilder sb = new StringBuilder();
			sb.append("n=");
			sb.append(n);
			sb.append("\n");
			sb.append(ToStringUtil.toNotPrettyString(TimeSequence.next()));
			sb.append("\n");
			sb.append(ToStringUtil.toNotPrettyString(TimeSequence.next()));
			sb.append("\n");
			sb.append(ToStringUtil.toNotPrettyString(TimeSequence.next()));
			sb.append("\n");
			sb.append(ToStringUtil.toNotPrettyString(TimeSequence.next()));
			sb.append("\n");
			sb.append(ToStringUtil.toNotPrettyString(TimeSequence.next()));
			sb.append("\n");
			String s = sb.toString();
			System.out.println(s);
			return s;
		}
		
	}

}
