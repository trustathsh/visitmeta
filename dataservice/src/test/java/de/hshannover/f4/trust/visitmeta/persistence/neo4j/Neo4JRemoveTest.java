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

package de.hshannover.f4.trust.visitmeta.persistence.neo4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import de.hshannover.f4.trust.visitmeta.persistence.inmemory.InMemoryIdentifier;
import de.hshannover.f4.trust.visitmeta.persistence.inmemory.InMemoryMetadata;

// TODO align tests with new 'remove semantics': check for correct 'timestamp.end' values
public class Neo4JRemoveTest {

	private GraphDatabaseService mGraphDb;
	private Neo4JRepository mRepo;

	private Neo4JLink l1;
	private Neo4JIdentifier id1, id2;
	private Neo4JMetadata m1, m2, m3;

	private InMemoryIdentifier mIdentityIdent, mARIdent;
	private InMemoryMetadata mAuthAsMeta, mRoleMeta, mEventMeta;

	@Before
	public void prepareTestDatabase() throws Exception {
		mGraphDb =
				new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder().newGraphDatabase();

		Neo4JConnection neo4jConnection = mock(Neo4JConnection.class);
		when(neo4jConnection.getConnection()).thenReturn(mGraphDb);

		mRepo = new Neo4JRepository(neo4jConnection, MessageDigest.getInstance("MD5"));
		insertTestData();
	}

	private void insertTestData() {
		Transaction tx = mGraphDb.beginTx();

		mIdentityIdent = new InMemoryIdentifier("identity");
		mIdentityIdent.addProperty("/identity/name", "John Smith");

		mARIdent =  new InMemoryIdentifier("access-request");
		mARIdent.addProperty("/access-request/name", "111:33");

		mAuthAsMeta = new InMemoryMetadata("authenticated-as", true, 42);

		mRoleMeta = new InMemoryMetadata("role", true, 42);
		mRoleMeta.addProperty("/role/name", "admin");

		mEventMeta = new InMemoryMetadata("event", false, 42);
		mEventMeta.addProperty("/event/name", "xyz");
		mEventMeta.addProperty("/event/cve", "34235");

		id1 = (Neo4JIdentifier) mRepo.insert(mIdentityIdent);
		id2 = (Neo4JIdentifier) mRepo.insert(mARIdent);
		l1 = (Neo4JLink) mRepo.connect(id1, id2);

		m1 = (Neo4JMetadata) mRepo.insert(mAuthAsMeta);
		m2 = (Neo4JMetadata) mRepo.insert(mRoleMeta);
		m3 = (Neo4JMetadata) mRepo.insert(mEventMeta);

		mRepo.connectMeta(l1, m1);
		mRepo.connectMeta(l1, m2);
		mRepo.connectMeta(id1, m3);

		// TODO insert hash property

		tx.success();
		tx.finish();
	}

	@After
	public void destroyTestDatabase() {
		mGraphDb.shutdown();
	}

	@Ignore("fails because of new 'remove semantics'")
	@Test
	public void testDeleteMetadata() {
		mRepo.getMetadata(m3.getNode().getId());
		mRepo.remove(m3.getNode().getId());
		try {
			mRepo.getMetadata(m3.getNode().getId());
			assertFalse(true);
		} catch(Exception e) {
			assertEquals(NotFoundException.class, e.getCause().getClass());
		}
	}

	@Ignore("fails because of new 'remove semantics'")
	@Test
	public void testDeleteLink() {
		mRepo.getLink(l1.getNode().getId());
		mRepo.getMetadata(m1.getNode().getId());
		mRepo.getMetadata(m2.getNode().getId());

		mRepo.remove(l1.getNode().getId());

		try {
			mRepo.getLink(l1.getNode().getId());
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(NotFoundException.class, e.getCause().getClass());
		}
		try {
			mRepo.getMetadata(m1.getNode().getId());
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(NotFoundException.class, e.getCause().getClass());
		}
		try {
			mRepo.getMetadata(m2.getNode().getId());
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(NotFoundException.class, e.getCause().getClass());
		}

	}

	@Ignore("fails because of new 'remove semantics'")
	@Test
	public void testDeleteIdentifier() {
		Iterator<Relationship> it = mRepo.getRoot().getRelationships(LinkTypes.Creation).iterator();
		assertEquals(id2.getTypeName(), it.next().getEndNode().getProperty(Neo4JPropertyConstants.KEY_TYPE_NAME));
		assertEquals(id1.getTypeName(), it.next().getEndNode().getProperty(Neo4JPropertyConstants.KEY_TYPE_NAME));
		mRepo.getIdentifier(id1.getNode().getId());
		mRepo.getMetadata(m3.getNode().getId());
		mRepo.getLink(l1.getNode().getId());
		mRepo.getMetadata(m1.getNode().getId());
		mRepo.getMetadata(m2.getNode().getId());

		mRepo.remove(id1.getNode().getId());

		Iterator<Relationship> it2 = mRepo.getRoot().getRelationships(LinkTypes.Creation).iterator();
		assertEquals(id2.getTypeName(), it2.next().getEndNode().getProperty(Neo4JPropertyConstants.KEY_TYPE_NAME));
		try {
			it2.next();
			assertFalse(true);
		} catch(Exception e) {
			assertEquals(NoSuchElementException.class, e.getClass());
		}
		try {
			mRepo.getIdentifier(id1.getNode().getId());
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(NotFoundException.class, e.getCause().getClass());
		}
		try {
			mRepo.getMetadata(m3.getNode().getId());
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(NotFoundException.class, e.getCause().getClass());
		}
		try {
			mRepo.getLink(l1.getNode().getId());
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(NotFoundException.class, e.getCause().getClass());
		}
		try {
			mRepo.getMetadata(m1.getNode().getId());
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(NotFoundException.class, e.getCause().getClass());
		}
		try {
			mRepo.getMetadata(m2.getNode().getId());
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(NotFoundException.class, e.getCause().getClass());
		}
	}

}
