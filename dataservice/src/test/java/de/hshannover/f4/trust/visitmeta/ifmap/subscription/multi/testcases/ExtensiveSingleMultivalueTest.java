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
import static org.junit.Assert.fail;

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

public class ExtensiveSingleMultivalueTest extends AbstractMultiSubscriptionTestCase {

	private static final Date FIRST_TIMESTAMP = new Date(3333);

	private static final Date SECOND_TIMESTAMP = new Date(5555);

	private static final Date THIRD_TIMESTAMP = new Date(8888);

	private SortedMap<Long, Long> mFirstChangesMap;

	private SortedMap<Long, Long> mSecondChangesMap;

	private SortedMap<Long, Long> mThirdChangesMap;

	/**
	 * Makes two polls with different PollResult. The PollResult are the same as if when we makes two subscriptions.
	 */
	private void executeFirstTwoPolls() {
		// mock first and second poll results
		PollResult firstPollResult = buildFirstPollResult();
		PollResult secondPollResult = buildSecondPollResult();

		// run first poll
		startPollTask(firstPollResult);

		// save current ChangesMap after the first poll
		mFirstChangesMap = super.mService.getChangesMap();

		// run second poll
		startPollTask(secondPollResult);

		// save current ChangesMap after the second poll
		mSecondChangesMap = super.mService.getChangesMap();

	}

	private void executeThirdPollWithSingleValue() {
		PollResult thirdPollResult = buildThirdSingleValuePollResult();

		startPollTask(thirdPollResult);

		// save current ChangesMap after the third poll
		mThirdChangesMap = super.mService.getChangesMap();

	}

	private void executeThirdPollWithMultiValue() {
		PollResult thirdPollResult = buildThirdMultiValuePollResult();

		startPollTask(thirdPollResult);

		// save current ChangesMap after the third poll
		mThirdChangesMap = super.mService.getChangesMap();

	}

	/**
	 * Check the changeMap size is right.
	 * The first ChangesMap size may be increased only by 1 from the first ChangesMap.
	 * The second ChangesMap must have only one new timestamp.
	 */
	@Test
	public void twoPolls_ShouldReturnTheRightChangeMapSize() {
		executeFirstTwoPolls();

		if (mSecondChangesMap.size() - 1 != mFirstChangesMap.size()) {
			fail("Because the second ChangesMap size is wrong. May be increased by one. (FirstMap-Size: "
					+ mFirstChangesMap.size() + " || SecondMap-Size: " + mSecondChangesMap.size());
		}
	}

	/**
	 * Check the changeMap keys and values.
	 * All keys and values from the first ChangesMap may be contains in the second ChangesMap.
	 */
	@Test
	public void twoPolls_ShouldReturnTheRightChangeMapChangeValues() {
		executeFirstTwoPolls();

		for (Entry<Long, Long> entry : mFirstChangesMap.entrySet()) {
			if (mSecondChangesMap.get(entry.getKey()) != entry.getValue()) {
				// all keys from the first ChangesMap must contains in the second and the values must the same, if
				// not -> FAIL
				fail("Because the changes from change-map timestamp " + entry.getKey()
						+ " are not the same. (FirstMap-Value: " + entry.getValue() + " || SecondMap-Value: "
						+ mSecondChangesMap.get(entry.getKey()));
			}
		}
	}

	/**
	 * Check the new timestamp for the second changeMap.
	 * The second ChangesMap must have only one new timestamp and this one have only one changes.
	 */
	@Test
	public void twoPolls_ShouldReturnTheRightSecondChangeMapChangeValue() {
		executeFirstTwoPolls();

		for (Entry<Long, Long> entry : mSecondChangesMap.entrySet()) {
			if (!mFirstChangesMap.containsKey(entry.getKey())) {
				// only the one new key in the second ChangesMap
				assertEquals("Because the changes from the change-map timestamp(" + entry.getKey() + ") must be 1.",
						1L, entry.getValue().longValue());
				break;
			}
		}
	}

	/**
	 * Check the changeMap size is right.
	 * The third ChangesMap size may be increased only by 1 from the second ChangesMap.
	 * The third ChangesMap must have only one new timestamp.
	 */
	@Test
	public void thirdPollSingleValue_ShouldReturnTheRightChangeMapSize() {
		executeFirstTwoPolls();
		executeThirdPollWithSingleValue();

		if (mThirdChangesMap.size() - 1 != mSecondChangesMap.size()) {
			fail("Because the third ChangesMap size is wrong. May be increased by one. (SecondMap-Size: "
					+ mSecondChangesMap.size() + " || ThirdMap-Size: " + mThirdChangesMap.size());
		}
	}

	/**
	 * Check the changeMap keys and values.
	 * All keys and values from the second ChangesMap may be contains in the third ChangesMap.
	 */
	@Test
	public void thirdPollSingleValue_ShouldReturnTheRightChangeMapChangeValues() {
		executeFirstTwoPolls();
		executeThirdPollWithSingleValue();

		for (Entry<Long, Long> entry : mSecondChangesMap.entrySet()) {
			if (mThirdChangesMap.get(entry.getKey()) != entry.getValue()) {
				// all keys from the second ChangesMap must contains in the third and the values must the same, if
				// not -> FAIL
				fail("Because the changes from change-map timestamp " + entry.getKey()
						+ " are not the same. (SecondMap-Value: " + entry.getValue() + " || ThirdMap-Value: "
						+ mThirdChangesMap.get(entry.getKey()));
			}
		}
	}

