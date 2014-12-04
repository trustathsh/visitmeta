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

import java.util.List;
import java.util.SortedMap;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

public class EmptyGraphTestCase extends AbstractTestCase {

	@Override
	public String getTestcaseFilename() {
		return null;
	}

	@Override
	public void getInitialGraph() throws JSONException {
		List<IdentifierGraph> initialGraph = mService.getInitialGraph();

		assertTrue(initialGraph.isEmpty());

		JSONArray actual = toJson(initialGraph);
		JSONArray expected = new JSONArray();

		assertTrue(jsonsEqual(actual, expected));
	}

	@Override
	public void getGraphAt() throws JSONException {
		long timestamp = 0;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		assertTrue(graphAt.isEmpty());

		JSONArray actual = toJson(graphAt);
		JSONArray expected = new JSONArray();

		assertTrue(jsonsEqual(actual, expected));
	}

	@Override
	public void getCurrentGraph() throws JSONException {
		List<IdentifierGraph> currentGraph = mService.getCurrentGraph();

		assertTrue(currentGraph.isEmpty());

		JSONArray actual = toJson(currentGraph);
		JSONArray expected = new JSONArray();

		assertTrue(jsonsEqual(actual, expected));
	}

	@Override
	public void getDelta() {
		long t1 = 0;
		long t2 = 0;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		assertTrue(deletes.isEmpty());
		assertTrue(updates.isEmpty());

		JSONObject actual = toJson(delta);
		String expected = "{\"updates\":[],\"deletes\":[]}";

		assertEquals(expected, actual.toString());
	}

	@Override
	public void getChangesMap() throws JSONException{
		SortedMap<Long,Long> changesMap = mService.getChangesMap();

		assertTrue(changesMap.isEmpty());

		JSONObject actual = toJson(changesMap);
		JSONObject expected = new JSONObject();

		assertTrue(jsonsEqual(actual, expected));
	}
}
