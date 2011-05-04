package net.caprazzi.skimpy.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Utils {

	private static final SecureRandom random = new SecureRandom();

	public static String nextSessionId() {
		return new BigInteger(130, random).toString(32);
	}

	public static boolean isEmpty(String email) {
		// TODO Auto-generated method stub
		return false;
	}
}
