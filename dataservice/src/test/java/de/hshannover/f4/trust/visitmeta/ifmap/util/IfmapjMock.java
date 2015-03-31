package de.hshannover.f4.trust.visitmeta.ifmap.util;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;

import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.messages.PollResult;
import de.hshannover.f4.trust.ifmapj.messages.ResultItem;
import de.hshannover.f4.trust.ifmapj.messages.SearchResult;
import de.hshannover.f4.trust.ifmapj.metadata.Cardinality;
import de.hshannover.f4.trust.ifmapj.metadata.EnforcementAction;
import de.hshannover.f4.trust.ifmapj.metadata.EventType;
import de.hshannover.f4.trust.ifmapj.metadata.LocationInformation;
import de.hshannover.f4.trust.ifmapj.metadata.Significance;
import de.hshannover.f4.trust.ifmapj.metadata.WlanSecurityType;

public class IfmapjMock {

	private static final String mDefaultSubscriptionName = "DefaultSubscriptionName";

	public static PollResult PollResultMock(SearchResult... searchResults) {
		PollResultMock pollResult = new PollResultMock(searchResults);
		return pollResult.getMock();
	}

	public static SearchResult SearchResultMock(String searchResultName, SearchResult.Type type, ResultItem... items) {
		SearchResultMock searchResult = new SearchResultMock(searchResultName, type, Arrays.asList(items));
		return searchResult.getMock();
	}

	public static SearchResult SearchResultMock(SearchResult.Type type, ResultItem... items) {
		SearchResultMock searchResult = new SearchResultMock(mDefaultSubscriptionName, type, Arrays.asList(items));
		return searchResult.getMock();
	}

	public static ResultItem ResultItemMock(Identifier id1, Identifier id2, Document... metadata) {
		ResultItemMock resultItemMock = new ResultItemMock(id1, id2);
		resultItemMock.addMetadata(metadata);

		return resultItemMock.getMock();
	}

	public static ResultItem ResultItemMock(Identifier id, Document... metadata) {
		ResultItemMock resultItemMock = new ResultItemMock(id);
		resultItemMock.addMetadata(metadata);

		return resultItemMock.getMock();
	}

	public static Document CreateIpMac(String startTime, String endTime, String dhcpServer, Date timestamp) {
		return MetadataMock.createIpMac(startTime, endTime, dhcpServer, timestamp);
	}

	public static Document CreateIpMac(Date timestamp) {
		return MetadataMock.createIpMac(timestamp);
	}

	public static Document CreateArMac(Date timestamp) {
		return MetadataMock.createArMac(timestamp);
	}

	public static Document CreateArDev(Date timestamp) {
		return MetadataMock.createArDev(timestamp);
	}

	public static Document CreateArIp(Date timestamp) {
		return MetadataMock.createArIp(timestamp);
	}

	public static Document CreateAuthAs(Date timestamp) {
		return MetadataMock.createAuthAs(timestamp);
	}

	public static Document CreateAuthBy(Date timestamp) {
		return MetadataMock.createAuthBy(timestamp);
	}

	public static Document CreateDevIp(Date timestamp) {
		return MetadataMock.createDevIp(timestamp);
	}

	public static Document CreateDiscoveredBy(Date timestamp) {
		return MetadataMock.createDiscoveredBy(timestamp);
	}

	public static Document CreateRole(String name, String administrativeDomain, Date timestamp) {
		return MetadataMock.createRole(name, administrativeDomain, timestamp);
	}

	public static Document CreateRole(String name, Date timestamp) {
		return MetadataMock.createRole(name, timestamp);
	}

	public static Document CreateDevAttr(String name, Date timestamp) {
		return MetadataMock.createDevAttr(name, timestamp);
	}

	public static Document CreateCapability(String name, String administrativeDomain, Date timestamp) {
		return MetadataMock.createCapability(name, administrativeDomain, timestamp);
	}

	public static Document CreateCapability(String name, Date timestamp) {
		return MetadataMock.createCapability(name, timestamp);
	}

	public static Document CreateDevChar(String manufacturer, String model, String os, String osVersion,
			String deviceType, String discoveredTime, String discovererId, String discoveryMethod,
			Date timestamp) {
		return MetadataMock.createDevChar(manufacturer, model, os, osVersion, deviceType, discoveredTime, discovererId,
				discoveryMethod, timestamp);
	}

	public static Document CreateEnforcementReport(EnforcementAction enforcementAction, String otherTypeDefinition,
			String enforcementReason, Date timestamp) {
		return MetadataMock.createEnforcementReport(enforcementAction, otherTypeDefinition, enforcementReason,
				timestamp);
	}

	public static Document CreateEvent(String name, String discoveredTime, String discovererId, Integer magnitude,
			Integer confidence, Significance significance, EventType type,
			String otherTypeDefinition, String information, String vulnerabilityUri, Date timestamp) {
		return MetadataMock.createEvent(name, discoveredTime, discovererId, magnitude, confidence, significance, type,
				otherTypeDefinition, information, vulnerabilityUri, timestamp);
	}

	public static Document Create(String elementName, String qualifiedName, String uri, Cardinality cardinality,
			Date timestamp) {
		return MetadataMock.create(elementName, qualifiedName, uri, cardinality, timestamp);
	}

	public static Document Create(String elementName, String qualifiedName, String uri, Cardinality cardinality,
			String attrName, String attrValue, Date timestamp) {
		return MetadataMock.create(elementName, qualifiedName, uri, cardinality, attrName, attrValue, timestamp);
	}

	public static Document Create(String elementName, String qualifiedName, String uri, Cardinality cardinality,
			HashMap<String, String> attributes, Date timestamp) {
		return MetadataMock.create(elementName, qualifiedName, uri, cardinality, attributes, timestamp);
	}

	public static Document CreateLayer2Information(Integer vlan, String vlanName, Integer port,
			String administrativeDomain, Date timestamp) {
		return MetadataMock.createLayer2Information(vlan, vlanName, port, administrativeDomain, timestamp);
	}

	public static Document CreateLocation(List<LocationInformation> locationInformation, String discoveredTime,
			String discovererId, Date timestamp) {
		return MetadataMock.createLocation(locationInformation, discoveredTime, discovererId, timestamp);
	}

	public static Document CreateRequestForInvestigation(String qualifier, Date timestamp) {
		return MetadataMock.createRequestForInvestigation(qualifier, timestamp);
	}

	public static Document CreateWlanInformation(String ssid, List<WlanSecurityType> ssidUnicastSecurity,
			WlanSecurityType ssidGroupSecurity, List<WlanSecurityType> ssidManagementSecurity,
			Date timestamp) {
		return MetadataMock.createWlanInformation(ssid, ssidUnicastSecurity, ssidGroupSecurity, ssidManagementSecurity,
				timestamp);
	}

	public static Document CreateUnexpectedBehavior(String discoveredTime, String discovererId, Integer magnitude,
			Integer confidence, Significance significance, String type, Date timestamp) {
		return MetadataMock.createUnexpectedBehavior(discoveredTime, discovererId, magnitude, confidence, significance,
				type, timestamp);
	}

	public static Document CreateClientTime(String time, Date timestamp) {
		return MetadataMock.createClientTime(time, timestamp);
	}

}
