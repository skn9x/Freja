package freja.di.internal.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import freja.di.Binder;
import freja.di.ComponentFactory;
import freja.di.Container;
import freja.di.Context;

/**
 * @author SiroKuro
 */
public class PrototypeContainer implements Container {
	private final List<Command> commands = new ArrayList<Command>();
	
	@Override
	public void aspect(final Class<?> interface1, final String name, final int priority) {
		commands.add(new Command() {
			@Override
			public void exec(Container container) {
				container.aspect(interface1, name, priority);
			}
		});
	}
	
	@Override
	public void aspect(final Pattern targetComponent, final String name, final int priority) {
		commands.add(new Command() {
			@Override
			public void exec(Container container) {
				container.aspect(targetComponent, name, priority);
			}
		});
	}
	
	public void commit(Container container) {
		for (Command cmd: commands) {
			cmd.exec(container);
		}
	}
	
	@Override
	public <T> T get(Class<T> clazz) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Object get(String name) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void popContext() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void pushContext(Context context) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void pushContext(String name) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void put(final String name, final int priority, final Object object) {
		commands.add(new Command() {
			@Override
			public void exec(Container container) {
				container.put(name, priority, object);
			}
		});
	}
	
	@Override
	public void put(final String name, final int priority, final String scope, final ComponentFactory factory) {
		commands.add(new Command() {
			@Override
			public void exec(Container container) {
				container.put(name, priority, scope, factory);
			}
		});
	}
	
	@Override
	public void put(final String name, final int priority, final String scope, final String className, final Binder binder) {
		commands.add(new Command() {
			@Override
			public void exec(Container container) {
				container.put(name, priority, scope, className, binder);
			}
		});
	}
	
	private static interface Command {
		public void exec(Container container);
	}
}
