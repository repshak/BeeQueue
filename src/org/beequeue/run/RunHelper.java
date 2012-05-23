package org.beequeue.run;

import java.util.Map;

import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.msg.BeeQueueRun;
import org.beequeue.msg.BeeQueueStage;
import org.beequeue.msg.RunState;
import org.beequeue.util.ToStringUtil;
import org.beequeue.worker.Singletons;
import org.beequeue.worker.Worker;
import org.beequeue.worker.WorkerData;

public class RunHelper {
	
	public BeeQueueStage stage;
	public BeeQueueRun run;
	
	public RunHelper(BeeQueueStage stage, Worker worker) {
		this.stage = stage;
		this.run = new BeeQueueRun();
		run.stageId = stage.stageId;
		run.workerId = worker.id;
		run.state = RunState.RUNNING;
		Map<String, String> vars = BeeQueueHome.instance.getHomeVariables();
		vars.putAll(Singletons.getGlobalConfig().properties);
		vars.putAll(stage.job.message.domainTemplate().properties);
		vars.putAll(stage.job.message.parameters );
		run.cmd = stage.stageTemplate().commands.get("os:*").cmd.generate(vars);
	}
	
	

	public BeeQueueRun start() {
		// TODO Auto-generated method stub
		return null;
	}

}
