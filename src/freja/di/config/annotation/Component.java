package freja.di.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import freja.di.Container;

/**
 * AnnotationConfiguration によって登録されるコンポーネントに付けられるアノテーションです。
 * @author SiroKuro
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
	/**
	 * このコンポーネントに付ける名前を定義します。
	 * @return コンポーネント名の配列
	 */
	String[] name() default { };
	
	/**
	 * このコンポーネントの優先度を指定します。
	 * @return 優先度
	 */
	int priority() default 0;
	
	/**
	 * このコンポーネントのスコープを指定します。
	 * @return スコープを表す文字列
	 * @see Container#SCOPE_SINGLETON
	 * @see Container#SCOPE_PROTOTYPE
	 * @see Container#SCOPE_VOLATILE
	 * @see Container#SCOPE_SOFTREF
	 */
	String scope() default Container.SCOPE_SINGLETON;
	
	/**
	 * このコンポーネントに付ける名前を定義します。
	 * このプロパティは、{@see Component#name()} と併せて利用することができます。
	 * @return コンポーネント名
	 */
	String value() default "";
}
