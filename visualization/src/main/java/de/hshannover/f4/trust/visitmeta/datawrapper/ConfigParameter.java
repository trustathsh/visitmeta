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
 * This file is part of visitmeta-visualization, version 0.2.0,
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
package de.hshannover.f4.trust.visitmeta.datawrapper;

public final class ConfigParameter {

	public static final String VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT = "visualization.user.connection.dataservice.count";

	private static final String PRE_VISUALIZATION_USER_CONNECTION_DATASERVICE = "visualization.user.connection.dataservice.";

	public static final String VISUALIZATION_DEFAULT_URL = "visualization.default.url";

	public static String VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_NAME(int count){
		return PRE_VISUALIZATION_USER_CONNECTION_DATASERVICE + count + ".name";
	}

	public static String VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_URL(int count){
		return PRE_VISUALIZATION_USER_CONNECTION_DATASERVICE + count + ".url";
	}

	public static String VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_RAWXML(int count){
		return PRE_VISUALIZATION_USER_CONNECTION_DATASERVICE + count + ".rawxml";
	}
}
