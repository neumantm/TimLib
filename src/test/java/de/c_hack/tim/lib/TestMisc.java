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

import org.junit.Assert;
import org.junit.Test;

/**
 * Testing the Misc class
 * 
 * @author Tim Neumann
 */
public class TestMisc {

	/**
	 * Test method for
	 * {@link Misc#findStringInArr(java.lang.String[], java.lang.String, boolean)}.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testFindStringInArrStringArrayStringBoolean() {
		String[] arr = { "asd", "", "a", "bf", "123zu", "BSAG", "bGf" };

		Assert.assertEquals("Normal lowercase only", 0, Misc.findStringInArr(arr, "asd", true));
		Assert.assertEquals("Normal lowercase only", 2, Misc.findStringInArr(arr, "a", true));
		Assert.assertEquals("Normal lowercase only, empty", 1, Misc.findStringInArr(arr, "", true));
		Assert.assertEquals("Normal lowercase only", 3, Misc.findStringInArr(arr, "bf", true));
		Assert.assertEquals("Normal", 4, Misc.findStringInArr(arr, "123zu", true));
		Assert.assertEquals("Normal", 5, Misc.findStringInArr(arr, "BSAG", true));
		Assert.assertEquals("Normal", 6, Misc.findStringInArr(arr, "bGf", true));
		Assert.assertEquals("caseInsensitve 1", 0, Misc.findStringInArr(arr, "ASD", false));
		Assert.assertEquals("caseInsensitve 2", 0, Misc.findStringInArr(arr, "asd", false));
		Assert.assertEquals("caseInsensitve 3", 0, Misc.findStringInArr(arr, "aSd", false));
		Assert.assertEquals("caseInsensitve 4", 0, Misc.findStringInArr(arr, "AsD", false));
		Assert.assertEquals("caseInsensitve", 1, Misc.findStringInArr(arr, "", false));
		Assert.assertEquals("caseInsensitve", 5, Misc.findStringInArr(arr, "BSAG", false));
		Assert.assertEquals("caseInsensitve", 5, Misc.findStringInArr(arr, "bsag", false));
		Assert.assertEquals("caseInsensitve", 5, Misc.findStringInArr(arr, "BSaG", false));
		Assert.assertEquals("caseInsensitve", 6, Misc.findStringInArr(arr, "bGf", false));
		Assert.assertEquals("caseInsensitve", 6, Misc.findStringInArr(arr, "bgf", false));
		Assert.assertEquals("caseInsensitve", 6, Misc.findStringInArr(arr, "BGF", false));

		Assert.assertEquals("NOT FOUND, normal", -1, Misc.findStringInArr(arr, "as", true));
		Assert.assertEquals("NOT FOUND, normal", -1, Misc.findStringInArr(arr, "ASD", true));
		Assert.assertEquals("NOT FOUND, normal", -1, Misc.findStringInArr(arr, "BF", true));
		Assert.assertEquals("NOT FOUND, normal", -1, Misc.findStringInArr(arr, "123zU", true));
		Assert.assertEquals("NOT FOUND, cI", -1, Misc.findStringInArr(arr, "as", false));
		Assert.assertEquals("NOT FOUND, cI", -1, Misc.findStringInArr(arr, "5", false));
	}

}
