package freja.di.internal;

import java.lang.reflect.InvocationHandler;
import freja.di.Binder;
import freja.di.ComponentFactory;
import freja.di.except.InjectionFailureException;

/**
 * コンポーネントを新規作成する ComponentFactory です。
 * @author SiroKuro
 */
public class NewComponentFactory implements ComponentFactory {
	/**
	 * 内部で管理される Names オブジェクト
	 */
	protected final Names names;
	private volatile ComponentInitializer cache = null;
	private volatile int version = 0;
	
	/**
	 * オブジェクトを初期化します。
	 * @param clazz コンポーネントの実装クラス名
	 * @param binder コンポーネントの初期化に用いる Binder オブジェクト
	 */
	public NewComponentFactory(String clazz, Binder binder) {
		this(new Names(clazz, binder));
	}
	
	/**
	 * オブジェクトを初期化します。
	 * @param names コンポーネント情報を持った Names オブジェクト
	 */
	protected NewComponentFactory(Names names) {
		assert names != null;
		this.names = names;
	}
	
	public ComponentInitializer getCache() {
		return cache;
	}
	
	@Override
	public Object getImplementation() throws InjectionFailureException {
		return prepareClasses().newCoreObject();
	}
	
	@Override
	public int getVersion() {
		return version;
	}
	
	@Override
	public String getName() {
		return names.getClassName();
	}
	
	@Override
	public Object newProxyInstance(InvocationHandler handler) {
		return prepareClasses().newProxyInstance(handler);
	}
	
	protected ComponentInitializer prepareClasses() {
		synchronized (this) {
			if (cache == null) {
				setCache(names.newInitializer());
			}
			return this.cache;
		}
	}
	
	protected void setCache(ComponentInitializer cache) {
		synchronized (this) {
			this.cache = cache;
			version++;
		}
	}
}
