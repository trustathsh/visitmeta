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
 * This file is part of visitmeta dataservice, version 0.0.6,
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

package de.hshannover.f4.trust.visitmeta.dataservice.persistence;

import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifierGraph;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.persistence.AbstractReader;
import de.hshannover.f4.trust.visitmeta.persistence.Repository;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.LinkTypes;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JConnection;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JRepository;

import static org.junit.Assert.*;

public class AbstractReaderTest {

	private static final long PUBLISH_TIMESTAMP = 1352721297349L;

	private AbstractReader mReader;

	private GraphDatabaseService mGraphDb;
	private Repository mRepository;

	class AbstractReaderStub extends AbstractReader {

		public AbstractReaderStub(Repository repo) {
			mRepo = repo;
		}

		@Override
		public SortedMap<Long, Long> getChangesMap() {
			throw new UnsupportedOperationException();
		}

		@Override
		public long getTimeOfLastUpdate() {
			throw new UnsupportedOperationException();
		}
	}

	@Before
	public void prepareTestDatabase() throws Exception {
		mGraphDb =
				new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder().newGraphDatabase();

		Neo4JConnection neo4jConnection = mock(Neo4JConnection.class);
		when(neo4jConnection.getConnection()).thenReturn(mGraphDb);
		mRepository = new Neo4JRepository(neo4jConnection, MessageDigest.getInstance("MD5"));
		mReader = new AbstractReaderStub(mRepository);
	}

	@After
	public void destroyTestDatabase() {
		mGraphDb.shutdown();
	}

	private void insertTwoIdentifierWithOneMetadata() {
		Transaction tx = mGraphDb.beginTx();

		Node id1 = mGraphDb.createNode();
		id1.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_IDENTIFIER);
		id1.setProperty(KEY_TYPE_NAME, "device");
		id1.setProperty(KEY_HASH, "2f91feecc8e13d631e09b56f2c1d0110");
		id1.setProperty("/device/name", "device99");

		Relationship r1 = mGraphDb.getReferenceNode().
				createRelationshipTo(id1, LinkTypes.Creation);
		r1.setProperty(KEY_TIMESTAMP_PUBLISH, PUBLISH_TIMESTAMP);

