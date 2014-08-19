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
 * This file is part of visitmeta dataservice, version 0.1.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@HsH
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

package de.hshannover.f4.trust.visitmeta.dataservice.graphservice;

import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_META_CARDINALITY;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TIMESTAMP_DELETE;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TIMESTAMP_PUBLISH;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TYPE_NAME;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_META_CARDINALITY_MULTI;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_META_CARDINALITY_SINGLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.MessageDigest;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;
import de.hshannover.f4.trust.visitmeta.persistence.Reader;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.LinkTypes;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JConnection;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JReader;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JRepository;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JTimestampManager;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JTypeLabels;

public class SimpleGraphServiceTest {

	private Reader mReader;
	private Executor mExecutor;
	private GraphDatabaseService mGraphDb;

	@Before
	public void prepareTestDatabase() throws Exception {
		mGraphDb = new TestGraphDatabaseFactory()
				.newImpermanentDatabaseBuilder().newGraphDatabase();

		Neo4JConnection neo4jConnection = mock(Neo4JConnection.class);
		when(neo4jConnection.getConnection()).thenReturn(mGraphDb);
		Neo4JRepository repo = new Neo4JRepository(neo4jConnection,
				MessageDigest.getInstance("MD5"));
		Neo4JConnection dbConnection = mock(Neo4JConnection.class);
		Neo4JTimestampManager timestampManager = new Neo4JTimestampManager(dbConnection);
		when(dbConnection.getTimestampManager()).thenReturn(timestampManager);
		when(dbConnection.getConnection()).thenReturn(mGraphDb);
		mReader = new Neo4JReader(repo, dbConnection);
		mExecutor = new Neo4JExecutor(dbConnection);
	}

	@After
	public void destroyTestDatabase() {
		mGraphDb.shutdown();
	}

	private void insertSimpleGraph(long publishTimestamp, long deleteTimestamp) {

	}

	private void insertLinkWithIdentifier(
			String id1,
			String id2,
			String metadata,
			long publishTimestamp,
			long deleteTimestamp) {
		Transaction tx = mGraphDb.beginTx();

		Node identifier1 = insertIdentifier(id1);
		Node identifier2 = insertIdentifier(id2);

		Node meta = insertMetadata(metadata, VALUE_META_CARDINALITY_SINGLE,
				publishTimestamp,
				deleteTimestamp);
		insertLink(identifier1, identifier2, meta);

		tx.success();
		tx.finish();
	}

	private void insertLinkWithMultiValueMetadataUpdate(
			String id1,
			String id2,
			String firstMetadata,
			String secondMetadata,
			long firstTimestamp,
			long secondTimestamp) {
		Transaction tx = mGraphDb.beginTx();

		Node identifier1 = insertIdentifier(id1);
		Node identifier2 = insertIdentifier(id2);

		Node firstMeta = insertMetadata(firstMetadata, VALUE_META_CARDINALITY_MULTI,
				firstTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);

		Node secondMeta = insertMetadata(secondMetadata, VALUE_META_CARDINALITY_MULTI,
				secondTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);

		insertLink(identifier1, identifier2, firstMeta, secondMeta);

		tx.success();
		tx.finish();

	}

	private Node insertMetadata(
			String name, String cardinality, long publishTimestamp, long deleteTimestamp) {
		Node meta = mGraphDb.createNode();
		meta.addLabel(Neo4JTypeLabels.METADATA);
		meta.setProperty(KEY_TYPE_NAME, name);
		meta.setProperty(KEY_META_CARDINALITY, cardinality);
		meta.setProperty(KEY_TIMESTAMP_PUBLISH, publishTimestamp);
		meta.setProperty(KEY_TIMESTAMP_DELETE, deleteTimestamp);
		meta.setProperty("/meta:" + name + "[@ifmap-cardinality]", "singleValue");
		return meta;
	}

	private Node insertIdentifier(String name) {
		Node identifier = mGraphDb.createNode();
		identifier.addLabel(Neo4JTypeLabels.IDENTIFIER);
		identifier.setProperty(KEY_TYPE_NAME, name);
		identifier.setProperty("name", name);
		identifier.addLabel(Neo4JTypeLabels.IDENTIFIER);
		return identifier;
	}

