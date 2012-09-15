package freja.di.internal;

import java.lang.reflect.InvocationHandler;
import freja.di.ComponentFactory;

/**
 * ComponentFactory のデコレータのための抽象クラスです。
 * @author SiroKuro
 */
public abstract class AbstractWrappedComponentFactory implements ComponentFactory {
	/**
	 * internal デコレート対象となる ComponentFactory オブジェクト
	 */
	protected final ComponentFactory internal;
	
	/**
	 * オブジェクトを初期化します。
	 * @param internal デコレート対象となる ComponentFactory オブジェクト
	 */
	public AbstractWrappedComponentFactory(ComponentFactory internal) {
		assert internal != null;
		this.internal = internal;
	}
	
	/**
	 * internal.getLoadCount() への移譲メソッドです。
	 */
	@Override
	public int getVersion() {
		return internal.getVersion();
	}
	
	/**
	 * internal.getName() への移譲メソッドです。
	 */
	@Override
	public String getName() {
		return internal.getName();
	}
	
	/**
	 * internal.newProxyInstance(handler) への移譲メソッドです。
	 */
	@Override
	public Object newProxyInstance(InvocationHandler handler) {
		return internal.newProxyInstance(handler);
	}
}
