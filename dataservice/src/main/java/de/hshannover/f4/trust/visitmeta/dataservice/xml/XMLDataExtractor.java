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
 * This file is part of visitmeta dataservice, version 0.1.2,
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

import org.w3c.dom.Document;

/**
 * A <tt>XMLDataExtractor</tt> collects all information from a XML document into a simplified
 * format representing the same information.
 *
 * @author Ralf Steuerwald
 *
 */
public interface XMLDataExtractor {

	/**
	 * Extract the information from the given XML document into a sequence of key/value pair, where
	 * every key represents a XPATH-like address to its value.
	 *
	 * @param document the XML to process
	 * @return key/value pairs representing the XML document
	 */
	public Map<String, String> extractToKeyValuePairs(Document document);

	public String extractTypename(Document document);

	/**
	 * Returns <tt>true</tt> when the given document is a metadata document
	 * with <tt>singleValue</tt> metadata. If the document is
	 * <tt>multiValue</tt> metadata or no metadata at all <tt>false</tt> is
	 * returned.
	 *
	 * @param document the document to check
	 * @return <tt>true</tt> if document is <tt>singleValue</tt> metadata
	 */
	public boolean isSingleValueMetadata(Document document);

	/**
	 * Extracts the <tt>ifmap-timestamp</tt> from the given document.
	 * If the attribute is not found or not a valid xsd:dateTime the
	 * current local time will be returned.
	 *
	 * @param document the document to parse
	 * @return the parsed timestamp in milliseconds since 1970-01-01
	 */
	public long extractIfmapMetadataTimestamp(Document document);

}
