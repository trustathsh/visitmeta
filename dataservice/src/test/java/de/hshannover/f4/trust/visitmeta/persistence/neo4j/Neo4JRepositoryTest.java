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
 * This file is part of visitmeta-dataservice, version 0.4.0,
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

package de.hshannover.f4.trust.visitmeta.persistence.neo4j;

import java.security.MessageDigest;

import org.junit.Test;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.persistence.inmemory.InMemoryIdentifier;
import de.hshannover.f4.trust.visitmeta.persistence.inmemory.InMemoryMetadata;

import static org.junit.Assert.*;

public class Neo4JRepositoryTest {

	@Test
	public void samePropablesShouldHaveSameHash() throws Exception {
		Neo4JRepository repo = new Neo4JRepository(null, MessageDigest.getInstance("MD5"));

		InternalIdentifier p1 = new InMemoryIdentifier("ip-address");
		InternalIdentifier p2 = new InMemoryIdentifier("ip-address");
		p1.addProperty("key1", "value1");
		p1.addProperty("key2", "value2");
		p2.addProperty("key1", "value1");
		p2.addProperty("key2", "value2");

		assertEquals(repo.calcHash(p1), repo.calcHash(p2));
	}

	@Test
	public void differentPropablesShouldHaveDifferentHash() throws Exception {
		Neo4JRepository repo = new Neo4JRepository(null, MessageDigest.getInstance("MD5"));

		InternalIdentifier p1 = new InMemoryIdentifier("ip-address");
		InternalIdentifier p2 = new InMemoryIdentifier("ip-address");
		p1.addProperty("key1", "value1");
		p1.addProperty("key2", "value2");
		p2.addProperty("abc", "xyz");
		p2.addProperty("42", "42");

		assertNotSame(repo.calcHash(p1), repo.calcHash(p2));
	}

	@Test
	public void differentTypesWithSamePropertiesShouldHaveDifferentHash()
			throws Exception {
		Neo4JRepository repo = new Neo4JRepository(null, MessageDigest.getInstance("MD5"));

		InternalIdentifier p1 = new InMemoryIdentifier("ip-address");
		InternalMetadata p2 = new InMemoryMetadata("ip-address", true, 42);
		p1.addProperty("key1", "value1");
		p1.addProperty("key2", "value2");
		p2.addProperty("key1", "value1");
		p2.addProperty("key2", "value2");

		assertNotSame(repo.calcHash(p1), repo.calcHash(p2));
	}

	@Test
	public void hashStringsShouldHaveTheCorrectHexLength() throws Exception {
		int md5HexLength  = 32;
		int sha1HexLength = 40;

		Neo4JRepository repoMd5  = new Neo4JRepository(null, MessageDigest.getInstance("MD5"));
		Neo4JRepository repoSha1 = new Neo4JRepository(null, MessageDigest.getInstance("SHA1"));

		InternalIdentifier p1 = new InMemoryIdentifier("ip-address");
		p1.addProperty("key1", "value1");
		p1.addProperty("key2", "value2");

		assertEquals(md5HexLength, repoMd5.calcHash(p1).length());
		assertEquals(sha1HexLength, repoSha1.calcHash(p1).length());
	}

}
