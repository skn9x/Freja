package freja.di.config.annotation;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import freja.di.Container;
import freja.di.config.annotation.AnnotationBinder;
import freja.di.config.annotation.Bind;
import freja.di.config.empty.EmptyBinder;
import freja.di.except.InjectionFailureException;
import freja.di.internal.DefaultContainer;
import freja.di.internal.util.DIUtil;

/**
 * @author SiroKuro
 */
public class AnnotationBinderTest {
	private final Container container = new DefaultContainer();
	
	@Test
	public void testBindObject01() {
		AnnotationBinder.bindObject(container, new Object() {
			@SuppressWarnings("unused")
			private Cat cat;
		});
	}
	
	@Test(expected = InjectionFailureException.class)
	public void testBindObject02() {
		AnnotationBinder.bindObject(container, new Object() {
			@SuppressWarnings("unused")
			@Bind
			private Cat cat;
		});
	}
	
	@Test
	public void testBindObject03() {
		Object obj = new Object() {
			@Bind
			private Cat cat;
			
			@Override
			public String toString() {
				return cat.getName();
			}
		};
		container.put(DIUtil.toComponentName(Cat.class), 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		AnnotationBinder.bindObject(container, obj);
		
		assertEquals("tama", obj.toString());
	}
	
	@Test
	public void testBindObject04() {
		Object obj = new Object() {
			@Bind("tama")
			private Cat cat;
			
			@Override
			public String toString() {
				return cat.getName();
			}
		};
		container.put("tama", 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		AnnotationBinder.bindObject(container, obj);
		
		assertEquals("tama", obj.toString());
	}
	
	@Test
	public void testBindObject05() {
		Object obj = new Object() {
			@Bind("tama")
			private Cat tama;
			@Bind("koma")
			private Cat koma;
			
			@Override
			public String toString() {
				return tama.getName() + "&" + koma.getName();
			}
		};
		container.put("tama", 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		container.put("koma", 0, Container.SCOPE_SINGLETON, Koma.class.getName(), EmptyBinder.INSTANCE);
		AnnotationBinder.bindObject(container, obj);
		
		assertEquals("tama&koma", obj.toString());
	}
	
	@Test(expected = InjectionFailureException.class)
	public void testBindObject06() {
		container.put(DIUtil.toComponentName(Cat.class), 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		AnnotationBinder.bindObject(container, new Object() {
			@SuppressWarnings("unused")
			@Bind
			private final Cat cat = null;
		});
	}
	
	@Test
	public void testBindObject07() {
		Object obj = new Object() {
			@Bind
			public volatile Cat cat;
			
			@Override
			public String toString() {
				return cat.getName();
			}
		};
		container.put(DIUtil.toComponentName(Cat.class), 0, Container.SCOPE_SINGLETON, Tama.class.getName(), EmptyBinder.INSTANCE);
		AnnotationBinder.bindObject(container, obj);
		
		assertEquals("tama", obj.toString());
	}
	
	private static interface Cat {
		public String getName();
	}
	
	private static class Koma implements Cat {
		@Override
		public String getName() {
			return "koma";
		}
	}
	
	private static class Tama implements Cat {
		@Override
		public String getName() {
			return "tama";
		}
	}
}
