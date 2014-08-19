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
package de.hshannover.f4.trust.metalyzer.gui.labels;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

/**
 * This class provides a bunch of static label descriptions that are displayed in on top of all views,
 * which hold user specific information about the selected view
 * */

public class InfoLabels {
	public static final String ABOUT_INFO = "Metalyzer was developed during\n"
			+ "a Bachelor Project at Hochschule\n"
			+ "Hannover in ooperation with Trust@HsH";
	
	public static final String FREQUENCY_IDENTIFER_INFO = "This view provides some diagrams of the frequency of identifers, which are present at a given time. "
			+ "You have a bar chart which provides the absolute frequency and a pie chart which show the relative frequency of identifer types. "
			+ "You can choose between the following three options: time intervall, timestamp and live. The timestamp option gives you the frequency to a "
			+ "single timestamp. Furthermore the time intervall option gives you the frequency between a given from and to timestamp. At last "
			+ "there is the possiblity to use the live option, which provides the latest data.";
	
	public static final String FREQUENCY_METADATA_INFO = "This view provides some diagrams of the frequency of metadata, which are present at a given time. "
			+ "You have a bar chart which provides the absolute frequency and a pie chart which show the relative frequency of metadata. "
			+ "You can choose between the following three options: time intervall, timestamp and live. The timestamp option gives you the frequency to a "
			+ "single timestamp. Furthermore the time intervall option gives you the frequency between a given from and to timestamp. At last "
			+ "there is the possiblity to use the live option, which provides the latest data.";
	
	public static final String FREQUENCY_DEVICES_INFO = "This view provides some diagrams of the frequency of devices, which are present at a given time. "
			+ "You have a bar chart which provides the absolute frequency and a pie chart which show the relative frequency of devices. "
			+ "You can choose between the following three options: time intervall, timestamp and live. The timestamp option gives you the frequency to a "
			+ "single timestamp. Furthermore the time intervall option gives you the frequency between a given from and to timestamp. At last "
			+ "there is the possiblity to use the live option, which provides the latest data. ";
	
	public static final String FREQUENCY_ROLES_INFO = "This view provides some diagrams of the frequency of roles, which are present at a given time. "
			+ "You have a bar chart which provides the absolute frequency and a pie chart which show the relative frequency of roles. "
			+ "You can choose between the following three options: time intervall, timestamp and live. The timestamp option gives you the frequency to a "
			+ "single timestamp. Furthermore the time intervall option gives you the frequency between a given from and to timestamp. At last "
			+ "there is the possiblity to use the live option, which provides the latest data.";
	
	public static final String MEAN_LINKS_INFO = "This view provides some information of the average number of links of a specific "
			+ "if-map identifier type, which are present at a given time. You can choose between the following three options: time intervall, "
			+ "timestamp and live. The timestamp option gives you the mean of links to a single timestamp. Furthermore "
			+ "the time intervall option gives you the mean of links between a given from and to timestamp. At last "
			+ "there is the possiblity to use the live option, which provides the latest data. ";
	
	public static final String GRAPH_NODES_INFO = "This view provides some information about the ifmap graph itself. You can see the count of nodes that are "
			+ "available in the selected intervall, the mean of all nodes and the standard deviation. With the time intervall option you can modify the range "
			+ "of timestamps and nodes that are displayed in the table and chart. The Scrollbar allows you only to scoll within the selcted intervall. Due to "
			+ "draw limitations of the chart, only a few timestamp are shown each scroll step.";
	
	public static final String GRAPH_EDGES_INFO = "This view provides some information about the ifmap graph itself. You can see the count of edges that are "
			+ "available in the selected intervall, the mean of all edges and the standard deviation. With the time intervall option you can modify the range "
			+ "of timestamps and nodes that are displayed in the table and chart. The Scrollbar allows you only to scoll within the selcted intervall. Due to "
			+ "draw limitations of the chart, only a few timestamp are shown each scroll step.";
	
	public static final String GRAPH_MEAN_INFO = "This view provides some information about the ifmap graph itself. You can see the mean of metadata that are "
			+ "available in the selected intervall, the overall mean of egdes and the standard deviation. With the time intervall option you can modify the range "
			+ "of timestamps and nodes that are displayed in the table and chart. The Scrollbar allows you only to scoll within the selcted intervall. Due to "
			+ "draw limitations of the chart, only a few timestamp are shown each scroll step.";
	
	public static final String USER_ALL_INFO = "This overview provides all informations of a single user. You can choose the user and this view shows the "
			+ "the first login time, the count of ip-, mac-addresses and devices this user posses. The table provides you additional detail information "
			+ "about user's role and which devices and ip- or mac-addresses are in use of this user. You can choose between the following three options: "
			+ "time intervall, timestamp and live. The timestamp option gives you the all user informations to a single timestamp. Furthermore the time intervall "
			+ "option gives you the user informations between a given from and to timestamp. At last there is the possiblity to use the live option, which provides "
			+ "the latest data.";
	
	public static final String USER_IP_INFO = "This view shows a table where you can see all IP-Addresses with their version, that a user possessed. "
			+"You can choose the user and to show only IP-Addresses that were used in a specific time intervall, or to a timestamp, or a live view, that gets "
			+"updated every 5 seconds, and shows the current state.";
	
	public static final String USER_MAC_INFO = "This view shows a table where you can see all Mac-addresses, that a user possessed. You can choose "
			+"the user and to show only Mac-Addresses that were used in a specific time intervall, or to a timestamp, or a live view, that gets updated "
			+"every 5 seconds, and shows the current state.";
	
