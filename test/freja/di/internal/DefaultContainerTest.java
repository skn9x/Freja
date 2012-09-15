package freja.di.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import org.junit.Test;
import freja.di.Container;
import freja.di.Interceptor;
import freja.di.MethodInvocation;
import freja.di.config.empty.EmptyBinder;
import freja.di.except.InjectionFailureException;
import freja.di.internal.DefaultContainer;

/**
 * @author SiroKuro
 */
public class DefaultContainerTest {
	private DefaultContainer container = new DefaultContainer();
	
	@Test
	public void testAspect01() {
		container.put("cat", 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		container.put("aspect:bracket", 0, Container.SCOPE_SINGLETON, BracketAspect.class.getName(), EmptyBinder.INSTANCE);
		
		container.aspect(Pattern.compile("cat"), "aspect:bracket", 0);
		
		Cat cat = (Cat) container.get("cat");
		assertNotNull(cat);
		assertEquals("[tama]", cat.getName());
	}
	
	@Test
	public void testAspect02() {
		container.put("cat-tama", 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		container.put("aspect:bracket", 0, Container.SCOPE_SINGLETON, BracketAspect.class.getName(), EmptyBinder.INSTANCE);
		
		container.aspect(Pattern.compile("cat"), "aspect:bracket", 0);
		
		Cat cat = (Cat) container.get("cat-tama");
		assertNotNull(cat);
		assertEquals("tama", cat.getName());
	}
	
	@Test
	public void testAspect03() {
		container.put("cat-tama", 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		container.put("cat-koma", 0, Container.SCOPE_SINGLETON, Koma.class.getName(), EmptyBinder.INSTANCE);
		container.put("cat-kuro", 0, Container.SCOPE_SINGLETON, Kuro.class.getName(), EmptyBinder.INSTANCE);
		container.put("aspect:bracket", 0, Container.SCOPE_SINGLETON, BracketAspect.class.getName(), EmptyBinder.INSTANCE);
		
		container.aspect(Pattern.compile("cat.+"), "aspect:bracket", 0);
		
		Cat cat = (Cat) container.get("cat-tama");
		assertNotNull(cat);
		assertEquals("[tama]", cat.getName());
		
		cat = (Cat) container.get("cat-koma");
		assertNotNull(cat);
		assertEquals("[koma]", cat.getName());
		
		cat = (Cat) container.get("cat-kuro");
		assertNotNull(cat);
		assertEquals("[kuro]", cat.getName());
	}
	
	@Test
	public void testAspect04() {
		container.put("cat", 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		container.put("aspect:bracket", 0, Container.SCOPE_SINGLETON, BracketAspect.class.getName(), EmptyBinder.INSTANCE);
		container.put("aspect:paren", 0, Container.SCOPE_SINGLETON, ParenAspect.class.getName(), EmptyBinder.INSTANCE);
		
		container.aspect(Pattern.compile(".*"), "aspect:bracket", 1);
		container.aspect(Pattern.compile(".*"), "aspect:paren", 0);
		
		Cat cat = (Cat) container.get("cat");
		assertNotNull(cat);
		assertEquals("[(tama)]", cat.getName());
	}
	
	@Test
	public void testAspect05() {
		container.put("cat", 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		container.put("aspect:bracket", 0, Container.SCOPE_SINGLETON, BracketAspect.class.getName(), EmptyBinder.INSTANCE);
		container.put("aspect:paren", 0, Container.SCOPE_SINGLETON, ParenAspect.class.getName(), EmptyBinder.INSTANCE);
		
		container.aspect(Pattern.compile(".*"), "aspect:paren", 1);
		container.aspect(Pattern.compile(".*"), "aspect:bracket", 0);
		
		Cat cat = (Cat) container.get("cat");
		assertNotNull(cat);
		assertEquals("([tama])", cat.getName());
	}
	
	@Test
	public void testAspect06() {
		container.put("cat", 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		container.put("aspect:bracket", 0, Container.SCOPE_SINGLETON, BracketAspect.class.getName(), EmptyBinder.INSTANCE);
		
		container.aspect(Pattern.compile(".*"), "aspect:bracket", 0);
		container.aspect(Pattern.compile(".*"), "aspect:bracket", 0);
		
		Cat cat = (Cat) container.get("cat");
		assertNotNull(cat);
		assertEquals("[tama]", cat.getName());
	}
	
	@Test
	public void testAspect07() {
		container.put("cat-tama", 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		container.put("cat-koma", 0, Container.SCOPE_SINGLETON, Koma.class.getName(), EmptyBinder.INSTANCE);
		container.put("cat-kuro", 0, Container.SCOPE_SINGLETON, Kuro.class.getName(), EmptyBinder.INSTANCE);
		container.put("aspect:bracket", 0, Container.SCOPE_SINGLETON, BracketAspect.class.getName(), EmptyBinder.INSTANCE);
		
		container.aspect(Cat.class, "aspect:bracket", 0);
		
		Cat cat = (Cat) container.get("cat-tama");
		assertNotNull(cat);
		assertEquals("[tama]", cat.getName());
		
		cat = (Cat) container.get("cat-koma");
		assertNotNull(cat);
		assertEquals("[koma]", cat.getName());
		
		cat = (Cat) container.get("cat-kuro");
		assertNotNull(cat);
		assertEquals("[kuro]", cat.getName());
	}
	
	@Test(expected = InjectionFailureException.class)
	public void testGetComponentNotFound() {
		container.get("zzz...");
	}
	
	@Test
	public void testGetContextScopeComponent() {
		container.put("tama", 0, "abc", Tama.class.getName(), EmptyBinder.INSTANCE);
		
		Cat tama01 = (Cat) container.get("tama");
		Cat tama02 = (Cat) container.get("tama");
		assertNotNull(tama01);
		assertNotNull(tama02);
		
		container.pushContext("abc");
		try {
			assertEquals("tama", tama01.getName());
			assertTrue(tama01.getId() == tama01.getId());
			assertEquals("tama", tama02.getName());
			assertTrue(tama01.getId() == tama02.getId());
		}
		finally {
			container.popContext();
		}
	}
	
	@Test
	public void testGetContextScopeComponent02() {
		container.put("tama", 0, "abc", Tama.class.getName(), EmptyBinder.INSTANCE);
		int id;
		
		Cat tama01 = (Cat) container.get("tama");
		assertNotNull(tama01);
		
		container.pushContext("abc");
		try {
			assertEquals("tama", tama01.getName());
			assertTrue(tama01.getId() == tama01.getId());
			id = tama01.getId();
		}
		finally {
			container.popContext();
		}
		
		container.pushContext("abc");
		try {
			assertEquals("tama", tama01.getName());
			assertTrue(tama01.getId() == tama01.getId());
			assertTrue(id != tama01.getId());
		}
		finally {
			container.popContext();
		}
	}
	
	@Test
	public void testGetInstanceScopeComponent() {
		container.put("tama", 0, Container.SCOPE_PROTOTYPE, Tama.class.getName(), EmptyBinder.INSTANCE);
		
		Cat tama01 = (Cat) container.get("tama");
		assertNotNull(tama01);
		assertEquals("tama", tama01.getName());
		
		Cat tama02 = (Cat) container.get("tama");
		assertNotNull(tama02);
		assertEquals("tama", tama02.getName());
		
		assertTrue(tama01.getId() != tama02.getId());
	}
	
	@Test
	public void testGetObject() {
		container.put("cat", 0, "tama");
		assertEquals("tama", container.get("cat"));
	}
	
	@Test
	public void testGetSingletonScopeComponent() {
		container.put("tama", 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		
		Cat tama01 = (Cat) container.get("tama");
		assertNotNull(tama01);
		assertEquals("tama", tama01.getName());
		
		Cat tama02 = (Cat) container.get("tama");
		assertNotNull(tama02);
		assertEquals("tama", tama02.getName());
		
		assertTrue(tama01.getId() == tama02.getId());
	}
	
	@Test
	public void testGetTwoComponent() {
		container.put("tama", 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		container.put("koma", 0, Container.SCOPE_SINGLETON, Koma.class.getName(), EmptyBinder.INSTANCE);
		
		Cat tama = (Cat) container.get("tama");
		Cat koma = (Cat) container.get("koma");
		assertNotNull(tama);
		assertNotNull(koma);
		
		assertEquals("tama", tama.getName());
		assertEquals("koma", koma.getName());
	}
	
	@Test
	public void testGetVolatileScopeComponent() {
		container.put("tama", 0, Container.SCOPE_VOLATILE, Tama.class.getName(), EmptyBinder.INSTANCE);
		
		Cat tama01 = (Cat) container.get("tama");
		assertNotNull(tama01);
		assertEquals("tama", tama01.getName());
		assertTrue(tama01.getId() != tama01.getId());
	}
	
	@Test(expected = InjectionFailureException.class)
	public void testPutWithPriority01() {
		container.put("cat", 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		container.put("cat", 0, Container.SCOPE_SINGLETON, Koma.class.getName(), EmptyBinder.INSTANCE);
		container.get("cat");
	}
	
	@Test
	public void testPutWithPriority02() {
		container.put("cat", 1, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		container.put("cat", 0, Container.SCOPE_SINGLETON, Koma.class.getName(), EmptyBinder.INSTANCE);
		
		Cat cat = (Cat) container.get("cat");
		assertNotNull(cat);
		assertEquals("tama", cat.getName());
	}
	
	@Test
	public void testPutWithPriority03() {
		container.put("cat", 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		container.put("cat", 1, Container.SCOPE_SINGLETON, Koma.class.getName(), EmptyBinder.INSTANCE);
		
		Cat cat = (Cat) container.get("cat");
		assertNotNull(cat);
		assertEquals("koma", cat.getName());
	}
	
	@Test
	public void testPutWithPriority04() {
		container.put("cat", 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		container.put("cat", 0, Container.SCOPE_SINGLETON, Koma.class.getName(), EmptyBinder.INSTANCE);
		container.put("cat", 1, Container.SCOPE_SINGLETON, Kuro.class.getName(), EmptyBinder.INSTANCE);
		
		Cat cat = (Cat) container.get("cat");
		assertNotNull(cat);
		assertEquals("kuro", cat.getName());
	}
	
	@Test(expected = InjectionFailureException.class)
	public void testPutWithPriority05() {
		container.put("cat", 1, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		container.put("cat", 1, Container.SCOPE_SINGLETON, Koma.class.getName(), EmptyBinder.INSTANCE);
		container.put("cat", 0, Container.SCOPE_SINGLETON, Kuro.class.getName(), EmptyBinder.INSTANCE);
		
		container.get("cat");
	}
	
	private static abstract class AbstractCat implements Cat {
		private static final AtomicInteger counter = new AtomicInteger();
		private final int id = counter.getAndIncrement();
		
		@Override
		public int getId() {
			return id;
		}
	}
	
	private static class BracketAspect implements Interceptor {
		@Override
		public Object intercept(String name, MethodInvocation invocation, Object[] args) throws Exception {
			if (invocation.getMethodName().equals("getName")) {
				return "[" + invocation.invoke(args) + "]";
			}
			else {
				return invocation.invoke(args);
			}
		}
	}
	
	private static interface Cat {
		public int getId();
		
		public String getName();
	}
	
	private static class Koma extends AbstractCat {
		@Override
		public String getName() {
			return "koma";
		}
	}
	
	private static class Kuro extends AbstractCat {
		@Override
		public String getName() {
			return "kuro";
		}
	}
	
	private static class ParenAspect implements Interceptor {
		@Override
		public Object intercept(String name, MethodInvocation invocation, Object[] args) throws Exception {
			if (invocation.getMethodName().equals("getName")) {
				return "(" + invocation.invoke(args) + ")";
			}
			else {
				return invocation.invoke(args);
			}
		}
	}
	
	private static class Tama extends AbstractCat {
		@Override
		public String getName() {
			return "tama";
		}
	}
}
