package freja.di.except;

import freja.di.FrejaRuntimeException;

/**
 * �ݒ蒆�ɑ��s�s�\�ȗ�O�������������Ƃ�\����O�ł��B
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
