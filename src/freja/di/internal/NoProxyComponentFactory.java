package freja.di.internal;

import java.lang.reflect.InvocationHandler;

/**
 * @author SiroKuro
 */
class NoProxyComponentFactory extends InstanceHolderComponentFactory {
	
	/**
	 * @param instance
	 */
	public NoProxyComponentFactory(Object instance) {
		super(instance);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object newProxyInstance(InvocationHandler handler) {
		throw new UnsupportedOperationException();
	}
}
