package org.beequeue.template;

import java.io.IOException;

import org.beequeue.piles.LazyMap;
import org.codehaus.groovy.control.CompilationFailedException;
import org.junit.Test;

public class GroovyTemplateTest {

	@Test
	public void test() throws CompilationFailedException, ClassNotFoundException, IOException {
		String z[] = { "Float" };
		for (int i = 0; i < z.length; i++) {
			String v = z[i];
			
			System.out.println(
					new GroovyTemplate("classPath:/org/beequeue/template/DataType.gt")
					.generate(new LazyMap<String, Object>()
							.add("T", v)
							.add("U", v.toUpperCase())
							));
			
		}
	}

}
