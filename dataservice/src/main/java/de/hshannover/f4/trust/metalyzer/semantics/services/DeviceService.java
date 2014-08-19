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

import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.api.MetalyzerDelta;
import de.hshannover.f4.trust.metalyzer.semantics.entities.Device;
import de.hshannover.f4.trust.metalyzer.semantics.entities.IpAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.MacAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.User;
import de.hshannover.f4.trust.metalyzer.semantics.helper.UserHelper;
import de.hshannover.f4.trust.metalyzer.semantics.helper.IpAddressHelper;
import de.hshannover.f4.trust.metalyzer.semantics.helper.MacAddressHelper;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController.RequestType;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierPair;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Service-class to provide query-methods to search information for specific Devices.
 * The DeviceService provides methods for timestamp-, timeinterval- and current-requests.
 * @author Sven
 *
 */

public class DeviceService {
	private SemanticsController semCon;
	private static final Logger log = Logger.getLogger(DeviceService.class);
	
	/**
	 * Default constructor uses getInstance of @link{SemanticsController}
	 */
	public DeviceService() {
		semCon = SemanticsController.getInstance();
	}

	/**
	 * Constructor to set a given SemanticsController (mainly used for Unittests)
	 * @param semCon
	 * @link{SemanticsController}
	 */
	public DeviceService(SemanticsController semCon){
		this.semCon= semCon;
	}

	/**
	 * Query-method to get all devices available at given Unix-Timestamp
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param fromTimestamp
	 * Unix-Timestamp of the beginning of the search-interval.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search-interval.
	 * @return
	 * Returns all devices available at the given Unix-Timestamp
	 */
	public ArrayList<Device> getDevices(RequestType requestType, long fromTimestamp, long toTimestamp) {
		log.info("---- DeviceService: getDevices Begin----");
		
		//get List of available device Identifier
		Collection<Identifier> deviceList = null;
		if (requestType == RequestType.TIMESTAMP_REQUEST) {
			log.info("    searching for Devices at timestamp: "+fromTimestamp);
			deviceList = getAllDeviceIdentifiers(fromTimestamp);
		} else if (requestType == RequestType.TIMEINTERVAL_REQUEST) {
			log.info("    searching for Devices in intervall: ["+fromTimestamp+","+toTimestamp+"]");
			deviceList = getAllDeviceIdentifiers(fromTimestamp, toTimestamp);
		} else if (requestType == RequestType.CURRENT_REQUEST) {
			log.info("    searching for Devices at current time");
			deviceList = getAllDeviceIdentifiers();
		}

		ArrayList<Device> devices= new ArrayList<Device>();

		for(Identifier deviceId : deviceList){
			long publishTimestamp= 0;
			ArrayList<String> attributes= new ArrayList<String>();
			
			for(Link link : deviceId.getLinks()){
				// search for publish timestamp of the device and search for attributes 
				// connected to it
				for(Metadata meta : link.getMetadata()){
					if(meta.getTypeName().equals("device-attribute")){
						String propName= "/meta:device-attribute/name";
						if(meta.hasProperty(propName)){
							attributes.add(meta.valueFor(propName));
						}
					} else if(meta.getTypeName().equals("access-request-device") || 
							meta.getTypeName().equals("authenticated-by") ||
							meta.getTypeName().equals("layer2-information")){
						publishTimestamp= meta.getPublishTimestamp();
						log.info("    found timestamp: "+publishTimestamp+" for "+meta.getTypeName()+" device");
					}
				}
			}
			Device device= null;
			// Add device to the list
			if(attributes.size() > 0){
				device= new Device(publishTimestamp,deviceId.valueFor("/device/name"),attributes);
			} else {
				device= new Device(publishTimestamp,deviceId.valueFor("/device/name"),attributes);
			}
			if(!devices.contains(device)){
				log.info("    adding device "+device);
				devices.add(device);
			}
		}
		log.info("---- DeviceService: getDevices End----");
		return devices;
	}

