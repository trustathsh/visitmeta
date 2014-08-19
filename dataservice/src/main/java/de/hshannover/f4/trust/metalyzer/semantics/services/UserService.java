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

import java.util.Collection;
import java.util.ArrayList;
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
import de.hshannover.f4.trust.metalyzer.semantics.helper.IpAddressHelper;
import de.hshannover.f4.trust.metalyzer.semantics.helper.MacAddressHelper;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController.RequestType;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierPair;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Service-class to provide query-methods to search information for specific users.
 * The UserService provides methods for timestamp-, timeinterval- and current-requests.
 *
 */
public class UserService{
	// SemanticsController for the connection to User-REST-API and MetalyzerAPI: 
	private SemanticsController semCon;
	
	// Logging:
	private static final Logger log = Logger.getLogger(UserService.class);

	/**
	 * Constructor
	 * Uses the getInstance-method to set the SemanticsController-Attribute.
	 */
	public UserService() {
		semCon = SemanticsController.getInstance();
	}

	/**
	 * Constructor
	 * Uses a parameter to set the SemanticsController-Attribute.
	 * Mainly used in MacAddressService-UnitTests.
	 * @param semCon
	 * SemanticsController
	 */
	public UserService(SemanticsController semCon) {
		this.semCon = semCon;
	}

	/**
	 * Query-method to get all users at a given Unix-Timestamp.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param fromTimestamp
	 * Unix-Timestamp of the beginning of the search-interval.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search-interval.
	 * @return
	 * Returns a list of all users at the given Unix-Timestamp.
	 */
	public ArrayList<User> getUsers(RequestType requestType, long fromTimestamp, long toTimestamp) throws MetalyzerAPIException {
		log.info("Searching for all User-Objects");
		
		//Searching for all user-identifiers (timestamp, timeinterval, current).
		Collection<Identifier> userList = null;
		if (requestType == RequestType.TIMESTAMP_REQUEST) {
			userList = getAllUserIdentifiers(fromTimestamp);
		} else if (requestType == RequestType.TIMEINTERVAL_REQUEST) {
			userList = getAllUserIdentifiers(fromTimestamp, toTimestamp);
		} else if (requestType == RequestType.CURRENT_REQUEST) {
			userList = getAllUserIdentifiers();
		}

		ArrayList<User> users = new ArrayList<User>();
		String username;
		String role;
		long publishTimestamp = 0;
		
		for (Identifier userIdentifier : userList) {
			username= "";
			role= "";
			if(!userIdentifier.valueFor("/identity[@type]").equals("other")) {
				username = userIdentifier.valueFor("/identity[@name]");
				for (Link userLink : userIdentifier.getLinks()) {
					for (Metadata linkMetadata : userLink.getMetadata()) {
						//Searching for the corresponding publish-timestamp:
						if (linkMetadata.getTypeName().equals("authenticated-as")) {
							publishTimestamp = linkMetadata.getPublishTimestamp();
						}

						//Searching for the corresponding role:
						if (linkMetadata.getTypeName().equals("role")) {
							role = linkMetadata.valueFor("/meta:role/name");
						}
					}
				}
				users.add(new User(publishTimestamp, username, role));
			}
		}
		return users;
	}

	/**
	 * Query-method to get the publish-timestamp of a specific User-Object.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param user
	 * User to which the Unix-Timestamp should be searched.
	 * @return
	 * Returns the Unix-Timestamp of the given user.
	 */
	public long getTimestamp(RequestType requestType, User user) throws MetalyzerAPIException {
		log.info("Searching for the publish-timestamp of a given user");
		
		//Searching for the User-Identifier.
		Identifier userIdentifier = getUserIdentifier(requestType, user);
		
		Metadata authenticatedAs = null;

		//Searching for the publish-timestamp for the user.
		if (userIdentifier != null) {
			//Check all links.
			for (Link userLink : userIdentifier.getLinks()) {
				for (Metadata linkMetadata : userLink.getMetadata()) {
					if (linkMetadata.getTypeName().equals("authenticated-as")) {
						authenticatedAs = linkMetadata;
						break;
					}
				}
			}
		}
		
		if (authenticatedAs != null) {
			//Try to get the publish-timestamp from the authenticated-as-metadata.
			return authenticatedAs.getPublishTimestamp();
		} else {
			//Else no publish-timestamp was found, so set it to 0.
			return 0;
		}
	}

