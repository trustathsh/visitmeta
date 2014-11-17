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

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.testcases.AbstractTestCase;
import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

public class AccessRequestDeviceFilledDBTest extends AbstractTestCase {

	private static final Logger mLog = Logger.getLogger(AccessRequestDeviceFilledDBTest.class);

	private final String METADATA_CHANGES_DIRECTORY = TESTCASES_DIRECTORY + File.separator + "changes";

	private final String SINGLE_VALUE_METADATA_CHANGES_DIRECTORY = METADATA_CHANGES_DIRECTORY + File.separator + "single_value_metadata";

	private final String MULTI_VALUE_METADATA_CHANGES_DIRECTORY = METADATA_CHANGES_DIRECTORY + File.separator + "multi_value_metadata";

	private final String FILENAME = SINGLE_VALUE_METADATA_CHANGES_DIRECTORY + File.separator + "access-request-device_FilledDB.yml";


	@Override
	public String getTestcaseFilename() {
		return FILENAME;
	}

	@Override
	public void getInitialGraph() {
		// Was already tested in AccessRequestDeviceEmptyDBTest
	}

	@Override
	public void getGraphAt() {
		// Graph at 0 - 5 was already tested in AccessRequestDeviceEmptyDBTest
		getGraphAt6();

	}

	private void getGraphAt6() {
		long timestamp = 6;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		JSONArray actual = toJson(graphAt);

		// check IdentifierGraph size
		assertEquals(1, graphAt.size());

		// check metadata properties
		JSONObject properties = getPropertiesFromMetadata(actual, 0, 0);
		Map<String, Object> expectedProperties = getPropertiesFromMetadata("link5", "meta1");

		assertTrue(equalsMetadataProperties(properties, expectedProperties));

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]
	}

	@Override
	public void getCurrentGraph() {
		// Was already tested in AccessRequestDeviceEmptyDBTest
	}

	@Override
	public void getDelta() {
		//										x Δ x -> x= the property number
		getDeltaFrom0To6();		//				0 Δ 1 (metadata delete Stamp is -1)
		getDeltaFrom1To6();		//				1 Δ 1 (metadata delete Stamp is -1)
		getDeltaFrom3To6();		//				2 Δ 1 (metadata delete Stamp is -1)

	}

	/**
	 * Delta from a graph with one metadata without properties and a graph with one metadata(Delete Stamp is -1) and one property.
	 */
	private void getDeltaFrom0To6() {
		long t1 = 0;
		long t2 = 6;
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
		Map<String, Object> expectedPropertiesUpdates = getPropertiesFromMetadata("link5", "meta1");

		assertTrue(equalsMetadataProperties(propertiesUpdates, expectedPropertiesUpdates));

		// check Identifiers size

		// TODO [MR]

		// check JSON-String

		// TODO [MR]

	}


	/**
	 * Delta from a graph with one metadata and one property and a graph with one metadata and one property, too
	 */
	private void getDeltaFrom1To6() {
		long t1 = 1;
		long t2 = 6;
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
	 * Delta from a graph with one metadata and two properties and a graph with metadata with one property
	 */
	private void getDeltaFrom3To6() {
		long t1 = 3;
		long t2 = 6;
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

	@Override
	public void getChangesMap() {
		// Was already tested in AccessRequestDeviceEmptyDBTest
	}

}
