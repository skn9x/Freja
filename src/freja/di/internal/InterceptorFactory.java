package freja.di.internal;

import java.util.Set;
import freja.di.Interceptor;

/**
 * @author SiroKuro
 */
public interface InterceptorFactory {
	public Interceptor getInterceptor(String componentName, Set<String> componentInterfaceNames);
}
