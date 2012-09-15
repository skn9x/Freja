package freja.di.except;

import freja.di.FrejaRuntimeException;

/**
 * 設定中に続行不能な例外が発生したことを表す例外です。
 * @author SiroKuro
 */
public class ConfigurationFailureException extends FrejaRuntimeException {
	
	/**
	 * 
	 */
	public ConfigurationFailureException() {
	}
	
	/**
	 * @param message
	 * @param args
	 */
	public ConfigurationFailureException(String message, Object... args) {
		super(message, args);
	}
	
	/**
	 * @param cause
	 * @param message
	 * @param args
	 */
	public ConfigurationFailureException(Throwable cause, String message, Object... args) {
		super(cause, message, args);
	}
	
}
