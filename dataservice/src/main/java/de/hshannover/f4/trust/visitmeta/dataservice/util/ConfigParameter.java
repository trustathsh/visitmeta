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
 * This file is part of visitmeta dataservice, version 0.0.3,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.dataservice.util;



public final class ConfigParameter {

	public static final String IFMAP_TRUSTSTORE_PATH = "ifmap.truststore.path";
	public static final String IFMAP_TRUSTSTORE_PASS = "ifmap.truststore.pw";
	public static final String IFMAP_BASIC_AUTH_URL = "ifmap.auth.url";
	public static final String IFMAP_USER = "ifmap.user";
	public static final String IFMAP_PASS = "ifmap.pw";
	public static final String IFMAP_START_IDENTIFIER = "ifmap.start.identifier";
	public static final String IFMAP_START_IDENTIFIER_TYPE = "ifmap.start.type";
	public static final String IFMAP_MAX_DEPTH = "ifmap.maxdepth";
	public static final String IFMAP_MAX_SIZE = "ifmap.maxsize";
	public static final String IFMAP_SUBSCRIPTION_NAME = "ifmap.subscription.name";
		public static final String IFMAP_SUBSCRIPTION_DUMPING_SLEEPTIME = "ifmap.subscription.dumping.sleeptime";

	public static final String IFMAP_MAX_RETRY = "ifmap.connection.maxretry";
	public static final String IFMAP_RETRY_INTERVAL = "ifmap.connection.retryinterval";

	public static final String NEO4J_DB_PATH = "neo4j.db.path";
	public static final String NEO4J_HASH_ALGO = "neo4j.db.hashalgo";
	public static final String NEO4J_CLEAR_DB_ON_STARTUP = "neo4j.db.clear";

	public static final String DS_REST_URL = "rest.url";
	public static final String DS_REST_ENABLE = "rest.enable";
	public static final String DS_CACHE_ENABLE = "cache.enable";
	public static final String DS_CACHE_SIZE = "cache.size";

}
