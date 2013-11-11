package de.fhhannover.inform.trust.visitmeta.dataservice.factories;

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
 * This file is part of VisITMeta, version 0.0.2, implemented by the Trust@FHH 
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


import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.fhhannover.inform.trust.visitmeta.dataservice.xml.DomHelper;
import de.fhhannover.inform.trust.visitmeta.dataservice.xml.SimpleKeyValueExtractor;
import de.fhhannover.inform.trust.visitmeta.dataservice.xml.XMLDataExtractor;
import de.fhhannover.inform.trust.visitmeta.persistence.inmemory.InMemoryIdentifier;

public class InMemoryIdentifierFactory implements InternalIdentifierFactory{

	private static final Logger log = Logger.getLogger(InMemoryIdentifierFactory.class);

	private XMLDataExtractor mXmlExtractor = new SimpleKeyValueExtractor();

	/**
	 * Create a new identifier based on the typename and properties of the given
	 * identifier.<br>
	 * <b>Note:</b> Neither the attached metadata nor the connected links are copied.
	 */
	@Override
	public InternalIdentifier createIdentifier(InternalIdentifier id) {
		log.trace("creating copy of " + id);
		InternalIdentifier idCopy = new InMemoryIdentifier(id);

		return idCopy;
	}

	@Override
	public InternalIdentifier createIdentifier(Document d) {
		log.trace("creating identifier from XML");
		String typename = mXmlExtractor.extractTypename(d);
		Map<String, String> properties = mXmlExtractor.extractToKeyValuePairs(d);

		InMemoryIdentifier id = new InMemoryIdentifier(typename);
		id.setRawData(DomHelper.documentToString(d));

		for (Entry<String, String> entry: properties.entrySet()) {
			id.addProperty(entry.getKey(), entry.getValue());
		}
		return id;
	}

}
