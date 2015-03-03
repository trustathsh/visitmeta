package de.hshannover.f4.trust.visitmeta.ifmap.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.IfmapJ;
import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;
import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.messages.ResultItem;
import de.hshannover.f4.trust.ifmapj.metadata.Cardinality;
import de.hshannover.f4.trust.ifmapj.metadata.EnforcementAction;
import de.hshannover.f4.trust.ifmapj.metadata.EventType;
import de.hshannover.f4.trust.ifmapj.metadata.LocationInformation;
import de.hshannover.f4.trust.ifmapj.metadata.Significance;
import de.hshannover.f4.trust.ifmapj.metadata.StandardIfmapMetadataFactory;
import de.hshannover.f4.trust.ifmapj.metadata.WlanSecurityType;

public class ResultItemMock implements IfmapMock<ResultItem> {

	public static final String XMLNS_PREFIX = "xmlns:";

	public static final String TIMESTAMP = "ifmap-timestamp";

	public static final String TIMESTAMP_FRACTION = "ifmap-timestamp-fraction";

	public static final String IFMAP_PUBLISHER_ID = "ifmap-publisher-id";

	private static final StandardIfmapMetadataFactory IFMAP_METADATA_FACTORY = IfmapJ.createStandardMetadataFactory();

	private static final SimpleDateFormat XSD_DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	private static final SimpleDateFormat XSD_DATETIME_TIMEZONE_FORMATTER = new SimpleDateFormat("Z");

	public final String mPublisherID = "ResultItemMock";

	private ResultItem mResultItem_mock;

	private List<Document> mMetadata;

	private ResultItemMock() {
		mResultItem_mock = mock(ResultItem.class);
		mMetadata = new ArrayList<Document>();
		when(mResultItem_mock.getMetadata()).thenReturn(mMetadata);
	}

	public ResultItemMock(Identifier id1, Identifier id2) {
		this();

		when(mResultItem_mock.getIdentifier1()).thenReturn(id1);
		when(mResultItem_mock.getIdentifier2()).thenReturn(id2);
		when(mResultItem_mock.holdsLink()).thenReturn(true);
	}

	public ResultItemMock(Identifier id1) {
		this();

		when(mResultItem_mock.getIdentifier1()).thenReturn(id1);
		when(mResultItem_mock.holdsLink()).thenReturn(false);
	}

	@Override
	public ResultItem getMock() {
		return mResultItem_mock;
	}

