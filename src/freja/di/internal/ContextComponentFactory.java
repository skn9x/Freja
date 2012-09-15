package freja.di.internal;

import freja.di.ComponentFactory;
import freja.di.Context;
import freja.di.except.ContextNotFoundException;

/**
 * @author SiroKuro
 */
public class ContextComponentFactory extends AbstractWrappedComponentFactory {
	private final ContextManager contextManager;
	private final String name;
	private final String scope;
	
	public ContextComponentFactory(ContextManager contextManager, String name, String scope, ComponentFactory internal) {
		super(internal);
		assert contextManager != null;
		assert name != null;
		assert scope != null;
		this.contextManager = contextManager;
		this.name = name;
		this.scope = scope;
	}
	
	@Override
	public Object getImplementation() {
		Context context = contextManager.getContext(scope);
		if (context == null) {
			throw new ContextNotFoundException("context not found");
		}
		return context.getComponent(name, internal);
	}
}
