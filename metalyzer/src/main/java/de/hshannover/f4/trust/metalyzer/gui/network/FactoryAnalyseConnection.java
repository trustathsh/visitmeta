package de.hshannover.f4.trust.metalyzer.gui.network;


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

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hshannover.f4.trust.metalyzer.interfaces.DeviceService;
import de.hshannover.f4.trust.metalyzer.interfaces.IpAddressService;
import de.hshannover.f4.trust.metalyzer.interfaces.MacAddressService;
import de.hshannover.f4.trust.metalyzer.interfaces.StatisticService;
import de.hshannover.f4.trust.metalyzer.interfaces.UserService;

/** 
 * Project: Metalyzer
 * Author: Anton Zaiser
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class FactoryAnalyseConnection {

	private static final int THREADPOOL_SIZE = 10;
	/**
	 * Returns a WebResource with given url
	 * @param {@link String} url
	 * @return {@link WebResource}
	 * */
	private static AsyncWebResource getWebResource(String url) {
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_THREADPOOL_SIZE, THREADPOOL_SIZE);
		Client client = Client.create(config);
		URI uri = UriBuilder.fromUri(url).build();
		return client.asyncResource(uri);
	}
	
	/**
	 * Returns an AnalyseConnection, a REST-Connection to the graph and analyse service
	 * @param url the path of the rest server
	 * @return {@link AnalyseConnection}
	 * */
	public static AnalyseConnection getConnection(String url) {
		return getConnection(url, "");
	}
	
	/**
	 * Returns an AnalyseConnection, a REST-Connection to the graph and analyse service
	 * @param url the path of the rest server
	 * @param connection has to be specified, if you use an different connection, than 'default'
	 * is connection empty the default connections is used
	 * @return {@link AnalyseConnection}
	 * */
	public static AnalyseConnection getConnection(String url, String connection) {
		UserService userService = null;
		IpAddressService ipAddressService = null;
		MacAddressService macAddressService = null;
		DeviceService deviceService = null;
		StatisticService statisticService = null;
		
		String restUrl = "";
		StringBuilder restBuilder = new StringBuilder();
		if(!connection.isEmpty()) {
			restBuilder.append(url).append("/").append(connection).append("/").append("analysis");
		} else {
			restBuilder.append(url).append("/").append("default").append("/").append("analysis");
		}
		restUrl = restBuilder.toString();
		if(restUrl.isEmpty()) {
			throw new RuntimeException("Rest url is invalid: "+restUrl);
		}
		
		AsyncWebResource analyseResource = getWebResource(restUrl);
		
		userService = new ProxyUserService(analyseResource);
		ipAddressService = new ProxyIpAddressService(analyseResource);
		macAddressService = new ProxyMacAddressService(analyseResource);
		deviceService = new ProxyDeviceService(analyseResource);
		statisticService = new ProxyStatisticService(analyseResource);
		
		return new AnalyseConnection(userService, ipAddressService, macAddressService, deviceService, statisticService);
	}
}
