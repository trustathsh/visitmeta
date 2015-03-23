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
 * This file is part of visitmeta-dataservice, version 0.3.0,
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

package de.hshannover.f4.trust.visitmeta.dataservice;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;

public class InternalIdentifierTest {

	@Test
	public void isValidShouldReturnFalseIfLinksIsEmptyAndNoMetadataIsValid() {
		long timestamp = 42;
		InternalMetadata meta1 = mock(InternalMetadata.class);
		when(meta1.isValidAt(timestamp)).thenReturn(false);
		InternalMetadata meta2 = mock(InternalMetadata.class);
		when(meta2.isValidAt(timestamp)).thenReturn(false);

		InternalIdentifierStub withoutLink = new InternalIdentifierStub();
		withoutLink.metadata = Arrays.asList(meta1, meta2);
		withoutLink.links = new ArrayList<>();

		assertFalse(withoutLink.isValidAt(timestamp));
	}

	@Test
	public void isValidShouldReturnTrueIfLinksIsEmptyAndSomeMetadataIsValid() {
		long timestamp = 42;
		InternalMetadata notValidMeta = mock(InternalMetadata.class);
		when(notValidMeta.isValidAt(timestamp)).thenReturn(false);
		InternalMetadata validMeta = mock(InternalMetadata.class);
		when(validMeta.isValidAt(timestamp)).thenReturn(true);

		InternalIdentifierStub withoutLink = new InternalIdentifierStub();
		withoutLink.metadata = Arrays.asList(notValidMeta, validMeta);
		withoutLink.links = new ArrayList<>();

		assertTrue(withoutLink.isValidAt(timestamp));
	}

	@Test
	public void isValidShouldReturnFalseIfMetadataIsEmptyAndNoLinkIsValid() {
		long timestamp = 42;
		InternalMetadata notValid = mock(InternalMetadata.class);
		when(notValid.isValidAt(timestamp)).thenReturn(false);

		List<InternalMetadata> meta = Arrays.asList(notValid);
		InternalLink link = mock(InternalLink.class);
		when(link.getMetadata()).thenReturn(meta);

		InternalIdentifierStub withoutMeta = new InternalIdentifierStub();
		withoutMeta.metadata = new ArrayList<>();
		withoutMeta.links = Arrays.asList(link);

		assertFalse(withoutMeta.isValidAt(timestamp));
	}

	@Test
	public void isValidShouldReturnTrueIfMetadataIsEmptyAndSomeLinkIsValid() {
		long timestamp = 42;
		InternalMetadata notValid = mock(InternalMetadata.class);
		when(notValid.isValidAt(timestamp)).thenReturn(true);

		List<InternalMetadata> meta = Arrays.asList(notValid);
		InternalLink link = mock(InternalLink.class);
		when(link.getMetadata()).thenReturn(meta);

		InternalIdentifierStub withoutMeta = new InternalIdentifierStub();
		withoutMeta.metadata = new ArrayList<>();
		withoutMeta.links = Arrays.asList(link);

		assertTrue(withoutMeta.isValidAt(timestamp));
	}

	@Test
	public void equalsShouldReturnTrueForSameObject() {
		InternalIdentifierStub id = new InternalIdentifierStub();
		assertEquals(id, id);
	}

	@Test
	public void equalsShouldReturnTrueForSameTypname() {
		InternalIdentifierStub id1 = new InternalIdentifierStub();
		InternalIdentifierStub id2 = new InternalIdentifierStub();

		id1.typename = "ip-address";
		id2.typename = "ip-address";

		assertEquals(id1, id2);
		assertEquals(id2, id1);
	}

	@Test
	public void equalsShouldReturnTrueForSameProperties() {
		InternalIdentifierStub id1 = new InternalIdentifierStub();
		InternalIdentifierStub id2 = new InternalIdentifierStub();

		id1.properties.put("hello", "world");
		id2.properties.put("hello", "world");

		assertEquals(id1, id2);
		assertEquals(id2, id1);
	}

	@Test
	public void equalsShouldReturnFalseForDifferentTypname() {
		InternalIdentifierStub id1 = new InternalIdentifierStub();
		InternalIdentifierStub id2 = new InternalIdentifierStub();

		id1.typename = "access-request";
		id2.typename = "ip-address";

		assertFalse(id1.equals(id2));
		assertFalse(id2.equals(id1));
	}

	@Test
	public void equalsShouldReturnFalseForDifferentProperties() {
		InternalIdentifierStub id1 = new InternalIdentifierStub();
		InternalIdentifierStub id2 = new InternalIdentifierStub();

		// size mismatch
		id1.properties.put("hello", "world");
		assertFalse(id1.equals(id2));
		assertFalse(id2.equals(id1));

		// content mismatch
		id2.properties.put("21", "42");
		assertFalse(id1.equals(id2));
		assertFalse(id2.equals(id1));
	}

	@Test
	public void hashCodeShouldReturnSameValueForEqualIdentifier() {
		InternalIdentifierStub id1 = new InternalIdentifierStub();
		InternalIdentifierStub id2 = new InternalIdentifierStub();

		assertEquals(id1.hashCode(), id2.hashCode());

		id1.typename = "ip-address";
		id2.typename = "ip-address";

		assertEquals(id1.hashCode(), id2.hashCode());

		id1.properties.put("hello", "world");
		id2.properties.put("hello", "world");

		assertEquals(id1.hashCode(), id2.hashCode());
	}

	@Test
	public void hashCodeShouldReturnDifferentValueForUnequalIdentifier() {
		InternalIdentifierStub id1 = new InternalIdentifierStub();
		InternalIdentifierStub id2 = new InternalIdentifierStub();

		id1.typename = "mac-address";
		id2.typename = "ip-address";

		assertNotSame(id1.hashCode(), id2.hashCode());

		id2.typename = "mac-address";

		id1.properties.put("hello", "world");
		id2.properties.put("hello", "42");

		assertNotSame(id1.hashCode(), id2.hashCode());
	}
}
