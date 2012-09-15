package freja.di.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 主クラスは {@link DefaultContainer}
 * @author SiroKuro
 */
class AspectPool { // TODO 同期化
	private final List<Holder> aspects = new ArrayList<Holder>();
	
	public void bind(Pattern targetComponent, String name, int priority) {
		aspects.add(new NameAspect(targetComponent, name, priority));
		Collections.sort(aspects, HolderComparator.INSTANCE); // TODO 毎回行うのは美しくない
	}
	
	public void bind(String _interface, String name, int priority) {
		aspects.add(new InterfaceAspect(_interface, name, priority));
		Collections.sort(aspects, HolderComparator.INSTANCE); // TODO 毎回行うのは美しくない
	}
	
	public List<String> get(String componentName, Set<String> interfaces) {
		List<String> result = new ArrayList<String>();
		for (Holder holder: aspects) {
			if (!result.contains(holder.componentName) && holder.isMatch(componentName, interfaces)) {
				result.add(holder.componentName);
			}
		}
		return result;
	}
	
	private static abstract class Holder {
		public final String componentName;
		public final int priority;
		
		public Holder(String componentName, int priority) {
			this.componentName = componentName;
			this.priority = priority;
		}
		
		public abstract boolean isMatch(String componentName, Set<String> interfaces);
	}
	
	private static class HolderComparator implements Comparator<Holder>, Serializable {
		private static final long serialVersionUID = 5701445202812112244L;
		public static final HolderComparator INSTANCE = new HolderComparator();
		
		@Override
		public int compare(Holder o1, Holder o2) {
			return Integer.valueOf(o2.priority).compareTo(o1.priority);
		}
	}
	
	private static class InterfaceAspect extends Holder {
		public final String _interface;
		
		public InterfaceAspect(String _interface, String componentName, int priority) {
			super(componentName, priority);
			this._interface = _interface;
		}
		
		@Override
		public boolean isMatch(String componentName, Set<String> interfaces) {
			return interfaces.contains(_interface);
		}
	}
	
	private static class NameAspect extends Holder {
		public final Pattern targetComponent;
		
		public NameAspect(Pattern targetComponent, String componentName, int priority) {
			super(componentName, priority);
			this.targetComponent = targetComponent;
		}
		
		@Override
		public boolean isMatch(String componentName, Set<String> interfaces) {
			return targetComponent.matcher(componentName).matches();
		}
	}
}
