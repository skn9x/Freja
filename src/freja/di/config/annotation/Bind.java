package freja.di.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AnnotationBinder によってバインディング対象となるフィールドを指定するアノテーションです。
 * @author SiroKuro
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bind {
	/**
	 * バインドするコンポーネントの名前を指定します。
	 * デフォルトまたは空文字を指定した場合は、フィールドの型を元に名前が決定されます。
	 * @return コンポーネント名
	 */
	String value() default "";
}
