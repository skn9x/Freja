package freja.etc;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import freja.etc.PropertyTool;

public class PropertyToolTest {
	private final PropertyTool tool = PropertyTool.getInstance();
	
	@Test
	public void testGet空のオブジェクト() {
		Map<String, Object> prop = tool.get(new Object() {});
		assertEquals(0, prop.size());
	}
	
	@Test
	public void testGetプロパティ１個() {
		Map<String, Object> prop = tool.get(new Object() {
			public String getName() {
				return "tama";
			}
		});
		assertEquals(1, prop.size());
		assertEquals("tama", prop.get("name"));
	}
	
	@Test
	public void testSet空のオブジェクト() {
		tool.set(new Object() {}, new HashMap<String, Object>());
	}
	
	@Test
	public void testSetプロパティ１個() {
		Object obj = new Object() {
			private String name;
			
			public void setName(String name) {
				this.name = name;
			}
			
			public String getName() {
				return name;
			}
		};
		
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("name", "koma");
		tool.set(obj, props);
		
		Map<String, Object> prop = tool.get(obj);
		assertEquals(1, prop.size());
		assertEquals("koma", prop.get("name"));
	}
}
