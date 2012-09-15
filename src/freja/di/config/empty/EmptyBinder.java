package freja.di.config.empty;

import freja.di.Binder;

/**
 * 何も行わない Binder オブジェクトです。
 * @author SiroKuro
 */
public class EmptyBinder implements Binder {
	/**
	 * EmptyBinder のインスタンスです。
	 */
	public static final EmptyBinder INSTANCE = new EmptyBinder();
	
	protected EmptyBinder() {
		;
	}
	
	/**
	 * 何も行いません。
	 */
	@Override
	public void bind(Object object) {
		;
	}
	
}
