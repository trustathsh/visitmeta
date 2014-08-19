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

package de.hshannover.f4.trust.metalyzer.semantics.services;

import de.hshannover.f4.trust.metalyzer.api.MetalyzerAPI;
import de.hshannover.f4.trust.metalyzer.semantics.rest.SemanticsDeviceResource;
import de.hshannover.f4.trust.metalyzer.semantics.rest.SemanticsIpAddressResource;
import de.hshannover.f4.trust.metalyzer.semantics.rest.SemanticsMacAddressResource;
import de.hshannover.f4.trust.metalyzer.semantics.rest.SemanticsUserResource;
import de.hshannover.f4.trust.metalyzer.statistic.StatisticController;

/**
 * Controller-Class to instantiate all Service- and Resource-Classes and to delegate
 * the incoming requests from the Resource-Classes to the specific Service-Classes.
 * It also delegates the requests of all semantics-services to the MetalyzerAPI. 
 *
 */
public class SemanticsController {
	//Enums for the 3 different types of semantic-requests.
	public enum RequestType {
		TIMESTAMP_REQUEST, TIMEINTERVAL_REQUEST, CURRENT_REQUEST
	}
	
	//Semantics-Services, Semantics-Resources, MetalyzerAPI and statisticController 
	private static SemanticsController semController;
	private MetalyzerAPI mAPI;
	private UserService userServ;
	private DeviceService devServ;
	private IpAddressService ipServ;
	private MacAddressService macServ;
	private SemanticsUserResource semUserRes;
	private SemanticsIpAddressResource semIpAddressRes;
	private SemanticsMacAddressResource semMacAddressRes;
	private SemanticsDeviceResource semDeviceRes;
	private StatisticController evaCon;
	
	private String connectionName;
	
	/**
	 * Constructor
	 * (Empty)
	 */
	private SemanticsController() {
	}
	
	/**
	 * Initialization of the SemanticsController and the semantics-services.
	 */
	public void initialize(){
		//mAPI= MetalyzerAPI.getInstance();
		userServ= new UserService();
		devServ= new DeviceService();
		ipServ= new IpAddressService();
		macServ= new MacAddressService();
		semUserRes = new SemanticsUserResource();
		semIpAddressRes = new SemanticsIpAddressResource();
		semMacAddressRes = new SemanticsMacAddressResource();
		semDeviceRes = new SemanticsDeviceResource();
		connectionName= "default";
		//evaCon= new EvaluationController(); // Connection to the statistic-layer; for later use
	}
	
	/**
	 * GetInstance-Method for singleton-pattern.
	 * @return 
	 * Returns an object of the SemanticsController.
	 */
	
	public static synchronized SemanticsController getInstance() {
		if (semController == null) {
			semController = new SemanticsController();
			semController.initialize();
		}
		return semController;
	}
	
	/**
	 * To get the connection to MetalyzerAPI.
	 * @return 
	 * Returns an object of the MetalyzerAPI.
	 */
	
	public MetalyzerAPI getConnection(){
		return MetalyzerAPI.getInstance(connectionName);
	}
	
	public void setConnection(String connectionName){
		if(connectionName != null){
			this.connectionName= connectionName;
		}
	}
	
	/**
	 * To get the instance of the UserService.
	 * @return 
	 * Returns an object of the UserService.
	 */
	public UserService getUserService() {
		return userServ;
	}
	
	/**
	 * To get the instance of the DeviceService.
	 * @return 
	 * Returns an object of the DeviceService.
	 */
	public DeviceService getDeviceService() {
		return devServ;
	}
	
	/**
	 * To get the instance of the IpAddressService.
	 * @return 
	 * Returns an object of the IpAddressService.
	 */
	public IpAddressService getIpAddressService() {
		return ipServ;
	}
	
	/**
	 * To get the instance of the MacAddressService.
	 * @return 
	 * Returns an object of the MacAddressService.
	 */
	public MacAddressService getMacAddressService() {
		return macServ;
	}
	
	/**
	 * To get the instance of the SemanticsUserResource.
	 * @return 
	 * Returns an object of the SemanticsUserResource.
	 */
	public SemanticsUserResource getSemanticsUserResource() {
		return semUserRes;
	}
	
	/**
	 * To get the instance of the SemanticsIpAddressResource.
	 * @return 
	 * Returns an object of the SemanticsIpAddressResource.
	 */
	public SemanticsIpAddressResource getSemanticsIpAddressResource() {
		return semIpAddressRes;
	}
	
	/**
	 * To get the instance of the SemanticsMacAddressResource.
	 * @return 
	 * Returns an object of the SemanticsMacAddressResource.
	 */
	public SemanticsMacAddressResource getSemanticsMacAddressResource() {
		return semMacAddressRes;
	}
	
	/**
	 * To get the instance of the SemanticsDeviceResource.
	 * @return 
	 * Returns an object of the SemanticsDeviceResource.
	 */
	public SemanticsDeviceResource getSemanticsDeviceResource() {
		return semDeviceRes;
	}
	
	/**
	 * To get the connection to the statisticsController.
	 * @return
	 * Returns an object of the StatisticController.
	 */
	public StatisticController getEvaluationController() {
		return evaCon;
	}
}
