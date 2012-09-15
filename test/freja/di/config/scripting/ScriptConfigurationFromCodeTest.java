package freja.di.config.scripting;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import freja.di.Container;
import freja.di.config.scripting.ScriptConfiguration;
import freja.di.internal.DefaultContainer;

/**
 * @author SiroKuro
 */
public class ScriptConfigurationFromCodeTest {
	private Container container = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		container = new DefaultContainer();
	}
	
	@Test
	public void test01() {
		String code = "";
		code += "freja.put('tama', '_singleton', '" + Cat.class.getName() + "');";
		
		ScriptConfiguration.fromCode("js", code).config(container);
		Animal animal = (Animal) container.get("tama");
		assertEquals("unknown says 'meow meow'.", animal.bark());
	}
	
	@Test
	public void test02() {
		String code = "";
		code += "freja.put('koma', '_singleton', '" + Cat.class.getName() + "', 'binder');";
		code += "function binder(object) { object.name = 'koma' }";
		
		ScriptConfiguration.fromCode("js", code).config(container);
		Animal animal = (Animal) container.get("koma");
		assertEquals("koma says 'meow meow'.", animal.bark());
	}
	
	private interface Animal {
		public String bark();
	}
	
	private static class Cat implements Animal {
		private String name = "unknown";
		
		@Override
		public String bark() {
			return name + " says 'meow meow'.";
		}
		
		/**
		 * @return the name
		 */
		@SuppressWarnings("unused")
		public String getName() {
			return name;
		}
		
		/**
		 * @param name the name to set
		 */
		@SuppressWarnings("unused")
		public void setName(String name) {
			this.name = name;
		}
	}
}
