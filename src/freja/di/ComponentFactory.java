package freja.di;

import java.lang.reflect.InvocationHandler;

/**
 * コンポーネントを作成・管理するファクトリオブジェクトです。
 * @author SiroKuro
 */
public interface ComponentFactory {
	/**
	 * コンポーネント実装クラスのインスタンスを返します。
	 * 返却されたインスタンスは初期化やバインディングが既に行われています。
	 * @return コンポーネント実装クラスのインスタンス
	 */
	public Object getImplementation();
	
	/**
	 * コンポーネント実装クラスのバージョン数を返します。
	 * 初回クラスロードを 0 として、クラスが更新されるたびに１増加した数が返されます。
	 * @return コンポーネント実装クラスのバージョン
	 */
	public int getVersion();
	
	/**
	 * このファクトリを識別する名前を返します。
	 * @return ファクトリ名
	 */
	public String getName();
	
	/**
	 * コンポーネントのプロキシインスタンスを返します。
	 * @param handler 仮インスタンスの動作をトラップする InvocationHandler オブジェクト
	 * @return コンポーネントのプロキシインスタンス
	 */
	public Object newProxyInstance(InvocationHandler handler);
}
