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
import de.hshannover.f4.trust.metalyzer.semantics.helper.IpAddressHelper;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController.RequestType;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierPair;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Service-class to provide query-methods to search information for specific MAC-Addresses.
 * The MacAddressService provides methods for timestamp-, timeinterval- and current-requests.
 *
 */
public class MacAddressService {
	// SemanticsController for the connection to MacAddress-REST-API and MetalyzerAPI: 
	private SemanticsController semCon;
	
	// Logging:
	private static final Logger log = Logger.getLogger(MacAddressService.class);

	/**
	 * Constructor
	 * Uses the getInstance-method to set the SemanticsController-Attribute.
	 */
	public MacAddressService() {
		semCon = SemanticsController.getInstance();
	}

	/**
	 * Constructor
	 * Uses a parameter to set the SemanticsController-Attribute.
	 * Mainly used in MacAddressService-UnitTests.
	 * @param semCon
	 * SemanticsController
	 */
	public MacAddressService(SemanticsController semCon) {
		this.semCon = semCon;
	}

	/**
	 * Query-method to get all MAC-Addresses at a given Unix-Timestamp.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param fromTimestamp
	 * Unix-Timestamp of the beginning of the search-interval.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search-interval.
	 * @return
	 * Returns a list of all MAC-Addresses at the given Unix-Timestamp.
	 */
	public ArrayList<MacAddress> getMacAddresses(RequestType requestType, long fromTimestamp, long toTimestamp) 
			throws MetalyzerAPIException {
		log.info("Searching for all MAC-Address-Objects");
		
		//Searching for the MAC-Address-identifiers (timestamp, timeinterval, current).
		Collection<Identifier> macAddressList = null;
		if (requestType == RequestType.TIMESTAMP_REQUEST) {
			macAddressList = getAllMacAddressIdentifiers(fromTimestamp);
		} else if (requestType == RequestType.TIMEINTERVAL_REQUEST) {
			macAddressList = getAllMacAddressIdentifiers(fromTimestamp, toTimestamp);
		} else if (requestType == RequestType.CURRENT_REQUEST) {
			macAddressList = getAllMacAddressIdentifiers();
		}

		ArrayList<MacAddress> macAddresses = new ArrayList<MacAddress>();
		String address = "";
		long publishTimestamp = 0;
		
		for (Identifier macAddressIdentifier : macAddressList) {
			//Searching for the address.
			address = macAddressIdentifier.valueFor("/mac-address[@value]");
			for (Link macAddressLink : macAddressIdentifier.getLinks()) {
				for (Metadata linkMetadata : macAddressLink.getMetadata()) {
					//Searching for the corresponding publish-timestamp of the MAC-Address.
					if (linkMetadata.getTypeName().equals("access-request-mac")) {
						publishTimestamp = linkMetadata.getPublishTimestamp();
					}
				}
			}
			MacAddress macAddress = new MacAddress(publishTimestamp, address);
			//Checking for duplicates.
			if (!macAddresses.contains(macAddress)) {
				macAddresses.add(macAddress);
			}
		}
		return macAddresses;
	}

