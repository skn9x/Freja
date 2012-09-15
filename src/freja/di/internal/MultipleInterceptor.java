package freja.di.internal;

import java.lang.reflect.Method;
import freja.di.Interceptor;
import freja.di.MethodInvocation;

/**
 * ������ Interceptor �����s�\�� Interceptor �ł��B
 * @author SiroKuro
 */
class MultipleInterceptor implements Interceptor {
	private final Interceptor[] interceptors;
	
	/**
	 * �I�u�W�F�N�g�����������܂��B
	 * @param interceptors ���s����鏇�� Interceptor �I�u�W�F�N�g���i�[�����z��
	 */
	protected MultipleInterceptor(Interceptor... interceptors) {
		assert interceptors != null;
		this.interceptors = interceptors.clone();
	}
	
	/**
	 * Interceptor �����Ɏ��s���Ă����܂��B
	 */
	public Object intercept(String name, MethodInvocation invocation, Object[] args) throws Exception {
		return new MultipleInvocation(invocation, name).invoke(args);
	}
	
	/**
	 * Interceptor ���q���� Interceptor �I�u�W�F�N�g��Ԃ��܂��B
	 * @param interceptors �������� Interceptor �����Ɋi�[���ꂽ�z��
	 * @return �������ꂽ Interceptor / Interceptor �� 0 �̏ꍇ�� null
	 */
	public static Interceptor concat(Interceptor... interceptors) {
		if (interceptors == null || interceptors.length == 0)
			return null;
		if (interceptors.length == 1)
			return interceptors[0];
		return new MultipleInterceptor(interceptors);
	}
	
	/**
	 * ������̌ďo���ɑ΂��� Interceptor �����Ɏ��s���Ă��� Invocation �I�u�W�F�N�g�B
	 * @author SiroKuro
	 */
	private class MultipleInvocation implements MethodInvocation {
		/**
		 * �I�[�ƂȂ� MethodInvocation
		 */
		private final MethodInvocation terminal;
		/**
		 * �R���|�[�l���g��
		 */
		private final String name;
		/**
		 * invoke ���\�b�h�̌Ăяo����
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
