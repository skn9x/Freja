package freja.di;

import java.util.regex.Pattern;
import freja.di.except.ContextOperationFailureException;
import freja.di.except.InjectionFailureException;
import freja.di.internal.util.DIUtil;

/**
 * DI �R���e�i�̖{�̂ƂȂ�I�u�W�F�N�g��\���܂��B
 * @author SiroKuro
 */
public interface Container {
	/**
	 * Singleton �X�R�[�v��\���L�[���[�h
	 */
	public static final String SCOPE_SINGLETON = "_singleton";
	/**
	 * Instance �X�R�[�v��\���L�[���[�h
	 */
	public static final String SCOPE_PROTOTYPE = "_prototype";
	/**
	 * Volatile �X�R�[�v��\���L�[���[�h
	 */
	public static final String SCOPE_VOLATILE = "_volatile";
	/**
	 * SoftRef �X�R�[�v��\���L�[���[�h
	 */
	public static final String SCOPE_SOFTREF = "_softref";
	
	/**
	 * �w��̃C���^�t�F�C�X�����������R���|�[�l���g�ɃC���^�[�Z�v�^�����ѕt���܂��B
	 * �C���^�[�Z�v�^�� {@link Interceptor} ���p�����A�\�� register ���\�b�h�ɂăR���e�i�ɓo�^���Ă����K�v������܂��B
	 * @param _interface ���т����̃R���|�[�l���g���p�����Ă���C���^�t�F�C�X
	 * @param name �C���^�[�Z�v�^�̃R���|�[�l���g��
	 * @param priority �C���^�[�Z�v�^�̗D��x
	 */
	public void aspect(Class<?> _interface, String name, int priority);
	
	/**
	 * �w��̐��K�\���Ƀ}�b�`���閼�O�����R���|�[�l���g�ɃC���^�[�Z�v�^�����ѕt���܂��B
	 * �C���^�[�Z�v�^�� {@link Interceptor} ���p�����A�\�� register ���\�b�h�ɂăR���e�i�ɓo�^���Ă����K�v������܂��B
	 * @param targetComponent ���т����̃R���|�[�l���g���Ƀ}�b�`���鐳�K�\��(���S��v)
	 * @param name �C���^�[�Z�v�^�̃R���|�[�l���g��
	 * @param priority �C���^�[�Z�v�^�̗D��x
	 */
	public void aspect(Pattern targetComponent, String name, int priority);
	
	/**
	 * �C���^�t�F�C�X�ɑΉ������R���|�[�l���g��Ԃ��܂��B
	 * �C���^�t�F�C�X�ɑΉ�����R���|�[�l���g���ւ̕ϊ��� {@link DIUtil#toComponentName(Class)} ���p�����܂��B
	 * @param clazz �I�u�W�F�N�g
	 * @return �R���|�[�l���g
	 * @throws InjectionFailureException �ˑ����̉����Ɏ��s�����ꍇ
	 * @throws IllegalArgumentException clazz ���C���^�t�F�C�X��\���N���X�I�u�W�F�N�g�ł͂Ȃ��ꍇ
	 */
	public <T> T get(Class<T> clazz);
	
	/**
	 * �R���|�[�l���g���ɑΉ������R���|�[�l���g��Ԃ��܂��B
	 * @param name �R���|�[�l���g��
	 * @return �R���|�[�l���g
	 * @throws InjectionFailureException �ˑ����̉����Ɏ��s�����ꍇ
	 */
	public Object get(String name);
	
	/**
	 * �J�����g�R���e�L�X�g���|�b�v���܂��B
	 * @throws ContextOperationFailureException �R���e�L�X�g�X�^�b�N����̏ꍇ
	 */
	public void popContext();
	
	/**
	 * �w�肳�ꂽ�R���e�L�X�g���v�b�V�����A�J�����g�R���e�L�X�g�ɂ��܂��B
	 * @param context �R���e�L�X�g
	 */
	public void pushContext(Context context);
	
	/**
	 * �w�肳�ꂽ���O�̃R���e�L�X�g���v�b�V�����A�J�����g�R���e�L�X�g�ɂ��܂��B
	 * @param name �V�����R���e�L�X�g�ɕt���閼�O
	 */
	public void pushContext(String name);
	
	/**
	 * �쐬�ς݃I�u�W�F�N�g���R���|�[�l���g�Ƃ��ēo�^���܂��B
	 * @param name �o�^����R���|�[�l���g��
	 * @param priority �R���|�[�l���g�̗D��x
	 * @param object �R���|�[�l���g�Ƃ��ēo�^����I�u�W�F�N�g
	 */
	public void put(String name, int priority, Object object);
	
	/**
	 * �R���|�[�l���g��o�^���܂��B
	 * @param name �o�^����R���|�[�l���g��
	 * @param priority �R���|�[�l���g�̗D��x
	 * @param scope �R���|�[�l���g�̃X�R�[�v
	 * @param factory �R���|�[�l���g�t�@�N�g��
	 */
	public void put(String name, int priority, String scope, ComponentFactory factory);
	
	/**
	 * �R���|�[�l���g��o�^���܂��B
	 * @param name �o�^����R���|�[�l���g��
	 * @param priority �R���|�[�l���g�̗D��x
	 * @param scope �R���|�[�l���g�̃X�R�[�v
	 * @param className �R���|�[�l���g�{�̂̃N���X��
	 * @param binder �R���|�[�l���g�̏��������s�� Binder �I�u�W�F�N�g
	 */
	public void put(String name, int priority, String scope, String className, Binder binder);
}
