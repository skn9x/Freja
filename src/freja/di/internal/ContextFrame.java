package freja.di.internal;

import freja.di.Context;

/**
 * ŽåƒNƒ‰ƒX‚Í {@link ContextManager}
 * @author SiroKuro
 */
class ContextFrame {
	private final Context context;
	private final ContextFrame parent;
	
	public ContextFrame(Context context, ContextFrame parent) {
		assert context != null;
		this.context = context;
		this.parent = parent;
	}
	
	public Context getContext() {
		return context;
	}
	
	public ContextFrame getParent() {
		return parent;
	}
}
