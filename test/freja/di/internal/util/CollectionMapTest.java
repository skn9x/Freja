package freja.di.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import freja.di.internal.util.CollectionMap;

/**
 * @author SiroKuro
 */
public class CollectionMapTest {
	private CollectionMap<String, String> map = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		map = new CollectionMap<String, String>();
	}
	
	@Test
	public void testClear() {
		map.put("foo", "ABC");
		map.put("foo", "DEF");
		map.put("bar", "abc");
		map.put("bar", "def");
		map.clear();
		Collection<String> result01 = map.get("foo");
		Collection<String> result02 = map.get("bar");
		
		assertEquals(0, result01.size());
		assertEquals(0, result02.size());
	}
	
	@Test
	public void testPutAndGet() {
		Collection<String> result01 = map.get("foo");
		map.put("foo", "ABC");
		Collection<String> result02 = map.get("foo");
		map.put("foo", "DEF");
		Collection<String> result03 = map.get("foo");
		map.put("foo", "ABC");
		Collection<String> result04 = map.get("foo");
		
		assertEquals(0, result01.size());
		
		assertEquals(1, result02.size());
		assertTrue(result02.contains("ABC"));
		
		assertEquals(2, result03.size());
		assertTrue(result03.contains("ABC"));
		assertTrue(result03.contains("DEF"));
		
		assertEquals(3, result04.size());
		assertTrue(result04.contains("ABC"));
		assertTrue(result04.contains("DEF"));
	}
	
	@Test
	public void testPutAndGet02() {
		map.put("foo", "ABC");
		map.put("foo", "DEF");
		map.put("bar", "abc");
		map.put("bar", "def");
		Collection<String> result01 = map.get("foo");
		Collection<String> result02 = map.get("bar");
		
		assertEquals(2, result01.size());
		assertTrue(result01.contains("ABC"));
		assertTrue(result01.contains("DEF"));
		
		assertEquals(2, result02.size());
		assertTrue(result02.contains("abc"));
		assertTrue(result02.contains("def"));
	}
	
	@Test
	public void testRemove() {
		map.put("foo", "ABC");
		map.put("foo", "DEF");
		map.put("bar", "abc");
		map.put("bar", "def");
		map.remove("bar");
		Collection<String> result01 = map.get("foo");
		Collection<String> result02 = map.get("bar");
		
		assertEquals(2, result01.size());
		assertEquals(0, result02.size());
	}
}
