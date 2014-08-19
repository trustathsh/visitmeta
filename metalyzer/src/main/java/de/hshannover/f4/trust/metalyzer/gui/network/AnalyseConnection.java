package de.hshannover.f4.trust.metalyzer.gui.network;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.statistic.EdgeResult;
import de.hshannover.f4.trust.metalyzer.statistic.FrequencySelection;
import de.hshannover.f4.trust.metalyzer.statistic.FrequencyType;
import de.hshannover.f4.trust.metalyzer.statistic.MeanOfEdgeResult;
import de.hshannover.f4.trust.metalyzer.statistic.MeanType;
import de.hshannover.f4.trust.metalyzer.statistic.NodeResult;
import de.hshannover.f4.trust.metalyzer.statistic.ResultObject;
import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.interfaces.DeviceService;
import de.hshannover.f4.trust.metalyzer.interfaces.IpAddressService;
import de.hshannover.f4.trust.metalyzer.interfaces.MacAddressService;
import de.hshannover.f4.trust.metalyzer.interfaces.StatisticService;
import de.hshannover.f4.trust.metalyzer.interfaces.UserService;
import de.hshannover.f4.trust.metalyzer.semantics.entities.Device;
import de.hshannover.f4.trust.metalyzer.semantics.entities.IpAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.MacAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.User;

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
 * This file is part of visitmeta metalyzer, version 0.0.1,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@FHH
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