	/**
	 * Query-method to get the publish timestamp for a given device object
	 * @param d
	 * @link{Device} to which the Unix-Timestamp should be searched.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @return
	 * Returns the Unix-Timestamp of the given device.
	 */
	public long getTimestamp(RequestType requestType, Device d) {
		log.info("---- DeviceService: getTimestamp Begin----");
		// get the device Identifier
		Identifier deviceId= getDeviceIdentifier(requestType, d);
		long publishTimestamp= 0;

		if(deviceId != null){
			log.info("    using Identifier: "+deviceId.valueFor("/device/name"));
			for(Link link : deviceId.getLinks()){
				for(Metadata meta : link.getMetadata()){
					// search for the publish timestamp in the graph
					if(meta.getTypeName().equals("authenticated-by") || 
							meta.getTypeName().equals("access-request-device") || 
							meta.getTypeName().equals("layer2-information")){
						publishTimestamp= meta.getPublishTimestamp();
					}
				}
			}
		}
		log.info("---- DeviceService: getTimestamp End: found timestamp: "+ publishTimestamp +"----");
		return publishTimestamp;
	}

	/**
	 * Query-method to get all IP-Addresses available at given Unix-Timestamp
	 * for a given device. Searching for directly linked IP-Addresses via device-ip.
	 * Otherwise searching for access-request-ip or IP-Address linked to a MAC-Address linked via access-request-mac.
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param d
	 * @link{Device} to which the Unix-Timestamp should be searched.
	 * @return
	 * Returns a list of all IP-Addresses of the given device.
	 */
	public ArrayList<IpAddress> getIpAddresses(RequestType requestType, Device d) {
		log.info("---- DeviceService: getIPAddresses Begin ----");
		log.info("    Devicename: "+d.getName());
		ArrayList<IpAddress> deviceIpAdresses= new ArrayList<IpAddress>();
		
		// get the device Identifier
		Identifier device= getDeviceIdentifier(requestType, d);
		Link accessRequestLink= null;
		boolean found= false;
		if(device != null){
			log.info("    searching for IP-Addresses for device identifier "+device.valueFor("/device/name"));
			log.info("Identifier: "+device.getTypeName());
			log.info("Identifier Name: "+device.valueFor("/device/name"));
			
			// try to search IP-Addresses directly linked to the device
			for(Link deviceLink : device.getLinks()){
				IdentifierPair ids= deviceLink.getIdentifiers();
				Identifier first= ids.getFirst();
				Identifier second= ids.getSecond();
				if(first.getTypeName().equals("ip-address") || 
						second.getTypeName().equals("ip-address")){
					IpAddressHelper.findIPAddressesFromIdentifier(device, deviceIpAdresses);
					found= true;
					break;
				} else if(first.getTypeName().equals("access-request") || second.getTypeName().equals("access-request")){
					accessRequestLink= deviceLink;
				}
			}
			
			// if no directly linked IP-Address is found, search for the access-request-ips
			if(!found && accessRequestLink != null){
				log.info("AccessRequestLink");
				log.info("Found: "+found);
				for(Metadata arMeta : accessRequestLink.getMetadata()){
					Identifier first= accessRequestLink.getIdentifiers().getFirst();
					Identifier second= accessRequestLink.getIdentifiers().getSecond();
					if(arMeta.getTypeName().equals("access-request-device")){
						log.info("arMeta Type: "+arMeta.getTypeName());
						log.info("first: "+first.getTypeName());
						log.info("first Name: "+first.valueFor("/device/name"));
						log.info("first Name: "+first.valueFor("/access-request[@name]"));
						log.info("second: "+second.getTypeName());
						log.info("second Name: "+second.valueFor("/device/name"));
						log.info("second Name: "+second.valueFor("/access-request[@name]"));
						if(first.getTypeName().equals("access-request")){
							IpAddressHelper.findIPAddressesFromIdentifier(first, deviceIpAdresses);
							IpAddressHelper.findIpAddressesOverMacAddresses(first, deviceIpAdresses);
							break;
						} else if (second.getTypeName().equals("access-request")){
							IpAddressHelper.findIPAddressesFromIdentifier(second, deviceIpAdresses);
							IpAddressHelper.findIpAddressesOverMacAddresses(second, deviceIpAdresses);
							break;
						}
					}
				}
			}
		}
		log.info("---- Device Service getIPAddresses End ----");
		return deviceIpAdresses;
	}

