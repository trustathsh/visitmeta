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
 * This file is part of visitmeta common, version 0.1.2,
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
package de.hshannover.f4.trust.metalyzer.interfaces;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import de.hshannover.f4.trust.metalyzer.semantics.entities.Device;
import de.hshannover.f4.trust.metalyzer.semantics.entities.IpAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.MacAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.User;

public interface MacAddressService {
	/**
	 * Query to get all MAC-Addresses at the given Unix-Timestamp.
	 * @param timestamp 
	 * Unix-Timestamp to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of all MAC-Addresses at the given Unix-Timestamp.
	 */
	public ArrayList<MacAddress> getAllMacAddresses(long timestamp) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * !!! At the moment this method returns an incorrect result because of an unexpected implementation of delta-methods !!!
	 * Query to get all MAC-Addresses at the given period of time.
	 * @param fromTimestamp 
	 * Unix-Timestamp of the beginning of the search-period.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search-period.
	 * @return 
	 * Returns a JSON-String of all MAC-Addresses at the given period of time.
	 */
	public ArrayList<MacAddress> getAllMacAddressesFromTo(long fromTimestamp, long toTimestamp) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all MAC-Addresses at the current timestamp.
	 * @return 
	 * Returns a JSON-String of all MAC-Addresses at the current timestamp.
	 */
	public ArrayList<MacAddress> getCurrentMacAddresses() throws InterruptedException, ExecutionException, TimeoutException;
	/**
	 * Query to get the Unix-Timestamp of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given MAC-Address.
	 */
	public long getTimestampForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get the Unix-Timestamp of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given MAC-Address.
	 */
	public long getTimestampForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get the Unix-Timestamp of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given MAC-Address.
	 */
	public long getTimestampForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;

	/**
	 * Query to get all users of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given MAC-Address.
	 */
	public ArrayList<User> getUsersForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all users of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given MAC-Address.
	 */
	public ArrayList<User> getUsersForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all users of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given MAC-Address.
	 */
	public ArrayList<User> getUsersForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all devices of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given MAC-Address.
	 */
	public ArrayList<Device> getDevicesForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all devices of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given MAC-Address.
	 */
	public ArrayList<Device> getDevicesForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all devices of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given MAC-Address.
	 */
	public ArrayList<Device> getDevicesForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;

	/**
	 * Query to get all IP-Addresses of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given MAC-Address.
	 */
	public ArrayList<IpAddress> getIpAddressesForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all IP-Addresses of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given MAC-Address.
	 */
	public ArrayList<IpAddress> getIpAddressesForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all IP-Addresses of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given MAC-Address.
	 */
	public ArrayList<IpAddress> getIpAddressesForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
}
