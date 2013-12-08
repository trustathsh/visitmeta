package de.fhhannover.inform.trust.visitmeta.dataservice.rest;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
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
 * This file is part of VisITMeta, version 0.0.1, implemented by the Trust@FHH
 * research group at the Hochschule Hannover.
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

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import de.fhhannover.inform.trust.visitmeta.ifmap.ConnectionManager;
import de.fhhannover.inform.trust.visitmeta.ifmap.exception.ConnectionException;


@Path("{connectionName}/dump")
public class DumpingResource {


	/**
	 * Start the Dumping-Service.
	 * 
	 * !! Dumping is NOT IF-MAP 2.0 compliant an can only be used with irond. !!
	 * 
	 * Example-URL: <tt>http://example.com:8000/default/dump/start</tt>
	 */
	@PUT
	@Path("start")
	public String startDump(@PathParam("connectionName") String name) {

		try {

			ConnectionManager.deleteSubscriptionsFromConnection(name);

			ConnectionManager.startDumpingServiceFromConnection(name);

		} catch (ConnectionException e) {

			return "ERROR: dumping is not running: " + e.getClass().getSimpleName();
		};

		return "INFO: dumping starts successfully";
	}

	/**
	 * Stop the Dumping-Service.
	 * 
	 * !! Dumping is NOT IF-MAP 2.0 compliant an can only be used with irond. !!
	 * 
	 * Example-URL: <tt>http://example.com:8000/default/dump/stop</tt>
	 */
	@PUT
	@Path("stop")
	public String stopDump(@PathParam("connectionName") String name) {

		try{

			ConnectionManager.stopDumpingServiceFromConnection(name);

		} catch (ConnectionException e) {

			return "ERROR: " + e.getClass().getSimpleName();
		};

		return "INFO: dumping stop successfully";
	}
}