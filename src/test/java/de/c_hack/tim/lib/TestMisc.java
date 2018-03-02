/*
 * TimLib
 * 
 * A collection of useful classes and methods.
 * 
 * @version 1.2.0
 * @author Tim Neumann
 * @copyright (c) Tim Neumann 2015-2017
 * @license:
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */
package de.c_hack.tim.lib;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

		Assertions.assertEquals(0, Misc.findStringInArr(arr, "asd", true), "Normal lowercase only");
		Assertions.assertEquals(2, Misc.findStringInArr(arr, "a", true), "Normal lowercase only");
		Assertions.assertEquals(1, Misc.findStringInArr(arr, "", true), "Normal lowercase only, empty");
		Assertions.assertEquals(3, Misc.findStringInArr(arr, "bf", true), "Normal lowercase only");
		Assertions.assertEquals(4, Misc.findStringInArr(arr, "123zu", true), "Normal");
		Assertions.assertEquals(5, Misc.findStringInArr(arr, "BSAG", true), "Normal");
		Assertions.assertEquals(0, Misc.findStringInArr(arr, "ASD", false), "caseInsensitve 1");
		Assertions.assertEquals(0, Misc.findStringInArr(arr, "asd", false), "caseInsensitve 2");
		Assertions.assertEquals(0, Misc.findStringInArr(arr, "aSd", false), "caseInsensitve 3");
		Assertions.assertEquals(0, Misc.findStringInArr(arr, "AsD", false), "caseInsensitve 4");
		Assertions.assertEquals(1, Misc.findStringInArr(arr, "", false), "caseInsensitve");
		Assertions.assertEquals(5, Misc.findStringInArr(arr, "BSAG", false), "caseInsensitve");
		Assertions.assertEquals(5, Misc.findStringInArr(arr, "bsag", false), "caseInsensitve");
		Assertions.assertEquals(5, Misc.findStringInArr(arr, "BSaG", false), "caseInsensitve");
		Assertions.assertEquals(6, Misc.findStringInArr(arr, "bGf", false), "caseInsensitve");
		Assertions.assertEquals(6, Misc.findStringInArr(arr, "bgf", false), "caseInsensitve");
		Assertions.assertEquals(6, Misc.findStringInArr(arr, "BGF", false), "caseInsensitve");

		Assertions.assertEquals(-1, Misc.findStringInArr(arr, "as", true), "NOT FOUND, normal");
		Assertions.assertEquals(-1, Misc.findStringInArr(arr, "ASD", true), "NOT FOUND, normal");
		Assertions.assertEquals(-1, Misc.findStringInArr(arr, "BF", true), "NOT FOUND, normal");
		Assertions.assertEquals(-1, Misc.findStringInArr(arr, "123zU", true), "NOT FOUND, normal");
		Assertions.assertEquals(-1, Misc.findStringInArr(arr, "as", false), "NOT FOUND, cI");
		Assertions.assertEquals(-1, Misc.findStringInArr(arr, "5", false), "NOT FOUND, cI");
	}

}
