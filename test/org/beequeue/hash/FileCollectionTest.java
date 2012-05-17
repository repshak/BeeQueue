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
	public void tesToo() 
			throws JsonParseException, JsonMappingException, IOException {
		DbCoordinator c = DbCoordinatorTest.getCoordinator();
		TransactionContext.push();
		
		ContentTree push = c.push(new File(SEED_TREE),"config");
		System.out.println(push);
		String pathname = SEED_TREE.replaceAll("test/", "test-out/")+"Too";
		File destination = new File(pathname);
		System.out.println(c.sync(FileCollection.buildContentTree("config",destination),destination));
		System.out.println(c.sync(FileCollection.buildContentTree("config",destination),destination));
	
		TransactionContext.pop();

	}
	

}
