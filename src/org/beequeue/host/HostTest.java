package org.beequeue.host;

import org.beequeue.util.ToStringUtil;
import org.junit.Test;

public class HostTest {

	@Test
	public void test() {
		System.out.println(ToStringUtil.toString(Host.localHost()));
	}

}
