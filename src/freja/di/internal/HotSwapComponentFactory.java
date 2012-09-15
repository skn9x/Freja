package freja.di.internal;

import java.io.File;
import freja.di.Binder;
import freja.di.except.ComponentClassNotFoundException;
import freja.di.except.InjectionFailureException;
import freja.di.internal.hotswap.FileModifiedEventListener;
import freja.di.internal.hotswap.HotSwapService;
import freja.internal.logging.Log;

/**
 * �z�b�g�X���b�v�ɑΉ����� NewComponentFactory �ł��B
 * @author SiroKuro
 */
public class HotSwapComponentFactory extends NewComponentFactory {
	
	/**
	 * �I�u�W�F�N�g�����������܂��B
	 * @param clazz �R���|�[�l���g�̎����N���X��
	 * @param binder �R���|�[�l���g�̏������ɗp���� Binder �I�u�W�F�N�g
	 */
	public HotSwapComponentFactory(String clazz, Binder binder) {
		super(new HotSwapNames(clazz, binder));
	}
	
	/**
	 * �z�b�g�X���b�v�����s���܂��B
	 * @return �z�b�g�X���b�v�ł����Ȃ�� true / �����łȂ��Ȃ�� false
	 */
	public boolean doHotSwap() {
		try {
			ComponentInitializer new_cache = names.newInitializer();
			
			// �o�C���h�������Ă݂�
			// InjectionFailureException ��������ꂽ���ɂ̓z�b�g�X���b�v�𒆎~����
			new_cache.newCoreObject();
			
			// �O��̃N���X�Ɠ������̂�����ꂽ�ꍇ�͌x������
			ComponentInitializer cache = getCache();
			if (cache != null && new_cache.getComponentClass() == cache.getComponentClass()) {
				Log.warning("�z�b�g�X���b�v��\�����Ă��܂������A�O�񃍁[�h���Ɠ����N���X {0} �������܂����B", new_cache.getComponentClass());
			}
			
			// �z�b�g�X���b�v
			setCache(new_cache);
			
			return true;
		}
		catch (ComponentClassNotFoundException ex) {
			Log.warning(ex, "�N���X {0} ��������܂���B�z�b�g�X���b�v�𒆎~���܂����B", names.getClassName());
			return false;
		}
		catch (InjectionFailureException ex) {
			Log.warning(ex, "�N���X {0} �̃o�C���f�B���O�Ɏ��s���܂����B�z�b�g�X���b�v�𒆎~���܂����B", names.getClassName());
			return false;
		}
	}
	
	/**
	 * HotSwapService �I�u�W�F�N�g�ɂ��̃I�u�W�F�N�g��o�^���܂��B
	 * @param agent HotSwapService �C���X�^���X
	 */
	public void registerTo(HotSwapService agent) {
		File file = names.getFile();
		if (file != null) {
			agent.addFileModifiedListener(file, new FileModifiedEventListener() {
				@Override
				public void onFileModified(Object sender, File file) {
					doHotSwap();
				}
			});
		}
	}
}
