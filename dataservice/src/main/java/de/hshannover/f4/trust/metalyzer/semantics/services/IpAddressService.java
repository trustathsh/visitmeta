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
 * This file is part of visitmeta dataservice, version 0.1.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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
/** Project: Metalyzer
 * Author: Michael Felchner
 * Author: Mihaela Stein
 * Author: Sven Steinbach
 * Last Change:
 * 	by: $Author: $
 * 	date: $Date: $
 * Copyright (c): Hochschule Hannover
 */


package de.hshannover.f4.trust.metalyzer.semantics.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.api.MetalyzerDelta;
import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.semantics.entities.Device;
import de.hshannover.f4.trust.metalyzer.semantics.entities.IpAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.MacAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.User;
import de.hshannover.f4.trust.metalyzer.semantics.helper.DeviceHelper;
import de.hshannover.f4.trust.metalyzer.semantics.helper.UserHelper;
import de.hshannover.f4.trust.metalyzer.semantics.helper.MacAddressHelper;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController.RequestType;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierPair;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Service-class to provide query-methods to search information for specific Ip-Addresses.
 * The IpAddressService provides methods for timestamp-, timeinterval- and current-requests.
 *
 */
public class IpAddressService {
	private SemanticsController semCon;
	
	private static final Logger log = Logger.getLogger(IpAddressService.class);

	/**
	 * Constructor
	 * Uses the getInstance-method to set the SemanticsController-Attribute.
	 */
	
	public IpAddressService() {
		semCon = SemanticsController.getInstance();
	}

	/**
	 * Constructor
	 * Uses a parameter to set the SemanticsController-Attribute.
	 * Mainly used in IpAddressService-UnitTests.
	 * @param semCon
	 * SemanticsController
	 */
	
	public IpAddressService(SemanticsController semCon) {
		this.semCon = semCon;
	}

	/**
	 * Query-method to get all IP-Addresses at a given Unix-Timestamp.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param fromTimestamp
	 * Unix-Timestamp of the beginning of the search-interval.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search-interval.
	 * @return
	 * Returns a list of all IP-Addresses at the given Unix-Timestamp.
	 */
	public ArrayList<IpAddress> getIpAddresses(RequestType requestType, long fromTimestamp,long toTimestamp) 
			throws MetalyzerAPIException{
		log.info("Searching for all IP-Address-Objects");
		
		//Searching for the IP-Address-identifiers
		Collection<Identifier> ipAddressList = null;
		if (requestType == RequestType.TIMESTAMP_REQUEST) {
			ipAddressList = getAllIpAddressIdentifiers(fromTimestamp);
		} else if (requestType == RequestType.TIMEINTERVAL_REQUEST) {			
			ipAddressList = getAllIpAddressIdentifiers(fromTimestamp, toTimestamp);
		} else if (requestType == RequestType.CURRENT_REQUEST) {
			ipAddressList = getAllIpAddressIdentifiers();
		}
		
		ArrayList<IpAddress> ipAddresses = new ArrayList<IpAddress>();
		String address = "";
		String type = "";
		long publishTimestamp = 0;

		for (Identifier ipAddressIdentifier : ipAddressList) {
			//Searching for the address.
			address = ipAddressIdentifier.valueFor("/ip-address[@value]");
			type = ipAddressIdentifier.valueFor("/ip-address[@type]");
			for (Link ipAddressLink : ipAddressIdentifier.getLinks()) {
				for (Metadata linkMetadata : ipAddressLink.getMetadata()) {
					//Searching for the corresponding publish-timestamp of the IP-Address.
					if (linkMetadata.getTypeName().equals("access-request-ip")) {
						publishTimestamp = linkMetadata.getPublishTimestamp();
					}
					if (linkMetadata.getTypeName().equals("device-ip")) {
						publishTimestamp = linkMetadata.getPublishTimestamp();
					}
				}
			}
			IpAddress ip = new IpAddress(publishTimestamp, address,type);
			//Checking for duplicates.
			if (!ipAddresses.contains(ip)) {
				ipAddresses.add(ip);
			}
		}
		return ipAddresses;
	}

