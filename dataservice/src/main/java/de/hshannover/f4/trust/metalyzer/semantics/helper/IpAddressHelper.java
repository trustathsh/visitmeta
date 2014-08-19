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
package de.hshannover.f4.trust.metalyzer.semantics.helper;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.semantics.entities.IpAddress;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierPair;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Helper-Class which provides IP-Address-Helper-Methods to extract information out of the IF-MAP-Graph.
 *
 */
public abstract class IpAddressHelper {
	
	//Logging: 
	private static final Logger log = Logger.getLogger(IpAddressHelper.class);
	
	/**
	 * Helper-Method to find IP-Address-identifiers from other identifiers (mainly used with access-request- and device-identifiers).
	 * @param identifier
	 * Identifier to start from (should be an access-request or device (with IP-Address linked via device-ip)).
	 * @param ipAddresses
	 * ArrayList where the IP-Addresses will be saved in.
	 */
	public static void findIPAddressesFromIdentifier(Identifier identifier, ArrayList<IpAddress> ipAddresses) {
		log.info("Call of the helper-method 'findIpAddressesFromIdentifier'");
		
		//Check all links.
		for (Link link : identifier.getLinks()) {
			IdentifierPair identifierPair = link.getIdentifiers();
			Identifier first = identifierPair.getFirst();
			Identifier second = identifierPair.getSecond();

			//Searching for the publish-timestamp of the IP-Address.
			long timestamp = 0;
			for (Metadata meta : link.getMetadata()) {
				if(meta.getTypeName().equals("access-request-ip") || meta.getTypeName().equals("device-ip") || meta.getTypeName().equals("ip-mac")) {
					timestamp = meta.getPublishTimestamp();
				}
			}

			//First identifier of the link is the IP-Address.
			if (first.getTypeName().equals("ip-address")) {
				IpAddress ip = new IpAddress(timestamp, first.valueFor("/ip-address[@value]"), first.valueFor("/ip-address[@type]"));
				
				//Check for duplicates.
				if (!ipAddresses.contains(ip)) {
					ipAddresses.add(ip);
				}

				//Second identifier of the link is the IP-Address.
			} else if(second.getTypeName().equals("ip-address")) {
				IpAddress ip = new IpAddress(timestamp, second.valueFor("/ip-address[@value]"), second.valueFor("/ip-address[@type]"));
				
				//Check for duplicates.
				if (!ipAddresses.contains(ip)) {
					ipAddresses.add(ip);
				}
			}
		}
	}

	/**
	 * Helper-Method to find IP-Address-identifiers from other identifiers.
	 * @param identifier
	 * Identifier to start from (should be an access-request or device (with IP-Address linked via device-ip)).
	 * @param ipAddresses
	 * ArrayList where the IP-Addresses will be saved in.
	 */
	public static void findIPAddressIdentfiersFromIdentifier(Identifier identifier, ArrayList<Identifier> ipAddresses) {
		log.info("Call of the helper-method 'findIpAddressIdentifiersFromIdentifier'");
		
		//Check all links.
		for (Link link : identifier.getLinks()) {
			IdentifierPair identifierPair = link.getIdentifiers();
			Identifier first = identifierPair.getFirst();
			Identifier second = identifierPair.getSecond();

			//First identifier of the link is the IP-Address.
			if (first.getTypeName().equals("ip-address")) {
				
				//Check for duplicates.
				if (!ipAddresses.contains(first)) {
					ipAddresses.add(first);
				}

				//Second identifier of the link is the IP-Address.
			} else if(second.getTypeName().equals("ip-address")) {
				
				//Check for duplicates.
				if (!ipAddresses.contains(second)) {
					ipAddresses.add(second);
				}
			}
		}
	}

