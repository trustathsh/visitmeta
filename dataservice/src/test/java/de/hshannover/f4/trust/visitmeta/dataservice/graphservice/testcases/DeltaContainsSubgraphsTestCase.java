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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

public class DeltaContainsSubgraphsTestCase extends AbstractTestCase {

	private final String TESTCASE_FILENAME = TESTCASES_DIRECTORY + File.separator + "deltaContainsSubgraphs.yml";

	@Override
	public String getTestcaseFilename() {
		return TESTCASE_FILENAME;
	}

	@Override
	public void getInitialGraph() throws JSONException {
		List<IdentifierGraph> initialGraph = mService.getInitialGraph();

		testGraphAt0(initialGraph);
	}

	@Override
	public void getGraphAt() throws JSONException {
		getGraphAt0();
		getGraphAt1();
		getGraphAt2();
	}

	private void getGraphAt0() throws JSONException {
		long timestamp = 0;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		testGraphAt0(graphAt);
	}

	private void testGraphAt0(List<IdentifierGraph> graphs) throws JSONException {
		testGraphListSize(graphs, 1);

		IdentifierGraph graph = graphs.get(0);
		testIdentifierCount(graph, 2);

		JSONArray actual = toJson(graphs);
		JSONArray expected = new JSONArray();
		expected.put(createJSON(0, createJSONIdentifierMetadataConnection("ip-address1", "mac-address1", "ip-mac1")));
		assertTrue(jsonsEqual(actual, expected));
	}

	private void getGraphAt1() throws JSONException {
		long timestamp = 1;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		testGraphListSize(graphAt, 1);

		IdentifierGraph graph = graphAt.get(0);
		testIdentifierCount(graph, 3);

		JSONArray actual = toJson(graphAt);
		JSONArray expected = new JSONArray();
		List<JSONObject> jsonElements = new ArrayList<JSONObject>();
		jsonElements.add(createJSONIdentifierMetadataConnection("ip-address1", "mac-address1", "ip-mac1"));
		jsonElements.add(createJSONIdentifierMetadataConnection("access-request1", "mac-address1", "access-request-mac1"));
		expected.put(createJSON(timestamp, jsonElements));

		assertTrue(jsonsEqual(actual, expected));
	}

	private void getGraphAt2() throws JSONException {
		long timestamp = 2;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		testGraphAt2(graphAt);
	}

	private void testGraphAt2(List<IdentifierGraph> graphs) throws JSONException {
		long timestamp = 2;

		testGraphListSize(graphs, 1);

		IdentifierGraph graph = graphs.get(0);
		testIdentifierCount(graph, 4);

		JSONArray actual = toJson(graphs);
		JSONArray expected = new JSONArray();
		List<JSONObject> jsonElements = new ArrayList<JSONObject>();
		jsonElements.add(createJSONIdentifierMetadataConnection("mac-address1", "ip-address1", "ip-mac1"));
		jsonElements.add(createJSONIdentifierMetadataConnection("mac-address1", "access-request1", "access-request-mac1"));
		jsonElements.add(createJSONIdentifierMetadataConnection("ip-address1", "device1", "device-ip1"));
		expected.put(createJSON(timestamp, jsonElements));

		assertTrue(jsonsEqual(actual, expected));
	}

	@Override
	public void getCurrentGraph() throws JSONException {
		List<IdentifierGraph> currentGraph = mService.getCurrentGraph();

		testGraphAt2(currentGraph);
	}

	@Override
	public void getDelta() throws JSONException {
		getDeltaFrom0To1();
		getDeltaFrom1To2();
		getDeltaFrom0To2();
	}

	private void getDeltaFrom0To1() throws JSONException {
		long t1 = 0;
		long t2 = 1;
		Delta delta = mService.getDelta(t1, t2);

		testDeltaSize(delta, 1, 0);

		List<Identifier> updateIdentifiers = delta.getUpdates().get(0).getIdentifiers();
		assertEquals(2, updateIdentifiers.size());

		JSONArray actualUpdates = toJson(delta.getUpdates());
		JSONArray expectedUpdates = new JSONArray();
		expectedUpdates.put(createJSON(t2, createJSONIdentifierMetadataConnection("mac-address1", "access-request1", "access-request-mac1")));
		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));

		JSONArray actualDeletes = toJson(delta.getDeletes());
		JSONArray expectedDeletes = new JSONArray();
		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));
	}

	private void getDeltaFrom1To2() throws JSONException {
		long t1 = 1;
		long t2 = 2;
		Delta delta = mService.getDelta(t1, t2);

		testDeltaSize(delta, 1, 0);

		List<Identifier> updateIdentifiers = delta.getUpdates().get(0).getIdentifiers();
		assertEquals(2, updateIdentifiers.size());

		JSONArray actualUpdates = toJson(delta.getUpdates());
		JSONArray expectedUpdates = new JSONArray();
		expectedUpdates.put(createJSON(t2, createJSONIdentifierMetadataConnection("ip-address1", "device1", "device-ip1")));
		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));

		JSONArray actualDeletes = toJson(delta.getDeletes());
		JSONArray expectedDeletes = new JSONArray();
		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));
	}

	private void getDeltaFrom0To2() throws JSONException {
		long t1 = 0;
		long t2 = 2;
		Delta delta = mService.getDelta(t1, t2);

		testDeltaSize(delta, 2, 0);

		List<Identifier> updateIdentifiers1 = delta.getUpdates().get(0).getIdentifiers();
		assertEquals(2, updateIdentifiers1.size());

		List<Identifier> updateIdentifiers2 = delta.getUpdates().get(1).getIdentifiers();
		assertEquals(2, updateIdentifiers2.size());

		JSONArray actualUpdates = toJson(delta.getUpdates());
		JSONArray expectedUpdates = new JSONArray();
		expectedUpdates.put(createJSON(t2, createJSONIdentifierMetadataConnection("mac-address1", "access-request1", "access-request-mac1")));
		expectedUpdates.put(createJSON(t2, createJSONIdentifierMetadataConnection("ip-address1", "device1", "device-ip1")));

		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));

		JSONArray actualDeletes = toJson(delta.getDeletes());
		JSONArray expectedDeletes = new JSONArray();
		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));
	}

	@Override
	public void getChangesMap() throws JSONException {
		SortedMap<Long,Long> changesMap = mService.getChangesMap();

		SortedMap<Long,Long> changes = new TreeMap<Long,Long>();
		changes.put(0L, 1L);	// t0
		changes.put(1L, 1L);	// t1
		changes.put(2L, 1L);	// t2

		testChangesMap(changes, changesMap);
		testChangesMapJSON(changes, changesMap);
	}

}
