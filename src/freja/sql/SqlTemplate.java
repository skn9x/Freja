package freja.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import freja.etc.Util;

public class SqlTemplate {
	private final String sql;
	private final List<String> args;
	
	private SqlTemplate(String sql, List<String> args) {
		this.sql = sql;
		this.args = args;
	}
	
	public String getSql() {
		return sql;
	}
	
	public List<String> getArgs() {
		return args;
	}
	
	public static SqlTemplate parse(String code) {
		Parser parser = Parser.parse(code);
		return new SqlTemplate(parser.getSql(), parser.getArgs());
	}
	
	private static class Parser {
		private final String input;
		private final StringBuffer sql = new StringBuffer();
		private final List<String> args = new ArrayList<String>();
		private static final Pattern quote = Pattern.compile("([^\"\']*)(\"(?:\\\\\"|\"\"|[^\"])*\"|\'(?:\\\\\'|\'\'|[^\'])*\')");
		private static final Pattern praceHolder = Pattern.compile("#(#|\\{[A-Za-z0-9_.:\\s]+\\})?");
		
		private Parser(String input) {
			this.input = input;
		}
		
		public List<String> getArgs() {
			return Collections.unmodifiableList(args);
		}
		
		public String getSql() {
			return sql.toString();
		}
		
		private String appendPlaceHolder(String code) {
			if ("#".equals(code)) {
				return "#";
			}
			if (code.startsWith("{") && code.endsWith("}")) {
				code = code.substring(1, code.length() - 2).trim();
			}
			args.add(code);
			return "?";
		}
		
		private void parse() {
			Matcher mm = quote.matcher(input);
			int end = 0;
			while (mm.find()) {
				parseCode(mm.group(1));
				sql.append(mm.group(2));
				end = mm.end();
			}
			parseCode(input.substring(end));
		}
		
		private void parseCode(String code) {
			if (code.contains("?")) {
				throw new SqlParseException("? を含めることは出来ません。"); // TODO メッセージの変更
			}
			Matcher mm = praceHolder.matcher(code);
			while (mm.find()) {
				String ph = mm.group(1);
				if (Util.isEmpty(ph)) {
					throw new SqlParseException("プレースホルダの書式が不正です。"); // TODO メッセージの変更
				}
				mm.appendReplacement(sql, Matcher.quoteReplacement(appendPlaceHolder(ph)));
			}
			mm.appendTail(sql);
		}
		
		public static Parser parse(String code) {
			Parser result = new Parser(code);
			result.parse();
			return result;
		}
	}
}
