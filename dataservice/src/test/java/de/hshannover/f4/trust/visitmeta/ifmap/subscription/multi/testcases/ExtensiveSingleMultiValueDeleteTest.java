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
package de.hshannover.f4.trust.visitmeta.ifmap.subscription.multi.testcases;

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

public class ExtensiveSingleMultiValueDeleteTest extends AbstractMultiSubscriptionTestCase {

	private static final Date FIRST_TIMESTAMP = new Date(3333);

	private SortedMap<Long, Long> mFirstChangesMap;

	private SortedMap<Long, Long> mSecondChangesMap;

	@Test
	public void shouldReturnTheRightChangeMapSize() {
		executeExtensiveSingleMultiValueDeleteTest();

		super.assertEqualsMapSize(mSecondChangesMap, mFirstChangesMap.size() + 1);
	}

	@Test
	public void shouldReturnTheRightChangeMapChangeValues() {
		executeExtensiveSingleMultiValueDeleteTest();

		super.assertEqualsMapValues(mFirstChangesMap, mSecondChangesMap);
	}

	@Test
	public void shouldReturnTheRightSecondChangeMapChangeValue() {
		executeExtensiveSingleMultiValueDeleteTest();

		super.assertEqualsNewValues(mFirstChangesMap, mSecondChangesMap, 1);
	}

	private void executeExtensiveSingleMultiValueDeleteTest() {
		PollResult pollResult = buildStartingGraphPollResult();

		startPollTask(pollResult);

		mFirstChangesMap = super.mService.getChangesMap();

		PollResult deletePollResult = buildDeletePollResult();

		startPollTask(deletePollResult);

		mSecondChangesMap = super.mService.getChangesMap();
	}

	private ResultItem buildSub1DeleteResultItem() {
		Identifier identifierAR = Identifiers.createAr("ARMultiSubscriptionTest");
		Identifier identifierMAC = Identifiers.createMac("11:22:33:44:55:66");
		ResultItemMock resultItem1_mock = new ResultItemMock(identifierAR, identifierMAC);
		resultItem1_mock.addArMac(FIRST_TIMESTAMP);
		return resultItem1_mock.getMock();
	}

	private PollResult buildDeletePollResult() {
		ResultItem resultItemSub1 = buildSub1DeleteResultItem();
		List<ResultItem> resultItemSub2 = buildStartingGraphResultItems();

		SearchResultMock searchResult1_mock = new SearchResultMock(resultItemSub2, Type.deleteResult);
		SearchResultMock searchResult2_mock = new SearchResultMock(Type.deleteResult);
		searchResult2_mock.addResultItem(resultItemSub1);

		PollResultMock pollResult_mock = new PollResultMock();
		pollResult_mock.addSearchResult(searchResult1_mock.getMock());
		pollResult_mock.addSearchResult(searchResult2_mock.getMock());

		return pollResult_mock.getMock();
	}

	private PollResult buildStartingGraphPollResult() {
		List<ResultItem> resultItems = buildStartingGraphResultItems();

		SearchResultMock searchResult_mock = new SearchResultMock(resultItems, Type.updateResult);

		PollResultMock pollResult_mock = new PollResultMock();
		pollResult_mock.addSearchResult(searchResult_mock.getMock());

		return pollResult_mock.getMock();
	}

	private List<ResultItem> buildStartingGraphResultItems() {
		Identifier identifierAR = Identifiers.createAr("ARMultiSubscriptionTest");
		Identifier identifierMAC1 = Identifiers.createMac("00:11:22:33:44:55");
		Identifier identifierMAC2 = Identifiers.createMac("11:22:33:44:55:66");
		Identifier identifierMAC3 = Identifiers.createMac("22:33:44:55:66:77");
		Identifier identifierDEV = Identifiers.createDev("DEV45");

		ResultItemMock resultItem1_mock = new ResultItemMock(identifierAR, identifierMAC1);
		ResultItemMock resultItem2_mock = new ResultItemMock(identifierAR, identifierMAC2);
		ResultItemMock resultItem3_mock = new ResultItemMock(identifierAR, identifierMAC3);
		ResultItemMock resultItem4_mock = new ResultItemMock(identifierAR, identifierDEV);
		ResultItemMock resultItem5_mock = new ResultItemMock(identifierAR);

		resultItem1_mock.addArMac(FIRST_TIMESTAMP);
		resultItem2_mock.addArMac(FIRST_TIMESTAMP);
		resultItem3_mock.addArMac(FIRST_TIMESTAMP);
		resultItem4_mock.addDevAttr("DEV45", FIRST_TIMESTAMP);
		resultItem5_mock.addArDev(FIRST_TIMESTAMP);
		resultItem5_mock.addCapability("CAP1", FIRST_TIMESTAMP);

		List<ResultItem> resultItems = new ArrayList<ResultItem>();
		resultItems.add(resultItem1_mock.getMock());
		resultItems.add(resultItem2_mock.getMock());
		resultItems.add(resultItem3_mock.getMock());
		resultItems.add(resultItem4_mock.getMock());
		resultItems.add(resultItem5_mock.getMock());

		return resultItems;
	}
}
