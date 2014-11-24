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
 * This file is part of visitmeta-dataservice, version 0.2.0,
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
package de.hshannover.f4.trust.visitmeta.dataservice.graphservice.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;

import de.hshannover.f4.trust.visitmeta.dataservice.factories.Neo4JTestDatabaseFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.DummyGraphCache;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.SimpleGraphService;
import de.hshannover.f4.trust.visitmeta.dataservice.rest.JsonMarshaller;
import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;
import de.hshannover.f4.trust.visitmeta.persistence.Executor;
import de.hshannover.f4.trust.visitmeta.persistence.Reader;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JConnection;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JExecutor;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JReader;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JRepository;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JTimestampManager;
import de.hshannover.f4.trust.visitmeta.util.yaml.YamlReader;

public abstract class AbstractTestCase {

	private Reader mReader;
	private Executor mExecutor;
	private Neo4JConnection mDbConnection;
	private JsonMarshaller mJsonMarshaller;

	protected GraphDatabaseService mGraphDb;
	protected Neo4JTimestampManager mTimestampManager;
	protected GraphService mService;

	protected final String TESTCASES_DIRECTORY = "src/test/resources/testcases";

	private final Logger logger = Logger.getLogger(AbstractTestCase.class);

	@Before
	public void setUp() throws Exception {
		mGraphDb = Neo4JTestDatabaseFactory.createGraphDB();
		assumeNotNull(mGraphDb);

		mDbConnection = mock(Neo4JConnection.class);
		when(mDbConnection.getConnection()).thenReturn(mGraphDb);

		mTimestampManager = new Neo4JTimestampManager(mDbConnection);
		when(mDbConnection.getTimestampManager()).thenReturn(mTimestampManager);

		loadTestcaseIntoGraphDB();

		Neo4JRepository repo = new Neo4JRepository(mDbConnection,
				MessageDigest.getInstance("MD5"));
		mReader = new Neo4JReader(repo, mDbConnection);
		mExecutor = new Neo4JExecutor(mDbConnection);

		mService = new SimpleGraphService(mReader, mExecutor, new DummyGraphCache());
		mJsonMarshaller = new JsonMarshaller();
	}

	@After
	public void tearDown() {
		if (mGraphDb != null) {
			mGraphDb.shutdown();
		}
	}

	public abstract String getTestcaseFilename();

	private void loadTestcaseIntoGraphDB() {
		try {
			String testcaseFilename = getTestcaseFilename();
			if (testcaseFilename != null) {
				Map<String, Object> testcase = YamlReader.loadMap(testcaseFilename);
				assumeTrue(!testcase.isEmpty());
				Neo4JTestDatabaseFactory.loadTestCaseIntoGraphDB(testcase, mGraphDb, mTimestampManager);
			} else {
				logger.info("Testcase filename was null, using empty graph database for tests.");
			}
		} catch (IOException e) {
			logger.error("Could not load '" + getTestcaseFilename() + "'; skipping tests");
		}
	}

	@Test
	public abstract void getInitialGraph() throws Exception;

	@Test
	public abstract void getGraphAt() throws Exception;

	@Test
	public abstract void getCurrentGraph() throws Exception;

	@Test
	public abstract void getDelta() throws Exception;

	@Test
	public abstract void getChangesMap();

	public JSONObject toJson(SortedMap<Long,Long> changesMap) {
		return new JSONObject(changesMap);
	}

	public JSONObject toJson(Delta delta) {
		return mJsonMarshaller.toJson(delta);
	}

	public JSONArray toJson(List<IdentifierGraph> graphs) {
		return mJsonMarshaller.toJson(graphs);
	}

	public JSONObject toJson(IdentifierGraph graph) {
		return mJsonMarshaller.toJson(graph);
	}

	public boolean equalsJsonObject(JSONObject expected, JSONObject actual) {
		if (actual.toString().equals(expected.toString())) {
			return true;
		} else {
			return false;
		}
	}

