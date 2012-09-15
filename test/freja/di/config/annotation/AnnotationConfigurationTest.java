package freja.di.config.annotation;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import freja.di.Container;
import freja.di.Interceptor;
import freja.di.MethodInvocation;
import freja.di.config.annotation.AnnotationConfiguration;
import freja.di.config.annotation.Aspect;
import freja.di.config.annotation.Component;
import freja.di.internal.DefaultContainer;
import freja.di.internal.util.DIUtil;

/**
 * @author SiroKuro
 */
public class AnnotationConfigurationTest {
	private Container container = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		container = new DefaultContainer();
	}
	
	/**
	 * {@link freja.di.config.annotation.AnnotationConfiguration#config(freja.di.Container)}
	 * のためのテスト・メソッド。
	 */
	@Test
	public void testInitContainer01() {
		AnnotationConfiguration.getDefaultSetting().config(container);
		Animal component = (Animal) container.get(DIUtil.toComponentName(Animal.class));
		assertEquals("[[meow meow]]", component.bark());
	}
	
	private interface Animal {
		public String bark();
	}
	
	@Component("tama")
	@SuppressWarnings("unused")
	private static class Cat implements Animal {
		@Override
		public String bark() {
			return "meow meow";
		}
	}
	
	@Aspect(targetInterface = {
		Animal.class
	}, targetName = { // TODO クラス名で取得したとき、名前指定のアスペクトがウィーブされない
		"tama"
	})
	@SuppressWarnings("unused")
	private static class StrongAspect implements Interceptor {
		@Override
		public Object intercept(String name, MethodInvocation invocation, Object[] args) throws Exception {
			if (invocation.getMethodName().equals("bark"))
				return "[[" + invocation.invoke(args) + "]]";
			else
				return invocation.invoke(args);
		}
	}
}
