package freja.di.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import freja.di.Container;

/**
 * AnnotationConfiguration �ɂ���ēo�^�����R���|�[�l���g�ɕt������A�m�e�[�V�����ł��B
 * @author SiroKuro
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
	/**
	 * ���̃R���|�[�l���g�ɕt���閼�O���`���܂��B
	 * @return �R���|�[�l���g���̔z��
	 */
	String[] name() default { };
	
	/**
	 * ���̃R���|�[�l���g�̗D��x���w�肵�܂��B
	 * @return �D��x
	 */
	int priority() default 0;
	
	/**
	 * ���̃R���|�[�l���g�̃X�R�[�v���w�肵�܂��B
	 * @return �X�R�[�v��\��������
	 * @see Container#SCOPE_SINGLETON
	 * @see Container#SCOPE_PROTOTYPE
	 * @see Container#SCOPE_VOLATILE
	 * @see Container#SCOPE_SOFTREF
	 */
	String scope() default Container.SCOPE_SINGLETON;
	
	/**
	 * ���̃R���|�[�l���g�ɕt���閼�O���`���܂��B
	 * ���̃v���p�e�B�́A{@see Component#name()} �ƕ����ė��p���邱�Ƃ��ł��܂��B
	 * @return �R���|�[�l���g��
	 */
	String value() default "";
}
