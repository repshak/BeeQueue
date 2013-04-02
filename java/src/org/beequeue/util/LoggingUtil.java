/** ==== BEGIN LICENSE =====
   Copyright 2012 - BeeQueue.org

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
 *  ===== END LICENSE ====== */
package org.beequeue.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.beequeue.msg.BeeQueueJob;
import org.beequeue.msg.BeeQueueStage;

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

	public static void warn(String msg, Object ... suplimental) {
		// TODO think about how to display warning like this.
		
	}

}
