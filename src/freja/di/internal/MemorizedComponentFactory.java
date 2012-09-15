package freja.di.internal;

import freja.di.ComponentFactory;

/**
 * ���Q�Ƃɂ��L���b�V�����s�� ComponentFactory �ł��B
 * @author SiroKuro
 */
public class MemorizedComponentFactory extends AbstractCachedComponentFactory {
	private Object cache = null;
	
	/**
	 * �I�u�W�F�N�g�����������܂��B
	 * @param internal �f�R���[�g�ΏۂƂȂ� ComponentFactory �I�u�W�F�N�g
	 */
	public MemorizedComponentFactory(ComponentFactory internal) {
		super(internal);
	}
	
	@Override
	protected void clear() {
		cache = null;
	}
	
	@Override
	protected Object prepare() {
		if (cache == null) {
			cache = internal.getImplementation();
		}
		return cache;
	}
	
}
