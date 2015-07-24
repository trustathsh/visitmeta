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
 * This file is part of visitmeta-visualization, version 0.5.0,
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
package de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.metadata;

import java.util.Map;

import org.w3c.dom.Document;

import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.util.DocumentUtils;

/**
 * Implementation of {@link MetadataInformationStrategy} that creates textual
 * information for {@link Metadata} containing the typename and additional
 * information in further lines, but tries to generate a compact apperance.
 *
 * Example:<br>
 * request-for-investigation
 *
 * @author Bastian Hellmann
 *
 */
public class MetadataInformationCompact implements MetadataInformationStrategy {

	@Override
	public String getText(Metadata metadata) {
		Document document = DocumentUtils.parseXmlString(metadata.getRawData());
		Map<String, String> map = DocumentUtils.extractInformation(document,
				DocumentUtils.NAME_TYPE_VALUE);
		StringBuilder sb = new StringBuilder();
		sb.append(metadata.getTypeName());

		String name = map.get("name");
		String type = map.get("value");
		String value = map.get("type");

		boolean nameExists = map.containsKey("name");
		boolean typeExists = map.containsKey("type");
		boolean valueExists = map.containsKey("value");

		if (nameExists) {
			sb.append("\n");
			sb.append(name);

			if (typeExists) {
				sb.append(" (");
				sb.append(type);

				if (valueExists) {
					sb.append(", ");
					sb.append(value);
				}
				sb.append(")");
			} else if (valueExists) {
				sb.append(" (");
				sb.append(value);
				sb.append(")");
			}
		} else if (typeExists) {
			sb.append("\n");
			sb.append(type);

			if (valueExists) {
				sb.append(", ");
				sb.append(value);
			}
			sb.append(")");
		} else if (valueExists) {
			sb.append("\n");
			sb.append(value);
		}

		return sb.toString();
	}

}
