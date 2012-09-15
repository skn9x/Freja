package freja.etc;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class PropertyTool {
	protected PropertyTool() {
		;
	}
	
	public abstract void copy(Object from, Object to);
	
	public abstract Map<String, Object> get(Object obj);
	
	public abstract void set(Object obj, Map<String, Object> props);
	
	public static PropertyTool getInstance() {
		// TODO ASM とか Javassist とか使うバージョンも返せるように改造
		return new PropertyToolReflectionImpl();
	}
	
	public static String toPropName(String methodName) {
		if (isEmpty(methodName)) {
			return "";
		}
		else {
			if (methodName.startsWith("get") || methodName.startsWith("set")) {
				methodName = methodName.substring(3);
			}
			else if (methodName.startsWith("is")) {
				methodName = methodName.substring(2);
			}
			if (1 <= methodName.length()) {
				char head = methodName.charAt(0);
				if (Character.isUpperCase(head)) {
					StringBuilder sb = new StringBuilder(methodName);
					sb.setCharAt(0, Character.toLowerCase(head));
					methodName = sb.toString();
				}
			}
			return methodName;
		}
	}
	
	protected static Set<Method> getGetter(Class<?> clazz) {
		Set<Method> result = new HashSet<Method>();
		for (Method mm: clazz.getMethods()) {
			int mfs = mm.getModifiers();
			if (mm.getParameterTypes().length == 0 && mm.getName().startsWith("get") && !Modifier.isStatic(mfs)
					&& Modifier.isPublic(mfs) && mm.getDeclaringClass() != Object.class) {
				result.add(mm);
			}
		}
		return result;
	}
	
	protected static Set<Method> getSetter(Class<?> clazz) {
		Set<Method> result = new HashSet<Method>();
		for (Method mm: clazz.getMethods()) {
			int mfs = mm.getModifiers();
			if (mm.getParameterTypes().length == 1 && mm.getName().startsWith("set") && !Modifier.isStatic(mfs)
					&& Modifier.isPublic(mfs) && mm.getDeclaringClass() != Object.class) {
				result.add(mm);
			}
		}
		return result;
	}
	
	private static boolean isEmpty(String text) {
		return text == null || text.length() == 0;
	}
}
