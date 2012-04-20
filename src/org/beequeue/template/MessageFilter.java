package org.beequeue.template;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import org.beequeue.msg.BeeQueueMessage;

public class MessageFilter {
	public String expression;


	public MessageFilter() {}


	public MessageFilter(String expression) {
		this.expression = expression;
	}


	public boolean evalFilter(BeeQueueMessage msg){
		Binding binding = new Binding();
		binding.setVariable("msg", msg);
		GroovyShell shell = new GroovyShell(binding);
		Object value = shell.evaluate(expression);
		if( !(value instanceof Boolean) ){
			throw new RuntimeException("Boolean value expected out of expression: "+expression);
		}
		return (Boolean)value;
	}
}
