package freja.di;

import java.text.MessageFormat;

/**
 * Freja DI フレームワーク内で発生した実行時例外のルートクラスです。
 * @author SiroKuro
 */
public abstract class FrejaRuntimeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8664484559295288606L;
	
	/**
	 * オブジェクトを初期化します。
	 */
	public FrejaRuntimeException() {
		;
	}
	
	/**
	 * オブジェクトを指定のメッセージで初期化します。
	 * @param message メッセージパターン
	 * @param args メッセージの置換文字列
	 */
	public FrejaRuntimeException(String message, Object... args) {
		super(createMessage(message, args));
	}
	
	/**
	 * オブジェクトを指定の原因とメッセージで初期化します。
	 * @param cause 原因となった例外オブジェクト
	 * @param message メッセージパターン
	 * @param args メッセージの置換文字列
	 */
	public FrejaRuntimeException(Throwable cause, String message, Object... args) {
		super(createMessage(message, args), cause);
	}
	
	/**
	 * メッセージ文字列を生成します。
	 * @param message メッセージパターン
	 * @param args メッセージの置換文字列
	 * @return 生成されたメッセージ文字列
	 */
	protected static String createMessage(String message, Object... args) {
		if (args == null || args.length == 0) {
			return message;
		}
		else {
			return MessageFormat.format(message, args);
		}
	}
	
}
