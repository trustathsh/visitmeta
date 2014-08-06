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
 * This file is part of visitmeta dataservice, version 0.1.1,
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



import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Extracts information from XML documents into a set of key/value pairs.
 * A <tt>SimpleKeyValueExtractor</tt> is only capable of extracting information
 * from "simple" XML documents. "Simple" means:<br>
 * There is <b>always</b> only one XML element with the same name at the same level.<br>
 * Bad:
 * {@code
 * <foo>
 *   <bar>...</bar>
 *   <bar>...</bar> <!-- duplicate element at the same level -->
 * </foo>
 * }
 * OK:
 * {@code
 * <foo>
 *   <bar>...</bar>
 * </foo>
 * }
 *
 * @author Ralf Steuerwald
 *
 */
public class SimpleKeyValueExtractor extends AbstractXMLDataExtractor {

	private static final Logger log = Logger.getLogger(SimpleKeyValueExtractor.class);

	@Override
	public Map<String, String> extractToKeyValuePairs(Document document) {
		log.trace("extracting property key/value pairs from XML");
		Map<String, String> properties = new HashMap<>();

		Element documentElement = document.getDocumentElement();
		collectProperties(documentElement, "", properties);

		log.trace("extracted properties: " + properties);
		return properties;
	}

	/* Extracts the information of 'node' into the given mapping 'properties'. If 'node'
	 * is a leaf node the text value is stored . The key is a XPATH-like string representing
	 * the path to the node. If the node is not a leaf node then the information of its child
	 * nodes will be recursively collected.
	 */
	private static void collectProperties(
			Node node, String parentPath, Map<String, String> properties) {
		String path = parentPath + "/" + node.getNodeName();
		collectXmlAttributes(node, parentPath + "/" + node.getNodeName(), properties);

		NodeList childs = node.getChildNodes();
		for (int i = 0; i < childs.getLength(); i++) {
			Node child = childs.item(i);

			if (child.getNodeType() == Node.TEXT_NODE) {
				if (properties.containsKey(path)) {
					log.warn("duplicate key-expression '"+path+"', overwriting old value");
				}
				properties.put(path, child.getTextContent());
			}
			else {
				collectProperties(child, path, properties);
			}
		}
	}

	/* Extract the XML attributes from 'node' into the given mapping.
	 */
	private static void collectXmlAttributes(
			Node node, String parentPath, Map<String, String> properties) {
		NamedNodeMap attributes = node.getAttributes();
		for (int j = 0; j < attributes.getLength(); j++) {
			Node attribute = attributes.item(j);

			String key = parentPath + "[@" + attribute.getNodeName() + "]";

			if (properties.containsKey(key)) {
				log.warn("duplicate key-expression '"+parentPath+"', overwriting old value");
			}
			properties.put(key, attribute.getTextContent());
		}
	}
}
