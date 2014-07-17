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
 * This file is part of visitmeta visualization, version 0.1.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.datawrapper;





import java.io.IOException;
import java.util.Properties;

import org.apache.commons.collections15.map.HashedMap;
import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.util.PropertiesReaderWriter;

/**
 * Management class for properties.
 */
public class PropertiesManager {
	private static final Logger                              LOGGER         = Logger.getLogger(PropertiesManager.class);
	private static HashedMap<String, PropertiesReaderWriter> mPropertyFiles = new HashedMap<>();

	/**
	 * Get a value out of the {@link Properties} file.
	 * @param pFileName the Name of the File without ".properties".
	 * @param pKey
	 * @param pAlternative
	 * @return the value corresponding to key or alternative if value is null or an empty string.
	 */
	public static synchronized String getProperty(String pFileName, String pKey, String pAlternative) {
		LOGGER.trace("Method getProperty(" + pFileName + ", " + pKey + ", " + pAlternative + ") called.");
		String                 vPath         = null;
		PropertiesReaderWriter vReaderWriter = mPropertyFiles.get(pFileName);
		if(vReaderWriter == null) {
			/* Get PropertiesReaderWriter */
			try {
				vPath         = PropertiesManager.class.getClassLoader().getResource(pFileName + ".properties").getPath();
				vReaderWriter = new PropertiesReaderWriter(vPath, false);
				mPropertyFiles.put(pFileName, vReaderWriter);
			} catch (IOException | NullPointerException e) {
				LOGGER.warn("Couldn't load properties from file \"" + pFileName + ".properties\".");
				return pAlternative;
			}
		}
		/* Load Properties */
		return vReaderWriter.getProperty(pKey, pAlternative);
	}

	/**
	 * Stores a key value pair and writes a new corresponding {@link Properties} file.
	 * @param pFileName the Name of the File without ".properties".
	 * @param pKey
	 * @param pValue
	 */
	public static synchronized void storeProperty(String pFileName, String pKey, String pValue) {
		LOGGER.trace("Method storeProperty(" + pFileName + ", " + pKey + ", " + pValue + ") called.");
		String                 vPath         = null;
		PropertiesReaderWriter vReaderWriter = mPropertyFiles.get(pFileName);
		try {
			if(vReaderWriter == null) {
				/* Get PropertiesReaderWriter */
				vPath         = PropertiesManager.class.getClassLoader().getResource(pFileName + ".properties").getPath();
				vReaderWriter = new PropertiesReaderWriter(vPath, false);
				mPropertyFiles.put(pFileName, vReaderWriter);
			}
			/* Store Properties */
			vReaderWriter.storeProperty(pKey, pValue);
		} catch (IOException | NullPointerException e) {
			LOGGER.warn("Couldn't store properties in file \"" + pFileName + ".properties\".");
		}
	}


	public static synchronized void removeProperty(String pFileName, String pKey) {
		LOGGER.trace("Method removeProperty(" + pFileName + ", " + pKey + ", " + ") called.");
		String                 vPath         = null;
		PropertiesReaderWriter vReaderWriter = mPropertyFiles.get(pFileName);
		try {
			if(vReaderWriter == null) {
				/* Get PropertiesReaderWriter */
				vPath         = PropertiesManager.class.getClassLoader().getResource(pFileName + ".properties").getPath();
				vReaderWriter = new PropertiesReaderWriter(vPath, false);
				mPropertyFiles.put(pFileName, vReaderWriter);
			}
			/* Remove Properties */
			vReaderWriter.removeProperty(pKey);
		} catch (IOException | NullPointerException e) {
			LOGGER.warn("Couldn't remove property in file \"" + pFileName + ".properties\".");
		}
	}
}
