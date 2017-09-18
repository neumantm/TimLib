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
package de.c_hack.tim.lib.DataManager;

import java.io.Serializable;

/**
 * A object for the data Handler. This contains the real data.
 * 
 * @author Tim Neumann
 * @param <T>
 *            The type of the primary ID
 */
public abstract class DataObject<T> implements Serializable, Cloneable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3408175220675078299L;

	/**
	 * @return the primary ID of the Data Object.
	 */
	public abstract T getPrimaryID();

	@Override
	public abstract DataObject<T> clone();
}
