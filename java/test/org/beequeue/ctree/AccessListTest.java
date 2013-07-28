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
		Access a1 = Access.FULL_CONTROL;
		BuzzPath p1 = new BuzzPath("a","b");
		row.set(AccessList.UID, u1);
		row.set(AccessList.ACCESS, a1);
		row.set(AccessList.PATH, p1);
		row.addToTargetTable();
		StringWriter w = new StringWriter();
		accessList.writeTable(w);
		System.out.println(w.toString());
		UserId u2 = (UserId) accessList.getRow(0).get(AccessList.UID);
		Access a2 = (Access) accessList.getRow(0).get(AccessList.ACCESS);
		BuzzPath p2 = (BuzzPath)accessList.getRow(0).get(AccessList.PATH);
		Assert.assertEquals(u2, u1);
		Assert.assertEquals(a2, a1);
		Assert.assertEquals(p2, p1);
		AccessList al2 = AccessList.readTable(new StringReader(w.toString()));
		u2 = (UserId) al2.getRow(0).get(AccessList.UID);
		a2 = (Access) accessList.getRow(0).get(AccessList.ACCESS);
		p2 = (BuzzPath)accessList.getRow(0).get(AccessList.PATH);
		Assert.assertEquals(u2, u1);
		Assert.assertEquals(a2, a1);
		Assert.assertEquals(p2, p1);
	}

}
