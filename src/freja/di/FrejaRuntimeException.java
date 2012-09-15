package freja.di;

import java.text.MessageFormat;

/**
 * Freja DI �t���[�����[�N���Ŕ����������s����O�̃��[�g�N���X�ł��B
 * @author SiroKuro
 */
public abstract class FrejaRuntimeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8664484559295288606L;
	
	/**
	 * �I�u�W�F�N�g�����������܂��B
	 */
	public FrejaRuntimeException() {
		;
	}
	
	/**
	 * �I�u�W�F�N�g���w��̃��b�Z�[�W�ŏ��������܂��B
	 * @param message ���b�Z�[�W�p�^�[��
	 * @param args ���b�Z�[�W�̒u��������
	 */
	public FrejaRuntimeException(String message, Object... args) {
		super(createMessage(message, args));
	}
	
	/**
	 * �I�u�W�F�N�g���w��̌����ƃ��b�Z�[�W�ŏ��������܂��B
	 * @param cause �����ƂȂ�����O�I�u�W�F�N�g
	 * @param message ���b�Z�[�W�p�^�[��
	 * @param args ���b�Z�[�W�̒u��������
	 */
	public FrejaRuntimeException(Throwable cause, String message, Object... args) {
		super(createMessage(message, args), cause);
	}
	
	/**
	 * ���b�Z�[�W������𐶐����܂��B
	 * @param message ���b�Z�[�W�p�^�[��
	 * @param args ���b�Z�[�W�̒u��������
	 * @return �������ꂽ���b�Z�[�W������
	 */
	protected static String createMessage(String message, Object... args) {
		if (args == null || args.length == 0) {
			return message;
		}
		else {
			return MessageFormat.format(message, args);
		}
	}
	
}