	/**
	 * Query-method to get the publish-timestamp of a specific MAC-Address-Object.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param macAddress
	 * MAC-Address to which the Unix-Timestamp should be searched.
	 * @return
	 * Returns the Unix-Timestamp of the given MAC-Address.
	 */
	public long getTimestamp(RequestType requestType, MacAddress macAddress) throws MetalyzerAPIException {
		log.info("Searching for the publish-timestamp of a given MAC-Address");
		
		//Searching for the MAC-Address-Identifier.
		Identifier macAddressIdentifier = getMacAddressIdentifier(requestType, macAddress);

		Metadata accessRequestMac = null;
		Metadata ipMac = null;
		
		//Searching for the publish-timestamp for the MAC-Address.
		if (macAddressIdentifier != null) {
			//Check all links.
			for (Link macAddressLink : macAddressIdentifier.getLinks()) {
				for (Metadata linkMetadata : macAddressLink.getMetadata()) {
					if (linkMetadata.getTypeName().equals("access-request-mac")) {
						accessRequestMac = linkMetadata;
						break;
					} else if (linkMetadata.getTypeName().equals("ip-mac")) {
						ipMac = linkMetadata;
					}
				}
			}
		}
		
		if (accessRequestMac != null) {
			//First try to get the publish-timestamp from access-request-mac-metadata.
			return accessRequestMac.getPublishTimestamp();
		} else if (ipMac != null) {
			//Second try to get the publish-timestamp from ip-mac-metadata.
			return ipMac.getPublishTimestamp();
		} else {
			//Else no publish-timestamp was found, so set it to 0.
			return 0;
		}
	}

	/**
	 * Query-method to get all associated users of a given MAC-Address-Object at a 
	 * given Unix-Timestamp. The timestamp-attribute of the MAC-Address-Object can 
	 * be chosen freely to narrow down the search-results. In the IF-MAP-Graph only 
	 * users with an earlier or same publish-timestamp as the timestamp-attribute of
	 * the given MAC-Address-Object will be found.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param macAddress  
	 * MAC-Address to which the users should be searched.
	 * @return
	 * Returns a list of all users of the given MAC-Address.
	 */
	public ArrayList<User> getUsers(RequestType requestType, MacAddress macAddress) throws MetalyzerAPIException {
		log.info("Searching for the users of a given MAC-Address");
		
		//Searching for the MAC-Address-Identifier.
		Identifier macAddressIdentifier = getMacAddressIdentifier(requestType, macAddress);
		
		ArrayList<User> users = new ArrayList<User>();
		
		//Searching for the corresponding users for the MAC-Address.
		if (macAddressIdentifier != null) {
			//Check all links.
			for (Link macAddressLink : macAddressIdentifier.getLinks()) {
				IdentifierPair identifierPair = macAddressLink.getIdentifiers();
				Identifier first = identifierPair.getFirst();
				Identifier second = identifierPair.getSecond();
				
				//First identifier of the link is the access-request.
				if (first.getTypeName().equals("access-request")) {
					UserHelper.findUsersFromIdentifier(first, users);
					
					//Second identifier of the link is the access-request.
				} else if (second.getTypeName().equals("access-request")) {
					UserHelper.findUsersFromIdentifier(second, users);
					
					//First identifier of the link is the IP-Address.
				} else if (first.getTypeName().equals("ip-address")) {
					UserHelper.findUsersOverIdentifier(first, users);
					
					//Second identifier of the link is the IP-Address.
				} else if (second.getTypeName().equals("ip-address")) {
					UserHelper.findUsersOverIdentifier(second, users);
				}
			}
		}
		return users;
	}

