package freja.di.except;

import freja.di.FrejaRuntimeException;

/**
 * �C���W�F�N�V�������s���ɃX���[�������s����O�ł��B
 * @author SiroKuro
 */
public class InjectionFailureException extends FrejaRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6606894194153899902L;
	
	public InjectionFailureException() {
		;
	}
	
	public InjectionFailureException(String message, Object... args) {
		super(message, args);
	}
	
	public InjectionFailureException(Throwable cause, String message, Object... args) {
		super(cause, message, args);
	}
	
}
