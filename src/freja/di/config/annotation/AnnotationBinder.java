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
 * �A�m�e�[�V�����ɂ���ăo�C���f�B���O���s�� Binder �ł��B
 * �n���ꂽ�I�u�W�F�N�g�� {@link Bind} �A�m�e�[�V�����̕t�����t�B�[���h�ɁA�o�C���f�B���O���s���܂��B
 * @author SiroKuro
 */
public class AnnotationBinder implements Binder {
	private final Container container;
	
	private final Map<Class<?>, Dependency[]> cache = new WeakHashMap<Class<?>, Dependency[]>();
	
	/**
	 * �I�u�W�F�N�g�����������܂��B
	 * @param container �o�C���f�B���O���ɗp����R���e�i�I�u�W�F�N�g
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
				throw new InjectionFailureException(ex, "�R���|�[�l���g {0} ���t�B�[���h {1} �ɒ����ł��܂���ł����B", d.name, d.f);
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
				throw new InjectionFailureException("�t�B�[���h {0} �ɂ͒����ł��܂���Bfinal �ȃt�B�[���h�ւ̃o�C���h�̓T�|�[�g����܂���B", f);
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
	 * �I�u�W�F�N�g�ɁA�w��̃R���e�i��p���ăo�C���f�B���O���s���܂��B
	 * @param container �o�C���f�B���O�ɗp����R���e�i
	 * @param object �o�C���h��̃I�u�W�F�N�g
	 */
	public static void bindObject(Container container, Object object) {
		new AnnotationBinder(container).bind(object);
	}
	
	/**
	 * �I�u�W�F�N�g�ɁAFreja.getContainer() �̃R���e�i��p���ăo�C���f�B���O���s���܂��B
	 * @param object �o�C���h��̃I�u�W�F�N�g
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
