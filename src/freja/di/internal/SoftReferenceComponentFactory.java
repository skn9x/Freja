package freja.di.internal;

import java.lang.ref.SoftReference;
import freja.di.ComponentFactory;

/**
 * �\�t�g�Q�Ƃɂ��L���b�V�����s�� ComponentFactory �ł��B
 * @author SiroKuro
 */
public class SoftReferenceComponentFactory extends AbstractCachedComponentFactory {
	private SoftReference<Object> cache = null;
	
	/**
	 * �I�u�W�F�N�g�����������܂��B
	 * @param internal �f�R���[�g�ΏۂƂȂ� ComponentFactory �I�u�W�F�N�g
	 */
	public SoftReferenceComponentFactory(ComponentFactory internal) {
		super(internal);
	}
	
	@Override
	protected void clear() {
		cache = null;
	}
	
	@Override
	protected Object prepare() {
		Object result;
		if (cache == null || (result = cache.get()) == null) {
			cache = new SoftReference<Object>(result = internal.getImplementation());
		}
		return result;
	}
}
