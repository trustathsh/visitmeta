package de.fhhannover.inform.trust.visitmeta.ifmap;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
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
 * This file is part of VisITMeta, version 0.0.1, implemented by the Trust@FHH 
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@FHH
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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import de.fhhannover.inform.trust.ifmapj.IfmapJ;
import de.fhhannover.inform.trust.ifmapj.channel.ARC;
import de.fhhannover.inform.trust.ifmapj.identifier.Identifier;
import de.fhhannover.inform.trust.ifmapj.identifier.Identifiers;
import de.fhhannover.inform.trust.ifmapj.messages.PollResult;
import de.fhhannover.inform.trust.ifmapj.messages.ResultItem;
import de.fhhannover.inform.trust.ifmapj.messages.SearchResult;
import de.fhhannover.inform.trust.ifmapj.messages.SearchResult.Type;
import de.fhhannover.inform.trust.ifmapj.metadata.StandardIfmapMetadataFactory;
import de.fhhannover.inform.trust.visitmeta.dataservice.factories.InternalIdentifierFactory;
import de.fhhannover.inform.trust.visitmeta.dataservice.factories.InternalMetadataFactory;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;

public class PollTaskTest {

	private StandardIfmapMetadataFactory mIfmapMetadataFactory;

	private ARC mArc;
	private InternalMetadataFactory mMetadataFactory;
	private IfmapJHelper mIfmapJHelper;

	private PollTask mPollTask;

	@Before
	public void setup() {
		mArc = mock(ARC.class);
		mMetadataFactory = mock(InternalMetadataFactory.class);
		mIfmapJHelper = new IfmapJHelper(new IdentifierFactoryStub());
		InternalMetadata metadataMock = mock(InternalMetadata.class);
		when(mMetadataFactory.createMetadata(any(Document.class))).thenReturn(metadataMock);

		mIfmapMetadataFactory = IfmapJ.createStandardMetadataFactory();

		mPollTask = new PollTask(mArc, mMetadataFactory, mIfmapJHelper);
	}

	@Test
	public void thePollResultShouldContainAllReceivedUpdates() throws Exception {
		PollResult pollResult = PollResultMock(Arrays.asList(
				SearchResultMock(Arrays.asList(
						ResultItemMock(
								Identifiers.createIp4("10.1.1.1"),
								Identifiers.createMac("ee:ee:ee:ee:ee:ee"),
								Arrays.asList(mIfmapMetadataFactory.createIpMac())),
						ResultItemMock(
								Identifiers.createIp4("10.1.1.9"),
								Identifiers.createMac("ee:ee:ee:ee:ee:ee"),
								Arrays.asList(mIfmapMetadataFactory.createIpMac()))
				), Type.updateResult),
				SearchResultMock(Arrays.asList(
						ResultItemMock(
								Identifiers.createAr("111:33"),
								Identifiers.createDev("device42"),
								Arrays.asList(mIfmapMetadataFactory.createArDev()))
				), Type.updateResult)
		));

		when(mArc.poll()).thenReturn(pollResult);

		de.fhhannover.inform.trust.visitmeta.ifmap.PollResult p = mPollTask.call();
		assertEquals(3, p.getUpdates().size());
	}

	@Test
	public void thePollResultShouldNotContainUpdatesWithNoMetadataOnIdentifers() throws Exception {
		PollResult pollResult = PollResultMock(Arrays.asList(
				SearchResultMock(Arrays.asList(
						ResultItemMock(
								Identifiers.createAr("111:33"),
								Collections.EMPTY_LIST)
				), Type.updateResult)
		));

		when(mArc.poll()).thenReturn(pollResult);

		de.fhhannover.inform.trust.visitmeta.ifmap.PollResult p = mPollTask.call();
		assertEquals(0, p.getUpdates().size());
	}

	@Test
	public void thePollResultShouldNotContainUpdatesWithNoMetadataOnLinks() throws Exception {
		PollResult pollResult = PollResultMock(Arrays.asList(
				SearchResultMock(Arrays.asList(
						ResultItemMock(
								Identifiers.createAr("111:33"),
								Identifiers.createDev("device42"),
								Collections.EMPTY_LIST)
				), Type.updateResult)
		));

		when(mArc.poll()).thenReturn(pollResult);

		de.fhhannover.inform.trust.visitmeta.ifmap.PollResult p = mPollTask.call();
		assertEquals(0, p.getUpdates().size());
	}

