package freja.etc;

import org.junit.Test;
import freja.etc.Util;

public class UtilTest {
	@Test
	public void testDontNull����() {
		Util.dontNull(new Object(), "test");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDontNull���s() {
		Util.dontNull(null, "test");
	}
	
}