	/**
	 * Query-method to get the publish-timestamp of a specific IP-Address-Object.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param ip
	 * IP-Address to which the Unix-Timestamp should be searched.
	 * @return
	 * Returns the Unix-Timestamp of the given IP-Address.
	 */
	public long getTimestamp(RequestType requestType,IpAddress ip) throws MetalyzerAPIException{
		log.info("Searching for the publish-timestamp of a given IP-Address");
		
		//Searching for the Ip-Address-Identifier.
		Identifier ipAddressIdentifier = getIpAddressIdentifier(requestType,ip);

		Metadata accessRequestIp = null;
		Metadata deviceIp = null;
		
		//Searching for the publish-timestamp for the IP-Address.
		if (ipAddressIdentifier != null) {
			//Check all links.
			for (Link ipAddressLink : ipAddressIdentifier.getLinks()) {
				for (Metadata linkMetadata : ipAddressLink.getMetadata()) {
					if (linkMetadata.getTypeName().equals("access-request-ip")) {
						accessRequestIp = linkMetadata;
						break;
					} else if (linkMetadata.getTypeName().equals("device-ip")) {
						deviceIp = linkMetadata;
					}
				}
			}
		}
		
		if (accessRequestIp != null) {
			//First try to get the publish-timestamp from access-request-ip-metadata.
			return accessRequestIp.getPublishTimestamp();
		} else if (deviceIp != null) {
			//Second try to get the publish-timestamp from device-ip-metadata.
			return deviceIp.getPublishTimestamp();
		} else {
			//Else no publish-timestamp was found, so set it to 0.
			return 0;
		}
	}

	/**
	 * Query-method to get all associated users of a given IP-Address-Object at a 
	 * given Unix-Timestamp. The timestamp-attribute of the IP-Address-Object can 
	 * be chosen freely to narrow down the search-results. In the IF-MAP-Graph only 
	 * users with an earlier or same publish-timestamp as the timestamp-attribute of
	 * the given IP-Address-Object will be found.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param ip  
	 * IP-Address to which the users should be searched.
	 * @return
	 * Returns a list of all users of the given IP-Address.
	 */
	public ArrayList<User> getUsers(RequestType requestType,IpAddress ip) throws MetalyzerAPIException{
		log.info("Searching for the users of a given IP-Address");
		
		//Searching for the IP-Address-Identifier.
		Identifier ipAddressIdentifier = getIpAddressIdentifier(requestType,ip);

		ArrayList<User> users = new ArrayList<User>();

		//Searching for the corresponding users for Ip-Address ip.
		if (ipAddressIdentifier != null) {
			for (Link ipAddressLink:ipAddressIdentifier.getLinks()) {
				IdentifierPair ids = ipAddressLink.getIdentifiers();
				Identifier first = ids.getFirst();
				Identifier second = ids.getSecond();

				//First identifier of the link is the access-request.
				if (first.getTypeName().equals("access-request")) {
					UserHelper.findUsersFromIdentifier(first, users);

					//Second identifier of the link is the access-request.
				} else if (second.getTypeName().equals("access-request")) {
					UserHelper.findUsersFromIdentifier(second, users);
					
					//First identifier of the link is the MAC-Address.
				} else if (first.getTypeName().equals("mac-address")) {
					UserHelper.findUsersOverIdentifier(first, users);
					
					//Second identifier of the link is the MAC-Address.
				} else if (second.getTypeName().equals("mac-address")) {
					UserHelper.findUsersOverIdentifier(second, users);
				}
			}
		}
		return users;
	}

