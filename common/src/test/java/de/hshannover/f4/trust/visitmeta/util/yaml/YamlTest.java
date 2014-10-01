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
 * This file is part of visitmeta dataservice, version 0.1.2,
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

package de.hshannover.f4.trust.visitmeta.util.yaml;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import de.hshannover.f4.trust.visitmeta.util.Util;

public class YamlTest {

	private final String mFilePath_notExists = "src/test/resources/123456789/test.yml";
	private final String mFilePath_yamlReaderCreateNewFileTest = "src/test/resources/yamlReaderCreateNewFileTest.yml";
	private final String mFilePath_yamlWriterCreateNewFileTest = "src/test/resources/yamlWriterCreateNewFileTest.yml";
	private final String mFilePath_simpleDatatypsTest = "src/test/resources/testSimpleDatatyps.yml";
	private final String mFilePath_collectionsTest = "src/test/resources/testCollections.yml";
	private final String mFilePath_emptyMapTest = "src/test/resources/testEmptyMap.yml";

	private final String mString_YamlWriterSaveObject = "YamlWriterSaveObject";
	private final String mSimpleDatatypsTest_String = "String-Value Object";
	private final Integer mSimpleDatatypsTest_Int = 123456789;
	private final Double mSimpleDatatypsTest_Double = 123456789.123456789;
	private final Boolean mSimpleDatatypsTest_Boolean = true;

	private final Map<String, Object> mCollectionsTest_Map = Util.buildTestMap();
	private final List<String> mCollectionsTest_List = Util.buildTestList();
	private final Set<String> mCollectionsTest_Set = Util.buildTestSet();

	private Constructor mYamlConstructor = new Constructor();

	@Before
	public void setUp() {
		File f = new File("src/test/resources/");
		if (!f.exists()) {
			// If it doens't exist, try to create it.
			f.mkdir();
		}
	}

	// ******************************
	// ****** YamlWriter Tests ******
	// ******************************

	/**
	 * 1. Tests the IOException from YamlWriter when the file path not exists.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IOException.class)
	public void testYamlWriter_IOException() throws IOException {
		YamlWriter.persist(mFilePath_notExists, mString_YamlWriterSaveObject);
	}

	/**
	 * 1. Tests the NullPointerException from YamlWriter when the filename parameter is null.
	 * 
	 * @throws NullPointerException
	 */
	@Test(expected=NullPointerException.class)
	public void testYamlWriter_NullPointerException_parameter_filename() throws IOException {
		YamlWriter.persist(null, mString_YamlWriterSaveObject, new Representer(), new DumperOptions());
	}

	/**
	 * 1. Tests the NullPointerException from YamlWriter when the object parameter is null.
	 * 
	 * @throws NullPointerException
	 */
	@Test(expected=NullPointerException.class)
	public void testYamlWriter_NullPointerException_parameter_object() throws IOException {
		YamlWriter.persist(mFilePath_notExists, null, new Representer(), new DumperOptions());
	}

	/**
	 * 1. Tests the NullPointerException from YamlWriter when the Representer parameter is null.
	 * 
	 * @throws NullPointerException
	 */
	@Test(expected=NullPointerException.class)
	public void testYamlWriter_NullPointerException_parameter_Representer() throws IOException {
		YamlWriter.persist(mFilePath_notExists, mString_YamlWriterSaveObject, null, new DumperOptions());
	}

	/**
	 * 1. Tests the NullPointerException from YamlWriter when the DumperOptions parameter is null.
	 * 
	 * @throws NullPointerException
	 */
	@Test(expected=NullPointerException.class)
	public void testYamlWriter_NullPointerException_parameter_DumperOptions() throws IOException {
		YamlWriter.persist(mFilePath_notExists, mString_YamlWriterSaveObject, new Representer(), null);
	}

	/**
	 * 1. Tests the YamlWriter when the file does not exist if it is created.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testYamlWriter_createNewFile() throws IOException {
		// clean the system
		Util.deleteTestFile(mFilePath_yamlWriterCreateNewFileTest);

		// create the file
		YamlWriter.persist(mFilePath_yamlWriterCreateNewFileTest, mString_YamlWriterSaveObject);

		// tests if exists and clean the system
		Util.checkAndDeleteTestFile(mFilePath_yamlWriterCreateNewFileTest);
	}

	// ******************************
	// ****** YamlReader Tests ******
	// ******************************

	/**
	 * 1. Tests the IOException from YamlReader when the file path not exists.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IOException.class)
	public void testYamlReader_IOException() throws IOException {
		YamlReader.loadAs(mFilePath_notExists, String.class, mYamlConstructor);
	}

	/**
	 * 1. Tests the NullPointerException from YamlReader when the filename parameter is null.
	 * 
	 * @throws NullPointerException
	 */
	@Test(expected=NullPointerException.class)
	public void testYamlReader_NullPointerException_parameter_filename() throws IOException {
		YamlReader.loadAs(null, String.class, mYamlConstructor);
	}

