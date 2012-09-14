package org.beequeue.launcher;

import java.lang.reflect.Method;
import java.util.List;

public class BeeQueueCommand {
	private static final String COMMANDS_PACKAGE = "org.beequeue.command.";
	protected static final String JAVA_CLASS_PATH = "java.class.path";

 	public static void runMain(String command, List<String> clientArgs){
	    try {
	    	BeeQueueHome.instance.setJavaLibraryPath();
			Class<?> cls = Class.forName(COMMANDS_PACKAGE + command);
			Method mainMethod = cls.getMethod("main", new Class[] { String[].class });
			mainMethod.invoke(null, new Object[] { clientArgs
					.toArray(new String[clientArgs.size()]) });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	 }
	
}
