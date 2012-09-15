package freja.di.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collection;
import freja.di.ComponentFactory;
import freja.di.internal.util.DIUtil;

/**
 * �C���X�^���X�𒼐ڕێ����� ComponentFactory �ł��B
 * @author SiroKuro
 */
public class InstanceHolderComponentFactory implements ComponentFactory {
	private final Object instance;
	
	/**
	 * �I�u�W�F�N�g�����������܂��B
	 * @param instance �ێ�����C���X�^���X
	 */
	public InstanceHolderComponentFactory(Object instance) {
		assert instance != null;
		this.instance = instance;
	}
	
	@Override
	public Object getImplementation() {
		return instance;
	}
	
	@Override
	public int getVersion() {
		return 0; // �C���X�^���X�̃X���b�v�ɑΉ����Ă�������������Ȃ�
	}
	
	@Override
	public String getName() {
		return instance.toString();
	}
	
	@Override
	public Object newProxyInstance(InvocationHandler handler) {
		Collection<Class<?>> interfaces = DIUtil.getSuperInterfaces(instance.getClass());
		return Proxy.newProxyInstance(instance.getClass().getClassLoader(), interfaces.toArray(new Class<?>[interfaces.size()]), handler);
	}
	
}
