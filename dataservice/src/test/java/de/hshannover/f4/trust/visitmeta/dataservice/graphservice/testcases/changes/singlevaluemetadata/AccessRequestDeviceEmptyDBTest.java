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
package de.hshannover.f4.trust.visitmeta.dataservice.graphservice.testcases.changes.singlevaluemetadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.testcases.AbstractTestCase;
import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

public class AccessRequestDeviceEmptyDBTest extends AbstractTestCase {

	private static final Logger mLog = Logger.getLogger(AccessRequestDeviceEmptyDBTest.class);

	private final String METADATA_CHANGES_DIRECTORY = TESTCASES_DIRECTORY + File.separator + "changes";

	private final String SINGLE_VALUE_METADATA_CHANGES_DIRECTORY = METADATA_CHANGES_DIRECTORY + File.separator + "single_value_metadata";

	private final String MULTI_VALUE_METADATA_CHANGES_DIRECTORY = METADATA_CHANGES_DIRECTORY + File.separator + "multi_value_metadata";

	private final String FILENAME = SINGLE_VALUE_METADATA_CHANGES_DIRECTORY + File.separator + "access-request-device_EmptyDB.yml";


	@Override
	public String getTestcaseFilename() {
		return FILENAME;
	}

	@Override
	public void getInitialGraph() {
		List<IdentifierGraph> initialGraph = mService.getInitialGraph();

		JSONArray actual = toJson(initialGraph);

		// check IdentifierGraph size
		assertEquals(1, initialGraph.size());

		// check metadata properties
		JSONObject properties = getPropertiesFromMetadata(actual, 0, 0);
		Map<String, Object> expectedProperties = getPropertiesFromMetadata("link1", "meta1");

		assertTrue(equalsMetadataProperties(properties, expectedProperties));

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]
	}

	@Override
	public void getGraphAt() {

		getGraphAt0();
		getGraphAt1();
		getGraphAt2();
		getGraphAt3();
		getGraphAt4();
		getGraphAt5();
		getGraphAt6();

	}

	private void getGraphAt6() {
		long timestamp = 6;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		JSONArray actual = toJson(graphAt);

		// check IdentifierGraph size
		assertEquals(0, graphAt.size());

		// check metadata properties
		// nothing to check, size must be 0

		// check Identifiers size
		// nothing to check, size must be 0

		// check JSON-String
		String expected = "[]";
		assertEquals(expected, actual.toString());
	}

	private void getGraphAt5() {
		long timestamp = 5;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		JSONArray actual = toJson(graphAt);

		// check IdentifierGraph size
		assertEquals(1, graphAt.size());

		// check metadata properties
		JSONObject properties = getPropertiesFromMetadata(actual, 0, 0);
		Map<String, Object> expectedProperties = getPropertiesFromMetadata("link5", "meta1");

		assertTrue(equalsMetadataProperties(properties, expectedProperties));

		// check Identifiers size
		// nothing to check, size must be 0

		// check JSON-String
		// TODO [MR]
	}

	private void getGraphAt4() {
		long timestamp = 4;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		JSONArray actual = toJson(graphAt);

		// check IdentifierGraph size
		assertEquals(0, graphAt.size());

		// check metadata properties
		// nothing to check, size must be 0

		// check Identifiers size
		// nothing to check, size must be 0

		// check JSON-String
		String expected = "[]";
		assertEquals(expected, actual.toString());
	}

	private void getGraphAt3() {
		long timestamp = 3;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		JSONArray actual = toJson(graphAt);

		// check IdentifierGraph size
		assertEquals(1, graphAt.size());

		// check metadata properties
		JSONObject properties = getPropertiesFromMetadata(actual, 0, 0);
		Map<String, Object> expectedProperties = getPropertiesFromMetadata("link4", "meta1");

		assertTrue(equalsMetadataProperties(properties, expectedProperties));

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]
	}

	private void getGraphAt2() {
		long timestamp = 2;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		JSONArray actual = toJson(graphAt);

		// check IdentifierGraph size
		assertEquals(1, graphAt.size());

		// check metadata properties
		JSONObject properties = getPropertiesFromMetadata(actual, 0, 0);
		Map<String, Object> expectedProperties = getPropertiesFromMetadata("link3", "meta1");

		assertTrue(equalsMetadataProperties(properties, expectedProperties));

		// check Identifiers size
		// nothing to check, size must be 0

		// check JSON-String
		// TODO [MR]
	}

	private void getGraphAt1() {
		long timestamp = 1;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		JSONArray actual = toJson(graphAt);

		// check IdentifierGraph size
		assertEquals(1, graphAt.size());

		// check metadata properties
		JSONObject properties = getPropertiesFromMetadata(actual, 0, 0);
		Map<String, Object> expectedProperties = getPropertiesFromMetadata("link2", "meta1");

		assertTrue(equalsMetadataProperties(properties, expectedProperties));

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]
	}

	private void getGraphAt0() {
		long timestamp = 0;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		JSONArray actual = toJson(graphAt);

		// check IdentifierGraph size
		assertEquals(1, graphAt.size());

		// check metadata properties
		JSONObject properties = getPropertiesFromMetadata(actual, 0, 0);
		Map<String, Object> expectedProperties = getPropertiesFromMetadata("link1", "meta1");

		assertTrue(equalsMetadataProperties(properties, expectedProperties));

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]
	}

	@Override
	public void getCurrentGraph() {
		List<IdentifierGraph> currentGraph = mService.getCurrentGraph();

		JSONArray actual = toJson(currentGraph);

		// check IdentifierGraph size
		assertTrue(currentGraph.isEmpty());

		// check metadata properties
		// nothing to check, size must be 0

		// check Identifiers size
		// nothing to check, size must be 0

		// check JSON-String
		String expected = "[]";
		assertEquals(expected, actual.toString());
	}

	@Override
	public void getDelta() {
		//										x Δ x -> x= the property number
		getDeltaFrom0To1();		//				0 Δ 1
		getDeltaFrom0To2();		//				0 Δ 0
		getDeltaFrom0To3();		//				0 Δ 2
		getDeltaFrom0To4();		//				0 Δ (empty graph)
		getDeltaFrom1To2();		//				1 Δ 0
		getDeltaFrom1To3();		//				1 Δ 2
		getDeltaFrom1To4();		//				1 Δ (empty graph)
		getDeltaFrom1To5();		//				1 Δ 1
		getDeltaFrom2To3();		//				0 Δ 2
		getDeltaFrom2To4();		//				0 Δ (empty graph)
		getDeltaFrom3To5();		//				2 Δ 1
		getDeltaFrom4To5();		//	(empty graph) Δ 1

	}

	/**
	 * Delta from a graph with one metadata without properties and a empty graph
	 */
	private void getDeltaFrom0To4() {
		long t1 = 0;
		long t2 = 4;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		JSONArray actualDeletes = toJson(deletes);
		JSONArray actualUpdates = toJson(updates);

		// check Delta sizes for deletes and updates
		assertEquals(1, deletes.size());
		assertEquals(0, updates.size());

		// check metadata properties
		// check deletes
		JSONObject propertiesDeletes = getPropertiesFromMetadata(actualDeletes, 0, 0);
		Map<String, Object> expectedPropertiesDeletes = getPropertiesFromMetadata("link1", "meta1");

		assertTrue(equalsMetadataProperties(propertiesDeletes, expectedPropertiesDeletes));

		// check updates
		// nothing to check, size must be 0

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]

	}

	/**
	 * Delta from a graph with one metadata without properties and a graph with one metadata and two properties
	 */
	private void getDeltaFrom0To3() {
		long t1 = 0;
		long t2 = 3;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		JSONArray actualDeletes = toJson(deletes);
		JSONArray actualUpdates = toJson(updates);

		// check Delta sizes for deletes and updates
		assertEquals(1, deletes.size());
		assertEquals(1, updates.size());

		// check metadata properties
		// check deletes
		JSONObject propertiesDeletes = getPropertiesFromMetadata(actualDeletes, 0, 0);
		Map<String, Object> expectedPropertiesDeletes = getPropertiesFromMetadata("link1", "meta1");

		assertTrue(equalsMetadataProperties(propertiesDeletes, expectedPropertiesDeletes));

		// check updates
		JSONObject propertiesUpdates = getPropertiesFromMetadata(actualUpdates, 0, 0);
		Map<String, Object> expectedPropertiesUpdates = getPropertiesFromMetadata("link4", "meta1");

		assertTrue(equalsMetadataProperties(propertiesUpdates, expectedPropertiesUpdates));

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]

	}

	/**
	 * Delta from a graph with one metadata without properties and a graph with metadata without properties, too
	 */
	private void getDeltaFrom0To2() {
		long t1 = 0;
		long t2 = 2;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		JSONArray actualDeletes = toJson(deletes);
		JSONArray actualUpdates = toJson(updates);

		// check Delta sizes for deletes and updates
		assertEquals(0, deletes.size());
		assertEquals(0, updates.size());

		// check metadata properties
		// check deletes
		// nothing to check, size must be 0

		// check updates
		// nothing to check, size must be 0

		// check Identifiers size
		// nothing to check, size must be 0

		// check JSON-String
		// check deletes
		String expectedDeletes = "[]";
		assertEquals(expectedDeletes, actualDeletes.toString());

		// check updates
		String expectedUpdates = "[]";
		assertEquals(expectedUpdates, actualUpdates.toString());
	}

	/**
	 * Delta from a graph with one metadata without properties and a graph with one metadata and one property
	 */
	private void getDeltaFrom0To1() {
		long t1 = 0;
		long t2 = 1;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		JSONArray actualDeletes = toJson(deletes);
		JSONArray actualUpdates = toJson(updates);

		// check Delta sizes for deletes and updates
		assertEquals(1, deletes.size());
		assertEquals(1, updates.size());

		// check metadata properties
		// check deletes
		JSONObject propertiesDeletes = getPropertiesFromMetadata(actualDeletes, 0, 0);
		Map<String, Object> expectedPropertiesDeletes = getPropertiesFromMetadata("link1", "meta1");

		assertTrue(equalsMetadataProperties(propertiesDeletes, expectedPropertiesDeletes));

		// check updates
		JSONObject propertiesUpdates = getPropertiesFromMetadata(actualUpdates, 0, 0);
		Map<String, Object> expectedPropertiesUpdates = getPropertiesFromMetadata("link2", "meta1");

		assertTrue(equalsMetadataProperties(propertiesUpdates, expectedPropertiesUpdates));

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]
	}

	/**
	 * Delta from a graph with one metadata and one property and a graph with one metadata and one property, too
	 */
	private void getDeltaFrom1To5() {
		long t1 = 1;
		long t2 = 5;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		JSONArray actualDeletes = toJson(deletes);
		JSONArray actualUpdates = toJson(updates);

		// check Delta sizes for deletes and updates
		assertEquals(0, deletes.size());
		assertEquals(0, updates.size());

		// check metadata properties
		// check deletes
		// nothing to check, size must be 0

		// check updates
		// nothing to check, size must be 0

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]
	}

	/**
	 * Delta from a graph with one metadata and one property and a empty graph
	 */
	private void getDeltaFrom1To4() {
		long t1 = 1;
		long t2 = 4;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		JSONArray actualDeletes = toJson(deletes);
		JSONArray actualUpdates = toJson(updates);

		// check Delta sizes for deletes and updates
		assertEquals(1, deletes.size());
		assertEquals(0, updates.size());

		// check metadata properties
		// check deletes
		JSONObject propertiesDeletes = getPropertiesFromMetadata(actualDeletes, 0, 0);
		Map<String, Object> expectedPropertiesDeletes = getPropertiesFromMetadata("link2", "meta1");

		assertTrue(equalsMetadataProperties(propertiesDeletes, expectedPropertiesDeletes));

		// check updates
		// nothing to check, size must be 0

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]
	}

	/**
	 * Delta from a graph with one metadata and one property and a graph with metadata and two properties
	 */
	private void getDeltaFrom1To3() {
		long t1 = 1;
		long t2 = 3;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		JSONArray actualDeletes = toJson(deletes);
		JSONArray actualUpdates = toJson(updates);

		// check Delta sizes for deletes and updates
		assertEquals(1, deletes.size());
		assertEquals(1, updates.size());

		// check metadata properties
		// check deletes
		JSONObject propertiesDeletes = getPropertiesFromMetadata(actualDeletes, 0, 0);
		Map<String, Object> expectedPropertiesDeletes = getPropertiesFromMetadata("link2", "meta1");

		assertTrue(equalsMetadataProperties(propertiesDeletes, expectedPropertiesDeletes));

		// check updates
		JSONObject propertiesUpdates = getPropertiesFromMetadata(actualUpdates, 0, 0);
		Map<String, Object> expectedPropertiesUpdates = getPropertiesFromMetadata("link4", "meta1");

		assertTrue(equalsMetadataProperties(propertiesUpdates, expectedPropertiesUpdates));

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]
	}

	/**
	 * Delta from a graph with one metadata and one property and a graph with metadata without properties
	 */
	private void getDeltaFrom1To2() {
		long t1 = 1;
		long t2 = 2;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		JSONArray actualDeletes = toJson(deletes);
		JSONArray actualUpdates = toJson(updates);

		// check Delta sizes for deletes and updates
		assertEquals(1, deletes.size());
		assertEquals(1, updates.size());

		// check metadata properties
		// check deletes
		JSONObject propertiesDeletes = getPropertiesFromMetadata(actualDeletes, 0, 0);
		Map<String, Object> expectedPropertiesDeletes = getPropertiesFromMetadata("link2", "meta1");

		assertTrue(equalsMetadataProperties(propertiesDeletes, expectedPropertiesDeletes));

		// check updates
		JSONObject propertiesUpdates = getPropertiesFromMetadata(actualUpdates, 0, 0);
		Map<String, Object> expectedPropertiesUpdates = getPropertiesFromMetadata("link3", "meta1");

		assertTrue(equalsMetadataProperties(propertiesUpdates, expectedPropertiesUpdates));

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]
	}

	/**
	 * Delta from a graph with one metadata without properties and a empty graph
	 */
	private void getDeltaFrom2To4() {
		long t1 = 2;
		long t2 = 4;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		JSONArray actualDeletes = toJson(deletes);
		JSONArray actualUpdates = toJson(updates);

		// check Delta sizes for deletes and updates
		assertEquals(1, deletes.size());
		assertEquals(0, updates.size());

		// check metadata properties
		// check deletes
		JSONObject propertiesDeletes = getPropertiesFromMetadata(actualDeletes, 0, 0);
		Map<String, Object> expectedPropertiesDeletes = getPropertiesFromMetadata("link3", "meta1");

		assertTrue(equalsMetadataProperties(propertiesDeletes, expectedPropertiesDeletes));

		// check updates
		// nothing to check, size must be 0

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]
	}

	/**
	 * Delta from a graph with one metadata without properties and a graph with metadata with two properties
	 */
	private void getDeltaFrom2To3() {
		long t1 = 2;
		long t2 = 3;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		JSONArray actualDeletes = toJson(deletes);
		JSONArray actualUpdates = toJson(updates);

		// check Delta sizes for deletes and updates
		assertEquals(1, deletes.size());
		assertEquals(1, updates.size());

		// check metadata properties
		// check deletes
		JSONObject propertiesDeletes = getPropertiesFromMetadata(actualDeletes, 0, 0);
		Map<String, Object> expectedPropertiesDeletes = getPropertiesFromMetadata("link3", "meta1");

		assertTrue(equalsMetadataProperties(propertiesDeletes, expectedPropertiesDeletes));

		// check updates
		JSONObject propertiesUpdates = getPropertiesFromMetadata(actualUpdates, 0, 0);
		Map<String, Object> expectedPropertiesUpdates = getPropertiesFromMetadata("link4", "meta1");

		assertTrue(equalsMetadataProperties(propertiesUpdates, expectedPropertiesUpdates));

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]
	}

	/**
	 * Delta from a graph with one metadata and two properties and a graph with metadata with one property
	 */
	private void getDeltaFrom3To5() {
		long t1 = 3;
		long t2 = 5;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		JSONArray actualDeletes = toJson(deletes);
		JSONArray actualUpdates = toJson(updates);

		// check Delta sizes for deletes and updates
		assertEquals(1, deletes.size());
		assertEquals(1, updates.size());

		// check metadata properties
		// check deletes
		JSONObject propertiesDeletes = getPropertiesFromMetadata(actualDeletes, 0, 0);
		Map<String, Object> expectedPropertiesDeletes = getPropertiesFromMetadata("link4", "meta1");

		assertTrue(equalsMetadataProperties(propertiesDeletes, expectedPropertiesDeletes));

		// check updates
		JSONObject propertiesUpdates = getPropertiesFromMetadata(actualUpdates, 0, 0);
		Map<String, Object> expectedPropertiesUpdates = getPropertiesFromMetadata("link5", "meta1");

		assertTrue(equalsMetadataProperties(propertiesUpdates, expectedPropertiesUpdates));

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]
	}

	/**
	 * Delta from a empty graph and a graph with one metadata and one property
	 */
	private void getDeltaFrom4To5() {
		long t1 = 4;
		long t2 = 5;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		JSONArray actualDeletes = toJson(deletes);
		JSONArray actualUpdates = toJson(updates);

		// check Delta sizes for deletes and updates
		assertEquals(0, deletes.size());
		assertEquals(1, updates.size());

		// check metadata properties
		// check deletes
		// nothing to check, size must be 0

		// check updates
		JSONObject propertiesUpdates = getPropertiesFromMetadata(actualUpdates, 0, 0);
		Map<String, Object> expectedPropertiesUpdates = getPropertiesFromMetadata("link5", "meta1");

		assertTrue(equalsMetadataProperties(propertiesUpdates, expectedPropertiesUpdates));

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]
	}

	@Override
	public void getChangesMap() {
		SortedMap<Long,Long> changesMap = mService.getChangesMap();

		long t0 = 0;
		long t1 = 1;
		long t2 = 2;
		long t3 = 3;
		long t4 = 4;
		long t5 = 5;
		long t6 = 6;

		assertTrue(changesMap.size() == 7);

		assertEquals(1, (long) changesMap.get(t0));
		assertEquals(2, (long) changesMap.get(t1));
		assertEquals(2, (long) changesMap.get(t2));
		assertEquals(2, (long) changesMap.get(t3));
		assertEquals(1, (long) changesMap.get(t4));
		assertEquals(1, (long) changesMap.get(t5));
		assertEquals(1, (long) changesMap.get(t6));

		JSONObject actual = toJson(changesMap);
	}

}
