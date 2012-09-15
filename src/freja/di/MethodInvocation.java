package freja.di;

import java.lang.reflect.Method;

/**
 * ���\�b�h�Ăяo���Ɋւ�������i�[����I�u�W�F�N�g�ł��B
 * @author SiroKuro
 */
public interface MethodInvocation {
	/**
	 * ���\�b�h�Ăяo�����̖{���̈�����Ԃ��܂��B
	 * @return �����̔z��
	 */
	public Object[] getArguments();
	
	/**
	 * �������� Method �I�u�W�F�N�g���擾���܂��B
	 * @return Method �I�u�W�F�N�g / null = ���\�b�h��������Ȃ��ꍇ
	 */
	public Method getImplementsMethod();
	
	/**
	 * �C���^�t�F�C�X���� Method �I�u�W�F�N�g���擾���܂��B
	 * @return Method �I�u�W�F�N�g / null = ���\�b�h��������Ȃ��ꍇ
	 */
	public Method getInterfaceMethod();
	
	/**
	 * ���\�b�h����Ԃ��܂��B
	 * @return ���\�b�h�̖��O
	 */
	public String getMethodName();
	
	/**
	 * ���\�b�h�̎��ۂ̌Ăяo�����s���܂��B
	 * @param args ����
	 * @return �߂�l
	 * @throws Exception �Ăяo���惁�\�b�h���牽�炩�̗�O��������ꂽ�ꍇ
	 */
	public Object invoke(Object[] args) throws Exception;
}
