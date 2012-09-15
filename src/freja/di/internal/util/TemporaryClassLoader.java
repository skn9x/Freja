package freja.di.internal.util;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * 一時的なクラス読み込みに用いる {@link ClassLoader} です。
 * 全てのクラスを、(親クラスローダではなく)この ClassLoader を用いてロードすることを強制させることができます。
 * @author SiroKuro
 */
public class TemporaryClassLoader extends AbstractCustomClassLoader {
	protected TemporaryClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if (!isSystemClass(name)) {
			definePackageIfAbsent(getPackageName(name));
			return defineClass(name);
		}
		return super.findClass(name);
	}
	
	/**
	 * TemporaryClassLoader の新しいインスタンスを返します。
	 * @return TemporaryClassLoader オブジェクト
	 */
	public static TemporaryClassLoader newInstance() {
		return newInstance(TemporaryClassLoader.class.getClassLoader());
	}
	
	/**
	 * TemporaryClassLoader の新しいインスタンスを返します。
	 * @param parent 親となる ClassLoader
	 * @return TemporaryClassLoader オブジェクト
	 */
	public static TemporaryClassLoader newInstance(final ClassLoader parent) {
		return AccessController.doPrivileged(new PrivilegedAction<TemporaryClassLoader>() {
			@Override
			public TemporaryClassLoader run() {
				return new TemporaryClassLoader(parent);
			}
		});
	}
}
