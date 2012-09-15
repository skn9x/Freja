package freja.di.config.multiple;

import freja.di.Binder;
import freja.di.except.InjectionFailureException;

/**
 * 複数の {@link Binder} を適用することのできる Binder オブジェクトです。
 * @author SiroKuro
 */
public class MultipleBinder implements Binder {
	private final Binder[] binders;
	
	/**
	 * オブジェクトを初期化します。引数に指定された Binder が順に使用されます。
	 * @param binders 適用する Binder の配列
	 */
	public MultipleBinder(Binder... binders) {
		assert binders != null;
		this.binders = binders.clone();
	}
	
	@Override
	public void bind(Object object) throws InjectionFailureException {
		for (Binder binder: binders) {
			binder.bind(object);
		}
	}
	
}
