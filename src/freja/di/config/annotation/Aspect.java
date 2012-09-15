package freja.di.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import freja.di.Container;

/**
 * AnnotationConfiguration によって登録されるアスペクトに付けられるアノテーションです。
 * @author SiroKuro
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {	// TODO interface 用と name 用に分割する
	/**
	 * インターセプタの優先度を指定します。値が大きいアスペクトが、より先に呼び出されます。
	 * @return 優先度
	 */
	int priority() default 0;
	
	/**
	 * インターセプタのスコープを指定します。
	 * @return スコープ名
	 */
	String scope() default Container.SCOPE_SINGLETON;
	
	/**
	 * アスペクト挿入先のインタフェイスクラスを指定します。
	 * @return 挿入先インタフェイスの配列
	 */
	Class<?>[] targetInterface() default { };
	
	/**
	 * アスペクト挿入先のコンポーネント名を指定します。
	 * @return 挿入先コンポーネント名の配列
	 */
	String[] targetName() default { };
}
