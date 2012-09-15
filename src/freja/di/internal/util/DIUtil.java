package freja.di.internal.util;

import java.io.File;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import freja.di.except.InstantiationFailureException;

/**
 * @author SiroKuro
 */
public class DIUtil {
	private static final boolean USE_SETACCESSIBLETRUE_DOPRIVILEGED = false;
	
	/**
	 * 渡されたクラスが実装するインタフェイスの集合を返す。
	 * クラスの親クラスが実装するインタフェイスも合わせて返す。 {@link #getSuperInterfaces(Class)}
	 * とは異なり、インタフェイスの継承関係もたどる。
	 * @param c 調査対象のクラスオブジェクト
	 * @return インタフェイスの集合
	 */
	public static Set<Class<?>> getAllSuperInterfaces(Class<?> c) {
		Set<Class<?>> result = new HashSet<Class<?>>();
		getSuperInterfaces(result, c, true);
		return result;
	}
	
	/**
	 * 渡されたクラスが実装するインタフェイスの集合を返す。
	 * クラスの親クラスが実装するインタフェイスも合わせて返す。
	 * @param c 調査対象のクラスオブジェクト
	 * @return インタフェイスの集合
	 */
	public static Set<Class<?>> getSuperInterfaces(Class<?> c) {
		Set<Class<?>> result = new HashSet<Class<?>>();
		getSuperInterfaces(result, c, false);
		return result;
	}
	
	/**
	 * クラス clazz の引数なしコンストラクタを呼び出し、インスタンスを作成します。
	 * @param clazz インスタンスを作成するクラスオブジェクト
	 * @return インスタンス
	 * @throws InstantiationFailureException インスタンスの作成に失敗した場合
	 */
	public static <T> T newInstance(Class<T> clazz) {
		assert clazz != null;
		try {
			Constructor<T> c = clazz.getDeclaredConstructor();
			setAccessibleTrue(c);
			return c.newInstance();
		}
		catch (NoSuchMethodException ex) {
			throw new InstantiationFailureException(ex,
					"インスタンス生成に失敗しました。{0} に引数なしのコンストラクタが見つかりません。インスタンス生成には引数なしのコンストラクタが必要です。", clazz);
		}
		catch (IllegalArgumentException ex) {
			throw new InstantiationFailureException(ex, "インスタンス生成に失敗しました。予期せぬ内部例外です。");
		}
		catch (IllegalAccessException ex) {
			throw new InstantiationFailureException(ex, "インスタンス生成に失敗しました。予期せぬ内部例外です。");
		}
		catch (InvocationTargetException ex) {
			throw new InstantiationFailureException(ex, "インスタンス生成に失敗しました。{0} のコンストラクタが例外をスローしました。", clazz);
		}
		catch (InstantiationException ex) {
			throw new InstantiationFailureException(ex, "インスタンス生成に失敗しました。{0} は抽象クラスです。", clazz);
		}
	}
	
	public static void setAccessibleTrue(final AccessibleObject object) {
		if (!object.isAccessible()) {
			if (USE_SETACCESSIBLETRUE_DOPRIVILEGED) {
				AccessController.doPrivileged(new PrivilegedAction<Object>() {
					@Override
					public Object run() {
						object.setAccessible(true);
						return null;
					}
				});
			}
			else {
				object.setAccessible(true);
			}
		}
	}
	
	public static String toAspectName(Class<?> clazz) {
		return "aspect:" + clazz.getName();
	}
	
	public static String toComponentName(Class<?> clazz) {
		return "class:" + clazz.getName();
	}
	
	public static File toFile(URL url) throws URISyntaxException, MalformedURLException {
		String protocol = url.getProtocol();
		if ("jar".equalsIgnoreCase(protocol)) {
			String jarPath = url.toExternalForm();
			int begin = "jar:".length();
			int end = jarPath.indexOf("!/");
			if (end < 0) {
				// 本来ここには到達しないらしい。jar プロトコルでは "!/" は必須の仕様？
				return new File(new URL(jarPath.substring(begin)).toURI());
			}
			else {
				return new File(new URL(jarPath.substring(begin, end)).toURI());
			}
		}
		else {
			return new File(url.toURI());
		}
	}
	
	private static void getSuperInterfaces(Collection<Class<?>> result, Class<?> c, boolean allInterfaces) {
		if (c != null) {
			getSuperInterfaces(result, c.getSuperclass(), allInterfaces);
			for (Class<?> _interfaces: c.getInterfaces()) {
				result.add(_interfaces);
				if (allInterfaces) {
					getSuperInterfaces(result, _interfaces, allInterfaces);
				}
			}
		}
	}
}