		Node id2 = mGraphDb.createNode();
		id2.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_IDENTIFIER);
		id2.setProperty(KEY_TYPE_NAME, "access-request");
		id2.setProperty(KEY_HASH, "4da768ebd34c7c1103bdcd5c5118b000");
		id2.setProperty("/access-request[@name]", "111:33");

		Relationship r2 = mGraphDb.getReferenceNode().
				createRelationshipTo(id1, LinkTypes.Creation);
		r2.setProperty(KEY_TIMESTAMP_PUBLISH, PUBLISH_TIMESTAMP);

		Node link = mGraphDb.createNode();
		link.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_LINK);

		id1.createRelationshipTo(link, LinkTypes.Link);
		id2.createRelationshipTo(link, LinkTypes.Link);

		Node meta = mGraphDb.createNode();
		meta.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_METADATA);
		meta.setProperty(KEY_TYPE_NAME, "authenticated-by");
		meta.setProperty(KEY_META_CARDINALITY, VALUE_META_CARDINALITY_SINGLE);
		meta.setProperty(KEY_TIMESTAMP_PUBLISH, PUBLISH_TIMESTAMP);
		meta.setProperty(KEY_TIMESTAMP_DELETE, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		meta.setProperty("/meta:authenticated-by[@ifmap-cardinality]", "singleValue");
		meta.setProperty("/meta:authenticated-by[@ifmap-publisher-id]", "test--1052611423-1");
		meta.setProperty("/meta:authenticated-by[@ifmap-timestamp]", "2012-11-12T11:27:17+01:00");
		meta.setProperty("/meta:authenticated-by[@xmlns:meta]", "http://www.trustedcomputinggroup.org/2010/IFMAP-METADATA/2");
		meta.setProperty(KEY_HASH, "a354ec5182fd4ee4a6fa523b2f181f03");

		link.createRelationshipTo(meta, LinkTypes.Meta);

		tx.success();
		tx.finish();

	}

	private void insertSingleIdentifierWithMetadataAndNewerTimestamp() {
		final long newTimetamp = PUBLISH_TIMESTAMP + 1;

		Transaction tx = mGraphDb.beginTx();

		Node id = mGraphDb.createNode();
		id.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_IDENTIFIER);
		id.setProperty(KEY_TYPE_NAME, "ip-address");
		id.setProperty(KEY_HASH, "00000000000000000000000000000000");
		id.setProperty("/ip-address[@value]", "10.1.1.1");

		Relationship r1 = mGraphDb.getReferenceNode().
				createRelationshipTo(id, LinkTypes.Creation);
		r1.setProperty(KEY_TIMESTAMP_PUBLISH, PUBLISH_TIMESTAMP + 1);

		Node meta = mGraphDb.createNode();
		meta.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_METADATA);
		meta.setProperty(KEY_TYPE_NAME, "event");
		meta.setProperty(KEY_META_CARDINALITY, VALUE_META_CARDINALITY_MULTI);
		meta.setProperty(KEY_TIMESTAMP_PUBLISH, newTimetamp);
		meta.setProperty(KEY_TIMESTAMP_DELETE, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
		meta.setProperty("/meta:event[@ifmap-cardinality]", "multiValue");
		meta.setProperty("/meta:event[@ifmap-publisher-id]", "test--1052611423-1");
		meta.setProperty("/meta:event[@ifmap-timestamp]", "2012-11-12T11:27:17+01:00");
		meta.setProperty("/meta:event[@xmlns:meta]", "http://www.trustedcomputinggroup.org/2010/IFMAP-METADATA/2");
		meta.setProperty(KEY_HASH, "00000000000000000000000000000000");

		id.createRelationshipTo(meta, LinkTypes.Meta);

		tx.success();
		tx.finish();

	}

	private void insertSingleIdentifierWithMetadataAndEndTimestamp() {
		final long endTimestamp = PUBLISH_TIMESTAMP + 1;

		Transaction tx = mGraphDb.beginTx();

		Node id = mGraphDb.createNode();
		id.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_IDENTIFIER);
		id.setProperty(KEY_TYPE_NAME, "ip-address");
		id.setProperty(KEY_HASH, "00000000000000000000000000000000");
		id.setProperty("/ip-address[@value]", "10.1.1.1");

		Relationship r1 = mGraphDb.getReferenceNode().
				createRelationshipTo(id, LinkTypes.Creation);
		r1.setProperty(KEY_TIMESTAMP_PUBLISH, PUBLISH_TIMESTAMP);

		Node meta = mGraphDb.createNode();
		meta.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_METADATA);
		meta.setProperty(KEY_TYPE_NAME, "event");
		meta.setProperty(KEY_META_CARDINALITY, VALUE_META_CARDINALITY_MULTI);
		meta.setProperty(KEY_TIMESTAMP_PUBLISH, PUBLISH_TIMESTAMP);
		meta.setProperty(KEY_TIMESTAMP_DELETE, endTimestamp);
		meta.setProperty("/meta:event[@ifmap-cardinality]", "multiValue");
		meta.setProperty("/meta:event[@ifmap-publisher-id]", "test--1052611423-1");
		meta.setProperty("/meta:event[@ifmap-timestamp]", "2012-11-12T11:27:17+01:00");
		meta.setProperty("/meta:event[@xmlns:meta]", "http://www.trustedcomputinggroup.org/2010/IFMAP-METADATA/2");
		meta.setProperty(KEY_HASH, "00000000000000000000000000000000");

		id.createRelationshipTo(meta, LinkTypes.Meta);

		tx.success();
		tx.finish();

	}

	@Test
	public void getCurrentStateShouldReturnTheRightNumberOfGraphs() {
		insertTwoIdentifierWithOneMetadata(); // single graph

		List<InternalIdentifierGraph> graphs = mReader.getCurrentState();
		assertEquals(1, graphs.size());
	}

	@Test
	public void getCurrentStateShouldReturnTheRightNumberOfIdentifiers() {
		insertTwoIdentifierWithOneMetadata();

		List<InternalIdentifierGraph> graphs = mReader.getCurrentState();
		List<InternalIdentifier> ids = new ArrayList<>();
		for (InternalIdentifierGraph g : graphs) {
			ids.addAll(g.getIdentifiers());
		}
		assertEquals(2, ids.size());
	}

	@Test
	public void getGraphAtShouldReturnTheRightNumberOfGraphs() {
		insertTwoIdentifierWithOneMetadata(); // single graph

		List<InternalIdentifierGraph> graphs = mReader.getGraphAt(PUBLISH_TIMESTAMP);
		assertEquals(1, graphs.size());
	}

	@Test
	public void getGraphAtShouldReturnEmptyGraphIfNoDataWasPresent() {
		insertTwoIdentifierWithOneMetadata(); // single graph

		List<InternalIdentifierGraph> graphs = mReader.getGraphAt(1);
		assertEquals(1, graphs.size());
		for (InternalIdentifierGraph g : graphs) {
			assertEquals(0, g.getIdentifiers().size());
		}
	}

	@Test
	public void getGraphAtShouldReturnTheDataUntilTheGivenTimestamp() {
		insertTwoIdentifierWithOneMetadata();

		List<InternalIdentifierGraph> graphs = mReader.getGraphAt(PUBLISH_TIMESTAMP);
		List<InternalIdentifier> ids = new ArrayList<>();
		for (InternalIdentifierGraph g : graphs) {
			ids.addAll(g.getIdentifiers());
		}
		assertEquals(2, ids.size());
	}

	@Test
	public void getGraphAtShouldNotReturnDataNewerThanTheGivenTimestamp() {
		insertTwoIdentifierWithOneMetadata();

		List<InternalIdentifierGraph> graphs = mReader.getGraphAt(PUBLISH_TIMESTAMP);
		List<InternalIdentifier> ids = new ArrayList<>();
		for (InternalIdentifierGraph g : graphs) {
			ids.addAll(g.getIdentifiers());
		}
		assertEquals(2, ids.size());

		insertSingleIdentifierWithMetadataAndNewerTimestamp();

		graphs = mReader.getGraphAt(PUBLISH_TIMESTAMP);
		ids.clear();
		for (InternalIdentifierGraph g : graphs) {
			ids.addAll(g.getIdentifiers());
		}
		assertEquals(2, ids.size());

		graphs = mReader.getGraphAt(PUBLISH_TIMESTAMP + 100);
		ids.clear();
		for (InternalIdentifierGraph g : graphs) {
			ids.addAll(g.getIdentifiers());
		}
		assertEquals(3, ids.size());
	}
	@Test
	public void getGraphAtShouldNotReturnOutdatedData() {
		insertTwoIdentifierWithOneMetadata();
		insertSingleIdentifierWithMetadataAndEndTimestamp();
		List<InternalIdentifierGraph> graphs = mReader.getGraphAt(PUBLISH_TIMESTAMP);
		List<InternalIdentifier> ids = new ArrayList<>();
		for (InternalIdentifierGraph g : graphs) {
			ids.addAll(g.getIdentifiers());
		}
		assertEquals(3, ids.size());

		graphs = mReader.getGraphAt(PUBLISH_TIMESTAMP+100);
		ids.clear();
		for (InternalIdentifierGraph g : graphs) {
			ids.addAll(g.getIdentifiers());
		}
		assertEquals(2, ids.size());

	}

}
