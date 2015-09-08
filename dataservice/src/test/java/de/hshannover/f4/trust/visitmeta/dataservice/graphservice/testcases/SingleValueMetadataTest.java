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
 * This file is part of visitmeta-dataservice, version 0.5.2,
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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

public class SingleValueMetadataTest extends AbstractTestCase {

	private final String FILENAME = TESTCASES_DIRECTORY + File.separator + "SingleValueMetadataTestDB.yml";

	@Override
	public String getTestcaseFilename() {
		return FILENAME;
	}

	@Override
	public void getInitialGraph() throws JSONException {
		List<IdentifierGraph> initialGraph = mService.getInitialGraph();
		JSONArray actual = toJson(initialGraph);

		// ### check IdentifierGraph size ###
		testGraphListSize(initialGraph, 1);

		// ### check Identifiers size ###
		IdentifierGraph graph = initialGraph.get(0);
		testIdentifierCount(graph, 2);

		// ### check JSON-String ###
		JSONArray expected = new JSONArray();
		expected.put(createJSON(0L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device1_rawData1")));

		assertTrue(jsonsEqual(actual, expected));
	}

	@Override
	public void getGraphAt() throws JSONException {

		getGraphAt0();
		getGraphAt1();
		getGraphAt2();
		getGraphAt3();
		getGraphAt4();
		getGraphAt5();

	}

	private void getGraphAt5() throws JSONException {
		long timestamp = 5;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		JSONArray actual = toJson(graphAt);

		// ### check IdentifierGraph size ###
		testGraphListSize(graphAt, 1);

		// ### check Identifiers size ###
		IdentifierGraph graph = graphAt.get(0);
		testIdentifierCount(graph, 2);

		// ### check JSON-String ###
		JSONArray expected = new JSONArray();
		expected.put(createJSON(5L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device3_rawData1")));

		assertTrue(jsonsEqual(actual, expected));
	}

	private void getGraphAt4() throws JSONException {
		long timestamp = 4;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		JSONArray actual = toJson(graphAt);

		// ### check IdentifierGraph size ###
		testGraphListSize(graphAt, 0);

		// ### check Identifiers size ###
		// nothing to check, size must be 0

		// ### check metadata properties ###
		// nothing to check, size must be 0

		// ### check JSON-String ###
		JSONArray expected = new JSONArray();
		assertTrue(jsonsEqual(actual, expected));
	}

	private void getGraphAt3() throws JSONException {
		long timestamp = 3;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		JSONArray actual = toJson(graphAt);

		// ### check IdentifierGraph size ###
		testGraphListSize(graphAt, 1);

		// ### check Identifiers size ###
		IdentifierGraph graph = graphAt.get(0);
		testIdentifierCount(graph, 2);

		// ### check JSON-String ###
		JSONArray expected = new JSONArray();
		expected.put(createJSON(3L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device3_rawData1")));

		assertTrue(jsonsEqual(actual, expected));

	}

	private void getGraphAt2() throws JSONException {
		long timestamp = 2;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		JSONArray actual = toJson(graphAt);

		// ### check IdentifierGraph size ###
		testGraphListSize(graphAt, 0);

		// ### check Identifiers size ###
		// nothing to check, size must be 0

		// ### check metadata properties ###
		// nothing to check, size must be 0

		// ### check JSON-String ###
		JSONArray expected = new JSONArray();
		assertTrue(jsonsEqual(actual, expected));
	}

	private void getGraphAt1() throws JSONException {
		long timestamp = 1;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		JSONArray actual = toJson(graphAt);

		// ### check IdentifierGraph size ###
		testGraphListSize(graphAt, 1);

		// ### check Identifiers size ###
		IdentifierGraph graph = graphAt.get(0);
		testIdentifierCount(graph, 2);

		// ### check JSON-String ###
		JSONArray expected = new JSONArray();
		expected.put(createJSON(1L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device2_rawData1")));

		assertTrue(jsonsEqual(actual, expected));
	}

	private void getGraphAt0() throws JSONException {
		long timestamp = 0;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		JSONArray actual = toJson(graphAt);

		// ### check IdentifierGraph size ###
		testGraphListSize(graphAt, 1);

		// ### check Identifiers size ###
		IdentifierGraph graph = graphAt.get(0);
		testIdentifierCount(graph, 2);

		// ### check JSON-String ###
		JSONArray expected = new JSONArray();
		expected.put(createJSON(0L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device1_rawData1")));

		assertTrue(jsonsEqual(actual, expected));
	}

	@Override
	public void getCurrentGraph() throws JSONException {
		List<IdentifierGraph> currentGraph = mService.getCurrentGraph();

		JSONArray actual = toJson(currentGraph);

		// ### check IdentifierGraph size ###
		testGraphListSize(currentGraph, 1);

		// ### check Identifiers size ###
		IdentifierGraph graph = currentGraph.get(0);
		testIdentifierCount(graph, 2);

		// ### check JSON-String ###
		JSONArray expected = new JSONArray();
		expected.put(createJSON(5L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device3_rawData1")));

		assertTrue(jsonsEqual(actual, expected));
	}

	@Override
	public void getDelta() throws JSONException {
		//										x Δ x -> x= the property number
		getDeltaFrom0To1();		//				0 Δ 2
		getDeltaFrom0To2();		//				0 Δ (empty graph)
		getDeltaFrom0To3();		//				0 Δ 1
		getDeltaFrom0To4();		//				0 Δ (empty graph)
		getDeltaFrom0To5();		//				0 Δ 1 (metadata delete Stamp is -1)

		getDeltaFrom1To2();		//				2 Δ (empty graph)
		getDeltaFrom1To3();		//				2 Δ 1
		getDeltaFrom1To4();		//				2 Δ (empty graph)
		getDeltaFrom1To5();		//				2 Δ 1

		getDeltaFrom2To3();		//				2 Δ 1
		getDeltaFrom2To4();		//				2 Δ (empty graph)
		getDeltaFrom2To5();		//				2 Δ 1

		getDeltaFrom3To4();		//				1 Δ (empty graph)
		getDeltaFrom3To5();		//				1 Δ 1

		getDeltaFrom4To5();		//	(empty graph) Δ 1

	}

	/**
	 * Delta from a graph with one metadata without properties and a graph with one metadata and one property
	 * @throws JSONException
	 */
	private void getDeltaFrom0To5() throws JSONException {
		long t1 = 0;
		long t2 = 5;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		// ### check Delta sizes for deletes and updates ###
		testDeltaSize(delta, 1, 1);

		// ### check Identifiers size ###
		// check deletes
		IdentifierGraph deletegraph = deletes.get(0);
		testIdentifierCount(deletegraph, 2);

		// check updates
		IdentifierGraph updategraph = updates.get(0);
		testIdentifierCount(updategraph, 2);

		// ### check JSON-String ###
		// check deletes
		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedDeletes = new JSONArray();
		expectedDeletes.put(createJSON(5L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device1_rawData1")));

		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));

		// check updates
		JSONArray actualUpdates = toJson(updates);
		JSONArray expectedUpdates = new JSONArray();
		expectedUpdates.put(createJSON(5L,
				createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device3_rawData1")));

		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));
	}

	/**
	 * Delta from a graph with one metadata without properties and a empty graph
	 * @throws JSONException
	 */
	private void getDeltaFrom0To4() throws JSONException {
		long t1 = 0;
		long t2 = 4;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();

		// ### check Delta sizes for deletes and updates ###
		testDeltaSize(delta, 0, 1);

		// ### check Identifiers size ###
		// check deletes
		IdentifierGraph deletegraph = deletes.get(0);
		testIdentifierCount(deletegraph, 2);

		// check updates
		// nothing to check, size must be 0

		// ### check JSON-String ###
		// check deletes
		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedDeletes = new JSONArray();
		expectedDeletes.put(createJSON(4L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device1_rawData1")));

		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));

		// check updates
		// nothing to check, size must be 0
	}

	/**
	 * Delta from a graph with one metadata without properties and a graph with one metadata and one property
	 * @throws JSONException
	 */
	private void getDeltaFrom0To3() throws JSONException {
		long t1 = 0;
		long t2 = 3;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		// ### check Delta sizes for deletes and updates ###
		testDeltaSize(delta, 1, 1);

		// ### check Identifiers size ###
		// check deletes
		IdentifierGraph deletegraph = deletes.get(0);
		testIdentifierCount(deletegraph, 2);

		// check updates
		IdentifierGraph updategraph = updates.get(0);
		testIdentifierCount(updategraph, 2);

		// ### check JSON-String ###
		// check deletes
		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedDeletes = new JSONArray();
		expectedDeletes.put(createJSON(3L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device1_rawData1")));

		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));

		// check updates
		JSONArray actualUpdates = toJson(updates);
		JSONArray expectedUpdates = new JSONArray();
		expectedUpdates.put(createJSON(3L,
				createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device3_rawData1")));

		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));
	}

	/**
	 * Delta from a graph with one metadata without properties and a empty graph
	 * @throws JSONException
	 */
	private void getDeltaFrom0To2() throws JSONException {
		long t1 = 0;
		long t2 = 2;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();

		// ### check Delta sizes for deletes and updates ###
		testDeltaSize(delta, 0, 1);

		// ### check Identifiers size ###
		// check deletes
		IdentifierGraph deletegraph = deletes.get(0);
		testIdentifierCount(deletegraph, 2);

		// check updates
		// nothing to check, size must be 0

		// ### check JSON-String ###
		// check deletes
		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedDeletes = new JSONArray();
		expectedDeletes.put(createJSON(2L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device1_rawData1")));

		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));

		// check updates
		// nothing to check, size must be 0
	}

	/**
	 * Delta from a graph with one metadata without properties and a graph with one metadata and two properties
	 * @throws JSONException
	 */
	private void getDeltaFrom0To1() throws JSONException {
		long t1 = 0;
		long t2 = 1;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		// ### check Delta sizes for deletes and updates ###
		testDeltaSize(delta, 1, 1);

		// ### check Identifiers size ###
		// check deletes
		IdentifierGraph deletegraph = deletes.get(0);
		testIdentifierCount(deletegraph, 2);

		// check updates
		IdentifierGraph updategraph = updates.get(0);
		testIdentifierCount(updategraph, 2);

		// ### check JSON-String ###
		// check deletes
		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedDeletes = new JSONArray();
		expectedDeletes.put(createJSON(1L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device1_rawData1")));

		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));

		// check updates
		JSONArray actualUpdates = toJson(updates);
		JSONArray expectedUpdates = new JSONArray();
		expectedUpdates.put(createJSON(1L,
				createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device2_rawData1")));

		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));
	}

	/**
	 * Delta from a graph with one metadata and two properties and a graph with one metadata and one property
	 * @throws JSONException
	 */
	private void getDeltaFrom1To5() throws JSONException {
		long t1 = 1;
		long t2 = 5;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		// ### check Delta sizes for deletes and updates ###
		testDeltaSize(delta, 1, 1);

		// ### check Identifiers size ###
		// check deletes
		IdentifierGraph deletegraph = deletes.get(0);
		testIdentifierCount(deletegraph, 2);

		// check updates
		IdentifierGraph updategraph = updates.get(0);
		testIdentifierCount(updategraph, 2);

		// ### check JSON-String ###
		// check deletes
		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedDeletes = new JSONArray();
		expectedDeletes.put(createJSON(5L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device2_rawData1")));

		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));

		// check updates
		JSONArray actualUpdates = toJson(updates);
		JSONArray expectedUpdates = new JSONArray();
		expectedUpdates.put(createJSON(5L,
				createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device3_rawData1")));

		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));
	}

	/**
	 * Delta from a graph with one metadata and two properties and a empty graph
	 * @throws JSONException
	 */
	private void getDeltaFrom1To4() throws JSONException {
		long t1 = 1;
		long t2 = 4;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();

		// ### check Delta sizes for deletes and updates ###
		testDeltaSize(delta, 0, 1);

		// ### check Identifiers size ###
		// check deletes
		IdentifierGraph deletegraph = deletes.get(0);
		testIdentifierCount(deletegraph, 2);

		// check updates
		// nothing to check, size must be 0

		// ### check JSON-String ###
		// check deletes
		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedDeletes = new JSONArray();
		expectedDeletes.put(createJSON(4L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device2_rawData1")));

		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));

		// check updates
		// nothing to check, size must be 0
	}

	/**
	 * Delta from a graph with one metadata and two properties and a graph with metadata and one property
	 * @throws JSONException
	 */
	private void getDeltaFrom1To3() throws JSONException {
		long t1 = 1;
		long t2 = 3;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		// ### check Delta sizes for deletes and updates ###
		testDeltaSize(delta, 1, 1);

		// ### check Identifiers size ###
		// check deletes
		IdentifierGraph deletegraph = deletes.get(0);
		testIdentifierCount(deletegraph, 2);

		// check updates
		IdentifierGraph updategraph = updates.get(0);
		testIdentifierCount(updategraph, 2);

		// ### check JSON-String ###
		// check deletes
		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedDeletes = new JSONArray();
		expectedDeletes.put(createJSON(3L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device2_rawData1")));

		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));

		// check updates
		JSONArray actualUpdates = toJson(updates);
		JSONArray expectedUpdates = new JSONArray();
		expectedUpdates.put(createJSON(3L,
				createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device3_rawData1")));

		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));
	}

	/**
	 * Delta from a graph with one metadata and two properties and a empty graph
	 * @throws JSONException
	 */
	private void getDeltaFrom1To2() throws JSONException {
		long t1 = 1;
		long t2 = 2;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();

		// ### check Delta sizes for deletes and updates ###
		testDeltaSize(delta, 0, 1);

		// ### check Identifiers size ###
		// check deletes
		IdentifierGraph deletegraph = deletes.get(0);
		testIdentifierCount(deletegraph, 2);

		// check updates
		// nothing to check, size must be 0

		// ### check JSON-String ###
		// check deletes
		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedDeletes = new JSONArray();
		expectedDeletes.put(createJSON(2L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device2_rawData1")));

		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));

		// check updates
		// nothing to check, size must be 0
	}

	/**
	 * Delta from a graph with one metadata and two properties and a graph with metadata and one property
	 * @throws JSONException
	 */
	private void getDeltaFrom2To5() throws JSONException {
		long t1 = 2;
		long t2 = 5;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> updates = delta.getUpdates();

		// ### check Delta sizes for deletes and updates ###
		testDeltaSize(delta, 1, 0);

		// ### check Identifiers size ###
		// check deletes
		// nothing to check, size must be 0

		// check updates
		IdentifierGraph updategraph = updates.get(0);
		testIdentifierCount(updategraph, 2);

		// ### check JSON-String ###
		// check deletes

		// check updates
		JSONArray actualUpdates = toJson(updates);
		JSONArray expectedUpdates = new JSONArray();
		expectedUpdates.put(createJSON(5L,
				createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device3_rawData1")));

		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));
	}

	/**
	 * Delta from a graph with one metadata and two properties and a empty graph
	 * @throws JSONException
	 */
	private void getDeltaFrom2To4() throws JSONException {
		long t1 = 2;
		long t2 = 4;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		// ### check Delta sizes for deletes and updates ###
		testDeltaSize(delta, 0, 0);

		// ### check metadata properties ###
		// check deletes
		// nothing to check, size must be 0

		// check updates
		// nothing to check, size must be 0

		// ### check Identifiers size ###
		// nothing to check, size must be 0

		// ### check JSON-String ###
		// check deletes
		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedDeletes = new JSONArray();
		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));

		// check updates
		JSONArray actualUpdates = toJson(updates);
		JSONArray expectedUpdates = new JSONArray();
		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));
	}

	/**
	 * Delta from a graph with one metadata and two properties and a graph with metadata with one property
	 * @throws JSONException
	 */
	private void getDeltaFrom2To3() throws JSONException {
		long t1 = 2;
		long t2 = 3;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> updates = delta.getUpdates();

		// ### check Delta sizes for deletes and updates ###
		testDeltaSize(delta, 1, 0);

		// ### check Identifiers size ###
		// check deletes
		// nothing to check, size must be 0

		// check updates
		IdentifierGraph updategraph = updates.get(0);
		testIdentifierCount(updategraph, 2);

		// ### check JSON-String ###
		// check deletes

		// check updates
		JSONArray actualUpdates = toJson(updates);
		JSONArray expectedUpdates = new JSONArray();
		expectedUpdates.put(createJSON(3L,
				createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device3_rawData1")));

		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));
	}

	/**
	 * Delta from a graph with one metadata and one property and a graph with metadata with one property
	 * @throws JSONException
	 */
	private void getDeltaFrom3To5() throws JSONException {
		long t1 = 3;
		long t2 = 5;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		// ### check Delta sizes for deletes and updates ###
		testDeltaSize(delta, 0, 0);

		// ### check metadata properties ###
		// check deletes
		// nothing to check, size must be 0

		// check updates
		// nothing to check, size must be 0

		// ### check Identifiers size ###
		// nothing to check, size must be 0

		// ### check JSON-String ###
		// check deletes
		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedDeletes = new JSONArray();
		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));

		// check updates
		JSONArray actualUpdates = toJson(updates);
		JSONArray expectedUpdates = new JSONArray();
		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));
	}

	/**
	 * Delta from a graph with one metadata and one property and a empty graph
	 * @throws JSONException
	 */
	private void getDeltaFrom3To4() throws JSONException {
		long t1 = 3;
		long t2 = 4;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();

		// ### check Delta sizes for deletes and updates ###
		testDeltaSize(delta, 0, 1);

		// ### check Identifiers size ###
		// check deletes
		IdentifierGraph deletegraph = deletes.get(0);
		testIdentifierCount(deletegraph, 2);

		// check updates
		// nothing to check, size must be 0

		// ### check JSON-String ###
		// check deletes
		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedDeletes = new JSONArray();
		expectedDeletes.put(createJSON(4L,createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device3_rawData1")));

		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));

		// check updates
		// nothing to check, size must be 0
	}

	/**
	 * Delta from a empty graph and a graph with one metadata and one property
	 * @throws JSONException
	 */
	private void getDeltaFrom4To5() throws JSONException {
		long t1 = 4;
		long t2 = 5;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> updates = delta.getUpdates();

		// ### check Delta sizes for deletes and updates ###
		testDeltaSize(delta, 1, 0);

		// ### check Identifiers size ###
		// check deletes
		// nothing to check, size must be 0

		// check updates
		IdentifierGraph updategraph = updates.get(0);
		testIdentifierCount(updategraph, 2);

		// ### check JSON-String ###
		// check deletes

		// check updates
		JSONArray actualUpdates = toJson(updates);
		JSONArray expectedUpdates = new JSONArray();
		expectedUpdates.put(createJSON(5L,
				createJSONIdentifierMetadataConnection("access-request_rawData1", "device_rawData1", "access-request-device3_rawData1")));

		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));
	}

	@Override
	public void getChangesMap() throws JSONException {
		SortedMap<Long,Long> changesMap = mService.getChangesMap();

		SortedMap<Long,Long> expectedChanges = new TreeMap<Long,Long>();
		expectedChanges.put(0L, 1L);	// t0
		expectedChanges.put(1L, 2L);	// t1
		expectedChanges.put(2L, 1L);	// t2
		expectedChanges.put(3L, 1L);	// t3
		expectedChanges.put(4L, 1L);	// t4
		expectedChanges.put(5L, 1L);	// t5

		testChangesMap(expectedChanges, changesMap);
		testChangesMapJSON(expectedChanges, changesMap);
	}

}
