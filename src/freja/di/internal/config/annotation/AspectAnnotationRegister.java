package freja.di.internal.config.annotation;

import java.util.regex.Pattern;
import freja.di.Container;
import freja.di.config.annotation.AnnotationBinder;
import freja.di.config.annotation.Aspect;
import freja.di.config.annotation.ComponentRegister;
import freja.di.internal.util.DIUtil;
import freja.etc.Util;

/**
 * @author SiroKuro
 */
public class AspectAnnotationRegister implements ComponentRegister {
	public void visit(Container container, Class<?> clazz) {
		Aspect aspectAnnotation = getAspectAnnotation(clazz);
		if (aspectAnnotation != null) {
			String name = DIUtil.toAspectName(clazz);
			
			// アノテーションをコンテナに登録
			container.put(name, 0, aspectAnnotation.scope(), clazz.getName(), new AnnotationBinder(container));
			
			// コンポーネント名での割り当て
			for (String targetName: aspectAnnotation.targetName()) {
				if (!Util.isEmpty(targetName)) {
					container.aspect(Pattern.compile(targetName), name, aspectAnnotation.priority());
				}
			}
			// インタフェイスでの割り当て
			for (Class<?> interfaze: aspectAnnotation.targetInterface()) {
				container.aspect(interfaze, name, aspectAnnotation.priority());
			}
		}
	}
	
	private static Aspect getAspectAnnotation(Class<?> clazz) {
		if (clazz == null || clazz.isInterface() || clazz.isAnnotation() || clazz.isArray() || clazz.isEnum() || clazz.isPrimitive()) {
			return null;
		}
		return clazz.getAnnotation(Aspect.class);
	}
}