	/**
	 * Query-method to get all associated devices of a given MAC-Address-Object at a 
	 * given Unix-Timestamp. The timestamp-attribute of the MAC-Address-Object can be 
	 * chosen freely to narrow down the search-results. In the IF-MAP-Graph only devices
	 * with an earlier or same publish-timestamp as the timestamp-attribute of the given
	 * MAC-Address-Object will be found.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param macAddress  
	 * MAC-Address to which the devices should be searched.
	 * @return
	 * Returns a list of all devices of the given MAC-Address.
	 */
	public ArrayList<Device> getDevices(RequestType requestType, MacAddress macAddress) throws MetalyzerAPIException {
		log.info("Searching for the devices of a given MAC-Address");
		
		//Searching for the MAC-Address-Identifier.
		Identifier macAddressIdentifier = getMacAddressIdentifier(requestType, macAddress);

		ArrayList<Device> devices = new ArrayList<Device>();

		//Searching for the corresponding devices for the MAC-Address.
		if (macAddressIdentifier != null) {
			//Check all links.
			for (Link macAddressLink : macAddressIdentifier.getLinks()) {
				IdentifierPair identifierPair = macAddressLink.getIdentifiers();
				Identifier first = identifierPair.getFirst();
				Identifier second = identifierPair.getSecond();

				//First identifier of the link is the access-request.
				if (first.getTypeName().equals("access-request")) {
					//DeviceHelper.findDevicesFromIdentifier(first, devices); //To find also infrastructure-devices (Switch etc.)
					DeviceHelper.findAccessRequestDevices(first, devices);

					//Second identifier of the link is the access-request.
				} else if (second.getTypeName().equals("access-request")) {
					//DeviceHelper.findDevicesFromIdentifier(second, devices); //To find also infrastructure-devices (Switch etc.)
					DeviceHelper.findAccessRequestDevices(second, devices);
					
					//First identifier of the link is the IP-Address.
				} else if (first.getTypeName().equals("ip-address")) {
					DeviceHelper.findDevicesOverIdentifier(first, devices);
					
					//Second identifier of the link is the IP-Address.
				} else if (second.getTypeName().equals("ip-address")) {
					DeviceHelper.findDevicesOverIdentifier(second, devices);
				}
			}
		}
		return devices;
	}

	/**
	 * Query-method to get all associated IP-Addresses of a given MAC-Address-Object at a 
	 * given Unix-Timestamp. The timestamp-attribute of the MAC-Address-Object can be chosen
	 * freely to narrow down the search-results. In the IF-MAP-Graph only IP-Addresses with 
	 * an earlier or same publish-timestamp as the timestamp-attribute of the given 
	 * MAC-Address-Object will be found.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param macAddress
	 * MAC-Address to which the IP-Addresses should be searched.
	 * @return
	 * Returns a list of all IP-Addresses of the given MAC-Address.
	 */
	public ArrayList<IpAddress> getIpAddresses(RequestType requestType, MacAddress macAddress) throws MetalyzerAPIException {
		log.info("Searching for the IP-Addresses of a given MAC-Address");
		
		//Searching for the MAC-Address-Identifier.
		Identifier macAddressIdentifier = getMacAddressIdentifier(requestType, macAddress);

		ArrayList<IpAddress> ipAddresses = new ArrayList<IpAddress>();

		//Searching for the corresponding IP-Addresses for the MAC-Address.
		if (macAddressIdentifier != null) {
			//Check all links.
			for (Link macAddressLink : macAddressIdentifier.getLinks()) {
				IdentifierPair identifierPair = macAddressLink.getIdentifiers();
				Identifier first = identifierPair.getFirst();
				Identifier second = identifierPair.getSecond();

				//First identifier of the link is the access-request.
				if (first.getTypeName().equals("access-request")) {
					IpAddressHelper.findIPAddressesFromIdentifier(first, ipAddresses);

					//Second identifier of the link is the access-request.
				} else if (second.getTypeName().equals("access-request")) {
					IpAddressHelper.findIPAddressesFromIdentifier(second, ipAddresses);
					
					//First identifier of the link is the IP-Address.
				} else if (first.getTypeName().equals("ip-address")) {
					IpAddressHelper.findDirectlyLinkedIpAddresses(first, ipAddresses);
					
					//Second identifier of the link is the IP-Address.
				} else if (second.getTypeName().equals("ip-address")) {
					IpAddressHelper.findDirectlyLinkedIpAddresses(second, ipAddresses);
				}
			}
		}
		return ipAddresses;
	}
	
	
	//########################### Helper-Methods ###########################
	
	
	/**
	 * Helper-method to get the corresponding Identifier of a given MAC-Address-Object.
	 * The timestamp-attributes of the MAC-Address-Object can be chosen freely to get the 
	 * identifier in a specific situation of the IF-MAP-Graph.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param macAddress  
	 * MAC-Address to which the corresponding identifier should be searched.
	 * @return
	 * Returns the identifier of the given MAC-Address.
	 */
	private Identifier getMacAddressIdentifier(RequestType requestType, MacAddress macAddress) throws MetalyzerAPIException {
		//Searching for all MAC-Address-identifiers.
		Collection<Identifier> macAddressList = null;
		if (requestType == RequestType.TIMESTAMP_REQUEST) {
			macAddressList = getAllMacAddressIdentifiers(macAddress.getSearchTimestamp());
		} else if (requestType == RequestType.TIMEINTERVAL_REQUEST) {
			macAddressList = getAllMacAddressIdentifiers(macAddress.getSearchFromTimestamp(), macAddress.getSearchToTimestamp());
		} else if (requestType == RequestType.CURRENT_REQUEST) {
			macAddressList = getAllMacAddressIdentifiers();
		}
		
		log.debug("Available MAC-Addresses: " + macAddressList.toString());
		
		//Searching for the corresponding identifier for the MAC-Address.
		Identifier macAddressIdentifier = null;
		Iterator<Identifier> macAddressIterator = macAddressList.iterator();
		boolean foundMacAddress = false;
		while (macAddressIterator.hasNext() && !foundMacAddress) {
			macAddressIdentifier = macAddressIterator.next();
			if (macAddressIdentifier.getTypeName().equals("mac-address")) {
				if (macAddressIdentifier.valueFor("/mac-address[@value]").equals(macAddress.getAddress())) {
					foundMacAddress = true;
				}
			}
		}
		
		//Check if the MacAddress-identifier was found.
		if (foundMacAddress) {
			return macAddressIdentifier;
		} else {
			return null;
		}
	}

