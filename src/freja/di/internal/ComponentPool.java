package freja.di.internal;

import java.util.HashMap;
import java.util.Map;
import freja.di.ComponentFactory;
import freja.di.Container;
import freja.di.except.InjectionFailureException;
import freja.internal.logging.Log;

/**
 * ��N���X�� {@link DefaultContainer}
 * @author SiroKuro
 */
class ComponentPool { // TODO ������
	private final Map<String, Holder> factories = new HashMap<String, Holder>();
	private final Map<String, Integer> priorities = new HashMap<String, Integer>();
	private final ContextManager contextManager;
	
	public ComponentPool(ContextManager contextManager) {
		assert contextManager != null;
		this.contextManager = contextManager;
	}
	
	public ComponentFactory get(String name) {
		assert contextManager != null;
		
		// �R���|�[�l���g�N���X���̌���
		Holder holder = factories.get(name);
		if (holder == null) {
			if (priorities.containsKey(name)) {
				throw new InjectionFailureException("�R���|�[�l���g {0} ������������܂����B", name);
			}
			else {
				throw new InjectionFailureException("�R���|�[�l���g {0} ��������܂���B", name);
			}
		}
		
		ComponentFactory result = holder.factory;
		if (holder.scopeType == ScopeType.INSTANCE) {
			// instance �̓v���L�V�I�u�W�F�N�g�ɓ����Ƃ��ɕ��
			result = new MemorizedComponentFactory(result);
		}
		return result;
	}
	
	public void put(ComponentDescription description, ComponentFactory factory) {
		assert description != null;
		assert factory != null;
		
		String name = description.getName();
		String scope = description.getScope();
		int priority = description.getPriority();
		Holder holder;
		
		if (Container.SCOPE_SINGLETON.equals(scope)) {
			holder = new Holder(ScopeType.SINGLETON, description, new MemorizedComponentFactory(factory));
			// singleton �� factory �ɓ����Ƃ��ɕ��
		}
		else if (Container.SCOPE_SOFTREF.equals(scope)) {
			holder = new Holder(ScopeType.SOFTREF, description, new SoftReferenceComponentFactory(factory));
			// softref �� factory �ɓ����Ƃ��ɕ��
		}
		else if (Container.SCOPE_PROTOTYPE.equals(scope)) {
			holder = new Holder(ScopeType.INSTANCE, description, factory);
			// instance �̓v���L�V�I�u�W�F�N�g�ɓ����Ƃ��ɕ��
		}
		else if (Container.SCOPE_VOLATILE.equals(scope)) {
			holder = new Holder(ScopeType.VOLATILE, description, factory);
			// volatile �͉�����܂����̂܂܎g��
		}
		else {
			holder = new Holder(ScopeType.CONTEXT, description, new ContextComponentFactory(contextManager, name, scope, factory));
			// ���̑��̃X�R�[�v�́A���̏�� ContextComponentFactory �ɕ��
		}
		
		// �}��
		synchronized (this) {
			Integer nowPriority = priorities.get(name);
			if (nowPriority == null) {
				// ���o�^�͖������Œǉ�
				factories.put(name, holder);
				priorities.put(name, priority);
			}
			else if (nowPriority == priority) {
				// ����D��x�Ȃ�� duplicated
				factories.remove(name);
				Log.warning("���� {0} �ɓ���D��x�̃R���|�[�l���g���o�^����Ă��邽�߁A�������������܂����B�o���Ƃ�����������܂��B", name);
			}
			else if (nowPriority < priority) {
				// �V�����R���|�[�l���g�����D��x�Ȃ�A������Œu��������
				factories.put(name, holder);
				priorities.put(name, priority);
				Log.config("���� {0} �ɒ�D��x�̃R���|�[�l���g���o�^����Ă��邽�߁A�㔭�� {1} ���D�悳��܂��B", name, factory.getName());
			}
			else if (priority < nowPriority) {
				// �V�����R���|�[�l���g����D��x�Ȃ�A����𖳎�����
				Log.config("���� {0} �ɍ��D��x�̃R���|�[�l���g���o�^����Ă��邽�߁A�㔭�� {1} �͖�������܂��B", name, factory.getName());
			}
		}
	}
	
	private static class Holder {
		public final ScopeType scopeType;
		@SuppressWarnings("unused")
		public final ComponentDescription description;
		public final ComponentFactory factory;
		
		public Holder(ScopeType scope, ComponentDescription description, ComponentFactory factory) {
			assert scope != null;
			assert description != null;
			assert factory != null;
			this.scopeType = scope;
			this.description = description;
			this.factory = factory;
		}
	}
	
	private enum ScopeType {
		SINGLETON, CONTEXT, INSTANCE, VOLATILE, NO_CACHE, SOFTREF,
	}
}
