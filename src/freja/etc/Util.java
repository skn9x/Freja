package freja.etc;

public class Util {
	private Util() {
		
	}
	
	/**
	 * ������ null �̏ꍇ�ɗ�O���X���[����B
	 * @param obj �����Ώۂ̃I�u�W�F�N�g
	 * @param argName �����Ώۂ̈�����
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
