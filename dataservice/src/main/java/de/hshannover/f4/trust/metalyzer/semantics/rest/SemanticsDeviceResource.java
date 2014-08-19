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
package de.hshannover.f4.trust.metalyzer.semantics.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.semantics.entities.Device;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController.RequestType;

/**
 * Boundary-Class to provide the Semantic-Device-Services over REST.
 *
 */
@Path("{connectionName}/analysis/semantics/device")
public class SemanticsDeviceResource {
	private Gson gson;
	private SemanticsController semCon;
	
	private static final Logger log = Logger.getLogger(SemanticsDeviceResource.class);

	@PathParam("connectionName")
	private String connectionName;
	
	public SemanticsDeviceResource() {
		semCon = SemanticsController.getInstance();
		gson = new Gson();
	}

	//######################### Device-Requests ########################	

	/**
	 * Query to get all devices at the given Unix-Timestamp.
	 * @param timestamp
	 * Unix-Timestamp to which the devices should be searched.
	 * @return
	 * Returns a JSON-String of all devices at the given Unix-Timestamp.
	 */
	@GET
	@Path("devices/{timestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllDevices(@PathParam("timestamp") long timestamp) throws MetalyzerAPIException{
		log.info("Request for getAllDevices at timestamp " + timestamp);
		semCon.setConnection(connectionName);
		return gson.toJson(semCon.getDeviceService().getDevices(RequestType.TIMESTAMP_REQUEST, timestamp, 0));
	}
	
	/**
	 * Query to get all devices at the given period of time.
	 * @param fromTimestamp 
	 * Unix-Timestamp of the beginning of the search-period.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search-period.
	 * @return 
	 * Returns a JSON-String of all devices at the given period of time.
	 */
	@GET
	@Path("devices/{fromTimestamp}/{toTimestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllDevicesFromTo(@PathParam("fromTimestamp") long fromTimestamp, @PathParam("toTimestamp") long toTimestamp) throws MetalyzerAPIException {
		log.info("Request for getAllDevices at interval [" + fromTimestamp + ", " + toTimestamp + "]");
		semCon.setConnection(connectionName);
		return gson.toJson(semCon.getDeviceService().getDevices(RequestType.TIMEINTERVAL_REQUEST, fromTimestamp, toTimestamp));
	}
	
	/**
	 * Query to get all devices at the current timestamp.
	 * @return 
	 * Returns a JSON-String of all devices at the current timestamp.
	 */
	@GET
	@Path("devices/current")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrentDevices() throws MetalyzerAPIException {
		log.info("Request for getCurrentDevices at current timestamp");
		semCon.setConnection(connectionName);
		return gson.toJson(semCon.getDeviceService().getDevices(RequestType.CURRENT_REQUEST, 0, 0));
	}

	/**
	 * Query to get the Unix-Timestamp of a specific device.
	 * @param device 
	 * Device to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given device.
	 */
	@PUT
	@Path("timestamps")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getTimestampForDevice(String jsonDevice) throws MetalyzerAPIException {
		Device dev = gson.fromJson(jsonDevice, Device.class);
		semCon.setConnection(connectionName);
		log.info("Request for getTimestampForDevice at timestamp " + dev.getTimestamp());
		return gson.toJson(semCon.getDeviceService().getTimestamp(RequestType.TIMESTAMP_REQUEST,dev));
	}
	
	/**
	 * Query to get the Unix-Timestamp of a specific device.
	 * @param device 
	 * Device to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given device.
	 */
	@PUT
	@Path("timestamps/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getTimestampForDeviceCurrent(String jsonDevice) throws MetalyzerAPIException {
		Device dev = gson.fromJson(jsonDevice, Device.class);
		semCon.setConnection(connectionName);
		log.info("Request for getTimestampForDevice at current timestamp");
		return gson.toJson(semCon.getDeviceService().getTimestamp(RequestType.CURRENT_REQUEST,dev));
	}
		
	/**
	 * Query to get the Unix-Timestamp of a specific device.
	 * @param device 
	 * Device to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given device.
	 */
	@PUT
	@Path("timestamps/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getTimestampForDeviceFromTo(String jsonDevice) throws MetalyzerAPIException {
		Device dev = gson.fromJson(jsonDevice, Device.class);
		semCon.setConnection(connectionName);
		log.info("Request for getTimestampForDevice at interval [" + dev.getSearchFromTimestamp() + ", " + dev.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getDeviceService().getTimestamp(RequestType.TIMEINTERVAL_REQUEST,dev));
	}
	
	/**
	 * Query to get all IP-Addresses of a specific device.
	 * @param device 
	 * Device to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given device.
	 */
	@PUT
	@Path("ipaddresses")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getIpAddressesForDevice(String jsonDevice) throws MetalyzerAPIException {
		Device dev = gson.fromJson(jsonDevice, Device.class);
		semCon.setConnection(connectionName);
		log.info("Request for getIpAddressesForDevice at timestamp " + dev.getTimestamp());
		return gson.toJson(semCon.getDeviceService().getIpAddresses(RequestType.TIMESTAMP_REQUEST,dev));
	}
	
