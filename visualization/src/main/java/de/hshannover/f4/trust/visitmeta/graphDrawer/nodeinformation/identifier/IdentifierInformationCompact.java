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
 * This file is part of visitmeta-visualization, version 0.5.1,
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
package de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.identifier;

import java.util.Map;

import org.w3c.dom.Document;

import de.hshannover.f4.trust.visitmeta.IfmapStrings;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.util.DocumentUtils;
import de.hshannover.f4.trust.visitmeta.util.IdentifierWrapper;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

/**
 * A class that implements {@link IdentifierInformationStrategy} and returns a
 * compact textual representation of an {@link Identifier}, according to its
 * type.
 *
 * Examples:
 * <ul>
 * <li>access-request: ar1
 * <li>device: switch
 * <li>ip-address: 127.0.0.1 (IPv4)
 * <li>mac-address: aa:bb:cc:dd:ee:ff
 * <li>identity: John Doe (username)
 * <li>extended-identifier: service
 * </ul>
 *
 * @author Bastian Hellmann
 *
 */
public class IdentifierInformationCompact extends IdentifierInformationStrategy {

	@Override
	public String createTextForAccessRequest(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append(": ");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@"
				+ IfmapStrings.ACCESS_REQUEST_ATTR_NAME, "name")); // name
		return sb.toString();
	}

	@Override
	public String createTextForIPAddress(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append(": ");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@"
				+ IfmapStrings.IP_ADDRESS_ATTR_VALUE, "value")); // value
		sb.append(" (");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@"
				+ IfmapStrings.IP_ADDRESS_ATTR_TYPE, "type")); // type
		sb.append(")");
		return sb.toString();
	}

	@Override
	public String createTextForMacAddress(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append(": ");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@"
				+ IfmapStrings.MAC_ADDRESS_ATTR_VALUE, "value")); // value
		return sb.toString();
	}

	@Override
	public String createTextForIdentity(IdentifierWrapper wrapper) {
		String type = wrapper.getValueForXpathExpressionOrElse("@"
				+ IfmapStrings.IDENTITY_ATTR_TYPE, "type"); // type
		String name = wrapper.getValueForXpathExpressionOrElse("@"
				+ IfmapStrings.IDENTITY_ATTR_NAME, "name"); // name
		String otherTypeDefinition = wrapper.getValueForXpathExpressionOrElse(
				"@"
						+ IfmapStrings.IDENTITY_ATTR_OTHER_TYPE_DEF,
				"other-type-definition"); // other-type-definition

		StringBuilder sb = new StringBuilder();
		if (type.equals("other")) {
			boolean showExtendedIdentifierPrefix =
					mConfig.getBoolean(VisualizationConfig.KEY_SHOW_EXTENDED_IDENTIFIER_PREFIX,
							VisualizationConfig.DEFAULT_VALUE_SHOW_EXTENDED_IDENTIFIER_PREFIX);
			if (showExtendedIdentifierPrefix) {
				sb.append("extended-identifier: ");
			}
			int idxFirstSemicolon = name.indexOf(";");
			if (idxFirstSemicolon != -1) {
				sb.append(name.substring(name.indexOf(";") + 1,
						name.indexOf(" ")));
				sb.append("\n");

				Document document = DocumentUtils.parseEscapedXmlString(name);
				appendFurtherInformationWhenAvailable(sb, document);
			} else {
				sb.append(name); // name
				sb.append(" (");
				sb.append(otherTypeDefinition); // other-type-definition
				sb.append(")");
			}
		} else {
			sb.append(wrapper.getTypeName());
			sb.append(": ");
			sb.append(name); // name
			sb.append(" (");
			sb.append(type); // type
			sb.append(")");
		}
		return sb.toString();
	}

	private void appendFurtherInformationWhenAvailable(StringBuilder sb,
			Document document) {

		Map<String, String> furtherInformationWhenAvailable = DocumentUtils
				.extractInformation(document, DocumentUtils.NAME_TYPE_VALUE);

		String name = furtherInformationWhenAvailable.get("name");
		String type = furtherInformationWhenAvailable.get("value");
		String value = furtherInformationWhenAvailable.get("type");

		boolean nameExists = name != null;
		boolean typeExists = type != null;
		boolean valueExists = value != null;

		if (nameExists) {
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
			sb.append(type);

			if (valueExists) {
				sb.append(", ");
				sb.append(value);
			}
			sb.append(")");
		} else if (valueExists) {
			sb.append(value);
		}
	}

	@Override
	public String createTextForDevice(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append(": ");
		sb.append(wrapper.getValueForXpathExpressionOrElse(
				IfmapStrings.DEVICE_NAME_EL_NAME, "name")); // name
		return sb.toString();
	}
}
