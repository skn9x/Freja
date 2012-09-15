package freja.sql;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SqlTemplateTest {
	@Test
	public void testParseプレースホルダのないSQL() {
		SqlTemplate template = SqlTemplate.parse("select 1");
		assertEquals("select 1", template.getSql());
		assertEquals(0, template.getArgs().size());
	}
	
	@Test
	public void testParse単一プレースホルダのあるSQL() {
		SqlTemplate template = SqlTemplate.parse("select * from TEST where id = #{p1}");
		assertEquals("select * from TEST where id = ?", template.getSql());
		assertEquals(1, template.getArgs().size());
	}
	
	@Test
	public void testParseエスケープ() {
		SqlTemplate template = SqlTemplate.parse("select * from TEST where id = ##");
		assertEquals("select * from TEST where id = #", template.getSql());
		assertEquals(0, template.getArgs().size());
	}
	
	@Test(expected = SqlParseException.class)
	public void testParse井桁が１個だけの場合() {
		SqlTemplate.parse("select * from TEST where id = #");
	}
	
	@Test
	public void testParse文字列中のプレースホルダ() {
		SqlTemplate template = SqlTemplate.parse("abc \"#{ p1 }\" def #{ p2 } ghi \'#{ p3 }\' jkl \"0\" mno #{ p4 } pqr");
		assertEquals("abc \"#{ p1 }\" def ? ghi \'#{ p3 }\' jkl \"0\" mno ? pqr", template.getSql());
		assertEquals(2, template.getArgs().size());
	}
	
	@Test
	public void testParse文字列のエスケープ() {
		assertEquals("\"\\\"#{p1}\"", SqlTemplate.parse("\"\\\"#{p1}\"").getSql());
		assertEquals("\'\\\'#{p1}\'", SqlTemplate.parse("\'\\\'#{p1}\'").getSql());
		assertEquals("\"\"\"#{p1}\"", SqlTemplate.parse("\"\"\"#{p1}\"").getSql());
		assertEquals("\'\'\'#{p1}\'", SqlTemplate.parse("\'\'\'#{p1}\'").getSql());
		assertEquals("\"\'\'#{p1}\"", SqlTemplate.parse("\"\'\'#{p1}\"").getSql());
		assertEquals("\'\"\"#{p1}\'", SqlTemplate.parse("\'\"\"#{p1}\'").getSql());
	}
	
	@Test(expected = SqlParseException.class)
	public void testParse禁止文字() {
		SqlTemplate.parse("abc ? def");
	}
	
	@Test
	public void testParse文字列中の禁止文字() {
		assertEquals("\"?\"", SqlTemplate.parse("\"?\"").getSql());
	}
}
