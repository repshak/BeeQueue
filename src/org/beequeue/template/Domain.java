package org.beequeue.template;

import java.util.LinkedHashMap;
import java.util.Map;

public class Domain {
	public String domainName;
	public MessageTemplate[] messages;
	public Map<String,String> properties = new LinkedHashMap<String, String>();
}
