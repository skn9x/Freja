package freja.di.config.annotation;

import java.lang.ref.SoftReference;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import freja.di.Configuration;
import freja.di.Container;
import freja.di.internal.config.annotation.AspectAnnotationRegister;
import freja.di.internal.config.annotation.ComponentAnnotationRegister;
import freja.di.internal.util.TemporaryClassLoader;
import freja.di.internal.util.classfinder.ClassFinder;
import freja.internal.logging.Log;

/**
 * アノテーションによって設定を行う Configuration です。
 * @author SiroKuro
 */
public class AnnotationConfiguration implements Configuration {
	private final ClassFinder finder = new ClassFinder();
	private final Set<ComponentRegister> registers = new CopyOnWriteArraySet<ComponentRegister>();
	private SoftReference<ClassLoader> loader = new SoftReference<ClassLoader>(null);
	
	/**
	 * クラスの探索を行うパスを追加します。
	 * @param classPath クラスパス
	 * @param includeJarFile フォルダ内に配置された JAR ファイルを読み込むか否か
	 */
	public void addClassPath(String classPath, boolean includeJarFile) {
		finder.addClassPath(classPath, includeJarFile);
	}
	
	/**
	 * {@link ComponentRegister} を追加します。
	 * @param register ComponentRegister オブジェクト
	 */
	public void addComponentRegister(ComponentRegister register) {
		registers.add(register);
	}
	
	/**
	 * デフォルトのクラスパスを探索先に追加します。
	 * 具体的には「java.ext.dirs」「java.class.path」に指定されたパスを追加します。
	 */
	public void addDefaultClassPath() {
		finder.addDefaultClassPath();
	}
	
	/**
	 * デフォルトの ComponentRegister を追加します。
	 * 具体的には Component アノテーションを処理する ComponentRegister と、
	 * Aspect アノテーションを処理する ComponentRegister を追加します。
	 */
	public void addDefaultComponentRegister() {
		addComponentRegister(new ComponentAnnotationRegister());
		addComponentRegister(new AspectAnnotationRegister());
	}
	
	/**
	 * コンテナを初期化します。
	 * @param container 初期化対象のコンテナオブジェクト
	 */
	@Override
	public void config(Container container) {
		Set<String> allClassName = finder.getAllClassName();
		for (ComponentRegister register: registers) {
			for (String className: allClassName) {
				Class<?> clazz = loadTemporaryClass(className);
				if (clazz != null) {
					register.visit(container, clazz);
				}
			}
		}
	}
	
	private Class<?> loadTemporaryClass(String name) {
		Class<?> result = null;
		try {
			result = prepareClassLoader().loadClass(name);
		}
		catch (NoClassDefFoundError ex) {
			Log.debug(ex, "exception caught. ignored loading class {0}.", name);
		}
		catch (ClassNotFoundException ex) {
			Log.debug(ex, "exception caught. ignoredd loading class {0}.", name);
		}
		return result;
	}
	
	private ClassLoader prepareClassLoader() {
		ClassLoader result = loader.get();
		if (result == null) {
			result = TemporaryClassLoader.newInstance();
			loader = new SoftReference<ClassLoader>(result);
		}
		return result;
	}
	
	/**
	 * AnnotationConfiguration を作成します。
	 * このコンストラクタで初期化されたオブジェクトは、
	 * 「java.ext.dirs」「java.class.path」に配置されたクラスファイルを読み込み、 {@link Component}
	 * アノテーションの付いたクラスを探して、コンテナへと登録します。
	 * また、{@link Aspect} アノテーションの付いたクラスを探して、コンテナへと登録します。
	 * @return 作成された AnnotationConfiguration オブジェクト
	 */
	public static AnnotationConfiguration getDefaultSetting() {
		AnnotationConfiguration result = new AnnotationConfiguration();
		result.addDefaultClassPath();
		result.addDefaultComponentRegister();
		return result;
	}
}
