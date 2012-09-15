package freja.di;

/**
 * �A�X�y�N�g�E�B�[�o����Ăяo�����C���^�[�Z�v�^�ł��B
 * @author SiroKuro
 */
public interface Interceptor {
	/**
	 * �ΏۂƂȂ����R���|�[�l���g�̃��\�b�h���Ăяo���ꂽ�ۂɁA���荞�݂ŌĂяo����郁�\�b�h�ł��B
	 * @param name �Ώۂ̃R���|�[�l���g��
	 * @param invocation �Ăяo���惁�\�b�h�̏��
	 * @param args �Ăяo�����̈���
	 * @return ���\�b�h�̖߂�l
	 * @throws Exception ���炩�̗�O���X���[���ꂽ�ꍇ
	 */
	public Object intercept(String name, MethodInvocation invocation, Object[] args) throws Exception;
}
