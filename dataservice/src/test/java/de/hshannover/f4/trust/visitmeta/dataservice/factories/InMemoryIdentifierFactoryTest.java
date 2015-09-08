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
 * This file is part of visitmeta-dataservice, version 0.5.2,
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

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;

import static org.junit.Assert.*;

public class InMemoryIdentifierFactoryTest {

	private DocumentBuilder mBuilder;

	@Before
	public void setup() throws Exception {
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		f.setNamespaceAware(true);
		mBuilder = f.newDocumentBuilder();
	}

	@Test
	public void theFactoryShouldCreateIdentifierWithTypenameFromDocuments() {
		InternalIdentifierFactory factory = new InMemoryIdentifierFactory();
		Document document = createIpv4AddressDocument("10.1.1.1");

		InternalIdentifier id = factory.createIdentifier(document);
		assertEquals("ip-address", id.getTypeName());
	}

	@Test
	public void theFactoryShouldCreateIdentifierWithRightNumberOfPropertiesFromDocuments() {
		InternalIdentifierFactory factory = new InMemoryIdentifierFactory();
		Document document = createIpv4AddressDocument("10.1.1.1");

		InternalIdentifier id = factory.createIdentifier(document);
		assertEquals(2, id.getProperties().size());
	}

	@Test
	public void theFactoryShouldCreateIdentifierWithRightPropertiesFromDocuments() {
		InternalIdentifierFactory factory = new InMemoryIdentifierFactory();
		Document document = createIpv4AddressDocument("10.1.1.1");

		InternalIdentifier id = factory.createIdentifier(document);
		List<String> properties = id.getProperties();
		assertTrue(properties.contains("/ip-address[@value]"));
		assertTrue(properties.contains("/ip-address[@type]"));
	}

	@Test
	public void theFactoryShouldCreateIdentifierWithRightValuesFromDocuments() {
		InternalIdentifierFactory factory = new InMemoryIdentifierFactory();
		Document document = createIpv4AddressDocument("10.1.1.1");

		InternalIdentifier id = factory.createIdentifier(document);
		assertEquals(id.valueFor("/ip-address[@value]"), "10.1.1.1");
		assertEquals(id.valueFor("/ip-address[@type]"), "IPv4");
	}

	private Document createIpv4AddressDocument(String value) {
		de.hshannover.f4.trust.ifmapj.identifier.Identifier id = Identifiers.createIp4("10.1.1.1");
		Document document = mBuilder.newDocument();
		Element element = Identifiers.tryToElement(id, document);
		document.appendChild(element);
		return document;
	}

}
