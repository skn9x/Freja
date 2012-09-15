package freja.di.except;

import freja.di.FrejaRuntimeException;

/**
 * コンポーネントクラスが見つからなかった時にスローされる実行時例外です。
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
