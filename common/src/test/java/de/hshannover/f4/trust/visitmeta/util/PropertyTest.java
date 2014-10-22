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

package de.hshannover.f4.trust.visitmeta.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.hshannover.f4.trust.visitmeta.util.properties.Properties;
import de.hshannover.f4.trust.visitmeta.util.properties.PropertyException;

public class PropertyTest extends Properties{

	private final static String mFilePath_notExists = "src/test/resources/123456789/test.yml";
	private final String mFilePath_EqualityTest = "src/test/resources/testEquality.yml";

	private final String mKey_oneTokenKey = "foo1";
	private final String mKey_twoTokenKeys = "foo2.bar";
	private final String mKey_twoTokenKeys2 = "foo2.bar2";
	private final String mKey_manyTokenKeys = "foo3.bar.fubar.baz";
	private final String mKey_manyTokenKeys2 = "foo3.bar.fubar.baz2";

	private final String mKeyValue_oneTokenKey = "KeyValue_oneTokenKey";
	private final String mKeyValue_twoTokenKeys = "KeyValue_twoTokenKeys";
	private final String mKeyValue_manyTokenKeys = "KeyValue_manyTokenKeys";

	private final List<String> mCollectionsTest_List = Util.buildTestList();

	
	public PropertyTest() {
		super(mFilePath_notExists);
	}
	
	@Before
	public void setUp() {
		File f = new File("src/test/resources/");
		if (!f.exists()) {
			// If it doens't exist, try to create it.
			f.mkdir();
		}
	}

	// ******************************
	// ****** Property Tests ********
	// ******************************

	/**
	 * 1. Tests the PropertyException when the file path not exists.
	 * 
	 * @throws PropertyException
	 */
	@Test(expected=PropertyException.class)
	public void testPropertyException() throws PropertyException {
		new Properties(mFilePath_notExists).load();
	}

	/**
	 * 1. Tests the PropertyException from Properties when the filename parameter is null.
	 * 
	 * @throws PropertyException
	 */
	@Test(expected=NullPointerException.class)
	public void testPropertyException_parameter_filename() throws PropertyException {
		new Properties(null);
	}

	/**
	 * 1. Tests if String-Value can be stored and loaded under one token key.
	 * 2. Tests if String-Value can be stored and loaded under two token keys.
	 * 3. Tests if String-Value can be stored and loaded under many token keys.
	 * 
	 * @throws PropertyException
	 */
	@Test
	public void testForEquality_emptyProperties() throws PropertyException {
		// clean the system
		Util.deleteTestFile(mFilePath_EqualityTest);

		// with one Token key
		new Properties(mFilePath_EqualityTest).set(mKey_oneTokenKey, mKeyValue_oneTokenKey);
		assertEquals(mKeyValue_oneTokenKey, new Properties(mFilePath_EqualityTest).getValue(mKey_oneTokenKey).toString());
		Util.checkAndDeleteTestFile(mFilePath_EqualityTest);

		// with two Token keys
		new Properties(mFilePath_EqualityTest).set(mKey_twoTokenKeys, mKeyValue_twoTokenKeys);
		assertEquals(mKeyValue_twoTokenKeys, new Properties(mFilePath_EqualityTest).getValue(mKey_twoTokenKeys).toString());
		Util.checkAndDeleteTestFile(mFilePath_EqualityTest);

		// with many Token keys
		new Properties(mFilePath_EqualityTest).set(mKey_manyTokenKeys, mKeyValue_manyTokenKeys);
		assertEquals(mKeyValue_manyTokenKeys, new Properties(mFilePath_EqualityTest).getValue(mKey_manyTokenKeys).toString());
		Util.checkAndDeleteTestFile(mFilePath_EqualityTest);
	}

	/**
	 * 1. Tests whether existing values ​​are overwritten with new values.
	 * 
	 * @throws PropertyException
	 */
	@Test
	public void testForEquality_filledProperties() throws PropertyException {
		// clean the system
		Util.deleteTestFile(mFilePath_EqualityTest);

		// setUp
		Properties properties = new Properties(mFilePath_EqualityTest);
		properties.set(mKey_manyTokenKeys, mCollectionsTest_List);
		properties.set(mKey_twoTokenKeys, mCollectionsTest_List);

		// equals setUp Values
		Properties properties2 = new Properties(mFilePath_EqualityTest);
		assertEquals(mCollectionsTest_List, properties2.getValue(mKey_manyTokenKeys));
		assertEquals(mCollectionsTest_List, properties2.getValue(mKey_twoTokenKeys));

		// add new values
		properties2.set(mKey_manyTokenKeys2, mKeyValue_manyTokenKeys);
		properties2.set(mKey_twoTokenKeys2, mKeyValue_twoTokenKeys);

		// equals setUp values and new values
		Properties properties3 = new Properties(mFilePath_EqualityTest);
		assertEquals(mCollectionsTest_List, properties3.getValue(mKey_manyTokenKeys));
		assertEquals(mCollectionsTest_List, properties3.getValue(mKey_twoTokenKeys));
		assertEquals(mKeyValue_manyTokenKeys, properties3.getValue(mKey_manyTokenKeys2));
		assertEquals(mKeyValue_twoTokenKeys, properties3.getValue(mKey_twoTokenKeys2));

		// tests if exists and clean the system
		Util.checkAndDeleteTestFile(mFilePath_EqualityTest);
	}
	
