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
package de.hshannover.f4.trust.visitmeta.util;

import de.hshannover.f4.trust.visitmeta.IfmapStrings;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Utility class that helps extracting information of {@link Metadata} objects.
 *
 * @author Bastian Hellmann
 *
 */
public class MetadataHelper {

	/**
	 * Private constructor, as the the class only contains static methods.
	 */
	private MetadataHelper() {
	}

	/**
	 * Tries to extract the IF-MAP publisher id of a {@link Metadata} object.
	 *
	 * @param metadata
	 *            a {@link Metadata} object
	 * @return the IF-MAP publisher id for the given {@link Metadata} object, if
	 *         it is found in the properties; otherwise, a empty string is
	 *         returned
	 */
	public static String extractPublisherId(Metadata metadata) {
		return extractProperty(metadata, "[@"
				+ IfmapStrings.PUBLISHER_ID_ATTR + "]");
	}

	/**
	 * Tries to extract the IF-MAP publisher timestamp of a {@link Metadata} object.
	 *
	 * @param metadata
	 *            a {@link Metadata} object
	 * @return the IF-MAP publisher timestamp for the given {@link Metadata} object, if
	 *         it is found in the properties; otherwise, a empty string is
	 *         returned
	 */
	public static String extractTimestamp(Metadata metadata) {
		return extractProperty(metadata, "[@"
				+ IfmapStrings.IFMAP_TIMESTAMP_ATTR + "]");
	}

	/**
	 * Tries to extract a property id of a {@link Metadata} object.
	 *
	 * @param metadata
	 *            a {@link Metadata} object
	 * @param propertyKey
	 *            key of the property
	 *
	 * @return the value of the property for the given {@link Metadata} object, if
	 *         it is found in the properties; otherwise, a empty string is
	 *         returned
	 */
	public static String extractProperty(Metadata metadata, String propertyKey) {
		for (String property : metadata.getProperties()) {
			if (property.contains(propertyKey)) {
				return metadata.valueFor(property);
			}
		}

		return "";
	}
}