	/**
	 * Query-method to get all MAC-Addresses available at given Unix-Timestamp
	 * for a given device. Searching for directly linked MAC-Addresses linked via ip-mac from a device-ip IP-Address Identifier.
	 * Otherwise searching for access-request-mac or MAC-Address linked to a IP-Address linked via access-request-ip. 
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param d
	 * @link{Device} to which the Unix-Timestamp should be searched.
	 * @return
	 * Returns a list of all MAC-Addresses of the given device.
	 */
	public ArrayList<MacAddress> getMacAddresses(RequestType requestType, Device d) {
		log.info("---- DeviceService: getMacAddresses Begin ----");
		log.info("    Devicename: "+d.getName());
		ArrayList<MacAddress> deviceMacAdresses= new ArrayList<MacAddress>();
		
		// get the device identifier
		Identifier device= getDeviceIdentifier(requestType, d);
		if(device != null){
			log.info("    searching for MAC-Address for device identifier "+device.valueFor("/device/name"));
			ArrayList<Identifier> ipIds= getIPIdentfiers(device);
			boolean found= false;
			
			for(Identifier ipId : ipIds){
				// try to find directly linked MAC-Addresses linked to the IP
				for(Link ipLink : ipId.getLinks()){
					IdentifierPair ids= ipLink.getIdentifiers();
					Identifier first= ids.getFirst();
					Identifier second= ids.getSecond();

					if(first.getTypeName().equals("mac-address")){
						log.info("    first is the Identifier");
						MacAddressHelper.findDirectlyLinkedMacAddresses(first, deviceMacAdresses);
						found= true;
						break;
					} else if (second.getTypeName().equals("mac-address")){
						log.info("    second is the Identifier");
						MacAddressHelper.findDirectlyLinkedMacAddresses(second, deviceMacAdresses);
						found= true;
						break;
					}
				}
			}
			
			// if no directly linked MAC-Address is found search from the access-request
			if(!found){
				log.info("    directly linked MAC not found, searching at access-request");
				for(Link deviceLink : device.getLinks()){
					boolean isArDevice= false;
					for(Metadata deviceMeta : deviceLink.getMetadata()){
						if(deviceMeta.getTypeName().equals("access-request-device")){
							log.info("    found access-request-device");
							isArDevice= true;
							break;
						}
					}
					if(isArDevice){
						log.info("    searching for MAC-addresses linked to access-request");
						IdentifierPair ids= deviceLink.getIdentifiers();
						Identifier first= ids.getFirst();
						Identifier second= ids.getSecond();

						if(first.getTypeName().equals("access-request")){
							MacAddressHelper.findMacAddressesFromIdentifier(first, deviceMacAdresses);
							MacAddressHelper.findMacAddressesOverIpAddresses(first, deviceMacAdresses);
						} else if (second.getTypeName().equals("access-request")){
							MacAddressHelper.findMacAddressesFromIdentifier(second, deviceMacAdresses);
							MacAddressHelper.findMacAddressesOverIpAddresses(second, deviceMacAdresses);
						}
					}
				}
			}
		}
		log.info("---- DeviceService: getMacAddresses End ----");
		return deviceMacAdresses;
	}

	/**
	 * Query-method to get all Users available at given Unix-Timestamp
	 * for a given device. Searching for the users linked to the access-request via authenticated-as
	 * @param requestType
	 * Type of the request (timestamp, interval, current).
	 * @param d
	 * @link{Device} to which the Unix-Timestamp should be searched.
	 * @return
	 * Returns a list of all users of the given device. 
	 */
	public ArrayList<User> getUsers(RequestType requestType, Device d) {
		log.info("---- DeviceService: getUsers Begin ----");
		ArrayList<User> deviceUsers= new ArrayList<User>();
		
		// get the device identifier
		Identifier device= getDeviceIdentifier(requestType, d);
		if(device != null){
			log.info("    using device identifier: "+device.valueFor("/device/name"));
			
			// search for all Users linked via authenticated-as to an access-request
			for(Link deviceLink : device.getLinks()){
				log.info("    searching for Users from the access-request");
				IdentifierPair ids= deviceLink.getIdentifiers();
				Identifier first= ids.getFirst();
				Identifier second= ids.getSecond();

				if(first.getTypeName().equals("access-request")){
					UserHelper.findUsersFromIdentifier(first, deviceUsers);
				} else if(second.getTypeName().equals("access-request")){
					UserHelper.findUsersFromIdentifier(second, deviceUsers);
				}
			}
		}
		log.info("---- DeviceService: getUsers End ----");
		return deviceUsers;
	}
	
	
	/**
	 * Helper method to get all IP-Address Identifiers from an device
	 * @param d
	 * @link{Identifier} from which the IP Identifiers should be searched.
	 * Should be of type Device.
	 * @return List of IP Identifiers
	 */
	private ArrayList<Identifier> getIPIdentfiers(Identifier d) {
		log.info("---- DeviceService: getIPIdentfiers Begin ----");
		ArrayList<Identifier> ipIds= new ArrayList<Identifier>();
		log.info("    searching for IPAddress identifier for specified identifier "+d.getTypeName());
		IpAddressHelper.findIPAddressIdentfiersFromIdentifier(d, ipIds);
		log.info("---- DeviceService: getIPIdentfiers End ----");
		return ipIds;
	}
	
