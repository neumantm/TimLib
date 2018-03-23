/*
 * TimLib
 * 
 * A collection of useful classes and methods.
 * 
 * @version 0.2.0
 * @author Tim Neumann
 * @copyright (c) Tim Neumann 2015-2018
 * @license:
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */
package de.c_hack.tim.lib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * A automated logger.
 * 
 * It can be configured to print errors to stdout/stderr as well as multiple log
 * files.
 * You can send a message with the log function.
 * It is also possible to log entire exceptions with stacktrace and so on.
 * 
 * It uses log or severity levels.
 * 
 * It can be set to absorb stderr, but not if logging to stderr is enabled.
 * 
 * @author Tim Neumann
 */
public class Log {
	/**
	 * Loglevel Error
	 */
	public static final int ERROR = 0;
	/**
	 * Loglevel Warning
	 */
	public static final int WARN = 1;
	/**
	 * Loglevel Info
	 */
	public static final int INFO = 2;
	/**
	 * Loglevel Debug
	 */
	public static final int DEBUG = 3;

	/**
	 * A list of files that the log get's written to with according log levels.
	 * If a file is called std, stdout and stderr are used.
	 */
	private HashMap<String, LogFile> files;

	/** The used date format. */
	private SimpleDateFormat df = new SimpleDateFormat("EEE, dd.MM.yy HH:mm:ss");

	/** The highest log level to log to stderr instead of stdout. */
	private int levelToStdErr = -1;

	/**
	 * Creates a new logger. Without logging to stderr, only to stdout.
	 * 
	 * @param par_files
	 *            The files to log to. (If a file is named "std", use stdout and
	 *            stderr)
	 * @param par_level
	 *            The default log level to use.
	 * @throws IOException
	 *             File system exceptions
	 */
	public Log(String[] par_files, int par_level) throws IOException {
		this(par_files, par_level, -1);
	}

	/**
	 * Creates a new logger
	 * 
	 * @param par_files
	 *            The files to log to. (If a file is named "std", use stdout and
	 *            stderr)
	 * @param par_level
	 *            The default log level to use.
	 * @param par_levelToStdErr
	 *            The highest log level to log to stderr instead of stdout.
	 *            All log messages with this or a lower level will be logged to
	 *            stderr,
	 *            if they would have been logged to stdout.
	 *            Enter a negativ number for turning of logging to stderr.
	 *            If logging to stderr is enabled, redirectSTDErr to this
	 *            logger is not possible.
	 * @throws IOException
	 *             File system exceptions
	 */
	public Log(String[] par_files, int par_level, int par_levelToStdErr) throws IOException {
		this.levelToStdErr = par_levelToStdErr;
		this.files = new HashMap<>();
		for (String file : par_files) {
			this.files.put(file, new LogFile(file, par_level));
		}
	}

	/**
	 * Redirect the stderr of the application to this logger.
	 * This doesn't work if logging to stderr is enabled. (eg if levelToStdErr
	 * of the constructor is > -1)
	 */
	public void redirectSTDErr() {
		if (this.levelToStdErr > -1) throw new IllegalStateException("Can't redirect stderr, if logging to stderr is enabled.");
		//Create Stream
		OutputStream oS = new OutputStream() {
			/**
			 * The buffer for the errorStream
			 */
			String errorBuffer = "";

			//What to do when the Stream writes
			@Override
			public void write(int c) throws IOException {
				//Only write at new line
				if (c == '\n') {
					//Log line and reset buffer
					log(this.errorBuffer, Log.ERROR);
					this.errorBuffer = "";
				}
				else {
					//Add to buffer
					this.errorBuffer += (char) c;
				}
			}

			/**
			 * @see java.lang.Object#finalize()
			 */
			@Override
			protected void finalize() throws Throwable {
				super.finalize();
				close();
			}
		};

		PrintStream pS = new PrintStream(oS);

		//Set stdErr Stream
		System.setErr(pS);
	}

	/**
	 * Returns the loglevel as int. If unknown returns loglevel INFO
	 * 
	 * @param name
	 *            The name of the loglevel to return.
	 * @return The int representation of the log level.
	 */
	public static int getLogLevel(String name) {
		if (name.toLowerCase().equals("error")) return Log.ERROR;
		if (name.toLowerCase().equals("warn")) return Log.WARN;
		if (name.toLowerCase().equals("info")) return Log.INFO;
		if (name.toLowerCase().equals("debug")) return Log.DEBUG;
		return Log.INFO;
	}

	/**
	 * Handles a exception
	 * 
	 * @param e
	 *            The Exception
	 * @param level
	 *            The log level to log to.
	 * @param fatal
	 *            Wether the exception is fatal.
	 */
	public void logException(Exception e, int level, boolean fatal) {
		String msg;
		Iterator<Entry<String, LogFile>> it = this.files.entrySet().iterator();

		while (it.hasNext()) {
			Entry<String, LogFile> pair = it.next();

			if (level > pair.getValue().getLevel()) {
				continue;
			}

			switch (level) {
				case 0:
					msg = "[ERROR]";
				break;

				case 1:
					msg = "[WARN]";
				break;

				case 2:
					msg = "[INFO]";
				break;

				case 3:
					msg = "[DEBUG]";
				break;

				default:
					msg = "[UNKNOWN LOG]";
				break;
			}

			msg += "<" + this.df.format(new Date()) + "> ";
			msg += e.getMessage();
			msg += System.getProperty("line.separator");

			for (StackTraceElement el : e.getStackTrace()) {
				msg = msg + "   " + el.toString() + System.getProperty("line.separator");
			}

			if (fatal) {
				msg = msg + " This is fatal. Exiting!" + System.getProperty("line.separator");
			}

			if (pair.getValue().getName() == "std") {
				if (level < this.levelToStdErr) {
					System.err.println(msg);
				}
				else {
					System.out.print(msg);
				}
				continue;
			}

			try {
				pair.getValue().getbW().write(msg);
				pair.getValue().getbW().flush();

			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(1111);
			}
		}

		if (fatal) {
			System.exit(1);
		}

	}

