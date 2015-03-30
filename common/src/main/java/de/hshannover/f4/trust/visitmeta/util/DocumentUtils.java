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
 * This file is part of visitmeta-common, version 0.4.1,
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

import org.w3c.dom.Document;

/**
 * Utility class for W3C {@link Document} objects.
 * 
 * @author Bastian Hellmann
 *
 */
public class DocumentUtils {

	/**
	 * Takes escaped XML as a {@link String} and replaces any escaped
	 * character with its non-escaped version.
	 * 
	 * <ul>
	 * <li> &ampamp; -> &
	 * <li> &amplt; -> <
	 * <li> &ampgt; -> >
	 * <li> &ampquot; -> "
	 * <li> &ampapos; -> '
	 * </ul>
	 * 
	 * @param input
	 * @return
	 */
	public static String deEscapeXml(String input) {
		String ret = input;

		String []unwanted = {"&amp;", "&lt;", "&gt;", "&quot;", "&apos;"};
		String []replaceBy =  {"&",     "<",    ">",   "\"",    "'"};

		for (int i = 0; i < unwanted.length; i++) {
			ret = ret.replace(unwanted[i], replaceBy[i]);
		}

		return ret;
	}
}
