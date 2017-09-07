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
