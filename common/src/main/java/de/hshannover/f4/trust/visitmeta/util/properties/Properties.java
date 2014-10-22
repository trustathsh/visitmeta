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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hshannover.f4.trust.visitmeta.util.NullCheck;

/**
 * Class that encapsulates application properties in YAML-files.
 * @author MR
 */
public class Properties {

	private PropertiesWriter mWriter;

	private PropertiesReader mReader;

	private String mFileName;
	
	private String mPreFixPropertyPath = "";

	private Properties(Properties propertyOriginal, String propertyKey) {
		mWriter = propertyOriginal.mWriter;
		mReader = propertyOriginal.mReader;
		mFileName = propertyOriginal.mFileName;
		mPreFixPropertyPath = addPath(propertyOriginal.mPreFixPropertyPath, propertyKey);
	}

	/**
	 * Create a ApplicationProperties for load and save Application-YAML files.
	 * @param fileName The file name or the file path to the yml-file.
	 * @throws PropertyException If the file could not open, create or is a directory.
	 */
	public Properties(String fileName) {
		NullCheck.check(fileName, "fileName is null");
		mFileName = fileName;
		mWriter = new PropertiesWriter(mFileName);
		mReader = new PropertiesReader(mFileName);
	}

	/**
	 * Load the application properties as Map<String, Object>.
	 * @throws PropertyException If the file could not open, create or is a directory.
	 */
	public Map<String, Object> load() throws PropertyException {
		try {
			return mReader.load();
		} catch (IOException e) {
			throw new PropertyException(e.getMessage());
		}
	}

