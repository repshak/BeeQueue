/** ==== BEGIN LICENSE =====
   Copyright 2012 - BeeQueue.org

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
 *  ===== END LICENSE ====== */
package org.beequeue.template;

import java.io.IOException;

import junit.framework.Assert;

import org.beequeue.util.ToStringUtil;
import org.beequeue.util.deprecated.DataType;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class DomainTest {

	@Test
	public void testCase2() throws JsonParseException, JsonMappingException, IOException {
		DomainTemplate d = new DomainTemplate();
		d.properties.put("length", "10" );
		d.properties.put("width", "20" );
		d.messages = new EventTemplate[] {
				new EventTemplate(), 
				new EventTemplate(), 
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
		Assert.assertEquals(s,ToStringUtil.toString(ToStringUtil.toObject(s, DomainTemplate.class)));
		
//		System.out.println(s);
	}
	@Test
	public void testCase1() throws JsonParseException, JsonMappingException, IOException {
		DomainTemplate d = new DomainTemplate();
		d.properties.put("dp1", "10" );
		d.properties.put("dp2", "20" );
		d.messages = new EventTemplate[] {
			new EventTemplate(), 
		};
		EventTemplate msg = d.messages[0];
		msg.messageName="ReportRequest";
		msg.columns= new EventAttribute[] {
				new EventAttribute("ReportName",AttributeType.PARALLEL,DataType.STRING), 
				new EventAttribute("StartDate",AttributeType.SEQUENTIAL,DataType.DATE), 
				new EventAttribute("EndDdate",AttributeType.SEQUENTIAL,DataType.DATE) };
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
		Assert.assertEquals(s,ToStringUtil.toString(ToStringUtil.toObject(s, DomainTemplate.class)));
		
	}

}
