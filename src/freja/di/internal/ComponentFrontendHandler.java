package freja.di.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import freja.di.ComponentFactory;
import freja.di.Interceptor;
import freja.di.MethodInvocation;
import freja.di.internal.util.DIUtil;

/**
 * ��N���X�� {@link DefaultContainer}
 * @author SiroKuro
 */
class ComponentFrontendHandler implements InvocationHandler {
	private static final boolean USE_TRANSFEREDMETHOD = false;
	private final String name;
	private final ComponentFactory factory;
	private Interceptor interceptor = null;
	
	/**
	 * �I�u�W�F�N�g�����������܂��B
	 * @param name �R���|�[�l���g��
	 * @param factory �R���|�[�l���g�����ɗp���� ComponentFactory
	 */
	public ComponentFrontendHandler(String name, ComponentFactory factory) {
		assert name != null;
		assert factory != null;
		this.name = name;
		this.factory = factory;
	}
	
	/**
	 * @return the interceptor
	 */
	public Interceptor getInterceptor() {
		return interceptor;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
		Object object = factory.getImplementation();
		Interceptor interceptor = getInterceptor();
		if (interceptor == null)
			return invokeObject(object, method, args);
		else
			return interceptor.intercept(name, new TerminalInvocation(object, method, args), args);
	}
	
	/**
	 * @param interceptor the interceptor to set
	 */
	public void setInterceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
	}
	
	protected static Object invokeObject(Object object, Method mm, Object... args) throws Exception {
		if (USE_TRANSFEREDMETHOD) {
			mm = transfer(object, mm);
		}
		DIUtil.setAccessibleTrue(mm);
		return mm.invoke(object, args);
	}
	
	protected static Method transfer(Object object, Method mm) throws NoSuchMethodException {
		return object.getClass().getMethod(mm.getName(), mm.getParameterTypes());
	}
	
	/**
	 * �����N���X�̃��\�b�h���Ăяo�����߂� MethodInvocation �I�u�W�F�N�g
	 * @author SiroKuro
	 */
	private static class TerminalInvocation implements MethodInvocation {
		/**
		 * �Ăяo���Ώ̃I�u�W�F�N�g
		 */
		private final Object object;
		/**
		 * �C���^�t�F�C�X�� Method �I�u�W�F�N�g
		 */
		private final Method method;
		/**
		 * ����Ăяo�����̈���
		 */
		private final Object[] args;
		
		/**
		 * @param object
		 * @param method
		 * @param args
		 */
		private TerminalInvocation(Object object, Method method, Object[] args) {
			assert object != null;
			assert method != null;
			this.method = method;
			this.args = args;
			this.object = object;
		}
		
		@Override
		public Object[] getArguments() {
			return args;
		}
		
		@Override
		public Method getImplementsMethod() {
			try {
				return transfer(object, method);
			}
			catch (NoSuchMethodException ex) {
				return null;
			}
		}
		
		@Override
		public Method getInterfaceMethod() {
			return method;
		}
		
		@Override
		public String getMethodName() {
			return method.getName();
		}
		
		@Override
		public Object invoke(Object[] new_args) throws Exception {
			return invokeObject(object, method, new_args);
		}
	}
}