	/**
	 * Query-method to get all associated MAC-Addresses of a given IP-Address-Object at a 
	 * given Unix-Timestamp. The timestamp-attribute of the IP-Address-Object can be chosen
	 * freely to narrow down the search-results. In the IF-MAP-Graph only MAC-Addresses with 
	 * an earlier or same publish-timestamp as the timestamp-attribute of the given 
	 * IP-Address-Object will be found.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param ip
	 * IP-Address to which the MAC-Addresses should be searched.
	 * @return
	 * Returns a list of all MAC-Addresses of the given IP-Address.
	 */
	public ArrayList<MacAddress> getMacAddresses(RequestType requestType,IpAddress ip) throws MetalyzerAPIException{
		log.info("Searching for the MAC-Addresses of a given IP-Address");
		
		//Searching for the Ip-Address-Identifier.
		Identifier ipAddressIdentifier = getIpAddressIdentifier(requestType,ip);

		ArrayList<MacAddress> macAddresses = new ArrayList<MacAddress>();

		//Searching for the corresponding MAC-Addresses for Ip-Address ip.
		if (ipAddressIdentifier != null) {
			//Check all links.
			for (Link ipAddressLink:ipAddressIdentifier.getLinks()) {
				IdentifierPair ids = ipAddressLink.getIdentifiers();
				Identifier first = ids.getFirst();
				Identifier second = ids.getSecond();

				//First identifier of the link is the access-request.
				if (first.getTypeName().equals("access-request")) {
					//Using the FinderHelper-class to find all associated MAC-Addresses.
					MacAddressHelper.findMacAddressesFromIdentifier(first, macAddresses);

					//Second identifier of the link is the access-request.
				} else if (second.getTypeName().equals("access-request")) {
					//Using the FinderHelper-class to find all associated MAC-Addresses.
					MacAddressHelper.findMacAddressesFromIdentifier(second, macAddresses);
					
					//First identifier of the link is the MAC-Address.
				} else if (first.getTypeName().equals("mac-address")) {
					MacAddressHelper.findDirectlyLinkedMacAddresses(first, macAddresses);
					
					//Second identifier of the link is the MAC-Address.
				} else if (second.getTypeName().equals("mac-address")) {
					MacAddressHelper.findDirectlyLinkedMacAddresses(second, macAddresses);
				} 
			}
		}
		return macAddresses;
	}

	/**
	 * Query-method to get all associated devices of a given IP-Address-Object at a 
	 * given Unix-Timestamp. The timestamp-attribute of the IP-Address-Object can be 
	 * chosen freely to narrow down the search-results. In the IF-MAP-Graph only devices
	 * with an earlier or same publish-timestamp as the timestamp-attribute of the given
	 * IP-Address-Object will be found.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param ip  
	 * IP-Address to which the devices should be searched.
	 * @return
	 * Returns a list of all devices of the given IP-Address.
	 */
	public ArrayList<Device> getDevices(RequestType requestType,IpAddress ip) throws MetalyzerAPIException{
		log.info("Searching for the devices of a given IP-Address");
		
		//Searching for the IP-Address-Identifier.
		Identifier ipAddressIdentifier = getIpAddressIdentifier(requestType,ip);

		ArrayList<Device> devices = new ArrayList<Device>();

		//Searching for the corresponding devices for the IP-Address.
		if (ipAddressIdentifier != null) {
			//Check all links.
			for (Link ipAddressLink:ipAddressIdentifier.getLinks()) {
				IdentifierPair ids = ipAddressLink.getIdentifiers();
				Identifier first = ids.getFirst();
				Identifier second = ids.getSecond();

				//First identifier of the link is the access-request.
				if (first.getTypeName().equals("access-request")) {
					//DeviceHelper.findDevicesFromIdentifier(first, devices);
					DeviceHelper.findAccessRequestDevices(first, devices);
					
					//Second identifier of the link is the access-request.
				} else if (second.getTypeName().equals("access-request")) {
					//DeviceHelper.findDevicesFromIdentifier(second, devices);
					DeviceHelper.findAccessRequestDevices(second, devices);
					
					//First identifier of the link is the MAC-Address.
				} else if (first.getTypeName().equals("mac-address")) {
					DeviceHelper.findDevicesOverIdentifier(first, devices);
					
					//Second identifier of the link is the MAC-Address.
				} else if (second.getTypeName().equals("mac-address")) {
					DeviceHelper.findDevicesOverIdentifier(second, devices);
					
					//First or second identifier of the link is the device.
				} else if (first.getTypeName().equals("device") || second.getTypeName().equals("device")) {
					DeviceHelper.findDevicesFromIdentifier(ipAddressIdentifier, devices);
				}
			}
		}
		return devices;
	}

	
	//########################### Helper-Methods ###########################
	
