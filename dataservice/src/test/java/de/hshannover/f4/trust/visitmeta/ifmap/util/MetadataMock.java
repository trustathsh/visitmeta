package de.hshannover.f4.trust.visitmeta.ifmap.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.IfmapJ;
import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;
import de.hshannover.f4.trust.ifmapj.metadata.Cardinality;
import de.hshannover.f4.trust.ifmapj.metadata.EnforcementAction;
import de.hshannover.f4.trust.ifmapj.metadata.EventType;
import de.hshannover.f4.trust.ifmapj.metadata.LocationInformation;
import de.hshannover.f4.trust.ifmapj.metadata.Significance;
import de.hshannover.f4.trust.ifmapj.metadata.StandardIfmapMetadataFactory;
import de.hshannover.f4.trust.ifmapj.metadata.WlanSecurityType;

public class MetadataMock {

	public static final String XMLNS_PREFIX = "xmlns:";

	public static final String TIMESTAMP = "ifmap-timestamp";

	public static final String TIMESTAMP_FRACTION = "ifmap-timestamp-fraction";

	public static final String IFMAP_PUBLISHER_ID = "ifmap-publisher-id";

	private static final SimpleDateFormat XSD_DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	private static final SimpleDateFormat XSD_DATETIME_TIMEZONE_FORMATTER = new SimpleDateFormat("Z");
	
	private static final StandardIfmapMetadataFactory IFMAP_METADATA_FACTORY = IfmapJ.createStandardMetadataFactory();

	public static final String mPublisherID = "IfmapjMock";

	/**
	 * Simulated the irond(v. 0.5.1) and set default metadata attributes.
	 * 
	 * @param doc
	 * @param timestamp
	 */
	private static void setDocumentXmlAttributes(Document doc, Date timestamp) {
		String timezone = XSD_DATETIME_TIMEZONE_FORMATTER.format(timestamp);
		String timeValue = XSD_DATE_TIME_FORMATTER.format(timestamp) + timezone.substring(0, 3) + ":"
				+ timezone.substring(3);

		Element mXmlElement = (Element) doc.getFirstChild();

		mXmlElement.setAttribute(XMLNS_PREFIX + IfmapStrings.STD_METADATA_PREFIX, IfmapStrings.STD_METADATA_NS_URI);
		mXmlElement.setAttribute(IFMAP_PUBLISHER_ID, mPublisherID);
		mXmlElement.setAttribute(TIMESTAMP_FRACTION, getSecondFraction(timestamp) + "");
		mXmlElement.setAttribute(TIMESTAMP, timeValue);
	}

	/**
	 * Copied from the irond(v. 0.5.1) sources (de.hshannover.f4.trust.iron.mapserver.utils.TimestampFraction)
	 * 
	 * Return the decimal fraction of a second of the given {@link Date}.
	 *
	 * @param dt the {@link Date} from with to extract the fraction
	 * @return a double containing the decimal fraction of a second
	 */
	private static double getSecondFraction(Date dt) {
		return dt.getTime() % 1000 / 1000.0;
	}

	public static Document createIpMac(String startTime, String endTime, String dhcpServer, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createIpMac(startTime, endTime, dhcpServer);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createIpMac(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createIpMac();
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createArMac(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createArMac();
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createArDev(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createArDev();
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createArIp(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createArIp();
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createAuthAs(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createAuthAs();
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createAuthBy(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createAuthBy();
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createDevIp(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createDevIp();
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createDiscoveredBy(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createDiscoveredBy();
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createRole(String name, String administrativeDomain, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createRole(name, administrativeDomain);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createRole(String name, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createRole(name);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createDevAttr(String name, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createDevAttr(name);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createCapability(String name, String administrativeDomain, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createCapability(name, administrativeDomain);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createCapability(String name, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createCapability(name);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createDevChar(String manufacturer, String model, String os, String osVersion,
			String deviceType, String discoveredTime, String discovererId, String discoveryMethod,
			Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createDevChar(manufacturer, model, os, osVersion, deviceType, discoveredTime, discovererId, discoveryMethod);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createEnforcementReport(EnforcementAction enforcementAction, String otherTypeDefinition,
			String enforcementReason, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createEnforcementReport(enforcementAction, otherTypeDefinition, enforcementReason);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createEvent(String name, String discoveredTime, String discovererId, Integer magnitude,
			Integer confidence, Significance significance, EventType type,
			String otherTypeDefinition, String information, String vulnerabilityUri, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createEvent(name, discoveredTime, discovererId, magnitude, confidence, significance, type, otherTypeDefinition, information,
				vulnerabilityUri);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document create(String elementName, String qualifiedName, String uri, Cardinality cardinality,
			Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.create(elementName, qualifiedName, uri, cardinality);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document create(String elementName, String qualifiedName, String uri, Cardinality cardinality,
			String attrName, String attrValue, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.create(elementName, qualifiedName, uri, cardinality, attrName, attrValue);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document create(String elementName, String qualifiedName, String uri, Cardinality cardinality,
			HashMap<String, String> attributes, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.create(elementName, qualifiedName, uri, cardinality, attributes);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createLayer2Information(Integer vlan, String vlanName, Integer port,
			String administrativeDomain, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createLayer2Information(vlan, vlanName, port, administrativeDomain);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createLocation(List<LocationInformation> locationInformation, String discoveredTime,
			String discovererId, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createLocation(locationInformation, discoveredTime, discovererId);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createRequestForInvestigation(String qualifier, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createRequestForInvestigation(qualifier);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createWlanInformation(String ssid, List<WlanSecurityType> ssidUnicastSecurity,
			WlanSecurityType ssidGroupSecurity, List<WlanSecurityType> ssidManagementSecurity,
			Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createWlanInformation(ssid, ssidUnicastSecurity, ssidGroupSecurity, ssidManagementSecurity);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createUnexpectedBehavior(String discoveredTime, String discovererId, Integer magnitude,
			Integer confidence, Significance significance, String type, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createUnexpectedBehavior(discoveredTime, discovererId, magnitude, confidence, significance, type);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

	public static Document createClientTime(String time, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createClientTime(time);
		setDocumentXmlAttributes(doc, timestamp);
		return doc;
	}

}
