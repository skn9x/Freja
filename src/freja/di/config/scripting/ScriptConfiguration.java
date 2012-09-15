package freja.di.config.scripting;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import freja.di.Configuration;
import freja.di.Container;
import freja.di.except.ConfigurationFailureException;
import freja.di.internal.util.PrototypeContainer;

/**
 * @author SiroKuro
 */
public abstract class ScriptConfiguration implements Configuration {
	private ScriptEngine engine = null;
	private PrototypeContainer prototype = null;
	
	@Override
	public void config(Container container) {
		preparePrototype(prepareScriptEngine()).commit(container);
	}
	
	protected abstract void eval(ScriptEngine engine) throws ScriptException;
	
	protected abstract ScriptEngine newScriptEngine();
	
	private PrototypeContainer preparePrototype(ScriptEngine engine) {
		synchronized (this) {
			if (prototype == null) {
				prototype = new PrototypeContainer();
				try {
					engine.put("freja", new ScriptingFacade(prototype, engine));
					eval(engine);
				}
				catch (ScriptException ex) {
					throw new ConfigurationFailureException(ex, "an exception occured.");
				}
			}
			return prototype;
		}
	}
	
	private ScriptEngine prepareScriptEngine() {
		synchronized (this) {
			if (engine == null) {
				engine = newScriptEngine();
			}
			return engine;
		}
	}
	
	public static ScriptConfiguration fromCode(String engineName, String script) {
		return new FromCode(engineName, script);
	}
	
	public static ScriptConfiguration fromResource(String path) {
		return new FromResource(path);
	}
	
	private static class FromCode extends ScriptConfiguration {
		private final String engineName;
		private final String script;
		
		public FromCode(String engineName, String script) {
			assert engineName != null;
			assert script != null;
			this.engineName = engineName;
			this.script = script;
		}
		
		@Override
		public void eval(ScriptEngine engine) throws ScriptException {
			engine.eval(script);
		}
		
		@Override
		protected ScriptEngine newScriptEngine() {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName(engineName);
			if (engine == null)
				throw new ConfigurationFailureException("script engine [{0}] not found.", engineName);
			return engine;
		}
		
	}
	
	private static class FromResource extends ScriptConfiguration {
		private final String path;
		
		public FromResource(String path) {
			assert path != null;
			this.path = path;
		}
		
		@Override
		public void eval(ScriptEngine engine) throws ScriptException {
			InputStream input = ScriptConfiguration.class.getClassLoader().getResourceAsStream(path);
			if (input == null) {
				throw new ConfigurationFailureException("resource [{0}] not found.", path);
			}
			try {
				Reader reader = new InputStreamReader(input);
				try {
					engine.eval(reader);
				}
				finally {
					reader.close();
					input.close();
				}
			}
			catch (IOException ex) {
				throw new ConfigurationFailureException(ex, "an exception occured.");
			}
		}
		
		@Override
		protected ScriptEngine newScriptEngine() {
			String ext = getExtension(path);
			ScriptEngine engine = new ScriptEngineManager().getEngineByExtension(ext);
			if (engine == null)
				throw new ConfigurationFailureException("script engine [{0}] not found.", ext);
			return engine;
		}
		
		private static String getExtension(String path) {
			if (path == null)
				return "";
			int index = path.lastIndexOf('.');
			if (index < 0)
				return "";
			return path.substring(index + 1);
		}
	}
}
