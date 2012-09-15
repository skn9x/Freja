package freja.di.config.annotation;

import freja.di.Container;

/**
 * {@link AnnotationConfiguration} が探索したクラスに対し、
 * 実際にコンテナへの登録を行うインタフェイスです。
 * @author SiroKuro
 */
public interface ComponentRegister {
	/**
	 * クラスを訪問します。クラスがコンポーネントならば、コンテナへ登録します。
	 * @param container 登録先コンテナ
	 * @param clazz 訪問したクラスオブジェクト
	 */
	public void visit(Container container, Class<?> clazz);
}
