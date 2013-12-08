package de.fhhannover.inform.trust.visitmeta.persistence.neo4j;

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
 * This file is part of VisITMeta, version 0.0.2, implemented by the Trust@FHH 
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

import java.util.SortedMap;

import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.visitmeta.ifmap.Connection;
import de.fhhannover.inform.trust.visitmeta.persistence.AbstractReader;
public class Neo4JReader extends AbstractReader {

	private Logger log = Logger.getLogger(Connection.class);

	private Neo4JConnection mConnection;

	public Neo4JReader(Neo4JRepository repo, Neo4JConnection connection) {
		log.trace("new Neo4JReader()");
		mRepo = repo;
		mConnection = connection;
	}

	@Override
	public long getTimeOfLastUpdate() {
		return getChangesMap().lastKey();
	}

	@Override
	public SortedMap<Long, Long> getChangesMap() {
		return mConnection.getTimestampManager().getChangesMap();
	}

}
