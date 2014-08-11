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
package de.hshannover.f4.trust.visitmeta.dataservice.factories;




import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.dataservice.xml.DomHelper;
import de.hshannover.f4.trust.visitmeta.dataservice.xml.SimpleKeyValueExtractor;
import de.hshannover.f4.trust.visitmeta.dataservice.xml.XMLDataExtractor;
import de.hshannover.f4.trust.visitmeta.persistence.inmemory.InMemoryMetadata;

public class InMemoryMetadataFactory implements InternalMetadataFactory{

	private static final Logger log = Logger.getLogger(InMemoryMetadataFactory.class);

	private XMLDataExtractor mXmlExtractor = new SimpleKeyValueExtractor();

	@Override
	public InternalMetadata createMetadata(Document document) {
		log.trace("creating metadata from XML");

		// extract metadata object attributes
		String typename = mXmlExtractor.extractTypename(document);
		boolean isSingleValueMetadata = mXmlExtractor.isSingleValueMetadata(document);
		long timestamp = mXmlExtractor.extractIfmapMetadataTimestamp(document);

		// recursively collect all information under the top-level element
		Map<String, String> properties = mXmlExtractor.extractToKeyValuePairs(document);

		// create a internal metadata object
		InMemoryMetadata metadata = new InMemoryMetadata(
				typename, isSingleValueMetadata, timestamp);
		metadata.setRawData(DomHelper.documentToString(document));

		for (Entry<String, String> e : properties.entrySet()) {
			metadata.addProperty(e.getKey(), e.getValue());
		}

		return metadata;
	}

	@Override
	public InternalMetadata createMetadata(InternalMetadata meta) {
		log.trace("creating copy of " + meta);

		InternalMetadata metaCopy = new InMemoryMetadata(meta);
		return metaCopy;
	}



}
