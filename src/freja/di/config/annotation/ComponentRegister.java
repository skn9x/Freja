package freja.di.config.annotation;

import freja.di.Container;

/**
 * {@link AnnotationConfiguration} ���T�������N���X�ɑ΂��A
 * ���ۂɃR���e�i�ւ̓o�^���s���C���^�t�F�C�X�ł��B
 * @author SiroKuro
 */
public interface ComponentRegister {
	/**
	 * �N���X��K�₵�܂��B�N���X���R���|�[�l���g�Ȃ�΁A�R���e�i�֓o�^���܂��B
	 * @param container �o�^��R���e�i
	 * @param clazz �K�₵���N���X�I�u�W�F�N�g
	 */
	public void visit(Container container, Class<?> clazz);
}
