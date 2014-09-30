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
 * This file is part of visitmeta common, version 0.1.2,
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
package de.hshannover.f4.trust.visitmeta.util.properties;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hshannover.f4.trust.visitmeta.util.NullCheck;

/**
 * Class that encapsulates application properties in YAML-files.
 * @author MR
 */
public class Properties {

	private PropertiesWriter mWriter;

	private PropertiesReader mReader;

	private String mFileName;

	private Map<String, Object> mApplicationConfigs;

	private Properties(Properties context) {
		mWriter = context.mWriter;
		mReader = context.mReader;
		mFileName = context.mFileName;
		mApplicationConfigs = context.mApplicationConfigs;
	}

	/**
	 * Create a ApplicationProperties for load and save Application-YAML files.
	 * @param fileName The file name or the file path to the yml-file.
	 * @throws PropertyException If the file could not open, create or is a directory.
	 */
	public Properties(String fileName) throws PropertyException {
		NullCheck.check(fileName, "fileName is null");
		mFileName = fileName;
		mWriter = new PropertiesWriter(mFileName);
		mReader = new PropertiesReader(mFileName);
		load();
	}

	/**
	 * Load the application properties as Map<String, Object>.
	 * @throws PropertyException If the file could not open, create or is a directory.
	 */
	public void load() throws PropertyException {
		try {
			mApplicationConfigs = mReader.load();
		} catch (IOException e) {
			throw new PropertyException(e.getMessage());
		}
	}

	/**
	 * Save all properties in a YAML-files and override them.
	 * @throws PropertyException If the file could not open or is a directory.
	 */
	public void save() throws PropertyException {
		try {
			mWriter.save(mApplicationConfigs);
		} catch (IOException e) {
			throw new PropertyException(e.getMessage());
		}
	}

	/**
	 * Get a new Properties from the property key. Only property keys accepted but not a path.
	 * @param propertyKey
	 * @return Properties A new Property instance of the propertyKey.
	 * @throws PropertyException If the propertyKey-param is not a property key or is a not part of a property path.
	 */
	@SuppressWarnings("unchecked")
	public Properties get(String propertyKey) throws PropertyException {
		NullCheck.check(propertyKey, "propertyKey is null");
		Properties context = new Properties(this);
		Object o = mApplicationConfigs.get(propertyKey);
		if (o == null) {
			throw new PropertyException(propertyKey + " is not a property key!");
		}
		if (o instanceof Map) {
			context.mApplicationConfigs = (Map<String, Object>) o;
		} else {
			throw new PropertyException(propertyKey + " is not a part of a property path!");
		}
		return context;
	}

	/**
	 * Get the value from the property path.
	 * @param propertyPath foo.bar.property
	 * @return Object The value from the propertyPath.
	 * @throws PropertyException If the propertyKey-Path is not a property key or is a not part of a property path.
	 */
	@SuppressWarnings("unchecked")
	public Object getValue(String propertyPath) throws PropertyException {
		// check propertyPath
		NullCheck.check(propertyPath, "propertyPath is null");

		// split propertyPath
		String[] propertyKeyArray = propertyPath.split("\\.");

		// iterate the root Map for every token
		Map<String, Object> applicationConfigs = mApplicationConfigs;
		for (int i = 0; i < propertyKeyArray.length; i++) {
			Object tmp = applicationConfigs.get(propertyKeyArray[i]);
			if (tmp == null) {
				if (propertyKeyArray.length > 1) {
					throw new PropertyException("property path[" + propertyPath + "] have not a property key["
							+ propertyKeyArray[i] + "] !");
				} else {
					throw new PropertyException(propertyKeyArray[i] + "] is not a property key!");
				}
			}
			if (tmp instanceof Map) {
				applicationConfigs = (Map<String, Object>) tmp;
			} else {
				return tmp;
			}
		}
		return applicationConfigs;
	}

	/**
	 * Get the value from the property path.
	 * If the property path does not exist, the default value is returned.
	 * @param propertyPath foo.bar.property
	 * @param defaultValue
	 * @return Object The value from the propertyPath.
	 */
	public Object getValue(String propertyPath, Object defaultValue) {
		Object o = null;
		try {
			o = getValue(propertyPath);
		} catch (PropertyException e) {
			return defaultValue;
		}
		return o;
	}

	/**
	 * Get the String-value from the property path. Throw a PropertyException when the value is not a String.
	 * @param propertyPath foo.bar.property
	 * @return String
	 * @throws PropertyException If the propertyKey-Path is not a property key or is a not part of a property path.
	 */
	public String getString(String propertyPath) throws PropertyException {
		Object o =  getValue(propertyPath);
		return o.toString();
	}

	/**
	 * Get the String-value from the property path.
	 * If the property path does not exist, the default value is returned.
	 * @param propertyPath foo.bar.property
	 * @param defaultValue
	 * @return String
	 */
	public String getString(String propertyPath, String defaultValue) {
		Object o =  getValue(propertyPath, defaultValue);
		return o.toString();
	}

