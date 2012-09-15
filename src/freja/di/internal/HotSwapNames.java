package freja.di.internal;

import freja.di.Binder;
import freja.di.internal.util.SpecializedClassLoader;

/**
 * ホットスワップに対応した Names オブジェクトです。
 * 主クラスは {@link DefaultContainer}
 * @author SiroKuro
 */
class HotSwapNames extends Names {
	
	public HotSwapNames(String className, Binder binder) {
		super(className, binder);
	}
	
	@Override
	protected ClassLoader getClassLoader() {
		return SpecializedClassLoader.newInstance(getClassName());
	}
	
}
