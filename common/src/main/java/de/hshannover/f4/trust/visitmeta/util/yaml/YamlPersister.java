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
 * This file is part of visitmeta common, version 0.0.6,
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

public class YamlPersister {

	private static final Charset UTF8 = Charset.forName("UTF-8");


	public static void persist(String fileName, Object obj, boolean append, Representer representer, DumperOptions options) throws FileNotFoundException {
		OutputStream outFile = new FileOutputStream(fileName, append);
		Writer outWriter = new OutputStreamWriter(outFile, UTF8);

		Yaml yaml = getYaml(null, representer, options);

		yaml.dump(obj, outWriter);
	}

	public static void persist(String fileName, Object obj, boolean append) throws FileNotFoundException {
		persist(fileName, obj, append, null, null);
	}

	public static void persist(String fileName, Object obj, boolean append, Representer representer) throws FileNotFoundException {
		persist(fileName, obj, append, representer, null);
	}

	public static void persist(String fileName, Object obj, boolean append, DumperOptions options) throws FileNotFoundException {
		persist(fileName, obj, append, null, options);
	}

	public static Object load(String fileName, BaseConstructor constructor) throws FileNotFoundException{
		InputStream  input = new FileInputStream(fileName);

		Yaml yaml = getYaml(constructor, null, null);
		Object data = yaml.load(input);

		return data;
	}

	@SuppressWarnings("unchecked")
	public static <T> T loadAs(String fileName, Class<T> clazz, BaseConstructor constructor) throws FileNotFoundException{
		InputStream  input = new FileInputStream(fileName);

		Yaml yaml = getYaml(constructor, null, null);
		Object data = yaml.loadAs(input, clazz);

		return (T) data;
	}

	public static Iterable<Object> loadAll(String fileName, BaseConstructor constructor) throws FileNotFoundException{
		InputStream  input = new FileInputStream(fileName);

		Yaml yaml = getYaml(constructor, null, null);
		Iterable<Object> data = yaml.loadAll(input);

		return data;
	}

	public static Iterable<Object> loadAll(String fileName) throws FileNotFoundException{
		return loadAll(fileName, null);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> loadMap(String fileName, BaseConstructor constructor) throws FileNotFoundException {
		return loadAs(fileName, HashMap.class, constructor);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> loadMap(String fileName) throws FileNotFoundException {
		return loadAs(fileName, HashMap.class, null);
	}

	@SuppressWarnings("unchecked")
	public static Set<Object> loadSet(String fileName, BaseConstructor constructor) throws FileNotFoundException {
		return loadAs(fileName, HashSet.class, constructor);
	}

	@SuppressWarnings("unchecked")
	public static Set<Object> loadSet(String fileName) throws FileNotFoundException {
		return loadAs(fileName, HashSet.class, null);
	}

	@SuppressWarnings("unchecked")
	public static List<Object> loadList(String fileName, BaseConstructor constructor) throws FileNotFoundException {
		return loadAs(fileName, ArrayList.class, constructor);
	}

	@SuppressWarnings("unchecked")
	public static List<Object> loadList(String fileName) throws FileNotFoundException {
		return loadAs(fileName, ArrayList.class, null);
	}

	private static Yaml getYaml(BaseConstructor constructor, Representer representer, DumperOptions options){
		BaseConstructor yamlConstructor;
		Representer yamlRepresenter;
		DumperOptions yamlOptions;

		if(constructor == null){
			yamlConstructor = new Constructor();
		}else{
			yamlConstructor = constructor;
		}

		if(representer == null){
			yamlRepresenter = new Representer();
		}else{
			yamlRepresenter = representer;
		}

		if(options == null){
			yamlOptions = new DumperOptions();
		}else{
			yamlOptions = options;
		}
		return new Yaml(yamlConstructor, yamlRepresenter, yamlOptions);
	}

}
