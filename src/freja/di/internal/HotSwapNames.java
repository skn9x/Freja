package freja.di.internal;

import freja.di.Binder;
import freja.di.internal.util.SpecializedClassLoader;

/**
 * �z�b�g�X���b�v�ɑΉ����� Names �I�u�W�F�N�g�ł��B
 * ��N���X�� {@link DefaultContainer}
 * @author SiroKuro
 */
class HotSwapNames extends Names {
	
	public HotSwapNames(String className, Binder binder) {
		super(className, binder);
	}
	
	@Override
	protected ClassLoader getClassLoader() {
		return SpecializedClassLoader.newInstance(getClassName());
	}
	
}
