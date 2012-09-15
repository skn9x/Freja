package freja.di;

import java.util.regex.Pattern;
import freja.di.except.ContextOperationFailureException;
import freja.di.except.InjectionFailureException;
import freja.di.internal.util.DIUtil;

/**
 * DI コンテナの本体となるオブジェクトを表します。
 * @author SiroKuro
 */
public interface Container {
	/**
	 * Singleton スコープを表すキーワード
	 */
	public static final String SCOPE_SINGLETON = "_singleton";
	/**
	 * Instance スコープを表すキーワード
	 */
	public static final String SCOPE_PROTOTYPE = "_prototype";
	/**
	 * Volatile スコープを表すキーワード
	 */
	public static final String SCOPE_VOLATILE = "_volatile";
	/**
	 * SoftRef スコープを表すキーワード
	 */
	public static final String SCOPE_SOFTREF = "_softref";
	
	/**
	 * 指定のインタフェイスを実装したコンポーネントにインターセプタを結び付けます。
	 * インターセプタは {@link Interceptor} を継承し、予め register メソッドにてコンテナに登録しておく必要があります。
	 * @param _interface 結びつける先のコンポーネントが継承しているインタフェイス
	 * @param name インターセプタのコンポーネント名
	 * @param priority インターセプタの優先度
	 */
	public void aspect(Class<?> _interface, String name, int priority);
	
	/**
	 * 指定の正規表現にマッチする名前を持つコンポーネントにインターセプタを結び付けます。
	 * インターセプタは {@link Interceptor} を継承し、予め register メソッドにてコンテナに登録しておく必要があります。
	 * @param targetComponent 結びつける先のコンポーネント名にマッチする正規表現(完全一致)
	 * @param name インターセプタのコンポーネント名
	 * @param priority インターセプタの優先度
	 */
	public void aspect(Pattern targetComponent, String name, int priority);
	
	/**
	 * インタフェイスに対応したコンポーネントを返します。
	 * インタフェイスに対応するコンポーネント名への変換は {@link DIUtil#toComponentName(Class)} が用いられます。
	 * @param clazz オブジェクト
	 * @return コンポーネント
	 * @throws InjectionFailureException 依存性の解決に失敗した場合
	 * @throws IllegalArgumentException clazz がインタフェイスを表すクラスオブジェクトではない場合
	 */
	public <T> T get(Class<T> clazz);
	
	/**
	 * コンポーネント名に対応したコンポーネントを返します。
	 * @param name コンポーネント名
	 * @return コンポーネント
	 * @throws InjectionFailureException 依存性の解決に失敗した場合
	 */
	public Object get(String name);
	
	/**
	 * カレントコンテキストをポップします。
	 * @throws ContextOperationFailureException コンテキストスタックが空の場合
	 */
	public void popContext();
	
	/**
	 * 指定されたコンテキストをプッシュし、カレントコンテキストにします。
	 * @param context コンテキスト
	 */
	public void pushContext(Context context);
	
	/**
	 * 指定された名前のコンテキストをプッシュし、カレントコンテキストにします。
	 * @param name 新しいコンテキストに付ける名前
	 */
	public void pushContext(String name);
	
	/**
	 * 作成済みオブジェクトをコンポーネントとして登録します。
	 * @param name 登録するコンポーネント名
	 * @param priority コンポーネントの優先度
	 * @param object コンポーネントとして登録するオブジェクト
	 */
	public void put(String name, int priority, Object object);
	
	/**
	 * コンポーネントを登録します。
	 * @param name 登録するコンポーネント名
	 * @param priority コンポーネントの優先度
	 * @param scope コンポーネントのスコープ
	 * @param factory コンポーネントファクトリ
	 */
	public void put(String name, int priority, String scope, ComponentFactory factory);
	
	/**
	 * コンポーネントを登録します。
	 * @param name 登録するコンポーネント名
	 * @param priority コンポーネントの優先度
	 * @param scope コンポーネントのスコープ
	 * @param className コンポーネント本体のクラス名
	 * @param binder コンポーネントの初期化を行う Binder オブジェクト
	 */
	public void put(String name, int priority, String scope, String className, Binder binder);
}
