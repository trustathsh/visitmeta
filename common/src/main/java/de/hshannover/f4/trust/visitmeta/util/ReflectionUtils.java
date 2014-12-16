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
 * This file is part of visitmeta-common, version 0.3.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

/**
 * Utility class for Reflection operations.
 * 
 * @author Bastian Hellmann
 * 
 */
public class ReflectionUtils {

	private static Logger logger = Logger.getLogger(ReflectionUtils.class);

	/**
	 * Checks a list of given class names if they implement a given interface
	 * and loads it via reflection.
	 * 
	 * @param classLoader
	 *            a given {@link ClassLoader} instance (configured with specific
	 *            URLs for JARs etc.)
	 * @param classNames
	 *            a {@link List} of class names
	 * @param t
	 *            a {@link Class} that specifies a interface class
	 * @return a fresh instance of the class type given as parameter;
	 *         <b<null</b> if no class was found
	 */
	@SuppressWarnings("unchecked")
	public static <T> T loadClass(ClassLoader classLoader,
			List<String> classNames, Class<T> t) {
		for (String className : classNames) {
			try {
				Class<?> classToLoad = Class.forName(className, true,
						classLoader);
				Class<?>[] interfaces = classToLoad.getInterfaces();

				boolean implementsInterface = false;
				for (Class<?> i : interfaces) {
					if (i.getCanonicalName().equals(t.getCanonicalName())) {
						implementsInterface = true;
					}
				}

				if (implementsInterface) {
					logger.trace(className + " implements "
							+ t.getCanonicalName());
					return (T) classToLoad.newInstance();
				} else {
					logger.trace(className + " does not implement "
							+ t.getCanonicalName());
				}
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException e) {
				logger.warn("could not load " + className + ": " + e);
			}
		}

		return null;
	}

	/**
	 * Creates a {@link List} of all classes within a given JAR file.
	 * 
	 * From:
	 * http://stackoverflow.com/questions/15720822/how-to-get-names-of-classes
	 * -inside-a-jar-file
	 * 
	 * @param jarFile
	 *            the JAR file to search for Classes
	 * @return a {@link List} with all found classes
	 * @throws IOException
	 */
	public static List<String> getClassNames(URL jarFile) throws IOException {
		List<String> classNames = new ArrayList<String>();
		ZipInputStream zip = new ZipInputStream(new FileInputStream(
				jarFile.getPath()));
		for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip
				.getNextEntry()) {
			if (entry.getName().endsWith(".class") && !entry.isDirectory()) {
				// This ZipEntry represents a class. Now, what class does it
				// represent?
				StringBuilder className = new StringBuilder();
				for (String part : entry.getName().split("/")) {
					if (className.length() != 0) {
						className.append(".");
					}
					className.append(part);
					if (part.endsWith(".class")) {
						className.setLength(className.length()
								- ".class".length());
					}
				}
				classNames.add(className.toString());
			}
		}

		zip.close();

		return classNames;
	}

}
