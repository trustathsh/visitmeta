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

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import org.junit.Test;

import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.messages.PollResult;
import de.hshannover.f4.trust.ifmapj.messages.SearchResult.Type;
import de.hshannover.f4.trust.visitmeta.ifmap.AbstractMultiSubscriptionTestCase;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

public class SimpleSingleMultiValueTest extends AbstractMultiSubscriptionTestCase {

	private static final Date FIRST_TIMESTAMP = new Date(3333);

	private SortedMap<Long, Long> mChangesMap;

	@Test
	public void singleValue_ShouldReturnTheRightChangeMapSize() {
		executePollWithSingleValue();

		super.assertEqualsMapSize(mChangesMap, 1);
	}

	@Test
	public void singleValue_ShouldReturnTheRightChangeMapChangeValue() {
		executePollWithSingleValue();

		for (Entry<Long, Long> entry : mChangesMap.entrySet()) {
			assertEquals("Because the value from the key(" + entry.getKey() + ") is not right.", 1L, entry.getValue()
					.longValue());
			break;
		}
	}

	@Test
	public void singleValue_ShouldReturnTheRightGraph() {
		executePollWithSingleValue();

		List<IdentifierGraph> currentGraph = super.mService.getCurrentGraph();

		super.assertRightGraph(currentGraph, 1, 2, 1);
	}

	@Test
	public void multiValue_ShouldReturnTheRightChangeMapSize() {
		executePollWithMultiValue();

		super.assertEqualsMapSize(mChangesMap, 1);
	}

	@Test
	public void multiValue_ShouldReturnTheRightChangeMapChangeValue() {
		executePollWithMultiValue();

		for (Entry<Long, Long> entry : mChangesMap.entrySet()) {
			assertEquals("Because the value from the key(" + entry.getKey() + ") is not right.", 1L, entry.getValue()
					.longValue());
			break;
		}
	}

	@Test
	public void multiValue_ShouldReturnTheRightGraph() {
		executePollWithMultiValue();

		List<IdentifierGraph> currentGraph = super.mService.getCurrentGraph();

		super.assertRightGraph(currentGraph, 1, 2, 1);
	}

	private void executePollWithSingleValue() {
		// mock poll results
		PollResult pollResult = buildPollResultWithSingleValue();

		// run poll
		super.startPollTask(pollResult);

		// save current ChangesMap after the first poll
		mChangesMap = super.mService.getChangesMap();

	}

	private void executePollWithMultiValue() {
		// mock poll results
		PollResult pollResult = buildMultiValuePollResult();

		// run poll
		super.startPollTask(pollResult);

		// save current ChangesMap after the first poll
		mChangesMap = super.mService.getChangesMap();

	}

	private PollResult buildMultiValuePollResult() {
		return PollResultMock(
				SearchResultMock(SUB1, Type.updateResult,
						ResultItemMock(
								Identifiers.createIp4(IP4_ADDRESS),
								Identifiers.createAr(MAC1),
								CreateIpMac(FIRST_TIMESTAMP))),
				SearchResultMock(SUB2, Type.updateResult,
						ResultItemMock(
								Identifiers.createIp4(IP4_ADDRESS),
								Identifiers.createAr(MAC1),
								CreateIpMac(FIRST_TIMESTAMP))));
	}

	private PollResult buildPollResultWithSingleValue() {
		return PollResultMock(
				SearchResultMock(SUB1, Type.updateResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createMac(MAC1),
								CreateArMac(FIRST_TIMESTAMP))),
				SearchResultMock(SUB2, Type.updateResult,
						ResultItemMock(
								Identifiers.createAr(ACCESS_REQUEST),
								Identifiers.createMac(MAC1),
								CreateArMac(FIRST_TIMESTAMP))));
	}

}
