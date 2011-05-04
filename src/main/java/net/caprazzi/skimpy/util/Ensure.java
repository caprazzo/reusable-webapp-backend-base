package net.caprazzi.skimpy.util;


public class Ensure {

	public static void notEmpty(Object... objs) {
		for(int i=0; i< objs.length; i++) {
			Object o = objs[i];
			if (o == null) {
				throw new RuntimeException("Unexpected null in pos " + i);
			}
			if (o instanceof String && ((String)o).trim().length() == 0) {
				throw new RuntimeException("Unexpected empty string in pos " + i);
			}
		}
	}
}
