package freja.di.internal;

import java.io.File;
import freja.di.Binder;
import freja.di.except.ComponentClassNotFoundException;
import freja.di.except.InjectionFailureException;
import freja.di.internal.hotswap.FileModifiedEventListener;
import freja.di.internal.hotswap.HotSwapService;
import freja.internal.logging.Log;

/**
 * ホットスワップに対応した NewComponentFactory です。
 * @author SiroKuro
 */
public class HotSwapComponentFactory extends NewComponentFactory {
	
	/**
	 * オブジェクトを初期化します。
	 * @param clazz コンポーネントの実装クラス名
	 * @param binder コンポーネントの初期化に用いる Binder オブジェクト
	 */
	public HotSwapComponentFactory(String clazz, Binder binder) {
		super(new HotSwapNames(clazz, binder));
	}
	
	/**
	 * ホットスワップを実行します。
	 * @return ホットスワップできたならば true / そうでないならば false
	 */
	public boolean doHotSwap() {
		try {
			ComponentInitializer new_cache = names.newInitializer();
			
			// バインドを試してみる
			// InjectionFailureException が投げられた時にはホットスワップを中止する
			new_cache.newCoreObject();
			
			// 前回のクラスと同じものが得られた場合は警告する
			ComponentInitializer cache = getCache();
			if (cache != null && new_cache.getComponentClass() == cache.getComponentClass()) {
				Log.warning("ホットスワップを予期していましたが、前回ロード時と同じクラス {0} が得られました。", new_cache.getComponentClass());
			}
			
			// ホットスワップ
			setCache(new_cache);
			
			return true;
		}
		catch (ComponentClassNotFoundException ex) {
			Log.warning(ex, "クラス {0} が見つかりません。ホットスワップを中止しました。", names.getClassName());
			return false;
		}
		catch (InjectionFailureException ex) {
			Log.warning(ex, "クラス {0} のバインディングに失敗しました。ホットスワップを中止しました。", names.getClassName());
			return false;
		}
	}
	
	/**
	 * HotSwapService オブジェクトにこのオブジェクトを登録します。
	 * @param agent HotSwapService インスタンス
	 */
	public void registerTo(HotSwapService agent) {
		File file = names.getFile();
		if (file != null) {
			agent.addFileModifiedListener(file, new FileModifiedEventListener() {
				@Override
				public void onFileModified(Object sender, File file) {
					doHotSwap();
				}
			});
		}
	}
}
