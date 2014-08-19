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
package de.hshannover.f4.trust.metalyzer.gui.misc;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.hshannover.f4.trust.metalyzer.semantics.entities.Device;
import de.hshannover.f4.trust.metalyzer.semantics.entities.IpAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.MacAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.User;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */


public class UtilHelper {
	
	/**
	 * Sorts a given list of {@link User} alphabetically by their names
	 * @param list of user names
	 * */
	public static void sortUsers(List<User> users) {
		Collections.sort(users, new Comparator<User>() {
			@Override
			public int compare(User u1, User u2) {
				return u1.getName().compareTo(u2.getName());
			}
		});
	}
	
	/**
	 * Sorts a given list of {@link IpAddress} by their address
	 * @param list of ip addresses
	 * */
	public static void sortIpAddresses(List<IpAddress> ips) {
		Collections.sort(ips, new Comparator<IpAddress>() {
			@Override
			public int compare(IpAddress i1, IpAddress i2) {
				return i1.getAddress().compareTo(i2.getAddress());
			}
		});
	}
	
	/**
	 * Sorts a given list of {@link MacAddress} by their address
	 * @param list of mac addresses
	 * */
	public static void sortMacAddresses(List<MacAddress> macs) {
		Collections.sort(macs, new Comparator<MacAddress>() {
			@Override
			public int compare(MacAddress m1, MacAddress m2) {
				return m1.getAddress().compareTo(m2.getAddress());
			}
		});
	}
	
	/**
	 * Sorts a given list of {@link Device} alphabetically by their names
	 * @param list of devices
	 * */
	public static void sortDevices(List<Device> devs) {
		Collections.sort(devs, new Comparator<Device>() {
			@Override
			public int compare(Device d1, Device d2) {
				return d1.getName().compareTo(d2.getName());
			}
		});
	}

	
	/**
	 * Converts a list of {@link User} names to a list of strings
	 * @param list of user names
	 * @return list of strings
	 * */
	public static List<String> convertUserNameToString(List<User> users) {
		List<String> stringData = new ArrayList<String>();
		for(User u : users) {
			stringData.add(u.getName());
		}
		return stringData;
	}
	
	/**
	 * Converts a list of {@link User} roles to a list of strings
	 * @param list of user roles
	 * @return list of strings
	 * */
	public static List<String> convertUserRoleToString(List<User> users) {
		List<String> stringData = new ArrayList<String>();
		for(User u : users) {
			stringData.add(u.getRole());
		}
		return stringData;
	}
	
	/**
	 * Converts a list of {@link IpAddress} to a list of strings
	 * @param list of ipaddresses
	 * @return list of strings
	 * */
	public static List<String> convertIpAddressToString(List<IpAddress> ips) {
		List<String> stringData = new ArrayList<String>();
		for(IpAddress i : ips) {
			stringData.add(i.getAddress());
		}
		return stringData;
	}
	
	/**
	 * Converts a list of {@link IpAddress} types to a list of strings
	 * @param list of ipaddress types
	 * @return list of strings
	 * */
	public static List<String> convertIpTypeToString(List<IpAddress> ips) {
		List<String> stringData = new ArrayList<String>();
		for(IpAddress i : ips) {
			stringData.add(i.getType());
		}
		return stringData;
	}
	
	/**
	 * Converts a list of {@link Device} names to a list of strings
	 * @param list of device names
	 * @return list of strings
	 * */
	public static List<String> convertDeviceNameToString(List<Device> devs) {
		List<String> stringData = new ArrayList<String>();
		for(Device d : devs) {
			stringData.add(d.getName());
		}
		return stringData;
	}
	
	/**
	 * Converts a list of {@link Device} attributes to a list of strings
	 * @param list of device attributes
	 * @return list of strings
	 * */
	public static List<String> convertDeviceAttributesToString(List<Device> devs) {
		List<String> stringData = new ArrayList<String>();
		for(Device d : devs) {
			stringData.add(d.getAttributes().toString());
		}
		return stringData;
	}
	
	/**
	 * Converts a list of {@link MacAddress} data to a list of strings
	 * @param list of macaddress data
	 * @return list of strings
	 * */
	public static List<String> convertMacToString(List<MacAddress> macs) {
		List<String> stringData = new ArrayList<String>();
		for(MacAddress m : macs) {
			stringData.add(m.getAddress());
		}
		return stringData;
	}
	

	/**
	 * Converts a set of longs in a list of longs
	 * 
	 * @param timestamps
	 *            set of timestamps
	 * @param timestamps
	 *            list of timestamps
	 * */
	public static List<Long> convertTimestamps(Set<Long> timestamps) {
		ArrayList<Long> copyTime = new ArrayList<Long>();
		if (timestamps == null || timestamps.size() < 1) {
			// Default values of the JSpinner list should have at least one
			// timestamp
			// otherwise an excption will be thrown
			copyTime.add(0L);
		}
		copyTime.addAll(timestamps);
		return copyTime;
	}

	/**
	 * Returns a sublist of timestamps
	 * 
	 * @param timestamps
	 * @return a list which ends at index list.size() - 1
	 * */
	public static List<Long> getStartList(Set<Long> srcTime) {
		ArrayList<Long> copyTime1 = new ArrayList<Long>();
		if (srcTime == null || srcTime.size() < 1) {
			// Default values of the JSpinner list should have at least one
			// timestamp
			// otherwise an excption will be thrown
			copyTime1.add(0L);
			return copyTime1;
		}
		copyTime1.addAll(srcTime);
		return copyTime1.subList(0, copyTime1.size() - 1);
	}

	/**
	 * Returns a sublist of timestamps
	 * 
	 * @param list
	 * @return a list which begins at index 1
	 * */
	public static List<Long> getEndList(Set<Long> srcTime) {
		ArrayList<Long> copyTime2 = new ArrayList<Long>();
		if (srcTime == null || srcTime.size() < 1) {
			// Default values of the JSpinner list should have at least one
			// timestamp
			// otherwise an excption will be thrown
			copyTime2.add(1000L);
			return copyTime2;
		}
		copyTime2.addAll(srcTime);
		return copyTime2.subList(1, copyTime2.size());
	}
	
	/**
	 * Creates a dummy list, with two long timestamps, which is be used for the {@link SpinnerListModel}.
	 * This model needs a non-null array list in order to work.
	 * @return dummy timestamp list
	 * */
	public static List<Long> createDummyList() {
		ArrayList<Long> dummy = new ArrayList<Long>();
		dummy.add(1000L);
		dummy.add(2000L);
		return dummy;
	}
	
	/**
	 * Converts a unixtimestamp into a date
	 * formatted String (yyyy-MM-dd HH:mm:ss) and returns it
	 * @param loginTimestamp Unixtimestamp of the first login of a User
	 * @return A date formatted String (yyyy-MM-dd HH:mm:ss)
	 */
	public static String convertTimestampToDatestring(long loginTimestamp) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date firstLoginDate = new Date((long)loginTimestamp);
		return df.format(firstLoginDate);
	}
	
	/**
	 * Retuns a String reprensentaion of the number, formated according to the precision
	 * @param minPrecsion
	 * @param maxPrecsion
	 * @see NumberFormat
	 * */
	public static String getPrecsion(double value, int minPrecsion, int maxPrecsion) {		
		Locale loacale = Locale.getDefault();
		NumberFormat nf = NumberFormat.getInstance(loacale);
		nf.setMaximumFractionDigits(maxPrecsion);
		nf.setMinimumFractionDigits(minPrecsion);
		return nf.format(value);
	}
}
