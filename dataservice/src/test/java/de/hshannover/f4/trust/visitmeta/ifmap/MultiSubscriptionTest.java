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
 * This file is part of visitmeta-dataservice, version 0.3.0,
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
package de.hshannover.f4.trust.visitmeta.ifmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;

import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.messages.PollResult;
import de.hshannover.f4.trust.ifmapj.messages.ResultItem;
import de.hshannover.f4.trust.ifmapj.messages.SearchResult.Type;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InMemoryIdentifierFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InMemoryMetadataFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InternalIdentifierFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InternalMetadataFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.Neo4JTestDatabaseFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.DummyGraphCache;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.SimpleGraphService;
import de.hshannover.f4.trust.visitmeta.dataservice.rest.JsonMarshaller;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.ifmap.util.PollResultMock;
import de.hshannover.f4.trust.visitmeta.ifmap.util.ResultItemMock;
import de.hshannover.f4.trust.visitmeta.ifmap.util.SearchResultMock;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;
import de.hshannover.f4.trust.visitmeta.interfaces.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.persistence.Executor;
import de.hshannover.f4.trust.visitmeta.persistence.Writer;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JConnection;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JExecutor;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JReader;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JRepository;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JTimestampManager;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JWriter;

public class MultiSubscriptionTest {

	private static final Logger mLog = Logger.getLogger(MultiSubscriptionTest.class);

	public static final String TIMESTAMP = "ifmap-timestamp";

	public static final String TIMESTAMP_FRACTION = "ifmap-timestamp-fraction";

	public static final String PUBLISHERID = "ifmap-publisher-id";

	private static final Date FIRST_TIMESTAMP = new Date(3333);

	private static final Date SECOND_TIMESTAMP = new Date(5555);

	private Writer mWriter;

	private Connection mConnection;

	private Executor mExecutor;

	private JsonMarshaller mJsonMarshaller;

	private Neo4JConnection mDbConnection;

	private Neo4JReader mNeo4jDb;

	private GraphDatabaseService mGraphDb;

	private Neo4JTimestampManager mTimestampManager;

	private GraphService mService;

	private Neo4JRepository mNeo4jRepo;

	private IfmapJHelper mIfmapJHelper;

	private InternalIdentifierFactory mIdentifierFactory;

	private InternalMetadataFactory mMetadataFactory;

	private SortedMap<Long, Long> mFirstChangesMap;

	private SortedMap<Long, Long> mSecondChangesMap;

	@Before
	public void setUp() throws Exception {
		mGraphDb = Neo4JTestDatabaseFactory.createGraphDB();
		assumeNotNull(mGraphDb);

		mDbConnection = mock(Neo4JConnection.class);
		when(mDbConnection.getConnection()).thenReturn(mGraphDb);

		mTimestampManager = new Neo4JTimestampManager(mDbConnection);
		when(mDbConnection.getTimestampManager()).thenReturn(mTimestampManager);

		mNeo4jRepo = new Neo4JRepository(mDbConnection, MessageDigest.getInstance("MD5"));
		mNeo4jDb = new Neo4JReader(mNeo4jRepo, mDbConnection);
		mExecutor = new Neo4JExecutor(mDbConnection);
		mWriter = new Neo4JWriter(mNeo4jRepo, mDbConnection);

		mService = new SimpleGraphService(mNeo4jDb, mExecutor, new DummyGraphCache());
		mJsonMarshaller = new JsonMarshaller();

		mIdentifierFactory = new InMemoryIdentifierFactory();
		mMetadataFactory = new InMemoryMetadataFactory();
		mIfmapJHelper = new IfmapJHelper(mIdentifierFactory);

		mConnection = mock(Connection.class);

		executeMultiSubscriptionTestCase();
	}

	/**
	 * Makes two polls with two different PollResult. The PollResult are the same as if when we makes two subscriptions.
	 */
	private void executeMultiSubscriptionTestCase() {
		// mock first and second poll results
		PollResult firstPollResult = buildFirstPollResult();
		PollResult secondPollResult = buildSecondPollResult();

		// run first poll
		startPollTask(firstPollResult);

		// save current ChangesMap after the first poll
		mFirstChangesMap = mService.getChangesMap();

		// printCurrentGraph();
		// printNeo4jDB();

		// run second poll
		startPollTask(secondPollResult);

		// printCurrentGraph();
		// printNeo4jDB();

		// save current ChangesMap after the second poll
		mSecondChangesMap = mService.getChangesMap();

		// printChangesMaps();
	}

	@After
	public void tearDown() {
		if (mGraphDb != null) {
			mGraphDb.shutdown();
		}
	}

	/**
	 * Check the changeMap size is right.
	 * The second ChangesMap size may be increased only by 1 from the first ChangesMap.
	 * The second ChangesMap must have only one new timestamp
	 */
	@Test
	public void shouldReturnTheRightChangeMapSize() {
		if (mSecondChangesMap.size() - 1 != mFirstChangesMap.size()) {
			fail("Becase the second ChangesMap size is wrong. May be increased by one. (FirstMap-Size: " + mFirstChangesMap.size() + " || SecondMap-Size: "
					+ mSecondChangesMap.size());
		}
	}

