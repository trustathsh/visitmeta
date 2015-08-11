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
package de.hshannover.f4.trust.visitmeta.util;

import de.hshannover.f4.trust.visitmeta.IfmapStrings;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;

/**
 * Utility class that helps extracting information of extended identifier objects.
 *
 * @author Bastian Hellmann
 *
 */
public class ExtendedIdentifierHelper {

	/**
	 * Private constructor, as the the class only contains static methods.
	 */
	private ExtendedIdentifierHelper() {
	}

	/**
	 * Checks whether a given {@link Identifier} is an extended identifier or not.
	 * Searches for the <i>type</i> attribute and checks if it is <i>other</i> or not.
	 *
	 * @param identifier
	 *            the {@link Identifier} oject
	 * @return true if the {@link Identifier} object is an extended identifier
	 */
	public static boolean isExtendedIdentifier(Identifier identifier) {
		IdentifierWrapper wrapper = IdentifierHelper.identifier(identifier);
		String type = wrapper.getValueForXpathExpressionOrElse("@"
				+ IfmapStrings.IDENTITY_ATTR_TYPE, "type"); // type
		if (type.equals("other")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Extracts the inner type name of an extended identifier.
	 * Searches for the <i>name</i> attribute.
	 * Tries to find the first <i>;</i>; if found, it returns the string starting after that <i>;</i> and ending before
	 * the first single whitespace character.
	 *
	 * @param identifier
	 *            the {@link Identifier} oject
	 * @return the <i>inner</i< typename of the extended identifier
	 */
	public static String getExtendedIdentifierInnerTypeName(Identifier identifier) {
		IdentifierWrapper wrapper = IdentifierHelper.identifier(identifier);
		String name = wrapper.getValueForXpathExpressionOrElse("@"
				+ IfmapStrings.IDENTITY_ATTR_NAME, "name"); // name

		int idxFirstSemicolon = name.indexOf(";");
		if (idxFirstSemicolon != -1) {
			return (name.substring(name.indexOf(";")
					+ 1,
					name.indexOf(" ")));
		}
		return "";
	}

}