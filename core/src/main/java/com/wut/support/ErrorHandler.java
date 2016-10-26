package com.wut.support;

import java.util.logging.Logger;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.pipeline.WutRequest;
//import com.wut.support.logging.WutLogger;

public class ErrorHandler {
	private static Logger logger = Logger.getLogger("ErrorHandler");
	// 	private static WutLogger log = new WutLogger(MockRequestBuilder.class);

	// TODO rename to Type
	private enum LEVEL {
		FATAL, SERIOUS, EXTENAL_ERROR, INTERNAL_ERROR, ERROR, WARNING, INFO, USER, UNSPECIFIED, EXCEPTION, INTIALIZATION, DATA_LOSS, TRIVIAL
	};

	// TODO make throws uncaught (not needed to be caught) error
	private static Data generateError(LEVEL level, Object... obs) {
		Data returnData = ErrorData.GENERIC_ERROR;
		String msg = "Error Occured. Error type '" + level.name() + "'";
		System.out.println(msg);
		
		// deal with additional obs
		if (obs != null) {
			for (Object o : obs) {
				if (o instanceof String) {
					if (level == LEVEL.USER) {
						returnData = new MessageData(String.valueOf(o));
					}
					printError(String.valueOf(o));
				} else if (o instanceof Exception) {
					Exception e = (Exception) o;
					e.printStackTrace();
					System.out.println("NOT TIMEOUT:");
					throw new RuntimeException(e);
				} else if (o instanceof Data) {
					returnData = (Data) o;
				} else {
					printError(String.valueOf(o));
				}
			}
		}
		System.out.flush();
		System.err.flush();
		
		return returnData;
	}

//	private static Data generateError(Exception e, Object... obs) {
//		return generateError(LEVEL.EXCEPTION, e, obs);
//	}

	private static void printError(String message) {
		//System.err.println("ERROR:" + message);
		logger.severe("ERROR: " + message);
	}

	// TODO make the follow too functions the only error functions used
//	private static String error(WutRequest request, LEVEL type, Exception e) {
//		String errorId = "opps";
//		String stackTrace = Thread.currentThread().getStackTrace().toString();
//		errorId = UniqueIdGenerator.getId();
//		// TODO save error to some datasource
//		generateError(LEVEL.EXCEPTION, e, null);
//		return errorId;
//	}

//	private static String error(WutRequest request, LEVEL type, String msg) {
//		String errorId = "opps";
//		String stackTrace = Thread.currentThread().getStackTrace().toString();
//		errorId = UniqueIdGenerator.getId();
//		// TODO save error to some datasource
//		generateError(LEVEL.EXCEPTION, (java.lang.Object[]) null);
//		return errorId;
//	}

	
	// FATAL TO SYSTEM AS A WHOLE
	public static void fatalError(Exception e, String msg) {
		generateError(LEVEL.FATAL, e, msg);
		Language.forceExit(e, "FATAL ERROR:" + msg);
	}
	
	// SOMETHING HAPPENED THAT SHOULDNT HAVE
	public static void systemError(WutRequest request, String msg, Exception e) {
		generateError(LEVEL.SERIOUS, request, e, msg);
	}
	public static void systemError(String msg) {
		generateError(LEVEL.SERIOUS, msg);
	}
	public static void systemError(String msg, Exception e) {
		generateError(LEVEL.SERIOUS, msg);
	}
	public static void systemError(Exception e, String msg) { // refactor out later
		generateError(LEVEL.SERIOUS, msg);
	}

	public static void userError(WutRequest request, String msg, Exception e) {
		generateError(LEVEL.USER, request, e, msg);
	}
	public static void userError(WutRequest request, String msg) {
		generateError(LEVEL.USER, request, msg);
	}
	public static void userError(String msg) {
		generateError(LEVEL.USER, msg);
	}
	public static void userError(String msg, Exception e) {
		generateError(LEVEL.USER, msg);
	}

	public static void fatalError(String msg, Exception e) {
		generateError(LEVEL.FATAL, e, msg);
	}
	
	public static void fatalError(String msg) {
		generateError(LEVEL.FATAL, msg);
	}

	public static void dataLossError(String msg) {
		generateError(LEVEL.DATA_LOSS, msg);
	}
	public static void dataLossError(String msg, Exception e) {
		generateError(LEVEL.DATA_LOSS, msg);
	}

}
