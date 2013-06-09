package org.beequeue.util;

import junit.framework.Assert;

import org.junit.Test;

public class ToStringUtilTest {
	private static final String JSON_A1 = "{\"name\":\"a1\",\"desc\":\"A1 description\"}";
	private static final String JSON_A2 = "{\"name\":\"a2\",\"desc\":null}";
	private static final String YAML_A1 = "---\nname: \"a1\"\ndesc: \"A1 description\"\n";
	private static final String YAML_A2 = "---\nname: \"a2\"\ndesc: null\n";

	public static class A {
		public String name;
		public String desc;
	}

	public A makeA2() {
		A a2 = new A();
		a2.name = "a2";
		return a2;
	}
	
	public A makeA1() {
		A a1 = new A();
		a1.name = "a1";
		a1.desc = "A1 description";
		return a1;
	}

	@Test
	public void json() throws Exception {
		checkJson(makeA1(), JSON_A1);
		checkJson(makeA2(), JSON_A2);
	}

	public void checkJson(A a1, String json) {
		Assert.assertEquals(ToStringUtil.toNotPrettyString(a1), json);
		Assert.assertEquals(ToStringUtil.toNotPrettyString(ToStringUtil.toObject(json, A.class)),json);
	}

	@Test
	public void yaml() throws Exception {
		checkYaml(makeA1(), YAML_A1);
		checkYaml(makeA2(), YAML_A2);
	}

	public void checkYaml(A a, String yaml) {
		Assert.assertEquals(ToStringUtil.toYamlString(a), yaml);
		Assert.assertEquals(ToStringUtil.toYamlString(ToStringUtil.toYamlObject(yaml, A.class)),yaml);
	}

}