	public boolean equalsJsonArray(JSONArray expected, JSONArray actual) {
		if (actual.toString().equals(expected.toString())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Equals JSONObjects or JSONArrays rekursive.
	 * @param obj1
	 * @param obj2
	 * @return true if the JSONObject are same, false otherwise
	 * @throws JSONException
	 */
	protected boolean jsonsEqual(Object obj1, Object obj2) throws JSONException {
		// ### equals the classes ###
		if ( !obj1.getClass().equals(obj2.getClass()) ) {
			return false;
		}

		if ( obj1 instanceof JSONObject ) {
			// ### equals JSONObjects ###

			JSONObject jsonObj1 = (JSONObject) obj1;
			JSONObject jsonObj2 = (JSONObject) obj2;

			JSONArray keyNames = jsonObj1.names();
			JSONArray keyNames2 = jsonObj2.names();

			// ### equals keys ###
			if (keyNames == null || keyNames2 == null) {
				if (keyNames == null && keyNames2 == null) {
					return true;
				} else {
					return false;
				}
			}

			if (keyNames.length() != keyNames2.length()){
				return false;
			}

			// equals the value from key (the sequence does not matter)
			for (int i=0; i < keyNames.length(); i++) {
				Object obj1FieldValue = jsonObj1.get((String)keyNames.get(i));
				Object obj2FieldValue = jsonObj2.get((String)keyNames.get(i));

				// rekursive call
				if (!jsonsEqual(obj1FieldValue, obj2FieldValue)){
					return false;
				}
			}

		} else if ( obj1 instanceof JSONArray ){
			// ### equals JSONArrays ###

			JSONArray obj1Array = (JSONArray) obj1;
			JSONArray obj2Array = (JSONArray) obj2;

			// ### equals the arrays ###
			if (obj1Array.length() != obj2Array.length()){
				return false;
			}

			// ### equals the objects from the arrays (the sequence does not matter) ###
			for (int i = 0; i < obj1Array.length(); i++){
				boolean matchFound = false;

				for (int j = 0; j < obj2Array.length(); j++){
					// rekursive call
					if (jsonsEqual(obj1Array.get(i), obj2Array.get(j))){
						matchFound = true;
						break;
					}
				}

				if ( !matchFound ){
					return false;
				}
			}
		} else {
			// ### equals Objects ###

			if ( !obj1.equals(obj2) ){
				return false;
			}
		}

		return true;
	}

	/**
	 * Builds a JSONArray to equals this with JsonMarshaller.toJson().
	 * Load the data from the yaml file.
	 * @param link
	 * @return JSONArray from the yaml testcase filename
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	protected JSONArray buildJSONFromYamlFile(String link, Long timestamp) throws JSONException {

		// ### load the expected from yaml file ###
		Map<String, Object> expectedDB = null;
		try {

			expectedDB = YamlReader.loadMap(getTestcaseFilename());

		} catch (IOException e) {
			logger.error("Could not load '" + getTestcaseFilename() + "'; skipping test ");
		}

		// ### extract first // second // metadata
		Map<String, Object> expectedLink = (Map<String, Object>) expectedDB.get(link);
		Map<String, Object> first = (Map<String, Object>) expectedLink.get("first");
		Map<String, Object> second = (Map<String, Object>) expectedLink.get("second");
		Map<String, Object> metadata = (Map<String, Object>) expectedLink.get("metadata");

		JSONArray expectedJSONArray = new JSONArray();
		JSONObject expectedJSONObject = new JSONObject();
		JSONArray linksJSONArray = new JSONArray();
		JSONObject linkJSONObject = new JSONObject();
		JSONArray identifiersJSONArray = new JSONArray();

		// ### build link object ###
		expectedJSONObject.put("timestamp", timestamp);
		expectedJSONObject.put("links", linksJSONArray);


		// ### build first Identifier ###
		JSONObject firstIdentifierJSONObject = buildJSONObjectFromMap(first);
		identifiersJSONArray.put(firstIdentifierJSONObject);

		// ### build second Identifier ###
		if(second != null){
			JSONObject secondIdentifierJSONObject = buildJSONObjectFromMap(second);

			identifiersJSONArray.put(secondIdentifierJSONObject);
			linkJSONObject.put("identifiers", identifiersJSONArray);

		} else {
			linkJSONObject.put("identifiers", firstIdentifierJSONObject);
		}

		// ### build metadata for the link object ###
		JSONArray metadataJSONArray = buildMetadataJSONArray(metadata);

		if(metadataJSONArray.length() == 1){
			// only one -> add simple JSONObject
			linkJSONObject.put("metadata", metadataJSONArray.get(0));

		}else if(metadataJSONArray.length() > 1){
			// more as one add the full array
			linkJSONObject.put("metadata", metadataJSONArray);

		} else {
			linkJSONObject.put("metadata", new JSONObject());
		}

		// ### add the link object to the links array ###
		linksJSONArray.put(linkJSONObject);

		// ### add the expected-JSONObject object to the root array ###
		expectedJSONArray.put(expectedJSONObject);

		return expectedJSONArray;
	}

	@SuppressWarnings("unchecked")
	private JSONArray buildMetadataJSONArray(Map<String, Object> metadata) throws JSONException {
		JSONArray metadataJSONArray = new JSONArray();
		for(Entry<String, Object> metadataEntry: metadata.entrySet()){

			Object tmp = metadataEntry.getValue();
			if(tmp instanceof HashMap){
				HashMap<String, Object> metaTmp = (HashMap<String, Object>) tmp;
				JSONObject metadataJSONObject = buildJSONObjectFromMap(metaTmp);

				metadataJSONArray.put(metadataJSONObject);
			} else {
				logger.error("The metadata-entry-set value is not a HashMap");
			}
		}
		return metadataJSONArray;
	}

	/**
	 * Build a JSONObject for identifiers or metadata
	 * @param valueMap
	 * @return JSONObject
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	private JSONObject buildJSONObjectFromMap(Map<String, Object> valueMap) throws JSONException {
		Map<String, Object> properties = (Map<String, Object>) valueMap.get("properties");

		JSONObject propertiesJSONObject = new JSONObject();
		if (properties != null) {
			for(Entry<String, Object> e: properties.entrySet()){
				propertiesJSONObject.put(e.getKey(), e.getValue());
			}
		}

		JSONObject valueJSONObject = new JSONObject();
		valueJSONObject.put("typename", valueMap.get("type"));
		valueJSONObject.put("properties", propertiesJSONObject);
		return valueJSONObject;
	}

	/**
	 * Equals the properties-JSONObject from the metadata with the expected properties from yaml-file.
	 * @param metadataProperties JSONObject with the metadata properties
	 * @param expected properties from yaml-file
	 * @return properties JSONObject
	 */
	protected boolean equalsMetadataProperties(JSONObject metadataProperties, Map<String, Object> expected) {
		JSONObject expectedJSON = new JSONObject(expected);
		return metadataProperties.toString().equals(expectedJSON.toString());
	}

	/**
	 * Extracted from jArray the properties-JSONObject from the metadata.
	 * Have the metadata no properties returns an empty JSONObject.
	 * @param jArray the JSONArray from List<IdentifierGraph>
	 * @param arrayIndex is the position of the JSONObject
	 * @param linkIndex is the link position of the JSONObject, to be used
	 * @return properties JSONObject
	 */
	protected JSONObject getPropertiesFromMetadata(JSONArray jArray, int arrayIndex, int linkIndex) {
		JSONObject properties = null;
		try {

			JSONObject jObj = jArray.getJSONObject(arrayIndex);
			JSONArray links = jObj.getJSONArray("links");
			JSONObject link = links.getJSONObject(linkIndex);
			JSONObject metadata = link.getJSONObject("metadata");

			try {
				properties = metadata.getJSONObject("properties");
			} catch (JSONException e) {
				logger.debug("No properties found in metadata[" + metadata + "]", e);
			}

		} catch (JSONException e) {
			logger.debug("Could not extracted the metadata from JSONArray[" + jArray + "] arrayIndex[" + arrayIndex + "] -> links["+linkIndex+"] -> metadata", e);
		}


		// return JSONObject, if is null return an empty JSONObject
		if(properties != null){
			return properties;
		} else {
			return new JSONObject();
		}
	}

	/**
	 * Load the test case file and extracted the properties-Map from the metadata.
	 * Have the metadata no properties returns an empty Map.
	 * @param sLink represents the link from yaml-File
	 * @param sMetadata represents the metadata from yaml-File
	 * @return properties Map<String, Object>
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Object> getPropertiesFromMetadata(String sLink, String sMetadata) {

		// load yaml file
		Map<String, Object> expectedDB = null;
		try {

			expectedDB = YamlReader.loadMap(getTestcaseFilename());

		} catch (IOException e) {
			logger.error("Could not load '" + getTestcaseFilename() + "'; skipping test ");
		}

		// load properties Map from Metadata sMetadata and sLink
		Map<String, Object> properties = null;
		if(expectedDB != null) {
			Map<String, Object> link = (Map<String, Object>) expectedDB.get(sLink);
			Map<String, Object> metadata = (Map<String, Object>) link.get("metadata");
			Map<String, Object> meta = (Map<String, Object>) metadata.get(sMetadata);
			properties = (Map<String, Object>) meta.get("properties");
		}

		// return map, if is null return an empty Map
		if(properties != null){
			return properties;
		} else {
			return new HashMap<String, Object>();
		}
	}

	protected void testGraphListSize(List<IdentifierGraph> list, int expectedGraphListSize) {
		assertEquals(expectedGraphListSize, list.size());
	}

	protected void testIdentifierCount(IdentifierGraph graph, int expectedIdentifierCount) {
		List<Identifier> identifiers = graph.getIdentifiers();
		assertEquals(expectedIdentifierCount, identifiers.size());
	}

	protected void testDeltaSize(Delta delta, int expectedUpdateGraphSize, int expectedDeleteGraphSize) {
		List<IdentifierGraph> updates = delta.getUpdates();
		List<IdentifierGraph> deletes = delta.getDeletes();

		assertEquals(expectedUpdateGraphSize, updates.size());
		assertEquals(expectedDeleteGraphSize, deletes.size());
	}

	protected void testChangesMap(Map<Long, Long> expectedValues, Map<Long, Long> changesMap) {
		assertEquals(expectedValues.size(), changesMap.size());

		Iterator<Long> iterator = expectedValues.keySet().iterator();
		while (iterator.hasNext()) {
			Long key = iterator.next();
			assertEquals(expectedValues.get(key), changesMap.get(key));
		}
	}

	protected void testChangesMapJSON(Map<Long, Long> expectedValues, SortedMap<Long, Long> changesMap) {
		String actual = toJson(changesMap).toString();

		StringBuilder sb = new StringBuilder();
		sb.append("{");

		Iterator<Long> iterator = changesMap.keySet().iterator();
		while (iterator.hasNext()) {
			Long key = iterator.next();
			Long expectedValue = expectedValues.get(key);
			sb.append("\"");
			sb.append(key);
			sb.append("\":");
			sb.append(expectedValue);
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}

		sb.append("}");
		String expected = sb.toString();

		assertEquals(expected, actual);
	}

}
