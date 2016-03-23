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
 * This file is part of visitmeta-dataservice, version 0.6.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2016 Trust@HsH
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
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

public class NotifyMetadataMixedTestCase extends AbstractTestCase {
	private final String TESTCASE_FILENAME = TESTCASES_DIRECTORY
			+ File.separator + "NotifyMetadataMixed.yml";

	@Override
	public String getTestcaseFilename() {
		return TESTCASE_FILENAME;
	}

	@Override
	public void getInitialGraph() throws Exception {
		List<IdentifierGraph> initialGraph = mService.getInitialGraph();
		assertEquals(2, initialGraph.get(0).getIdentifiers().size());

		JSONArray actual = toJson(initialGraph);
		JSONArray expected = new JSONArray();
		{
			JSONObject idPair1 = createJSONIdentifierMetadataConnection(
					"device1", "ip1", "dev-ip1");
			JSONObject subGraph1 = createJSON(0l, idPair1);
			expected.put(subGraph1);
		}
		assertTrue(jsonsEqual(actual, expected));
	}

	@Override
	public void getGraphAt() throws Exception {
		getGraphAt0();
		getGraphAt1();
		getGraphAt2();
	}

	private void getGraphAt0() throws Exception {
		long timestamp = 0;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);
		assertEquals(2, graphAt.get(0).getIdentifiers().size());

		JSONArray actual = toJson(graphAt);
		JSONArray expected = new JSONArray();
		{
			JSONObject idPair1 = createJSONIdentifierMetadataConnection(
					"device1", "ip1", "dev-ip1");
			JSONObject subGraph1 = createJSON(timestamp, idPair1);
			expected.put(subGraph1);
		}

		assertTrue(jsonsEqual(actual, expected));
	}

	private void getGraphAt1() throws Exception {
		long timestamp = 1;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);
		assertEquals(2, graphAt.get(0).getIdentifiers().size());

		JSONArray actual = toJson(graphAt);
		JSONArray expected = new JSONArray();
		{
			JSONObject idPair1 = createJSONIdentifierMetadataConnection(
					"device1", "ip1", "dev-ip1");
			JSONObject subGraph1 = createJSON(timestamp, idPair1);
			expected.put(subGraph1);
		}

		assertTrue(jsonsEqual(actual, expected));
	}

	private void getGraphAt2() throws Exception {
		long timestamp = 2;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);
		assertEquals(3, graphAt.get(0).getIdentifiers().size());

		JSONArray actual = toJson(graphAt);
		JSONArray expected = new JSONArray();
		{
			JSONObject idPair1 = createJSONIdentifierMetadataConnection(
					"device1", "ip1", "dev-ip1");
			JSONObject idPair2 = createJSONIdentifierMetadataConnection(
					"device1", "ar1", "ar-dev1");
			ArrayList<JSONObject> ids = new ArrayList<JSONObject>();
			ids.add(idPair1);
			ids.add(idPair2);
			JSONObject subGraph1 = createJSON(timestamp, ids);
			expected.put(subGraph1);
		}

		assertTrue(jsonsEqual(actual, expected));
	}

	@Override
	public void getCurrentGraph() throws Exception {
		List<IdentifierGraph> currentGraph = mService.getCurrentGraph();
		assertEquals(3, currentGraph.get(0).getIdentifiers().size());

		JSONArray actual = toJson(currentGraph);
		JSONArray expected = new JSONArray();
		{
			JSONObject idPair1 = createJSONIdentifierMetadataConnection(
					"device1", "ip1", "dev-ip1");
			JSONObject idPair2 = createJSONIdentifierMetadataConnection(
					"device1", "ar1", "ar-dev1");
			ArrayList<JSONObject> ids = new ArrayList<JSONObject>();
			ids.add(idPair1);
			ids.add(idPair2);
			JSONObject subGraph1 = createJSON(2l, ids);
			expected.put(subGraph1);
		}

		assertTrue(jsonsEqual(actual, expected));
	}

	@Override
	public void getDelta() throws Exception {
		// not implemented due to notify scenario
	}

	@Override
	public void getChangesMap() throws Exception {
		SortedMap<Long, Long> changesMap = mService.getChangesMap();
		SortedMap<Long, Long> expected = new TreeMap<Long, Long>();
		expected.put(0l, 1l);
		expected.put(1l, 1l);
		expected.put(2l, 2l);

		testChangesMap(expected, changesMap);
		testChangesMapJSON(expected, changesMap);
	}

	@Test
	public void getNotifiesAt() throws Exception {
		getNotifiesAt0();
		getNotifiesAt1();
		getNotifiesAt2();
	}

	public void getNotifiesAt0() throws Exception {
		long timestamp = 0;
		List<IdentifierGraph> notifiesAt = mService.getNotifiesAt(timestamp);
		assertEquals(0, notifiesAt.size());

		JSONArray actual = toJson(notifiesAt);
		JSONArray expected = new JSONArray();

		assertTrue(jsonsEqual(actual, expected));
	}

	public void getNotifiesAt1() throws Exception {
		long timestamp = 1;
		List<IdentifierGraph> notifiesAt = mService.getNotifiesAt(timestamp);
		assertEquals(1, notifiesAt.get(0).getIdentifiers().size());

		JSONArray actual = toJson(notifiesAt);
		JSONArray expected = new JSONArray();
		{
			JSONObject idPair1 = createJSONIdentifierMetadataConnection(
					"device1", "event1");
			JSONObject subGraph1 = createJSON(timestamp, idPair1);
			expected.put(subGraph1);
		}

		assertTrue(jsonsEqual(actual, expected));
	}

	public void getNotifiesAt2() throws Exception {
		long timestamp = 2;
		List<IdentifierGraph> notifiesAt = mService.getNotifiesAt(timestamp);
		assertEquals(1, notifiesAt.get(0).getIdentifiers().size());

		JSONArray actual = toJson(notifiesAt);
		JSONArray expected = new JSONArray();
		{
			JSONObject idPair1 = createJSONIdentifierMetadataConnection(
					"device1", "event2");
			JSONObject subGraph1 = createJSON(timestamp, idPair1);
			expected.put(subGraph1);
		}

		assertTrue(jsonsEqual(actual, expected));
	}
}