	/**
	 * Helper method to find the IP-Addresses of associated device-identifiers.
	 * @param accessRequest
	 * Access-Request-identifier
	 * @param ipAddresses
	 * ArrayList where the IP-Addresses will be saved in.
	 */
	public static void findDeviceIpAddresses(Identifier accessRequest, ArrayList<IpAddress> ipAddresses) {
		log.info("Call of the helper-method 'findDeviceIpAddresses'");
		
		//Searching for the device-identifier.
		for (Link link : accessRequest.getLinks()) {
			IdentifierPair identifierPair = link.getIdentifiers();
			Identifier first = identifierPair.getFirst();
			Identifier second = identifierPair.getSecond();

			if (first.getTypeName().equals("device")) {
				//Using the FinderHelper-class to find all associated IP-Addresses.
				IpAddressHelper.findIPAddressesFromIdentifier(first, ipAddresses);

				//Second identifier of the link is the access-request.
			} else if (second.getTypeName().equals("device")) {
				//Using the FinderHelper-class to find all associated IP-Addresses.
				IpAddressHelper.findIPAddressesFromIdentifier(second, ipAddresses);
			}
		}
	}

	/**
	 * Helper method to find directly linked IP-Addresses (over ip-mac-metadata).
	 * @param ipAddress
	 * IP-Address-identifier
	 * @param ipAddresses
	 * ArrayList where the IP-Addresses will be saved in.
	 */
	public static void findDirectlyLinkedIpAddresses(Identifier ipAddress, ArrayList<IpAddress> ipAddresses) {
		log.info("Call of the helper-method 'findDirectlyLinkedIpAddresses'");
		
		long publishTimestamp;
		Metadata accessRequestIp = null;
		Metadata ipMac = null;

		//Searching for the publish-timestamp of the IP-Address.
		for (Link ipAddressLink : ipAddress.getLinks()) {
			for (Metadata metadata : ipAddressLink.getMetadata()) {
				if (metadata.getTypeName().equals("access-request-ip")) {
					accessRequestIp = metadata;
				} else if (metadata.getTypeName().equals("ip-mac")) {
					ipMac = metadata;
				}
			}
		}

		//If the IP-Address is linked with an Access-Request then get the publish-timestamp from the access-request-ip-metadata.
		if (accessRequestIp != null) {
			publishTimestamp = accessRequestIp.getPublishTimestamp();

			//If the MAC-Address is not linked with an Access-Request then get the publish-timestamp from the ip-mac-metadata.
		} else if (ipMac != null) {
			publishTimestamp = ipMac.getPublishTimestamp();

			//Else the publish-timestamp is set to 0.
		} else {
			publishTimestamp = 0;
		}

		//Creating the new IP-Address-Object.
		IpAddress ip = new IpAddress(publishTimestamp, ipAddress.valueFor("/ip-address[@value]"), ipAddress.valueFor("/ip-address[@type]"));
		if (!ipAddresses.contains(ip)) {
			ipAddresses.add(ip);
		}
	}
	
	/**
	 * Helper method to find IP-Addresses which are linked with MAC-Addresses over ip-mac-metadata.
	 * @param accessRequest
	 * Access-Request-Identifier
	 * @param ipAddresses
	 * ArrayList where the IP-Addresses will be saved in.
	 */
	public static void findIpAddressesOverMacAddresses(Identifier accessRequest, ArrayList<IpAddress> ipAddresses) {
		log.info("Call of the helper-method 'findIpAddressesOverMacAddresses'");
		
		for (Link link : accessRequest.getLinks()) {
			IdentifierPair identifierPair = link.getIdentifiers();
			Identifier first = identifierPair.getFirst();
			Identifier second = identifierPair.getSecond();
			
			//First identifier of the link is the mac-address.
			if (first.getTypeName().equals("mac-address")) {
				findIPAddressesFromIdentifier(first, ipAddresses);
				
				//Second identifier of the link is the mac-address.
			} else if (second.getTypeName().equals("mac-address")) {
				findIPAddressesFromIdentifier(second, ipAddresses);
			}
		}
	}
}
