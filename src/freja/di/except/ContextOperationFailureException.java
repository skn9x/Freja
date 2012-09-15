package freja.di.except;

import freja.di.FrejaRuntimeException;

/**
 * �R���e�L�X�g�ɑ΂��鑀�삪���s�������ɃX���[������O�ł��B
 * @author SiroKuro
 */
public class ContextOperationFailureException extends FrejaRuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5282538160176150076L;
	
	public ContextOperationFailureException() {
	}
	
	public ContextOperationFailureException(String message, Object... args) {
		super(message, args);
	}
	
	public ContextOperationFailureException(Throwable cause, String message, Object... args) {
		super(cause, message, args);
	}
	
}
