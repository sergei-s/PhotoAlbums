package net.samsolutions.utils;

import java.util.Random;

/**
 * Utility for creating random string
 */
public final class RandomString {

	private static final int LENGTH = 10;
	private static final String CHARACTERS = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private RandomString() { }
	/**
	 * @return random string
	 */
	public static String generate()	{
		Random rng = new Random();
	    char[] text = new char[LENGTH];
	    for (int i = 0; i < LENGTH; i++) {
	        text[i] = CHARACTERS.charAt(rng.nextInt(CHARACTERS.length()));
	    }
	    return new String(text);
	}
}