	/**
	 * Save all properties in a YAML-files and override them.
	 * @throws PropertyException If the file could not open or is a directory.
	 */
	public void save(Map<String, Object> applicationConfigs) throws PropertyException {
		try {
			mWriter.save(applicationConfigs);
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
	public Properties get(String propertyKey) throws PropertyException {
		NullCheck.check(propertyKey, "propertyKey is null");
		Properties propertyCopy = new Properties(this, propertyKey);
		return propertyCopy;
	}

	/**
	 * Get the value from the property path.
	 * @param propertyPath foo.bar.property
	 * @return Object The value from the propertyPath.
	 * @throws PropertyException If the propertyKey-Path is not a property key or is a not part of a property path.
	 */
	@SuppressWarnings("unchecked")
	public Object getValue(String propertyPath) throws PropertyException {
		// add the PreFixPropertyPath if this Property is a copy deeper level
		String fullPropertyPath = addPath(mPreFixPropertyPath, propertyPath);

		// check propertyPath
		NullCheck.check(fullPropertyPath, "propertyPath is null");

		// split propertyPath
		String[] propertyKeyArray = fullPropertyPath.split("\\.");

		// load the application properties
		Map<String, Object> applicationConfigs = load();
		
		// iterate the root Map for every token
		for (int i = 0; i < propertyKeyArray.length; i++) {
			Object tmp = applicationConfigs.get(propertyKeyArray[i]);
			if (tmp == null) {
				if (propertyKeyArray.length > 1) {
					throw new PropertyException("property path[" + fullPropertyPath + "] have not a property key["
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
		if (o != null) {
			return o.toString();
		} else {
			// if the defaultValue is null
			return (String) o;
		}
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
	 * Return all keys of this Properties
	 * @return Set<String>
	 * @throws PropertyException 
	 */
	public Set<String> getKeySet() throws PropertyException {
		return load().keySet();
	}

	/**
	 * 
	 * Build a property path one level higher
	 * @param propertyPath foo.bar.property
	 * @return foo.bar
	 */
	@SuppressWarnings("unused")
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
	
	/**
	 * 
	 * Add a property path to an existing.
	 * If propertyPath == null or an empty String then return pathToAdded.
	 * @param propertyPath foo.bar
	 * @param pathToAdded new.property
	 * @return foo.bar.new.property
	 */
	private String addPath(String propertyPath, String pathToAdded) {
		if (propertyPath == null || propertyPath.equals("")) {
			return pathToAdded;
		} else {
			return new StringBuilder(propertyPath).append('.').append(pathToAdded).toString();
		}
	}

	@SuppressWarnings("unchecked")
	private void addToRootMap(String propertyPath, Object propertyValue)
			throws PropertyException {
		// split propertyPath
		String[] propertyKeyArray = propertyPath.split("\\.");

		// load configMap from disk
		Map<String, Object> configMap = load();

		// if simple token add too root map
		if (propertyKeyArray.length == 1) {
			configMap.put(propertyPath, propertyValue);
		}

		// add to root map
		Map<String, Object> deeperNestedMap = configMap;
		Object foundedValue = null;

		// search the subMap with the key[i] without the last key
		for(int i=0; i < propertyKeyArray.length - 1; i++){
			foundedValue = deeperNestedMap.get(propertyKeyArray[i]);
			if (foundedValue instanceof Map) {
				// go deeper if something was found
				deeperNestedMap = (Map<String, Object>) foundedValue;
			} else {
				// if foundedValue == null or not a Map
				// then build from the i position new nested map(s)
				String[] subArray = getCopyFrom(i+1, propertyKeyArray);
				Map<String, Object> newNestedMap = buildNewNestedMap(subArray, propertyValue);

				// add the newNestedMap map to the deeperNestedMap
				deeperNestedMap.put(propertyKeyArray[i], newNestedMap);
				break;
			}
			
			if (i == propertyKeyArray.length - 2) {
				deeperNestedMap.put(propertyKeyArray[propertyKeyArray.length - 1], propertyValue);
			}
		}

		// save all
		save(configMap);

	}

	/**
	 * Only for private use an Tests
	 * 
	 * @param mapKeys
	 * @param propertyValue
	 * @return
	 */
	protected Map<String, Object> buildNewNestedMap(String[] mapKeys, Object propertyValue) {
		Map<String, Object> rootMap = new HashMap<String, Object>();
		Map<String, Object> higherNestedMap = new HashMap<String, Object>();
		Map<String, Object> deeperNestedMap = new HashMap<String, Object>();

		// if only on key, add to the rootMap and return
		if (mapKeys.length - 1 == 0) {
			rootMap.put(mapKeys[0], propertyValue);
			return rootMap;
		}
		// after if we need min. one nestedMap

		// add deeperNestedMap to the higherNestedMap with the first key
		higherNestedMap.put(mapKeys[0], deeperNestedMap);

		// add the first nested map to the root map
		rootMap.putAll(higherNestedMap);

		// switch the higherNestedMap to the deeperNestedMap
		higherNestedMap = deeperNestedMap;

		// if mapKeys.length > 1
		for (int i=1; i <= mapKeys.length - 1; i++) {
			if (i == mapKeys.length -1) {
				higherNestedMap.put(mapKeys[i], propertyValue);
				break;
			}

			// make a new deeperNestedMap
			deeperNestedMap = new HashMap<String, Object>();

			// add deeperNestedMap to the higherNestedMap with the first key[i]
			higherNestedMap.put(mapKeys[i], deeperNestedMap);

			// switch the higherNestedMap to the deeperNestedMap
			higherNestedMap = deeperNestedMap;
		}

		return rootMap;
	}

	private String[] getCopyFrom(int from, String[] propertyKeyArray) {
		return Arrays.copyOfRange(propertyKeyArray, from, propertyKeyArray.length);
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
		
		// add the mPreFixPropertyPath if this Property is a copy with a deeper level
		String fullPropertyPath = addPath(mPreFixPropertyPath, propertyPath);

		// add propertyValue with the fullPropertyPath
		addToRootMap(fullPropertyPath, propertyValue);
	}


	@Override
	public String toString() {
		try {
			return load().toString();
		} catch (PropertyException e) {
			return e.toString();
		}
	}
}
