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
 * This file is part of visitmeta dataservice, version 0.1.1,
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
package de.hshannover.f4.trust.visitmeta.persistence.neo4j;



import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.HIDDEN_PROPERTIES_KEY_PREFIX;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.*;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;


public class Neo4JTimestampManager {
	private Neo4JConnection mConnection;
	private long mLastTimestamp;

	public Neo4JTimestampManager(Neo4JConnection connection) {
		mConnection = connection;
	}


	public long getLastTimestamp() {
		return mLastTimestamp;
	}

	public void incrementCounter(long timestamp) {
		Transaction tx = mConnection.getConnection().beginTx();
		try {
			Node changeNode = null;
			Iterator<Relationship> change = mConnection.getConnection()
					.getReferenceNode().getRelationships(LinkTypes.Change)
					.iterator();
			if (!change.hasNext()) {
				changeNode = mConnection.getConnection().createNode();
				changeNode.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_TIME_MAP);
				mConnection.getConnection().getReferenceNode()
						.createRelationshipTo(changeNode, LinkTypes.Change);
			} else {
				Relationship r = change.next();
				changeNode = r.getEndNode();
			}
			if (!changeNode.hasProperty(timestamp + "")) {
				changeNode.setProperty(timestamp + "",
						(long) 1);
			} else {
				long count = (long) changeNode.getProperty(timestamp+"");
				count++;
				changeNode.setProperty(timestamp+"", count);

			}
			tx.success();
		} finally {
			tx.finish();
		}
	}


	public SortedMap<Long, Long> getChangesMap() {
		TreeMap<Long, Long> changeMap = new TreeMap<>();
		Node changeNode = null;
		Iterator<Relationship> change = mConnection.getConnection().getReferenceNode().
				getRelationships(LinkTypes.Change).iterator();
		if(!change.hasNext()) {
			return changeMap;
		}
		Relationship r = change.next();
		changeNode = r.getEndNode();
		for (String s : changeNode.getPropertyKeys()) {
			if (!s.contains(HIDDEN_PROPERTIES_KEY_PREFIX))
				changeMap.put(Long.valueOf(s), (Long)changeNode.getProperty(s));
		}
		return changeMap;
	}


	public Long getCurrentTime() {
		mLastTimestamp = System.currentTimeMillis();
		return mLastTimestamp;
	}

}
