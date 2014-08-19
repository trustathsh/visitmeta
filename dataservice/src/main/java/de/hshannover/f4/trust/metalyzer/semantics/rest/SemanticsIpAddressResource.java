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
import de.hshannover.f4.trust.metalyzer.semantics.entities.IpAddress;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController.RequestType;

/**
 * Boundary-Class to provide the Semantic-IpAddress-Services over REST.
 *
 */
@Path("{connectionName}/analysis/semantics/ipaddress")
public class SemanticsIpAddressResource {
	private Gson gson;
	private SemanticsController semCon;

	private static final Logger log = Logger.getLogger(SemanticsIpAddressResource.class);
	
	@PathParam("connectionName")
	public String connectionName;

	public SemanticsIpAddressResource() {
		semCon = SemanticsController.getInstance();
		gson = new Gson();
	}

	//######################### IpAddress-Requests #########################

	/**
	 * Query to get all IP-Addresses at the given Unix-Timestamp.
	 * @param timestamp 
	 * Unix-Timestamp to which the IP-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of all IP-Addresses at the given Unix-Timestamp.
	 */
	@GET
	@Path("ipaddresses/{timestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllIpAddresses(@PathParam("timestamp") long timestamp) throws MetalyzerAPIException {
		log.info("Request for getAllIpAddresses at timestamp " + timestamp);
		semCon.setConnection(connectionName);
		return gson.toJson(semCon.getIpAddressService().getIpAddresses(RequestType.TIMESTAMP_REQUEST, timestamp, 0));
	}
	
	/**
	 * !!! At the moment this method returns an incorrect result because of an unexpected implementation of delta-methods !!!
	 * Query to get all IP-Addresses at the given period of time.
	 * @param fromTimestamp 
	 * Unix-Timestamp of the beginning of the search-period.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search-period.
	 * @return 
	 * Returns a JSON-String of all IP-Addresses at the given period of time.
	 */
	@GET
	@Path("ipaddresses/{fromTimestamp}/{toTimestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllIpAddressesFromTo(@PathParam("fromTimestamp") long fromTimestamp, @PathParam("toTimestamp") long toTimestamp) throws MetalyzerAPIException {
		log.info("Request for getAllIpAddresses at interval [" + fromTimestamp + ", " + toTimestamp + "]");
		semCon.setConnection(connectionName);
		return gson.toJson(semCon.getIpAddressService().getIpAddresses(RequestType.TIMEINTERVAL_REQUEST, fromTimestamp, toTimestamp));
	}
	
	/**
	 * Query to get all IP-Addresses at the current timestamp.
	 * @return 
	 * Returns a JSON-String of all IP-Addresses at the current timestamp.
	 */
	@GET
	@Path("ipaddresses/current")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrentIpAddresses(@PathParam("connectionName") String connectionName) throws MetalyzerAPIException {
		log.info("Request for getAllIpAddresses at current timestamp");
		semCon.setConnection(connectionName);
		return gson.toJson(semCon.getIpAddressService().getIpAddresses(RequestType.CURRENT_REQUEST, 0, 0));
	}
	
