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
 * This file is part of visitmeta-common, version 0.3.0,
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
package de.hshannover.f4.trust.visitmeta;

import java.util.Arrays;
import java.util.List;

/**
 * A class that contains IF-MAP related {@link String} constants and Lists.
 * Partly copied from ifmapj 2.2.0.
 * 
 * @author Bastian Hellmann
 *
 */
public class IfmapStrings {

	public static final String IFMAP_TIMESTAMP_ATTR = "ifmap-timestamp";
	public static final String IFMAP_CARDINALITY_ATTR = "ifmap-cardinality";

	/**
	 * Copied from ifmapj 2.2.0
	 * Package: de.hshannover.f4.trust.ifmapj.binding
	 * Class: IfmapStrings.java
	 */
	public static final String PUBLISHER_ID_ATTR = "ifmap-publisher-id";

	public static final String BASE_PREFIX = "ifmap";
	public static final String BASE_NS_URI =  "http://www.trustedcomputinggroup.org/2010/IFMAP/2";

	public static final String STD_METADATA_PREFIX =  "meta";
	public static final String STD_METADATA_NS_URI =  "http://www.trustedcomputinggroup.org/2010/IFMAP-METADATA/2";

	public static final String IDENTIFIER_ATTR_ADMIN_DOMAIN = "administrative-domain";

	public static final String ACCESS_REQUEST_EL_NAME = "access-request";
	public static final String ACCESS_REQUEST_ATTR_NAME = "name";

	public static final String DEVICE_EL_NAME = "device";
	public static final String DEVICE_NAME_EL_NAME = "name";

	public static final String IDENTITY_EL_NAME = "identity";
	public static final String IDENTITY_ATTR_NAME = "name";
	public static final String IDENTITY_ATTR_TYPE = "type";
	public static final String IDENTITY_ATTR_OTHER_TYPE_DEF = "other-type-definition";

	public static final String IP_ADDRESS_EL_NAME = "ip-address";
	public static final String IP_ADDRESS_ATTR_VALUE = "value";
	public static final String IP_ADDRESS_ATTR_TYPE = "type";

	public static final String MAC_ADDRESS_EL_NAME = "mac-address";
	public static final String MAC_ADDRESS_ATTR_VALUE = "value";

	public static final String OTHER_TYPE_EXTENDED_IDENTIFIER = "extended";
	/**
	 * End of copied block
	 */

	public static final List<String> IFMAP_OPERATIONAL_ATTRIBUTES = Arrays.asList(PUBLISHER_ID_ATTR, IFMAP_TIMESTAMP_ATTR, IFMAP_CARDINALITY_ATTR);

	public static final List<String> IDENTIFIER_TYPES = Arrays.asList(ACCESS_REQUEST_EL_NAME,
			DEVICE_EL_NAME,
			IDENTITY_EL_NAME,
			IP_ADDRESS_EL_NAME,
			MAC_ADDRESS_EL_NAME);
}
