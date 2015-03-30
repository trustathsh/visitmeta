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
 * This file is part of visitmeta-dataservice, version 0.4.1,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
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
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_META_CARDINALITY;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_RAW_DATA;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TIMESTAMP_DELETE;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TIMESTAMP_PUBLISH;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TYPE_NAME;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_META_CARDINALITY_SINGLE;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Transaction;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;

public class Neo4JMetadata extends InternalMetadata {

	private static final Logger log = Logger.getLogger(Neo4JMetadata.class);

	private Neo4JRepository mRepo;
	private Node mMe;

	public Neo4JMetadata(Node n, Neo4JRepository graph) {
		super();
		try (Transaction tx = graph.beginTx()) {
			if (!n.hasLabel(Neo4JTypeLabels.METADATA)) {
				String msg = "Trying to construct Metadata without METADATA Label"
						+ ". We clearly disapprove and will die ungracefully now";
				throw new RuntimeException(msg);
			}
			tx.success();
		}
		mMe = n;
		mRepo = graph;
	}

	public Node getNode() {
		return mMe;
	}

	@Override
	public List<String> getProperties() {
		List<String> l = new ArrayList<>();
		try (Transaction tx = mRepo.beginTx()) {
			for (String s : mMe.getPropertyKeys()) {
				if (!s.startsWith(HIDDEN_PROPERTIES_KEY_PREFIX)) {
					l.add(s);
				}
			}
			tx.success();
		}
		return l;
	}

	@Override
	public boolean hasProperty(String p) {
		boolean result = false;
		try (Transaction tx = mRepo.beginTx()) {
			result = mMe.hasProperty(p);
			tx.success();
		}
		return result;
	}

	@Override
	public String valueFor(String p) {
		String value = "";
		try (Transaction tx = mRepo.beginTx()) {
			value = (String) mMe.getProperty(p);
			tx.success();
		} catch (NotFoundException e) {
			log.warn("Property " + p + " does not exist in Metadata " + this);
		}
		return value;
	}

	@Override
	public String getTypeName() {
		// TODO handle false Metadata
		String type = "";
		try (Transaction tx = mRepo.beginTx()) {
			type = (String) mMe.getProperty(KEY_TYPE_NAME);
			tx.success();
		} catch (NotFoundException e) {
			log.warn("This Metadata does not has a Type declared! " + this);
		}
		return type;
	}

	@Override
	public boolean isSingleValue() {
		// TODO handle false Metadata
		String value = "";
		try (Transaction tx = mRepo.beginTx()) {
			value = (String) mMe.getProperty(KEY_META_CARDINALITY);
			tx.success();
		} catch (NotFoundException e) {
			log.warn("This Metadata does not has the Cardinality-Property set! "
					+ this);
		}
		if (value.equals(VALUE_META_CARDINALITY_SINGLE)) {
			return true;
		}
		return false;
	}

	@Override
	public void addProperty(String name, String value) {
		try (Transaction tx = mRepo.beginTx()) {
			// TODO this write operation should be moved to the graph as well
			if (mMe.hasProperty(name)) {
				log.warn("property '" + name
						+ "' already exists, overwriting with '" + value + "'");
			}
			mMe.setProperty(name, value);
			tx.success();
		}

	}

	@Override
	public long getPublishTimestamp() {
		// TODO handle false Metadata
		String value = "";
		try (Transaction tx = mRepo.beginTx()) {
			value = mMe.getProperty(KEY_TIMESTAMP_PUBLISH).toString();
			tx.success();
		} catch (NotFoundException e) {
			log.warn("This Metadata does not has a PublishTimestamp set! "
					+ this);
		}
		return Long.parseLong(value);
	}

	@Override
	public long getDeleteTimestamp() {
		// TODO handle false Metadata
		if(this.isNotify()) {
			return getPublishTimestamp();
		}
		String value = "";
		try (Transaction tx = mRepo.beginTx()) {
			value = mMe.getProperty(KEY_TIMESTAMP_DELETE,
					InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP + "")
					.toString();
			tx.success();
		} catch (NotFoundException e) {
			log.warn("This Metadata does not has a DeleteTimestamp! " + this);
		}
		return Long.parseLong(value);
	}

	@Override
	public void setPublishTimestamp(long timestamp) {
		try (Transaction tx = mRepo.beginTx()) {
			mMe.setProperty(KEY_TIMESTAMP_PUBLISH, timestamp);
			tx.success();
		}
	}

	@Override
	public String getRawData() {
		String result = "";
		try (Transaction tx = mRepo.beginTx()) {
			result = (String) mMe.getProperty(KEY_RAW_DATA, "");
			tx.success();
		} catch (NotFoundException e) {
			log.warn("This Metadata does not has Raw Data! " + this);
		}
		return result;
	}
}
