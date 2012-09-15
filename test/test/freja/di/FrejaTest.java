package test.freja.di;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import freja.di.Freja;
import freja.di.config.annotation.Bind;
import freja.di.config.annotation.Component;

public class FrejaTest {
	@Bind
	private Cat cat;
	
	@Before
	public void setUp() throws Exception {
		Freja.bind(this);
	}
	
	/**
	 * Freja.get から指定のコンポーネントを取得します。
	 */
	@Test
	public void test02() {
		assertEquals("tama", Freja.get("test.tama").toString());
	}
	
	/**
	 * this に DI したコンポーネントを使用します。
	 */
	@Test
	public void test01() {
		assertEquals("koma", cat.toString());
	}
	
	private interface Cat {
		@Override
		public String toString();
	}
	
	@Component(name = "test.tama")
	private static class Tama implements Cat {
		@Override
		public String toString() {
			return "tama";
		}
	}
	
	@Component(name = "test.koma", priority = 1)
	private static class Koma implements Cat {
		@Override
		public String toString() {
			return "koma";
		}
	}
}
