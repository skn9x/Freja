package freja.di.internal;

import java.lang.ref.SoftReference;
import freja.di.ComponentFactory;

/**
 * ソフト参照によりキャッシュを行う ComponentFactory です。
 * @author SiroKuro
 */
public class SoftReferenceComponentFactory extends AbstractCachedComponentFactory {
	private SoftReference<Object> cache = null;
	
	/**
	 * オブジェクトを初期化します。
	 * @param internal デコレート対象となる ComponentFactory オブジェクト
	 */
	public SoftReferenceComponentFactory(ComponentFactory internal) {
		super(internal);
	}
	
	@Override
	protected void clear() {
		cache = null;
	}
	
	@Override
	protected Object prepare() {
		Object result;
		if (cache == null || (result = cache.get()) == null) {
			cache = new SoftReference<Object>(result = internal.getImplementation());
		}
		return result;
	}
}
