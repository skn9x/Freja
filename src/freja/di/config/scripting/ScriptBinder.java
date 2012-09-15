package freja.di.config.scripting;

import javax.script.Invocable;
import javax.script.ScriptException;
import freja.di.Binder;
import freja.di.except.InjectionFailureException;

/**
 * @author SiroKuro
 */
public class ScriptBinder implements Binder {
	private final Invocable engine;
	private final String func;
	
	/**
	 * @param engine スクリプトエンジン
	 * @param func 関数名
	 */
	public ScriptBinder(Invocable engine, String func) {
		this.engine = engine;
		this.func = func;
	}
	
	@Override
	public void bind(Object object) throws InjectionFailureException {
		if (engine != null && func != null) {
			try {
				engine.invokeFunction(func, object);
			}
			catch (ScriptException ex) {
				throw new InjectionFailureException(ex, "オブジェクト {0} のバインディングに失敗しました。スクリプト内でエラーが発生しました。", object);
			}
			catch (NoSuchMethodException ex) {
				throw new InjectionFailureException(ex, "オブジェクト {0} のバインディングに失敗しました。関数 {1} が見つかりません。", object, func);
			}
		}
	}
	
}
