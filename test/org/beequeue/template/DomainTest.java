package org.beequeue.template;

import java.io.IOException;

import junit.framework.Assert;

import org.beequeue.util.ToStringUtil;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class DomainTest {

	@Test
	public void testCase2() throws JsonParseException, JsonMappingException, IOException {
		Domain d = new Domain();
		d.domainName = "test1" ;
		d.properties.put("length", "10" );
		d.properties.put("width", "20" );
		d.messages = new MessageTemplate[] {
				new MessageTemplate(), 
				new MessageTemplate(), 
		};
		d.messages[0].messageName="";
		d.messages[0].jobs = new JobTemplate[]{
				new JobTemplate()
		};
		d.messages[0].jobs[0].jobName = "measureSalinity";
		
		d.messages[1].messageName="chlorinLevel";
		d.messages[1].jobs = new JobTemplate[]{
				new JobTemplate(),
				new JobTemplate()
		};
		
		String s = ToStringUtil.toString(d);
		Assert.assertEquals(s,ToStringUtil.toString(ToStringUtil.toObject(s, Domain.class)));
		
//		System.out.println(s);
	}
	@Test
	public void testCase1() throws JsonParseException, JsonMappingException, IOException {
		Domain d = new Domain();
		d.domainName = "tc1-Balance" ;
		d.properties.put("dp1", "10" );
		d.properties.put("dp2", "20" );
		d.messages = new MessageTemplate[] {
			new MessageTemplate(), 
		};
		MessageTemplate msg = d.messages[0];
		msg.messageName="ReportRequest";
		msg.columns= new String[] {"ReportName", "StartDate", "EndDdate" };
		msg.jobs = new JobTemplate[]{
				new JobTemplate()
		};
		JobTemplate job = msg.jobs[0];
		job.jobName = "report";
		job.stages = new StageTemplate[] {
				new StageTemplate(),
		};
		StageTemplate stage = job.stages[0];
		stage.stageName = "run" ;
		stage.command("os:windows", CommandTemplate.withText("$reportRoot/bin/report.bat $ReportName $StartDate $EndDate"));
		stage.command("os:*", CommandTemplate.withText("$reportRoot/bin/report.sh $ReportName $StartDate $EndDate"));
		
		
		String s = ToStringUtil.toString(d);
		System.out.println(s);
		Assert.assertEquals(s,ToStringUtil.toString(ToStringUtil.toObject(s, Domain.class)));
		
	}

}
