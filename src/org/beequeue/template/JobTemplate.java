package org.beequeue.template;
/**
 * JobTemplate has many stages, they may depend on each other, but no circular dependencies allowed. 
 * 
 * @author sergeyk
 *
 */
public class JobTemplate {
	public String jobName;
	public StageTemplate[] stages;
	public MessageFilter filters[] = {new MessageFilter("true")};
}
