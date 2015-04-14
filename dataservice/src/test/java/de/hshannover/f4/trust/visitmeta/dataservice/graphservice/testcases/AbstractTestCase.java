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
 * This file is part of visitmeta-dataservice, version 0.4.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
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
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
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

import de.hshannover.f4.trust.ironcommon.yaml.YamlReader;
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

public abstract class AbstractTestCase {

	private static final String KEY_YAML_FIRST_IDENTIFIER = "first";
	private static final String KEY_YAML_SECOND_IDENTIFIER = "second";
	private static final String KEY_YAML_METADATA = "metadata";
	private static final String KEY_YAML_TYPENAME = "type";
	private static final String KEY_YAML_PROPERTIES = "properties";
	private static final String KEY_YAML_RAWDATA = "rawData";

	private static final String KEY_JSON_TYPENAME = "typename";
	private static final String KEY_JSON_PROPERTIES = "properties";

	private Reader mReader;
	private Executor mExecutor;
	private Neo4JConnection mDbConnection;
	private JsonMarshaller mJsonMarshaller;

	protected GraphDatabaseService mGraphDb;
	protected Neo4JTimestampManager mTimestampManager;
	protected GraphService mService;

	protected final String TESTCASES_DIRECTORY = "src/test/resources/testcases";

	private final Logger logger = Logger.getLogger(AbstractTestCase.class);
	private Map<String, Object> mTestcase;
	protected Map<String, JSONObject> mIdentifier;
	protected Map<String, JSONObject> mMetadata;

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

