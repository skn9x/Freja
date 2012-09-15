package freja.di.internal;

import freja.di.ComponentFactory;

/**
 * �R���|�[�l���g�����C���X�^���X������ɃL���b�V������ ComponentFactory �̂��߂̒��ۃN���X�ł��B
 * @author SiroKuro
 */
public abstract class AbstractCachedComponentFactory extends AbstractWrappedComponentFactory {
	private int oldLoadCount = -1;
	
	/**
	 * �I�u�W�F�N�g�����������܂��B
	 * @param internal �f�R���[�g�ΏۂƂȂ� ComponentFactory �I�u�W�F�N�g
	 */
	public AbstractCachedComponentFactory(ComponentFactory internal) {
		super(internal);
	}
	
	/**
	 * internal ���N���A���ꂽ���ǂ����𔻒f���AclearCache() �܂��� getCacheOrCreate() ���Ăяo���܂��B
	 */
	@Override
	public Object getImplementation() {
		synchronized (this) {
			// oldLoadCount �����Z����Ă���΁Acache ���N���A����
			int lc = internal.getVersion();
			if (oldLoadCount < lc) {
				oldLoadCount = lc;
				clear();
			}
			return prepare();
		}
	}
	
	/**
	 * �L���b�V�����N���A���܂��B
	 */
	protected abstract void clear();
	
	/**
	 * �L���b�V������擾�����l���A�܂��� internal.getImplementation() ���Ăяo���Ď擾�����l��Ԃ��܂��B
	 * @return �L���b�V�����ꂽ�R���|�[�l���g�����C���X�^���X
	 */
	protected abstract Object prepare();
}
