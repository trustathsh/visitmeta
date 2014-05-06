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
 * This file is part of visitmeta dataservice, version 0.0.3,
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

package de.hshannover.f4.trust.visitmeta.persistence.neo4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.*;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.persistence.inmemory.InMemoryIdentifier;

public class Neo4JIdentifierTest {

	private GraphDatabaseService mGraphDb;
	private Neo4JRepository mGraph;

	private Node mIpNode;

	@Before
	public void prepareTestDatabase() throws Exception {
		mGraphDb =
				new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder().newGraphDatabase();

		Neo4JConnection neo4jConnection = mock(Neo4JConnection.class);
		when(neo4jConnection.getConnection()).thenReturn(mGraphDb);

		mGraph = new Neo4JRepository(neo4jConnection, MessageDigest.getInstance("MD5"));
		insertTestData();
	}

	private void insertTestData() {
		Transaction tx = mGraphDb.beginTx();
		mIpNode = mGraphDb.createNode();

		mIpNode.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_IDENTIFIER);
		mIpNode.setProperty(KEY_TYPE_NAME, "ip-address");
		mIpNode.setProperty("/ip-address/value", "10.1.1.1");
		mIpNode.setProperty("/ip-address/type", "IPv4");

		// TODO insert hash property

		tx.success();
		tx.finish();
	}

	@After
	public void destroyTestDatabase() {
		mGraphDb.shutdown();
	}

	@Test
	public void testEqualsTrueForNeo4jOnly() {
		Neo4JIdentifier neo4jId1 = new Neo4JIdentifier(mIpNode, mGraph);
		Neo4JIdentifier neo4jId2 = new Neo4JIdentifier(mIpNode, mGraph);
		assertEquals(neo4jId1, neo4jId2);
		assertEquals(neo4jId2, neo4jId1);
		assertEquals(neo4jId1, neo4jId1);
		assertEquals(neo4jId2, neo4jId2);

		assertEquals(neo4jId1.hashCode(), neo4jId2.hashCode());
		assertEquals(neo4jId2.hashCode(), neo4jId1.hashCode());
		assertEquals(neo4jId1.hashCode(), neo4jId1.hashCode());
		assertEquals(neo4jId2.hashCode(), neo4jId2.hashCode());
	}

	// TODO test transitivity: 1 equlas 2, 2 equals 3 => 1 equlas 3

	@Test
	public void testEqualsTrueForMixed() {
		Neo4JIdentifier neo4jId = new Neo4JIdentifier(mIpNode, mGraph);
		InternalIdentifierStub internalId = new InternalIdentifierStub();
		internalId.typename = "ip-address";
		internalId.properties.put("/ip-address/value", "10.1.1.1");
		internalId.properties.put("/ip-address/type", "IPv4");

		assertTrue(neo4jId.equals(internalId));
	}

	@Test
	public void testEqualsTrueForInMemory() {
		Neo4JIdentifier neo4jId = new Neo4JIdentifier(mIpNode, mGraph);
		InMemoryIdentifier memory = new InMemoryIdentifier(neo4jId);

		assertTrue(neo4jId.equals(memory));
		assertTrue(memory.equals(neo4jId));
		assertEquals(neo4jId.hashCode(), memory.hashCode());
		assertEquals(memory.hashCode(), neo4jId.hashCode());
	}

	@Test
	public void testEqualsFalseForInMemory() {
		Neo4JIdentifier neo4jId = new Neo4JIdentifier(mIpNode, mGraph);
		InMemoryIdentifier memory = new InMemoryIdentifier(neo4jId);
		memory.addProperty("dummy", "42");

		assertFalse(neo4jId.equals(memory));
		assertFalse(memory.equals(neo4jId));
		assertNotSame(memory.hashCode(), neo4jId.hashCode());
		assertNotSame(neo4jId.hashCode(), memory.hashCode());
	}

	@Test
	public void testEqualsFalseForNeo4jOnly() {
		Transaction tx = mGraphDb.beginTx();
		Node node = mGraphDb.createNode();
		node.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_IDENTIFIER);
		node.setProperty(KEY_TYPE_NAME, "ip-address");
		node.setProperty("/ip-address/value", "10.1.1.99");
		node.setProperty("/ip-address/type", "IPv4");
		tx.success();
		tx.finish();

		Neo4JIdentifier neo4jId1 = new Neo4JIdentifier(mIpNode, mGraph);
		Neo4JIdentifier neo4jId2 = new Neo4JIdentifier(node, mGraph);
		assertFalse(neo4jId1.equals(neo4jId2));
		assertFalse(neo4jId2.equals(neo4jId1));
		assertNotSame(neo4jId1.hashCode(), neo4jId2.hashCode());
		assertNotSame(neo4jId2.hashCode(), neo4jId1.hashCode());

	}

	@Test
	public void testEqualsFalseForMixed() {
		Neo4JIdentifier neo4jId = new Neo4JIdentifier(mIpNode, mGraph);
		InternalIdentifierStub internalId = new InternalIdentifierStub();
		internalId.typename = "ip-address";
		internalId.properties.put("/ip-address/value", "10.1.1.99");
		internalId.properties.put("/ip-address/type", "IPv4");

		assertFalse(neo4jId.equals(internalId));
	}

	private class InternalIdentifierStub extends InternalIdentifier {

		public String typename;
		public Map<String, String> properties = new HashMap<>();

		@Override
		public List<InternalLink> getLinks() {
			throw new UnsupportedOperationException("not implemented in test stub");
		}

		@Override
		public List<InternalMetadata> getMetadata() {
			throw new UnsupportedOperationException("not implemented in test stub");
		}

		@Override
		public List<String> getProperties() {
			return new ArrayList<>(properties.keySet());
		}

		@Override
		public boolean hasProperty(String p) {
			return properties.containsKey(p);
		}

		@Override
		public String valueFor(String p) {
			return properties.get(p);
		}

		@Override
		public String getTypeName() {
			return typename;
		}

		@Override
		public void addMetadata(InternalMetadata meta) {
			throw new UnsupportedOperationException("not implemented in test stub");
		}

		@Override
		public void clearMetadata() {
			throw new UnsupportedOperationException("not implemented in test stub");
		}

		@Override
		public void removeMetadata(InternalMetadata meta) {
			throw new UnsupportedOperationException("not implemented in test stub");
		}
		
		@Override
		public void updateMetadata(InternalMetadata meta) {
			throw new UnsupportedOperationException("not implemented in test stub");
		}

		@Override
		public boolean hasMetadata(InternalMetadata meta) {
			throw new UnsupportedOperationException("not implemented in test stub");
		}

		@Override
		public void clearLinks() {
			throw new UnsupportedOperationException("not implemented in test stub");
		}

		@Override
		public void removeLink(InternalLink l) {
			throw new UnsupportedOperationException("not implemented in test stub");
		}

		@Override
		public void addProperty(String name, String value) {
			// TODO Auto-generated method stub
		}

		@Override
		public String getRawData() {
			// TODO Auto-generated method stub
			return null;
		}

	}
}