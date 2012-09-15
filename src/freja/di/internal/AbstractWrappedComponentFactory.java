package freja.di.internal;

import java.lang.reflect.InvocationHandler;
import freja.di.ComponentFactory;

/**
 * ComponentFactory �̃f�R���[�^�̂��߂̒��ۃN���X�ł��B
 * @author SiroKuro
 */
public abstract class AbstractWrappedComponentFactory implements ComponentFactory {
	/**
	 * internal �f�R���[�g�ΏۂƂȂ� ComponentFactory �I�u�W�F�N�g
	 */
	protected final ComponentFactory internal;
	
	/**
	 * �I�u�W�F�N�g�����������܂��B
	 * @param internal �f�R���[�g�ΏۂƂȂ� ComponentFactory �I�u�W�F�N�g
	 */
	public AbstractWrappedComponentFactory(ComponentFactory internal) {
		assert internal != null;
		this.internal = internal;
	}
	
	/**
	 * internal.getLoadCount() �ւ̈ڏ����\�b�h�ł��B
	 */
	@Override
	public int getVersion() {
		return internal.getVersion();
	}
	
	/**
	 * internal.getName() �ւ̈ڏ����\�b�h�ł��B
	 */
	@Override
	public String getName() {
		return internal.getName();
	}
	
	/**
	 * internal.newProxyInstance(handler) �ւ̈ڏ����\�b�h�ł��B
	 */
	@Override
	public Object newProxyInstance(InvocationHandler handler) {
		return internal.newProxyInstance(handler);
	}
}
