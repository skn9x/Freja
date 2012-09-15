package freja.di.internal;

import freja.di.Context;

/**
 * ŽåƒNƒ‰ƒX‚Í {@link DefaultContainer}
 * @author SiroKuro
 */
class ContextManager {
	private final ThreadLocal<ContextFrame> currentContext = new ThreadLocal<ContextFrame>();
	
	public Context getContext(String name) {
		assert name != null;
		ContextFrame frame = currentContext.get();
		while (frame != null) {
			if (name.equals(frame.getContext().getName())) {
				return frame.getContext();
			}
			frame = frame.getParent();
		}
		return null;
	}
	
	public void popContext() {
		ContextFrame frame = currentContext.get();
		if (frame == null) {
			throw new IllegalStateException("context stack underflow");
		}
		currentContext.set(frame.getParent());
	}
	
	public void pushContext(Context context) {
		currentContext.set(new ContextFrame(context, currentContext.get()));
	}
}
