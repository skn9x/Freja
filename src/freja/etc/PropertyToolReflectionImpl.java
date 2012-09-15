package freja.etc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class PropertyToolReflectionImpl extends PropertyTool {
	
	@Override
	public void copy(Object from, Object to) {
		set(to, get(from)); // TODO Žb’èŽÀ‘•
	}
	
	@Override
	public Map<String, Object> get(Object obj) {
		Map<String, Object> result = new HashMap<String, Object>();
		for (Method getter: PropertyToolReflectionImpl.getGetter(obj.getClass())) {
			try {
				if (!getter.isAccessible()) {
					getter.setAccessible(true);
				}
				result.put(toPropName(getter.getName()), getter.invoke(obj));
			}
			catch (InvocationTargetException ex) {
				throw new RuntimeException(ex); // TODO
			}
			catch (IllegalArgumentException ex) {
				throw new RuntimeException(ex); // TODO
			}
			catch (IllegalAccessException ex) {
				throw new RuntimeException(ex); // TODO
			}
		}
		return result;
	}
	
	@Override
	public void set(Object obj, Map<String, Object> props) {
		for (Method getter: PropertyToolReflectionImpl.getSetter(obj.getClass())) {
			try {
				if (!getter.isAccessible()) {
					getter.setAccessible(true);
				}
				String name = toPropName(getter.getName());
				if (props.containsKey(name)) {
					getter.invoke(obj, props.get(name));
				}
			}
			catch (InvocationTargetException ex) {
				throw new RuntimeException(ex); // TODO
			}
			catch (IllegalArgumentException ex) {
				throw new RuntimeException(ex); // TODO
			}
			catch (IllegalAccessException ex) {
				throw new RuntimeException(ex); // TODO
			}
		}
	}
}
