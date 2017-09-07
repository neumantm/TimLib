/*
 * TimLib
 * 
 * @copyright (c) Tim Neumann 2015-2017
 * @license CC BY-NC-SA 4.0
 *
 * @author Tim Neumann
 * @version 1.2.0
 *
 */
package de.c_hack.tim.lib;

/**
 * A collection of useful functions
 * 
 * @author Tim Neumann
 */
public class Misc {
	/**
	 * Finds a string in a string array.
	 * Only matches complete elements
	 * 
	 * @param haystack
	 *            The array to search in
	 * @param needle
	 *            The string to search
	 * @return Where the needle was found or -1 if not found.
	 */
	public static int findStringInArr(String[] haystack, String needle) {
		return (Misc.findStringInArr(haystack, needle, true));
	}

	/**
	 * Finds a string in a string array.
	 * Only matches complete elements
	 * 
	 * @param haystack
	 *            The array to search in
	 * @param needle
	 *            The string to search
	 * @param caseSensitive
	 *            Whether case matters
	 * @return Where the needle was found or -1 if not found.
	 */
	public static int findStringInArr(String[] haystack, String needle, boolean caseSensitive) {
		String needleR = needle;
		if (!caseSensitive) {
			needleR = needle.toLowerCase();
		}
		for (int i = 0; i < haystack.length; i++) {

			String hay = haystack[i];

			if (!caseSensitive) {
				hay = hay.toLowerCase();
			}

			if (hay.equals(needleR)) return (i);
		}
		return (-1);
	}
}
