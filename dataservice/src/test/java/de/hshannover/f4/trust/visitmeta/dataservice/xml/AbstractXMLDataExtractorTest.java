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
 * This file is part of visitmeta-dataservice, version 0.2.0,
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

package de.hshannover.f4.trust.visitmeta.dataservice.xml;

import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.*;

public class AbstractXMLDataExtractorTest {

	class AbstractXMLDataExtractorStub extends AbstractXMLDataExtractor {

		@Override
		public Map<String, String> extractToKeyValuePairs(Document document) {
			throw new UnsupportedOperationException();
		}
	}

	private DocumentBuilder mBuilder;
	private AbstractXMLDataExtractor mExtractor;

	@Before
	public void setup() throws Exception {
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		f.setNamespaceAware(true);
		mBuilder = f.newDocumentBuilder();
		mExtractor = new AbstractXMLDataExtractorStub();
	}

	// TODO test for malformed XML

	@Test
	public void extractCorrectUnixTimestampFromDocument() {
		String xsdTimestamp = "2012-11-26T12:01:22Z"; /*==*/ long timestamp = 1353931282000L;
		Document role = createRoleDocument(xsdTimestamp);
		assertEquals(timestamp, mExtractor.extractIfmapMetadataTimestamp(role));
	}

	@Test
	public void extractMultiValueIfmapCardinalityFromDocument() {
		Document role = createRoleDocument();
		assertFalse(mExtractor.isSingleValueMetadata(role));
	}

	@Test
	public void extractTypenameFromDocument() {
		Document role = createRoleDocument();
		assertEquals("role", mExtractor.extractTypename(role));
	}

	private Document createRoleDocument(String xsdTimestamp) {
		final String NAMESPACE = "http://www.trustedcomputinggroup.org/2010/IFMAP-METADATA/2";
		final String NAMESPACE_PRFIX = "meta";
		Document doc = mBuilder.newDocument();

		Element role = doc.createElementNS(NAMESPACE, NAMESPACE_PRFIX + ":role");
		role.setAttributeNS(null, "ifmap-cardinality", "multiValue");
		role.setAttributeNS(null, "ifmap-publisher-id", "publisher-42");
		role.setAttributeNS(null, "ifmap-timestamp", xsdTimestamp);
		doc.appendChild(role);

		Element name = doc.createElement("name");
		name.setTextContent("admin");
		role.appendChild(name);

		return doc;
	}

	private Document createRoleDocument() {
		return createRoleDocument("2012-11-26T12:01:22Z");
	}
}