	/**
	 * 1. Tests whether existing values ​​are overwritten with new values. This new value is now a new Map.
	 * Example:
	 * foo:
	 *   bar:
	 *     fubar: test
	 *     
	 * The new value is:
	 * foo:
	 *   bar:
	 *     fubar:
	 *       baz: TEST
	 * 
	 * @throws PropertyException
	 */
	@Test
	public void testForEquality_filledPropertiesAddNewMapProperty() throws PropertyException {

		// clean the system
		Util.deleteTestFile(mFilePath_EqualityTest);
		testForEquality_filledPropertiesAddNewMapProperty_oneMap();
		// tests if exists and clean the system
		Util.checkAndDeleteTestFile(mFilePath_EqualityTest);

		// clean the system
		Util.deleteTestFile(mFilePath_EqualityTest);
		testForEquality_filledPropertiesAddNewMapProperty_twoMaps();
		// tests if exists and clean the system
		Util.checkAndDeleteTestFile(mFilePath_EqualityTest);

		// clean the system
		Util.deleteTestFile(mFilePath_EqualityTest);
		testForEquality_filledPropertiesAddNewMapProperty_manyMaps();
		// tests if exists and clean the system
		Util.checkAndDeleteTestFile(mFilePath_EqualityTest);

	}
	

	private void testForEquality_filledPropertiesAddNewMapProperty_oneMap() throws PropertyException {
		// setUp
		Map<String, Object> equalsMap = super.buildNewNestedMap(new String[]{"bar", "fubar", "baz"}, "TEST");
		Properties properties = new Properties(mFilePath_EqualityTest);
		properties.set("foo.bar.fubar", "test");

		// add one Map
		Properties properties2 = new Properties(mFilePath_EqualityTest);
		properties2.set("foo.bar.fubar.baz", "TEST");

		// equals
		Properties equalsProperties = new Properties(mFilePath_EqualityTest);

		// is the right value under foo.bar.fubar.baz
		assertEquals("TEST", equalsProperties.getValue("foo.bar.fubar.baz"));

		@SuppressWarnings("unchecked")
		Map<String, Object> fooSubMap = (Map<String, Object>) equalsProperties.getValue("foo");
		// is the Map right
		assertEquals(true, fooSubMap.equals(equalsMap));

	}

	private void testForEquality_filledPropertiesAddNewMapProperty_twoMaps() throws PropertyException {
		// setUp
		Map<String, Object> equalsMap = super.buildNewNestedMap(new String[]{"bar", "fubar", "baz", "qux"}, "TEST");
		Properties properties = new Properties(mFilePath_EqualityTest);
		properties.set("foo.bar.fubar", "test");

		// add two Maps
		Properties properties2 = new Properties(mFilePath_EqualityTest);
		properties2.set("foo.bar.fubar.baz.qux", "TEST");

		// equals
		Properties equalsProperties = new Properties(mFilePath_EqualityTest);

		// is the right value under foo.bar.fubar.baz.qux
		assertEquals("TEST", equalsProperties.getValue("foo.bar.fubar.baz.qux"));

		@SuppressWarnings("unchecked")
		Map<String, Object> fooSubMap = (Map<String, Object>) equalsProperties.getValue("foo");
		// is the Map right
		assertEquals(true, fooSubMap.equals(equalsMap));

	}
	
	private void testForEquality_filledPropertiesAddNewMapProperty_manyMaps() throws PropertyException {
		// setUp
		Map<String, Object> equalsMap = super.buildNewNestedMap(new String[]{"bar", "fubar", "baz", "qux", "quux", "qux2", "quux2",}, "TEST");
		Properties properties = new Properties(mFilePath_EqualityTest);
		properties.set("foo.bar.fubar", "test");

		// add many Maps
		Properties properties2 = new Properties(mFilePath_EqualityTest);
		properties2.set("foo.bar.fubar.baz.qux.quux.qux2.quux2", "TEST");

		// equals
		Properties equalsProperties = new Properties(mFilePath_EqualityTest);

		// is the right value under foo.bar.fubar.baz.qux.quux.qux2.quux2
		assertEquals("TEST", equalsProperties.getValue("foo.bar.fubar.baz.qux.quux.qux2.quux2"));

		@SuppressWarnings("unchecked")
		Map<String, Object> fooSubMap = (Map<String, Object>) equalsProperties.getValue("foo");
		// is the Map right
		assertEquals(true, fooSubMap.equals(equalsMap));
		
	}
	
	/**
	 * 1. Tests the set(), when Properties is in a subMap
	 * 
	 * @throws PropertyException
	 */
	@Test
	public void testForEquality_filledProperties_SetFromAGet() throws PropertyException {

		// clean the system
		Util.deleteTestFile(mFilePath_EqualityTest);

		// setUp
		Map<String, Object> equalsMap = super.buildNewNestedMap(new String[]{"bar", "fubar"}, "TEST");
		Properties properties = new Properties(mFilePath_EqualityTest);
		properties.set("foo.bar.fubar", "test");
		
		// get and set
		Properties properties2 = new Properties(mFilePath_EqualityTest);
		// the set is called from a subMap foo.bar
		properties2.get("foo").get("bar").set("fubar", "TEST");
		
		// equals
		Properties equalsProperties = new Properties(mFilePath_EqualityTest);

		// is the right value under foo.bar.fubar.baz.qux.quux.qux2.quux2
		assertEquals("TEST", equalsProperties.getValue("foo.bar.fubar"));

		@SuppressWarnings("unchecked")
		Map<String, Object> fooSubMap = (Map<String, Object>) equalsProperties.getValue("foo");
		// is the Map right
		assertEquals(true, fooSubMap.equals(equalsMap));
		
		// tests if exists and clean the system
		Util.checkAndDeleteTestFile(mFilePath_EqualityTest);

	}
}