	private Node insertLink(Node id1, Node id2, Node...metadata) {
		Node link = mGraphDb.createNode();
		link.addLabel(Neo4JTypeLabels.LINK);
		id1.createRelationshipTo(link, LinkTypes.Link);
		id2.createRelationshipTo(link, LinkTypes.Link);

		for (Node meta : metadata) {
			link.createRelationshipTo(meta, LinkTypes.Meta);
		}
		return link;
	}

	private void insertIdentifierWithMultiValueMetadataUpdate(
			String id,
			String firstMetadata,
			String secondMetadata,
			long firstTimestamp,
			long secondTimestamp) {
		Transaction tx = mGraphDb.beginTx();

		Node identifier = insertIdentifier(id);
		Node firstMeta = insertMetadata(firstMetadata, VALUE_META_CARDINALITY_MULTI,
				firstTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		identifier.createRelationshipTo(firstMeta, LinkTypes.Meta);

		Node secondMeta = insertMetadata(secondMetadata, VALUE_META_CARDINALITY_MULTI,
				secondTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		identifier.createRelationshipTo(secondMeta, LinkTypes.Meta);

		tx.success();
		tx.finish();
	}

	private void insertIdentifierWithMultiValueMetadataDelete(
			String id,
			String someMetadata,
			String deletedMetadata,
			long firstTimestamp,
			long deleteTimestamp) {
		Transaction tx = mGraphDb.beginTx();

		Node identifier = insertIdentifier(id);
		Node firstMeta = insertMetadata(someMetadata, VALUE_META_CARDINALITY_MULTI,
				firstTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		identifier.createRelationshipTo(firstMeta, LinkTypes.Meta);

		Node secondMeta = insertMetadata(deletedMetadata, VALUE_META_CARDINALITY_MULTI,
				firstTimestamp, deleteTimestamp);
		identifier.createRelationshipTo(secondMeta, LinkTypes.Meta);

		tx.success();
		tx.finish();
	}

	private void insertLinkWithMultiValueUpdatesAndDeletes(String id1,
			String id2, String someMetadata, String updatedMeta, String deletedMeta,
			long firstTimestamp, long secondTimestamp) {
		Transaction tx = mGraphDb.beginTx();

		Node identifier1 = insertIdentifier(id1);
		Node identifier2 = insertIdentifier(id2);

		Node metaUnchanged = insertMetadata(someMetadata, VALUE_META_CARDINALITY_MULTI,
				firstTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		Node updatedMetadata = insertMetadata(updatedMeta, VALUE_META_CARDINALITY_MULTI,
				secondTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		Node deletedMetadata = insertMetadata(deletedMeta, VALUE_META_CARDINALITY_MULTI,
				firstTimestamp, secondTimestamp);

		insertLink(identifier1, identifier2, metaUnchanged, updatedMetadata, deletedMetadata);

		tx.success();
		tx.finish();
	}

	@Test
	public void testDeltaForSameTimeShouldBeEmpty() {
		final long timestamp = 42;
		SimpleGraphService sgs = new SimpleGraphService(mReader, mExecutor, new DummyGraphCache());
		insertLinkWithIdentifier("device", "ip-address", "device-ip",
				timestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		Delta delta = sgs.getDelta(timestamp, timestamp);

		assertEquals(0, delta.getDeletes().size());
		assertEquals(0, delta.getUpdates().size());
	}

	@Ignore("Disabled due to latest GraphService refactoring (ChangeMap and tests are not compatible yet)")
	@Test
	public void getDeltaShouldReturnTheCorrectNumberOfUpdatesInCaseOfUpdates() {
		SimpleGraphService sgs = new SimpleGraphService(mReader, mExecutor, new DummyGraphCache());
		final long firstTimestamp = 42;
		final long secondTimestamp = 50;
		insertLinkWithIdentifier("device", "ip-address", "device-ip",
				firstTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		insertLinkWithIdentifier("mac-address", "access-request", "access-request-mac",
				secondTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		Delta delta = sgs.getDelta(firstTimestamp, secondTimestamp);

		assertEquals(1, delta.getUpdates().size());
	}

	@Ignore("Disabled due to latest GraphService refactoring (ChangeMap and tests are not compatible yet)")
	@Test
	public void getDeltaShouldReturnTheCorrectContentOfUpdatesInCaseOfUpdates() {
		SimpleGraphService sgs = new SimpleGraphService(mReader, mExecutor, new DummyGraphCache());
		final long firstTimestamp = 42;
		final long secondTimestamp = 50;
		insertLinkWithIdentifier("device", "ip-address", "device-ip",
				firstTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		insertLinkWithIdentifier("mac-address", "access-request", "access-request-mac",
				secondTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		Delta delta = sgs.getDelta(firstTimestamp, secondTimestamp);

		assertEquals(1, delta.getUpdates().size());
		assertEquals(2, delta.getUpdates().get(0).getIdentifiers().size());

		String id1 = delta.getUpdates().get(0).getIdentifiers().get(0).getTypeName();
		String id2 = delta.getUpdates().get(0).getIdentifiers().get(1).getTypeName();

		assertTrue(id1.equals("mac-address") || id1.equals("access-request"));
		assertTrue(id2.equals("mac-address") || id2.equals("access-request"));
		assertEquals(1, delta.getUpdates().get(0).getIdentifiers().get(0).getLinks().size());
		assertEquals(1, delta.getUpdates().get(0).getIdentifiers().get(0).getLinks().get(0).getMetadata().size());
	}

	@Test
	public void getDeltaShouldReturnTheCorrectNumberOfDeletesInCaseOfUpdates() {
		SimpleGraphService sgs = new SimpleGraphService(mReader, mExecutor, new DummyGraphCache());
		final long firstTimestamp = 42;
		final long secondTimestamp = 50;
		insertLinkWithIdentifier("device", "ip-address", "device-ip",
				firstTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		insertLinkWithIdentifier("mac-address", "access-request", "access-request-mac",
				secondTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);

		Delta delta = sgs.getDelta(firstTimestamp, secondTimestamp);

		assertEquals(0, delta.getDeletes().size());
	}

	@Test
	public void getDeltaShouldReturnTheCorrectNumberOfUpdatesInCaseOfDelete() {
		SimpleGraphService sgs = new SimpleGraphService(mReader, mExecutor, new DummyGraphCache());
		final long firstTimestamp = 42;
		final long secondTimestamp = 50;
		insertLinkWithIdentifier("device", "ip-address", "device-ip",
				firstTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		insertLinkWithIdentifier("mac-address", "access-request", "access-request-mac",
				firstTimestamp, secondTimestamp);

		Delta delta = sgs.getDelta(firstTimestamp, secondTimestamp);

		assertEquals(0, delta.getUpdates().size());
	}

	@Ignore("Disabled due to latest GraphService refactoring (ChangeMap and tests are not compatible yet)")
	@Test
	public void getDeltaShouldReturnTheCorrectNumberOfDeletesInCaseOfDelete() {
		SimpleGraphService sgs = new SimpleGraphService(mReader, mExecutor, new DummyGraphCache());
		final long firstTimestamp = 42;
		final long secondTimestamp = 50;
		insertLinkWithIdentifier("device", "ip-address", "device-ip",
				firstTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		insertLinkWithIdentifier("mac-address", "access-request", "access-request-mac",
				firstTimestamp, secondTimestamp);

		Delta delta = sgs.getDelta(firstTimestamp, secondTimestamp);

		assertEquals(1, delta.getDeletes().size());
	}

	@Ignore("Disabled due to latest GraphService refactoring (ChangeMap and tests are not compatible yet)")
	@Test
	public void getDeltaShouldReturnTheCorrectContentOfDeletesInCaseOfDelete() {
		SimpleGraphService sgs = new SimpleGraphService(mReader, mExecutor, new DummyGraphCache());
		final long firstTimestamp = 42;
		final long secondTimestamp = 50;
		insertLinkWithIdentifier("device", "ip-address", "device-ip",
				firstTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		insertLinkWithIdentifier("mac-address", "access-request", "access-request-mac",
				firstTimestamp, secondTimestamp);

		Delta delta = sgs.getDelta(firstTimestamp, secondTimestamp);

		assertEquals(2, delta.getDeletes().get(0).getIdentifiers().size());

		String id1 = delta.getDeletes().get(0).getIdentifiers().get(0).getTypeName();
		String id2 = delta.getDeletes().get(0).getIdentifiers().get(1).getTypeName();

		assertTrue(id1.equals("mac-address") || id1.equals("access-request"));
		assertTrue(id2.equals("mac-address") || id2.equals("access-request"));
		assertEquals(1, delta.getDeletes().get(0).getIdentifiers().get(0).getLinks().size());
		assertEquals(1, delta.getDeletes().get(0).getIdentifiers().get(0).getLinks().get(0).getMetadata().size());
	}

	@Ignore("Disabled due to latest GraphService refactoring (ChangeMap and tests are not compatible yet)")
	@Test
	public void getDeltaShouldReturnTheCorrectNumberOfDeletesAndUpdates() {
		SimpleGraphService sgs = new SimpleGraphService(mReader, mExecutor, new DummyGraphCache());
		final long firstTimestamp = 42;
		final long secondTimestamp = 50;
		insertLinkWithIdentifier("device", "ip-address", "device-ip",
				firstTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		insertLinkWithIdentifier("mac-address", "access-request", "access-request-mac",
				firstTimestamp, secondTimestamp);
		insertLinkWithIdentifier("identity", "id42", "fancy-metadata",
				secondTimestamp, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);

		Delta delta = sgs.getDelta(firstTimestamp, secondTimestamp);
		assertEquals(1, delta.getUpdates().size());
		assertEquals(1, delta.getDeletes().size());
	}

	@Ignore("Disabled due to latest GraphService refactoring (ChangeMap and tests are not compatible yet)")
	@Test
	public void getDeltaShouldReturnAIdentiferWithMetadataUpdates() {
		SimpleGraphService sgs = new SimpleGraphService(mReader, mExecutor, new DummyGraphCache());
		final long firstTimestamp = 42;
		final long secondTimestamp = 50;
		final String newMeta = "event50";
		insertIdentifierWithMultiValueMetadataUpdate(
				"ip-address", "event42", newMeta,
				firstTimestamp, secondTimestamp);

		Delta delta = sgs.getDelta(firstTimestamp, secondTimestamp);

		assertEquals(1, delta.getUpdates().size());
		assertEquals(1, delta.getUpdates().get(0).getIdentifiers().size());
		assertEquals(1, delta.getUpdates().get(0).getIdentifiers().get(0).getMetadata().size());
		assertEquals(newMeta, delta.getUpdates().get(0).getIdentifiers().get(0).getMetadata().get(0).getTypeName());
	}

	@Ignore("Disabled due to latest GraphService refactoring (ChangeMap and tests are not compatible yet)")
	@Test
	public void getDeltaShouldReturnAIdentiferWithMetadataDeletes() {
		SimpleGraphService sgs = new SimpleGraphService(mReader, mExecutor, new DummyGraphCache());
		final long firstTimestamp = 42;
		final long secondTimestamp = 50;
		final String deletedMetadata = "event50";
		insertIdentifierWithMultiValueMetadataDelete(
				"ip-address", "event42", deletedMetadata,
				firstTimestamp, secondTimestamp);

		Delta delta = sgs.getDelta(firstTimestamp, secondTimestamp);

		assertEquals(0, delta.getUpdates().size());
		assertEquals(1, delta.getDeletes().size());
		assertEquals(1, delta.getDeletes().get(0).getIdentifiers().size());
		assertEquals(1, delta.getDeletes().get(0).getIdentifiers().get(0).getMetadata().size());
		assertEquals(deletedMetadata, delta.getDeletes().get(0).getIdentifiers().get(0).getMetadata().get(0).getTypeName());
	}

	@Ignore("Disabled due to latest GraphService refactoring (ChangeMap and tests are not compatible yet)")
	@Test
	public void getDeltaShouldReturnTwoIdentifiersWithMetadataUpdates() {
		SimpleGraphService sgs = new SimpleGraphService(mReader, mExecutor, new DummyGraphCache());
		final long firstTimestamp = 42;
		final long secondTimestamp = 50;
		final String newMeta = "metadata50";
		insertLinkWithMultiValueMetadataUpdate("ip-address", "mac-address",
				"metadata42", newMeta, firstTimestamp, secondTimestamp);

		Delta delta = sgs.getDelta(firstTimestamp, secondTimestamp);

		assertEquals(1, delta.getUpdates().size());
		assertEquals(2, delta.getUpdates().get(0).getIdentifiers().size());
		assertEquals(1, delta.getUpdates().get(0).getIdentifiers().get(0).getLinks().size());
		assertEquals(1, delta.getUpdates().get(0).getIdentifiers().get(1).getLinks().size());
		assertEquals(1, delta.getUpdates().get(0).getIdentifiers().get(0).getLinks().get(0).getMetadata().size());
		assertEquals(newMeta, delta.getUpdates().get(0).getIdentifiers().get(0).getLinks().get(0).getMetadata().get(0).getTypeName());
	}

	@Ignore("Disabled due to latest GraphService refactoring (ChangeMap and tests are not compatible yet)")
	@Test
	public void getDeltaShouldReturnCorrectResultsForUpdatesAndDeletes() {
		SimpleGraphService sgs = new SimpleGraphService(mReader, mExecutor, new DummyGraphCache());
		final long firstTimestamp = 42;
		final long secondTimestamp = 50;

		String updatedMeta = "updatedMeta";
		String deletedMeta = "deletedMeta";

		insertLinkWithMultiValueUpdatesAndDeletes("ip-address", "mac-address",
				"ip-mac", updatedMeta, deletedMeta,
				firstTimestamp, secondTimestamp);

		Delta delta = sgs.getDelta(firstTimestamp, secondTimestamp);

		assertEquals(1, delta.getUpdates().size());
		assertEquals(1, delta.getDeletes().size());
		assertEquals(2, delta.getUpdates().get(0).getIdentifiers().size());
		assertEquals(2, delta.getDeletes().get(0).getIdentifiers().size());

		String id1 = delta.getUpdates().get(0).getIdentifiers().get(0).getTypeName();
		String id2 = delta.getUpdates().get(0).getIdentifiers().get(1).getTypeName();
		assertTrue(id1.equals("ip-address") || id2.equals("mac-address"));
		assertTrue(id2.equals("ip-address") || id2.equals("mac-address"));

		id1 = delta.getDeletes().get(0).getIdentifiers().get(0).getTypeName();
		id2 = delta.getDeletes().get(0).getIdentifiers().get(1).getTypeName();
		assertTrue(id1.equals("ip-address") || id2.equals("mac-address"));
		assertTrue(id2.equals("ip-address") || id2.equals("mac-address"));

		assertEquals(1, delta.getDeletes().get(0).getIdentifiers().get(0).getLinks().size());
		assertEquals(1, delta.getDeletes().get(0).getIdentifiers().get(1).getLinks().size());

		assertEquals(1, delta.getUpdates().get(0).getIdentifiers().get(0).getLinks().size());
		assertEquals(1, delta.getUpdates().get(0).getIdentifiers().get(1).getLinks().size());

		assertEquals(1, delta.getDeletes().get(0).getIdentifiers().get(0).getLinks().get(0).getMetadata().size());
		assertEquals(deletedMeta, delta.getDeletes().get(0).getIdentifiers().get(0).getLinks().get(0).getMetadata().get(0).getTypeName());
	}

	@Ignore("Disabled due to latest GraphService refactoring (ChangeMap and tests are not compatible yet)")
	@Test
	public void getDeltaShouldSwapTheTimestampsIntoProperOrder() {
		SimpleGraphService sgs = new SimpleGraphService(mReader, mExecutor, new DummyGraphCache());
		final long firstTimestamp = 42;
		final long secondTimestamp = 50;
		insertLinkWithIdentifier("device", "ip-address", "device-ip",
				firstTimestamp , InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		insertLinkWithIdentifier("mac-address", "access-request", "access-request-mac",
				secondTimestamp , InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);

		// given: from > to
		Delta delta = sgs.getDelta(secondTimestamp, firstTimestamp);
		assertEquals(1, delta.getUpdates().size());
	}

	@Test
	public void identifierGraphObjectsContainCorrectTimestamp() {
		SimpleGraphService sgs = new SimpleGraphService(mReader, mExecutor, new DummyGraphCache());
		final long firstTimestamp = 42;
		insertLinkWithIdentifier("device", "ip-address", "device-ip",
				firstTimestamp , InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);

		List<IdentifierGraph> graphsAt42 = sgs.getGraphAt(firstTimestamp);
		for (IdentifierGraph g: graphsAt42) {
			assertEquals(firstTimestamp, g.getTimestamp());
		}
	}

}
