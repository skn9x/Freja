package freja.di;

import freja.di.config.annotation.AnnotationBinder;
import freja.di.config.annotation.AnnotationConfiguration;
import freja.di.internal.DefaultContainer;

/**
 * Freja DI �t���[�����[�N�̃t�@�T�[�h�ƂȂ�N���X�ł��B
 * �R���e�i������Ɏ����A�R���|�[�l���g���擾���� get ���\�b�h�Ȃǂ��T�|�[�g���܂��B
 * �����ɕێ�����R���e�i�� init ���\�b�h��p���ď��������邱�Ƃ��ł��܂��B
 * init ���\�b�h���Ăяo�����ɑ��̃��\�b�h���g�p�����ꍇ�́A
 * {@link AnnotationConfiguration#getDefaultSetting()} ��p���ď��������s���܂��B
 * @author SiroKuro
 */
public class Freja {
	private static Container currentContainer = null;
	
	private Freja() {
		;
	}
	
	/**
	 * �w�肵���C���^�t�F�C�X�N���X�ɑΉ������R���|�[�l���g��Ԃ��܂��B
	 * @param clazz �C���^�t�F�C�X��\���N���X�I�u�W�F�N�g
	 * @return �R���|�[�l���g�I�u�W�F�N�g
	 * @throws IllegalArgumentException �C���^�t�F�C�X�ȊO�̃N���X�I�u�W�F�N�g��n�����ꍇ
	 */
	public static <T> T get(Class<T> clazz) {
		return prepareContainer().get(clazz);
	}
	
	/**
	 * �w�肵���R���|�[�l���g���ɑΉ������R���|�[�l���g��Ԃ��܂��B
	 * @param name �R���|�[�l���g��
	 * @return �R���|�[�l���g�I�u�W�F�N�g
	 */
	public static Object get(String name) {
		return prepareContainer().get(name);
	}
	
	/**
	 * ���̃N���X���ێ����Ă���R���e�i��Ԃ��܂��B
	 * @return Container �I�u�W�F�N�g
	 */
	public static Container getContainer() {
		return prepareContainer();
	}
	
	/**
	 * �R���e�i�����������܂��B
	 * @param initializer �R���e�i�̏������ɗp���� Configuration �I�u�W�F�N�g
	 */
	public static void init(Configuration initializer) {
		init(null, initializer);
	}
	
	/**
	 * �R���e�i�����������܂��B
	 * @param container ���̃N���X�ɃZ�b�g����R���e�i
	 */
	public static void init(Container container, Configuration initializer) {
		currentContainer = initContainer(container, initializer);
	}
	
	/**
	 * �V�����R���e�i���쐬���܂��B
	 * �쐬���ꂽ�R���e�i�� {@link AnnotationConfiguration#getDefaultSetting()}
	 * �ɂ���ď���������܂��B
	 * @return �V�����R���e�i�I�u�W�F�N�g
	 */
	public static Container newContainer() {
		return initContainer(null, null);
	}
	
	private static Container initContainer(Container container, Configuration initializer) {
		container = container != null ? container : new DefaultContainer();
		initializer = initializer != null ? initializer : AnnotationConfiguration.getDefaultSetting();
		initializer.config(container);
		return container;
	}
	
	private static Container prepareContainer() {
		synchronized (Freja.class) {
			if (currentContainer == null) {
				currentContainer = newContainer();
			}
			return currentContainer;
		}
	}
	
	/**
	 * �w��̃I�u�W�F�N�g��DI���s���܂��B
	 * @param obj DI�Ώۂ̃I�u�W�F�N�g
	 */
	public static void bind(Object obj) {
		AnnotationBinder.bindObject(obj); // TODO ���̂��̂��g������
	}
}
