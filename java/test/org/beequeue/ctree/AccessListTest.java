package org.beequeue.ctree;

import java.io.StringReader;
import java.io.StringWriter;

import org.beequeue.buzz.BuzzPath;
import org.beequeue.json.BuzzRow;
import org.junit.Assert;
import org.junit.Test;

public class AccessListTest {

	@Test
	public void test() {
		AccessList accessList = new AccessList();
		BuzzRow row = accessList.newRow();
		UserId u1 = new UserId("user1");
		row.set(AccessList.UID, u1);
		row.set(AccessList.ACCESS, Access.FULL_CONTROL);
		row.set(AccessList.PATH, new BuzzPath("a","b"));
		row.addToTargetTable();
		StringWriter w = new StringWriter();
		accessList.writeTable(w);
		System.out.println(w.toString());
		UserId u2 = (UserId) accessList.getRow(0).get(AccessList.UID);
		Assert.assertEquals(u2, u1);
		AccessList al2 = AccessList.readTable(new StringReader(w.toString()));
		u2 = (UserId) al2.getRow(0).get(AccessList.UID);
		Assert.assertEquals(u2, u1);
	}

}
