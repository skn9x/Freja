package freja.di.config.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import freja.di.Binder;
import freja.di.Container;
import freja.di.Freja;
import freja.di.except.InjectionFailureException;
import freja.di.internal.util.DIUtil;
import freja.etc.Util;
import freja.internal.logging.Log;

/**
 * アノテーションによってバインディングを行う Binder です。
 * 渡されたオブジェクトの {@link Bind} アノテーションの付いたフィールドに、バインディングを行います。
 * @author SiroKuro
 */
public class AnnotationBinder implements Binder {
	private final Container container;
	
	private final Map<Class<?>, Dependency[]> cache = new WeakHashMap<Class<?>, Dependency[]>();
	
	/**
	 * オブジェクトを初期化します。
	 * @param container バインディング時に用いるコンテナオブジェクト
	 */
	public AnnotationBinder(Container container) {
		assert container != null;
		this.container = container;
	}
	
	@Override
	public void bind(Object object) {
		for (Dependency d: prepareDependency(object.getClass())) {
			Object component = container.get(d.name);
			try {
				d.f.set(object, component);
			}
			catch (IllegalAccessException ex) {
				throw new InjectionFailureException(ex, "コンポーネント {0} をフィールド {1} に注入できませんでした。", d.name, d.f);
			}
		}
	}
	
	private Dependency newDependency(Field f) {
		Bind lazy = f.getAnnotation(Bind.class);
		if (lazy == null) {
			return null;
		}
		else {
			Class<?> type = f.getType();
			String name = lazy.value();
			if (Util.isEmpty(name)) {
				name = DIUtil.toComponentName(type);
			}
			
			Log.trace("injection [{0}] to [{1}]", name, f);
			
			int modifier = f.getModifiers();
			if (Modifier.isFinal(modifier)) {
				throw new InjectionFailureException("フィールド {0} には注入できません。final なフィールドへのバインドはサポートされません。", f);
			}
			DIUtil.setAccessibleTrue(f);
			return new Dependency(f, name);
		}
	}
	
	private Dependency[] prepareDependency(Class<?> clazz) {
		Dependency[] result = cache.get(clazz);
		if (result == null) {
			List<Dependency> deps = new ArrayList<Dependency>();
			for (Field f: clazz.getDeclaredFields()) {
				Dependency d = newDependency(f);
				if (d != null) {
					deps.add(d);
				}
			}
			cache.put(clazz, result = deps.toArray(new Dependency[deps.size()]));
		}
		return result;
	}
	
	/**
	 * オブジェクトに、指定のコンテナを用いてバインディングを行います。
	 * @param container バインディングに用いるコンテナ
	 * @param object バインド先のオブジェクト
	 */
	public static void bindObject(Container container, Object object) {
		new AnnotationBinder(container).bind(object);
	}
	
	/**
	 * オブジェクトに、Freja.getContainer() のコンテナを用いてバインディングを行います。
	 * @param object バインド先のオブジェクト
	 */
	public static void bindObject(Object object) {
		bindObject(Freja.getContainer(), object);
	}
	
	private static class Dependency {
		public final Field f;
		public final String name;
		
		public Dependency(Field f, String name) {
			this.f = f;
			this.name = name;
		}
	}
}