	/**
	 * Query-method to get all associated IP-Addresses of a given User-Object at a 
	 * given Unix-Timestamp. The timestamp-attribute of the User-Object can be chosen
	 * freely to narrow down the search-results. In the IF-MAP-Graph only IP-Addresses
	 * with an earlier or same publish-timestamp as the timestamp-attribute of the given
	 * User-Object will be found.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param user
	 * User to which the IP-Addresses should be searched.
	 * @return
	 * Returns a list of all IP-Addresses of the given user.
	 */
	public ArrayList<IpAddress> getIpAddresses(RequestType requestType, User user) throws MetalyzerAPIException {
		log.info("Searching for the IP-Addresses of a given user");
		
		//Searching for the User-Identifier.
		Identifier userIdentifier = getUserIdentifier(requestType, user);

		ArrayList<IpAddress> ipAddresses = new ArrayList<IpAddress>();

		//Searching for the corresponding IP-Addresses for the user.
		if (userIdentifier != null) {
			//Check all links.
			for (Link userLink : userIdentifier.getLinks()) {
				IdentifierPair identifierPair = userLink.getIdentifiers();
				Identifier first = identifierPair.getFirst();
				Identifier second = identifierPair.getSecond();

				//First identifier of the link is the access-request.
				if (first.getTypeName().equals("access-request")) {
					IpAddressHelper.findIPAddressesFromIdentifier(first, ipAddresses); //IP-Addresses over Access-Request
					IpAddressHelper.findIpAddressesOverMacAddresses(first, ipAddresses); //IP-Addresses over MAC-Address

					//Second identifier of the link is the access-request.
				} else if (second.getTypeName().equals("access-request")) {
					IpAddressHelper.findIPAddressesFromIdentifier(second, ipAddresses); //IP-Addresses over Access-Request
					IpAddressHelper.findIpAddressesOverMacAddresses(second, ipAddresses); //IP-Addresses over MAC-Address
				}
			}
		}
		return ipAddresses;
	}


	/**
	 * Query-method to get all associated MAC-Addresses of a given User-Object at a 
	 * given Unix-Timestamp. The timestamp-attribute of the User-Object can be chosen
	 * freely to narrow down the search-results. In the IF-MAP-Graph only MAC-Addresses
	 * with an earlier or same publish-timestamp as the timestamp-attribute of the given
	 * User-Object will be found.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param user
	 * User to which the MAC-Addresses should be searched.
	 * @return
	 * Returns a list of all MAC-Addresses of the given user.
	 */
	public ArrayList<MacAddress> getMacAddresses(RequestType requestType, User user) throws MetalyzerAPIException {
		log.info("Searching for the MAC-Addresses of a given user");
		
		//Searching for the User-Identifier.
		Identifier userIdentifier = getUserIdentifier(requestType, user);

		ArrayList<MacAddress> macAddresses = new ArrayList<MacAddress>();

		//Searching for the corresponding MAC-Addresses for the user.
		if (userIdentifier != null) {
			//Check all links.
			for (Link userLink : userIdentifier.getLinks()) {
				IdentifierPair identifierPair = userLink.getIdentifiers();
				Identifier first = identifierPair.getFirst();
				Identifier second = identifierPair.getSecond();

				//First identifier of the link is the access-request.
				if (first.getTypeName().equals("access-request")) {
					MacAddressHelper.findMacAddressesFromIdentifier(first, macAddresses); //MAC-Addresses over Access-Request
					MacAddressHelper.findMacAddressesOverIpAddresses(first, macAddresses); //MAC-Addresses over IP-Address

					//Second identifier of the link is the access-request.
				} else if (second.getTypeName().equals("access-request")) {
					MacAddressHelper.findMacAddressesFromIdentifier(second, macAddresses); //MAC-Addresses over Access-Request
					MacAddressHelper.findMacAddressesOverIpAddresses(second, macAddresses); //MAC-Addresses over IP-Address
				}
			}
		}
		return macAddresses;
	}