	/**
	 * Helper-method to get a collection of all MAC-Address-Identifiers at a given Unix-Timestamp.
	 * @param timestamp  
	 * Unix-Timestamp to which the MAC-Address-identifiers should be searched.
	 * @return
	 * Returns a collection of all MAC-Address-identifiers at the given Unix-Timestamp.
	 */
	private Collection<Identifier> getAllMacAddressIdentifiers(long timestamp) throws MetalyzerAPIException {
		log.info("Request to get all MAC-Address-Identifiers from the MetalyzerAPI at timestamp " + timestamp);
		
		return semCon.getConnection().getIdentifierFinder().get(StandardIdentifierType.MAC_ADDRESS, timestamp);
	}
	
	/**
	 * Helper-method to get a collection of all MAC-Address-Identifiers at a given period of time.
	 * @param fromTimestamp  
	 * Unix-Timestamp of the beginning of the search period.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search period.
	 * @return
	 * Returns a collection of all MAC-Address-identifiers at the given period of time.
	 */
	private Collection<Identifier> getAllMacAddressIdentifiers(long fromTimestamp, long toTimestamp) throws MetalyzerAPIException {
		log.info("Request to get all MAC-Address-Identifiers from the MetalyzerAPI at interval [" + fromTimestamp + ", " + toTimestamp + "]");
		
		MetalyzerDelta<Identifier> delta = semCon.getConnection().getIdentifierFinder().get(StandardIdentifierType.MAC_ADDRESS, fromTimestamp, toTimestamp);
		//return delta.getAvailables(); //To get all available MacAddresses at the timeinterval.
		return delta.getUpdates(); //To get just the updates of MacAddresses at the timeinterval. 
	}
	
	/**
	 * Helper-method to get a collection of all MAC-Address-Identifiers at the current timestamp.
	 * @return
	 * Returns a collection of all MAC-Address-identifiers at the current timestamp.
	 */
	private Collection<Identifier> getAllMacAddressIdentifiers() throws MetalyzerAPIException {
		log.info("Request to get all MAC-Address-Identifiers from the MetalyzerAPI at the current timestamp");
		
		return semCon.getConnection().getIdentifierFinder().getCurrent(StandardIdentifierType.MAC_ADDRESS);
	}
}
