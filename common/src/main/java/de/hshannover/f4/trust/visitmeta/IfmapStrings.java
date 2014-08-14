package de.hshannover.f4.trust.visitmeta;

import java.util.Arrays;
import java.util.List;

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
