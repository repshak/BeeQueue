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
import org.beequeue.template.CommandTemplate;
import org.beequeue.template.GroovyTemplate;
import org.beequeue.util.BeeException;
import org.beequeue.util.ToStringUtil;
import org.beequeue.worker.Singletons;
import org.beequeue.worker.Worker;

public class RunHelper {
	
	private static final class Grabler extends Thread {
		private Process p;

		private Grabler(Process p) {
			this.p = p;
			setDaemon(true);
		}

		public void run() {
			try {
				InputStream in = p.getInputStream();
				while(in.read()!=-1);
				p.waitFor();
			} catch (Exception ignore) {
			}
			
		}
	}



	public BeeQueueStage stage;
	public BeeQueueRun run;
	
	public RunHelper(BeeQueueStage stage, Worker worker) {
		this.stage = stage;
		this.run = new BeeQueueRun();
		run.stageId = stage.stageId;
		run.workerId = worker.id;
		run.state = RunState.RUNNING;
		run.cmd = findOsSpecificTemplate().cmd.generate(buildStageContext());
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
		vars.put("BQ_RUN_DIR", runDir.toString());
		vars.put("BQ_RUN_ID", String.valueOf(id));
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
			File runDir = new File(BeeQueueHome.instance.getHost(),"runs/"+run.id);
			runDir.mkdirs();
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
		} catch (Exception e) {
			throw BeeException.cast(e);
		}
	}

}
