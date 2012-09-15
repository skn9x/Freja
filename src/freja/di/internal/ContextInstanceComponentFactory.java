package freja.di.internal;

import java.util.WeakHashMap;
import freja.di.ComponentFactory;
import freja.di.Context;
import freja.di.except.ContextNotFoundException;

/**
 * @author SiroKuro
 */
public class ContextInstanceComponentFactory extends AbstractWrappedComponentFactory {
	private final ContextManager contextManager;
	private final String scope;
	private final WeakHashMap<Context, Object> instances = new WeakHashMap<Context, Object>();
	
	public ContextInstanceComponentFactory(ContextManager contextManager, String scope, ComponentFactory internal) {
		super(internal);
		assert contextManager != null;
		assert scope != null;
		this.contextManager = contextManager;
		this.scope = scope;
	}
	
	@Override
	public Object getImplementation() {
		Context context = contextManager.getContext(scope);
		if (context == null) {
			throw new ContextNotFoundException("context not found");
		}
		synchronized (instances) {
			Object instance = instances.get(context);
			if (instance == null) {
				instance = internal.getImplementation();
				instances.put(context, instance);
			}
			return instance;
		}
	}
}