	/**
	 * Query-method to get all associated devices of a given User-Object at a 
	 * given Unix-Timestamp. The timestamp-attribute of the User-Object can be 
	 * chosen freely to narrow down the search-results. In the IF-MAP-Graph only
	 * devices with an earlier or same publish-timestamp as the timestamp-attribute
	 * of the given User-Object will be found.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param user  
	 * User to which the devices should be searched.
	 * @return
	 * Returns a list of all devices of the given user.
	 */
	public ArrayList<Device> getDevices(RequestType requestType, User user) throws MetalyzerAPIException {
		log.info("Searching for the devices of a given user");
		
		//Searching for the User-Identifier.
		Identifier userIdentifier = getUserIdentifier(requestType, user);

		ArrayList<Device> devices = new ArrayList<Device>();

		//Searching for the corresponding devices for the user.
		if (userIdentifier != null) {
			//Check all links.
			for (Link userLink : userIdentifier.getLinks()) {
				IdentifierPair identifierPair = userLink.getIdentifiers();
				Identifier first = identifierPair.getFirst();
				Identifier second = identifierPair.getSecond();

				//First identifier of the link is the access-request.
				if (first.getTypeName().equals("access-request")) {
					//DeviceHelper.findDevicesFromIdentifier(first, devices); //to find infrastructure-devices (Switch etc.)
					DeviceHelper.findAccessRequestDevices(first, devices); 

					//Second identifier of the link is the access-request.
				} else if (second.getTypeName().equals("access-request")) {
					//DeviceHelper.findDevicesFromIdentifier(second, devices); //to find infrastructure-devices (Switch etc.)
					DeviceHelper.findAccessRequestDevices(second, devices);
				}
			}
		}
		return devices;
	}
	
	
	//########################### Helper-Methods ###########################

	
	/**
	 * Helper-method to get the corresponding Identifier of a given User-Object.
	 * The timestamp-attributes of the User-Object can be chosen freely to get the 
	 * identifier in a specific situation of the IF-MAP-Graph.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param user  
	 * User to which the corresponding identifier should be searched.
	 * @return
	 * Returns the identifier of the given user.
	 */
	private Identifier getUserIdentifier(RequestType requestType, User user) throws MetalyzerAPIException {
		//Searching for all user-identifiers.
		Collection<Identifier> userList = null;
		if (requestType == RequestType.TIMESTAMP_REQUEST) {
			userList = getAllUserIdentifiers(user.getSearchTimestamp());
		} else if (requestType == RequestType.TIMEINTERVAL_REQUEST) {
			userList = getAllUserIdentifiers(user.getSearchFromTimestamp(), user.getSearchToTimestamp());
		} else if (requestType == RequestType.CURRENT_REQUEST) {
			userList = getAllUserIdentifiers();
		}
		
		log.debug("Available users: " + userList.toString());

		//Searching for the corresponding identifier for the user.
		Identifier userIdentifier = null;
		Iterator<Identifier> userIterator = userList.iterator();
		boolean foundUser = false;
		while (userIterator.hasNext() && !foundUser) {
			userIdentifier = userIterator.next();
			if (userIdentifier.getTypeName().equals("identity")) {
				if (userIdentifier.valueFor("/identity[@name]").equals(user.getName())) {
					foundUser = true;
				}
			}
		}
		
		//Check if the User-Identifier was found.
		if (foundUser) {
			return userIdentifier;
		} else {
			return null;
		}
	}

	/**
	 * Helper-method to get a collection of all User-Identifiers at a given Unix-Timestamp.
	 * @param timestamp  
	 * Unix-Timestamp to which the user-identifiers should be searched.
	 * @return
	 * Returns a collection of all user-identifiers at the given Unix-Timestamp.
	 */
	private Collection<Identifier> getAllUserIdentifiers(long timestamp) throws MetalyzerAPIException {
		log.info("Request to get all User-Identifiers from the MetalyzerAPI at timestamp " + timestamp);
		
		return semCon.getConnection().getIdentifierFinder().get(StandardIdentifierType.IDENTITY, timestamp);
	}
	
	/**
	 * Helper-method to get a collection of all User-Identifiers at a given period of time.
	 * @param fromTimestamp  
	 * Unix-Timestamp of the beginning of the search period.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search period.
	 * @return
	 * Returns a collection of all User-Identifiers at the given period of time.
	 */
	private Collection<Identifier> getAllUserIdentifiers(long fromTimestamp, long toTimestamp) throws MetalyzerAPIException {
		log.info("Request to get all User-Identifiers from the MetalyzerAPI at interval [" + fromTimestamp + ", " + toTimestamp + "]");
		MetalyzerDelta<Identifier> delta = semCon.getConnection().getIdentifierFinder().get(StandardIdentifierType.IDENTITY, fromTimestamp, toTimestamp);
		
		//return delta.getAvailables(); //To get all available MacAddresses at the timeinterval.
		return delta.getUpdates(); //To get just the updates of MacAddresses at the timeinterval.
	}
	
	/**
	 * Helper-method to get a collection of all User-Identifiers at the current timestamp.
	 * @return
	 * Returns a collection of all User-Identifiers at the current timestamp.
	 */
	private Collection<Identifier> getAllUserIdentifiers() throws MetalyzerAPIException {
		log.info("Request to get all User-Identifiers from the MetalyzerAPI at the current timestamp");
		
		return semCon.getConnection().getIdentifierFinder().getCurrent(StandardIdentifierType.IDENTITY);
	}
}