	/**
	 * 1. Tests the NullPointerException from YamlReader when the class parameter is null.
	 * 
	 * @throws NullPointerException
	 */
	@Test(expected=NullPointerException.class)
	public void testYamlReader_NullPointerException_parameter_class() throws IOException {
		YamlReader.loadAs(mFilePath_notExists, null, mYamlConstructor);
	}

	/**
	 * 1. Tests the NullPointerException from YamlReader when the constructor parameter is null.
	 * 
	 * @throws NullPointerException
	 */
	@Test(expected=NullPointerException.class)
	public void testYamlReader_NullPointerException_parameter_constructor() throws IOException {
		YamlReader.loadAs(mFilePath_notExists, String.class, null);
	}

	/**
	 * 1. Tests the YamlReader when the file does not exist if it is created.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testYamlReader_createNewFile() throws IOException {
		// clean the system
		Util.deleteTestFile(mFilePath_yamlReaderCreateNewFileTest);

		// create the file
		YamlReader.loadAs(mFilePath_yamlReaderCreateNewFileTest, String.class, mYamlConstructor);

		// tests if exists and clean the system
		Util.checkAndDeleteTestFile(mFilePath_yamlReaderCreateNewFileTest);
	}

	@Test
	public void testYamlReader_emptyMap() throws IOException{
		// clean the system
		Util.deleteTestFile(mFilePath_emptyMapTest);

		// create the file
		Map<String, Object> loadedMap = YamlReader.loadMap(mFilePath_emptyMapTest);

		// check if map is not null
		if(loadedMap == null){
			throw new RuntimeException("The map is null!");
		}

		// check if map is empty
		if(!loadedMap.isEmpty()){
			throw new RuntimeException("The map is not empty!");
		}

		// tests if exists and clean the system
		Util.checkAndDeleteTestFile(mFilePath_emptyMapTest);
	}

	// **************************
	// ****** Equals Tests ******
	// **************************

	/**
	 * 1. Tests if String|int|double|boolean Collections can be stored.
	 * 2. Tests if String|int|double|boolean Collections can be loaded.
	 * 3. Checked for equality
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSimpleDatatyps() throws IOException {
		// String Test
		YamlWriter.persist(mFilePath_simpleDatatypsTest, mSimpleDatatypsTest_String);
		assertEquals(mSimpleDatatypsTest_String, YamlReader.loadAs(mFilePath_simpleDatatypsTest, String.class, mYamlConstructor));

		// int Test
		YamlWriter.persist(mFilePath_simpleDatatypsTest, mSimpleDatatypsTest_Int);
		assertEquals(mSimpleDatatypsTest_Int, YamlReader.loadAs(mFilePath_simpleDatatypsTest, Integer.class, mYamlConstructor));

		// double Test
		YamlWriter.persist(mFilePath_simpleDatatypsTest, mSimpleDatatypsTest_Double);
		assertEquals(mSimpleDatatypsTest_Double, YamlReader.loadAs(mFilePath_simpleDatatypsTest, Double.class, mYamlConstructor));

		// boolean Test
		YamlWriter.persist(mFilePath_simpleDatatypsTest, mSimpleDatatypsTest_Boolean);
		assertEquals(mSimpleDatatypsTest_Boolean, YamlReader.loadAs(mFilePath_simpleDatatypsTest, Boolean.class, mYamlConstructor));

		Util.checkAndDeleteTestFile(mFilePath_simpleDatatypsTest);
	}

	/**
	 * 1. Tests if List|Set|Map Collections can be stored.
	 * 2. Tests if List|Set|Map Collections can be loaded.
	 * 3. Checked for equality
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCollections() throws IOException {
		// List Test
		YamlWriter.persist(mFilePath_collectionsTest, mCollectionsTest_List);
		assertEquals(mCollectionsTest_List, YamlReader.loadAs(mFilePath_collectionsTest, List.class, mYamlConstructor));

		// Set Test
		YamlWriter.persist(mFilePath_collectionsTest, mCollectionsTest_Set);
		assertEquals(mCollectionsTest_Set, YamlReader.loadAs(mFilePath_collectionsTest, Set.class, mYamlConstructor));

		// Map Test
		YamlWriter.persist(mFilePath_collectionsTest, mCollectionsTest_Map);
		assertEquals(mCollectionsTest_Map, YamlReader.loadAs(mFilePath_collectionsTest, Map.class, mYamlConstructor));
		assertEquals(mCollectionsTest_Map, YamlReader.loadMap(mFilePath_collectionsTest));

		Util.checkAndDeleteTestFile(mFilePath_collectionsTest);
	}

}
