package org.beequeue.template;

public class SpawnTemplate {

	private MessageTemplate messageTemplate;
	
	public MessageTemplate messageTemplate(){
		return messageTemplate;
	}
	
	public void init(MessageTemplate messageTemplate) {
		this.messageTemplate = messageTemplate;
		
	}

}
