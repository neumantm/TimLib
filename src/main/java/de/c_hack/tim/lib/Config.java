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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * The automated configuration loader.
 * 
 * @author Tim Neumann
 */
public class Config {
	/** The propertie instant used. */
	private Properties prop;
	/** All entries that we want to use. */
	private HashMap<String, ConfigEntry> entries;
	/** The path to write the conf to when saving. */
	private String path;
	/** The starting comment for the configuartion file. */
	private String comment;

	/**
	 * Initializes this class and reads the config from disk if the file exists.
	 * Otherwise it will be created and filled.
	 * 
	 * @param p_path
	 *            The path of the file.
	 * @param startingComment
	 *            The top comment.
	 * @param fields
	 *            A Map of Fields / Entries. For each name there must be a
	 *            standard value.
	 * @throws IOException
	 *             When writing or reading from the file.
	 */
	public Config(String p_path, String startingComment, HashMap<String, String> fields) throws IOException {
		this.path = p_path;
		this.comment = startingComment;

		this.entries = new HashMap<>();

		for (Entry<String, String> field : fields.entrySet()) {
			this.entries.put(field.getKey(), new ConfigEntry(field.getKey(), field.getValue()));
		}

		//Create necessary directorys.
		new File(p_path).getParentFile().mkdirs();

		//Create File if not existent
		if (!new File(p_path).exists()) {
			new File(p_path).createNewFile();
		}

		this.prop = new Properties();

		//Load existing file
		try (InputStream iS = new FileInputStream(p_path);) {

			this.prop.load(iS);

			//Parsing File
			boolean complete = true;
			for (ConfigEntry entry : this.entries.values()) {
				if (this.prop.containsKey(entry.name)) {
					entry.setValue(this.prop.getProperty(entry.name));
				}
				else {
					complete = false;
					this.prop.setProperty(entry.getName(), entry.getStandardValue());
					entry.setValue(entry.getStandardValue());
				}
			}

			//Write if not all options are present in file
			if (!complete) {
				writeConfToDisk();
			}

		}
	}

	/**
	 * Returns the current loaded value for the requested config parameter.
	 * 
	 * @param name
	 *            The name of the field to return
	 * @return The value requested
	 * @throws NoSuchElementException
	 *             if the given name was not found.
	 */
	public String getConfigValue(String name) throws NoSuchElementException {
		if (!this.entries.containsKey(name)) throw new NoSuchElementException("Did't find config setting'" + name + "'.");
		return this.entries.get(name).getValue();
	}

	/**
	 * Set's a config Entry to a specified value.
	 * 
	 * @param name
	 *            The entry to set.
	 * @param value
	 *            The value to set it to.
	 */
	public void setConfigValue(String name, String value) {
		if (!this.entries.containsKey(name)) throw new NoSuchElementException("Did't find config setting'" + name + "'.");
		this.entries.get(name).setValue(value);
	}

	/**
	 * Flushes the config to disk.
	 * 
	 * @throws IOException
	 *             File system exceptions
	 */
	public void writeConfToDisk() throws IOException {

		this.prop.clear();

		for (ConfigEntry entry : this.entries.values()) {
			this.prop.put(entry.name, entry.value);
		}

		try (OutputStream oS = new FileOutputStream(this.path)) {
			this.prop.store(oS, this.comment);
		}
	}

	/**
	 * One config entry.
	 */
	public static class ConfigEntry {
		/**
		 * Creates a new ConfigEntry with the name and standard value.
		 * 
		 * @param p_name
		 *            The name of the entry.
		 * @param p_standardValue
		 *            The standard value of the entry.
		 */
		public ConfigEntry(String p_name, String p_standardValue) {
			this.name = p_name;
			this.standardValue = p_standardValue;
		}

		/**
		 * The Name of the entry.
		 */
		String name = "";

		/**
		 * The standard value of the entry.
		 */
		String standardValue = "";

		/**
		 * The actual value of the entry.
		 */
		String value = "";

		/**
		 * Get's {@link #name name}
		 * 
		 * @return name
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * Get's {@link #standardValue standardValue}
		 * 
		 * @return standardValue
		 */
		public String getStandardValue() {
			return this.standardValue;
		}

		/**
		 * Get's {@link #value value}
		 * 
		 * @return value
		 */
		public String getValue() {
			return this.value;
		}

		/**
		 * Set's {@link #value value}
		 * 
		 * @param par_value
		 *            value
		 */
		public void setValue(String par_value) {
			this.value = par_value;
		}
	}
}
