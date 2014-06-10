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
 * This file is part of visitmeta dataservice, version 0.0.5,
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
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_HASH;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;

public class Neo4JIdentifier extends InternalIdentifier {

	private static final Logger logger = Logger.getLogger(Neo4JIdentifier.class);

	private Neo4JRepository mRepo;
	private Node mMe;

	public Neo4JIdentifier(Node n, Neo4JRepository graph) {
		super();
		if (!n.getProperty(NODE_TYPE_KEY).equals(VALUE_TYPE_NAME_IDENTIFIER)) {
			String msg = "Trying to construct an Identifier with a node of type " + n.getProperty(NODE_TYPE_KEY)
					+ ". We clearly disapprove and will die ungracefully now";
			throw new RuntimeException(msg);
		}
		mMe = n;
		mRepo = graph;
	}

	@Override
	public List<String> getProperties() {
		List<String> l = new ArrayList<>();
		for (String s : mMe.getPropertyKeys()) {
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
		return (String) mMe.getProperty(p);
	}

	@Override
	public String getTypeName() {
		return (String) mMe.getProperty(KEY_TYPE_NAME);
	}

	@Override
	public List<InternalLink> getLinks() {
		Iterator<Relationship> i = mMe.getRelationships(LinkTypes.Link).iterator();
		List<InternalLink> ll = new ArrayList<>();

		while (i.hasNext()) {
			Relationship r = i.next();
			InternalLink link = mRepo.getLink(r.getEndNode().getId());
			ll.add(link);
		}
		return ll;
	}

	@Override
	public List<InternalMetadata> getMetadata() {
		Iterator<Relationship> i = mMe.getRelationships(LinkTypes.Meta).iterator();
		List<InternalMetadata> lm = new ArrayList<>();

		while (i.hasNext()) {
			Relationship r = i.next();
			InternalMetadata metadata = mRepo.getMetadata(r.getEndNode().getId());
			lm.add(metadata);
		}
		return lm;
	}

	public Node getNode() {
		return mMe;
	}

	public String getHash() {
		return (String) mMe.getProperty(KEY_HASH);
	}

	/**
	 * Adds a given Metadata to the Identifier
	 * @param Metadata to add
	 */
	@Override
	public void addMetadata(InternalMetadata m) {
		Neo4JMetadata metadata = (Neo4JMetadata) mRepo.insert(m);
		mRepo.connectMeta(this, metadata);
	}

	/**
	 * Removes a given Metadata from the Identifier
	 * @param Metadata to remove
	 */
	@Override
	public void removeMetadata(InternalMetadata m) {
		for (Relationship r : mMe.getRelationships(LinkTypes.Meta)) {
			Neo4JMetadata n4jm = (Neo4JMetadata) mRepo.getMetadata(r.getEndNode().getId());
			if (m.equalsForLinks(n4jm)) {
				mRepo.removeMetadata(n4jm.getNode());
				break;
			}
		}
	}
	
	/**
	 * Updates the Metadata if it is a SingleValue Metadata and if (and only if!) an old node exists in the graph.
	 * @param SingleValue Metadata to update
	 */
	@Override
	public void updateMetadata(InternalMetadata m) {
		for (Relationship r : mMe.getRelationships(LinkTypes.Meta)) {
			Neo4JMetadata n4jm = (Neo4JMetadata) mRepo.getMetadata(r.getEndNode().getId());
			if (m.equalsForLinks(n4jm)) {
				Neo4JMetadata newM = (Neo4JMetadata) mRepo.updateMetadata(n4jm, m);
				mRepo.connectMeta(this, newM);
				break;
			}
		}
	}
	
	/**
	 * Check if this link has the given metadata connected to it.
	 * @param Metadata to check for
	 */
	@Override
	public boolean hasMetadata(InternalMetadata meta) {
		for (Relationship r : mMe.getRelationships(LinkTypes.Meta)) {
			Neo4JMetadata n4jm = (Neo4JMetadata) mRepo.getMetadata(r.getEndNode().getId());
			if (meta.equalsForLinks(n4jm)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes every Metadata from the Identifier
	 */
	@Override
	public void clearMetadata() {
		for (Relationship r : mMe.getRelationships(LinkTypes.Meta)) {
			mRepo.remove(r.getEndNode().getId());
		}
	}
	
	@Override
	public void addProperty(String name, String value) {
		Transaction tx = mRepo.beginTx();
		try {

			if (mMe.hasProperty(name)) {
				logger.warn("property '" + name + "' already exists, overwriting with '" + value + "'");
			}
			mMe.setProperty(name, value);
			tx.success();
		} finally {
			tx.finish();
		}

	}

	@Override
	public void clearLinks() {
		for (InternalLink l : getLinks())
			mRepo.remove(((Neo4JLink) l).getNode().getId());
	}

	@Override
	public void removeLink(InternalLink link) {
		for (InternalLink l : getLinks()) {
			if (l.equals(link)) {
				mRepo.remove(((Neo4JLink) l).getNode().getId());
				break;
			}
		}
	}

	@Override
	public String getRawData() {
		return (String) mMe.getProperty(KEY_RAW_DATA, "");
	}
}
