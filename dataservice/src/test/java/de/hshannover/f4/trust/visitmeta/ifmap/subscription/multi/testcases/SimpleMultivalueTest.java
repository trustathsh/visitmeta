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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
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

public class SimpleMultivalueTest extends AbstractMultiSubscriptionTestCase {

	private static final Date FIRST_TIMESTAMP = new Date(3333);

	private SortedMap<Long, Long> mChangesMap;

	private void executePollWithSingleValue() {
		// mock poll results
		PollResult pollResult = buildPollResultWithSingleValue();

		// run poll
		startPollTask(pollResult);

		// save current ChangesMap after the first poll
		mChangesMap = super.mService.getChangesMap();

	}

	private void executePollWithMultiValue() {
		// mock poll results
		PollResult pollResult = buildMultiValuePollResult();

		// run poll
		startPollTask(pollResult);

		// save current ChangesMap after the first poll
		mChangesMap = super.mService.getChangesMap();

	}

	/**
	 * Check the changeMap size is 1.
	 */
	@Test
	public void singleValue_ShouldReturnTheRightChangeMapSize() {
		executePollWithSingleValue();

		assertEquals("Because the ChangesMap size is wrong.", 1, mChangesMap.size());
	}

	/**
	 * The ChangesMap must have only one timestamp and this one have only one changes.
	 */
	@Test
	public void singleValue_ShouldReturnTheRightChangeMapChangeValue() {
		executePollWithSingleValue();

		for (Entry<Long, Long> entry : mChangesMap.entrySet()) {
			assertEquals("Because the changes from the change-map timestamp(" + entry.getKey() + ") must be 1.", 1L,
					entry.getValue().longValue());
			break;
		}
	}

	/**
	 * Check the changeMap size is 1.
	 */
	@Test
	public void multiValue_ShouldReturnTheRightChangeMapSize() {
		executePollWithMultiValue();

		assertEquals("Because the ChangesMap size is wrong.", 1, mChangesMap.size());
	}

	/**
	 * The ChangesMap must have only one timestamp and this one have only one changes.
	 */
	@Test
	public void multiValue_ShouldReturnTheRightChangeMapChangeValue() {
		executePollWithMultiValue();

		for (Entry<Long, Long> entry : mChangesMap.entrySet()) {
			assertEquals("Because the changes from the change-map timestamp(" + entry.getKey() + ") must be 1.", 1L,
					entry.getValue().longValue());
			break;
		}
	}

	private List<ResultItem> buildMultiValueResultItems() {
		Identifier identifierIP = Identifiers.createIp4("192.168.0.1");
		Identifier identifierMAC = Identifiers.createMac("00:11:22:33:44:55");

		ResultItemMock resultItem_mock = new ResultItemMock(identifierIP, identifierMAC);

		resultItem_mock.addIpMac(FIRST_TIMESTAMP);

		List<ResultItem> resultItems = new ArrayList<ResultItem>();
		resultItems.add(resultItem_mock.getMock());

		return resultItems;
	}

	private PollResult buildMultiValuePollResult() {
		List<ResultItem> resultItems = buildMultiValueResultItems();

		SearchResultMock searchResult1_mock = new SearchResultMock(resultItems, Type.updateResult);
		SearchResultMock searchResult2_mock = new SearchResultMock(resultItems, Type.updateResult);

		PollResultMock secondPollResult_mock = new PollResultMock();
		secondPollResult_mock.addSearchResult(searchResult1_mock.getMock());
		secondPollResult_mock.addSearchResult(searchResult2_mock.getMock());

		return secondPollResult_mock.getMock();
	}

	private List<ResultItem> buildSingleValueResultItems() {
		Identifier identifierAR = Identifiers.createAr("ARMultiSubscriptionTest");
		Identifier identifierMAC = Identifiers.createMac("00:11:22:33:44:55");

		ResultItemMock resultItem_mock = new ResultItemMock(identifierAR, identifierMAC);

		resultItem_mock.addArMac(FIRST_TIMESTAMP);

		List<ResultItem> resultItems = new ArrayList<ResultItem>();
		resultItems.add(resultItem_mock.getMock());

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

}
