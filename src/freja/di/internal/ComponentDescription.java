package freja.di.internal;

/**
 * ŽåƒNƒ‰ƒX‚Í {@link ComponentPool}
 * @author SiroKuro
 */
class ComponentDescription {
	private final String name;
	private final int priority;
	private final String scope;
	
	public ComponentDescription(String name, int priority, String scope) {
		this.name = name;
		this.priority = priority;
		this.scope = scope;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	
	/**
	 * @return the scopeType
	 */
	public String getScope() {
		return scope;
	}
	
}
