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
 * This file is part of visitmeta-visualization, version 0.6.0,
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
package de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.hshannover.f4.trust.visitmeta.IfmapStrings;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.util.DocumentUtils;
import de.hshannover.f4.trust.visitmeta.util.IdentifierHelper;
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
public class IdentifierInformationPolicyCompact extends IdentifierInformationStrategy {

	// list of extended identifer inner typenames of irongpm and/or irondetect
	// policy identifiers
	private static final List<String> ESUKOM_TYPENAMES = Arrays.asList("signature", "condition", "action", "anomaly",
			"hint", "category");
	private static final List<String> SIMU_TYPENAMES = Arrays.asList("pattern-vertex", "service", "implementation",
			"vulnerability");

	@Override
	public String createTextForAccessRequest(Identifier identifier) {
		IdentifierWrapper wrapper = IdentifierHelper.identifier(identifier);
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append(": ");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IfmapStrings.ACCESS_REQUEST_ATTR_NAME, "name")); // name
		return sb.toString();
	}

	@Override
	public String createTextForIPAddress(Identifier identifier) {
		IdentifierWrapper wrapper = IdentifierHelper.identifier(identifier);
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append(": ");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IfmapStrings.IP_ADDRESS_ATTR_VALUE, "value")); // value
		sb.append(" (");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IfmapStrings.IP_ADDRESS_ATTR_TYPE, "type")); // type
		sb.append(")");
		return sb.toString();
	}

	@Override
	public String createTextForMacAddress(Identifier identifier) {
		IdentifierWrapper wrapper = IdentifierHelper.identifier(identifier);
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append(": ");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IfmapStrings.MAC_ADDRESS_ATTR_VALUE, "value")); // value
		return sb.toString();
	}

	@Override
	public String createTextForIdentity(Identifier identifier) {
		IdentifierWrapper wrapper = IdentifierHelper.identifier(identifier);
		String type = wrapper.getValueForXpathExpressionOrElse("@" + IfmapStrings.IDENTITY_ATTR_TYPE, "type"); // type
		String name = wrapper.getValueForXpathExpressionOrElse("@" + IfmapStrings.IDENTITY_ATTR_NAME, "name"); // name

		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append(": ");
		sb.append(name); // name
		sb.append(" (");
		sb.append(type); // type
		sb.append(")");
		return sb.toString();
	}

	@Override
	public String createTextForDevice(Identifier identifier) {
		IdentifierWrapper wrapper = IdentifierHelper.identifier(identifier);
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append(": ");
		sb.append(wrapper.getValueForXpathExpressionOrElse(IfmapStrings.DEVICE_NAME_EL_NAME, "name")); // name
		return sb.toString();
	}

	@Override
	protected String createTextForExtendedIdentifier(Identifier identifier) {
		IdentifierWrapper wrapper = IdentifierHelper.identifier(identifier);
		String name = wrapper.getValueForXpathExpressionOrElse("@" + IfmapStrings.IDENTITY_ATTR_NAME, "name"); // name
		String otherTypeDefinition = wrapper.getValueForXpathExpressionOrElse(
				"@" + IfmapStrings.IDENTITY_ATTR_OTHER_TYPE_DEF, "other-type-definition"); // other-type-definition

		StringBuilder sb = new StringBuilder();
		boolean showExtendedIdentifierPrefix = mConfig.getBoolean(
				VisualizationConfig.KEY_SHOW_EXTENDED_IDENTIFIER_PREFIX,
				VisualizationConfig.DEFAULT_VALUE_SHOW_EXTENDED_IDENTIFIER_PREFIX);
		if (showExtendedIdentifierPrefix) {
			sb.append("extended-identifier: ");
		}

		int idxFirstSemicolon = name.indexOf(";");
		if (idxFirstSemicolon != -1) {
			Document innerDocument = DocumentUtils.parseEscapedXmlString(name);
			String extendedTypeName = name.substring(name.indexOf(";") + 1, name.indexOf(" "));

			String administrativeDomain = getAdministrativeDomain(innerDocument);
			boolean administrativeDomainNotNullOrEmpty = (administrativeDomain != null)
					&& (!administrativeDomain.equals(""));

			sb.append(extendedTypeName);

			if (SIMU_TYPENAMES.contains(extendedTypeName)) {
				handleSIMUIdentifiers(sb, extendedTypeName, innerDocument);
			} else if (administrativeDomainNotNullOrEmpty && administrativeDomain.contains("irongpm-policy")) {
				handleSIMUIdentifiers(sb, extendedTypeName, innerDocument);
			} else if (ESUKOM_TYPENAMES.contains(extendedTypeName)) {
				handleESUKOMIdentifiers(sb, extendedTypeName, innerDocument);
			} else if (administrativeDomainNotNullOrEmpty && administrativeDomain.contains("irondetect-policy")) {
				handleESUKOMIdentifiers(sb, extendedTypeName, innerDocument);
			} else {
				handleUnknownExtendedIdentifier(sb, extendedTypeName, innerDocument);
			}
		} else {
			sb.append(name); // name
			sb.append(" (");
			sb.append(otherTypeDefinition); // other-type-definition
			sb.append(")");
		}

		return sb.toString();
	}

	private void handleSIMUIdentifiers(StringBuilder sb, String extendedTypeName, Document document) {
		String internalTypename;

		NodeList tmpElements;
		Node tmpItem;
		NamedNodeMap attributes;

		switch (extendedTypeName) {
		case "pattern-vertex":
			internalTypename = document.getElementsByTagName("typename").item(0).getTextContent();
			sb.append(": " + internalTypename);
			addProperties(document, sb);
			break;
		case "rule":
			sb.append(": ");
			tmpElements = document.getElementsByTagName("name");
			sb.append(tmpElements.item(0).getTextContent());
			tmpElements = document.getElementsByTagName("id");
			sb.append(", #" + tmpElements.item(0).getTextContent());
			// tmpElements = document.getElementsByTagName("description");
			// sb.append(tmpElements.item(0).getTextContent());
			// tmpElements = document.getElementsByTagName("recommendation");
			// sb.append(", #" + tmpElements.item(0).getTextContent());
			break;
		case "policy":
			sb.append(": ");
			tmpElements = document.getElementsByTagName("name");
			sb.append(tmpElements.item(0).getTextContent());
			break;
		case "service":
			sb.append(": ");
			attributes = document.getFirstChild().getAttributes();
			tmpItem = attributes.getNamedItem("name");
			sb.append(tmpItem.getTextContent());
			tmpItem = attributes.getNamedItem("port");
			sb.append(", @port: ");
			sb.append(tmpItem.getTextContent());
			tmpItem = attributes.getNamedItem("type");
			sb.append("\ntype: ");
			sb.append(tmpItem.getTextContent());
			break;
		case "implementation":
			sb.append(": ");
			attributes = document.getFirstChild().getAttributes();
			tmpItem = attributes.getNamedItem("name");
			sb.append(tmpItem.getTextContent());
			tmpItem = attributes.getNamedItem("version");
			sb.append(", version ");
			sb.append(tmpItem.getTextContent());
			break;
		case "vulnerability":
			sb.append(": ");
			attributes = document.getFirstChild().getAttributes();
			tmpItem = attributes.getNamedItem("id");
			sb.append(tmpItem.getTextContent());
			tmpItem = attributes.getNamedItem("type");
			sb.append(" (");
			sb.append(tmpItem.getTextContent());			
			sb.append(")");
			break;
		default:
			break;
		}
	}

	private void handleESUKOMIdentifiers(StringBuilder sb, String extendedTypeName, Document document) {
		// String extendedTypeName =
		// ExtendedIdentifierHelper.getExtendedIdentifierInnerTypeName(identifier);
		// Document document = ExtendedIdentifierHelper.getDocument(identifier);

		String tmpString;

		switch (extendedTypeName) {
		case "signature":
			sb.append(": ");
			tmpString = document.getElementsByTagName("id").item(0).getTextContent();
			sb.append(tmpString);
			sb.append("\n");
			NodeList featureExpressions = document.getElementsByTagName("featureExpression");
			for (int i = 0; i < featureExpressions.getLength(); i++) {
				String featureExpression = featureExpressions.item(i).getTextContent();
				sb.append("feature-expression #");
				sb.append(i + 1);
				sb.append(": ");
				sb.append(featureExpression);
				sb.append("\n");
			}
			break;
		case "condition":
			sb.append(": ");
			tmpString = document.getElementsByTagName("id").item(0).getTextContent();
			sb.append(tmpString);
			sb.append("\n");
			NodeList conditionExpressions = document.getElementsByTagName("conditionExpression");
			for (int i = 0; i < conditionExpressions.getLength(); i++) {
				String conditionExpression = conditionExpressions.item(i).getTextContent();
				sb.append("condition-expression #");
				sb.append(i + 1);
				sb.append(": ");
				sb.append(conditionExpression);
				sb.append("\n");
			}
			break;
		case "action":
			sb.append(": ");
			tmpString = document.getElementsByTagName("id").item(0).getTextContent();
			sb.append(tmpString);
			String operation = document.getElementsByTagName("operation").item(0).getTextContent();
			sb.append("\n");
			sb.append("operation: ");
			sb.append(operation);
			break;
		case "rule":
			sb.append(": ");
			tmpString = document.getElementsByTagName("name").item(0).getTextContent();
			sb.append(tmpString);
			tmpString = document.getElementsByTagName("id").item(0).getTextContent();
			sb.append(tmpString);
			sb.append(" (ID)");
			sb.append(tmpString);
			break;
		case "policy":
			sb.append(": ");
			tmpString = document.getElementsByTagName("id").item(0).getTextContent();
			sb.append(tmpString);
			break;
		default:
			break;
		}
	}

	private void handleUnknownExtendedIdentifier(StringBuilder sb, String extendedTypeName, Document innerDocument) {
		sb.append("\n");
		appendFurtherInformationWhenAvailable(sb, innerDocument);
	}

	private void addProperties(Document document, StringBuilder sb) {
		NodeList properties = document.getElementsByTagName("properties");

		if (properties.getLength() > 0) {
			sb.append("\n");

			for (int i = 0; i < properties.getLength(); i++) {
				String property = properties.item(i).getTextContent();
				if (!property.contains("typename") && !property.equals("[]")) {
					sb.append(property.substring(property.lastIndexOf("/")));

					if (i < properties.getLength() - 1) {
						sb.append("\n");
					}
				}
			}
		}
	}

	private String getAdministrativeDomain(Document document) {
		NamedNodeMap attributes = document.getFirstChild().getAttributes();

		if (attributes != null && attributes.getLength() > 0) {
			Node node = attributes.getNamedItem(("administrative-domain"));
			if (node != null) {
				return node.getTextContent();
			} else {
				for (int i = 0; i < attributes.getLength(); i++) {
				}
				return "";
			}
		} else {
			return "";
		}
	}

	private void appendFurtherInformationWhenAvailable(StringBuilder sb, Document document) {

		Map<String, String> furtherInformationWhenAvailable = DocumentUtils.extractInformation(document,
				DocumentUtils.NAME_TYPE_VALUE);

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
}
