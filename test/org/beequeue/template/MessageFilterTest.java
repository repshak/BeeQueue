package org.beequeue.template;

import static org.junit.Assert.*;

import org.beequeue.msg.BeeQueueMessage;
import org.junit.Test;

public class MessageFilterTest {

	@Test
	public void test() {
		assertEquals(true, new MessageFilter("true").evalFilter(null));
		assertEquals(false, new MessageFilter("false").evalFilter(null));
	    
		BeeQueueMessage bqm = new BeeQueueMessage().set("val", "5");
	    
		assertEquals(true, new MessageFilter("msg.val.toInteger() > 3").evalFilter(bqm));
	    assertEquals(false, new MessageFilter("Integer.valueOf(msg.val)/2 > 6").evalFilter(bqm));
	    assertEquals(false, new MessageFilter("int i = msg.val.toInteger();i/2 > 6").evalFilter(bqm));
	    assertEquals(false, new MessageFilter("msg.val > '6'").evalFilter(bqm));
	    assertEquals(true, new MessageFilter("msg.val == '5'").evalFilter(bqm));
	    assertEquals(true, new MessageFilter("msg.val != '3'").evalFilter(bqm));
	
	}

}
