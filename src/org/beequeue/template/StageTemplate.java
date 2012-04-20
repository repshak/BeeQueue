package org.beequeue.template;

import java.util.LinkedHashMap;
import java.util.Map;

public class StageTemplate {
	public String stageName;
	public String[] dependOnStage;
	public Map<String,CommandTemplate> commands = new LinkedHashMap<String, CommandTemplate>();
	public RetryStrategy retryStrategy = new RetryStrategy();
	
	public StageTemplate command(String key, CommandTemplate cmd){
		commands.put(key, cmd);
		return this;
	}
}
