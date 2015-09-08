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
 * This file is part of visitmeta-dataservice, version 0.5.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
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

import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import org.junit.Test;

import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.messages.PollResult;
import de.hshannover.f4.trust.ifmapj.messages.SearchResult.Type;
import de.hshannover.f4.trust.visitmeta.ifmap.AbstractMultiSubscriptionTestCase;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

public class ExtensiveSingleMultivalueTest extends AbstractMultiSubscriptionTestCase {

	private static final Date FIRST_TIMESTAMP = new Date(3333);

	private static final Date SECOND_TIMESTAMP = new Date(5555);

	private static final Date THIRD_TIMESTAMP = new Date(8888);

	private SortedMap<Long, Long> mFirstChangesMap;

	private SortedMap<Long, Long> mSecondChangesMap;

	private SortedMap<Long, Long> mThirdChangesMap;

	@Test
	public void twoPolls_ShouldReturnTheRightChangeMapSize() {
		executeFirstTwoPolls();

		super.assertEqualsMapSize(mSecondChangesMap, mFirstChangesMap.size() + 1);
	}

	@Test
	public void twoPolls_ShouldReturnTheRightChangeMapChangeValues() {
		executeFirstTwoPolls();

		super.assertEqualsMapValues(mFirstChangesMap, mSecondChangesMap);
	}

	@Test
	public void twoPolls_ShouldReturnTheRightSecondChangeMapChangeValue() {
		executeFirstTwoPolls();

		super.assertEqualsNewValues(mFirstChangesMap, mSecondChangesMap, 1);
	}

	@Test
	public void twoPolls_ShouldReturnTheRightGraph() {
		executeFirstTwoPolls();

		List<IdentifierGraph> currentGraph = super.mService.getCurrentGraph();

		super.assertRightGraph(currentGraph, 1, 3, 5);
	}

	@Test
	public void thirdPollSingleValue_ShouldReturnTheRightChangeMapSize() {
		executeFirstTwoPolls();
		executeThirdPollWithSingleValue();

		super.assertEqualsMapSize(mThirdChangesMap, mSecondChangesMap.size() + 1);
	}

	@Test
	public void thirdPollSingleValue_ShouldReturnTheRightChangeMapChangeValues() {
		executeFirstTwoPolls();
		executeThirdPollWithSingleValue();

		super.assertEqualsMapValues(mSecondChangesMap, mThirdChangesMap);
	}

	@Test
	public void thirdPollSingleValue_ShouldReturnTheRightThirdChangeMapChangeValue() {
		executeFirstTwoPolls();
		executeThirdPollWithSingleValue();

		super.assertEqualsNewValues(mSecondChangesMap, mThirdChangesMap, 1);
	}

	@Test
	public void thirdPollSingleValue_ShouldReturnTheRightGraph() {
		executeFirstTwoPolls();
		executeThirdPollWithSingleValue();

		List<IdentifierGraph> currentGraph = super.mService.getCurrentGraph();

		super.assertRightGraph(currentGraph, 1, 4, 6);
	}

	@Test
	public void thirdPollMultiValue_ShouldReturnTheRightChangeMapSize() {
		executeFirstTwoPolls();
		executeThirdPollWithMultiValue();

		super.assertEqualsMapSize(mThirdChangesMap, mSecondChangesMap.size() + 1);
	}

	@Test
	public void thirdPollMultiValue_ShouldReturnTheRightChangeMapChangeValues() {
		executeFirstTwoPolls();
		executeThirdPollWithMultiValue();

		super.assertEqualsMapValues(mSecondChangesMap, mThirdChangesMap);
	}

	@Test
	public void thirdPollMultiValue_ShouldReturnTheRightThirdChangeMapChangeValue() {
		executeFirstTwoPolls();
		executeThirdPollWithMultiValue();

		super.assertEqualsNewValues(mSecondChangesMap, mThirdChangesMap, 1);
	}

	@Test
	public void thirdPollMultiValue_ShouldReturnTheRightGraph() {
		executeFirstTwoPolls();
		executeThirdPollWithMultiValue();

		List<IdentifierGraph> currentGraph = super.mService.getCurrentGraph();

		super.assertRightGraph(currentGraph, 1, 3, 6);
	}

	/**
	 * Makes two polls with different PollResult. The PollResult are the same as if when we makes two subscriptions.
	 */
	private void executeFirstTwoPolls() {
		// mock first and second poll results
		PollResult firstPollResult = buildFirstPollResult();
		PollResult secondPollResult = buildSecondPollResult();

		// run first poll
		super.startPollTask(firstPollResult);

		// save current ChangesMap after the first poll
		mFirstChangesMap = super.mService.getChangesMap();

		// run second poll
		super.startPollTask(secondPollResult);

		// save current ChangesMap after the second poll
		mSecondChangesMap = super.mService.getChangesMap();

	}

	private void executeThirdPollWithSingleValue() {
		PollResult thirdPollResult = buildThirdSingleValuePollResult();

		super.startPollTask(thirdPollResult);

		// save current ChangesMap after the third poll
		mThirdChangesMap = super.mService.getChangesMap();

	}

	private void executeThirdPollWithMultiValue() {
		PollResult thirdPollResult = buildThirdMultiValuePollResult();

		super.startPollTask(thirdPollResult);

		// save current ChangesMap after the third poll
		mThirdChangesMap = super.mService.getChangesMap();

	}

	private PollResult buildFirstPollResult() {
		return PollResultMock(
				SearchResultMock(SUB1, Type.updateResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								CreateCapability(CAP1, FIRST_TIMESTAMP)),
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createMac(MAC1),
								CreateArMac(FIRST_TIMESTAMP)),
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								CreateCapability(CAP2, FIRST_TIMESTAMP),
								CreateCapability(CAP3, FIRST_TIMESTAMP))));
	}

	private PollResult buildSecondPollResult() {
		return PollResultMock(
				SearchResultMock(SUB2, Type.updateResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								CreateCapability(CAP1, FIRST_TIMESTAMP)),
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createMac(MAC1),
								CreateArMac(FIRST_TIMESTAMP)),
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								CreateCapability(CAP2, FIRST_TIMESTAMP),
								CreateCapability(CAP3, FIRST_TIMESTAMP)),
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createMac(MAC2),
								CreateArMac(SECOND_TIMESTAMP))),
				SearchResultMock(SUB1, Type.updateResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createMac(MAC2),
								CreateArMac(SECOND_TIMESTAMP))));
	}

	private PollResult buildThirdMultiValuePollResult() {
		return PollResultMock(
				SearchResultMock(SUB1, Type.updateResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								CreateCapability(CAP4, THIRD_TIMESTAMP))),
				SearchResultMock(SUB2, Type.updateResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								CreateCapability(CAP4, THIRD_TIMESTAMP))));
	}

	private PollResult buildThirdSingleValuePollResult() {
		return PollResultMock(
				SearchResultMock(SUB1, Type.updateResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createMac(MAC4),
								CreateArMac(THIRD_TIMESTAMP))),
				SearchResultMock(SUB2, Type.updateResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createMac(MAC4),
								CreateArMac(THIRD_TIMESTAMP))));
	}

}
