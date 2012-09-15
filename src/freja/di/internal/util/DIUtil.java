package freja.di.internal.util;

import java.io.File;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import freja.di.except.InstantiationFailureException;

/**
 * @author SiroKuro
 */
public class DIUtil {
	private static final boolean USE_SETACCESSIBLETRUE_DOPRIVILEGED = false;
	
	/**
	 * �n���ꂽ�N���X����������C���^�t�F�C�X�̏W����Ԃ��B
	 * �N���X�̐e�N���X����������C���^�t�F�C�X�����킹�ĕԂ��B {@link #getSuperInterfaces(Class)}
	 * �Ƃ͈قȂ�A�C���^�t�F�C�X�̌p���֌W�����ǂ�B
	 * @param c �����Ώۂ̃N���X�I�u�W�F�N�g
	 * @return �C���^�t�F�C�X�̏W��
	 */
	public static Set<Class<?>> getAllSuperInterfaces(Class<?> c) {
		Set<Class<?>> result = new HashSet<Class<?>>();
		getSuperInterfaces(result, c, true);
		return result;
	}
	
	/**
	 * �n���ꂽ�N���X����������C���^�t�F�C�X�̏W����Ԃ��B
	 * �N���X�̐e�N���X����������C���^�t�F�C�X�����킹�ĕԂ��B
	 * @param c �����Ώۂ̃N���X�I�u�W�F�N�g
	 * @return �C���^�t�F�C�X�̏W��
	 */
	public static Set<Class<?>> getSuperInterfaces(Class<?> c) {
		Set<Class<?>> result = new HashSet<Class<?>>();
		getSuperInterfaces(result, c, false);
		return result;
	}
	
	/**
	 * �N���X clazz �̈����Ȃ��R���X�g���N�^���Ăяo���A�C���X�^���X���쐬���܂��B
	 * @param clazz �C���X�^���X���쐬����N���X�I�u�W�F�N�g
	 * @return �C���X�^���X
	 * @throws InstantiationFailureException �C���X�^���X�̍쐬�Ɏ��s�����ꍇ
	 */
	public static <T> T newInstance(Class<T> clazz) {
		assert clazz != null;
		try {
			Constructor<T> c = clazz.getDeclaredConstructor();
			setAccessibleTrue(c);
			return c.newInstance();
		}
		catch (NoSuchMethodException ex) {
			throw new InstantiationFailureException(ex,
					"�C���X�^���X�����Ɏ��s���܂����B{0} �Ɉ����Ȃ��̃R���X�g���N�^��������܂���B�C���X�^���X�����ɂ͈����Ȃ��̃R���X�g���N�^���K�v�ł��B", clazz);
		}
		catch (IllegalArgumentException ex) {
			throw new InstantiationFailureException(ex, "�C���X�^���X�����Ɏ��s���܂����B�\�����ʓ�����O�ł��B");
		}
		catch (IllegalAccessException ex) {
			throw new InstantiationFailureException(ex, "�C���X�^���X�����Ɏ��s���܂����B�\�����ʓ�����O�ł��B");
		}
		catch (InvocationTargetException ex) {
			throw new InstantiationFailureException(ex, "�C���X�^���X�����Ɏ��s���܂����B{0} �̃R���X�g���N�^����O���X���[���܂����B", clazz);
		}
		catch (InstantiationException ex) {
			throw new InstantiationFailureException(ex, "�C���X�^���X�����Ɏ��s���܂����B{0} �͒��ۃN���X�ł��B", clazz);
		}
	}
	
	public static void setAccessibleTrue(final AccessibleObject object) {
		if (!object.isAccessible()) {
			if (USE_SETACCESSIBLETRUE_DOPRIVILEGED) {
				AccessController.doPrivileged(new PrivilegedAction<Object>() {
					@Override
					public Object run() {
						object.setAccessible(true);
						return null;
					}
				});
			}
			else {
				object.setAccessible(true);
			}
		}
	}
	
	public static String toAspectName(Class<?> clazz) {
		return "aspect:" + clazz.getName();
	}
	
	public static String toComponentName(Class<?> clazz) {
		return "class:" + clazz.getName();
	}
	
	public static File toFile(URL url) throws URISyntaxException, MalformedURLException {
		String protocol = url.getProtocol();
		if ("jar".equalsIgnoreCase(protocol)) {
			String jarPath = url.toExternalForm();
			int begin = "jar:".length();
			int end = jarPath.indexOf("!/");
			if (end < 0) {
				// �{�������ɂ͓��B���Ȃ��炵���Bjar �v���g�R���ł� "!/" �͕K�{�̎d�l�H
				return new File(new URL(jarPath.substring(begin)).toURI());
			}
			else {
				return new File(new URL(jarPath.substring(begin, end)).toURI());
			}
		}
		else {
			return new File(url.toURI());
		}
	}
	
	private static void getSuperInterfaces(Collection<Class<?>> result, Class<?> c, boolean allInterfaces) {
		if (c != null) {
			getSuperInterfaces(result, c.getSuperclass(), allInterfaces);
			for (Class<?> _interfaces: c.getInterfaces()) {
				result.add(_interfaces);
				if (allInterfaces) {
					getSuperInterfaces(result, _interfaces, allInterfaces);
				}
			}
		}
	}
}
