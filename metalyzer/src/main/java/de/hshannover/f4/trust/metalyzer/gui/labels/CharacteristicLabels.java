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
 * This class provides a bunch of static label descriptions for all characteristics
 * */

public class CharacteristicLabels {
	/*Statisitic Characteristics*/
	public static final String FREQUENCY_IDENTIFER= "Frequency of Identifer";
	public static final String FREQUENCY_METADATA = "Frequency of Metadata";
	public static final String FREQUENCY_ROLES = "Frequency of Roles";
	
	public static final String MEAN_LINKS = "Mean of Links";

	public static final String GRAPH_NODES = "Graph: Nodes";
	public static final String GRAPH_EDGES = "Graph: Edges";
	public static final String GRAPH_MEAN ="Graph: Mean of Metadata";
	
	/*Semantic Characteristics*/
	public static final String USER_ALL = "Overview of Users";
	public static final String USER_IP = "IP-Addresses of Users";
	public static final String USER_MAC = "Mac-Addresses of Users";
	public static final String USER_DEVICES = "Devices of Users";
	public static final String USER_AUTH = "Authenticated Users and Roles";
	
	public static final String IP_ALL = "Overview of IP-Addresses";
	public static final String IP_USER = "Users of IP-Addresses";
	public static final String IP_MAC = "Mac-Addresses of IP-Addresses";
	public static final String IP_DEVICES = "Devices of IP-Addresses";
	public static final String IP_AUTH = "Authenticated IP-Addresses";
	
	public static final String MAC_ALL = "Overview of Mac-Addresses";
	public static final String MAC_IP = "IP-Addresses of Mac-Addresses";
	public static final String MAC_USER = "Users of Mac-Addresses";
	public static final String MAC_DEVICES = "Devices of Mac-Addresses";
	public static final String MAC_AUTH = "Authenticated of Mac-Addresses";
	
	public static final String DEVICE_ALL = "Overview of Devices";
	public static final String DEVICE_IP = "IP-Addresses of Devices";
	public static final String DEVICE_MAC = "Mac-Addresses of Devices";
	public static final String DEVICE_USER = "User of Devices";
	public static final String DEVICE_AUTH = "Authenticated of Devices";

}
