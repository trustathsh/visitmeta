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
import java.util.List;
import java.util.SortedMap;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

public class SingleValueMetadataInsertUpdateAndDeleteTestCase extends
AbstractTestCase {

	private final String TESTCASE_FILENAME = TESTCASES_DIRECTORY + File.separator + "singleValueMetadataInsertUpdateAndDelete.yml";

	@Override
	public String getTestcaseFilename() {
		return TESTCASE_FILENAME;
	}

	@Override
	public void getInitialGraph() {
		List<IdentifierGraph> initialGraph = mService.getInitialGraph();

		assertEquals(1, initialGraph.size());

		JSONArray actual = toJson(initialGraph);
	}

	@Override
	public void getGraphAt() {
		getGraphAt0();
		getGraphAt1();
		getGraphAt2();
	}

	private void getGraphAt2() {
		long timestamp = 2;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		assertEquals(0, graphAt.size());

		JSONArray actual = toJson(graphAt);
	}

	private void getGraphAt1() {
		long timestamp = 1;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		assertEquals(1, graphAt.size());

		JSONArray actual = toJson(graphAt);
	}

	private void getGraphAt0() {
		long timestamp = 0;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		assertEquals(1, graphAt.size());

		JSONArray actual = toJson(graphAt);
	}

	@Override
	public void getCurrentGraph() {
		List<IdentifierGraph> currentGraph = mService.getCurrentGraph();

		assertTrue(currentGraph.isEmpty());

		JSONArray actual = toJson(currentGraph);
	}

	@Override
	public void getDelta() {
		getDeltaFrom0To1();
		getDeltaFrom1To2();
		getDeltaFrom0To2();
	}

	private void getDeltaFrom0To2() {
		long t1 = 0;
		long t2 = 2;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		assertEquals(1, deletes.size());
		assertEquals(0, updates.size());

		JSONObject actual = toJson(delta);

	}

	private void getDeltaFrom1To2() {
		long t1 = 1;
		long t2 = 2;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		assertEquals(1, deletes.size());
		assertEquals(0, updates.size());

		JSONObject actual = toJson(delta);
	}

	private void getDeltaFrom0To1() {
		long t1 = 0;
		long t2 = 1;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		assertEquals(1, deletes.size());
		assertEquals(1, updates.size());

		JSONObject actual = toJson(delta);
	}

	@Override
	public void getChangesMap() {
		SortedMap<Long,Long> changesMap = mService.getChangesMap();

		long t0 = 0;
		long t1 = 1;
		long t2 = 2;

		assertTrue(changesMap.size() == 3);

		assertEquals(1, (long) changesMap.get(t0));
		assertEquals(2, (long) changesMap.get(t1));
		assertEquals(1, (long) changesMap.get(t2));

		JSONObject actual = toJson(changesMap);
	}
}
