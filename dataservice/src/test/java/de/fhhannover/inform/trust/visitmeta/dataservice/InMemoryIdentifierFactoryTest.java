package de.fhhannover.inform.trust.visitmeta.dataservice;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.fhhannover.inform.trust.ifmapj.identifier.Identifiers;
import de.fhhannover.inform.trust.ifmapj.identifier.Identity;
import de.fhhannover.inform.trust.ifmapj.identifier.IdentityType;
import de.fhhannover.inform.trust.ifmapj.identifier.IpAddress;
import de.fhhannover.inform.trust.visitmeta.dataservice.factories.InMemoryIdentifierFactory;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.fhhannover.inform.trust.visitmeta.dataservice.xml.DomHelper;

import static org.junit.Assert.*;

public class InMemoryIdentifierFactoryTest {

	private InMemoryIdentifierFactory mIdentifierFactory;
	private IpAddress mIpAddress;
	private Document mIpAddressDocument;

	private Identity mIdentity;
	private Document mIdentityDocument;

	@Before
	public void setup() throws Exception {
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		f.setNamespaceAware(true);
		DocumentBuilder builder = f.newDocumentBuilder();
		mIdentifierFactory = new InMemoryIdentifierFactory();

		mIpAddress = Identifiers.createIp4("10.1.1.1");
		mIpAddressDocument = builder.newDocument();
		Element e1 = Identifiers.tryToElement(mIpAddress, mIpAddressDocument);
		mIpAddressDocument.appendChild(e1);

		mIdentity = Identifiers.createIdentity(
				IdentityType.other, "smartphone.os.android", "smartphone:01", "32939:category");
		mIdentityDocument = builder.newDocument();
		Element e2 = Identifiers.tryToElement(mIdentity, mIdentityDocument);
		mIdentityDocument.appendChild(e2);
	}

	@Test
	public void testCreateIpIdentifierFromDocument() {
		InternalIdentifier ip = mIdentifierFactory.createIdentifier(mIpAddressDocument);
		assertEquals("ip-address", ip.getTypeName());
	}

	@Test
	public void testCreateIdentityIdentifierFromDocument() {
		InternalIdentifier id = mIdentifierFactory.createIdentifier(mIdentityDocument);
		assertEquals("identity", id.getTypeName());
	}

}
