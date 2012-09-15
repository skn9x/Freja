package freja.di.internal.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import freja.internal.logging.Log;

/**
 * Freja で用いる {@link ClassLoader} です。
 * 指定されたクラス名に該当するクラスを、(親クラスローダではなく)
 * この ClassLoader を用いてロードすることを強制させることができます。
 * @author SiroKuro
 */
public class SpecializedClassLoader extends AbstractCustomClassLoader {
	/**
	 * この ClassLoader を用いてロードさせるクラス名
	 */
	private final String specializedClassName;
	
	protected SpecializedClassLoader(ClassLoader parent, String className) {
		super(parent);
		specializedClassName = className;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if (isSystemClass(name) || !specializedClassName.equals(name)) {
			// システムクラスとスワップ対象外のクラスは通常の読み込み
			return super.findClass(name);
		}
		
		// パッケージ定義
		definePackageIfAbsent(getPackageName(name));
		
		IllegalAccessError iaex = null;
		try {
			// 読み込み
			return defineClass(name);
		}
		catch (IllegalAccessError ex) {
			iaex = ex;
		}
		catch (ClassNotFoundException ex) {
			if (ex.getCause() instanceof IllegalAccessError) {
				iaex = (IllegalAccessError) ex.getCause();
			}
			else {
				throw ex;
			}
		}
		
		// 再ロード
		Class<?> result = super.findClass(name);
		Log.warning(iaex, "クラス {0} のロード中に IllegalAccessError が発生したため、親クラスローダでロードしました。", name);
		return result;
	}
	
	/**
	 * TemporaryClassLoader の新しいインスタンスを返します。
	 * @param parent 親となる ClassLoader
	 * @return TemporaryClassLoader オブジェクト
	 */
	public static SpecializedClassLoader newInstance(final ClassLoader parent, final String className) {
		return AccessController.doPrivileged(new PrivilegedAction<SpecializedClassLoader>() {
			@Override
			public SpecializedClassLoader run() {
				return new SpecializedClassLoader(parent, className);
			}
		});
	}
	
	/**
	 * TemporaryClassLoader の新しいインスタンスを返します。
	 * @return TemporaryClassLoader オブジェクト
	 */
	public static SpecializedClassLoader newInstance(String className) {
		return newInstance(SpecializedClassLoader.class.getClassLoader(), className);
	}
	
}
