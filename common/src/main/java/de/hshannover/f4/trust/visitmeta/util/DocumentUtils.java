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
 * This file is part of visitmeta-common, version 0.6.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2016 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Utility class for W3C {@link Document} objects.
 *
 * @author Bastian Hellmann
 *
 */
public class DocumentUtils {

	/**
	 * Private constructor, as the the class only contains static methods.
	 */
	private DocumentUtils() {
	}

	private static final Logger LOGGER = Logger.getLogger(DocumentUtils.class);

	private static final String[] NAME_TYPE_VALUE_ARRAY = new String[] {
			"name", "value", "type"};
	public static final Set<String> NAME_TYPE_VALUE = new HashSet<>(
			Arrays.asList(NAME_TYPE_VALUE_ARRAY));

	private static DocumentBuilderFactory mFactory;
	private static DocumentBuilder mBuilder;

	static {
		mFactory = DocumentBuilderFactory.newInstance();
		try {
			mBuilder = mFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * Takes escaped XML as a {@link String} and replaces any escaped character
	 * with its non-escaped version.
	 *
	 * <ul>
	 * <li>&ampamp; -> &
	 * <li>&amplt; -> <
	 * <li>&ampgt; -> >
	 * <li>&ampquot; -> "
	 * <li>&ampapos; -> '
	 * </ul>
	 *
	 * @param input
	 * @return
	 */
	public static String deEscapeXml(String input) {
		String ret = input;

		String[] unwanted = {"&amp;", "&lt;", "&gt;", "&quot;", "&apos;"};
		String[] replaceBy = {"&", "<", ">", "\"", "'"};

		for (int i = 0; i < unwanted.length; i++) {
			ret = ret.replace(unwanted[i], replaceBy[i]);
		}

		return ret;
	}

	/**
	 * Parses a {@link String} containing a XML document to a {@link Document}.
	 *
	 * @param input
	 *            XML document as one {@link String}
	 * @return a {@link Document}
	 */
	public static synchronized Document parseXmlString(String input) {
		Document document = null;
		try {
			document = mBuilder.parse(new InputSource(new StringReader(input)));
		} catch (SAXException | IOException e) {
			LOGGER.error(e.getMessage());
		}

		return document;
	}

	/**
	 * Convenience method; takes an escaped XML document as input and returns a
	 * {@link Document} object.
	 *
	 * @param input
	 *            escaped XML as one {@link String}
	 * @return a {@link Document}
	 */
	public static Document parseEscapedXmlString(String input) {
		String deEscapedXml = deEscapeXml(input);
		return parseXmlString(deEscapedXml);
	}

	private static String extractSingleInformation(Document document, String key) {
		Element documentElement = document.getDocumentElement();

		NodeList childNodes = document.getChildNodes();
		String result = null;
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);

			if (item.getNodeName().equals(key)) {
				result = item.getNodeValue();
			}
		}

		if (result == null
				|| result.equals("")) {
			result = documentElement.getAttribute(key);
		}

		return result;
	}

	/**
	 * Tries to extract information of a given {@link Document} object, by
	 * looking for child elements and attributes at the root-node of the
	 * document.
	 *
	 * @param document
	 *            a {@link Document} object
	 * @param keys
	 *            a {@link Set} of keys that are looked up in the
	 *            {@link Document}
	 * @return a {@link Map} containing all keys and their found values
	 */
	public static Map<String, String> extractInformation(Document document,
			Set<String> keys) {
		HashMap<String, String> result = new HashMap<>();

		String tmpValue;
		for (String key : keys) {
			tmpValue = extractSingleInformation(document, key);
			if (tmpValue != null
					&& !tmpValue.isEmpty()) {
				result.put(key, tmpValue);
			}
		}

		return result;
	}
}
