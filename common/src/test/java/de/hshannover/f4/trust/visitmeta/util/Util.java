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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Util {

	// ****************************
	// ****** common methods ******
	// ****************************

	/**
	 * 1. Cleans the system after the test.
	 * 2. Tests if the test file exists.
	 * 3. Tests if all input or output streams were closed.
	 * 
	 * @param filePath
	 */
	public static void checkAndDeleteTestFile(String filePath) {
		checkExistsTestFile(filePath);
		deleteTestFile(filePath);
	}

	/**
	 * 1. Tests if the test file exists.
	 * 
	 * @param filePath
	 */
	public static void checkExistsTestFile(String filePath){
		File testFile = new File(filePath);
		if(!testFile.exists()){
			throw new RuntimeException("The test file " + filePath + " not exists!");
		}
	}

	/**
	 * 1. Cleans the system after the test.
	 * 2. Tests if all input or output streams were closed.
	 * 
	 * @param filePath
	 */
	public static void deleteTestFile(String filePath){
		File testFile = new File(filePath);
		if(testFile.exists()){
			if(!testFile.delete()){
				throw new RuntimeException("Test file " + filePath + " could not be deleted!");
			}
		}
	}

	public static List<String> buildTestList(){
		ArrayList<String> collectionsTest_List = new ArrayList<String>();
		collectionsTest_List.add("foo");
		collectionsTest_List.add("bar");
		collectionsTest_List.add("fubar");
		collectionsTest_List.add("baz");
		collectionsTest_List.add("qux");
		collectionsTest_List.add("quux");
		collectionsTest_List.add("The test String!");
		collectionsTest_List.add("!\"§$%&/\\()=?#'+*~-_.:,;µ<>|@€^°");
		return collectionsTest_List;
	}

	public static Set<String> buildTestSet(){
		Set<String> collectionsTest_Set = new HashSet<String>();
		collectionsTest_Set.add("!\"§$%&/\\()=?#'+*~-_.:,;µ<>|@€^°");
		collectionsTest_Set.add("The test String!");
		collectionsTest_Set.add("quux");
		collectionsTest_Set.add("qux");
		collectionsTest_Set.add("baz");
		collectionsTest_Set.add("fubar");
		collectionsTest_Set.add("bar");
		collectionsTest_Set.add("foo");
		return collectionsTest_Set;
	}

	public static Map<String, Object> buildTestMap(){
		Map<String, Object> collectionsTest_Map = new HashMap<String, Object>();
		collectionsTest_Map.put("mTestList", buildTestList());
		collectionsTest_Map.put("mTestSet", buildTestSet());
		return collectionsTest_Map;
	}
}