	private void setXmlAttributes(Document doc, Date timestamp) {
		String timezone = XSD_DATETIME_TIMEZONE_FORMATTER.format(timestamp);
		String timeValue = XSD_DATE_TIME_FORMATTER.format(timestamp) + timezone.substring(0, 3) + ":" + timezone.substring(3);

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
	private double getSecondFraction(Date dt) {
		return dt.getTime() % 1000 / 1000.0;
	}

	private void addNewMetadata(Document doc, Date timestamp) {
		setXmlAttributes(doc, timestamp);
		mMetadata.add(doc);
		when(mResultItem_mock.getMetadata()).thenReturn(mMetadata);
	}

	public void addIpMac(String startTime, String endTime, String dhcpServer, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createIpMac(startTime, endTime, dhcpServer);
		addNewMetadata(doc, timestamp);
	}

	public void addIpMac(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createIpMac();
		addNewMetadata(doc, timestamp);
	}

	public void addArMac(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createArMac();
		addNewMetadata(doc, timestamp);
	}

	public void addArDev(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createArDev();
		addNewMetadata(doc, timestamp);
	}

	public void addArIp(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createArIp();
		addNewMetadata(doc, timestamp);
	}

	public void addAuthAs(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createAuthAs();
		addNewMetadata(doc, timestamp);
	}

	public void addAuthBy(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createAuthBy();
		addNewMetadata(doc, timestamp);
	}

	public void addDevIp(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createDevIp();
		addNewMetadata(doc, timestamp);
	}

	public void addDiscoveredBy(Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createDiscoveredBy();
		addNewMetadata(doc, timestamp);
	}

	public void addRole(String name, String administrativeDomain, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createRole(name, administrativeDomain);
		addNewMetadata(doc, timestamp);
	}

	public void addRole(String name, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createRole(name);
		addNewMetadata(doc, timestamp);
	}

	public void addDevAttr(String name, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createDevAttr(name);
		addNewMetadata(doc, timestamp);
	}

	public void addCapability(String name, String administrativeDomain, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createCapability(name, administrativeDomain);
		addNewMetadata(doc, timestamp);
	}

	public void addCapability(String name, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createCapability(name);
		addNewMetadata(doc, timestamp);
	}

	public void addDevChar(String manufacturer, String model, String os, String osVersion, String deviceType, String discoveredTime, String discovererId, String discoveryMethod,
			Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createDevChar(manufacturer, model, os, osVersion, deviceType, discoveredTime, discovererId, discoveryMethod);
		addNewMetadata(doc, timestamp);
	}

	public void addEnforcementReport(EnforcementAction enforcementAction, String otherTypeDefinition, String enforcementReason, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createEnforcementReport(enforcementAction, otherTypeDefinition, enforcementReason);
		addNewMetadata(doc, timestamp);
	}

	public void addEvent(String name, String discoveredTime, String discovererId, Integer magnitude, Integer confidence, Significance significance, EventType type,
			String otherTypeDefinition, String information, String vulnerabilityUri, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createEvent(name, discoveredTime, discovererId, magnitude, confidence, significance, type, otherTypeDefinition, information,
				vulnerabilityUri);
		addNewMetadata(doc, timestamp);
	}

	public void add(String elementName, String qualifiedName, String uri, Cardinality cardinality, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.create(elementName, qualifiedName, uri, cardinality);
		addNewMetadata(doc, timestamp);
	}

	public void add(String elementName, String qualifiedName, String uri, Cardinality cardinality, String attrName, String attrValue, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.create(elementName, qualifiedName, uri, cardinality, attrName, attrValue);
		addNewMetadata(doc, timestamp);
	}

	public void add(String elementName, String qualifiedName, String uri, Cardinality cardinality, HashMap<String, String> attributes, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.create(elementName, qualifiedName, uri, cardinality, attributes);
		addNewMetadata(doc, timestamp);
	}

	public void addLayer2Information(Integer vlan, String vlanName, Integer port, String administrativeDomain, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createLayer2Information(vlan, vlanName, port, administrativeDomain);
		addNewMetadata(doc, timestamp);
	}

	public void addLocation(List<LocationInformation> locationInformation, String discoveredTime, String discovererId, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createLocation(locationInformation, discoveredTime, discovererId);
		addNewMetadata(doc, timestamp);
	}

	public void addRequestForInvestigation(String qualifier, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createRequestForInvestigation(qualifier);
		addNewMetadata(doc, timestamp);
	}

	public void addWlanInformation(String ssid, List<WlanSecurityType> ssidUnicastSecurity, WlanSecurityType ssidGroupSecurity, List<WlanSecurityType> ssidManagementSecurity,
			Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createWlanInformation(ssid, ssidUnicastSecurity, ssidGroupSecurity, ssidManagementSecurity);
		addNewMetadata(doc, timestamp);
	}

	public void addUnexpectedBehavior(String discoveredTime, String discovererId, Integer magnitude, Integer confidence, Significance significance, String type, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createUnexpectedBehavior(discoveredTime, discovererId, magnitude, confidence, significance, type);
		addNewMetadata(doc, timestamp);
	}

	public void addClientTime(String time, Date timestamp) {
		Document doc = IFMAP_METADATA_FACTORY.createClientTime(time);
		addNewMetadata(doc, timestamp);
	}

}
