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
package org.beequeue.run;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.msg.BeeQueueRun;
import org.beequeue.msg.BeeQueueStage;
import org.beequeue.msg.RunState;
import org.beequeue.msg.StageState;
import org.beequeue.template.CommandTemplate;
import org.beequeue.template.GroovyTemplate;
import org.beequeue.util.BeeException;
import org.beequeue.util.ToStringUtil;
import org.beequeue.worker.Singletons;
import org.beequeue.worker.Worker;

public class RunHelper {
	
	public static final String BQ_RUN_ID = "BQ_RUN_ID";
	public static final String BQ_RUN_DIR = "BQ_RUN_DIR";

	private static final class Grabler extends Thread {
		private Process p;
		private Grabler(Process p) {
			this.p = p;
			this.setDaemon(true);
		}
		public void run() {
			try {
				InputStream in = p.getInputStream();
				while(in.read()!=-1);
				p.waitFor();
			} catch (Exception ignore) {}
			
		}
	}



	public BeeQueueStage stage;
	public BeeQueueRun run;
	
	public RunHelper(BeeQueueStage stage, Worker worker) {
		this.stage = stage;
		this.run = new BeeQueueRun();
		run.stageId = stage.stageId;
		run.workerId = worker.id;
		run.state = RunState.PREPARE_TO_RUN;
	}



	public CommandTemplate findOsSpecificTemplate() {
		return stage.stageTemplate().commands.get("os:*");
	}


	public Map<String, String> buildStageContext() {
		Map<String, String> vars = buildDomainConfig();
		vars.putAll(stage.job.message.parameters );
		return vars;
	}

	public Map<String, String> buildRunContext(File runDir, long id) {
		Map<String, String> vars = buildDomainConfig();
		vars.putAll(stage.job.message.parameters );
		vars.put(BQ_RUN_DIR, runDir.toString());
		vars.put(BQ_RUN_ID, String.valueOf(id));
		return vars;
	}



	public Map<String, String> buildDomainConfig() {
		Map<String, String> vars = BeeQueueHome.instance.getHomeVariables();
		vars.putAll(Singletons.getGlobalConfig().properties);
		vars.putAll(stage.job.message.domainTemplate().allProperties());
		return vars;
	}
	
	public void start() {
		try {
			run.cmd = findOsSpecificTemplate().cmd.generate(buildStageContext());
			File runDir = new File(BeeQueueHome.instance.getHost(),"runs/"+run.id);
			runDir.mkdirs();
			run.dir = runDir.toString();
			GroovyTemplate runShTemplate = new GroovyTemplate("classPath:/org/beequeue/run/run.sh.gt" );
			Map<String, String> context = buildRunContext(runDir,run.id);
			ProcessBuilder pb = new ProcessBuilder();
			File run_sh = new File(runDir, "run.sh");
			pb.command(run_sh.toString());
			pb.environment().putAll(context);
			pb.redirectErrorStream(true);
			context.put("CMD", run.cmd);
			runShTemplate.generate(context, run_sh);
			run_sh.setExecutable(true);
			Thread.sleep(300L);
			final Process p = pb.start();
			new Grabler(p).start();
			run.state = RunState.RUNNING;
			stage.newState = StageState.RUNNING;
		} catch (Exception e) {
			throw BeeException.cast(e);
		}
	}

}
