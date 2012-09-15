package freja.di;

import java.lang.reflect.InvocationHandler;

/**
 * �R���|�[�l���g���쐬�E�Ǘ�����t�@�N�g���I�u�W�F�N�g�ł��B
 * @author SiroKuro
 */
public interface ComponentFactory {
	/**
	 * �R���|�[�l���g�����N���X�̃C���X�^���X��Ԃ��܂��B
	 * �ԋp���ꂽ�C���X�^���X�͏�������o�C���f�B���O�����ɍs���Ă��܂��B
	 * @return �R���|�[�l���g�����N���X�̃C���X�^���X
	 */
	public Object getImplementation();
	
	/**
	 * �R���|�[�l���g�����N���X�̃o�[�W��������Ԃ��܂��B
	 * ����N���X���[�h�� 0 �Ƃ��āA�N���X���X�V����邽�тɂP�������������Ԃ���܂��B
	 * @return �R���|�[�l���g�����N���X�̃o�[�W����
	 */
	public int getVersion();
	
	/**
	 * ���̃t�@�N�g�������ʂ��閼�O��Ԃ��܂��B
	 * @return �t�@�N�g����
	 */
	public String getName();
	
	/**
	 * �R���|�[�l���g�̃v���L�V�C���X�^���X��Ԃ��܂��B
	 * @param handler ���C���X�^���X�̓�����g���b�v���� InvocationHandler �I�u�W�F�N�g
	 * @return �R���|�[�l���g�̃v���L�V�C���X�^���X
	 */
	public Object newProxyInstance(InvocationHandler handler);
}
