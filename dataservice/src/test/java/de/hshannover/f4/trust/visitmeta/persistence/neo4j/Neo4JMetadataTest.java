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
 * This file is part of visitmeta dataservice, version 0.0.7,
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

import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;

public class Neo4JMetadataTest {

	private GraphDatabaseService mGraphDb;
	private Neo4JRepository mGraph;

	private Node mRoleNode;

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
		mRoleNode = mGraphDb.createNode();

		mRoleNode.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_METADATA);
		mRoleNode.setProperty(KEY_TYPE_NAME, "role");
		mRoleNode.setProperty("/role/name", "admin");
		mRoleNode.setProperty("/role[@ifmap-timestamp]", "2010-04-20T12:00:05Z");
		mRoleNode.setProperty("/role[@ifmap-publisher-id]", "42");
		mRoleNode.setProperty("/role[@ifmap-cardinality]", "multiValue");

		tx.success();
		tx.finish();
	}

	@After
	public void destroyTestDatabase() {
		mGraphDb.shutdown();
	}

	@Test
	public void testEqualsTrueForNeo4jOnly() {
		Neo4JMetadata neo4jMeta1 = new Neo4JMetadata(mRoleNode, mGraph);
		Neo4JMetadata neo4jMeta2 = new Neo4JMetadata(mRoleNode, mGraph);
		assertEquals(neo4jMeta1, neo4jMeta2);
		assertEquals(neo4jMeta2, neo4jMeta1);

		assertEquals(neo4jMeta1, neo4jMeta1);
		assertEquals(neo4jMeta2, neo4jMeta2);
	}

	@Test
	public void testEqualsTrueForMixed() {
		Neo4JMetadata neo4jMeta = new Neo4JMetadata(mRoleNode, mGraph);
		InternalMetadataStub internalMeta = new InternalMetadataStub();
		internalMeta.typename = "role";
		internalMeta.isSingleValue = false;
		internalMeta.properties.put("/role/name", "admin");
		internalMeta.properties.put("/role[@ifmap-timestamp]", "2010-04-20T12:00:05Z");
		internalMeta.properties.put("/role[@ifmap-publisher-id]", "42");
		internalMeta.properties.put("/role[@ifmap-cardinality]", "multiValue");

		assertTrue(neo4jMeta.equals(internalMeta));
	}

	@Test
	public void testEqualsFalseForNeo4jOnly() {
		Transaction tx = mGraphDb.beginTx();
		Node node = mGraphDb.createNode();
		node.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_METADATA);
		node.setProperty(KEY_TYPE_NAME, "role");
		node.setProperty("/role/name", "chef");
		node.setProperty("/role[@ifmap-cardinality]", "multiValue");
		tx.success();
		tx.finish();

		Neo4JMetadata neo4jMeta1 = new Neo4JMetadata(mRoleNode, mGraph);
		Neo4JMetadata neo4jMeta2 = new Neo4JMetadata(node, mGraph);
		assertFalse(neo4jMeta1.equals(neo4jMeta2));
		assertFalse(neo4jMeta2.equals(neo4jMeta1));
	}

	@Test
	public void testEqualsFalseForMixed() {
		Neo4JMetadata neo4jMeta = new Neo4JMetadata(mRoleNode, mGraph);
		InternalMetadataStub internalMeta = new InternalMetadataStub();
		internalMeta.typename = "role";
		internalMeta.isSingleValue = false;
		internalMeta.properties.put("/role/name", "chef");
		internalMeta.properties.put("/role[@ifmap-timestamp]", "2010-04-20T12:00:05Z");
		internalMeta.properties.put("/role[@ifmap-publisher-id]", "42");
		internalMeta.properties.put("/role[@ifmap-cardinality]", "multiValue");

		assertFalse(neo4jMeta.equals(internalMeta));
	}

	private class InternalMetadataStub extends InternalMetadata {

		public String typename;
		public boolean isSingleValue;
		public Map<String, String> properties = new HashMap<>();

		@Override
		public boolean isSingleValue() {
			return isSingleValue;
		}

		@Override
		public long getPublishTimestamp() {
			return 0;
		}

		@Override
		public long getDeleteTimestamp() {
			return 0;
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
		public void addProperty(String name, String value) {
			throw new UnsupportedOperationException("not implemented in test stub");
		}

		@Override
		public void setPublishTimestamp(long timestamp) {
			throw new UnsupportedOperationException("not implemented in test stub");
		}

		@Override
		public String getRawData() {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
