package org.beequeue.comb;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
