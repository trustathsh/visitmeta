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
 * This file is part of visitmeta-visualization, version 0.3.0,
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

import de.hshannover.f4.trust.visitmeta.IfmapStrings;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.util.IdentifierWrapper;

/**
 * A class that implements {@link IdentifierInformationStrategy} and returns
 * a multiline textual representation of an {@link Identifier}, according
 * to its type.
 * 
 * Example:<br>
 * identity<br>
 * [ name=John Doe ]<br>
 * [ type=username ]<br>
 * 
 * @author Bastian Hellmann
 *
 */
public class IdentifierInformationMultiLine extends IdentifierInformationStrategy {

	@Override
	public String createTextForAccessRequest(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append("\n");
		sb.append("[ name=");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IfmapStrings.ACCESS_REQUEST_ATTR_NAME, "name"));	// name
		sb.append(" ]");
		sb.append(getAdministrativeDomain(wrapper));
		return sb.toString();
	}

	@Override
	public String createTextForIPAddress(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append("\n");
		sb.append("[ value=");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IfmapStrings.IP_ADDRESS_ATTR_VALUE, "value"));	// value
		sb.append(" ]");
		sb.append("\n");
		sb.append("[ type=");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IfmapStrings.IP_ADDRESS_ATTR_TYPE, "type"));	// type
		sb.append(" ]");
		sb.append(getAdministrativeDomain(wrapper));
		return sb.toString();
	}

	@Override
	public String createTextForMacAddress(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append("\n");
		sb.append("[ value=");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IfmapStrings.MAC_ADDRESS_ATTR_VALUE, "value"));	// value
		sb.append(" ]");
		sb.append(getAdministrativeDomain(wrapper));
		return sb.toString();
	}

	@Override
	public String createTextForIdentity(IdentifierWrapper wrapper) {
		String type = wrapper.getValueForXpathExpressionOrElse("@" + IfmapStrings.IDENTITY_ATTR_TYPE, "type");	// type
		String name = wrapper.getValueForXpathExpressionOrElse("@" + IfmapStrings.IDENTITY_ATTR_NAME, "name");	// name

		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());

		if (type.equals("other")) {
			sb.append("\n");
			sb.append("[ name=");
			sb.append(name.substring(name.indexOf(";") + 1, name.indexOf(" ")));
			sb.append(" ]");
			sb.append("\n");

			sb.append("[ type=");
			sb.append(type);	// type
			sb.append(" ]");
			sb.append("\n");

			sb.append("[ other-type-definition=");
			sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IfmapStrings.IDENTITY_ATTR_OTHER_TYPE_DEF, "other-type-definition"));	// other-type-definition
			sb.append(" ]");
		} else {
			sb.append("\n");
			sb.append("[ name=");
			sb.append(name);	// name
			sb.append(" ]");
			sb.append("\n");
			sb.append("[ type=");
			sb.append(type);	// type
			sb.append(" ]");
		}
		sb.append(getAdministrativeDomain(wrapper));
		return sb.toString();
	}

	@Override
	public String createTextForDevice(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append("\n");
		sb.append("[ name=");
		sb.append(wrapper.getValueForXpathExpressionOrElse(IfmapStrings.DEVICE_NAME_EL_NAME, "name"));	// name
		sb.append(" ]");
		return sb.toString();
	}

	/**
	 * Returns a {@link String} representation of the <i>administrative domain</i>
	 * attribute, if it exists.
	 * 
	 * @param identifier {@link IdentifierWrapper} instance (encapsulating a {@link Identifier})
	 * @return {@link String} representing the <i>administrative domain</i> attribute
	 */
	private String getAdministrativeDomain(
			IdentifierWrapper identifier) {
		String administrativeDomain = identifier.getValueForXpathExpression("@" + IfmapStrings.IDENTIFIER_ATTR_ADMIN_DOMAIN);	// administrative-domain

		StringBuilder sb = new StringBuilder();
		if (administrativeDomain != null && !administrativeDomain.isEmpty()) {
			sb.append("\n");
			sb.append("[ administrative-domain=");
			sb.append(administrativeDomain);
			sb.append(" ]");
		}
		return sb.toString();
	}
}
