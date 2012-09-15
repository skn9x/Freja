package freja.di.internal;

import java.util.HashMap;
import java.util.Map;
import freja.di.ComponentFactory;
import freja.di.Context;

/**
 * Map オブジェクトによる Context の実装です。
 * @author SiroKuro
 */
public class SimpleMapContext implements Context {
	private final String name;
	private final Map<String, Object> components = new HashMap<String, Object>();
	
	/**
	 * オブジェクトを初期化します。
	 * @param name このコンテキストの名前
	 */
	public SimpleMapContext(String name) {
		assert name != null;
		this.name = name;
	}
	
	@Override
	public Object getComponent(String componentName, ComponentFactory factory) {
		synchronized (this) {
			Object result = components.get(componentName);
			if (result == null) {
				result = factory.getImplementation();
				components.put(componentName, result);
			}
			return result;
		}
	}
	
	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}
}
