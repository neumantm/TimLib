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

/**
 * This exception adds a field to save the object type encountered to the Class
 * Cast Exception
 * 
 * @author Tim Neumann
 */
public class WrongObjectException extends ClassCastException {
	/**
	 * Generated Serial Version ID.
	 */
	private static final long serialVersionUID = 2490528924364531589L;
	private Class<?> theClass;

	/**
	 * Constructs a WrongObjectException with the specified detail message and
	 * the class that was encountered.
	 * 
	 * @param p_message
	 *            The message
	 * @param p_theClass
	 *            The class the was encountered
	 */
	public WrongObjectException(String p_message, Class<?> p_theClass) {
		super(p_message);
		this.theClass = p_theClass;
	}

	/**
	 * @return The class that was encountered.
	 */
	public Class<?> getTheClass() {
		return this.theClass;
	}
}
