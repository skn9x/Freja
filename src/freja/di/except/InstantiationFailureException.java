package freja.di.except;

import freja.di.FrejaRuntimeException;

/**
 * �R���|�[�l���g�̃C���X�^���X�����s���ɃX���[�������s����O�ł��B
 * @author SiroKuro
 */
public class InstantiationFailureException extends FrejaRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4145716856146279979L;
	
	public InstantiationFailureException() {
		super();
	}
	
	public InstantiationFailureException(String message, Object... args) {
		super(message, args);
	}
	
	public InstantiationFailureException(Throwable cause, String message, Object... args) {
		super(cause, message, args);
	}
}
