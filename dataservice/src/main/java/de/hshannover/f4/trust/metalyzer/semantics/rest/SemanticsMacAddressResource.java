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
import de.hshannover.f4.trust.metalyzer.semantics.entities.MacAddress;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController.RequestType;

/**
 * Boundary-Class to provide the Semantic-MacAddress-Services over REST.
 *
 */
@Path("{connectionName}/analysis/semantics/macaddress")
public class SemanticsMacAddressResource {
	private Gson gson;
	private SemanticsController semCon;
	
	private static final Logger log = Logger.getLogger(SemanticsMacAddressResource.class);
	
	@PathParam("connectionName")
	private String connectionName;

	public SemanticsMacAddressResource() {
		semCon = SemanticsController.getInstance();
		gson = new Gson();
	}

	//######################### MacAddress-Requests #########################	

	/**
	 * Query to get all MAC-Addresses at the given Unix-Timestamp.
	 * @param timestamp 
	 * Unix-Timestamp to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of all MAC-Addresses at the given Unix-Timestamp.
	 */
	@GET
	@Path("macaddresses/{timestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllMacAddresses(@PathParam("timestamp") long timestamp) throws MetalyzerAPIException {
		log.info("Request for getAllMacAddresses at timestamp " + timestamp);
		semCon.setConnection(connectionName);
		return gson.toJson(semCon.getMacAddressService().getMacAddresses(RequestType.TIMESTAMP_REQUEST, timestamp, 0));
	}
	
	/**
	 * Query to get all MAC-Addresses at the given period of time.
	 * @param fromTimestamp 
	 * Unix-Timestamp of the beginning of the search-period.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search-period.
	 * @return 
	 * Returns a JSON-String of all MAC-Addresses at the given period of time.
	 */
	@GET
	@Path("macaddresses/{fromTimestamp}/{toTimestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllMacAddressesFromTo(@PathParam("fromTimestamp") long fromTimestamp, @PathParam("toTimestamp") long toTimestamp) throws MetalyzerAPIException {
		semCon.setConnection(connectionName);
		log.info("Request for getAllMacAddresses at interval [" + fromTimestamp + ", " + toTimestamp + "]");
		return gson.toJson(semCon.getMacAddressService().getMacAddresses(RequestType.TIMEINTERVAL_REQUEST, fromTimestamp, toTimestamp));
	}
	
	/**
	 * Query to get all MAC-Addresses at the current timestamp.
	 * @return 
	 * Returns a JSON-String of all MAC-Addresses at the current timestamp.
	 */
	@GET
	@Path("macaddresses/current")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrentMacAddresses(@PathParam("connectionName") String connectionName) throws MetalyzerAPIException {
		semCon.setConnection(connectionName);
		log.info("Request for getAllMacAddresses at current timestamp");
		return gson.toJson(semCon.getMacAddressService().getMacAddresses(RequestType.CURRENT_REQUEST, 0, 0));
	}

