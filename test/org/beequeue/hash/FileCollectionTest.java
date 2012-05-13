package org.beequeue.hash;

import java.io.File;
import java.io.IOException;

import org.beequeue.coordinator.DbCoordinatorTest;
import org.beequeue.coordinator.db.DbCoordinator;
import org.beequeue.sql.TransactionContext;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FileCollectionTest {
	private static final String COORDINATOR_JSON = "/Users/sergeyk/git/BeeQueue/bq-home";

	@Test
	public void test() 
			throws JsonParseException, JsonMappingException, IOException {
		DbCoordinator c = DbCoordinatorTest.getCoordinator();
		TransactionContext.push();
		
		HashKey push = c.push(new File(COORDINATOR_JSON));
		System.out.println(push);
		c.pull(push,new File(COORDINATOR_JSON+"2"),null);
	
		TransactionContext.pop();

	}
	

}
