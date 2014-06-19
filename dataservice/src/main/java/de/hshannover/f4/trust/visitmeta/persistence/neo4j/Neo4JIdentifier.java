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

	private static final Logger logger = Logger
			.getLogger(Neo4JIdentifier.class);

	private Neo4JRepository mRepo;
	private Node mMe;

	public Neo4JIdentifier(Node n, Neo4JRepository graph) {
		super();
		Transaction tx = graph.beginTx();
		try {
			if (!n.hasLabel(Neo4JTypeLabels.IDENTIFIER)) {
				String msg = "Trying to construct Identifier without IDENTIFIER Label"
						+ ". We clearly disapprove and will die ungracefully now";
				throw new RuntimeException(msg);
			}
			mMe = n;
			mRepo = graph;
			tx.success();
		} finally {
			tx.finish();
		}
	}

	@Override
	public List<String> getProperties() {
		Transaction tx = mRepo.beginTx();
		List<String> l = new ArrayList<>();
		try {
			for (String s : mMe.getPropertyKeys()) {
				if (!s.startsWith(HIDDEN_PROPERTIES_KEY_PREFIX)) {
					l.add(s);
				}
			}
			tx.success();
		} finally {
			tx.finish();
		}
		return l;
	}

	@Override
	public boolean hasProperty(String p) {
		Transaction tx = mRepo.beginTx();
		boolean result = false;
		try {
			result = mMe.hasProperty(p);
			tx.success();
		} finally {
			tx.finish();
		}
		return result;
	}

	@Override
	public String valueFor(String p) {
		Transaction tx = mRepo.beginTx();
		String result = null;
		try {
			result = (String) mMe.getProperty(p);
			tx.success();
		} finally {
			tx.finish();
		}
		return result;
	}

	@Override
	public String getTypeName() {
		Transaction tx = mRepo.beginTx();
		String result = null;
		try {
			result = (String) mMe.getProperty(KEY_TYPE_NAME);
			tx.success();
		} finally {
			tx.finish();
		}
		return result;
	}

	@Override
	public List<InternalLink> getLinks() {
		Transaction tx = mRepo.beginTx();
		List<InternalLink> ll = new ArrayList<>();
		try {
			Iterator<Relationship> i = mMe.getRelationships(LinkTypes.Link)
					.iterator();
	
			while (i.hasNext()) {
				Relationship r = i.next();
				InternalLink link = mRepo.getLink(r.getEndNode().getId());
				ll.add(link);
			}
			tx.success();
		} finally {
			tx.finish();
		}
		return ll;
	}

	@Override
	public List<InternalMetadata> getMetadata() {
		Transaction tx = mRepo.beginTx();
		List<InternalMetadata> lm = new ArrayList<>();
		try {
			Iterator<Relationship> i = mMe.getRelationships(LinkTypes.Meta)
					.iterator();
	
			while (i.hasNext()) {
				Relationship r = i.next();
				InternalMetadata metadata = mRepo.getMetadata(r.getEndNode()
						.getId());
				lm.add(metadata);
			}
			tx.success();
		} finally {
			tx.finish();
		}
		return lm;
	}

	public Node getNode() {
		return mMe;
	}

	public String getHash() {
		Transaction tx = mRepo.beginTx();
		String result = null;
		try {
			result = (String) mMe.getProperty(KEY_HASH);
			tx.success();
		} finally {
			tx.finish();
		}
		return result;
	}

	/**
	 * Adds a given Metadata to the Identifier
	 * 
	 * @param Metadata
	 *            to add
	 */
	@Override
	public void addMetadata(InternalMetadata m) {
		Neo4JMetadata metadata = (Neo4JMetadata) mRepo.insert(m);
		mRepo.connectMeta(this, metadata);
	}

	/**
	 * Removes a given Metadata from the Identifier
	 * 
	 * @param Metadata
	 *            to remove
	 */
	@Override
	public void removeMetadata(InternalMetadata m) {
		Transaction tx = mRepo.beginTx();
		try {
			for (Relationship r : mMe.getRelationships(LinkTypes.Meta)) {
				Neo4JMetadata n4jm = (Neo4JMetadata) mRepo.getMetadata(r
						.getEndNode().getId());
				if (m.equalsForLinks(n4jm)) {
					mRepo.removeMetadata(n4jm.getNode());
					break;
				}
			}
			tx.success();
		} finally {
			tx.finish();
		}
	}

	/**
	 * Updates the Metadata if it is a SingleValue Metadata and if (and only
	 * if!) an old node exists in the graph.
	 * 
	 * @param SingleValue
	 *            Metadata to update
	 */
	@Override
	public void updateMetadata(InternalMetadata m) {
		Transaction tx = mRepo.beginTx();
		try {
			for (Relationship r : mMe.getRelationships(LinkTypes.Meta)) {
				Neo4JMetadata n4jm = (Neo4JMetadata) mRepo.getMetadata(r
						.getEndNode().getId());
				if (m.equalsForLinks(n4jm)) {
					Neo4JMetadata newM = (Neo4JMetadata) mRepo.updateMetadata(n4jm,
							m);
					mRepo.connectMeta(this, newM);
					break;
				}
			}
			tx.success();
		} finally {
			tx.finish();
		}
	}

	/**
	 * Check if this link has the given metadata connected to it.
	 * 
	 * @param Metadata
	 *            to check for
	 */
	@Override
	public boolean hasMetadata(InternalMetadata meta) {
		Transaction tx = mRepo.beginTx();
		boolean result = false;
		try {
			for (Relationship r : mMe.getRelationships(LinkTypes.Meta)) {
				Neo4JMetadata n4jm = (Neo4JMetadata) mRepo.getMetadata(r
						.getEndNode().getId());
				if (meta.equalsForLinks(n4jm)) {
					result = true;
				}
			}
			tx.success();
		} finally {
			tx.finish();
		}
		return result;
	}

	/**
	 * Removes every Metadata from the Identifier
	 */
	@Override
	public void clearMetadata() {
		Transaction tx = mRepo.beginTx();
		try {
			for (Relationship r : mMe.getRelationships(LinkTypes.Meta)) {
				mRepo.remove(r.getEndNode().getId());
			}
			tx.success();
		} finally {
			tx.finish();
		}
	}

	@Override
	public void addProperty(String name, String value) {
		Transaction tx = mRepo.beginTx();
		try {

			if (mMe.hasProperty(name)) {
				logger.warn("property '" + name
						+ "' already exists, overwriting with '" + value + "'");
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
		Transaction tx = mRepo.beginTx();
		String result = null;
		try {
			result = (String) mMe.getProperty(KEY_RAW_DATA, "");
			tx.success();
		} finally {
			tx.finish();
		}
		return result;
	}
}
