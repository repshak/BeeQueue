package org.beequeue.template;

public class JobTemplate {
	public String jobName;
	public String criticalStage;
	public StageTemplate[] stages;
	public GroovyFilter[] filters = null;
}