	@Test
	public void thePollResultShouldContainAllReceivedDeletes() throws Exception {
		PollResult pollResult = PollResultMock(Arrays.asList(
				SearchResultMock(Arrays.asList(
						ResultItemMock(
								Identifiers.createIp4("10.1.1.1"),
								Identifiers.createMac("ee:ee:ee:ee:ee:ee"),
								Arrays.asList(mIfmapMetadataFactory.createIpMac())),
						ResultItemMock(
								Identifiers.createIp4("10.1.1.9"),
								Identifiers.createMac("ee:ee:ee:ee:ee:ee"),
								Arrays.asList(mIfmapMetadataFactory.createIpMac()))
				), Type.deleteResult),
				SearchResultMock(Arrays.asList(
						ResultItemMock(
								Identifiers.createAr("111:33"),
								Identifiers.createDev("device42"),
								Arrays.asList(mIfmapMetadataFactory.createArDev()))
				), Type.deleteResult)
		));

		when(mArc.poll()).thenReturn(pollResult);

		de.fhhannover.inform.trust.visitmeta.ifmap.PollResult p = mPollTask.call();
		assertEquals(3, p.getDeletes().size());
	}

	@Test
	public void thePollResultShouldContainAllReceivedUpdatesAndDeletes() throws Exception {
		PollResult pollResult = PollResultMock(Arrays.asList(
				SearchResultMock(Arrays.asList(
						ResultItemMock(
								Identifiers.createIp4("10.1.1.1"),
								Identifiers.createMac("ee:ee:ee:ee:ee:ee"),
								Arrays.asList(mIfmapMetadataFactory.createIpMac())),
						ResultItemMock(
								Identifiers.createIp4("10.1.1.9"),
								Identifiers.createMac("ee:ee:ee:ee:ee:ee"),
								Arrays.asList(mIfmapMetadataFactory.createIpMac()))
				), Type.updateResult),
				SearchResultMock(Arrays.asList(
						ResultItemMock(
								Identifiers.createAr("111:33"),
								Identifiers.createDev("device42"),
								Arrays.asList(mIfmapMetadataFactory.createArDev()))
				), Type.updateResult),
				SearchResultMock(Arrays.asList(
						ResultItemMock(
								Identifiers.createAr("111:44"),
								Identifiers.createDev("device77"),
								Arrays.asList(mIfmapMetadataFactory.createArDev()))
				), Type.deleteResult)
		));

		when(mArc.poll()).thenReturn(pollResult);

		de.fhhannover.inform.trust.visitmeta.ifmap.PollResult p = mPollTask.call();
		assertEquals(3, p.getUpdates().size());
		assertEquals(1, p.getDeletes().size());
	}

	private static PollResult PollResultMock(List<SearchResult> searchResults) {
		PollResult pollResult = mock(PollResult.class);
		when(pollResult.getResults()).thenReturn(searchResults);
		return pollResult;
	}

	private static SearchResult SearchResultMock(List<ResultItem> item, SearchResult.Type type) {
		SearchResult searchResult = mock(SearchResult.class);
		when(searchResult.getResultItems()).thenReturn(item);
		when(searchResult.getType()).thenReturn(type);
		return searchResult;
	}

	private static ResultItem ResultItemMock(
			Identifier id1, Identifier id2, List<Document> metadata) {
		ResultItem resultItem = mock(ResultItem.class);
		when(resultItem.getMetadata()).thenReturn(metadata);
		when(resultItem.getIdentifier1()).thenReturn(id1);
		when(resultItem.getIdentifier2()).thenReturn(id2);
		when(resultItem.holdsLink()).thenReturn(true);
		return resultItem;
	}

	private static ResultItem ResultItemMock(Identifier id, List<Document> metadata) {
		ResultItem resultItem = mock(ResultItem.class);
		when(resultItem.getMetadata()).thenReturn(metadata);
		when(resultItem.getIdentifier1()).thenReturn(id);
		when(resultItem.holdsLink()).thenReturn(false);
		return resultItem;
	}


	class IdentifierFactoryStub implements InternalIdentifierFactory {

		@Override
		public InternalIdentifier createIdentifier(InternalIdentifier id) {
			return mock(InternalIdentifier.class);
		}

		@Override
		public InternalIdentifier createIdentifier(Document d) {
			return mock(InternalIdentifier.class);
		}

	}
}