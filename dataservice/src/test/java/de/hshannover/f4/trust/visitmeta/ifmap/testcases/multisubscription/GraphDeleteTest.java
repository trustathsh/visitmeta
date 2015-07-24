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
 * This file is part of visitmeta-dataservice, version 0.5.0,
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

public class GraphDeleteTest extends AbstractMultiSubscriptionTestCase {

	private static final Date FIRST_TIMESTAMP = new Date(3333);

	private SortedMap<Long, Long> mFirstChangesMap;

	private SortedMap<Long, Long> mSecondChangesMap;

	@Test
	public void shouldReturnTheRightChangeMapSize() {
		executePollTask();

		super.assertEqualsMapSize(mSecondChangesMap, mFirstChangesMap.size() + 1);
	}

	@Test
	public void shouldReturnTheRightChangeMapChangeValues() {
		executePollTask();

		super.assertEqualsMapValues(mFirstChangesMap, mSecondChangesMap);
	}

	@Test
	public void shouldReturnTheRightChangeMapChangeValue() {
		executePollTask();

		super.assertEqualsNewValues(mFirstChangesMap, mSecondChangesMap, 1);
	}

	@Test
	public void shouldReturnTheRightGraph() {
		executePollTask();

		List<IdentifierGraph> currentGraph = super.mService.getCurrentGraph();

		super.assertRightGraph(currentGraph, 1, 4, 5);
	}

	private void executePollTask() {
		PollResult pollResult = buildStartingGraphPollResult();

		super.startPollTask(pollResult);

		mFirstChangesMap = super.mService.getChangesMap();

		PollResult deletePollResult = buildDeletePollResult();

		super.startPollTask(deletePollResult);

		mSecondChangesMap = super.mService.getChangesMap();
	}

	private PollResult buildDeletePollResult() {
		return PollResultMock(
				SearchResultMock(SUB1, Type.deleteResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createMac(MAC2),
								CreateArMac(FIRST_TIMESTAMP))),
				SearchResultMock(SUB2, Type.deleteResult,
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
								CreateArMac(FIRST_TIMESTAMP)),
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createDev(DEV45),
								CreateDevAttr(DEV45 + "-Attribute", FIRST_TIMESTAMP)),
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								CreateArDev(FIRST_TIMESTAMP),
								CreateCapability(CAP1, FIRST_TIMESTAMP))));
	}

	private PollResult buildStartingGraphPollResult() {
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
								CreateArMac(FIRST_TIMESTAMP)),
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createDev(DEV45),
								CreateDevAttr(DEV45 + "-Attribute", FIRST_TIMESTAMP)),
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								CreateArDev(FIRST_TIMESTAMP),
								CreateCapability(CAP1, FIRST_TIMESTAMP))));
	}
}
