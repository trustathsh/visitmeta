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
import de.hshannover.f4.trust.metalyzer.semantics.entities.User;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController.RequestType;

/**
 * Boundary-Class to provide the Semantic-User-Services over REST.
 *
 */
@Path("{connectionName}/analysis/semantics/user")
public class SemanticsUserResource {
	private Gson gson;
	private SemanticsController semCon;
	
	private static final Logger log = Logger.getLogger(SemanticsUserResource.class);
	
	@PathParam("connectionName")
	private String connectionName;
	
	public SemanticsUserResource() {
		semCon = SemanticsController.getInstance();
		gson = new Gson();
	}
	
//######################### User-Requests #########################	
	
	/**
	 * Query to get all users at the given Unix-Timestamp.
	 * @param timestamp 
	 * Unix-Timestamp to which the users should be searched.
	 * @return 
	 * Returns a JSON-String of all users at the given Unix-Timestamp.
	 */
	@GET
	@Path("users/{timestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllUsers(@PathParam("timestamp") long timestamp) throws MetalyzerAPIException {
		log.info("Request for getAllUsers at timestamp " + timestamp);
		semCon.setConnection(connectionName);
		return gson.toJson(semCon.getUserService().getUsers(RequestType.TIMESTAMP_REQUEST, timestamp, 0));
	}
	
	/**
	 * Query to get all users at the given period of time.
	 * @param fromTimestamp 
	 * Unix-Timestamp of the beginning of the search-period.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search-period.
	 * @return 
	 * Returns a JSON-String of all users at the given period of time.
	 */
	@GET
	@Path("users/{fromTimestamp}/{toTimestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllUsersFromTo(@PathParam("fromTimestamp") long fromTimestamp, @PathParam("toTimestamp") long toTimestamp) throws MetalyzerAPIException {
		semCon.setConnection(connectionName);
		log.info("Request for getAllUsers at interval [" + fromTimestamp + ", " + toTimestamp + "]");
		return gson.toJson(semCon.getUserService().getUsers(RequestType.TIMEINTERVAL_REQUEST, fromTimestamp, toTimestamp));
	}
	
	/**
	 * Query to get all users at the current timestamp.
	 * @return 
	 * Returns a JSON-String of all users at the current timestamp.
	 */
	@GET
	@Path("users/current")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrentUsers(@PathParam("connectionName") String connectionName) throws MetalyzerAPIException {
		semCon.setConnection(connectionName);
		log.info("Request for getAllUsers at current timestamp");
		return gson.toJson(semCon.getUserService().getUsers(RequestType.CURRENT_REQUEST, 0, 0));
	}
	
	/**
	 * Query to get the Unix-Timestamp of a specific user.
	 * @param user 
	 * User to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given user.
	 */
	@PUT
	@Path("timestamps")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getTimestampForUser(String jsonUser) throws MetalyzerAPIException {
		User user = gson.fromJson(jsonUser, User.class);
		semCon.setConnection(connectionName);
		log.info("Request for getTimestampForUser at timestamp " + user.getTimestamp());
		return gson.toJson(semCon.getUserService().getTimestamp(RequestType.TIMESTAMP_REQUEST, user));
	}
	
	/**
	 * Query to get the Unix-Timestamp of a specific user.
	 * @param user 
	 * User to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given user.
	 */
	@PUT
	@Path("timestamps/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getTimestampForUserFromTo(String jsonUser) throws MetalyzerAPIException {
		User user = gson.fromJson(jsonUser, User.class);
		semCon.setConnection(connectionName);
		log.info("Request for getTimestampForUser at interval [" + user.getSearchFromTimestamp() + ", " + user.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getUserService().getTimestamp(RequestType.TIMEINTERVAL_REQUEST, user));
	}
	
	/**
	 * Query to get the Unix-Timestamp of a specific user.
	 * @param user 
	 * User to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given user.
	 */
	@PUT
	@Path("timestamps/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getTimestampForUserCurrent(String jsonUser) throws MetalyzerAPIException {
		User user = gson.fromJson(jsonUser, User.class);
		semCon.setConnection(connectionName);
		log.info("Request for getTimestampForUser at current timestamp");
		return gson.toJson(semCon.getUserService().getTimestamp(RequestType.CURRENT_REQUEST, user));
	}
	
	/**
	 * Query to get all IP-Addresses of a specific user.
	 * @param user 
	 * User to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given user.
	 */
	@PUT
	@Path("ipaddresses")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getIpAddressesForUser(String jsonUser) throws MetalyzerAPIException {
		User user = gson.fromJson(jsonUser, User.class);
		semCon.setConnection(connectionName);
		log.info("Request for getIpAddressesForUser at timestamp " + user.getTimestamp());
		return gson.toJson(semCon.getUserService().getIpAddresses(RequestType.TIMESTAMP_REQUEST, user));
	}
	
