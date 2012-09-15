package freja.sql;
import java.io.*;

@SuppressWarnings("unused")
%%

%class FSqlScanner

//%debug
%unicode
%line
%column
%integer
%eofclose
%pack

%{
	private int token;
	private String value;
	private final StringBuilder code = new StringBuilder();
	
	public static final int TOKEN_EOF = 0;
	public static final int TOKEN_CODE = 1;
	public static final int TOKEN_STRING = 1;
	public static final int TOKEN_PARAM = 3;
	
	public boolean advance() throws java.io.IOException {
		token = yylex();
		return token != TOKEN_EOF;
	}
	
	public int token() {
		return token;
	}
	
	public String value() {
		return value;
	}
	
	private void errorInvalidChars(String text) {
		throw new RuntimeException();	// TODO
	}
	
	private void errorStringNotTerminated(String name) {
		throw new RuntimeException();	// TODO
	}
%}

%eofval{
	return TOKEN_EOF;
%eofval}

%state BLOCK_COMMENT, LINE_COMMENT, STRING_DQ, STRING_SQ, CODE

%%

<YYINITIAL>{
	"--"		{ value = code.toString(); code.setLength(0); code.append(" "); yybegin(LINE_COMMENT); return TOKEN_SQL; }
	"/*"		{ value = code.toString(); code.setLength(0); code.append(" "); yybegin(BLOCK_COMMENT); return TOKEN_SQL; }
	"#{"		{ value = code.toString(); code.setLength(0); code.append(" "); yybegin(CODE); return TOKEN_SQL; }
	"\""		{ value = code.toString(); code.setLength(0); code.append(yytext()); yybegin(STRING_DQ); return TOKEN_SQL; }
	"\'"		{ value = code.toString(); code.setLength(0); code.append(yytext()); yybegin(STRING_SQ); return TOKEN_SQL; }
	.			{ code.append(yytext()); }
}

<STRING_DQ>{
	"\"\""				{ code.append( yytext() ); }
	"\\\""				{ code.append( yytext() ); }
	"\""				{ code.append( yytext() ); value = string.toString(); yybegin(YYINITIAL); return TOKEN_STRING; }
	[^\n\r\"\\]+		{ string.append( yytext() ); }
	<<EOF>>				{ errorStringNotTerminated("string"); }
}
<STRING_SQ>{
	"\'\'"				{ code.append( yytext() ); }
	"\\\'"				{ code.append( yytext() ); }
	"\'"				{ code.append( yytext() ); value = string.toString(); yybegin(YYINITIAL); return TOKEN_STRING; }
	[^\n\r\'\\]+		{ string.append( yytext() ); }
	<<EOF>>				{ errorStringNotTerminated("string"); }
}
<BLOCK_COMMENT>{
	"*/"				{ yybegin(YYINITIAL); }
	[^*\r\n]*			{}
	[^*\r\n]*\n			{}
	"*"+[^*/\r\n]*		{}
	"*"+[^*/\r\n]*\n	{}
	<<EOF>>				{ errorStringNotTerminated("comment"); }
	.					{}
}
<LINE_COMMENT>{
	[\r\n]		{ yybegin(YYINITIAL); }
	[^\r\n]+	{}
}
