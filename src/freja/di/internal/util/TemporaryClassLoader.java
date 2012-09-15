package freja.di.internal.util;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * �ꎞ�I�ȃN���X�ǂݍ��݂ɗp���� {@link ClassLoader} �ł��B
 * �S�ẴN���X���A(�e�N���X���[�_�ł͂Ȃ�)���� ClassLoader ��p���ă��[�h���邱�Ƃ����������邱�Ƃ��ł��܂��B
 * @author SiroKuro
 */
public class TemporaryClassLoader extends AbstractCustomClassLoader {
	protected TemporaryClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if (!isSystemClass(name)) {
			definePackageIfAbsent(getPackageName(name));
			return defineClass(name);
		}
		return super.findClass(name);
	}
	
	/**
	 * TemporaryClassLoader �̐V�����C���X�^���X��Ԃ��܂��B
	 * @return TemporaryClassLoader �I�u�W�F�N�g
	 */
	public static TemporaryClassLoader newInstance() {
		return newInstance(TemporaryClassLoader.class.getClassLoader());
	}
	
	/**
	 * TemporaryClassLoader �̐V�����C���X�^���X��Ԃ��܂��B
	 * @param parent �e�ƂȂ� ClassLoader
	 * @return TemporaryClassLoader �I�u�W�F�N�g
	 */
	public static TemporaryClassLoader newInstance(final ClassLoader parent) {
		return AccessController.doPrivileged(new PrivilegedAction<TemporaryClassLoader>() {
			@Override
			public TemporaryClassLoader run() {
				return new TemporaryClassLoader(parent);
			}
		});
	}
}
