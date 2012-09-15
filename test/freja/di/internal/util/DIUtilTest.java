package freja.di.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.Ignore;
import org.junit.Test;
import freja.di.internal.util.DIUtil;

/**
 * @author SiroKuro
 */
public class DIUtilTest {
	@Test
	public void testToAspectName() {
		assertEquals("aspect:java.lang.String", DIUtil.toAspectName(String.class));
	}
	
	@Test
	public void testToComponentName() {
		assertEquals("class:java.lang.String", DIUtil.toComponentName(String.class));
	}
	
	@Test
	public void testToFile01() throws MalformedURLException, URISyntaxException {
		File result = DIUtil.toFile(new URL("file:/c:/example/test.jar"));
		assertNotNull(result);
		assertEquals(String.format("c:%sexample%stest.jar", File.separator, File.separator), result.getAbsolutePath());
	}
	
	@Test
	@Ignore
	public void testToFile02() throws MalformedURLException, URISyntaxException {
		File result = DIUtil.toFile(new URL("jar:file:/c:/example/test.jar"));
		assertNotNull(result);
		assertEquals(String.format("c:%sexample%stest.jar", File.separator, File.separator), result.getAbsolutePath());
	}
	
	@Test
	public void testToFile03() throws MalformedURLException, URISyntaxException {
		File result = DIUtil.toFile(new URL("jar:file:/c:/example/test.jar!/com/example/Example.class"));
		assertNotNull(result);
		assertEquals(String.format("c:%sexample%stest.jar", File.separator, File.separator), result.getAbsolutePath());
	}
}
