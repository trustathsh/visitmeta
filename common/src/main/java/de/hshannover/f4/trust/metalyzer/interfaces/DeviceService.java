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


public interface DeviceService {
	/**
	 * Query to get all devices at the given Unix-Timestamp.
	 * @param timestamp
	 * Unix-Timestamp to which the devices should be searched.
	 * @return
	 * Returns a JSON-String of all devices at the given Unix-Timestamp.
	 */
	public ArrayList<Device> getAllDevices(long timestamp) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all devices at the given period of time.
	 * @param fromTimestamp 
	 * Unix-Timestamp of the beginning of the search-period.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search-period.
	 * @return 
	 * Returns a JSON-String of all devices at the given period of time.
	 */
	public ArrayList<Device> getAllDevicesFromTo(long fromTimestamp, long toTimestamp)throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all devices at the current timestamp.
	 * @return 
	 * Returns a JSON-String of all devices at the current timestamp.
	 */
	public ArrayList<Device> getCurrentDevices() throws InterruptedException, ExecutionException, TimeoutException;

	/**
	 * Query to get the Unix-Timestamp of a specific device.
	 * @param device 
	 * Device to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given device.
	 */
	public long getTimestampForDevice(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get the Unix-Timestamp of a specific device.
	 * @param device 
	 * Device to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given device.
	 */
	public long getTimestampForDeviceCurrent(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get the Unix-Timestamp of a specific device.
	 * @param device 
	 * Device to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given device.
	 */
	public long getTimestampForDeviceFromTo(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all IP-Addresses of a specific device.
	 * @param device 
	 * Device to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given device.
	 */
	public ArrayList<IpAddress> getIpAddressesForDevice(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all IP-Addresses of a specific device.
	 * @param device 
	 * Device to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given device.
	 */
	public ArrayList<IpAddress> getIpAddressesForDeviceCurrent(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all IP-Addresses of a specific device.
	 * @param device 
	 * Device to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given device.
	 */
	public ArrayList<IpAddress> getIpAddressesForDeviceFromTo(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all MAC-Addresses of a specific device.
	 * @param device 
	 * Device to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of the MAC-Addresses of the given device.
	 */
	public ArrayList<MacAddress> getMacAddressesForDevice(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	
	/**
	 * Query to get all MAC-Addresses of a specific device.
	 * @param device 
	 * Device to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of the MAC-Addresses of the given device.
	 */
	public ArrayList<MacAddress> getMacAddressesForDeviceCurrent(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all MAC-Addresses of a specific device.
	 * @param device 
	 * Device to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of the MAC-Addresses of the given device.
	 */
	public ArrayList<MacAddress> getMacAddressesForDeviceFromTo(Device dev) throws InterruptedException, ExecutionException, TimeoutException;

	/**
	 * Query to get all users of a specific device.
	 * @param device 
	 * Device to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given device.
	 */
	public ArrayList<User> getUsersForDevice(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all users of a specific device.
	 * @param device 
	 * Device to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given device.
	 */
	public ArrayList<User> getUsersForDeviceCurrent(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all users of a specific device.
	 * @param device 
	 * Device to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given device.
	 */
	public ArrayList<User> getUsersForDeviceFromTo(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
}
