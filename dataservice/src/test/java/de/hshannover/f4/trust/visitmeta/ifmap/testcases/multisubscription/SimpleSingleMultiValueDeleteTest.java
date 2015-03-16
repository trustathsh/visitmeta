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
package de.hshannover.f4.trust.visitmeta.ifmap.testcases.multisubscription;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import org.junit.Test;

import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.messages.PollResult;
import de.hshannover.f4.trust.ifmapj.messages.ResultItem;
import de.hshannover.f4.trust.ifmapj.messages.SearchResult.Type;
import de.hshannover.f4.trust.visitmeta.ifmap.AbstractMultiSubscriptionTestCase;
import de.hshannover.f4.trust.visitmeta.ifmap.util.PollResultMock;
import de.hshannover.f4.trust.visitmeta.ifmap.util.ResultItemMock;
import de.hshannover.f4.trust.visitmeta.ifmap.util.SearchResultMock;

public class SimpleSingleMultiValueDeleteTest extends AbstractMultiSubscriptionTestCase {

	private static final Date FIRST_TIMESTAMP = new Date(3333);

	private SortedMap<Long, Long> mFirstChangesMap;

	private SortedMap<Long, Long> mSecondChangesMap;

	// ########### MULTI VALUE ONE IDENTIFIER TESTS ##############

	@Test
	public void multiValueOneIdentifierTest_ShouldReturnTheRightChangeMapSize() {
		executePollMultiValueDeleteTestOneIdentifier();

		super.assertEqualsMapSize(mSecondChangesMap, mFirstChangesMap.size() + 1);
	}

	@Test
	public void multiValueOneIdentifierTest_ShouldReturnTheRightChangeMapChangeValues() {
		executePollMultiValueDeleteTestOneIdentifier();

		super.assertEqualsMapValues(mFirstChangesMap, mSecondChangesMap);
	}

	@Test
	public void multiValueOneIdentifierTest_ShouldReturnTheRightSecondChangeMapChangeValue() {
		executePollMultiValueDeleteTestOneIdentifier();

		super.assertEqualsNewValues(mFirstChangesMap, mSecondChangesMap, 1);
	}

	// ########### MULTI VALUE TWO IDENTIFIER TESTS ##############

	@Test
	public void multiValueTwoIdentifierTest_ShouldReturnTheRightChangeMapSize() {
		executePollMultiValueDeleteTestTwoIdentifier();

		super.assertEqualsMapSize(mSecondChangesMap, mFirstChangesMap.size() + 1);
	}

	@Test
	public void multiValueTwoIdentifierTest_ShouldReturnTheRightChangeMapChangeValues() {
		executePollMultiValueDeleteTestTwoIdentifier();

		super.assertEqualsMapValues(mFirstChangesMap, mSecondChangesMap);
	}

	@Test
	public void multiValueTwoIdentifierTest_ShouldReturnTheRightSecondChangeMapChangeValue() {
		executePollMultiValueDeleteTestTwoIdentifier();

		super.assertEqualsNewValues(mFirstChangesMap, mSecondChangesMap, 1);
	}

	// ########### SINGLE VALUE ONE IDENTIFIER TESTS ##############

	@Test
	public void singleValueOneIdentifierTest_ShouldReturnTheRightChangeMapSize() {
		executePollSingleValueDeleteTestOneIdentifier();

		super.assertEqualsMapSize(mSecondChangesMap, mFirstChangesMap.size() + 1);
	}

	@Test
	public void singleValueOneIdentifierTest_ShouldReturnTheRightChangeMapChangeValues() {
		executePollSingleValueDeleteTestOneIdentifier();

		super.assertEqualsMapValues(mFirstChangesMap, mSecondChangesMap);
	}

	@Test
	public void singleValueOneIdentifierTest_ShouldReturnTheRightSecondChangeMapChangeValue() {
		executePollSingleValueDeleteTestOneIdentifier();

		super.assertEqualsNewValues(mFirstChangesMap, mSecondChangesMap, 1);
	}

