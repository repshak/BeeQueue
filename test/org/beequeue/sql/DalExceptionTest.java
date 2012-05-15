package org.beequeue.sql;

import static org.junit.Assert.*;

import org.beequeue.coordinator.db.DbCoordinator;
import org.beequeue.util.BeeException;
import org.junit.Test;

public class DalExceptionTest {

	@Test
	public void test() {
		new BeeException("abc").withPayload(new DbCoordinator()).printStackTrace();
	}

}
