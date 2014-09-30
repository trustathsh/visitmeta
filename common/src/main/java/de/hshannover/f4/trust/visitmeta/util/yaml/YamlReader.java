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
package de.hshannover.f4.trust.visitmeta.util.yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;

import de.hshannover.f4.trust.visitmeta.util.NullCheck;

/**
 * Class that encapsulates reading of YAML-files.
 * @author MR
 */
public final class YamlReader {

	/**
	 * Only static calls are allowed.
	 */
	private YamlReader() { }

	/**
	 * 
	 * Load a Object from a yml-File.
	 * Attention! If the File exists but it is empty then snakyaml returns null.
	 * @param fileName The file name or the file path to the yml-file.
	 * @param clazz The returned data-type.
	 * @param constructor A SnakeYAML BaseConstructor.
	 * @return The yml-File as Class<T>.
	 * @throws IOException If the file could not open, create or is a directory.
	 */
	@SuppressWarnings("unchecked")
	public static synchronized <T> T loadAs(String fileName, Class<T> clazz, BaseConstructor constructor)
			throws IOException {
		NullCheck.check(fileName, "fileName is null");
		NullCheck.check(clazz, "clazz is null");
		NullCheck.check(constructor, "constructor is null");

		FileReader fileReader = null;
		File f = null;

		try {
			fileReader = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			f = new File(fileName);
			if (f.isDirectory()) {
				throw new IOException(fileName + " is a directory");
			} else if (f.isFile()) {
				throw new IOException("Could not open " + fileName + ": " + e.getMessage());
			} else {
				// If it doens't exist and it's not a directory, try to create it.
				try {
					new FileWriter(fileName).close();
				} catch (IOException ee) {
					throw new IOException("Could not create " + fileName + ": " + ee.getMessage());
				}
				try {
					fileReader = new FileReader(fileName);
				} catch (IOException ee) {
					throw new IOException("Could not open " + fileName + ": " + ee.getMessage());
				}
			}
		}

		Yaml yaml = new Yaml(constructor);
		Object data = yaml.loadAs(fileReader, clazz);

		fileReader.close();
		return (T) data;
	}

	/**
	 * Load a yml-File as Map<String, Object>.
	 * @param fileName The file name or the file path to the yml-file.
	 * @param constructor A SnakeYAML BaseConstructor.
	 * @return If the File exists but it is empty then returns a empty Map<String, Object>.
	 * @throws IOException If the file could not open, create or is a directory.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> loadMap(String fileName, BaseConstructor constructor) throws IOException {
		Map<String, Object> ymlMap = loadAs(fileName, HashMap.class, constructor);
		if (ymlMap == null) {
			// then the File is empty return a empty HashMap
			return new HashMap<String, Object>();
		}
		return ymlMap;
	}

	/**
	 * Load a yml-File as Map<String, Object>.
	 * @param fileName
	 * @return If the File exists but it is empty then returns a empty Map<String, Object>.
	 * @throws IOException If the file could not open, create or is a directory.
	 */
	public static Map<String, Object> loadMap(String fileName) throws IOException {
		return loadMap(fileName, new Constructor());
	}

}
