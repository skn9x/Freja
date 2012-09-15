package freja.di.config.annotation;

import java.lang.ref.SoftReference;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import freja.di.Configuration;
import freja.di.Container;
import freja.di.internal.config.annotation.AspectAnnotationRegister;
import freja.di.internal.config.annotation.ComponentAnnotationRegister;
import freja.di.internal.util.TemporaryClassLoader;
import freja.di.internal.util.classfinder.ClassFinder;
import freja.internal.logging.Log;

/**
 * �A�m�e�[�V�����ɂ���Đݒ���s�� Configuration �ł��B
 * @author SiroKuro
 */
public class AnnotationConfiguration implements Configuration {
	private final ClassFinder finder = new ClassFinder();
	private final Set<ComponentRegister> registers = new CopyOnWriteArraySet<ComponentRegister>();
	private SoftReference<ClassLoader> loader = new SoftReference<ClassLoader>(null);
	
	/**
	 * �N���X�̒T�����s���p�X��ǉ����܂��B
	 * @param classPath �N���X�p�X
	 * @param includeJarFile �t�H���_���ɔz�u���ꂽ JAR �t�@�C����ǂݍ��ނ��ۂ�
	 */
	public void addClassPath(String classPath, boolean includeJarFile) {
		finder.addClassPath(classPath, includeJarFile);
	}
	
	/**
	 * {@link ComponentRegister} ��ǉ����܂��B
	 * @param register ComponentRegister �I�u�W�F�N�g
	 */
	public void addComponentRegister(ComponentRegister register) {
		registers.add(register);
	}
	
	/**
	 * �f�t�H���g�̃N���X�p�X��T����ɒǉ����܂��B
	 * ��̓I�ɂ́ujava.ext.dirs�v�ujava.class.path�v�Ɏw�肳�ꂽ�p�X��ǉ����܂��B
	 */
	public void addDefaultClassPath() {
		finder.addDefaultClassPath();
	}
	
	/**
	 * �f�t�H���g�� ComponentRegister ��ǉ����܂��B
	 * ��̓I�ɂ� Component �A�m�e�[�V�������������� ComponentRegister �ƁA
	 * Aspect �A�m�e�[�V�������������� ComponentRegister ��ǉ����܂��B
	 */
	public void addDefaultComponentRegister() {
		addComponentRegister(new ComponentAnnotationRegister());
		addComponentRegister(new AspectAnnotationRegister());
	}
	
	/**
	 * �R���e�i�����������܂��B
	 * @param container �������Ώۂ̃R���e�i�I�u�W�F�N�g
	 */
	@Override
	public void config(Container container) {
		Set<String> allClassName = finder.getAllClassName();
		for (ComponentRegister register: registers) {
			for (String className: allClassName) {
				Class<?> clazz = loadTemporaryClass(className);
				if (clazz != null) {
					register.visit(container, clazz);
				}
			}
		}
	}
	
	private Class<?> loadTemporaryClass(String name) {
		Class<?> result = null;
		try {
			result = prepareClassLoader().loadClass(name);
		}
		catch (NoClassDefFoundError ex) {
			Log.debug(ex, "exception caught. ignored loading class {0}.", name);
		}
		catch (ClassNotFoundException ex) {
			Log.debug(ex, "exception caught. ignoredd loading class {0}.", name);
		}
		return result;
	}
	
	private ClassLoader prepareClassLoader() {
		ClassLoader result = loader.get();
		if (result == null) {
			result = TemporaryClassLoader.newInstance();
			loader = new SoftReference<ClassLoader>(result);
		}
		return result;
	}
	
	/**
	 * AnnotationConfiguration ���쐬���܂��B
	 * ���̃R���X�g���N�^�ŏ��������ꂽ�I�u�W�F�N�g�́A
	 * �ujava.ext.dirs�v�ujava.class.path�v�ɔz�u���ꂽ�N���X�t�@�C����ǂݍ��݁A {@link Component}
	 * �A�m�e�[�V�����̕t�����N���X��T���āA�R���e�i�ւƓo�^���܂��B
	 * �܂��A{@link Aspect} �A�m�e�[�V�����̕t�����N���X��T���āA�R���e�i�ւƓo�^���܂��B
	 * @return �쐬���ꂽ AnnotationConfiguration �I�u�W�F�N�g
	 */
	public static AnnotationConfiguration getDefaultSetting() {
		AnnotationConfiguration result = new AnnotationConfiguration();
		result.addDefaultClassPath();
		result.addDefaultComponentRegister();
		return result;
	}
}