		mService = new SimpleGraphService(mReader, mExecutor,
				new DummyGraphCache());
		mJsonMarshaller = new JsonMarshaller();
	}

	@After
	public void tearDown() {
		if (mGraphDb != null) {
			mGraphDb.shutdown();
		}
	}

	public abstract String getTestcaseFilename();

	private void loadTestcaseIntoGraphDB() throws JSONException {
		try {
			String testcaseFilename = getTestcaseFilename();
			if (testcaseFilename != null) {
				mTestcase = YamlReader.loadMap(testcaseFilename);
				assumeTrue(!mTestcase.isEmpty());
				parseYamlMap();
				Neo4JTestDatabaseFactory.loadTestCaseIntoGraphDB(mTestcase,
						mGraphDb, mTimestampManager);
			} else {
				logger.info("Testcase filename was null, using empty graph database for tests.");
			}
		} catch (IOException e) {
			logger.error("Could not load '" + getTestcaseFilename()
					+ "'; skipping tests");
		}
	}

	@SuppressWarnings("unchecked")
	private void parseYamlMap() throws JSONException {
		mIdentifier = new HashMap<String, JSONObject>();
		mMetadata = new HashMap<String, JSONObject>();

		for (String linkKey : mTestcase.keySet()) {
			Map<String, Object> link = (Map<String, Object>) mTestcase
					.get(linkKey);
			Map<String, Object> first = (Map<String, Object>) link
					.get(KEY_YAML_FIRST_IDENTIFIER);
			Map<String, Object> second = (Map<String, Object>) link
					.get(KEY_YAML_SECOND_IDENTIFIER);
			Map<String, Object> metadata = (Map<String, Object>) link
					.get(KEY_YAML_METADATA);

			if (!mIdentifier.containsKey(first.get(KEY_YAML_RAWDATA))) {
				mIdentifier.put((String) first.get(KEY_YAML_RAWDATA),
						buildJSONObjectFromMap(first));
			}
			if (second != null
					&& !mIdentifier.containsKey(second.get(KEY_YAML_RAWDATA))) {
				mIdentifier.put((String) second.get(KEY_YAML_RAWDATA),
						buildJSONObjectFromMap(second));
			}

			for (String metaKey : metadata.keySet()) {
				Map<String, Object> meta = (Map<String, Object>) metadata
						.get(metaKey);
				mMetadata.put((String) meta.get(KEY_YAML_RAWDATA),
						buildJSONObjectFromMap(meta));

			}
		}
	}

	/**
	 *
	 * @param timestamp
	 *            of the subgraph
	 * @param objects
	 *            List of Identifier-Metadata connections contained in the
	 *            subgraph
	 * @return JSONObject containing the subgraph
	 * @throws JSONException
	 */
	protected JSONObject createJSON(long timestamp, List<JSONObject> objects)
			throws JSONException {
		JSONObject json = new JSONObject();
		json.put("timestamp", timestamp);

		JSONArray linksWrapperArray = new JSONArray();
		json.put("links", linksWrapperArray);

		for (JSONObject obj : objects) {
			linksWrapperArray.put(obj);
		}

		return json;
	}

	protected JSONObject createJSON(long timestamp, JSONObject object)
			throws JSONException {
		ArrayList<JSONObject> objects = new ArrayList<JSONObject>();
		objects.add(object);
		return createJSON(timestamp, objects);
	}

	/**
	 *
	 * @param first
	 *            key for the first Identifier (rawData)
	 * @param second
	 *            key for the second Identifier (rawData) (second may be null)
	 * @param metadata
	 *            List of keys for metadata (rawData)
	 * @return JSONObject containing Identifier-Metadata connection.
	 * @throws JSONException
	 */
	protected JSONObject createJSONIdentifierMetadataConnection(String first,
			String second, List<String> metadata) throws JSONException {
		JSONObject l = new JSONObject();
		Object identifiers;
		if (second != null) {
			identifiers = new JSONArray();
			((JSONArray) identifiers).put(mIdentifier.get(first));
			((JSONArray) identifiers).put(mIdentifier.get(second));
		} else {
			identifiers = mIdentifier.get(first);
		}
		l.put("identifiers", identifiers);
		Object meta;
		if (metadata.size() > 1) {
			meta = new JSONArray();
			for (String metaKey : metadata) {
				((JSONArray) meta).put(mMetadata.get(metaKey));
			}
		} else {
			meta = mMetadata.get(metadata.get(0));
		}
		l.put("metadata", meta);

		return l;
	}

	protected JSONObject createJSONIdentifierMetadataConnection(String first,
			List<String> metadata) throws JSONException {
		return createJSONIdentifierMetadataConnection(first, null, metadata);
	}

	protected JSONObject createJSONIdentifierMetadataConnection(String first,
			String second, String metadata) throws JSONException {
		ArrayList<String> meta = new ArrayList<String>();
		meta.add(metadata);
		return createJSONIdentifierMetadataConnection(first, second, meta);
	}

	protected JSONObject createJSONIdentifierMetadataConnection(String first,
			String metadata) throws JSONException {
		return createJSONIdentifierMetadataConnection(first, null, metadata);
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
	public abstract void getChangesMap() throws Exception;

	public JSONObject toJson(Map<Long, Long> changesMap) throws JSONException {
		return mJsonMarshaller.toJson(changesMap);
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

	/**
	 * Equals JSONObjects or JSONArrays rekursive.
	 *
	 * @param obj1
	 * @param obj2
	 * @return true if the JSONObject are same, false otherwise
	 * @throws JSONException
	 */
	protected boolean jsonsEqual(Object obj1, Object obj2) throws JSONException {
		// ### equals the classes ###
		if (!obj1.getClass().equals(obj2.getClass())) {
			return false;
		}

		if (obj1 instanceof JSONObject) {
			// ### equals JSONObjects ###

			JSONObject jsonObj1 = (JSONObject) obj1;
			JSONObject jsonObj2 = (JSONObject) obj2;

			JSONArray keyNames = jsonObj1.names();
			JSONArray keyNames2 = jsonObj2.names();

			// check keys of null
			if (keyNames == null || keyNames2 == null) {
				if (keyNames == null && keyNames2 == null) {
					return true;
				} else {
					return false;
				}
			}

			// check keys length
			if (keyNames.length() != keyNames2.length()) {
				return false;
			}

			// check keys Class of String
			for (int i = 0; i < keyNames.length(); i++) {
				if (!keyNames.get(i).getClass().equals(String.class)
						|| !keyNames2.get(i).getClass().equals(String.class)) {
					throw new JSONException(
							"The key from the JSONObject must be a String");
				}
			}

			// equals the value from key (the sequence does not matter)
			for (int i = 0; i < keyNames.length(); i++) {
				Object obj1FieldValue = jsonObj1.get(keyNames.getString(i));
				Object obj2FieldValue = jsonObj2.get(keyNames.getString(i));

				// rekursive call
				if (!jsonsEqual(obj1FieldValue, obj2FieldValue)) {
					return false;
				}
			}

		} else if (obj1 instanceof JSONArray) {
			// ### equals JSONArrays ###

			JSONArray obj1Array = (JSONArray) obj1;
			JSONArray obj2Array = (JSONArray) obj2;

			// ### equals the arrays ###
			if (obj1Array.length() != obj2Array.length()) {
				return false;
			}

			// ### equals the objects from the arrays (the sequence does not
			// matter) ###
			for (int i = 0; i < obj1Array.length(); i++) {
				boolean matchFound = false;

				for (int j = 0; j < obj2Array.length(); j++) {
					// rekursive call
					if (jsonsEqual(obj1Array.get(i), obj2Array.get(j))) {
						matchFound = true;
						break;
					}
				}

				if (!matchFound) {
					return false;
				}
			}
		} else {
			// ### equals Objects ###

			if (!obj1.equals(obj2)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Build a JSONObject for identifiers or metadata
	 *
	 * @param valueMap
	 * @return JSONObject
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	private JSONObject buildJSONObjectFromMap(Map<String, Object> valueMap)
			throws JSONException {
		Map<String, Object> properties = (Map<String, Object>) valueMap
				.get(KEY_YAML_PROPERTIES);

		JSONObject propertiesJSONObject = new JSONObject();
		if (properties != null) {
			for (Entry<String, Object> e : properties.entrySet()) {
				propertiesJSONObject.put(e.getKey(), e.getValue());
			}
		}

		JSONObject valueJSONObject = new JSONObject();
		valueJSONObject.put(KEY_JSON_TYPENAME, valueMap.get(KEY_YAML_TYPENAME));
		valueJSONObject.put(KEY_JSON_PROPERTIES, propertiesJSONObject);
		return valueJSONObject;
	}

	protected void testGraphListSize(List<IdentifierGraph> list,
			int expectedGraphListSize) {
		assertEquals(expectedGraphListSize, list.size());
	}

	protected void testIdentifierCount(IdentifierGraph graph,
			int expectedIdentifierCount) {
		List<Identifier> identifiers = graph.getIdentifiers();
		assertEquals(expectedIdentifierCount, identifiers.size());
	}

	protected void testDeltaSize(Delta delta, int expectedUpdateGraphSize,
			int expectedDeleteGraphSize) {
		List<IdentifierGraph> updates = delta.getUpdates();
		List<IdentifierGraph> deletes = delta.getDeletes();

		assertEquals(expectedUpdateGraphSize, updates.size());
		assertEquals(expectedDeleteGraphSize, deletes.size());
	}

	protected void testChangesMap(Map<Long, Long> expectedValues,
			Map<Long, Long> changesMap) {
		assertEquals(expectedValues.size(), changesMap.size());

		Iterator<Long> iterator = expectedValues.keySet().iterator();
		while (iterator.hasNext()) {
			Long key = iterator.next();
			assertEquals(expectedValues.get(key), changesMap.get(key));
		}
	}

	protected void testChangesMapJSON(SortedMap<Long, Long> expectedValues,
			SortedMap<Long, Long> changesMap) throws JSONException {
		JSONObject actual = toJson(changesMap);
		JSONObject expected = toJson(expectedValues);

		assertTrue(jsonsEqual(expected, actual));
	}

}
