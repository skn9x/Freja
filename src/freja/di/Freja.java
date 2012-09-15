package freja.di;

import freja.di.config.annotation.AnnotationBinder;
import freja.di.config.annotation.AnnotationConfiguration;
import freja.di.internal.DefaultContainer;

/**
 * Freja DI フレームワークのファサードとなるクラスです。
 * コンテナを内部に持ち、コンポーネントを取得する get メソッドなどをサポートします。
 * 内部に保持するコンテナは init メソッドを用いて初期化することができます。
 * init メソッドを呼び出さずに他のメソッドを使用した場合は、
 * {@link AnnotationConfiguration#getDefaultSetting()} を用いて初期化を行います。
 * @author SiroKuro
 */
public class Freja {
	private static Container currentContainer = null;
	
	private Freja() {
		;
	}
	
	/**
	 * 指定したインタフェイスクラスに対応したコンポーネントを返します。
	 * @param clazz インタフェイスを表すクラスオブジェクト
	 * @return コンポーネントオブジェクト
	 * @throws IllegalArgumentException インタフェイス以外のクラスオブジェクトを渡した場合
	 */
	public static <T> T get(Class<T> clazz) {
		return prepareContainer().get(clazz);
	}
	
	/**
	 * 指定したコンポーネント名に対応したコンポーネントを返します。
	 * @param name コンポーネント名
	 * @return コンポーネントオブジェクト
	 */
	public static Object get(String name) {
		return prepareContainer().get(name);
	}
	
	/**
	 * このクラスが保持しているコンテナを返します。
	 * @return Container オブジェクト
	 */
	public static Container getContainer() {
		return prepareContainer();
	}
	
	/**
	 * コンテナを初期化します。
	 * @param initializer コンテナの初期化に用いる Configuration オブジェクト
	 */
	public static void init(Configuration initializer) {
		init(null, initializer);
	}
	
	/**
	 * コンテナを初期化します。
	 * @param container このクラスにセットするコンテナ
	 */
	public static void init(Container container, Configuration initializer) {
		currentContainer = initContainer(container, initializer);
	}
	
	/**
	 * 新しいコンテナを作成します。
	 * 作成されたコンテナは {@link AnnotationConfiguration#getDefaultSetting()}
	 * によって初期化されます。
	 * @return 新しいコンテナオブジェクト
	 */
	public static Container newContainer() {
		return initContainer(null, null);
	}
	
	private static Container initContainer(Container container, Configuration initializer) {
		container = container != null ? container : new DefaultContainer();
		initializer = initializer != null ? initializer : AnnotationConfiguration.getDefaultSetting();
		initializer.config(container);
		return container;
	}
	
	private static Container prepareContainer() {
		synchronized (Freja.class) {
			if (currentContainer == null) {
				currentContainer = newContainer();
			}
			return currentContainer;
		}
	}
	
	/**
	 * 指定のオブジェクトにDIを行います。
	 * @param obj DI対象のオブジェクト
	 */
	public static void bind(Object obj) {
		AnnotationBinder.bindObject(obj); // TODO 他のものも使いたい
	}
}
