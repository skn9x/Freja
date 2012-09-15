package freja.di;

import freja.di.except.InjectionFailureException;

/**
 * オブジェクトに対しバインディングを行うオブジェクトを表します。
 * @author SiroKuro
 */
public interface Binder {
	/**
	 * object に対しバインディングを行います。
	 * @param object バインディング先のオブジェクト
	 * @throws InjectionFailureException 依存性の解決に失敗した場合
	 */
	public void bind(Object object) throws InjectionFailureException;
}
