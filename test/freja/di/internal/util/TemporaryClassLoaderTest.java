package freja.di.internal.util;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import example.components.SampleComponent;
import freja.di.config.annotation.Component;
import freja.di.internal.util.TemporaryClassLoader;

/**
 * @author SiroKuro
 */
public class TemporaryClassLoaderTest {
	
	@Test(expected = ClassNotFoundException.class)
	public void testFindNotFoundClass() throws ClassNotFoundException {
		ClassLoader loader = TemporaryClassLoader.newInstance(null);
		loader.loadClass("abc.def.Hogehoge");
	}
	
	@Test
	public void testFindObjectClass() throws ClassNotFoundException {
		ClassLoader loader = TemporaryClassLoader.newInstance();
		assertSame(Object.class, loader.loadClass(Object.class.getName()));
	}
	
	@Test
	public void testFindComponentAnnotationClass() throws ClassNotFoundException {
		ClassLoader loader = TemporaryClassLoader.newInstance();
		assertSame(Component.class, loader.loadClass(Component.class.getName()));
	}
	
	@Test
	public void testFindSampleComponentClass() throws ClassNotFoundException {
		ClassLoader loader = TemporaryClassLoader.newInstance();
		assertNotSame(SampleComponent.class, loader.loadClass(SampleComponent.class.getName()));
	}
}