	/**
	 * Check the changeMap keys and values.
	 * All keys and values from the first ChangesMap may be contains in the second ChangesMap.
	 */
	@Test
	public void shouldReturnTheRightChangeMapValues() {
		for (Entry<Long, Long> entry : mFirstChangesMap.entrySet()) {
			if (mSecondChangesMap.get(entry.getKey()) != entry.getValue()) {
				// all keys from the first must contains in the second and the values must the same, if not -> FAIL
				fail("Becase the changes from change-map timestamp " + entry.getKey() + " are not the same. (FirstMap-Value: " + entry.getValue() + " || SecondMap-Value: "
						+ mSecondChangesMap.get(entry.getKey()));
			}
		}
	}

	/**
	 * Check the new timestamp for the second changeMap.
	 * The second ChangesMap must have only one new timestamp and this one have only one changes.
	 */
	@Test
	public void shouldReturnTheRightSecondChangeMap() {
		for (Entry<Long, Long> entry : mSecondChangesMap.entrySet()) {
			if (!mFirstChangesMap.containsKey(entry.getKey())) {
				// only the one new key in the secondChangesMap
				assertEquals("Becase the changes from the change-map timestamp(" + entry.getKey() + ") must be 1.", 1L, entry.getValue().longValue());
				break;
			}
		}
	}

	private void startPollTask(PollResult ifmapPollResult) {
		mLog.debug("PollTask started....");

		try {
			when(mConnection.poll()).thenReturn(ifmapPollResult);
		} catch (ConnectionException e) {
			mLog.error(e.getMessage(), e);
		}

		PollTask task = new PollTask(mConnection, mMetadataFactory, mIfmapJHelper);

		try {

			de.hshannover.f4.trust.visitmeta.ifmap.PollResult pollResult = task.call();
			mWriter.submitPollResult(pollResult);

		} catch (ConnectionException e) {
			mLog.error(e.toString(), e);
		}

		mLog.debug("... PollTask finished");
	}

	private List<ResultItem> buildFirstResultItems() {
		Identifier identifierAR = Identifiers.createAr("ARMultiSubscriptionTest");
		Identifier identifierMAC1 = Identifiers.createMac("00:11:22:33:44:55");

		ResultItemMock resultItem1_mock = new ResultItemMock(identifierAR);
		ResultItemMock resultItem2_mock = new ResultItemMock(identifierAR, identifierMAC1);
		ResultItemMock resultItem3_mock = new ResultItemMock(identifierAR);

		resultItem1_mock.addCapability("CAP1", "ADMINISTRATIVE_DOMAIN_1", FIRST_TIMESTAMP);
		resultItem2_mock.addArMac(FIRST_TIMESTAMP);
		resultItem3_mock.addCapability("CAP2", "ADMINISTRATIVE_DOMAIN_2", FIRST_TIMESTAMP);
		resultItem3_mock.addCapability("CAP3", "ADMINISTRATIVE_DOMAIN_3", FIRST_TIMESTAMP);

		List<ResultItem> resultItems = new ArrayList<ResultItem>();
		resultItems.add(resultItem1_mock.getMock());
		resultItems.add(resultItem2_mock.getMock());
		resultItems.add(resultItem3_mock.getMock());

		return resultItems;
	}

	private PollResult buildFirstPollResult() {
		List<ResultItem> resultItems = buildFirstResultItems();

		SearchResultMock searchResult_mock = new SearchResultMock(resultItems, Type.updateResult);

		PollResultMock pollResult_mock = new PollResultMock();
		pollResult_mock.addSearchResult(searchResult_mock.getMock());

		return pollResult_mock.getMock();
	}

	private List<ResultItem> buildSecondResultItems() {
		Identifier identifierAR = Identifiers.createAr("ARMultiSubscriptionTest");
		Identifier identifierMAC = Identifiers.createMac("11:22:33:44:55:66");

		ResultItemMock resultItem_mock = new ResultItemMock(identifierAR, identifierMAC);

		resultItem_mock.addArMac(SECOND_TIMESTAMP);

		List<ResultItem> resultItems = new ArrayList<ResultItem>();
		resultItems.add(resultItem_mock.getMock());

		return resultItems;
	}

	private PollResult buildSecondPollResult() {
		List<ResultItem> firstResultItems = buildFirstResultItems();
		List<ResultItem> secondResultItems = buildSecondResultItems();

		firstResultItems.addAll(secondResultItems);

		SearchResultMock searchResult1_mock = new SearchResultMock(firstResultItems, Type.updateResult);
		SearchResultMock searchResult2_mock = new SearchResultMock(secondResultItems, Type.updateResult);

		PollResultMock secondPollResult_mock = new PollResultMock();
		secondPollResult_mock.addSearchResult(searchResult1_mock.getMock());
		secondPollResult_mock.addSearchResult(searchResult2_mock.getMock());

		return secondPollResult_mock.getMock();
	}

	private void printNeo4jDB() {
		Neo4JTestDatabaseFactory.printDB(mGraphDb);
	}

	private void printCurrentGraph() {
		List<IdentifierGraph> currentGraph = mService.getCurrentGraph();
		JSONArray currentGraphJSON = mJsonMarshaller.toJson(currentGraph);

		System.out.println(currentGraphJSON);
	}

	private void printChangesMaps() {
		System.out.println("First-Changes-Map: " + mFirstChangesMap);
		System.out.println("Second-Changes-Map: " + mSecondChangesMap);
	}

}
