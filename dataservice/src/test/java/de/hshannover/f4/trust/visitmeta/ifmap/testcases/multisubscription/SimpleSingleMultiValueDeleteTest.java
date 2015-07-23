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
 * This file is part of visitmeta-dataservice, version 0.4.2,
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

	@Test
	public void multiValueOneIdentifierTest_ShouldReturnTheRightGraph() {
		executePollMultiValueDeleteTestOneIdentifier();

		List<IdentifierGraph> currentGraph = super.mService.getCurrentGraph();

		super.assertRightGraph(currentGraph, 1, 3, 2);
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

	@Test
	public void multiValueTwoIdentifierTest_ShouldReturnTheRightGraph() {
		executePollMultiValueDeleteTestTwoIdentifier();

		List<IdentifierGraph> currentGraph = super.mService.getCurrentGraph();

		super.assertRightGraph(currentGraph, 1, 3, 2);
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

	@Test
	public void singleValueOneIdentifierTest_ShouldReturnTheRightGraph() {
		executePollSingleValueDeleteTestOneIdentifier();

		List<IdentifierGraph> currentGraph = super.mService.getCurrentGraph();

		super.assertRightGraph(currentGraph, 1, 3, 2);
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

	@Test
	public void singleValueTwoIdentifierTest_ShouldReturnTheRightGraph() {
		executePollSingleValueDeleteTestTwoIdentifier();

		List<IdentifierGraph> currentGraph = super.mService.getCurrentGraph();

		super.assertRightGraph(currentGraph, 1, 3, 2);
	}

	private void executePollMultiValueDeleteTestOneIdentifier() {
		PollResult pollResult = buildMultiValueOneIdentifierPollResult();

		super.startPollTask(pollResult);

		mFirstChangesMap = super.mService.getChangesMap();

		PollResult deletePollResult = buildMultiValueOneIdentifierDeletePollResult();

		super.startPollTask(deletePollResult);

		mSecondChangesMap = super.mService.getChangesMap();
	}

	private void executePollMultiValueDeleteTestTwoIdentifier() {
		PollResult pollResult = buildMultiValueTwoIdentifierPollResult();

		super.startPollTask(pollResult);

		mFirstChangesMap = super.mService.getChangesMap();

		PollResult deletePollResult = buildMultiValueTwoIdentifierDeletePollResult();

		super.startPollTask(deletePollResult);

		mSecondChangesMap = super.mService.getChangesMap();
	}

	private void executePollSingleValueDeleteTestOneIdentifier() {
		PollResult pollResult = buildSingleValueOneIdentifierPollResult();

		super.startPollTask(pollResult);

		mFirstChangesMap = super.mService.getChangesMap();

		PollResult deletePollResult = buildSingleValueOneIdentifierDeletePollResult();

		super.startPollTask(deletePollResult);

		mSecondChangesMap = super.mService.getChangesMap();
	}

	private void executePollSingleValueDeleteTestTwoIdentifier() {
		PollResult pollResult = buildSingleValueTwoIdentifierPollResult();

		super.startPollTask(pollResult);

		mFirstChangesMap = super.mService.getChangesMap();

		PollResult deletePollResult = buildSingleValueTwoIdentifierDeletePollResult();

		super.startPollTask(deletePollResult);

		mSecondChangesMap = super.mService.getChangesMap();
	}

	private PollResult buildMultiValueOneIdentifierDeletePollResult() {
		return PollResultMock(
				SearchResultMock(SUB1, Type.deleteResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								CreateCapability(CAP1, FIRST_TIMESTAMP))),
				SearchResultMock(SUB2, Type.deleteResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								CreateCapability(CAP1, FIRST_TIMESTAMP))));
	}

	private PollResult buildMultiValueTwoIdentifierDeletePollResult() {
		return PollResultMock(
				SearchResultMock(SUB1, Type.deleteResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createDev(DEV45),
								CreateDevAttr(DEV45 + "-Attribute", FIRST_TIMESTAMP))),
				SearchResultMock(SUB2, Type.deleteResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createDev(DEV45),
								CreateDevAttr(DEV45 + "-Attribute", FIRST_TIMESTAMP))));
	}

	private PollResult buildSingleValueTwoIdentifierDeletePollResult() {
		return PollResultMock(
				SearchResultMock(SUB1, Type.deleteResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createMac(MAC3),
								CreateArMac(FIRST_TIMESTAMP))),
				SearchResultMock(SUB2, Type.deleteResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createMac(MAC3),
								CreateArMac(FIRST_TIMESTAMP))));
	}

	private PollResult buildSingleValueOneIdentifierDeletePollResult() {
		return PollResultMock(
				SearchResultMock(SUB1, Type.deleteResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								CreateArDev(FIRST_TIMESTAMP))),
				SearchResultMock(SUB2, Type.deleteResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								CreateArDev(FIRST_TIMESTAMP))));
	}

	private PollResult buildMultiValueOneIdentifierPollResult() {
		return PollResultMock(
				SearchResultMock(Type.updateResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createMac(MAC1),
								CreateArMac(FIRST_TIMESTAMP)),
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createMac(MAC2),
								CreateArMac(FIRST_TIMESTAMP)),
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								CreateCapability(CAP1, FIRST_TIMESTAMP))));
	}

	private PollResult buildMultiValueTwoIdentifierPollResult() {
		return PollResultMock(
				SearchResultMock(Type.updateResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createMac(MAC1),
								CreateArMac(FIRST_TIMESTAMP)),
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createMac(MAC2),
								CreateArMac(FIRST_TIMESTAMP)),
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createDev(DEV45),
								CreateDevAttr(DEV45 + "-Attribute", FIRST_TIMESTAMP))));
	}

	private PollResult buildSingleValueTwoIdentifierPollResult() {
		return PollResultMock(
				SearchResultMock(Type.updateResult,
					ResultItemMock(
							Identifiers.createAr(ACCESS_REQUEST),
							Identifiers.createMac(MAC1),
							CreateArMac(FIRST_TIMESTAMP)),
					ResultItemMock(
							Identifiers.createAr(ACCESS_REQUEST),
							Identifiers.createMac(MAC2),
							CreateArMac(FIRST_TIMESTAMP)),
					ResultItemMock(
							Identifiers.createAr(ACCESS_REQUEST),
						Identifiers.createMac(MAC3),
						CreateArMac(FIRST_TIMESTAMP))));
	}

	private PollResult buildSingleValueOneIdentifierPollResult() {
		return PollResultMock(
				SearchResultMock(Type.updateResult,
					ResultItemMock(
							Identifiers.createAr(ACCESS_REQUEST),
							Identifiers.createMac(MAC1),
							CreateArMac(FIRST_TIMESTAMP)),
					ResultItemMock(
							Identifiers.createAr(ACCESS_REQUEST),
							Identifiers.createMac(MAC2),
							CreateArMac(FIRST_TIMESTAMP)),
					ResultItemMock(
							Identifiers.createAr(ACCESS_REQUEST),
							CreateArDev(FIRST_TIMESTAMP))));
	}
}
