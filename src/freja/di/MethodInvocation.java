package freja.di;

import java.lang.reflect.Method;

/**
 * メソッド呼び出しに関する情報を格納するオブジェクトです。
 * @author SiroKuro
 */
public interface MethodInvocation {
	/**
	 * メソッド呼び出し時の本来の引数を返します。
	 * @return 引数の配列
	 */
	public Object[] getArguments();
	
	/**
	 * 実装側の Method オブジェクトを取得します。
	 * @return Method オブジェクト / null = メソッドが見つからない場合
	 */
	public Method getImplementsMethod();
	
	/**
	 * インタフェイス側の Method オブジェクトを取得します。
	 * @return Method オブジェクト / null = メソッドが見つからない場合
	 */
	public Method getInterfaceMethod();
	
	/**
	 * メソッド名を返します。
	 * @return メソッドの名前
	 */
	public String getMethodName();
	
	/**
	 * メソッドの実際の呼び出しを行います。
	 * @param args 引数
	 * @return 戻り値
	 * @throws Exception 呼び出し先メソッドから何らかの例外が投げられた場合
	 */
	public Object invoke(Object[] args) throws Exception;
}
