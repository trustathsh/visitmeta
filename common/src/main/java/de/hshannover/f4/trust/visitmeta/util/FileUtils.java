/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of visitmeta-common, version 0.4.1,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.hshannover.f4.trust.visitmeta.util;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Utility class for {@link File} operations.
 * 
 * @author Bastian Hellmann
 *
 */
public class FileUtils {

	private static Logger logger = Logger.getLogger(FileUtils.class);

	/**
	 * Lists all direct subdirectories for a given directory.
	 * 
	 * @param startDirectoryPath the directory, for which subdirectories will be searched
	 * @return a {@link List} with all subdirectories for the given directory
	 */
	public static List<File> listSubDirectories(String startDirectoryPath) {
		List<File> subDirectories = new ArrayList<>();

		File startDirectory = new File(startDirectoryPath);
		if (startDirectory.isDirectory()) {
			File[] files = startDirectory.listFiles();

			for (File file : files) {
				if (file.isDirectory()) {
					subDirectories.add(file);
				}
			}
		} else {
			logger.warn("'" + startDirectoryPath + "' is not a directory.");
		}

		return subDirectories;
	}

	/**
	 * Searches for a folder within a given directory.
	 * 
	 * @param startDirectory the directory, in which the subdirectory will be searched
	 * @return the subfolder, if found; elsewise <b>null</b> is returned
	 */
	public static File findDirectory(File startDirectory, String folderToSearchFor) {
		File searchForFolder = new File(startDirectory.getAbsolutePath() + File.separator + folderToSearchFor);
		if (searchForFolder.exists()) {
			logger.debug("Found '" + folderToSearchFor + "' subfolder at: " + searchForFolder.getAbsolutePath());
			return searchForFolder;
		} else {
			logger.warn("Did not found '" + folderToSearchFor + "' subfolder at: " + searchForFolder.getAbsolutePath());
			return null;
		}
	}

	/**
	 * Returns a list of {@link URL} instances, containing all jar URL from
	 * the procedure directory.
	 * 
	 * @param directory the directory, in which JAR file swill be searched
	 * @return a {@link List} of {@link URL} for every JAR file in the given directory
	 */
	public static List<URL> listJarFiles(File directory) {
		List<URL> jarFiles = new ArrayList<URL>();

		String[] files = directory.list();

		for (String f : files) {
			if (f.endsWith(".jar")) {
				try {
					URL u = new File(directory + File.separator + f).toURI().toURL();
					jarFiles.add(u);
					logger.debug("Found jar: " + u);

				} catch (MalformedURLException e) {
					logger.warn("Could not load " + f + ": " + e.getMessage());
				}
			}
		}

		return jarFiles;
	}

	/**
	 * Partly from: http://blog.cedarsoft.com/2010/11/setting-java-library-path-programmatically/
	 * 
	 * Appends the given directory to the java.library.path and FORCES the JVM
	 * to reload all native library folder.
	 * 
	 * @param directoryPath
	 */
	public static void appendToLibraryPath(String directoryPath) {
		String originalJavaLibraryPath = System.getProperty("java.library.path");
		String newJavaLibraryPath = originalJavaLibraryPath + File.pathSeparator + directoryPath;

		System.setProperty("java.library.path", newJavaLibraryPath);
		logger.debug("Setting 'java.library.path' to '" + newJavaLibraryPath + "'");

		try {
			Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			logger.warn("Could not append '" + directoryPath + "' to java.library.path: " + e);
		}
	}
}
