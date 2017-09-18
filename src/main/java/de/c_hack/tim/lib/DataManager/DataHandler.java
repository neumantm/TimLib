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

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * A generic data Handler. On construction it loads data from a given file.
 * Then you can read, set and remove data.
 * 
 * @author Tim Neumann
 * @param <I>
 *            The type of the primary ID
 * @param <T>
 *            The type of the data.
 */
public class DataHandler<I, T extends DataObject<I>> {

	private Class<T> theType;
	private HashMap<I, T> dataMap = new HashMap<>();
	private File location;

	/** Whether auto save every write action. */
	private boolean autoSaving;

	/**
	 * Creates a new standard DataHandler. (With auto saving and multiple files
	 * off.)
	 * 
	 * @param p_theDataType
	 *            The data type (class) this Data Handler is for.
	 * @param p_location
	 *            The file location of this Data Handler.
	 * @throws NullPointerException
	 *             When a parameter is null
	 * @throws IOException
	 *             When there is a problem with the file location
	 */
	public DataHandler(Class<T> p_theDataType, File p_location) throws NullPointerException, IOException {
		this(p_theDataType, p_location, false);
	}

	/**
	 * Creates a new DataHandler
	 * 
	 * @param p_theDataType
	 *            The data type (class) this Data Handler is for.
	 * @param p_location
	 *            The file location of this Data Handler.
	 * @param p_autoSaving
	 *            Whether to automatically save on write.
	 * @throws NullPointerException
	 *             When a parameter is null
	 * @throws IOException
	 *             When there is a problem with the file location
	 */
	public DataHandler(Class<T> p_theDataType, File p_location, boolean p_autoSaving) throws NullPointerException, IOException {
		if (p_theDataType == null || p_location == null) throw new NullPointerException();
		this.theType = p_theDataType;
		this.location = p_location;
		this.autoSaving = p_autoSaving;
		this.checkDataFile();
	}

	/**
	 * Get's all the data in a HashMap.
	 * 
	 * @return a HashMap with all Object of this DataHandler.
	 * @throws ClassCastException
	 *             When a object in the DataHandler can't be cloned safely.
	 */
	public HashMap<I, T> getAllData() throws ClassCastException {
		HashMap<I, T> ret = new HashMap<>();

		for (Entry<I, T> e : this.dataMap.entrySet()) {
			ret.put(e.getKey(), cloneData(e.getValue()));
		}

		return ret;
	}

	/**
	 * Get a data object.
	 * 
	 * @param primaryKey
	 *            The key for the data object to get.
	 * @return The data object or null, if the searched object is not found.
	 * @throws ClassCastException
	 *             When the retrieved object can't be cloned safely.
	 */
	public T getData(I primaryKey) throws ClassCastException {
		return cloneData(this.dataMap.get(primaryKey));
	}

	/**
	 * Adds a new data object or replaces a old one if the primaryKey already
	 * exists
	 * 
	 * @param obj
	 *            The data object to set.
	 * @throws ClassCastException
	 *             When the given object can't be cloned safely.
	 * @throws NullPointerException
	 *             When the parameter is null.
	 * @throws IOException
	 *             When saving fails.
	 */
	public void setData(T obj) throws ClassCastException, NullPointerException, IOException {
		if (obj == null) throw new NullPointerException("Can't set data, because the given object is null.");
		this.dataMap.put(obj.getPrimaryID(), cloneData(obj));
		if (this.autoSaving) {
			saveData();
		}
	}

	/**
	 * Removes a data object from the data handler.
	 * 
	 * @param primaryKey
	 *            The key for the data object to remove.
	 * @throws IOException
	 *             When saving fails.
	 * 
	 */
	public void removeData(I primaryKey) throws IOException {
		this.dataMap.remove(primaryKey);
		if (this.autoSaving) {
			saveData();
		}
	}

	/**
	 * Saves the data of this DataHandler
	 * 
	 * @throws IOException
	 *             If something goes wrong with the IO.
	 */
	public void saveData() throws IOException {
		//To save potential exceptions so we can throw them again after Stream is closed.
		IOException savedE = null;

		try (ObjectOutputStream oUS = new ObjectOutputStream(new FileOutputStream(this.location))) {
			for (T obj : this.dataMap.values()) {
				oUS.writeObject(obj);
			}
		} catch (IOException e) {
			savedE = e;
		}
		if (savedE != null) throw savedE;

	}

	/**
	 * Loads the data of the file system
	 * 
	 * @throws IOException
	 *             If something goes wrong with the IO.
	 * @throws WrongObjectException
	 *             If the file contains an object that is the wrong type.
	 * @throws ClassNotFoundException
	 *             If the class of an object in the file is not defined in this
	 *             project.
	 */
	public void loadData() throws IOException, WrongObjectException, ClassNotFoundException {
		IOException savedIOE = null;
		ClassCastException savedWOE = null;
		ClassNotFoundException savedCNFE = null;
		this.dataMap = new HashMap<>();
		try (ObjectInputStream oIS = new ObjectInputStream(new FileInputStream(this.location))) {
			while (true) {
				loadSingleDataObject(oIS.readObject());
			}
		} catch (EOFException e) {
			//Ignore. Just the way to know when done reading the file.(Yes this is weird.)
		} catch (IOException e) {
			savedIOE = e;
		} catch (WrongObjectException e) {
			savedWOE = e;
		} catch (ClassNotFoundException e) {
			savedCNFE = e;
		}
		if (savedIOE != null) throw savedIOE;
		if (savedWOE != null) throw savedWOE;
		if (savedCNFE != null) throw savedCNFE;
	}

	/**
	 * Safely clones a data object.
	 * 
	 * @param toClone
	 *            The object to clone
	 * @throws ClassCastException
	 *             When the given object isn't compatible to this DataHandlers
	 *             type or clones to another type
	 * @return A clone of the original object.
	 */
	private T cloneData(T toClone) throws ClassCastException {
		if (!toClone.getClass().equals(this.theType)) throw new ClassCastException("Can't process data, because the given object is not of the specific type this handler is configuered for. (Was " + toClone.getClass().getName() + ". Should be " + this.theType.getName());
		DataObject<I> clone = toClone.clone();
		if (!clone.getClass().equals(toClone.getClass())) throw new ClassCastException("The class " + toClone.getClass().getName() + " doesn't return an object of the same class when cloned!");
		@SuppressWarnings("unchecked")
		T cloneT = (T) clone;
		return cloneT;
	}

	/**
	 * Checks the data file location. If it doesn't exist it creates all
	 * necessary directories.
	 * 
	 * @throws IOException
	 *             When the directory can't be created or the given location is
	 *             a
	 *             directory or not writable
	 */
	private void checkDataFile() throws IOException {
		if (!this.location.exists()) {
			if (!this.location.getParentFile().mkdirs()) throw new IOException("Couldn't create directory for the data file!");
		}
		else if (!(this.location.isFile() && this.location.canWrite())) throw new IOException("Can't write to the data file.");
	}

	private void loadSingleDataObject(Object p_objectFromStream) throws ClassCastException, NullPointerException, IOException {
		if (!(p_objectFromStream.getClass().equals(this.theType))) throw new WrongObjectException("There is a incompatible Object in the data file. Object Type:" + p_objectFromStream.getClass().getName(), p_objectFromStream.getClass());
		@SuppressWarnings("unchecked")
		T tmpT = (T) p_objectFromStream;
		this.setData(tmpT);
	}
}
