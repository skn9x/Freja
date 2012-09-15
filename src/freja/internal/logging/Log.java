package freja.internal.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author SiroKuro
 */
public class Log {
	private static final String FREJA_PACKAGE_PREFIX = "freja.";
	private static final Logger logger = Logger.getLogger("freja");
	
	public static void config(String msg, Object... params) {
		log(Level.CONFIG, msg, null, params);
	}
	
	public static void debug(String msg, Object... params) {
		log(Level.FINE, msg, null, params);
	}
	
	public static void debug(Throwable th, String msg, Object... params) {
		log(Level.FINE, msg, th, params);
	}
	
	public static void error(String msg, Object... params) {
		log(Level.SEVERE, msg, null, params);
	}
	
	public static void error(Throwable th, String msg, Object... params) {
		log(Level.SEVERE, msg, th, params);
	}
	
	public static void info(String msg, Object... params) {
		log(Level.INFO, msg, null, params);
	}
	
	public static void trace(String msg, Object... params) {
		log(Level.FINER, msg, null, params);
	}
	
	public static void warning(String msg, Object... params) {
		log(Level.WARNING, msg, null, params);
	}
	
	public static void warning(Throwable th, String msg, Object... params) {
		log(Level.WARNING, msg, th, params);
	}
	
	private static void log(Level level, String msg, Throwable th, Object[] params) {
		if (logger.isLoggable(level)) {
			String sourceClassName = "";
			String sourceMethodName = "";
			for (StackTraceElement st: Thread.currentThread().getStackTrace()) {
				String cn = st.getClassName();
				if (cn.startsWith(FREJA_PACKAGE_PREFIX) && !Log.class.getName().equals(cn)) {
					sourceClassName = cn;
					sourceMethodName = st.getMethodName();
					break;
				}
			}
			
			LogRecord record = new LogRecord(level, msg);
			record.setLoggerName(logger.getName());
			record.setMillis(System.currentTimeMillis());
			record.setParameters(params);
			record.setSourceClassName(sourceClassName);
			record.setSourceMethodName(sourceMethodName);
			record.setThrown(th);
			
			logger.log(record);
		}
	}
}
