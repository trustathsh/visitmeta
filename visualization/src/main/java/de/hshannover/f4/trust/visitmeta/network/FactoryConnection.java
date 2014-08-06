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
 * This file is part of visitmeta visualization, version 0.1.1,
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
package de.hshannover.f4.trust.visitmeta.network;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;

/**
 * Factory for a Connection.
 */
public abstract class FactoryConnection {

	private static final Logger LOGGER = Logger.getLogger(FactoryConnection.class);

	public static enum ConnectionType {
		LOCAL,
		REST;
	}

	/**
	 * Returns a Connection defined by type.
	 * @param type define witch Connection to return.
	 *        "LOCAL" a Connection that run the DataService in an extra thread and connect to the
	 *                if-map server.
	 *        TODO "REST" a Connection that connect to the DataService over REST.
	 */
	public static Connection getConnection(ConnectionType type, GraphContainer connection) {
		LOGGER.trace("Method getConnection(" + type + ") called.");
		GraphService graphService = null;
		switch(type) {
		case LOCAL:
			LOGGER.error("Created a new local connection to dataservice");
			throw new UnsupportedOperationException("Local connection not implemented.");
		case REST:
			DataserviceConnection dataservice = connection.getDataserviceConnection();
			boolean includeRawXML = dataservice.isRawXml();
			graphService = new ProxyGraphService(dataservice.getGraphResource(connection.getRestConnectionName()), includeRawXML);

			LOGGER.info("Create a new REST connection to dataservice with URI: " + dataservice.getGraphResource(connection.getRestConnectionName()).getURI());
			return new Connection(graphService, connection);

		default:
			throw new RuntimeException("Error creating connection to dataservice; tried with type '" + type.name() + "'");
		}
	}
}
