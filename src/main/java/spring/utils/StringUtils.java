package spring.utils;

public class StringUtils {

	public static boolean isEmpty(String value) {
		if (value != null && !"".equals(value.trim())) return true;
		return false;

	}
}
