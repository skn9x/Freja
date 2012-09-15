package freja.di.internal;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import freja.di.Binder;
import freja.di.except.ComponentClassNotFoundException;
import freja.di.internal.util.DIUtil;
import freja.internal.logging.Log;

/**
 * コンポーネントのクラス名と Binder を管理します。
 * 主クラスは {@link DefaultContainer}
 * @author SiroKuro
 */
class Names {
	private final String className;
	private final Binder binder;
	
	public Names(String className, Binder binder) {
		assert className != null;
		assert binder != null;
		this.className = className;
		this.binder = binder;
	}
	
	/**
	 * @return the binder
	 */
	public Binder getBinder() {
		return binder;
	}
	
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	
	public File getFile() {
		URL url = getClassLoader().getResource(className.replace('.', '/').concat(".class"));
		if (url != null) {
			try {
				return DIUtil.toFile(url);
			}
			catch (MalformedURLException ex) {
				Log.debug(ex, "an unexpected error occurred.");
			}
			catch (URISyntaxException ex) {
				Log.debug(ex, "an unexpected error occurred.");
			}
		}
		return null;
	}
	
	public ComponentInitializer newInitializer() {
		try {
			return new ComponentInitializer(getClassLoader().loadClass(className), binder);
		}
		catch (ClassNotFoundException ex) {
			throw new ComponentClassNotFoundException(ex, "クラスが見つかりません。");
		}
	}
	
	protected ClassLoader getClassLoader() {
		return this.getClass().getClassLoader();
	}
	
}
