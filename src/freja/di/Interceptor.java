package freja.di;

/**
 * アスペクトウィーバから呼び出されるインターセプタです。
 * @author SiroKuro
 */
public interface Interceptor {
	/**
	 * 対象となったコンポーネントのメソッドが呼び出された際に、割り込みで呼び出されるメソッドです。
	 * @param name 対象のコンポーネント名
	 * @param invocation 呼び出し先メソッドの情報
	 * @param args 呼び出し時の引数
	 * @return メソッドの戻り値
	 * @throws Exception 何らかの例外がスローされた場合
	 */
	public Object intercept(String name, MethodInvocation invocation, Object[] args) throws Exception;
}
