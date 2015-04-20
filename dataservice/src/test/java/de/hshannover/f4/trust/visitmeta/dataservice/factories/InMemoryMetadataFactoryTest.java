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

package de.hshannover.f4.trust.visitmeta.dataservice.factories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import de.hshannover.f4.trust.ifmapj.IfmapJ;
import de.hshannover.f4.trust.ifmapj.metadata.EventType;
import de.hshannover.f4.trust.ifmapj.metadata.Significance;
import de.hshannover.f4.trust.ifmapj.metadata.StandardIfmapMetadataFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;

public class InMemoryMetadataFactoryTest {

	private StandardIfmapMetadataFactory mMetadataFactory;
	private DocumentBuilder mBuilder;

	@Before
	public void setup() throws Exception {
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		f.setNamespaceAware(true);
		mBuilder = f.newDocumentBuilder();

		mMetadataFactory = IfmapJ.createStandardMetadataFactory();
	}

	@Test
	public void theFactoryShouldCreateMetadataWithTypenameFromDocuments() {
		InternalMetadataFactory factory = new InMemoryMetadataFactory();
		Document document = createEventDocument();

		InternalMetadata meta = factory.createMetadata(document);
		assertEquals("event", meta.getTypeName());
	}

	@Test
	public void theFactoryShouldCreateMetadataWithRightNumberOfPropertiesFromDocuments() {
		InternalMetadataFactory factory = new InMemoryMetadataFactory();
		Document document = createEventDocument();

		InternalMetadata meta = factory.createMetadata(document);
		assertEquals(11, meta.getProperties().size());
	}

	@Test
	public void theFactoryShouldCreateMetadataWithRightPropertiesFromDocuments() {
		InternalMetadataFactory factory = new InMemoryMetadataFactory();
		Document document = createEventDocument();

		InternalMetadata meta = factory.createMetadata(document);
		List<String> properties = meta.getProperties();
		assertTrue(properties.contains("/meta:event/discovered-time"));
		assertTrue(properties.contains("/meta:event[@ifmap-cardinality]"));
		assertTrue(properties.contains("/meta:event/other-type-definition"));
		assertTrue(properties.contains("/meta:event/vulnerability-uri"));
		assertTrue(properties.contains("/meta:event/type"));
		assertTrue(properties.contains("/meta:event/confidence"));
		assertTrue(properties.contains("/meta:event/magnitude"));
		assertTrue(properties.contains("/meta:event/information"));
		assertTrue(properties.contains("/meta:event/discoverer-id"));
		assertTrue(properties.contains("/meta:event/name"));
		assertTrue(properties.contains("/meta:event/significance"));
	}

	@Test
	public void theFactoryShouldCreateMetadataWithRightValuesFromDocuments() {
		InternalMetadataFactory factory = new InMemoryMetadataFactory();
		Document document = createEventDocument();

		InternalMetadata meta = factory.createMetadata(document);
		assertEquals(meta.valueFor("/meta:event/discovered-time"), "2010-04-21T16:11:09Z");
		assertEquals(meta.valueFor("/meta:event[@ifmap-cardinality]"), "multiValue");
		assertEquals(meta.valueFor("/meta:event/other-type-definition"), "");
		assertEquals(meta.valueFor("/meta:event/vulnerability-uri"), "CVE-2011-0997");
		assertEquals(meta.valueFor("/meta:event/type"), "cve");
		assertEquals(meta.valueFor("/meta:event/confidence"), "1");
		assertEquals(meta.valueFor("/meta:event/magnitude"), "1");
		assertEquals(meta.valueFor("/meta:event/information"), "42");
		assertEquals(meta.valueFor("/meta:event/discoverer-id"), "discovererId");
		assertEquals(meta.valueFor("/meta:event/name"), "event42");
		assertEquals(meta.valueFor("/meta:event/significance"), "critical");
	}

	private Document createEventDocument() {
		return mMetadataFactory.createEvent(
				"event42",
				"2010-04-21T16:11:09Z",
				"discovererId",
				1,
				1,
				Significance.critical,
				EventType.cve,
				"",
				"42",
				"CVE-2011-0997");
	}

}
