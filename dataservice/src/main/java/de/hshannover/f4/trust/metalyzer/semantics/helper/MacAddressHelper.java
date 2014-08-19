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

import de.hshannover.f4.trust.metalyzer.semantics.entities.MacAddress;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierPair;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Helper-Class which provides MAC-Address-Helper-Methods to extract information out of the IF-MAP-Graph.
 *
 */
public abstract class MacAddressHelper {
	
	// Logging:
	private static final Logger log = Logger.getLogger(MacAddressHelper.class);
	
	/**
	 * Helper-Method to find MacAddress-Identifiers from other identifiers (mainly used with access-request-identifiers).
	 * @param accessRequest
	 * Identifier to start from (should be an access-request).
	 * @param macAddresses
	 * ArrayList where the MAC-Addresses will be saved in.
	 */
	public static void findMacAddressesFromIdentifier(Identifier accessRequest, ArrayList<MacAddress> macAddresses) {
		log.info("Call of the helper-method 'findMacAddressesFromIdentifier'");
		
		//Check all links.
		for (Link link : accessRequest.getLinks()) {
			IdentifierPair identifierPair = link.getIdentifiers();
			Identifier first = identifierPair.getFirst();
			Identifier second = identifierPair.getSecond();

			//Searching for the publish-timestamp of the MAC-Address.
			long timestamp = 0;
			for (Metadata meta : link.getMetadata()) {
				if(meta.getTypeName().equals("access-request-mac") || meta.getTypeName().equals("ip-mac")) {
					timestamp = meta.getPublishTimestamp();
				}
			}

			//First identifier of the link is the MAC-Address.
			if (first.getTypeName().equals("mac-address")) {
				MacAddress mac = new MacAddress(timestamp, first.valueFor("/mac-address[@value]"));
				
				//Check for duplicates.
				if (!macAddresses.contains(mac)) {
					macAddresses.add(mac);
				}

				//Second identifier of the link is the MAC-Address.
			} else if(second.getTypeName().equals("mac-address")){
				MacAddress mac = new MacAddress(timestamp, second.valueFor("/mac-address[@value]"));
				
				//Check for duplicates.
				if (!macAddresses.contains(mac)) {
					macAddresses.add(mac);
				}
			}
		}
	}

	/**
	 * Helper method to find directly linked MAC-Addresses (over ip-mac-metadata).
	 * @param macAddress
	 * MAC-Address-identifier
	 * @param macAddresses
	 * ArrayList where the MAC-Addresses will be saved in.
	 */
	public static void findDirectlyLinkedMacAddresses(Identifier macAddress, ArrayList<MacAddress> macAddresses) {
		log.info("Call of the helper-method 'findDirectlyLinkedMacAddresses'");
		
		long publishTimestamp;
		Metadata accessRequestMac = null;
		Metadata ipMac = null;

		//Searching for the publish-timestamp of the MAC-Address.
		for (Link macAddressLink : macAddress.getLinks()) {
			for (Metadata metadata : macAddressLink.getMetadata()) {
				if (metadata.getTypeName().equals("access-request-mac")) {
					accessRequestMac = metadata;
				} else if (metadata.getTypeName().equals("ip-mac")) {
					ipMac = metadata;
				}
			}
		}

		//If the MAC-Address is linked with an Access-Request then get the publish-timestamp from the access-request-mac-metadata.
		if (accessRequestMac != null) {
			publishTimestamp = accessRequestMac.getPublishTimestamp();

			//If the MAC-Address is not linked with an Access-Request then get the publish-timestamp from the ip-mac-metadata.
		} else if (ipMac != null) {
			publishTimestamp = ipMac.getPublishTimestamp();

			//Else the publish-timestamp is set to 0.
		} else {
			publishTimestamp = 0;
		}

		//Creating the new MAC-Address-Object.
		MacAddress mac = new MacAddress(publishTimestamp, macAddress.valueFor("/mac-address[@value]"));
		
		//Check for duplicates.
		if (!macAddresses.contains(mac)) {
			macAddresses.add(mac);
		}
	}
	
	/**
	 * Helper method to find MAC-Addresses which are linked with IP-Addresses over ip-mac-metadata.
	 * @param accessRequest
	 * Access-Request-Identifier
	 * @param macAddresses
	 * ArrayList where the MAC-Addresses will be saved in.
	 */
	public static void findMacAddressesOverIpAddresses(Identifier accessRequest, ArrayList<MacAddress> macAddresses) {
		log.info("Call of the helper-method 'findMacAddressesOverIpAddresses'");
		
		//Check all links.
		for (Link link : accessRequest.getLinks()) {
			IdentifierPair identifierPair = link.getIdentifiers();
			Identifier first = identifierPair.getFirst();
			Identifier second = identifierPair.getSecond();
			
			//First identifier of the link is the IP-Address.
			if (first.getTypeName().equals("ip-address")) {
				findMacAddressesFromIdentifier(first, macAddresses);
				
				//Second identifier of the link is the IP-Address.
			} else if (second.getTypeName().equals("ip-address")) {
				findMacAddressesFromIdentifier(second, macAddresses);
			}
		}
	}
}