	/**
	 * Helper method to get the device @link{Identifier} for a given @link{Device} object 
	 * Returns the @link{Identifier} which the Metalyzer Semantics Device object represents in the graph
	 * @param requestType Type of the request
	 * @param d @link{Device} to search the identifier for
	 * @return Device @link{Identifier} which the Metalyzer Semantics @link{Device} object does represent in the graph
	 */
	private Identifier getDeviceIdentifier(RequestType requestType, Device d){
		log.info("---- DeviceService: getDeviceIdentifier Begin ----");
		Identifier device= null;
		
		// get all available Device Identifiers for given RequestType
		Collection<Identifier> deviceList= null;
		if (requestType == RequestType.TIMESTAMP_REQUEST) {
			deviceList = getAllDeviceIdentifiers(d.getSearchTimestamp());
		} else if (requestType == RequestType.TIMEINTERVAL_REQUEST) {
			deviceList = getAllDeviceIdentifiers(d.getSearchFromTimestamp(), d.getSearchToTimestamp());
		} else if (requestType == RequestType.CURRENT_REQUEST) {
			deviceList = getAllDeviceIdentifiers();
		}
		if(deviceList != null){
			Iterator<Identifier> deviceIter= deviceList.iterator();
			boolean deviceFound= false;

			// search for the device identifier
			while(deviceIter.hasNext() && !deviceFound){
				device= deviceIter.next();
				if(device.getTypeName().equals("device")){
					log.info("device Name: "+d.getName());
					log.info("Identifier Name: "+device.valueFor("/device/name"));
					
					// if device is found stop here, otherwise reset device
					if(device.valueFor("/device/name").equals(d.getName())){
						log.info("device equals d? "+device.valueFor("/device/name").equals(d.getName()));
						deviceFound= true;
					} else {
						device= null;
					}
				}
			}
		}
		log.info("---- DeviceService: getDeviceIdentifier End ----");
		return device;
	}

	/**
	 * Returns all device @link{Identifier}s currently available.
	 * @return @see{Collection} of device @link{Identifiers}
	 */
	private Collection<Identifier> getAllDeviceIdentifiers() throws MetalyzerAPIException {
		return semCon.getConnection().getIdentifierFinder().getCurrent(StandardIdentifierType.DEVICE);
	}

	/**
	 * Returns all device identifiers available in the given period.
	 * @param fromTimestamp
	 * @param toTimestamp
	 * @return @link{Collection} of device @link{Identifiers}s
	 */
	private Collection<Identifier> getAllDeviceIdentifiers(long fromTimestamp,
			long toTimestamp) throws MetalyzerAPIException {
		MetalyzerDelta<Identifier> deviceDelta = semCon.getConnection().getIdentifierFinder().get(StandardIdentifierType.DEVICE, fromTimestamp, toTimestamp);
		return deviceDelta.getAvailables();
	}

	/**
	 * Returns all device identifiers available at the given timestamp.
	 * @param timestamp
	 * @return @link{Collection} of device @link{Identifiers}s
	 */
	private Collection<Identifier> getAllDeviceIdentifiers(long timestamp) throws MetalyzerAPIException {
		return semCon.getConnection().getIdentifierFinder().get(StandardIdentifierType.DEVICE, timestamp);
	}	

}
