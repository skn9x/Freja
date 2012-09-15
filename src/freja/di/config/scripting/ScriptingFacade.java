package freja.di.config.scripting;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import freja.di.Container;
import freja.di.config.annotation.AnnotationBinder;
import freja.di.config.empty.EmptyBinder;
import freja.di.config.multiple.MultipleBinder;
import freja.di.except.ConfigurationFailureException;

/**
 * @author SiroKuro
 */
public class ScriptingFacade {
	private final ScriptEngine engine;
	private final Container container;
	
	public ScriptingFacade(Container container, ScriptEngine engine) {
		this.container = container;
		this.engine = engine;
	}
	
	public void put(String name, int priority, String scope, String className) {
		container.put(name, priority, scope, className, EmptyBinder.INSTANCE);
	}
	
	public void put(String name, int priority, String scope, String className, String func) {
		container.put(name, priority, scope, className, new MultipleBinder(new AnnotationBinder(container), new ScriptBinder(prepareInvocable(), func)));
	}
	
	public void put(String name, String scope, String className) {
		put(name, 0, scope, className);
	}
	
	public void put(String name, String scope, String className, String func) {
		put(name, 0, scope, className, func);
	}
	
	public void putObject(String name, int priority, Object object) {
		container.put(name, priority, object);
	}
	
	public void putObject(String name, Object object) {
		container.put(name, 0, object);
	}
	
	private Invocable prepareInvocable() {
		if (engine instanceof Invocable)
			return (Invocable) engine;
		else
			throw new ConfigurationFailureException("スクリプトエンジン {0} は Invocable を実装していないため、Binder として使用できません。", engine);
	}
	
}
