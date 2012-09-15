package freja.di.except;

import freja.di.FrejaRuntimeException;

/**
 * コンテキストが見つからなかったときにスローされる例外です。
 * @author SiroKuro
 */
public class ContextNotFoundException extends FrejaRuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3318687622791652182L;
	
	public ContextNotFoundException() {
		
	}
	
	public ContextNotFoundException(String message, Object... args) {
		super(message, args);
	}
	
	public ContextNotFoundException(Throwable cause, String message, Object... args) {
		super(cause, message, args);
	}
	
}
