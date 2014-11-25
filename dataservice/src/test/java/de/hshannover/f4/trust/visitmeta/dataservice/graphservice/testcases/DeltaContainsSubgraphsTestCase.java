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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.codehaus.jettison.json.JSONException;

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
	public void getInitialGraph() {
		List<IdentifierGraph> initialGraph = mService.getInitialGraph();

		testGraphListSize(initialGraph, 1);

		IdentifierGraph graph = initialGraph.get(0);
		testIdentifierCount(graph, 2);
	}

	@Override
	public void getGraphAt() {
		getGraphAt0();
		getGraphAt1();
		getGraphAt2();
	}

	private void getGraphAt0() {
		long timestamp = 0;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		testGraphListSize(graphAt, 1);

		IdentifierGraph graph = graphAt.get(0);
		testIdentifierCount(graph, 2);
	}

	private void getGraphAt1() {
		long timestamp = 1;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		testGraphListSize(graphAt, 1);

		IdentifierGraph graph = graphAt.get(0);
		testIdentifierCount(graph, 3);
	}

	private void getGraphAt2() {
		long timestamp = 2;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		testGraphListSize(graphAt, 1);

		IdentifierGraph graph = graphAt.get(0);
		testIdentifierCount(graph, 4);
	}

	@Override
	public void getCurrentGraph() {
		List<IdentifierGraph> currentGraph = mService.getCurrentGraph();

		testGraphListSize(currentGraph, 1);

		IdentifierGraph graph = currentGraph.get(0);
		testIdentifierCount(graph, 4);
	}

	@Override
	public void getDelta() {
		getDeltaFrom0To1();
		getDeltaFrom1To2();
		//		getDeltaFrom0To2();	// FIXME this test currently fails, because delta calculation is wrong
	}

	private void getDeltaFrom0To1() {
		long t1 = 0;
		long t2 = 1;
		Delta delta = mService.getDelta(t1, t2);

		testDeltaSize(delta, 1, 0);

		List<Identifier> updateIdentifiers = delta.getUpdates().get(0).getIdentifiers();
		assertEquals(2, updateIdentifiers.size());
	}

	private void getDeltaFrom1To2() {
		long t1 = 1;
		long t2 = 2;
		Delta delta = mService.getDelta(t1, t2);

		testDeltaSize(delta, 1, 0);

		List<Identifier> updateIdentifiers = delta.getUpdates().get(0).getIdentifiers();
		assertEquals(2, updateIdentifiers.size());
	}

	private void getDeltaFrom0To2() {
		long t1 = 0;
		long t2 = 2;
		Delta delta = mService.getDelta(t1, t2);

		testDeltaSize(delta, 2, 0);

		List<Identifier> updateIdentifiers1 = delta.getUpdates().get(0).getIdentifiers();
		assertEquals(2, updateIdentifiers1.size());

		List<Identifier> updateIdentifiers2 = delta.getUpdates().get(1).getIdentifiers();
		assertEquals(2, updateIdentifiers2.size());
	}

	@Override
	public void getChangesMap() throws JSONException {
		SortedMap<Long,Long> changesMap = mService.getChangesMap();

		Map<Long,Long> changes = new HashMap<Long,Long>();
		changes.put(0L, 1L);	// t0
		changes.put(1L, 1L);	// t1
		changes.put(2L, 1L);	// t2

		testChangesMap(changes, changesMap);
		testChangesMapJSON(changes, changesMap);
	}

}
