package freja.di.internal;

import java.lang.reflect.Method;
import freja.di.Interceptor;
import freja.di.MethodInvocation;

/**
 * 複数の Interceptor を実行可能な Interceptor です。
 * @author SiroKuro
 */
class MultipleInterceptor implements Interceptor {
	private final Interceptor[] interceptors;
	
	/**
	 * オブジェクトを初期化します。
	 * @param interceptors 実行される順に Interceptor オブジェクトを格納した配列
	 */
	protected MultipleInterceptor(Interceptor... interceptors) {
		assert interceptors != null;
		this.interceptors = interceptors.clone();
	}
	
	/**
	 * Interceptor を順に実行していきます。
	 */
	public Object intercept(String name, MethodInvocation invocation, Object[] args) throws Exception {
		return new MultipleInvocation(invocation, name).invoke(args);
	}
	
	/**
	 * Interceptor を繋いだ Interceptor オブジェクトを返します。
	 * @param interceptors 結合する Interceptor が順に格納された配列
	 * @return 結合された Interceptor / Interceptor が 0 個の場合は null
	 */
	public static Interceptor concat(Interceptor... interceptors) {
		if (interceptors == null || interceptors.length == 0)
			return null;
		if (interceptors.length == 1)
			return interceptors[0];
		return new MultipleInterceptor(interceptors);
	}
	
	/**
	 * 複数回の呼出しに対して Interceptor を順に実行していく Invocation オブジェクト。
	 * @author SiroKuro
	 */
	private class MultipleInvocation implements MethodInvocation {
		/**
		 * 終端となる MethodInvocation
		 */
		private final MethodInvocation terminal;
		/**
		 * コンポーネント名
		 */
		private final String name;
		/**
		 * invoke メソッドの呼び出し回数
		 */
		private int index = 0;
		
		/**
		 * @param terminal
		 * @param name
		 */
		private MultipleInvocation(MethodInvocation terminal, String name) {
			this.terminal = terminal;
			this.name = name;
		}
		
		@Override
		public Object[] getArguments() {
			return terminal.getArguments();
		}
		
		@Override
		public Method getImplementsMethod() {
			return terminal.getImplementsMethod();
		}
		
		@Override
		public Method getInterfaceMethod() {
			return terminal.getInterfaceMethod();
		}
		
		@Override
		public String getMethodName() {
			return terminal.getMethodName();
		}
		
		@Override
		public Object invoke(Object[] args) throws Exception {
			int i = index++;
			try {
				if (i < interceptors.length) {
					if (interceptors[i] == null)
						return invoke(args);
					else
						return interceptors[i].intercept(name, this, args);
				}
				else {
					return terminal.invoke(args);
				}
			}
			finally {
				index--;
			}
		}
	}
}
