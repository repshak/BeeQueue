package org.beequeue.template;

import java.io.IOException;

import junit.framework.Assert;

import org.beequeue.util.ToStringUtil;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class DomainTest {

	@Test
	public void test() throws JsonParseException, JsonMappingException, IOException {
		Domain d = new Domain();
		d.domainName = "test1" ;
		d.properties.put("root", "/home/dir/");
		d.properties.put("length", "10" );
		d.properties.put("width", "20" );
		d.messages = new MessageTemplate[] {
			new MessageTemplate(), 
			new MessageTemplate()	
		};
		d.messages[0].messageName="waterLevel";
		d.messages[0].jobs = new JobTemplate[]{
				new JobTemplate()
		};
		d.messages[0].jobs[0].jobName = "measureSalinity";
		
		d.messages[1].messageName="processByWidth";
		d.messages[1].jobs = new JobTemplate[]{
				new JobTemplate(),
				new JobTemplate()
		};
		
		String s = ToStringUtil.toString(d);
		Assert.assertEquals(s,ToStringUtil.toString(ToStringUtil.toObject(s, Domain.class)));
		
		System.out.println(s);
	}

}
