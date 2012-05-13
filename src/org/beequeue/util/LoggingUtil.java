package org.beequeue.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggingUtil {
	public static  ConsoleHandler setLevelForConsoleHandler(Level level) {
		Logger topLogger = java.util.logging.Logger.getLogger("");
		topLogger.setLevel(Level.ALL);
		ConsoleHandler consoleHandler = null;
	    for (Handler handler : topLogger.getHandlers()) {
	        if (handler instanceof ConsoleHandler) {
	            consoleHandler = (ConsoleHandler) handler;
	            break;
	        }
	    }
	    if (consoleHandler == null) {
	        consoleHandler = new ConsoleHandler();
	        topLogger.addHandler(consoleHandler);
	    }
	    consoleHandler.setLevel(level);
		return consoleHandler;
	}

	public static void setSimpleFormatter(ConsoleHandler ch) {
		ch.setFormatter(new Formatter() {
			@Override
			public String format(LogRecord r) {
				String date = new SimpleDateFormat("yyyy-MM-dd hh:mm.ss ").format(new Date(r.getMillis()));
				return date + r.getLevel() + " " + 
					r.getLoggerName()+"#"+ r.getSourceMethodName()+" "+
					r.getMessage()+"\n"; 
			}
		});
	}

}
