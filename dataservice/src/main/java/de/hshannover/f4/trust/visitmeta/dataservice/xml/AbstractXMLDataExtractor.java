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
package de.hshannover.f4.trust.visitmeta.dataservice.xml;



import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

abstract public class AbstractXMLDataExtractor implements XMLDataExtractor {

	private static final Logger log = Logger.getLogger(AbstractXMLDataExtractor.class);

	@Override
	public long extractIfmapMetadataTimestamp(Document document) {
		log.trace("extracting timestamp from XML");

		Element root = document.getDocumentElement();

		String time = null;
		try {
			time = root.getAttribute("ifmap-timestamp");
			Calendar cal = DatatypeConverter.parseDateTime(time);
			long unixTime = cal.getTime().getTime();
			log.trace("found timestamp '"+unixTime+"'");
			return unixTime;
		} catch (IllegalArgumentException e) {
			log.error("could not parse timestamp '"+ time +"' in " + document.getLocalName() +
					"setting current local time as timestamp");
			return System.currentTimeMillis();
		}
	}

	@Override
	public boolean isSingleValueMetadata(Document document) {
		log.trace("extracting ifmap-cardinality from XML");

		Element root = document.getDocumentElement();
		if (root.hasAttribute("ifmap-cardinality")) {
			String cardinality = root.getAttribute("ifmap-cardinality");
			log.trace("found '"+cardinality+"' cardinality");
			return cardinality.equals("singleValue");
		} else {
			log.error("could not find 'ifmap-cardinality' assuming 'multiValue'");
			return false;
		}
	}

	@Override
	public String extractTypename(Document document) {
		log.trace("extracting typename from XML");

		Element root = document.getDocumentElement();
		return root.getLocalName();
	}
}
