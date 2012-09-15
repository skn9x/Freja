package freja.di.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.Serializable;
import org.junit.Test;
import freja.di.Binder;
import freja.di.config.empty.EmptyBinder;
import freja.di.except.InjectionFailureException;
import freja.di.internal.HotSwapComponentFactory;
import freja.di.internal.hotswap.FileModifiedEventListener;
import freja.di.internal.hotswap.HotSwapService;

/**
 * @author SiroKuro
 */
public class HotSwapComponentFactoryTest {
	@Test
	public void testDoHotSwap() {
		HotSwapComponentFactory factory = new HotSwapComponentFactory(Tama.class.getName(), EmptyBinder.INSTANCE);
		assertEquals(0, factory.getVersion());
		
		assertTrue(factory.doHotSwap());
		assertEquals(1, factory.getVersion());
		
		assertTrue(factory.doHotSwap());
		assertEquals(2, factory.getVersion());
	}
	
	@Test
	public void testDoHotSwapFailure() {
		HotSwapComponentFactory factory = new HotSwapComponentFactory(Tama.class.getName(), new Binder() {
			@Override
			public void bind(Object object) {
				throw new InjectionFailureException();
			}
		});
		assertEquals(0, factory.getVersion());
		
		assertFalse(factory.doHotSwap());
		assertEquals(0, factory.getVersion());
	}
	
	@Test
	public void testDoHotSwapFailure02() {
		HotSwapComponentFactory factory = new HotSwapComponentFactory("HogeHoge", EmptyBinder.INSTANCE);
		assertEquals(0, factory.getVersion());
		
		assertFalse(factory.doHotSwap());
		assertEquals(0, factory.getVersion());
	}
	
	@Test
	public void testRegisterTo() {
		HotSwapComponentFactory factory = new HotSwapComponentFactory(Tama.class.getName(), EmptyBinder.INSTANCE);
		HotSwapServiceMock mock = new HotSwapServiceMock();
		factory.registerTo(mock);
		mock.fire();
	}
	
	private static abstract class AbstractCat implements Cat, Serializable, Cloneable {
		
	}
	
	private interface Cat {
		
	}
	
	private static class HotSwapServiceMock implements HotSwapService {
		private File target = null;
		private FileModifiedEventListener listener = null;
		
		@Override
		public void addFileModifiedListener(File target, FileModifiedEventListener listener) {
			assertNotNull(target);
			assertNotNull(listener);
			this.target = target;
			this.listener = listener;
		}
		
		public void fire() {
			if (target != null && listener != null) {
				listener.onFileModified(this, target);
			}
		}
		
		@Override
		public void start() {
			fail();
		}
	}
	
	private static class Tama extends AbstractCat {
		
	}
	
}
