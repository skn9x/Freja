package freja.di.internal.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import freja.internal.logging.Log;

/**
 * Freja �ŗp���� {@link ClassLoader} �ł��B
 * �w�肳�ꂽ�N���X���ɊY������N���X���A(�e�N���X���[�_�ł͂Ȃ�)
 * ���� ClassLoader ��p���ă��[�h���邱�Ƃ����������邱�Ƃ��ł��܂��B
 * @author SiroKuro
 */
public class SpecializedClassLoader extends AbstractCustomClassLoader {
	/**
	 * ���� ClassLoader ��p���ă��[�h������N���X��
	 */
	private final String specializedClassName;
	
	protected SpecializedClassLoader(ClassLoader parent, String className) {
		super(parent);
		specializedClassName = className;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if (isSystemClass(name) || !specializedClassName.equals(name)) {
			// �V�X�e���N���X�ƃX���b�v�ΏۊO�̃N���X�͒ʏ�̓ǂݍ���
			return super.findClass(name);
		}
		
		// �p�b�P�[�W��`
		definePackageIfAbsent(getPackageName(name));
		
		IllegalAccessError iaex = null;
		try {
			// �ǂݍ���
			return defineClass(name);
		}
		catch (IllegalAccessError ex) {
			iaex = ex;
		}
		catch (ClassNotFoundException ex) {
			if (ex.getCause() instanceof IllegalAccessError) {
				iaex = (IllegalAccessError) ex.getCause();
			}
			else {
				throw ex;
			}
		}
		
		// �ă��[�h
		Class<?> result = super.findClass(name);
		Log.warning(iaex, "�N���X {0} �̃��[�h���� IllegalAccessError �������������߁A�e�N���X���[�_�Ń��[�h���܂����B", name);
		return result;
	}
	
	/**
	 * TemporaryClassLoader �̐V�����C���X�^���X��Ԃ��܂��B
	 * @param parent �e�ƂȂ� ClassLoader
	 * @return TemporaryClassLoader �I�u�W�F�N�g
	 */
	public static SpecializedClassLoader newInstance(final ClassLoader parent, final String className) {
		return AccessController.doPrivileged(new PrivilegedAction<SpecializedClassLoader>() {
			@Override
			public SpecializedClassLoader run() {
				return new SpecializedClassLoader(parent, className);
			}
		});
	}
	
	/**
	 * TemporaryClassLoader �̐V�����C���X�^���X��Ԃ��܂��B
	 * @return TemporaryClassLoader �I�u�W�F�N�g
	 */
	public static SpecializedClassLoader newInstance(String className) {
		return newInstance(SpecializedClassLoader.class.getClassLoader(), className);
	}
	
}
