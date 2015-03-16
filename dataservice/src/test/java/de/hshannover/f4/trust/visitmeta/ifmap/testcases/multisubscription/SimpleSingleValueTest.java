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

public class SimpleSingleValueTest extends AbstractMultiSubscriptionTestCase {

	private static final Date FIRST_TIMESTAMP = new Date(3333);

	private static final Date SECOND_TIMESTAMP = new Date(5555);

	private SortedMap<Long, Long> mFirstChangesMap;

	private SortedMap<Long, Long> mSecondChangesMap;

	@Test
	public void shouldReturnTheRightChangeMapSize() {
		executeFirstPoll();
		executePollWithSingleValue();

		super.assertEqualsMapSize(mSecondChangesMap, mFirstChangesMap.size() + 1);
	}

	@Test
	public void shouldReturnTheRightChangeMapChangeValues() {
		executeFirstPoll();
		executePollWithSingleValue();

		super.assertEqualsMapValues(mFirstChangesMap, mSecondChangesMap);
	}

	@Test
	public void shouldReturnTheRightSecondChangeMapChangeValue() {
		executeFirstPoll();
		executePollWithSingleValue();

		super.assertEqualsNewValues(mFirstChangesMap, mSecondChangesMap, 1);
	}

	private void executeFirstPoll() {
		// mock first and second poll results
		PollResult firstPollResult = buildFirstPollResult();

		// run first poll
		startPollTask(firstPollResult);

		// save current ChangesMap after the first poll
		mFirstChangesMap = super.mService.getChangesMap();

	}

	private void executePollWithSingleValue() {
		// mock poll results
		PollResult pollResult = buildPollResultWithSingleValue();

		// run poll
		startPollTask(pollResult);

		// save current ChangesMap after the second poll
		mSecondChangesMap = super.mService.getChangesMap();

	}

	private List<ResultItem> buildSingleValueResultItems() {
		Identifier identifierAR = Identifiers.createAr("ARMultiSubscriptionTest");

		ResultItemMock resultItem1_mock = new ResultItemMock(identifierAR);

		resultItem1_mock.addArDev(SECOND_TIMESTAMP);

		List<ResultItem> resultItems = new ArrayList<ResultItem>();
		resultItems.add(resultItem1_mock.getMock());

		return resultItems;
	}

	private PollResult buildPollResultWithSingleValue() {
		List<ResultItem> resultItems = buildSingleValueResultItems();

		SearchResultMock searchResult1_mock = new SearchResultMock(resultItems, Type.updateResult);
		SearchResultMock searchResult2_mock = new SearchResultMock(resultItems, Type.updateResult);

		PollResultMock secondPollResult_mock = new PollResultMock();
		secondPollResult_mock.addSearchResult(searchResult1_mock.getMock());
		secondPollResult_mock.addSearchResult(searchResult2_mock.getMock());

		return secondPollResult_mock.getMock();
	}

	private List<ResultItem> buildFirstResultItems() {
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

	private PollResult buildFirstPollResult() {
		List<ResultItem> resultItems = buildFirstResultItems();

		SearchResultMock searchResult_mock = new SearchResultMock(resultItems, Type.updateResult);

		PollResultMock pollResult_mock = new PollResultMock();
		pollResult_mock.addSearchResult(searchResult_mock.getMock());

		return pollResult_mock.getMock();
	}

}
