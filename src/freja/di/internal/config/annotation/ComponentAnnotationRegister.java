package freja.di.internal.config.annotation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import freja.di.Binder;
import freja.di.Container;
import freja.di.config.annotation.AnnotationBinder;
import freja.di.config.annotation.Component;
import freja.di.config.annotation.ComponentRegister;
import freja.di.internal.util.DIUtil;
import freja.etc.Util;

/**
 * @author SiroKuro
 */
public class ComponentAnnotationRegister implements ComponentRegister {
	public void visit(Container container, Class<?> clazz) {
		Component annot = getComponentAnnotation(clazz);
		if (annot != null) {
			int priority = annot.priority();
			String scope = annot.scope();
			String className = clazz.getName();
			Binder binder = new AnnotationBinder(container);
			
			// 指定されたコンポーネント名にて追加
			Set<String> names = new HashSet<String>();
			names.add(annot.value());
			names.addAll(Arrays.asList(annot.name()));
			names.addAll(createComponentNames(clazz));
			for (String name: names) {
				if (!Util.isEmpty(name)) {
					container.put(name, priority, scope, className, binder);
				}
			}
		}
	}
	
	private static Set<String> createComponentNames(Class<?> clazz) {
		Set<String> result = new TreeSet<String>();
		for (Class<?> interfaze: DIUtil.getAllSuperInterfaces(clazz)) {
			result.add(DIUtil.toComponentName(interfaze));
		}
		result.add(DIUtil.toComponentName(clazz));
		return result;
	}
	
	private static Component getComponentAnnotation(Class<?> clazz) {
		if (clazz == null || clazz.isInterface() || clazz.isAnnotation() || clazz.isArray() || clazz.isEnum() || clazz.isPrimitive()) {
			return null;
		}
		return clazz.getAnnotation(Component.class);
	}
}
