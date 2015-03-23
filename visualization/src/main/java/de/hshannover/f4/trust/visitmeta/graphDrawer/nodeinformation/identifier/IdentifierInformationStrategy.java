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
 * This file is part of visitmeta-visualization, version 0.4.0,
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

import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.util.IdentifierHelper;
import de.hshannover.f4.trust.visitmeta.util.IdentifierWrapper;

/**
 * Abstract superclass for classes that create the text for {@link Identifier}
 * elements within the graphical representation.
 * 
 * @author Bastian Hellmann
 *
 */
public abstract class IdentifierInformationStrategy {

	/**
	 * Creates the text written into a {@link Identifier}-node,
	 * based on the type and the information of the identifier itself.
	 * 
	 * Uses abstract methods for each {@link Identifier}-type, e.g. device or
	 * mac-address.
	 *
	 * @param identifier the {@link Identifier} for creating the text
	 * @return a {@link String} containing the specific information for the given {@link Identifier}
	 */
	public String getText(Identifier identifier) {
		IdentifierWrapper wrapper = IdentifierHelper.identifier(identifier);

		switch (wrapper.getTypeName()) {
		case IfmapStrings.ACCESS_REQUEST_EL_NAME:
			return createTextForAccessRequest(wrapper);
		case IfmapStrings.DEVICE_EL_NAME:
			return createTextForDevice(wrapper);
		case IfmapStrings.IDENTITY_EL_NAME:
			return createTextForIdentity(wrapper);
		case IfmapStrings.MAC_ADDRESS_EL_NAME:
			return createTextForMacAddress(wrapper);
		case IfmapStrings.IP_ADDRESS_EL_NAME:
			return createTextForIPAddress(wrapper);
		default:
			throw new IllegalArgumentException("Unsupported identifier type: " + wrapper.getTypeName());
		}
	}

	/**
	 * Returns a {@link String} representation of a IF-MAP <i>access-request</i> {@link Identifier}.
	 * 
	 * @param identifier {@link IdentifierWrapper} instance (encapsulating a {@link Identifier})
	 * @return {@link String} representing the <i>access-request</i> {@link Identifier}
	 */
	protected abstract String createTextForAccessRequest(IdentifierWrapper identifier);

	/**
	 * Returns a {@link String} representation of a IF-MAP <i>ip-address</i> {@link Identifier}.
	 * 
	 * @param identifier {@link IdentifierWrapper} instance (encapsulating a {@link Identifier})
	 * @return {@link String} representing the <i>ip-address</i> {@link Identifier}
	 */
	protected abstract String createTextForIPAddress(IdentifierWrapper identifier);

	/**
	 * Returns a {@link String} representation of a IF-MAP <i>mac-address</i> {@link Identifier}.
	 * 
	 * @param identifier {@link IdentifierWrapper} instance (encapsulating a {@link Identifier})
	 * @return {@link String} representing the <i>mac-address</i> {@link Identifier}
	 */
	protected abstract String createTextForMacAddress(IdentifierWrapper identifier);

	/**
	 * Returns a {@link String} representation of a IF-MAP <i>identity</i> {@link Identifier}.
	 * 
	 * @param identifier {@link IdentifierWrapper} instance (encapsulating a {@link Identifier})
	 * @return {@link String} representing the <i>identity</i> {@link Identifier}
	 */
	protected abstract String createTextForIdentity(IdentifierWrapper identifier);

	/**
	 * Returns a {@link String} representation of a IF-MAP <i>device</i> {@link Identifier}.
	 * 
	 * @param identifier {@link IdentifierWrapper} instance (encapsulating a {@link Identifier})
	 * @return {@link String} representing the <i>device</i> {@link Identifier}
	 */
	protected abstract String createTextForDevice(IdentifierWrapper identifier);
}
