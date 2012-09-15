package freja.di.internal;

import freja.di.ComponentFactory;

/**
 * 強参照によりキャッシュを行う ComponentFactory です。
 * @author SiroKuro
 */
public class MemorizedComponentFactory extends AbstractCachedComponentFactory {
	private Object cache = null;
	
	/**
	 * オブジェクトを初期化します。
	 * @param internal デコレート対象となる ComponentFactory オブジェクト
	 */
	public MemorizedComponentFactory(ComponentFactory internal) {
		super(internal);
	}
	
	@Override
	protected void clear() {
		cache = null;
	}
	
	@Override
	protected Object prepare() {
		if (cache == null) {
			cache = internal.getImplementation();
		}
		return cache;
	}
	
}
