package freja.di.internal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import freja.di.Binder;
import freja.di.ComponentFactory;
import freja.di.Container;
import freja.di.Context;
import freja.di.Interceptor;
import freja.di.internal.hotswap.HotSwapAgent;
import freja.di.internal.hotswap.HotSwapService;
import freja.di.internal.util.DIUtil;
import freja.internal.logging.Log;

/**
 * {@link Container} の実装クラスです。
 * @author SiroKuro
 */
public class DefaultContainer implements Container {
	private static final boolean hotswapEnable;
	
	private final ContextManager contextManager = new ContextManager();
	private final ComponentPool components = new ComponentPool(contextManager);
	private final AspectPool aspects = new AspectPool();
	private final HotSwapService hotswapAgent;
	
	static {
		String flag = System.getProperty("freja.di.enableHotswap", "false");
		hotswapEnable = flag.equalsIgnoreCase("true");
		Log.config("freja.di.enableHotswap = {0}", hotswapEnable);
	}
	
	public DefaultContainer() {
		this(hotswapEnable);
	}
	
	public DefaultContainer(boolean useHotSwap) {
		if (useHotSwap) {
			hotswapAgent = new HotSwapAgent();
			hotswapAgent.start();
		}
		else {
			hotswapAgent = null;
		}
	}
	
	public void aspect(Class<?> _interface, String name, int priority) {
		Log.config("bindAspect: target [{0}] name [{1}]", _interface, name);
		aspects.bind(_interface.getName(), name, priority);
	}
	
	public void aspect(Pattern targetComponent, String name, int priority) {
		Log.config("bindAspect: target [{0}] name [{1}]", targetComponent, name);
		aspects.bind(targetComponent, name, priority);
	}
	
	@Override
	public <T> T get(Class<T> clazz) {
		if (clazz == null || !clazz.isInterface()) {
			throw new IllegalArgumentException("引数 clazz はインタフェイスを表すクラスである必要があります。");
		}
		return clazz.cast(get(DIUtil.toComponentName(clazz)));
	}
	
	public Object get(String name) {
		Log.trace("get: component [{0}]", name);
		return getComponent(name, true);
	}
	
	/**
	 * @see freja.di.internal.ContextManager#popContext()
	 */
	public void popContext() {
		contextManager.popContext();
	}
	
	@Override
	public void pushContext(Context context) {
		contextManager.pushContext(context);
	}
	
	/**
	 * @param name
	 * @see freja.di.internal.ContextManager#pushContext(Context)
	 */
	public void pushContext(String name) {
		pushContext(new SimpleMapContext(name));
	}
	
	@Override
	public void put(String name, int priority, Object object) {
		put(name, priority, SCOPE_VOLATILE, new NoProxyComponentFactory(object));
	}
	
	@Override
	public void put(String name, int priority, String scope, ComponentFactory factory) {
		Log.config("register: [{0}] name [{1}] scope [{2}]", factory.getName(), name, scope);
		
		ComponentDescription description = new ComponentDescription(name, priority, scope);
		components.put(description, factory);
	}
	
	public void put(String name, int priority, String scope, String componentClassName, Binder binder) {
		if (hotswapAgent != null) {
			HotSwapComponentFactory factory = new HotSwapComponentFactory(componentClassName, binder);
			factory.registerTo(hotswapAgent);
			put(name, priority, scope, factory);
		}
		else {
			put(name, priority, scope, new NewComponentFactory(componentClassName, binder));
		}
	}
	
	private Object getComponent(String name, boolean useAspect) {
		// コンポーネントの元ネタ作成
		ComponentFactory factory = components.get(name);
		
		if (factory instanceof NoProxyComponentFactory) {
			return factory.getImplementation();
		}
		else {
			ComponentFrontendHandler handler = new ComponentFrontendHandler(name, factory);
			Object result = factory.newProxyInstance(handler);
			
			// Aspect の Interceptor を接続
			if (useAspect) {
				handler.setInterceptor(getInterceptor(name, getAllSuperInterfaceNames(result.getClass())));
			}
			return result;
		}
	}
	
	private Interceptor getInterceptor(String componentName, Set<String> componentInterfaceNames) {
		List<String> aspectNames = aspects.get(componentName, componentInterfaceNames);
		if (aspectNames.isEmpty()) {
			return null;
		}
		Interceptor[] interceptors = new Interceptor[aspectNames.size()];
		for (int i = 0; i < interceptors.length; i++) {
			// Aspect のコンポーネントを取得
			interceptors[i] = (Interceptor) getComponent(aspectNames.get(i), false);
		}
		return MultipleInterceptor.concat(interceptors);
	}
	
	private static Set<String> getAllSuperInterfaceNames(Class<?> clazz) {
		Set<String> result = new HashSet<String>();
		for (Class<?> i: DIUtil.getAllSuperInterfaces(clazz)) {
			result.add(i.getName());
		}
		return result;
	}
}