	/**
	 * Check the new timestamp for the third changeMap.
	 * The third ChangesMap must have only one new timestamp and this one have only one changes.
	 * This third poll added a new single value Metadata
	 */
	@Test
	public void thirdPollSingleValue_ShouldReturnTheRightThirdChangeMapChangeValue() {
		executeFirstTwoPolls();
		executeThirdPollWithSingleValue();

		for (Entry<Long, Long> entry : mThirdChangesMap.entrySet()) {
			if (!mSecondChangesMap.containsKey(entry.getKey())) {
				// only the one new key in the third ChangesMap
				assertEquals("Because the changes from the change-map timestamp(" + entry.getKey() + ") must be 1.",
						1L, entry.getValue().longValue());
				break;
			}
		}
	}

	/**
	 * Check the changeMap size is right.
	 * The third ChangesMap size may be increased only by 1 from the second ChangesMap.
	 * The third ChangesMap must have only one new timestamp.
	 */
	@Test
	public void thirdPollMultiValue_ShouldReturnTheRightChangeMapSize() {
		executeFirstTwoPolls();
		executeThirdPollWithMultiValue();

		if (mThirdChangesMap.size() - 1 != mSecondChangesMap.size()) {
			fail("Because the third ChangesMap size is wrong. May be increased by one. (SecondMap-Size: "
					+ mSecondChangesMap.size() + " || ThirdMap-Size: " + mThirdChangesMap.size());
		}
	}

	/**
	 * Check the changeMap keys and values.
	 * All keys and values from the second ChangesMap may be contains in the third ChangesMap.
	 */
	@Test
	public void thirdPollMultiValue_ShouldReturnTheRightChangeMapChangeValues() {
		executeFirstTwoPolls();
		executeThirdPollWithMultiValue();

		for (Entry<Long, Long> entry : mSecondChangesMap.entrySet()) {
			if (mThirdChangesMap.get(entry.getKey()) != entry.getValue()) {
				// all keys from the second ChangesMap must contains in the third and the values must the same, if
				// not -> FAIL
				fail("Because the changes from change-map timestamp " + entry.getKey()
						+ " are not the same. (SecondMap-Value: " + entry.getValue() + " || ThirdMap-Value: "
						+ mThirdChangesMap.get(entry.getKey()));
			}
		}
	}

	/**
	 * Check the new timestamp for the third changeMap.
	 * The third ChangesMap must have only one new timestamp and this one have only one changes.
	 * This third poll added a new multi value Metadata
	 */
	@Test
	public void thirdPollMultiValue_ShouldReturnTheRightThirdChangeMapChangeValue() {
		executeFirstTwoPolls();
		executeThirdPollWithMultiValue();

		for (Entry<Long, Long> entry : mThirdChangesMap.entrySet()) {
			if (!mSecondChangesMap.containsKey(entry.getKey())) {
				// only the one new key in the third ChangesMap
				assertEquals("Because the changes from the change-map timestamp(" + entry.getKey() + ") must be 1.",
						1L, entry.getValue().longValue());
				break;
			}
		}
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

	private List<ResultItem> buildThirdMultiValueResultItems() {
		Identifier identifierAR = Identifiers.createAr("ARMultiSubscriptionTest");

		ResultItemMock resultItem_mock = new ResultItemMock(identifierAR);

		resultItem_mock.addCapability("CAP4", "ADMINISTRATIVE_DOMAIN_4", THIRD_TIMESTAMP);

		List<ResultItem> resultItems = new ArrayList<ResultItem>();
		resultItems.add(resultItem_mock.getMock());

		return resultItems;
	}

	private PollResult buildThirdMultiValuePollResult() {
		List<ResultItem> resultItems = buildThirdMultiValueResultItems();

		SearchResultMock searchResult1_mock = new SearchResultMock(resultItems, Type.updateResult);
		SearchResultMock searchResult2_mock = new SearchResultMock(resultItems, Type.updateResult);

		PollResultMock secondPollResult_mock = new PollResultMock();
		secondPollResult_mock.addSearchResult(searchResult1_mock.getMock());
		secondPollResult_mock.addSearchResult(searchResult2_mock.getMock());

		return secondPollResult_mock.getMock();
	}

	private List<ResultItem> buildThirdSingleValueResultItems() {
		Identifier identifierAR = Identifiers.createAr("ARMultiSubscriptionTest");
		Identifier identifierMAC = Identifiers.createMac("aa:bb:cc:dd:ee:ff");

		ResultItemMock resultItem_mock = new ResultItemMock(identifierAR, identifierMAC);

		resultItem_mock.addArMac(THIRD_TIMESTAMP);

		List<ResultItem> resultItems = new ArrayList<ResultItem>();
		resultItems.add(resultItem_mock.getMock());

		return resultItems;
	}

	private PollResult buildThirdSingleValuePollResult() {
		List<ResultItem> resultItems = buildThirdSingleValueResultItems();

		SearchResultMock searchResult1_mock = new SearchResultMock(resultItems, Type.updateResult);
		SearchResultMock searchResult2_mock = new SearchResultMock(resultItems, Type.updateResult);

		PollResultMock secondPollResult_mock = new PollResultMock();
		secondPollResult_mock.addSearchResult(searchResult1_mock.getMock());
		secondPollResult_mock.addSearchResult(searchResult2_mock.getMock());

		return secondPollResult_mock.getMock();
	}

}