	/**
	 * Query to get all IP-Addresses of a specific device.
	 * @param device 
	 * Device to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given device.
	 */
	@PUT
	@Path("ipaddresses/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getIpAddressesForDeviceFromTo(String jsonDevice) throws MetalyzerAPIException {
		Device dev = gson.fromJson(jsonDevice, Device.class);
		semCon.setConnection(connectionName);
		log.info("Request for getIpAddressesForDevice at interval [" + dev.getSearchFromTimestamp() + ", " + dev.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getDeviceService().getIpAddresses(RequestType.TIMEINTERVAL_REQUEST,dev));
	}
	
	/**
	 * Query to get all IP-Addresses of a specific device.
	 * @param device 
	 * Device to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given device.
	 */
	@PUT
	@Path("ipaddresses/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getIpAddressesForDeviceCurrent(String jsonDevice) throws MetalyzerAPIException {
		Device dev = gson.fromJson(jsonDevice, Device.class);
		semCon.setConnection(connectionName);
		log.info("Request for getIpAddressesForDevice at current timestamp");
		return gson.toJson(semCon.getDeviceService().getIpAddresses(RequestType.CURRENT_REQUEST,dev));
	}
	
	/**
	 * Query to get all MAC-Addresses of a specific device.
	 * @param device 
	 * Device to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of the MAC-Addresses of the given device.
	 */
	@PUT
	@Path("macaddresses")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getMacAddressesForDevice(String jsonDevice) throws MetalyzerAPIException {
		Device dev = gson.fromJson(jsonDevice, Device.class);
		semCon.setConnection(connectionName);
		log.info("Request for getMacAddressesForDevice at timestamp " + dev.getTimestamp());
		return gson.toJson(semCon.getDeviceService().getMacAddresses(RequestType.TIMESTAMP_REQUEST,dev));
	}
	
	
	/**
	 * Query to get all MAC-Addresses of a specific device.
	 * @param device 
	 * Device to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of the MAC-Addresses of the given device.
	 */
	@PUT
	@Path("macaddresses/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getMacAddressesForDeviceCurrent(String jsonDevice) throws MetalyzerAPIException {
		Device dev = gson.fromJson(jsonDevice, Device.class);
		semCon.setConnection(connectionName);
		log.info("Request for getMacAddressesForDevice at current timestamp");
		return gson.toJson(semCon.getDeviceService().getMacAddresses(RequestType.CURRENT_REQUEST,dev));
	}
	
	/**
	 * Query to get all MAC-Addresses of a specific device.
	 * @param device 
	 * Device to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of the MAC-Addresses of the given device.
	 */
	@PUT
	@Path("macaddresses/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getMacAddressesForDeviceFromTo(String jsonDevice) throws MetalyzerAPIException {
		Device dev = gson.fromJson(jsonDevice, Device.class);
		semCon.setConnection(connectionName);
		log.info("Request for getMacAddressesForDevice at interval [" + dev.getSearchFromTimestamp() + ", " + dev.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getDeviceService().getMacAddresses(RequestType.TIMEINTERVAL_REQUEST,dev));
	}

	/**
	 * Query to get all users of a specific device.
	 * @param device 
	 * Device to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given device.
	 */
	@PUT
	@Path("users")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getUsersForDevice(String jsonDevice) throws MetalyzerAPIException {
		Device dev = gson.fromJson(jsonDevice, Device.class);
		semCon.setConnection(connectionName);
		log.info("Request for getUsersForDevice at timestamp " + dev.getTimestamp());
		return gson.toJson(semCon.getDeviceService().getUsers(RequestType.TIMESTAMP_REQUEST,dev));
	}
	
	/**
	 * Query to get all users of a specific device.
	 * @param device 
	 * Device to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given device.
	 */
	@PUT
	@Path("users/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getUsersForDeviceCurrent(String jsonDevice) throws MetalyzerAPIException {
		Device dev = gson.fromJson(jsonDevice, Device.class);
		semCon.setConnection(connectionName);
		log.info("Request for getUsersForDevice at current timestamp");
		return gson.toJson(semCon.getDeviceService().getUsers(RequestType.CURRENT_REQUEST,dev));
	}
	
	/**
	 * Query to get all users of a specific device.
	 * @param device 
	 * Device to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given device.
	 */
	@PUT
	@Path("users/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getUsersForDeviceFromTo(String jsonDevice) throws MetalyzerAPIException {
		Device dev = gson.fromJson(jsonDevice, Device.class);
		semCon.setConnection(connectionName);
		log.info("Request for getUsersForDevice at interval [" + dev.getSearchFromTimestamp() + ", " + dev.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getDeviceService().getUsers(RequestType.TIMEINTERVAL_REQUEST,dev));
	}

	/**
	 * Query to count the number of different devices at the given Unix-Timestamp.
	 * @param timestamp 
	 * Unix-Timestamp to which the devices should be counted.
	 * @return 
	 * Returns a JSON-String of the number of devices at the given Unix-Timestamp.
	 */
	@GET
	@Path("count/{timestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public String countDevices(@PathParam("timestamp") long timestamp) {
		return "";
	}
}