	/**
	 * Query to get the Unix-Timestamp of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given MAC-Address.
	 */
	@PUT
	@Path("timestamps")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getTimestampForMacAddress(String jsonMacAddress) throws MetalyzerAPIException {
		MacAddress mac = gson.fromJson(jsonMacAddress, MacAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getTimestampForMacAddress at timestamp " + mac.getTimestamp());
		return gson.toJson(semCon.getMacAddressService().getTimestamp(RequestType.TIMESTAMP_REQUEST, mac));
	}
	
	/**
	 * Query to get the Unix-Timestamp of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given MAC-Address.
	 */
	@PUT
	@Path("timestamps/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getTimestampForMacAddressFromTo(String jsonMacAddress) throws MetalyzerAPIException {
		MacAddress mac = gson.fromJson(jsonMacAddress, MacAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getTimestampForMacAddress at interval [" + mac.getSearchFromTimestamp() + ", " + mac.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getMacAddressService().getTimestamp(RequestType.TIMEINTERVAL_REQUEST, mac));
	}
	
	/**
	 * Query to get the Unix-Timestamp of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given MAC-Address.
	 */
	@PUT
	@Path("timestamps/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getTimestampForMacAddressCurrent(String jsonMacAddress) throws MetalyzerAPIException {
		MacAddress mac = gson.fromJson(jsonMacAddress, MacAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getTimestampForMacAddress at current timestamp");
		return gson.toJson(semCon.getMacAddressService().getTimestamp(RequestType.CURRENT_REQUEST, mac));
	}

	/**
	 * Query to get all users of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given MAC-Address.
	 */
	@PUT
	@Path("users")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getUsersForMacAddress(String jsonMacAddress) throws MetalyzerAPIException {
		MacAddress mac = gson.fromJson(jsonMacAddress, MacAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getUsersForMacAddress at timestamp " + mac.getTimestamp());
		return gson.toJson(semCon.getMacAddressService().getUsers(RequestType.TIMESTAMP_REQUEST, mac));
	}
	
	/**
	 * Query to get all users of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given MAC-Address.
	 */
	@PUT
	@Path("users/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getUsersForMacAddressFromTo(String jsonMacAddress) throws MetalyzerAPIException {
		MacAddress mac = gson.fromJson(jsonMacAddress, MacAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getUsersForMacAddress at interval [" + mac.getSearchFromTimestamp() + ", " + mac.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getMacAddressService().getUsers(RequestType.TIMEINTERVAL_REQUEST, mac));
	}
	
	/**
	 * Query to get all users of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given MAC-Address.
	 */
	@PUT
	@Path("users/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getUsersForMacAddressCurrent(String jsonMacAddress) throws MetalyzerAPIException {
		MacAddress mac = gson.fromJson(jsonMacAddress, MacAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getUsersForMacAddress at current timestamp");
		return gson.toJson(semCon.getMacAddressService().getUsers(RequestType.CURRENT_REQUEST, mac));
	}

	/**
	 * Query to get all devices of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given MAC-Address.
	 */
	@PUT
	@Path("devices")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getDevicesForMacAddress(String jsonMacAddress) throws MetalyzerAPIException {
		MacAddress mac = gson.fromJson(jsonMacAddress, MacAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getDevicesForMacAddress at timestamp " + mac.getTimestamp());
		return gson.toJson(semCon.getMacAddressService().getDevices(RequestType.TIMESTAMP_REQUEST, mac));
	}
	
	/**
	 * Query to get all devices of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given MAC-Address.
	 */
	@PUT
	@Path("devices/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getDevicesForMacAddressFromTo(String jsonMacAddress) throws MetalyzerAPIException {
		MacAddress mac = gson.fromJson(jsonMacAddress, MacAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getDevicesForMacAddress at interval [" + mac.getSearchFromTimestamp() + ", " + mac.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getMacAddressService().getDevices(RequestType.TIMEINTERVAL_REQUEST, mac));
	}
	
	/**
	 * Query to get all devices of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given MAC-Address.
	 */
	@PUT
	@Path("devices/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getDevicesForMacAddressCurrent(String jsonMacAddress) throws MetalyzerAPIException {
		MacAddress mac = gson.fromJson(jsonMacAddress, MacAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getDevicesForMacAddress at current timestamp");
		return gson.toJson(semCon.getMacAddressService().getDevices(RequestType.CURRENT_REQUEST, mac));
	}

	/**
	 * Query to get all IP-Addresses of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given MAC-Address.
	 */
	@PUT
	@Path("ipaddresses")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getIpAddressesForMacAddress(String jsonMacAddress) throws MetalyzerAPIException {
		MacAddress mac = gson.fromJson(jsonMacAddress, MacAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getIpAddressesForMacAddress at timestamp " + mac.getTimestamp());
		return gson.toJson(semCon.getMacAddressService().getIpAddresses(RequestType.TIMESTAMP_REQUEST, mac));
	}
	
	/**
	 * Query to get all IP-Addresses of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given MAC-Address.
	 */
	@PUT
	@Path("ipaddresses/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getIpAddressesForMacAddressFromTo(String jsonMacAddress) throws MetalyzerAPIException {
		MacAddress mac = gson.fromJson(jsonMacAddress, MacAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getIpAddressesForMacAddress at interval [" + mac.getSearchFromTimestamp() + ", " + mac.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getMacAddressService().getIpAddresses(RequestType.TIMEINTERVAL_REQUEST, mac));
	}
	
	/**
	 * Query to get all IP-Addresses of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given MAC-Address.
	 */
	@PUT
	@Path("ipaddresses/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getIpAddressesForMacAddressCurrent(String jsonMacAddress) throws MetalyzerAPIException {
		MacAddress mac = gson.fromJson(jsonMacAddress, MacAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getIpAddressesForMacAddress at current timestamp");
		return gson.toJson(semCon.getMacAddressService().getIpAddresses(RequestType.CURRENT_REQUEST, mac));
	}

	/**
	 * !!! Not supported yet !!!
	 * Query to count the number of different MAC-Addresses at the given Unix-Timestamp.
	 * @param timestamp 
	 * Unix-Timestamp to which the MAC-Addresses should be counted.
	 * @return 
	 * Returns a JSON-String of the number of MAC-Addresses at the given Unix-Timestamp.
	 */
	@GET
	@Path("count/{timestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public String countMacAddresses(@PathParam("timestamp") long timestamp) {
		return "";
	}
}
