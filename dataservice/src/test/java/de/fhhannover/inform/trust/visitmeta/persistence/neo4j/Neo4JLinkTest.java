package de.fhhannover.inform.trust.visitmeta.persistence.neo4j;

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

import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TYPE_NAME;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.NODE_TYPE_KEY;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_TYPE_NAME_IDENTIFIER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.MessageDigest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.fhhannover.inform.trust.visitmeta.persistence.inmemory.InMemoryIdentifier;
import de.fhhannover.inform.trust.visitmeta.persistence.inmemory.InMemoryLink;
import de.fhhannover.inform.trust.visitmeta.persistence.inmemory.InMemoryMetadata;

public class Neo4JLinkTest {
	private GraphDatabaseService mGraphDb;
	private Neo4JRepository mRepo;

	private Node mLinkNode, mIpNode, mMacNode;
	private Neo4JIdentifier mIpID, mMacID;
	private Neo4JLink mLink;

	@Before
	public void prepareTestDatabase() throws Exception {
		mGraphDb = new TestGraphDatabaseFactory()
				.newImpermanentDatabaseBuilder().newGraphDatabase();

		Neo4JConnection neo4jConnection = mock(Neo4JConnection.class);
		when(neo4jConnection.getConnection()).thenReturn(mGraphDb);

		mRepo = new Neo4JRepository(neo4jConnection,
				MessageDigest.getInstance("MD5"));
		insertTestData();
	}

	private void insertTestData() {
		Transaction tx = mGraphDb.beginTx();
		mIpNode = mGraphDb.createNode();
		mMacNode = mGraphDb.createNode();

		mIpNode.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_IDENTIFIER);
		mIpNode.setProperty(KEY_TYPE_NAME, "ip-address");
		mIpNode.setProperty("/ip-address/value", "10.1.1.1");
		mIpNode.setProperty("/ip-address/type", "IPv4");
		
		mMacNode.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_IDENTIFIER);
		mMacNode.setProperty(KEY_TYPE_NAME, "mac-address");
		mMacNode.setProperty("/mac-address/value", "ee:ee:ee:ee:ee:ee");
		
		mIpID= new Neo4JIdentifier(mIpNode, mRepo);
		mMacID = new Neo4JIdentifier(mMacNode, mRepo);
		mLink = (Neo4JLink) mRepo.connect(mIpID, mMacID);
		mLinkNode = mLink.getNode();
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
		Neo4JLink neo4jL1 = new Neo4JLink(mLinkNode, mRepo);
		Neo4JLink neo4jL2 = new Neo4JLink(mLinkNode, mRepo);
		assertEquals(neo4jL1, neo4jL2);
		assertEquals(neo4jL2, neo4jL1);
		assertEquals(mLink, neo4jL1);
		assertEquals(neo4jL1, mLink);
		assertEquals(neo4jL1.hashCode(), neo4jL2.hashCode());
	}
	
	@Test
	public void testEqualsTrueForInMemory() {
		InMemoryLink imL1 = new InMemoryLink(mIpID, mMacID);
		Neo4JLink neo4jL1 = new Neo4JLink(mLinkNode, mRepo);
		assertEquals(neo4jL1, imL1);
		assertEquals(imL1, neo4jL1);
		assertEquals(mLink, imL1);
		assertEquals(imL1, mLink);
		assertEquals(neo4jL1.hashCode(), imL1.hashCode());
	}

	// TODO testEqualsTrueForMixed (InternalLinkStub?!)
	// TODO testEqualsFalseFor[Neo4jOnly,InMemory, Mixed]
	// TODO test transitivity: 1 equlas 2, 2 equals 3 => 1 equlas 3

}