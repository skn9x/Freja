package freja.di.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import freja.di.Container;

/**
 * AnnotationConfiguration �ɂ���ēo�^�����A�X�y�N�g�ɕt������A�m�e�[�V�����ł��B
 * @author SiroKuro
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {	// TODO interface �p�� name �p�ɕ�������
	/**
	 * �C���^�[�Z�v�^�̗D��x���w�肵�܂��B�l���傫���A�X�y�N�g���A����ɌĂяo����܂��B
	 * @return �D��x
	 */
	int priority() default 0;
	
	/**
	 * �C���^�[�Z�v�^�̃X�R�[�v���w�肵�܂��B
	 * @return �X�R�[�v��
	 */
	String scope() default Container.SCOPE_SINGLETON;
	
	/**
	 * �A�X�y�N�g�}����̃C���^�t�F�C�X�N���X���w�肵�܂��B
	 * @return �}����C���^�t�F�C�X�̔z��
	 */
	Class<?>[] targetInterface() default { };
	
	/**
	 * �A�X�y�N�g�}����̃R���|�[�l���g�����w�肵�܂��B
	 * @return �}����R���|�[�l���g���̔z��
	 */
	String[] targetName() default { };
}
