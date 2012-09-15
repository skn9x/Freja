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
			
			// �A�m�e�[�V�������R���e�i�ɓo�^
			container.put(name, 0, aspectAnnotation.scope(), clazz.getName(), new AnnotationBinder(container));
			
			// �R���|�[�l���g���ł̊��蓖��
			for (String targetName: aspectAnnotation.targetName()) {
				if (!Util.isEmpty(targetName)) {
					container.aspect(Pattern.compile(targetName), name, aspectAnnotation.priority());
				}
			}
			// �C���^�t�F�C�X�ł̊��蓖��
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
