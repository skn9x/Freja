package freja.di.internal;

import freja.di.ComponentFactory;

/**
 * コンポーネント実装インスタンスを内部にキャッシュする ComponentFactory のための抽象クラスです。
 * @author SiroKuro
 */
public abstract class AbstractCachedComponentFactory extends AbstractWrappedComponentFactory {
	private int oldLoadCount = -1;
	
	/**
	 * オブジェクトを初期化します。
	 * @param internal デコレート対象となる ComponentFactory オブジェクト
	 */
	public AbstractCachedComponentFactory(ComponentFactory internal) {
		super(internal);
	}
	
	/**
	 * internal がクリアされたかどうかを判断し、clearCache() または getCacheOrCreate() を呼び出します。
	 */
	@Override
	public Object getImplementation() {
		synchronized (this) {
			// oldLoadCount が加算されていれば、cache をクリアする
			int lc = internal.getVersion();
			if (oldLoadCount < lc) {
				oldLoadCount = lc;
				clear();
			}
			return prepare();
		}
	}
	
	/**
	 * キャッシュをクリアします。
	 */
	protected abstract void clear();
	
	/**
	 * キャッシュから取得した値か、または internal.getImplementation() を呼び出して取得した値を返します。
	 * @return キャッシュされたコンポーネント実装インスタンス
	 */
	protected abstract Object prepare();
}
