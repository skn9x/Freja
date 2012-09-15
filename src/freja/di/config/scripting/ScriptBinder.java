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
	 * @param engine �X�N���v�g�G���W��
	 * @param func �֐���
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
				throw new InjectionFailureException(ex, "�I�u�W�F�N�g {0} �̃o�C���f�B���O�Ɏ��s���܂����B�X�N���v�g���ŃG���[���������܂����B", object);
			}
			catch (NoSuchMethodException ex) {
				throw new InjectionFailureException(ex, "�I�u�W�F�N�g {0} �̃o�C���f�B���O�Ɏ��s���܂����B�֐� {1} ��������܂���B", object, func);
			}
		}
	}
	
}
