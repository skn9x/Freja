package freja.di.config.multiple;

import freja.di.Binder;
import freja.di.except.InjectionFailureException;

/**
 * ������ {@link Binder} ��K�p���邱�Ƃ̂ł��� Binder �I�u�W�F�N�g�ł��B
 * @author SiroKuro
 */
public class MultipleBinder implements Binder {
	private final Binder[] binders;
	
	/**
	 * �I�u�W�F�N�g�����������܂��B�����Ɏw�肳�ꂽ Binder �����Ɏg�p����܂��B
	 * @param binders �K�p���� Binder �̔z��
	 */
	public MultipleBinder(Binder... binders) {
		assert binders != null;
		this.binders = binders.clone();
	}
	
	@Override
	public void bind(Object object) throws InjectionFailureException {
		for (Binder binder: binders) {
			binder.bind(object);
		}
	}
	
}
