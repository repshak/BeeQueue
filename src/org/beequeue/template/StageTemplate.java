package org.beequeue.template;

import java.util.Map;

public class StageTemplate {
	public String stageName;
	public String[] dependOnStage;
	public Map<String,CommandTemplate> commandTemplates;
	public RetryStrategy retryStrategy = new RetryStrategy();
}
