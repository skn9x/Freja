package freja.di.internal;

import java.util.HashMap;
import java.util.Map;
import freja.di.ComponentFactory;
import freja.di.Container;
import freja.di.except.InjectionFailureException;
import freja.internal.logging.Log;

/**
 * 主クラスは {@link DefaultContainer}
 * @author SiroKuro
 */
class ComponentPool { // TODO 同期化
	private final Map<String, Holder> factories = new HashMap<String, Holder>();
	private final Map<String, Integer> priorities = new HashMap<String, Integer>();
	private final ContextManager contextManager;
	
	public ComponentPool(ContextManager contextManager) {
		assert contextManager != null;
		this.contextManager = contextManager;
	}
	
	public ComponentFactory get(String name) {
		assert contextManager != null;
		
		// コンポーネントクラス名の検索
		Holder holder = factories.get(name);
		if (holder == null) {
			if (priorities.containsKey(name)) {
				throw new InjectionFailureException("コンポーネント {0} が複数見つかりました。", name);
			}
			else {
				throw new InjectionFailureException("コンポーネント {0} が見つかりません。", name);
			}
		}
		
		ComponentFactory result = holder.factory;
		if (holder.scopeType == ScopeType.INSTANCE) {
			// instance はプロキシオブジェクトに入れるときに包む
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
			// singleton は factory に入れるときに包む
		}
		else if (Container.SCOPE_SOFTREF.equals(scope)) {
			holder = new Holder(ScopeType.SOFTREF, description, new SoftReferenceComponentFactory(factory));
			// softref は factory に入れるときに包む
		}
		else if (Container.SCOPE_PROTOTYPE.equals(scope)) {
			holder = new Holder(ScopeType.INSTANCE, description, factory);
			// instance はプロキシオブジェクトに入れるときに包む
		}
		else if (Container.SCOPE_VOLATILE.equals(scope)) {
			holder = new Holder(ScopeType.VOLATILE, description, factory);
			// volatile は何も包まずそのまま使う
		}
		else {
			holder = new Holder(ScopeType.CONTEXT, description, new ContextComponentFactory(contextManager, name, scope, factory));
			// その他のスコープは、この場で ContextComponentFactory に包む
		}
		
		// 挿入
		synchronized (this) {
			Integer nowPriority = priorities.get(name);
			if (nowPriority == null) {
				// 未登録は無条件で追加
				factories.put(name, holder);
				priorities.put(name, priority);
			}
			else if (nowPriority == priority) {
				// 同一優先度ならば duplicated
				factories.remove(name);
				Log.warning("名称 {0} に同一優先度のコンポーネントが登録されているため、競合が発生しました。双方とも無効化されます。", name);
			}
			else if (nowPriority < priority) {
				// 新しいコンポーネントが高優先度なら、そちらで置き換える
				factories.put(name, holder);
				priorities.put(name, priority);
				Log.config("名称 {0} に低優先度のコンポーネントが登録されているため、後発の {1} が優先されます。", name, factory.getName());
			}
			else if (priority < nowPriority) {
				// 新しいコンポーネントが低優先度なら、それを無視する
				Log.config("名称 {0} に高優先度のコンポーネントが登録されているため、後発の {1} は無視されます。", name, factory.getName());
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
