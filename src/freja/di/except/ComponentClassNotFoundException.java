package freja.di.except;

import freja.di.FrejaRuntimeException;

/**
 * �R���|�[�l���g�N���X��������Ȃ��������ɃX���[�������s����O�ł��B
 * @author SiroKuro
 */
public class ComponentClassNotFoundException extends FrejaRuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2441225000464983736L;
	
	public ComponentClassNotFoundException() {
		super();
	}
	
	public ComponentClassNotFoundException(String message, Object... args) {
		super(message, args);
	}
	
	public ComponentClassNotFoundException(Throwable cause, String message, Object... args) {
		super(cause, message, args);
	}
	
}