	/**
	 * Query to get the Unix-Timestamp of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given IP-Address.
	 */
	@PUT
	@Path("timestamps")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getTimestampForIpAddress(String jsonIpAddress) throws MetalyzerAPIException {
		IpAddress ip = gson.fromJson(jsonIpAddress, IpAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getTimestampForIpAddress at timestamp " + ip.getTimestamp());
		return gson.toJson(semCon.getIpAddressService().getTimestamp(RequestType.TIMESTAMP_REQUEST,ip));
	}
	/**
	 * !!! At the moment this method returns an incorrect result because of an unexpected implementation of delta-methods !!!
	 * Query to get the Unix-Timestamp of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given IP-Address.
	 */
	@PUT
	@Path("timestamps/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getTimestampForIpAddressFromTo(String jsonIpAddress) throws MetalyzerAPIException {
		IpAddress ip = gson.fromJson(jsonIpAddress, IpAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getTimestampForIpAddress at interval [" + ip.getSearchFromTimestamp() + ", " + ip.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getIpAddressService().getTimestamp(RequestType.TIMEINTERVAL_REQUEST, ip));
	}
	/**
	 * Query to get the Unix-Timestamp of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given IP-Address.
	 */
	@PUT
	@Path("timestamps/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getTimestampForIpAddressCurrent(String jsonIpAddress) throws MetalyzerAPIException {
		IpAddress ip = gson.fromJson(jsonIpAddress, IpAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getTimestampForIpAddress at current timestamp");
		return gson.toJson(semCon.getIpAddressService().getTimestamp(RequestType.CURRENT_REQUEST, ip));
	}
	
	/**
	 * Query to get all users of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given IP-Address.
	 */
	@PUT
	@Path("users")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getUsersForIpAddress(String jsonIpAddress) throws MetalyzerAPIException {
		IpAddress ip = gson.fromJson(jsonIpAddress, IpAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getUsersForIpAddress at timestamp " + ip.getTimestamp());
		return gson.toJson(semCon.getIpAddressService().getUsers(RequestType.TIMESTAMP_REQUEST,ip));
	}
	/**
	 * !!! At the moment this method returns an incorrect result because of an unexpected implementation of delta-methods !!!
	 * Query to get all users of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given IP-Address.
	 */
	@PUT
	@Path("users/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getUsersForIpAddressFromTo(String jsonIpAddress) throws MetalyzerAPIException {
		IpAddress ip = gson.fromJson(jsonIpAddress, IpAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getUsersForIpAddress at interval [" + ip.getSearchFromTimestamp() + ", " + ip.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getIpAddressService().getUsers(RequestType.TIMEINTERVAL_REQUEST, ip));
	}
	/**
	 * Query to get all users of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given IP-Address.
	 */
	@PUT
	@Path("users/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getUsersForIpAddressCurrent(String jsonIpAddress) throws MetalyzerAPIException {
		IpAddress ip = gson.fromJson(jsonIpAddress, IpAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getUsersForIpAddress at current timestamp");
		return gson.toJson(semCon.getIpAddressService().getUsers(RequestType.CURRENT_REQUEST, ip));
	}
	
	/**
	 * Query to get all devices of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given IP-Address.
	 */
	@PUT
	@Path("devices")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getDevicesForIpAddress(String jsonIpAddress) throws MetalyzerAPIException {
		IpAddress ip = gson.fromJson(jsonIpAddress, IpAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getDevicesForIpAddress at timestamp " + ip.getTimestamp());
		return gson.toJson(semCon.getIpAddressService().getDevices(RequestType.TIMESTAMP_REQUEST,ip));
	}
	/**
	 * !!! At the moment this method returns an incorrect result because of an unexpected implementation of delta-methods !!!
	 * Query to get all devices of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given IP-Address.
	 */
	@PUT
	@Path("devices/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getDevicesForIpAddressFromTo(String jsonIpAddress) throws MetalyzerAPIException {
		IpAddress ip = gson.fromJson(jsonIpAddress, IpAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getDevicesForIpAddress at interval [" + ip.getSearchFromTimestamp() + ", " + ip.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getIpAddressService().getDevices(RequestType.TIMEINTERVAL_REQUEST, ip));
	}
	
	/**
	 * Query to get all devices of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given IP-Address.
	 */
	@PUT
	@Path("devices/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getDevicesForIpAddressCurrent(String jsonIpAddress) throws MetalyzerAPIException {
		IpAddress ip = gson.fromJson(jsonIpAddress, IpAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getDevicesForIpAddress at current timestamp");
		return gson.toJson(semCon.getIpAddressService().getDevices(RequestType.CURRENT_REQUEST, ip));
	}

	/**
	 * Query to get all MAC-Addresses of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the MAC-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the MAC-Addresses of the given IP-Address.
	 */
	@PUT
	@Path("macaddresses")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getMacAddressesForIpAddress(String jsonIpAddress) throws MetalyzerAPIException {
		IpAddress ip = gson.fromJson(jsonIpAddress, IpAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getMacAddressesForIpAddress at timestamp " + ip.getTimestamp());
		return gson.toJson(semCon.getIpAddressService().getMacAddresses(RequestType.TIMESTAMP_REQUEST,ip));
	}
	/**
	 * !!! At the moment this method returns an incorrect result because of an unexpected implementation of delta-methods !!!
	 * Query to get all MAC-Addresses of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the MAC-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the MAC-Addresses of the given IP-Address.
	 */
	@PUT
	@Path("macaddresses/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getMacAddressesForIpAddressFromTo(String jsonIpAddress) throws MetalyzerAPIException {
		IpAddress ip = gson.fromJson(jsonIpAddress, IpAddress.class);
		semCon.setConnection(connectionName);
		log.info("Request for getMacAddressesForIpAddress at interval [" + ip.getSearchFromTimestamp() + ", " + ip.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getIpAddressService().getMacAddresses(RequestType.TIMEINTERVAL_REQUEST, ip));
	}
	
	/**
	 * Query to get all MAC-Addresses of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the MAC-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the MAC-Addresses of the given IP-Address.
	 */
	@PUT
	@Path("macaddresses/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getMacAddressesForIpAddressCurrent(String jsonIpAddress) throws MetalyzerAPIException {
		IpAddress ip = gson.fromJson(jsonIpAddress, IpAddress.class);
		log.info("Request for getMacAddressesForIpAddress at current timestamp");
		semCon.setConnection(connectionName);
		return gson.toJson(semCon.getIpAddressService().getMacAddresses(RequestType.CURRENT_REQUEST, ip));
	}
	/**
	 * Query to count the number of different IP-Addresses at the given Unix-Timestamp.
	 * @param timestamp 
	 * Unix-Timestamp to which the IP-Addresses should be counted.
	 * @return 
	 * Returns a JSON-String of the number of IP-Addresses at the given Unix-Timestamp.
	 */
	@GET
	@Path("count/{timestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public String countIpAddresses(@PathParam("timestamp") long timestamp) {
		return "";
	}
}
