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
import java.io.FileWriter;
import java.io.IOException;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import de.hshannover.f4.trust.visitmeta.util.NullCheck;

public class YamlWriter {

	/**
	 * Save the obj to the yml-file.
	 * @param fileName The file name or the file path to the yml-file.
	 * @param object The Object to be stored.
	 * @param representer A SnakeYAML Representer.
	 * @param options A SnakeYAML DumperOptions.
	 * @throws IOException
	 */
	public static synchronized void persist(String fileName, Object object, Representer representer, DumperOptions options) throws IOException {
		NullCheck.check(fileName, "fileName is null");
		NullCheck.check(object, "object is null");
		NullCheck.check(representer, "representer is null");
		NullCheck.check(options, "options is null");

		FileWriter fileWriter = null;
		File f = null;

		try {
			fileWriter = new FileWriter(fileName);
		} catch (IOException e) {
			f = new File(fileName);
			if (f.isDirectory()) {
				throw new IOException(fileName + " is a directory");
			} else {
				throw new IOException("Could not create " + fileName + ": " + e.getMessage());
			}
		}

		Yaml yaml = new Yaml(representer, options);
		yaml.dump(object, fileWriter);
		fileWriter.close();
	}

	/**
	 * Save the obj to the yml-file.
	 * @param fileName The file name or the file path to the yml-file.
	 * @param obj The Object to be stored.
	 * @throws IOException
	 */
	public static void persist(String fileName, Object obj) throws IOException {
		persist(fileName, obj, new Representer(), new DumperOptions());
	}

	/**
	 * Save the obj to the yml-file.
	 * @param fileName The file name or the file path to the yml-file.
	 * @param obj The Object to be stored.
	 * @param representer A SnakeYAML Representer.
	 * @throws IOException
	 */
	public static void persist(String fileName, Object obj, Representer representer) throws IOException {
		persist(fileName, obj, representer, new DumperOptions());
	}

	/**
	 * Save the obj to the yml-file.
	 * @param fileName The file name or the file path to the yml-file.
	 * @param obj The Object to be stored.
	 * @param options A SnakeYAML DumperOptions.
	 * @throws IOException
	 */
	public static void persist(String fileName, Object obj, DumperOptions options) throws IOException {
		persist(fileName, obj, new Representer(), options);
	}

}
