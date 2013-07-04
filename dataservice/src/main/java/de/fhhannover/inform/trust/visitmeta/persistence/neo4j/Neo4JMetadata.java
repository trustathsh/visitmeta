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

import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.HIDDEN_PROPERTIES_KEY_PREFIX;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_META_CARDINALITY;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TIMESTAMP_DELETE;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TIMESTAMP_PUBLISH;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TYPE_NAME;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.NODE_TYPE_KEY;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_META_CARDINALITY_SINGLE;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_TYPE_NAME_METADATA;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_RAW_DATA;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;


public class Neo4JMetadata extends InternalMetadata {

	private static final Logger log = Logger.getLogger(Neo4JMetadata.class);

	private Neo4JRepository mRepo;
	private Node mMe;

	public Neo4JMetadata(Node n, Neo4JRepository graph){
		super();
		if (!n.getProperty(NODE_TYPE_KEY).equals(VALUE_TYPE_NAME_METADATA)) {
			String msg = "Trying to construct Metadata with a node of type "
					+n.getProperty(NODE_TYPE_KEY)+
					". We clearly disapprove and will die ungracefully now";
					throw new RuntimeException(msg);
		}
		mMe = n;
		mRepo = graph;
	}

	public Node getNode(){
		return mMe;
	}

	@Override
	public List<String> getProperties() {
		List<String> l = new ArrayList<>();
		for (String s: mMe.getPropertyKeys()) {
			if (!s.startsWith(HIDDEN_PROPERTIES_KEY_PREFIX)) {
				l.add(s);
			}
		}
		return l;
	}

	@Override
	public boolean hasProperty(String p) {
		return mMe.hasProperty(p);
	}

	@Override
	public String valueFor(String p) {
		return (String)mMe.getProperty(p);
	}

	@Override
	public String getTypeName() {
		return (String)mMe.getProperty(KEY_TYPE_NAME);
	}

	@Override
	public boolean isSingleValue() {
		String value = (String) mMe.getProperty(KEY_META_CARDINALITY);
		if(value.equals(VALUE_META_CARDINALITY_SINGLE)) {
			return true;
		}
		return false;
	}

	@Override
	public void addProperty(String name, String value) {
		//TODO this write operation should be moved to the graph as well
		if (mMe.hasProperty(name)) {
			log.warn("property '"+name+"' already exists, overwriting with '"+value+"'");
		}
		Transaction tx = mRepo.beginTx();
		try {
			mMe.setProperty(name, value);
			tx.success();
		} finally {
			tx.finish();
		}

	}

	@Override
	public long getPublishTimestamp() {
		String value = mMe.getProperty(KEY_TIMESTAMP_PUBLISH).toString();
		return Long.parseLong(value);
	}

	@Override
	public long getDeleteTimestamp() {
		String value =
				mMe.getProperty(KEY_TIMESTAMP_DELETE, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP+"").toString();
		return Long.parseLong(value);
	}

	@Override
	public void setPublishTimestamp(long timestamp) {
		Transaction tx = mRepo.beginTx();
		try {
			mMe.setProperty(KEY_TIMESTAMP_PUBLISH, timestamp);
			tx.success();
		} finally {
			tx.finish();
		}
	}

	@Override
	public String getRawData() {
		return (String)mMe.getProperty(KEY_RAW_DATA, "");
	}
}
