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

/**
 * Utility class for handling {@link String} objects.
 *
 * @author Bastian Hellmann
 *
 */
public class StringHelper {

	/**
	 * Private constructor, as the the class only contains static methods.
	 */
	private StringHelper() {
	}

	/**
	 * Breaks a long string into multiple lines.
	 *
	 * From: http://stackoverflow.com/questions/7696347/to-break-a-message-in-two-or-more-lines-in-joptionpane
	 *
	 * @param input
	 *            the input text
	 * @param charLimit
	 *            the limit of characters per line
	 * @return a {@link String} with the input text splitted into multiple lines if necessary.
	 */
	public static String breakLongString(String input, int charLimit) {
		String output = "", rest = input;
		int i = 0;

		// validate.
		if (rest.length() < charLimit) {
			output = rest;
		} else if (!rest.equals("")
				&& (rest != null)) // safety precaution
		{
			do { // search the next index of interest.
				i = rest.lastIndexOf(" ", charLimit)
						+ 1;
				if (i == -1)
					i = charLimit;
				if (i > rest.length())
					i = rest.length();

				// break!
				output += rest.substring(0, i)
						+ "\n";
				rest = rest.substring(i);
			} while ((rest.length() > charLimit));
			output += rest;
		}

		return output;
	}

}