	/**
	 * Get the int-value from the property path. Throw a PropertyException when the value is not a int.
	 * @param propertyPath foo.bar.property
	 * @return int
	 * @throws PropertyException If the propertyKey-Path is not a property key or is a not part of a property path.
	 */
	public int getInt(String propertyPath) throws PropertyException {
		Object o =  getValue(propertyPath);
		return Integer.parseInt(o.toString());
	}

	/**
	 * Get the int-value from the property path.
	 * If the property path does not exist, the default value is returned.
	 * @param propertyPath foo.bar.property
	 * @param defaultValue
	 * @return int
	 */
	public int getInt(String propertyPath, int defaultValue) {
		Object o =  getValue(propertyPath, defaultValue);
		return Integer.parseInt(o.toString());
	}

	/**
	 * Get the double-value from the property path. Throw a PropertyException when the value is not a double.
	 * @param propertyPath foo.bar.property
	 * @return double
	 * @throws PropertyException If the propertyKey-Path is not a property key or is a not part of a property path.
	 */
	public double getDouble(String propertyPath) throws PropertyException {
		Object o =  getValue(propertyPath);
		return Double.parseDouble(o.toString());
	}

	/**
	 * Get the double-value from the property path.
	 * If the property path does not exist, the default value is returned.
	 * @param propertyPath foo.bar.property
	 * @param defaultValue
	 * @return double
	 */
	public double getDouble(String propertyPath, double defaultValue) {
		Object o =  getValue(propertyPath, defaultValue);
		return Double.parseDouble(o.toString());
	}

	/**
	 * Get the boolean-value from the property path. Throw a PropertyException when the value is not a boolean.
	 * @param propertyPath foo.bar.property
	 * @return boolean
	 * @throws PropertyException If the propertyKey-Path is not a property key or is a not part of a property path.
	 */
	public boolean getBoolean(String propertyPath) throws PropertyException {
		Object o =  getValue(propertyPath);
		return Boolean.parseBoolean(o.toString());
	}

	/**
	 * Get the boolean-value from the property path.
	 * If the property path does not exist, the default value is returned.
	 * @param propertyPath foo.bar.property
	 * @param defaultValue
	 * @return boolean
	 */
	public boolean getBoolean(String propertyPath, boolean defaultValue) {
		Object o =  getValue(propertyPath, defaultValue);
		return Boolean.parseBoolean(o.toString());
	}

	/**
	 * 
	 * Build a property path one level higher
	 * Example:
	 * @param foo.bar.property
	 * @return foo.bar
	 */
	private String removeLastToken(String propertyPath) {
		String[] propertyKeyArray = propertyPath.split("\\.");

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < propertyKeyArray.length - 2; i++) {
			sb.append(propertyKeyArray[i]);
			sb.append('.');
		}
		sb.append(propertyKeyArray[propertyKeyArray.length - 2]);
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> addRecursiveInExistingMap(String propertyPath, Object propertyValue)
			throws PropertyException {
		// split propertyPath
		String[] propertyKeyArray = propertyPath.split("\\.");

		// if simple token add too root map
		if (propertyKeyArray.length == 1) {
			mApplicationConfigs.put(propertyPath, propertyValue);
			return mApplicationConfigs;
		}

		// the path one level higher
		String newPath = removeLastToken(propertyPath);

		// find the value for this new path
		Object foundedValue = null;
		try {
			foundedValue = getValue(newPath);
		} catch (PropertyException e) {
			foundedValue = null;
		}

		// If map founded the put new value, else call recursive with new path and new Map
		Map<String, Object> foundedMap = null;
		if (foundedValue instanceof Map) {
			foundedMap = (Map<String, Object>) foundedValue;
			foundedMap.put(propertyKeyArray[propertyKeyArray.length - 1], propertyValue);
		} else {
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.put(propertyKeyArray[propertyKeyArray.length - 1], propertyValue);
			foundedMap = addRecursiveInExistingMap(newPath, newMap);
		}

		return foundedMap;
	}

	/**
	 * Save the value under the key.
	 * Maps and List can only obtain simple data-types.
	 * @param propertyPath foo.bar.property
	 * @param propertyValue Only String|int|double|boolean|Map|List
	 * @throws PropertyException If the propertyKey-Path is not a property key or is a not part of a property path.
	 * Only String|int|double|boolean|Map|List can be set!
	 */
	public void set(String propertyPath, Object propertyValue) throws PropertyException {
		// check propertyPath
		NullCheck.check(propertyPath, "propertyPath is null");

		// check propertyValue
		if (!(propertyValue instanceof String) && !(propertyValue instanceof Integer)
				&& !(propertyValue instanceof Double) && !(propertyValue instanceof Boolean)
				&& !(propertyValue instanceof Map) && !(propertyValue instanceof List)) {
			throw new PropertyException("Only String|int|double|boolean|Map|List can be set!");
		}

		// add to root map
		addRecursiveInExistingMap(propertyPath, propertyValue);

		// save all
		save();
	}


	@Override
	public String toString() {
		return mApplicationConfigs.toString();
	}
}
