package freja.di.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.junit.Test;
import freja.di.Binder;
import freja.di.config.empty.EmptyBinder;
import freja.di.except.ComponentClassNotFoundException;
import freja.di.except.InjectionFailureException;
import freja.di.except.InstantiationFailureException;
import freja.di.internal.NewComponentFactory;

/**
 * @author SiroKuro
 */
public class NewComponentFactoryTest {
	
	@Test
	public void testGetInstance() {
		NewComponentFactory factory = new NewComponentFactory(Tama.class.getName(), EmptyBinder.INSTANCE);
		assertEquals(0, factory.getVersion());
		
		assertNotNull(factory.getImplementation());
		assertEquals(1, factory.getVersion());
		
		assertNotNull(factory.getImplementation());
		assertEquals(1, factory.getVersion());
	}
	
	@Test(expected = ComponentClassNotFoundException.class)
	public void testGetInstanceFailure01() {
		NewComponentFactory factory = new NewComponentFactory("HogeHoge", EmptyBinder.INSTANCE);
		factory.getImplementation();
	}
	
	@Test(expected = InstantiationFailureException.class)
	public void testGetInstanceFailure02() {
		NewComponentFactory factory = new NewComponentFactory(Kuro.class.getName(), EmptyBinder.INSTANCE);
		factory.getImplementation();
	}
	
	@Test(expected = InstantiationFailureException.class)
	public void testGetInstanceFailure03() {
		NewComponentFactory factory = new NewComponentFactory(AbstractCat.class.getName(), EmptyBinder.INSTANCE);
		factory.getImplementation();
	}
	
	@Test(expected = InstantiationFailureException.class)
	public void testGetInstanceFailure04() {
		NewComponentFactory factory = new NewComponentFactory(Koma.class.getName(), EmptyBinder.INSTANCE);
		factory.getImplementation();
	}
	
	@Test(expected = InjectionFailureException.class)
	public void testGetInstanceFailure05() {
		NewComponentFactory factory = new NewComponentFactory(Tama.class.getName(), new Binder() {
			@Override
			public void bind(Object object) throws InjectionFailureException {
				throw new InjectionFailureException();
			}
		});
		factory.getImplementation();
	}
	
	@Test(expected = InstantiationFailureException.class)
	public void testGetInstanceFailure06() {
		NewComponentFactory factory = new NewComponentFactory(Tama.class.getName(), new Binder() {
			@Override
			public void bind(Object object) throws InjectionFailureException {
				throw new InstantiationFailureException();
			}
		});
		factory.getImplementation();
	}
	
	@Test
	public void testNewProxyInstance() {
		NewComponentFactory factory = new NewComponentFactory(Tama.class.getName(), EmptyBinder.INSTANCE);
		Object result = factory.newProxyInstance(new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return null;
			}
		});
		
		assertNotNull(result);
		assertTrue(result instanceof Cat);
		assertTrue(result instanceof Serializable);
		assertTrue(result instanceof Cloneable);
	}
	
	private static abstract class AbstractCat implements Cat, Serializable, Cloneable {
		
	}
	
	private interface Cat {
		
	}
	
	private static class Koma extends AbstractCat {
		public Koma() {
			throw new RuntimeException("construct failure.");
		}
	}
	
	private static class Kuro extends AbstractCat {
		public Kuro(String name) {
			;
		}
	}
	
	private static class Tama extends AbstractCat {
		
	}
}