	/**
	 * Query to get all IP-Addresses of a specific user.
	 * @param user 
	 * User to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given user.
	 */
	@PUT
	@Path("ipaddresses/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getIpAddressesForUserFromTo(String jsonUser) throws MetalyzerAPIException {
		User user = gson.fromJson(jsonUser, User.class);
		semCon.setConnection(connectionName);
		log.info("Request for getIpAddressesForUser at interval [" + user.getSearchFromTimestamp() + ", " + user.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getUserService().getIpAddresses(RequestType.TIMEINTERVAL_REQUEST, user));
	}
	
	/**
	 * Query to get all IP-Addresses of a specific user.
	 * @param user 
	 * User to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given user.
	 */
	@PUT
	@Path("ipaddresses/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getIpAddressesForUserCurrent(String jsonUser) throws MetalyzerAPIException {
		User user = gson.fromJson(jsonUser, User.class);
		semCon.setConnection(connectionName);
		log.info("Request for getIpAddressesForUser at current timestamp");
		return gson.toJson(semCon.getUserService().getIpAddresses(RequestType.CURRENT_REQUEST, user));
	}
	
	/**
	 * Query to get all MAC-Addresses of a specific user.
	 * @param user 
	 * User to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of the MAC-Addresses of the given user.
	 */
	@PUT
	@Path("macaddresses")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getMacAddressesForUser(String jsonUser) throws MetalyzerAPIException {
		User user = gson.fromJson(jsonUser, User.class);
		semCon.setConnection(connectionName);
		log.info("Request for getMacAddressesForUser at timestamp " + user.getTimestamp());
		return gson.toJson(semCon.getUserService().getMacAddresses(RequestType.TIMESTAMP_REQUEST, user));
	}
	
	/**
	 * Query to get all MAC-Addresses of a specific user.
	 * @param user 
	 * User to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of the MAC-Addresses of the given user.
	 */
	@PUT
	@Path("macaddresses/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getMacAddressesForUserFromTo(String jsonUser) throws MetalyzerAPIException {
		User user = gson.fromJson(jsonUser, User.class);
		semCon.setConnection(connectionName);
		log.info("Request for getMacAddressesForUser at interval [" + user.getSearchFromTimestamp() + ", " + user.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getUserService().getMacAddresses(RequestType.TIMEINTERVAL_REQUEST, user));
	}
	
	/**
	 * Query to get all MAC-Addresses of a specific user.
	 * @param user 
	 * User to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of the MAC-Addresses of the given user.
	 */
	@PUT
	@Path("macaddresses/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getMacAddressesForUserCurrent(String jsonUser) throws MetalyzerAPIException {
		User user = gson.fromJson(jsonUser, User.class);
		semCon.setConnection(connectionName);
		log.info("Request for getMacAddressesForUser at current timestamp");
		return gson.toJson(semCon.getUserService().getMacAddresses(RequestType.CURRENT_REQUEST, user));
	}
	
	/**
	 * Query to get all devices of a specific user.
	 * @param user 
	 * User to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given user.
	 */
	@PUT
	@Path("devices")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getDevicesForUser(String jsonUser) throws MetalyzerAPIException {
		User user = gson.fromJson(jsonUser, User.class);
		semCon.setConnection(connectionName);
		log.info("Request for getDevicesForUser at timestamp " + user.getTimestamp());
		return gson.toJson(semCon.getUserService().getDevices(RequestType.TIMESTAMP_REQUEST, user));
	}
	
	/**
	 * Query to get all devices of a specific user.
	 * @param user 
	 * User to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given user.
	 */
	@PUT
	@Path("devices/fromto")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getDevicesForUserFromTo(String jsonUser) throws MetalyzerAPIException {
		User user = gson.fromJson(jsonUser, User.class);
		semCon.setConnection(connectionName);
		log.info("Request for getDevicesForUser at interval [" + user.getSearchFromTimestamp() + ", " + user.getSearchToTimestamp() + "]");
		return gson.toJson(semCon.getUserService().getDevices(RequestType.TIMEINTERVAL_REQUEST, user));
	}
	
	/**
	 * Query to get all devices of a specific user.
	 * @param user 
	 * User to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given user.
	 */
	@PUT
	@Path("devices/current")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getDevicesForUserCurrent(String jsonUser) throws MetalyzerAPIException {
		User user = gson.fromJson(jsonUser, User.class);
		semCon.setConnection(connectionName);
		log.info("Request for getDevicesForUser at current timestamp");
		return gson.toJson(semCon.getUserService().getDevices(RequestType.CURRENT_REQUEST, user));
	}
	
	/**
	 * !!! Not supported yet !!!
	 * Query to count the number of different users at the given Unix-Timestamp.
	 * @param timestamp 
	 * Unix-Timestamp to which the users should be counted.
	 * @return 
	 * Returns a JSON-String of the number of users at the given Unix-Timestamp.
	 */
	@GET
	@Path("count/{timestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public String countUsers(@PathParam("timestamp") long timestamp) {
		return "";
	}
}
