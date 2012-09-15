package freja.etc;

import org.junit.Test;
import freja.etc.Util;

public class UtilTest {
	@Test
	public void testDontNullê¨å˜() {
		Util.dontNull(new Object(), "test");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDontNullé∏îs() {
		Util.dontNull(null, "test");
	}
	
}