	/**
	 * Logs the message to the specified level.
	 * 
	 * @param message
	 *            The log meassage to write
	 * @param level
	 *            The loglevel to use.
	 */
	public void log(String message, int level) {
		String msg;
		Iterator<Entry<String, LogFile>> it = this.files.entrySet().iterator();

		while (it.hasNext()) {
			Entry<String, LogFile> pair = it.next();

			if (level > pair.getValue().getLevel()) {
				continue;
			}

			switch (level) {
				case 0:
					msg = "[ERROR]";
				break;

				case 1:
					msg = "[WARN]";
				break;

				case 2:
					msg = "[INFO]";
				break;

				case 3:
					msg = "[DEBUG]";
				break;

				default:
					msg = "[UNKNOWN LOG]";
				break;
			}

			msg += "<" + this.df.format(new Date()) + "> ";
			msg += message;
			msg += System.getProperty("line.separator");

			if (pair.getValue().getName() == "std") {
				if (level < this.levelToStdErr) {
					System.err.println(msg);
				}
				else {
					System.out.print(msg);
				}
				continue;
			}

			try {
				pair.getValue().getbW().write(msg);
				pair.getValue().getbW().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Put's the file to the log with specified log level. See {@link #files
	 * files}
	 * 
	 * @param par_file
	 *            file
	 * @param level
	 *            The log level to use for this file
	 * @throws IOException
	 *             file system error
	 */
	public void addFile(String par_file, int level) throws IOException {
		this.files.put(par_file, new LogFile(par_file, level));
	}

	/**
	 * Removes the specified file from the logger. See {@link #files files}
	 * 
	 * @param par_file
	 *            The file to remove
	 */
	public void removeFile(String par_file) {
		this.files.remove(par_file);
	}

	/**
	 * Set's the loglevel of a specified logFile
	 * 
	 * @param file
	 *            The file to set the loglevel for.
	 * @param level
	 *            The new level to set.
	 */

	public void setLogLevel(String file, int level) {
		if (this.files.containsKey(file)) {
			LogFile lf = this.files.get(file);
			lf.setLevel(level);
			this.files.put(file, lf);

		}
	}

	/**
	 * Set's the loglevel for all logfiles with a specific current log level
	 * 
	 * @param level
	 *            The new level to set.
	 * @param nowLevel
	 *            Only the level of files with this level get changed.
	 */
	public void setLogLevel(int level, int nowLevel) {
		Iterator<Entry<String, LogFile>> it = this.files.entrySet().iterator();

		while (it.hasNext()) {
			Entry<String, LogFile> pair = it.next();
			if (pair.getValue().getLevel() == nowLevel) {
				setLogLevel(pair.getValue().getName(), level);
			}
		}
	}

	/**
	 * Set's the loglevel for all logfiles
	 * 
	 * @param level
	 *            The new level to set.
	 */
	public void setLogLevel(int level) {
		Iterator<Entry<String, LogFile>> it = this.files.entrySet().iterator();

		while (it.hasNext()) {
			Entry<String, LogFile> pair = it.next();
			setLogLevel(pair.getValue().getName(), level);
		}
	}

	/**
	 * The structure of a file to log to
	 */
	public static class LogFile {
		private String name;
		private int level;
		private BufferedWriter bW;

		/**
		 * New log file
		 * 
		 * @param p_name
		 *            The name of the log
		 * @param p_level
		 *            The log level to use
		 * @throws IOException
		 *             file system error
		 */
		public LogFile(String p_name, int p_level) throws IOException {
			this.name = p_name;
			this.level = p_level;

			if (p_name.equals("std")) {
				this.bW = null;
			}
			else {
				File f = new File(p_name).getAbsoluteFile();
				if (!f.getParentFile().exists()) {
					f.getParentFile().mkdirs();
				}
				this.bW = new BufferedWriter(new FileWriter(f, true));
			}
		}

		/**
		 * @see java.lang.Object#finalize()
		 */
		@Override
		protected void finalize() throws Throwable {
			super.finalize();
			this.bW.close();
		}

		/**
		 * Get's {@link #level level}
		 * 
		 * @return level
		 */
		public int getLevel() {
			return this.level;
		}

		/**
		 * Set's {@link #level level}
		 * 
		 * @param par_level
		 *            level
		 */
		public void setLevel(int par_level) {
			this.level = par_level;
		}

		/**
		 * Get's {@link #name name}
		 * 
		 * @return name
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * Get's {@link #bW bW}
		 * 
		 * @return bW
		 */
		public BufferedWriter getbW() {
			return this.bW;
		}
	}

}