package freja.di.internal;

import java.lang.reflect.InvocationHandler;
import freja.di.Binder;
import freja.di.ComponentFactory;
import freja.di.except.InjectionFailureException;

/**
 * �R���|�[�l���g��V�K�쐬���� ComponentFactory �ł��B
 * @author SiroKuro
 */
public class NewComponentFactory implements ComponentFactory {
	/**
	 * �����ŊǗ������ Names �I�u�W�F�N�g
	 */
	protected final Names names;
	private volatile ComponentInitializer cache = null;
	private volatile int version = 0;
	
	/**
	 * �I�u�W�F�N�g�����������܂��B
	 * @param clazz �R���|�[�l���g�̎����N���X��
	 * @param binder �R���|�[�l���g�̏������ɗp���� Binder �I�u�W�F�N�g
	 */
	public NewComponentFactory(String clazz, Binder binder) {
		this(new Names(clazz, binder));
	}
	
	/**
	 * �I�u�W�F�N�g�����������܂��B
	 * @param names �R���|�[�l���g���������� Names �I�u�W�F�N�g
	 */
	protected NewComponentFactory(Names names) {
		assert names != null;
		this.names = names;
	}
	
	public ComponentInitializer getCache() {
		return cache;
	}
	
	@Override
	public Object getImplementation() throws InjectionFailureException {
		return prepareClasses().newCoreObject();
	}
	
	@Override
	public int getVersion() {
		return version;
	}
	
	@Override
	public String getName() {
		return names.getClassName();
	}
	
	@Override
	public Object newProxyInstance(InvocationHandler handler) {
		return prepareClasses().newProxyInstance(handler);
	}
	
	protected ComponentInitializer prepareClasses() {
		synchronized (this) {
			if (cache == null) {
				setCache(names.newInitializer());
			}
			return this.cache;
		}
	}
	
	protected void setCache(ComponentInitializer cache) {
		synchronized (this) {
			this.cache = cache;
			version++;
		}
	}
}