	// ########### SINGLE VALUE TWO IDENTIFIER TESTS ##############

	@Test
	public void singleValueTwoIdentifierTest_ShouldReturnTheRightChangeMapSize() {
		executePollSingleValueDeleteTestTwoIdentifier();

		super.assertEqualsMapSize(mSecondChangesMap, mFirstChangesMap.size() + 1);
	}

	@Test
	public void singleValueTwoIdentifierTest_ShouldReturnTheRightChangeMapChangeValues() {
		executePollSingleValueDeleteTestTwoIdentifier();

		super.assertEqualsMapValues(mFirstChangesMap, mSecondChangesMap);
	}

	@Test
	public void singleValueTwoIdentifierTest_ShouldReturnTheRightSecondChangeMapChangeValue() {
		executePollSingleValueDeleteTestTwoIdentifier();

		super.assertEqualsNewValues(mFirstChangesMap, mSecondChangesMap, 1);
	}

	private void executePollMultiValueDeleteTestOneIdentifier() {
		PollResult pollResult = buildMultiValueOneIdentifierPollResult();

		startPollTask(pollResult);

		mFirstChangesMap = super.mService.getChangesMap();

		PollResult deletePollResult = buildMultiValueOneIdentifierDeletePollResult();

		startPollTask(deletePollResult);

		mSecondChangesMap = super.mService.getChangesMap();
	}

	private void executePollMultiValueDeleteTestTwoIdentifier() {
		PollResult pollResult = buildMultiValueTwoIdentifierPollResult();

		startPollTask(pollResult);

		mFirstChangesMap = super.mService.getChangesMap();

		PollResult deletePollResult = buildMultiValueTwoIdentifierDeletePollResult();

		startPollTask(deletePollResult);

		mSecondChangesMap = super.mService.getChangesMap();
	}

	private void executePollSingleValueDeleteTestOneIdentifier() {
		PollResult pollResult = buildSingleValueOneIdentifierPollResult();

		startPollTask(pollResult);

		mFirstChangesMap = super.mService.getChangesMap();

		PollResult deletePollResult = buildSingleValueOneIdentifierDeletePollResult();

		startPollTask(deletePollResult);

		mSecondChangesMap = super.mService.getChangesMap();
	}

	private void executePollSingleValueDeleteTestTwoIdentifier() {
		PollResult pollResult = buildSingleValueTwoIdentifierPollResult();

		startPollTask(pollResult);

		mFirstChangesMap = super.mService.getChangesMap();

		PollResult deletePollResult = buildSingleValueTwoIdentifierDeletePollResult();

		startPollTask(deletePollResult);

		mSecondChangesMap = super.mService.getChangesMap();
	}

	private ResultItem buildMultiValueOneIdentifierResultItem() {
		Identifier identifierAR = Identifiers.createAr("ARMultiSubscriptionTest");
		ResultItemMock resultItem1_mock = new ResultItemMock(identifierAR);
		resultItem1_mock.addCapability("CAP1", FIRST_TIMESTAMP);
		return resultItem1_mock.getMock();
	}

	private ResultItem buildMultiValueTwoIdentifierResultItem() {
		Identifier identifierAR = Identifiers.createAr("ARMultiSubscriptionTest");
		Identifier identifierDEV = Identifiers.createDev("DEV45");
		ResultItemMock resultItem1_mock = new ResultItemMock(identifierAR, identifierDEV);
		resultItem1_mock.addDevAttr("DEV45Attribute", FIRST_TIMESTAMP);
		return resultItem1_mock.getMock();
	}

	private ResultItem buildSingleValueTwoIdentifierResultItem() {
		Identifier identifierAR = Identifiers.createAr("ARMultiSubscriptionTest");
		Identifier identifierMAC = Identifiers.createMac("22:33:44:55:66:77");
		ResultItemMock resultItem1_mock = new ResultItemMock(identifierAR, identifierMAC);
		resultItem1_mock.addArMac(FIRST_TIMESTAMP);
		return resultItem1_mock.getMock();
	}

