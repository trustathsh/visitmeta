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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
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
	public abstract void getInitialGraph();

	@Test
	public abstract void getGraphAt();

	@Test
	public abstract void getCurrentGraph();

	@Test
	public abstract void getDelta();

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
}
