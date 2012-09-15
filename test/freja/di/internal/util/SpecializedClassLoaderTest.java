package freja.di.internal.util;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import example.components.SampleComponent;
import example.components.SampleComponent2;
import freja.di.internal.util.SpecializedClassLoader;

/**
 * @author SiroKuro
 */
public class SpecializedClassLoaderTest {
	
	@Test
	public void testSpecialLoadClass01() throws ClassNotFoundException {
		SpecializedClassLoader loader = SpecializedClassLoader.newInstance("");
		
		assertSame(SampleComponent.class, loader.loadClass(SampleComponent.class.getName()));
		assertSame(SampleComponent2.class, loader.loadClass(SampleComponent2.class.getName()));
	}
	
	@Test
	public void testSpecialLoadClass02() throws ClassNotFoundException {
		SpecializedClassLoader loader = SpecializedClassLoader.newInstance(SampleComponent.class.getName());
		
		assertNotSame(SampleComponent.class, loader.loadClass(SampleComponent.class.getName()));
		assertSame(SampleComponent2.class, loader.loadClass(SampleComponent2.class.getName()));
	}
}