	private ResultItem buildSingleValueOneIdentifierResultItem() {
		Identifier identifierAR = Identifiers.createAr("ARMultiSubscriptionTest");
		ResultItemMock resultItem1_mock = new ResultItemMock(identifierAR);
		resultItem1_mock.addArDev(FIRST_TIMESTAMP);
		return resultItem1_mock.getMock();
	}

	private PollResult buildMultiValueOneIdentifierDeletePollResult() {
		ResultItem sutResultItem = buildMultiValueOneIdentifierResultItem();
		return buildDeletePollResult(sutResultItem);
	}

	private PollResult buildMultiValueTwoIdentifierDeletePollResult() {
		ResultItem sutResultItem = buildMultiValueTwoIdentifierResultItem();
		return buildDeletePollResult(sutResultItem);
	}

	private PollResult buildSingleValueTwoIdentifierDeletePollResult() {
		ResultItem sutResultItem = buildSingleValueTwoIdentifierResultItem();
		return buildDeletePollResult(sutResultItem);
	}

	private PollResult buildSingleValueOneIdentifierDeletePollResult() {
		ResultItem sutResultItem = buildSingleValueOneIdentifierResultItem();
		return buildDeletePollResult(sutResultItem);
	}

	private PollResult buildDeletePollResult(ResultItem resultItem) {
		SearchResultMock searchResult_mock = new SearchResultMock(Type.deleteResult);
		searchResult_mock.addResultItem(resultItem);

		PollResultMock pollResult_mock = new PollResultMock();
		pollResult_mock.addSearchResult(searchResult_mock.getMock());

		return pollResult_mock.getMock();
	}

	private PollResult buildMultiValueOneIdentifierPollResult() {
		ResultItem sutResultItem = buildMultiValueOneIdentifierResultItem();
		return buildUpdatePollResultWithStartingGraph(sutResultItem);
	}

	private PollResult buildMultiValueTwoIdentifierPollResult() {
		ResultItem sutResultItem = buildMultiValueTwoIdentifierResultItem();
		return buildUpdatePollResultWithStartingGraph(sutResultItem);
	}

	private PollResult buildSingleValueTwoIdentifierPollResult() {
		ResultItem sutResultItem = buildSingleValueTwoIdentifierResultItem();
		return buildUpdatePollResultWithStartingGraph(sutResultItem);
	}

	private PollResult buildSingleValueOneIdentifierPollResult() {
		ResultItem sutResultItem = buildSingleValueOneIdentifierResultItem();
		return buildUpdatePollResultWithStartingGraph(sutResultItem);
	}

	private PollResult buildUpdatePollResultWithStartingGraph(ResultItem resultItem) {
		List<ResultItem> resultItems = buildStartingGraphResultItems();
		resultItems.add(resultItem);

		SearchResultMock searchResult_mock = new SearchResultMock(resultItems, Type.updateResult);

		PollResultMock pollResult_mock = new PollResultMock();
		pollResult_mock.addSearchResult(searchResult_mock.getMock());

		return pollResult_mock.getMock();
	}

	private List<ResultItem> buildStartingGraphResultItems() {
		Identifier identifierAR = Identifiers.createAr("ARMultiSubscriptionTest");
		Identifier identifierMAC1 = Identifiers.createMac("00:11:22:33:44:55");
		Identifier identifierMAC2 = Identifiers.createMac("11:22:33:44:55:66");

		ResultItemMock resultItem1_mock = new ResultItemMock(identifierAR, identifierMAC1);
		ResultItemMock resultItem2_mock = new ResultItemMock(identifierAR, identifierMAC2);

		resultItem1_mock.addArMac(FIRST_TIMESTAMP);
		resultItem2_mock.addArMac(FIRST_TIMESTAMP);

		List<ResultItem> resultItems = new ArrayList<ResultItem>();
		resultItems.add(resultItem1_mock.getMock());
		resultItems.add(resultItem2_mock.getMock());

		return resultItems;
	}
}
