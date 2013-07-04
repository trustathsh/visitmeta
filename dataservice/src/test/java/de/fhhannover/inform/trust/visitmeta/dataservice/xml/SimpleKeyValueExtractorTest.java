package de.fhhannover.inform.trust.visitmeta.dataservice.xml;

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

import java.io.StringReader;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import static org.junit.Assert.*;

public class SimpleKeyValueExtractorTest {

	private Document mDocument;
	private SimpleKeyValueExtractor mExtractor;

	@Before
	public void buildXML() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		String xml = "<meta:role "+
						"ifmap-cardinality=\"multiValue\" "+
						"ifmap-publisher-id=\"client-343rdq3r3\" " +
						"ifmap-timestamp=\"2010-04-20T12:00:05Z\" "+
						"xmlns:meta=\"http://www.trustedcomputinggroup.org/2010/IFMAP-METADATA/2\">"+
							"<administrative-domain>111:33</administrative-domain>"+
							"<name>admin</name>"+
					 "</meta:role>";
		InputSource input = new InputSource(new StringReader(xml));
		mDocument = builder.parse(input);

		mExtractor = new SimpleKeyValueExtractor();
	}

	@Test
	public void testResultSizeShouldMatch() {
		Map<String, String> result = mExtractor.extractToKeyValuePairs(mDocument);
		assertEquals(6, result.size());
	}

	@Test
	public void testResultContainsXmlAttributes() {
		Map<String, String> result = mExtractor.extractToKeyValuePairs(mDocument);
		assertEquals("multiValue", result.get("/meta:role[@ifmap-cardinality]"));
		assertEquals("client-343rdq3r3", result.get("/meta:role[@ifmap-publisher-id]"));
		assertEquals("2010-04-20T12:00:05Z", result.get("/meta:role[@ifmap-timestamp]"));
		assertEquals("http://www.trustedcomputinggroup.org/2010/IFMAP-METADATA/2", result.get("/meta:role[@xmlns:meta]"));
	}

	@Test
	public void testResultContainsXmlElements() {
		Map<String, String> result = mExtractor.extractToKeyValuePairs(mDocument);
		assertEquals("111:33", result.get("/meta:role/administrative-domain"));
		assertEquals("admin", result.get("/meta:role/name"));
	}

}
