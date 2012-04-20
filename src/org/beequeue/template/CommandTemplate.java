package org.beequeue.template;


public class CommandTemplate {
	public FileGroovyTemplate[] files = null;
	public GroovyTemplate cmd;
	
	public CommandTemplate() {}

	public static CommandTemplate withText(String templateText) {
		CommandTemplate ct = new CommandTemplate();
		ct.cmd = new GroovyTemplate(templateText);
		return ct;
	}
	
	
}
