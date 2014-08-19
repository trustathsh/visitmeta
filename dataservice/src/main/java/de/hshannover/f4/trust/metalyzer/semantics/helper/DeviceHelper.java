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

import de.hshannover.f4.trust.metalyzer.semantics.entities.Device;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierPair;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Helper-Class which provides Device-Helper-Methods to extract information out of the IF-MAP-Graph.
 *
 */
public abstract class DeviceHelper {
	
	//Logging:
	private static final Logger log = Logger.getLogger(DeviceHelper.class);
	
	/**
	 * Helper-Method to find device-identifiers from other identifiers (mainly used with access-request-identifiers).
	 * @param accessRequest
	 * Identifier to start from (should be an access-request).
	 * @param devices
	 * ArrayList where the devices will be saved in.
	 */
	public static void findDevicesFromIdentifier(Identifier accessRequest, ArrayList<Device> devices) {
		log.info("Call of the helper-method 'findDevicesFromIdentifier'");
		
		//Check all links.
		for (Link link : accessRequest.getLinks()) {
			IdentifierPair identifierPair = link.getIdentifiers();
			Identifier first = identifierPair.getFirst();
			Identifier second = identifierPair.getSecond();

			//Searching for the publish-timestamp and the attributes of the device.
			long timestamp = 0;
			ArrayList<String> attributes = new ArrayList<String>();
			for (Metadata meta : link.getMetadata()) {
				if(meta.getTypeName().equals("authenticated-by") || meta.getTypeName().equals("access-request-device") || meta.getTypeName().equals("device-ip")) {
					timestamp = meta.getPublishTimestamp();
				}

				if(meta.getTypeName().equals("device-attribute")) {
					String attribute = meta.valueFor("/meta:device-attribute/name");
					
					//Check for duplicates.
					if (!attributes.contains(attribute)) {
						attributes.add(attribute);
					}
				}
			}

			//First identifier of the link is the device.
			if (first.getTypeName().equals("device")) {
				Device d = new Device(timestamp, first.valueFor("/device/name"), attributes);
				
				//Check for duplicates.
				if (!devices.contains(d)) {
					devices.add(d);
				}

				//Second identifier of the link is the device.
			} else if(second.getTypeName().equals("device")) {
				Device d = new Device(timestamp, second.valueFor("/device/name"), attributes);
				
				//Check for duplicates.
				if (!devices.contains(d)) {
					devices.add(d);
				}
			}
		}
	}
	
	/**
	 * Helper-Method to find device-identifiers from any other identifier.
	 * @param identifier
	 * Identifier to start from (should be an IP-Address or a MAC-Address).
	 * @param devices
	 * ArrayList where the devices will be saved in.
	 */
	public static void findDevicesOverIdentifier(Identifier identifier, ArrayList<Device> devices) {
		log.info("Call of the helper-method 'findDeivcesOverIdentifier'");
		
		//Check all links.
		for (Link link : identifier.getLinks()) {
			IdentifierPair identifierPair = link.getIdentifiers();
			Identifier first = identifierPair.getFirst();
			Identifier second = identifierPair.getSecond();
			
			//First identifier of the link is the access-request.
			if (first.getTypeName().equals("access-request")) {
				findAccessRequestDevices(first, devices);
				
				//Second identifier of the link is the access-request.
			} else if (second.getTypeName().equals("access-request")) {
				findAccessRequestDevices(second, devices);
			}
		}
	}
	
	/**
	 * Helper-Method to find access-request-devices.
	 * @param accessRequest
	 * Identifier to start from (should be an access-request).
	 * @param devices
	 * ArrayList where the devices will be saved in.
	 */
	public static void findAccessRequestDevices(Identifier accessRequest, ArrayList<Device> devices) {
		log.info("Call of the helper-method 'findAccessRequestDevices'");
		
		//Check all links.
		for (Link link : accessRequest.getLinks()) {
			IdentifierPair identifierPair = link.getIdentifiers();
			Identifier first = identifierPair.getFirst();
			Identifier second = identifierPair.getSecond();

			//Searching for the publish-timestamp and the attributes of the device.
			long timestamp = 0;
			ArrayList<String> attributes = new ArrayList<String>();
			for (Metadata meta : link.getMetadata()) {
				if(meta.getTypeName().equals("access-request-device")) {
					timestamp = meta.getPublishTimestamp();
				}

				if(meta.getTypeName().equals("device-attribute")) {
					String attribute = meta.valueFor("/meta:device-attribute/name");
					
					//Check for duplicates.
					if (!attributes.contains(attribute)) {
						attributes.add(attribute);
					}
				}
			}

			//First identifier of the link is the device.
			if (first.getTypeName().equals("device") && timestamp > 0) {
				Device d = new Device(timestamp, first.valueFor("/device/name"), attributes);
				
				//Check for duplicates.
				if (!devices.contains(d)) {
					devices.add(d);
				}

				//Second identifier of the link is the device.
			} else if(second.getTypeName().equals("device") && timestamp > 0) {
				Device d = new Device(timestamp, second.valueFor("/device/name"), attributes);
				
				//Check for duplicates.
				if (!devices.contains(d)) {
					devices.add(d);
				}
			}
		}
	}
	
	/**
	 * Helper-Method to find authenticated-by-devices.
	 * @param accessRequest
	 * Identifier to start from (should be an access-request).
	 * @param devices
	 * ArrayList where the devices will be saved in.
	 */
	public static void findAuthenticatedByDevices(Identifier accessRequest, ArrayList<Device> devices) {
		log.info("Call of the helper-method 'findAuthenticatedByDevices'");
		
		//Check all links.
		for (Link link : accessRequest.getLinks()) {
			IdentifierPair identifierPair = link.getIdentifiers();
			Identifier first = identifierPair.getFirst();
			Identifier second = identifierPair.getSecond();

			//Searching for the publish-timestamp and the attributes of the device.
			long timestamp = 0;
			ArrayList<String> attributes = new ArrayList<String>();
			for (Metadata meta : link.getMetadata()) {
				if(meta.getTypeName().equals("authenticated-by")) {
					timestamp = meta.getPublishTimestamp();
				}

				if(meta.getTypeName().equals("device-attribute")) {
					String attribute = meta.valueFor("/meta:device-attribute/name");
					
					//Check for duplicates.
					if (!attributes.contains(attribute)) {
						attributes.add(attribute);
					}
				}
			}

			//First identifier of the link is the device.
			if (first.getTypeName().equals("device") && timestamp > 0) {
				Device d = new Device(timestamp, first.valueFor("/device/name"), attributes);
				
				//Check for duplicates.
				if (!devices.contains(d)) {
					devices.add(d);
				}

				//Second identifier of the link is the device.
			} else if(second.getTypeName().equals("device") && timestamp > 0) {
				Device d = new Device(timestamp, second.valueFor("/device/name"), attributes);
				
				//Check for duplicates.
				if (!devices.contains(d)) {
					devices.add(d);
				}
			}
		}
	}
}
