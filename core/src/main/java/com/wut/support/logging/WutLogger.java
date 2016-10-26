package com.wut.support.logging;

import java.util.logging.Logger;

public class WutLogger {
	Logger logger;
	
	public WutLogger(Class<?> c) {
		this.logger = Logger.getAnonymousLogger();
	}
	
	public static void stopAllLogging() {
//		Logger rootLogger = LogManager.getLogManager().getLogger("");
//		Handler[] handlers = rootLogger.getHandlers();
//		for (int i = 0; i < handlers.length; i++) {
//			rootLogger.removeHandler(handlers[i]);
//		}
		
		// NOT POSSIBLE WITH APP ENGINE
	}
	
	public void info(String msg) {
		logger.info(msg);
	}
	
	public void fatal(String msg) {
		logger.severe("#*#*#*#* FATAL ERROR #*#*#*#* " + msg);
	}
	
	public void error(String msg) {
		logger.severe(msg);
	}
	
	public void debug(String msg) {
		logger.fine(msg);
	}

	public static WutLogger create(Class<?> class1) {
		return new WutLogger(class1);
	}
}
