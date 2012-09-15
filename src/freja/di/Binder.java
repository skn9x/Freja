package freja.di;

import freja.di.except.InjectionFailureException;

/**
 * �I�u�W�F�N�g�ɑ΂��o�C���f�B���O���s���I�u�W�F�N�g��\���܂��B
 * @author SiroKuro
 */
public interface Binder {
	/**
	 * object �ɑ΂��o�C���f�B���O���s���܂��B
	 * @param object �o�C���f�B���O��̃I�u�W�F�N�g
	 * @throws InjectionFailureException �ˑ����̉����Ɏ��s�����ꍇ
	 */
	public void bind(Object object) throws InjectionFailureException;
}