/** 
 * Project: Metalyzer
 * Author: Anton Zaiser
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class AnalyseConnection implements  UserService, IpAddressService, MacAddressService, DeviceService, StatisticService{
	
	private static final Logger log = Logger.getLogger(AnalyseConnection.class);
	
	private UserService mUserService = null;
	private IpAddressService mIpAddressService = null;
	private MacAddressService mMacAddressService = null;
	private DeviceService mDeviceService = null;
	private StatisticService mStatisticService = null;
	
	
	public AnalyseConnection(UserService userService, IpAddressService ipAddressService,
			MacAddressService macAddressService, DeviceService deviceService,
			StatisticService statisticService) {
		this.mUserService = userService;
		this.mIpAddressService = ipAddressService;
		this.mMacAddressService = macAddressService;
		this.mDeviceService = deviceService;
		this.mStatisticService = statisticService;
	}
	
	@Override
	public double getMeanCurrent(StandardIdentifierType filter, MeanType type) throws InterruptedException, ExecutionException, TimeoutException {
		double mean = mStatisticService.getMeanCurrent(filter, type);
		log.debug("Method getMeanCurrent("+filter+", "+type+") called");
		log.debug("returnded "+mean);
		return mean;
	}
	
	@Override
	public double getMeanTimestamp(long timestamp, StandardIdentifierType filter, MeanType type) throws InterruptedException, ExecutionException, TimeoutException {
		double mean = mStatisticService.getMeanTimestamp(timestamp, filter, type);
		log.debug("Method getMeanTimestamp("+timestamp+", "+filter+", "+type+") called");
		log.debug("returnded "+mean);
		return mean;
	}

	@Override
	public double getMeanFromTo(long from, long to, StandardIdentifierType filter, MeanType type) throws InterruptedException, ExecutionException, TimeoutException {
		double mean = mStatisticService.getMeanFromTo(from, to, filter, type);
		log.debug("Method getMeanFromTo("+from+", "+to+", "+filter+", "+type+") called");
		log.debug("returnded "+mean);
		return mean;
	}
	
	@Override
	public Map<String, Double> getFrequencyCurrent(FrequencyType type, FrequencySelection selection) throws InterruptedException, ExecutionException, TimeoutException {
		Map<String,Double> frequency = mStatisticService.getFrequencyCurrent(type, selection);
		log.debug("Method getFrequencyCurrent("+type+", "+selection+") called");
		log.debug("returnded "+frequency);
		return frequency;
	}
	
	@Override
	public Map<String, Double> getFrequencyTimestamp(long timestamp, FrequencyType type, FrequencySelection selection) throws InterruptedException, ExecutionException, TimeoutException {
		Map<String,Double> frequency = mStatisticService.getFrequencyTimestamp(timestamp, type, selection);
		log.debug("Method getFrequencyTimestamp("+timestamp+", "+type+", "+selection+") called");
		log.debug("returnded "+frequency);
		return frequency;
	}

	@Override
	public Map<String, Double> getFrequencyFromTo(long from, long to,FrequencyType type, FrequencySelection selection) throws InterruptedException, ExecutionException, TimeoutException {
		Map<String,Double> frequency = mStatisticService.getFrequencyFromTo(from, to, type, selection);
		log.debug("Method getFrequencyFromTo("+from+", "+to+", "+type+", "+selection+") called");
		log.debug("returnded "+frequency);
		return frequency;
	}
	
	@Override
	public ResultObject<NodeResult> getNodeCharecteristic(long from, long to) throws InterruptedException, ExecutionException, TimeoutException {
		ResultObject<NodeResult> result = mStatisticService.getNodeCharecteristic(from, to);
		log.debug("Method getNodeCharecteristic("+from+", "+to+") called");
		log.debug("returnded "+result);
		return result;
	}
	
	@Override
	public ResultObject<EdgeResult> getEdgeCharecteristic(long from, long to) throws InterruptedException, ExecutionException, TimeoutException {
		ResultObject<EdgeResult> result = mStatisticService.getEdgeCharecteristic(from, to);
		log.debug("Method getNodeCharecteristic("+from+", "+to+") called");
		log.debug("returnded "+result);
		return result;
	}
	
	@Override
	public ResultObject<MeanOfEdgeResult> getMeanOfEdgeCharecteristic(long from, long to) throws InterruptedException, ExecutionException, TimeoutException {
		ResultObject<MeanOfEdgeResult> result = mStatisticService.getMeanOfEdgeCharecteristic(from, to);
		log.debug("Method getMeanOfEdgeCharecteristic("+from+", "+to+") called");
		log.debug("returnded "+result);
		return result;
	}
	
	@Override
	public int getIdentifierCount(long timestamp) throws InterruptedException, ExecutionException, TimeoutException {
		int identifer = mStatisticService.getIdentifierCount(timestamp);
		log.debug("Method getIdentifierCount("+timestamp+") called");
		log.debug("returnded "+identifer);
		return identifer;
	}

	@Override
	public int getIdentifierCount() throws InterruptedException, ExecutionException, TimeoutException {
		int identifer = mStatisticService.getIdentifierCount();
		log.debug("Method getIdentifierCount() called");
		log.debug("returnded "+identifer);
		return identifer;
	}

	@Override
	public int getIdentifierCount(long from, long to) throws InterruptedException, ExecutionException, TimeoutException {
		int identifer = mStatisticService.getIdentifierCount(from, to);
		log.debug("Method getIdentifierCount("+from+", "+to+") called");
		log.debug("returnded "+identifer);
		return identifer;
	}

	@Override
	public int getMetadataCount(long from, long to) throws InterruptedException, ExecutionException, TimeoutException {
		int metadata = mStatisticService.getMetadataCount(from, to);
		log.debug("Method getMetadataCount("+from+", "+to+") called");
		log.debug("returnded "+metadata);
		return metadata;
	}
	
	@Override
	public int getMetadataCount(long timestamp) throws InterruptedException,ExecutionException, TimeoutException {
		int metadata = mStatisticService.getMetadataCount(timestamp);
		log.debug("Method getMetadataCount("+timestamp+") called");
		log.debug("returnded "+metadata);
		return metadata;
	}

	@Override
	public int getMetadataCount() throws InterruptedException,ExecutionException, TimeoutException {
		int metadata = mStatisticService.getMetadataCount();
		log.debug("Method getMetadataCount() called");
		log.debug("returnded "+metadata);
		return metadata;
	}

	@Override
	public ArrayList<Device> getAllDevices(long timestamp) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<Device> devices = mDeviceService.getAllDevices(timestamp);
		log.debug("Method getAllDevices("+timestamp+") called");
		log.debug("returnded "+devices);
		return devices;
	}

	@Override
	public ArrayList<Device> getAllDevicesFromTo(long fromTimestamp, long toTimestamp) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<Device> devices = mDeviceService.getAllDevicesFromTo(fromTimestamp, toTimestamp);
		log.debug("Method getAllDevicesFromTo("+fromTimestamp+", "+toTimestamp+") called");
		log.debug("returnded "+devices);
		return devices;
	}

	@Override
	public ArrayList<Device> getCurrentDevices() throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<Device> devices = mDeviceService.getCurrentDevices();
		log.debug("Method getCurrentDevices() called");
		log.debug("returnded "+devices);
		return devices;
	}

	@Override
	public long getTimestampForDevice(Device dev) throws InterruptedException, ExecutionException, TimeoutException {
		long timestamp = mDeviceService.getTimestampForDevice(dev);
		log.debug("Method getTimestampForDevice("+dev.getName()+", "+dev.getAttributes().toString()+") called");
		log.debug("returnded "+timestamp);
		return timestamp;
	}

	@Override
	public long getTimestampForDeviceCurrent(Device dev) throws InterruptedException, ExecutionException, TimeoutException {
		long timestamp = mDeviceService.getTimestampForDeviceCurrent(dev);
		log.debug("Method getTimestampForDeviceCurrent("+dev.getName()+", "+dev.getAttributes().toString()+") called");
		log.debug("returnded "+timestamp);
		return timestamp;
	}

	@Override
	public long getTimestampForDeviceFromTo(Device dev) throws InterruptedException, ExecutionException, TimeoutException {
		long timestamp = mDeviceService.getTimestampForDeviceCurrent(dev);
		log.debug("Method getTimestampForDeviceCurrent("+dev.getName()+", "+dev.getAttributes().toString()+") called");
		log.debug("returnded "+timestamp);
		return timestamp;
	}

	@Override
	public ArrayList<IpAddress> getIpAddressesForDevice(Device dev) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<IpAddress> ipAddresses = mDeviceService.getIpAddressesForDevice(dev);
		log.debug("Method getIpAddressesForDevice("+dev.getName()+", "+dev.getAttributes().toString()+") called");
		log.debug("returnded "+ipAddresses);
		return ipAddresses;
	}

	@Override
	public ArrayList<IpAddress> getIpAddressesForDeviceCurrent(Device dev) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<IpAddress> ipAddresses = mDeviceService.getIpAddressesForDeviceCurrent(dev);
		log.debug("Method getIpAddressesForDeviceCurrent("+dev.getName()+", "+dev.getAttributes().toString()+") called");
		log.debug("returnded "+ipAddresses);
		return ipAddresses;
	}

	@Override
	public ArrayList<IpAddress> getIpAddressesForDeviceFromTo(Device dev) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<IpAddress> ipAddresses = mDeviceService.getIpAddressesForDeviceFromTo(dev);
		log.debug("Method getIpAddressesForDeviceFromTo("+dev.getName()+", "+dev.getAttributes().toString()+") called");
		log.debug("returnded "+ipAddresses);
		return ipAddresses;
	}

	@Override
	public ArrayList<MacAddress> getMacAddressesForDevice(Device dev) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<MacAddress> macAddress = mDeviceService.getMacAddressesForDevice(dev);
		log.debug("Method getMacAddressesForDevice("+dev.getName()+", "+dev.getAttributes().toString()+") called");
		log.debug("returnded "+macAddress);
		return macAddress;
	}

	@Override
	public ArrayList<MacAddress> getMacAddressesForDeviceCurrent(Device dev) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<MacAddress> macAddress = mDeviceService.getMacAddressesForDevice(dev);
		log.debug("Method getMacAddressesForDevice("+dev.getName()+", "+dev.getAttributes().toString()+") called");
		log.debug("returnded "+macAddress);
		return macAddress;
	}

	@Override
	public ArrayList<MacAddress> getMacAddressesForDeviceFromTo(Device dev) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<MacAddress> macAddress = mDeviceService.getMacAddressesForDeviceFromTo(dev);
		log.debug("Method getMacAddressesForDeviceFromTo("+dev.getName()+", "+dev.getAttributes().toString()+") called");
		log.debug("returnded "+macAddress);
		return macAddress;
	}

	@Override
	public ArrayList<User> getUsersForDevice(Device dev) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<User> users = mDeviceService.getUsersForDevice(dev);
		log.debug("Method getUsersForDevice("+dev.getName()+", "+dev.getAttributes().toString()+") called");
		log.debug("returnded "+users);
		return users;
	}

	@Override
	public ArrayList<User> getUsersForDeviceCurrent(Device dev) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<User> users = mDeviceService.getUsersForDeviceCurrent(dev);
		log.debug("Method getUsersForDeviceCurrent("+dev.getName()+", "+dev.getAttributes().toString()+") called");
		log.debug("returnded "+users);
		return users;
	}

	@Override
	public ArrayList<User> getUsersForDeviceFromTo(Device dev) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<User> users = mDeviceService.getUsersForDeviceFromTo(dev);
		log.debug("Method getUsersForDeviceFromTo("+dev.getName()+", "+dev.getAttributes().toString()+") called");
		log.debug("returnded "+users);
		return users;
	}

	@Override
	public ArrayList<MacAddress> getAllMacAddresses(long timestamp) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<MacAddress> macAddresses = mMacAddressService.getAllMacAddresses(timestamp);
		log.debug("Method getAllDevices("+timestamp+") called");
		log.debug("returnded "+macAddresses);
		return macAddresses;
	}

	@Override
	public ArrayList<MacAddress> getAllMacAddressesFromTo(long fromTimestamp, long toTimestamp) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<MacAddress> macAddresses = mMacAddressService.getAllMacAddressesFromTo(fromTimestamp, toTimestamp);
		log.debug("Method getAllMacAddressesFromTo("+fromTimestamp+", "+toTimestamp+") called");
		log.debug("returnded "+macAddresses);
		return macAddresses;
	}

	@Override
	public ArrayList<MacAddress> getCurrentMacAddresses() throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<MacAddress> macAddresses = mMacAddressService.getCurrentMacAddresses();
		log.debug("Method getCurrentMacAddresses() called");
		log.debug("returnded "+macAddresses);
		return macAddresses;
	}

	@Override
	public long getTimestampForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		long timestamp = mMacAddressService.getTimestampForMacAddress(mac);
		log.debug("Method getTimestampForMacAddress("+mac.getAddress()+") called");
		log.debug("returnded "+timestamp);
		return timestamp;
	}

	@Override
	public long getTimestampForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		long timestamp = mMacAddressService.getTimestampForMacAddressFromTo(mac);
		log.debug("Method getTimestampForMacAddressFromTo("+mac.getAddress()+") called");
		log.debug("returnded "+timestamp);
		return timestamp;
	}

	@Override
	public long getTimestampForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		long timestamp = mMacAddressService.getTimestampForMacAddressCurrent(mac);
		log.debug("Method getTimestampForMacAddressCurrent("+mac.getAddress()+") called");
		log.debug("returnded "+timestamp);
		return timestamp;
	}

	@Override
	public ArrayList<User> getUsersForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<User> users = mMacAddressService.getUsersForMacAddress(mac);
		log.debug("Method getUsersForMacAddress("+mac.getAddress()+") called");
		log.debug("returnded "+users);
		return users;
	}

	@Override
	public ArrayList<User> getUsersForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<User> users = mMacAddressService.getUsersForMacAddressFromTo(mac);
		log.debug("Method getUsersForMacAddressFromTo("+mac.getAddress()+") called");
		log.debug("returnded "+users);
		return users;
	}

	@Override
	public ArrayList<User> getUsersForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<User> users = mMacAddressService.getUsersForMacAddressCurrent(mac);
		log.debug("Method getUsersForMacAddressCurrent("+mac.getAddress()+") called");
		log.debug("returnded "+users);
		return users;
	}

	@Override
	public ArrayList<Device> getDevicesForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<Device> devices = mMacAddressService.getDevicesForMacAddress(mac);
		log.debug("Method getDevicesForMacAddress("+mac.getAddress()+") called");
		log.debug("returnded "+devices);
		return devices;
	}

	@Override
	public ArrayList<Device> getDevicesForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<Device> devices = mMacAddressService.getDevicesForMacAddressFromTo(mac);
		log.debug("Method getDevicesForMacAddressFromTo("+mac.getAddress()+") called");
		log.debug("returnded "+devices);
		return devices;
	}

	@Override
	public ArrayList<Device> getDevicesForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<Device> devices = mMacAddressService.getDevicesForMacAddressCurrent(mac);
		log.debug("Method getDevicesForMacAddressCurrent("+mac.getAddress()+") called");
		log.debug("returnded "+devices);
		return devices;
	}

	@Override
	public ArrayList<IpAddress> getIpAddressesForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<IpAddress> ipAddress = mMacAddressService.getIpAddressesForMacAddress(mac);
		log.debug("Method getIpAddressesForMacAddress("+mac.getAddress()+") called");
		log.debug("returnded "+ipAddress);
		return ipAddress;
	}

	@Override
	public ArrayList<IpAddress> getIpAddressesForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<IpAddress> ipAddress = mMacAddressService.getIpAddressesForMacAddressFromTo(mac);
		log.debug("Method getIpAddressesForMacAddressFromTo("+mac.getAddress()+") called");
		log.debug("returnded "+ipAddress);
		return ipAddress;
	}

	@Override
	public ArrayList<IpAddress> getIpAddressesForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException,TimeoutException {
		ArrayList<IpAddress> ipAddress = mMacAddressService.getIpAddressesForMacAddressCurrent(mac);
		log.debug("Method getIpAddressesForMacAddressCurrent("+mac.getAddress()+") called");
		log.debug("returnded "+ipAddress);
		return ipAddress;
	}

	@Override
	public ArrayList<IpAddress> getAllIpAddresses(long timestamp) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<IpAddress> ipAddress = mIpAddressService.getAllIpAddresses(timestamp);
		log.debug("Method getAllIpAddresses("+timestamp+") called");
		log.debug("returnded "+ipAddress);
		return ipAddress;
	}

	@Override
	public ArrayList<IpAddress> getAllIpAddressesFromTo(long fromTimestamp,long toTimestamp) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<IpAddress> ipAddress = mIpAddressService.getAllIpAddressesFromTo(fromTimestamp, toTimestamp);
		log.debug("Method getAllIpAddressesFromTo("+fromTimestamp+", "+toTimestamp+") called");
		log.debug("returnded "+ipAddress);
		return ipAddress;
	}

	@Override
	public ArrayList<IpAddress> getCurrentIpAddresses() throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<IpAddress> ipAddress = mIpAddressService.getCurrentIpAddresses();
		log.debug("Method getCurrentIpAddresses() called");
		log.debug("returnded "+ipAddress);
		return ipAddress;
	}

	@Override
	public long getTimestampForIpAddress(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException {
		long timestamp = mIpAddressService.getTimestampForIpAddress(ip);
		log.debug("Method getTimestampForIpAddress("+ip.getAddress()+", "+ip.getType()+") called");
		log.debug("returnded "+timestamp);
		return timestamp;
	}

	@Override
	public long getTimestampForIpAddressFromTo(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException {
		long timestamp = mIpAddressService.getTimestampForIpAddressFromTo(ip);
		log.debug("Method getTimestampForIpAddressFromTo("+ip.getAddress()+", "+ip.getType()+") called");
		log.debug("returnded "+timestamp);
		return timestamp;
	}

	@Override
	public long getTimestampForIpAddressCurrent(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException {
		long timestamp = mIpAddressService.getTimestampForIpAddressCurrent(ip);
		log.debug("Method getTimestampForIpAddressCurrent("+ip.getAddress()+", "+ip.getType()+") called");
		log.debug("returnded "+timestamp);
		return timestamp;
	}

	@Override
	public ArrayList<User> getUsersForIpAddress(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<User> users = mIpAddressService.getUsersForIpAddress(ip);
		log.debug("Method getUsersForIpAddress("+ip.getAddress()+", "+ip.getType()+") called");
		log.debug("returnded "+users);
		return users;
	}

	@Override
	public ArrayList<User> getUsersForIpAddressFromTo(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<User> users = mIpAddressService.getUsersForIpAddressFromTo(ip);
		log.debug("Method getUsersForIpAddressFromTo("+ip.getAddress()+", "+ip.getType()+") called");
		log.debug("returnded "+users);
		return users;
	}

	@Override
	public ArrayList<User> getUsersForIpAddressCurrent(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<User> users = mIpAddressService.getUsersForIpAddressCurrent(ip);
		log.debug("Method getUsersForIpAddressCurrent("+ip.getAddress()+", "+ip.getType()+") called");
		log.debug("returnded "+users);
		return users;
	}

	@Override
	public ArrayList<Device> getDevicesForIpAddress(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<Device> devices = mIpAddressService.getDevicesForIpAddress(ip);
		log.debug("Method getDevicesForIpAddress("+ip.getAddress()+", "+ip.getType()+") called");
		log.debug("returnded "+devices);
		return devices;
	}

	@Override
	public ArrayList<Device> getDevicesForIpAddressFromTo(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<Device> devices = mIpAddressService.getDevicesForIpAddressFromTo(ip);
		log.debug("Method getDevicesForIpAddressFromTo("+ip.getAddress()+", "+ip.getType()+") called");
		log.debug("returnded "+devices);
		return devices;
	}

	@Override
	public ArrayList<Device> getDevicesForIpAddressCurrent(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<Device> devices = mIpAddressService.getDevicesForIpAddressCurrent(ip);
		log.debug("Method getDevicesForIpAddressCurrent("+ip.getAddress()+", "+ip.getType()+") called");
		log.debug("returnded "+devices);
		return devices;
	}

	@Override
	public ArrayList<MacAddress> getMacAddressesForIpAddress(IpAddress ips) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<MacAddress> macAddresses = mIpAddressService.getMacAddressesForIpAddress(ips);
		log.debug("Method getMacAddressesForIpAddress("+ips.getAddress()+", "+ips.getType()+") called");
		log.debug("returnded "+macAddresses);
		return macAddresses;
	}

	@Override
	public ArrayList<MacAddress> getMacAddressesForIpAddressFromTo(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<MacAddress> macAddresses = mIpAddressService.getMacAddressesForIpAddressFromTo(ip);
		log.debug("Method getMacAddressesForIpAddressFromTo("+ip.getAddress()+", "+ip.getType()+") called");
		log.debug("returnded "+macAddresses);
		return macAddresses;
	}

	@Override
	public ArrayList<MacAddress> getMacAddressesForIpAddressCurrent(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<MacAddress> macAddresses = mIpAddressService.getMacAddressesForIpAddressCurrent(ip);
		log.debug("Method getMacAddressesForIpAddressCurrent("+ip.getAddress()+", "+ip.getType()+") called");
		log.debug("returnded "+macAddresses);
		return macAddresses;
	}

	@Override
	public ArrayList<User> getAllUsers(long timestamp) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<User> users = mUserService.getAllUsers(timestamp);
		log.debug("Method getAllUsers("+timestamp+") called");
		log.debug("returnded "+users);
		return users;
	}

	@Override
	public ArrayList<User> getAllUsersFromTo(long fromTimestamp,long toTimestamp) throws InterruptedException, ExecutionException,TimeoutException {
		ArrayList<User> users = mUserService.getAllUsersFromTo(fromTimestamp, toTimestamp);
		log.debug("Method getAllUsersFromTo("+fromTimestamp+", "+toTimestamp+") called");
		log.debug("returnded "+users);
		return users;
	}

	@Override
	public ArrayList<User> getCurrentUsers() throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<User> users = mUserService.getCurrentUsers();
		log.debug("Method getCurrentUsers() called");
		log.debug("returnded "+users);
		return users;
	}

	@Override
	public long getTimestampForUser(User user) throws InterruptedException, ExecutionException, TimeoutException {
		long timestamp = mUserService.getTimestampForUser(user);
		log.debug("Method getTimestampForUser("+user.getName()+", "+user.getRole()+") called");
		log.debug("returnded "+timestamp);
		return timestamp;
	}

	@Override
	public long getTimestampForUserFromTo(User user) throws InterruptedException, ExecutionException, TimeoutException {
		long timestamp = mUserService.getTimestampForUserFromTo(user);
		log.debug("Method getTimestampForUserFromTo("+user.getName()+", "+user.getRole()+") called");
		log.debug("returnded "+timestamp);
		return timestamp;
	}

	@Override
	public long getTimestampForUserCurrent(User user) throws InterruptedException, ExecutionException, TimeoutException {
		long timestamp = mUserService.getTimestampForUserCurrent(user);
		log.debug("Method getTimestampForUserCurrent("+user.getName()+", "+user.getRole()+") called");
		log.debug("returnded "+timestamp);
		return timestamp;
	}

	@Override
	public ArrayList<IpAddress> getIpAddressesForUser(User user) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<IpAddress> ipAddresses = mUserService.getIpAddressesForUser(user);
		log.debug("Method getIpAddressesForUser("+user.getName()+", "+user.getRole()+") called");
		log.debug("returnded "+ipAddresses);
		return ipAddresses;
	}

	@Override
	public ArrayList<IpAddress> getIpAddressesForUserFromTo(User user) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<IpAddress> ipAddresses = mUserService.getIpAddressesForUserFromTo(user);
		log.debug("Method getIpAddressesForUserFromTo("+user.getName()+", "+user.getRole()+") called");
		log.debug("returnded "+ipAddresses);
		return ipAddresses;
	}

	@Override
	public ArrayList<IpAddress> getIpAddressesForUserCurrent(User user) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<IpAddress> ipAddresses = mUserService.getIpAddressesForUserCurrent(user);
		log.debug("Method getIpAddressesForUserCurrent("+user.getName()+", "+user.getRole()+") called");
		log.debug("returnded "+ipAddresses);
		return ipAddresses;
	}

	@Override
	public ArrayList<MacAddress> getMacAddressesForUser(User user) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<MacAddress> macAddresses = mUserService.getMacAddressesForUser(user);
		log.debug("Method getMacAddressesForUser("+user.getName()+", "+user.getRole()+") called");
		log.debug("returnded "+macAddresses);
		return macAddresses;
	}

	@Override
	public ArrayList<MacAddress> getMacAddressesForUserFromTo(User user) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<MacAddress> macAddresses = mUserService.getMacAddressesForUserFromTo(user);
		log.debug("Method getMacAddressesForUserFromTo("+user.getName()+", "+user.getRole()+") called");
		log.debug("returnded "+macAddresses);
		return macAddresses;
	}

	@Override
	public ArrayList<MacAddress> getMacAddressesForUserCurrent(User user) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<MacAddress> macAddresses = mUserService.getMacAddressesForUserCurrent(user);
		log.debug("Method getMacAddressesForUserCurrent("+user.getName()+", "+user.getRole()+") called");
		log.debug("returnded "+macAddresses);
		return macAddresses;
	}

	@Override
	public ArrayList<Device> getDevicesForUser(User user) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<Device> devices = mUserService.getDevicesForUser(user);
		log.debug("Method getDevicesForUser("+user.getName()+", "+user.getRole()+") called");
		log.debug("returnded "+devices);
		return devices;
	}

	@Override
	public ArrayList<Device> getDevicesForUserFromTo(User user) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<Device> devices = mUserService.getDevicesForUserFromTo(user);
		log.debug("Method getDevicesForUserFromTo("+user.getName()+", "+user.getRole()+") called");
		log.debug("returnded "+devices);
		return devices;
	}

	@Override
	public ArrayList<Device> getDevicesForUserCurrent(User user) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<Device> devices = mUserService.getDevicesForUserCurrent(user);
		log.debug("Method getDevicesForUserCurrent("+user.getName()+", "+user.getRole()+") called");
		log.debug("returnded "+devices);
		return devices;
	}
}
