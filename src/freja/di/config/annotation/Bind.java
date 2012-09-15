package freja.di.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AnnotationBinder �ɂ���ăo�C���f�B���O�ΏۂƂȂ�t�B�[���h���w�肷��A�m�e�[�V�����ł��B
 * @author SiroKuro
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bind {
	/**
	 * �o�C���h����R���|�[�l���g�̖��O���w�肵�܂��B
	 * �f�t�H���g�܂��͋󕶎����w�肵���ꍇ�́A�t�B�[���h�̌^�����ɖ��O�����肳��܂��B
	 * @return �R���|�[�l���g��
	 */
	String value() default "";
}