	/**
	 * Helper-method to get the corresponding Identifier of a given IP-Address-Object.
	 * The timestamp-attributes of the IP-Address-Object can be chosen freely to get the 
	 * identifier in a specific situation of the IF-MAP-Graph.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param ip  
	 * IP-Address to which the corresponding identifier should be searched.
	 * @return
	 * Returns the identifier of the given IP-Address.
	 */
	private Identifier getIpAddressIdentifier(RequestType requestType,IpAddress ip) throws MetalyzerAPIException{
		
		//Searching for all Ip-Address-identifiers.
		Collection<Identifier> ipAddressList = null;
		if (requestType == RequestType.TIMESTAMP_REQUEST) {
			ipAddressList = getAllIpAddressIdentifiers(ip.getSearchTimestamp());
		} else if (requestType == RequestType.TIMEINTERVAL_REQUEST) {
			ipAddressList = getAllIpAddressIdentifiers(ip.getSearchFromTimestamp(), ip.getSearchToTimestamp());
		} else if (requestType == RequestType.CURRENT_REQUEST) {
			ipAddressList = getAllIpAddressIdentifiers();
		}
		
		log.debug("Available IP-Addresses: " + ipAddressList.toString());
		
		//Searching for the corresponding identifier for IP-Address.
		Identifier ipAddressIdentifier = null;
		Iterator<Identifier> ipAddressIterator = ipAddressList.iterator();
		boolean foundIpAddress = false;
		while (ipAddressIterator.hasNext() && !foundIpAddress) {
			ipAddressIdentifier = ipAddressIterator.next();
			if (ipAddressIdentifier.getTypeName().equals("ip-address")) {
				if (ipAddressIdentifier.valueFor("/ip-address[@value]").equals(ip.getAddress())) {
					foundIpAddress = true;
				}
			}
		}

		//Check if the IpAddress-identifier was found.
		if (foundIpAddress) {
			return ipAddressIdentifier;
		} else {
			return null;
		}
	}

	/**
	 * Helper-method to get a collection of all Ip-Address-Identifiers at a given Unix-Timestamp.
	 * @param timestamp  
	 * Unix-Timestamp to which the Ip-Address-identifiers should be searched.
	 * @return
	 * Returns a collection of all Ip-Address-identifiers at the given Unix-Timestamp.
	 */
	private Collection<Identifier> getAllIpAddressIdentifiers(long timestamp) throws MetalyzerAPIException{
		log.info("Request to get all IP-Address-Identifiers from the MetalyzerAPI at timestamp " + timestamp);
		return semCon.getAPI().getIdentifierFinder().get(StandardIdentifierType.IP_ADDRESS, timestamp);
	}
	/**
	 * Helper-method to get a collection of all IP-Address-Identifiers at a given period of time.
	 * @param fromTimestamp  
	 * Unix-Timestamp of the beginning of the search period.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search period.
	 * @return
	 * Returns a collection of all IP-Address-identifiers at the given period of time.
	 */
	private Collection<Identifier> getAllIpAddressIdentifiers(long fromTimestamp, long toTimestamp) throws MetalyzerAPIException{
		log.info("Request to get all IP-Address-Identifiers from the MetalyzerAPI at interval [" + fromTimestamp + ", " + toTimestamp + "]");
		MetalyzerDelta<Identifier> delta = semCon.getAPI().getIdentifierFinder().get(StandardIdentifierType.IP_ADDRESS, fromTimestamp, toTimestamp);
		//return delta.getAvailables();
		return delta.getUpdates();
	}
	
	/**
	 * Helper-method to get a collection of all IP-Address-Identifiers at the current timestamp.
	 * @return
	 * Returns a collection of all IP-Address-identifiers at the current timestamp.
	 */
	private Collection<Identifier> getAllIpAddressIdentifiers() throws MetalyzerAPIException{
		log.info("Request to get all IP-Address-Identifiers from the MetalyzerAPI at the current timestamp");
		return semCon.getAPI().getIdentifierFinder().getCurrent(StandardIdentifierType.IP_ADDRESS);
	}

}
