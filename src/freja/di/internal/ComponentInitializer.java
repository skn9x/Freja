package freja.di.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collection;
import freja.di.Binder;
import freja.di.internal.util.DIUtil;

/**
 * �R���|�[�l���g�̏������ɗp��������i�[���܂��B
 * ��N���X�� {@link NewComponentFactory}
 * @author SiroKuro
 */
class ComponentInitializer {
	private final Class<?> component;
	private final Binder binder;
	private final Class<?>[] interfaces;
	
	/**
	 * �I�u�W�F�N�g�����������܂��B
	 * @param component �R���|�[�l���g�̎��N���X
	 * @param binder �R���|�[�l���g�� DI �ɗp���� Binder �I�u�W�F�N�g
	 */
	public ComponentInitializer(Class<?> component, Binder binder) {
		assert component != null;
		assert binder != null;
		
		this.component = component;
		this.binder = binder;
		
		Collection<Class<?>> interfaces = DIUtil.getSuperInterfaces(component);
		this.interfaces = interfaces.toArray(new Class<?>[interfaces.size()]);
	}
	
	/**
	 * �R���|�[�l���g�ɑ΂��� DI �ɗp���� Binder �I�u�W�F�N�g��Ԃ��܂��B
	 * @return Binder �I�u�W�F�N�g
	 */
	public Binder getBinder() {
		return binder;
	}
	
	/**
	 * �R���|�[�l���g�̎��N���X��Ԃ��܂��B
	 * @return �R���|�[�l���g�̎��N���X
	 */
	public Class<?> getComponentClass() {
		return component;
	}
	
	/**
	 * �V�����R���|�[�l���g�̎��C���X�^���X��Ԃ��܂��B
	 * �܂��A������ Binder �ɂ��o�C���f�B���O���s�Ȃ��܂��B
	 * @return �쐬���ꂽ�R���|�[�l���g�̃C���X�^���X
	 */
	public Object newCoreObject() {
		Object result = DIUtil.newInstance(component);
		binder.bind(result);
		return result;
	}
	
	public Object newProxyInstance(InvocationHandler handler) {
		return Proxy.newProxyInstance(component.getClassLoader(), interfaces, handler);
	}
}
