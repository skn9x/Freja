package freja.di.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collection;
import freja.di.Binder;
import freja.di.internal.util.DIUtil;

/**
 * コンポーネントの初期化に用いる情報を格納します。
 * 主クラスは {@link NewComponentFactory}
 * @author SiroKuro
 */
class ComponentInitializer {
	private final Class<?> component;
	private final Binder binder;
	private final Class<?>[] interfaces;
	
	/**
	 * オブジェクトを初期化します。
	 * @param component コンポーネントの実クラス
	 * @param binder コンポーネントの DI に用いる Binder オブジェクト
	 */
	public ComponentInitializer(Class<?> component, Binder binder) {
		assert component != null;
		assert binder != null;
		
		this.component = component;
		this.binder = binder;
		
		Collection<Class<?>> interfaces = DIUtil.getSuperInterfaces(component);
		this.interfaces = interfaces.toArray(new Class<?>[interfaces.size()]);
	}
	
	/**
	 * コンポーネントに対する DI に用いる Binder オブジェクトを返します。
	 * @return Binder オブジェクト
	 */
	public Binder getBinder() {
		return binder;
	}
	
	/**
	 * コンポーネントの実クラスを返します。
	 * @return コンポーネントの実クラス
	 */
	public Class<?> getComponentClass() {
		return component;
	}
	
	/**
	 * 新しいコンポーネントの実インスタンスを返します。
	 * また、同時に Binder によるバインディングも行ないます。
	 * @return 作成されたコンポーネントのインスタンス
	 */
	public Object newCoreObject() {
		Object result = DIUtil.newInstance(component);
		binder.bind(result);
		return result;
	}
	
	public Object newProxyInstance(InvocationHandler handler) {
		return Proxy.newProxyInstance(component.getClassLoader(), interfaces, handler);
	}
}
