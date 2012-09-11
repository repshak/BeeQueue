package org.beequeue.launcher;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class BeeQueueCommand {
	private static final String COMMANDS_PACKAGE = "org.beequeue.command.";
	protected static final String JAVA_CLASS_PATH = "java.class.path";

 	public static void runMain(String command, List<String> clientArgs){
	    try {
			ClassLoader classLoader = createClassLoader(BeeQueueHome.instance
					.getClassPathElements());
			Class<?> cls = classLoader.loadClass(COMMANDS_PACKAGE
					+ command);
			Method mainMethod = cls.getMethod("main",
					new Class[] { String[].class });
			Thread.currentThread().setContextClassLoader(classLoader);
			mainMethod.invoke(null, new Object[] { clientArgs
					.toArray(new String[clientArgs.size()]) });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	 }
	
	 private static String appendSeparator(String s, String suffix) {
		    return s.endsWith(suffix)? s : s + suffix;
     }
	 
	 private static ClassLoader createClassLoader(List<File> libraries) throws MalformedURLException {
	    String classPath = appendSeparator(System.getProperty (JAVA_CLASS_PATH), File.pathSeparator);
	    StringBuilder newClassPath = new StringBuilder();
	    URL[] libUrls = new URL[libraries.size()];
	    int i = 0 ;
	    for (File jar : libraries) {
	      if( i > 0 ) {
	        newClassPath.append (File.pathSeparator);
	      }
	      newClassPath.append (jar.getPath());
	      libUrls[i++] = jar.toURI().toURL();
	    }
	    newClassPath.insert(0, classPath);
	    System.setProperty (JAVA_CLASS_PATH, newClassPath.toString());
	    return new URLClassLoader (libUrls, ClassLoader.getSystemClassLoader());
	 }
}