	public static final String USER_DEVICES_INFO = "This view shows a table where you can see all devices with its attributes, that a user possessed. "
			+"You can choose the user and to show only devices that were used in a specific time intervall, or to a timestamp, or a live view, that gets "
			+"updated every 5 seconds, and shows the current state.";
	
	public static final String USER_AUTH_INFO = "This view shows a table where you can see all users with their roles, which were authenticated at "
			+"a timestamp you can choose.";
	
	public static final String IP_ALL_INFO = "This overview provides all informations of a single ip-address. You can choose the ip-address and this view shows the "
			+ "the count of user, mac-addresses and devices that are assign to this ip-address. The table provides you additional detail information "
			+ "about the related users, mac-addresses and devices for this ip-address. You can choose between the following three options: "
			+ "time intervall, timestamp and live. The timestamp option gives you the all ip-address informations to a single timestamp. Furthermore the time intervall "
			+ "option gives you the ip-address informations between a given from and to timestamp. At last there is the possiblity to use the live option, which provides "
			+ "the latest data.";
	
	public static final String IP_USER_INFO = "This view shows a table where you can see all users with its roles, belonging to an IP-Address. You can "
			+"choose the IP-Address and to show only users which used this IP-Address in a specific time intervall, or to a timestamp, or a live view, "
			+"that gets updated every 5 seconds, and shows the current state.";
	
	public static final String IP_MAC_INFO = "This view shows a table where you can see all Mac-addresses, belonging to an IP-Address. You can choose "
			+"the IP-Address and to show only Mac-Addresses that were used in a specific time intervall, or to a timestamp, or a live view, that gets updated "
			+"every 5 seconds, and shows the current state.";
	
	public static final String IP_DEVICES_INFO = "This view shows a table where you can see all devices with its attributes,  belonging to an IP-Address. "
			+"You can choose the IP-Address and to show only devices that were used in a specific time intervall, or to a timestamp, or a live view, that gets" 
			+"updated every 5 seconds, and shows the current state.";
	
	public static final String IP_AUTH_INFO = "This view shows a table where you can see all IP-Addresses with their type, which were authenticated at "
			+"a timestamp you can choose.";
	
	public static final String MAC_ALL_INFO = "This overview provides all informations of a single mac-address. You can choose the mac-address and this view shows the "
			+ "the count of user, ip-addresses and devices that are assign to this mac-address. The table provides you additional detail information "
			+ "about the related users, ip-addresses and devices for this mac-address. You can choose between the following three options: "
			+ "time intervall, timestamp and live. The timestamp option gives you the all mac-address informations to a single timestamp. Furthermore the time intervall "
			+ "option gives you the mac-address informations between a given from and to timestamp. At last there is the possiblity to use the live option, which provides "
			+ "the latest data.";
	
	public static final String MAC_USER_INFO = "This view shows a table where you can see all users with its roles, belonging to a Mac-Address. You can "
			+"choose the Mac-Address and to show only users which used this Mac-Address in a specific time intervall, or to a timestamp, or a live view, "
			+"that gets updated every 5 seconds, and shows the current state.";
	
	public static final String MAC_IP_INFO = "This view shows a table where you can see all IP-Addresses with their version, belonging to a Mac-Address. "
			+"You can choose the Mac-Address and to show only IP-Addresses that were used in a specific time intervall, or to a timestamp, or a live view, that gets "
			+"updated every 5 seconds, and shows the current state.";
	
	public static final String MAC_DEVICES_INFO = "This view shows a table where you can see all devices with its attributes,  belonging to an Mac-Address. "
			+"You can choose the Mac-Address and to show only devices that were used in a specific time intervall, or to a timestamp, or a live view, that gets "
			+"updated every 5 seconds, and shows the current state.";
	
	public static final String MAC_AUTH_INFO = "This view shows a table where you can see all Mac-Addresses, which were authenticated at "
			+"a timestamp you can choose.";
	
	public static final String DEVICE_ALL_INFO = "This overview provides all informations of a single device. You can choose the device and this view shows the "
			+ "the count of user, ip- and mac-addresses that are assign to this devices. The table provides you additional detail information "
			+ "about the related users, ip- and mac-addresses for this device. You can choose between the following three options: "
			+ "time intervall, timestamp and live. The timestamp option gives you the all device informations to a single timestamp. Furthermore the time intervall "
			+ "option gives you the device informations between a given from and to timestamp. At last there is the possiblity to use the live option, which provides "
			+ "the latest data.";
	
	public static final String DEVICE_USER_INFO = "This view shows a table where you can see all users with its roles, belonging to a device. You can "
			+"choose the device and to show only users which used this device in a specific time intervall, or to a timestamp, or a live view, "
			+"that gets updated every 5 seconds, and shows the current state.";
	
	public static final String DEVICE_IP_INFO = "This view shows a table where you can see all IP-Addresses with their version, belonging to a device. "
			+"You can choose the device and to show only IP-Addresses that were used in a specific time intervall, or to a timestamp, or a live view, that gets "
			+"updated every 5 seconds, and shows the current state.";
	
	public static final String DEVICE_MAC_INFO = "This view shows a table where you can see all Mac-addresses, belonging to a device. You can choose "
			+"the device and to show only Mac-Addresses that were used in a specific time intervall, or to a timestamp, or a live view, that gets updated "
			+"every 5 seconds, and shows the current state.";
	
	public static final String DEVICE_AUTH_INFO = "This view shows a table where you can see all Devices with their attributes, which were authenticated "
			+"at a timestamp you can choose.";
	
}
