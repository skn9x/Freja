package freja.etc;

public class Util {
	private Util() {
		
	}
	
	/**
	 * 引数が null の場合に例外をスローする。
	 * @param obj 検査対象のオブジェクト
	 * @param argName 検査対象の引数名
	 */
	public static void dontNull(Object obj, String argName) {
		if (obj == null) {
			throw new IllegalArgumentException("argument " + argName + " require non-null object.");
		}
	}
	
	public static boolean isNotEmpty(String str) {
		return str != null && str.length() != 0;
	}
	
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
}
