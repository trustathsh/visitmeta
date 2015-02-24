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
 * This file is part of visitmeta-dataservice, version 0.3.0,
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



import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_HASH;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_META_CARDINALITY;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_RAW_DATA;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TIMESTAMP_DELETE;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TIMESTAMP_PUBLISH;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TYPE_NAME;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_META_CARDINALITY_MULTI;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_META_CARDINALITY_SINGLE;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.ReadableIndex;
import org.neo4j.tooling.GlobalGraphOperations;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.persistence.Repository;

public class Neo4JRepository implements Repository {

	private static final Logger log = Logger.getLogger(Neo4JRepository.class);
	private Neo4JConnection mConnection;
	private MessageDigest mMessageDigest;

	private Neo4JRepository() {
		log.trace("new Neo4JRepository()");
	}

	public Neo4JRepository(Neo4JConnection db, MessageDigest digest) {
		this();
		mConnection = db;
		mMessageDigest = digest;
	}
	/**
	 * Search an identifier in the database using the hash of the given identifier
	 *
	 * @param id
	 */
	@Override
	public InternalIdentifier findIdentifier(InternalIdentifier id) {
		ReadableIndex<Node> ri =
				mConnection.getConnection().index().getNodeAutoIndexer().getAutoIndex();
		String hash = calcHash(id);
		log.trace("Looking for Identifier with hash " +hash+ " in the database ...");
		Node n = ri.get(KEY_HASH, hash).getSingle();

		if (n == null) {
			log.trace("Identifier is not in the database");
			return null;
		} else {
			log.trace("Found Identifier in the database");
			Neo4JIdentifier neoId = new Neo4JIdentifier(n, this);
			return neoId;
		}
	}
	/**
	 * Search for a link-node between two given identifier.
	 *
	 * @param id1
	 * @param id2
	 */
	@Override
	public InternalLink findCommonLink(InternalIdentifier id1, InternalIdentifier id2) {
		log.trace("Looking for a Link between Identifiers " +id1+ " and " +id2+ " ...");
		Neo4JIdentifier idGraph1 = (Neo4JIdentifier) findIdentifier(id1);
		Neo4JIdentifier idGraph2 = (Neo4JIdentifier) findIdentifier(id2);

		for (Relationship r : idGraph1.getNode().getRelationships(LinkTypes.Link)) {
			for (Relationship rlink : r.getEndNode().getRelationships(LinkTypes.Link)) {
				if (rlink.getStartNode().getId() == idGraph2.getNode().getId()) {
					Neo4JLink link = new Neo4JLink(rlink.getEndNode(), this);
					log.trace("Found Link " +link);
					return link;
				}
			}
		}
		log.trace("No common Link found");
		return null;
	}

	private Node getNodeById(long id) {
		log.trace("Looking for a node with the id " +id+ " in the database ...");
		try {
			Node node = mConnection.getConnection().getNodeById(id);
			log.trace("Node found");
			return node;
		} catch (NotFoundException e) {
			String msg = "No such node in the database";
			log.error(msg);
			throw new RuntimeException(msg, e);
		}
	}

	@Override
	public InternalLink getLink(long id) {
		log.trace("Looking for a Link with the id " +id+ " in the database ...");
		Node node = getNodeById(id);
		Neo4JLink link = new Neo4JLink(node, this);
		return link;
	}

	@Override
	public InternalMetadata getMetadata(long id) {
		log.trace("Looking for Metadata with the id " +id+ " in the database ...");
		Node node = getNodeById(id);
		Neo4JMetadata meta = new Neo4JMetadata(node, this);
		if(node.hasLabel(Neo4JTypeLabels.NOTIFY)) {
			meta.setIsNotify(true);
		}
		return meta;
	}

	@Override
	public InternalIdentifier getIdentifier(long id) {
		log.trace("Looking for an Identifier with the id " +id+ " in the database ...");
		Node node = getNodeById(id);
		Neo4JIdentifier identifier = new Neo4JIdentifier(node, this);
		return identifier;
	}

	String calcHash(Propable p) {
		String hash = new String();
		List<String> l = p.getProperties();
		java.util.Collections.sort(l);
		Iterator<String> i = l.iterator();

		if (p instanceof InternalIdentifier) {
			hash += "id:";
		}
		if (p instanceof InternalMetadata) {
			hash += "meta:";
		}
		hash += p.getTypeName();
		while (i.hasNext()) {
			String key = i.next();
			hash += key + ":";
			hash += p.valueFor(key);
		}
		byte[] digest = mMessageDigest.digest(hash.getBytes());
		BigInteger big = new BigInteger(1, digest);
		String hexDigest = big.toString(16);

		int digestLengthInByte = mMessageDigest.getDigestLength();
		int digestLengthInHex  = digestLengthInByte * 8 / 4;

		while (hexDigest.length() < digestLengthInHex) {
			hexDigest = "0" + hexDigest;
		}
		return hexDigest;
	}

	@Override
	public InternalIdentifier insert(InternalIdentifier id) {
		log.trace("Adding Identifier " +id+ " to the database");
		Neo4JIdentifier neoI = null;
		try (Transaction tx = beginTx()) {
			Node n = mConnection.getConnection().createNode();
			n.addLabel(Neo4JTypeLabels.IDENTIFIER);
			n.setProperty(KEY_TYPE_NAME, id.getTypeName());
			n.setProperty(KEY_HASH, calcHash(id));
			n.setProperty(KEY_RAW_DATA, id.getRawData());

			Iterator<String> i = id.getProperties().iterator();
			String keyProp;
			while (i.hasNext()) {
				keyProp = i.next();
				n.setProperty(keyProp, id.valueFor(keyProp));
			}
			neoI = new Neo4JIdentifier(n, this);
			tx.success();
		}

		return neoI;
	}

	@Override
	public InternalMetadata insert(InternalMetadata meta) {
		log.trace("Adding Metadata " +meta+ " to the database");
		Neo4JMetadata m = null;
		try (Transaction tx = beginTx()) {
			Node metaNode = mConnection.getConnection().createNode();
			metaNode.addLabel(Neo4JTypeLabels.METADATA);
			metaNode.setProperty(KEY_TYPE_NAME, meta.getTypeName());
			metaNode.setProperty(KEY_META_CARDINALITY, meta.isSingleValue()?
					VALUE_META_CARDINALITY_SINGLE : VALUE_META_CARDINALITY_MULTI);
			metaNode.setProperty(KEY_TIMESTAMP_PUBLISH, meta.getPublishTimestamp());
			metaNode.setProperty(KEY_TIMESTAMP_DELETE, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
			metaNode.setProperty(KEY_RAW_DATA, meta.getRawData());
			if(meta.isNotify()) {
				metaNode.addLabel(Neo4JTypeLabels.NOTIFY);
				metaNode.setProperty(KEY_TIMESTAMP_DELETE, meta.getPublishTimestamp());
			}
			m = new Neo4JMetadata(metaNode, this);

			for (String key : meta.getProperties()) {
				m.addProperty(key, meta.valueFor(key));
			}

			metaNode.setProperty(KEY_HASH, calcHash(meta));

			tx.success();
		}
		return m;
	}
	/**
	 * Create a link-node between two identifiers and return the created link-node.
	 */
	@Override
	public InternalLink connect(InternalIdentifier id1, InternalIdentifier id2) {
		log.trace("Connecting Identifier " +id1+ " and " +id2);
		Node linkNode = null;
		try (Transaction tx = beginTx()) {
			Node idGraph1 = ((Neo4JIdentifier) id1).getNode();
			Node idGraph2 = ((Neo4JIdentifier) id2).getNode();

			linkNode = mConnection.getConnection().createNode();
			linkNode.addLabel(Neo4JTypeLabels.LINK);

			idGraph1.createRelationshipTo(linkNode, LinkTypes.Link);
			idGraph2.createRelationshipTo(linkNode, LinkTypes.Link);
			tx.success();
		}

		return (getLink(linkNode.getId()));
	}

	public void remove(long id) {
		log.trace("Removing Node with the id " +id+ " from the database");
		Node n = getNodeById(id);

		if(n.hasLabel(Neo4JTypeLabels.METADATA)) {
			removeMetadata(n);
		} else if (n.hasLabel(Neo4JTypeLabels.IDENTIFIER)) {
			removeIdentifier(n);
		} else if (n.hasLabel(Neo4JTypeLabels.LINK)) {
			removeLink(n);
		} else {
			log.warn("Node has got no supported Label  - not removing this node");
		}
	}

	public void removeMetadata(Node n) {
		try (Transaction tx = beginTx()) {
			long deleteTimestamp = mConnection.getTimestampManager().getLastTimestamp();

			//Remove the Metadata node itself
			n.setProperty(KEY_TIMESTAMP_DELETE, deleteTimestamp);
			tx.success();
		}
	}

	public InternalMetadata updateMetadata(InternalMetadata oldM, InternalMetadata newM) {
		InternalMetadata newN = null;
		try (Transaction tx = beginTx()) {
			long deleteTimestamp = newM.getPublishTimestamp();

			Node oldNode = ((Neo4JMetadata) oldM).getNode();
			oldNode.setProperty(KEY_TIMESTAMP_DELETE, deleteTimestamp);
			newN = insert(newM);
			tx.success();
		}
		return newN;
	}

	public void removeIdentifier(Node n) {
		try (Transaction tx = beginTx()) {
			// Remove all the Metadata
			Iterator<Relationship> meta = n.getRelationships(LinkTypes.Meta)
					.iterator();
			while (meta.hasNext()) {
				Relationship r = meta.next();
				removeMetadata(r.getEndNode());
			}
			// Remove all the Links
			Iterator<Relationship> links = n.getRelationships(LinkTypes.Link)
					.iterator();
			while (links.hasNext()) {
				Relationship r = links.next();
				removeLink(r.getEndNode());
			}
			// Remove all the Creation relationships
			//			Iterator<Relationship> creations = n.getRelationships(LinkTypes.Creation)
			//					.iterator();
			//			while (creations.hasNext()) {
			//				Relationship r = creations.next();
			//				r.delete();
			//			}
			//Remove the Identifier node itself
			n.delete();
			tx.success();
		}
	}

	public void removeLink(Node n) {
		try (Transaction tx = beginTx()) {
			//Remove all the Metadata
			Iterator<Relationship> meta = n.getRelationships(LinkTypes.Meta).iterator();
			while (meta.hasNext()) {
				Relationship r = meta.next();
				removeMetadata(r.getEndNode());
			}
			//Remove all the Link relationships
			Iterator<Relationship> links = n.getRelationships(LinkTypes.Link)
					.iterator();
			while (links.hasNext()) {
				Relationship r = links.next();
				r.delete();
			}
			//Remove the Link node itself
			n.delete();
			tx.success();
		}
	}

	@Override
	public void connectMeta(InternalLink link, InternalMetadata meta) {
		log.trace("Adding Metadata " +meta+ "to Link " +link);
		try (Transaction tx = beginTx()) {
			Node ln = ((Neo4JLink) link).getNode();
			Node mn = ((Neo4JMetadata) meta).getNode();

			ln.createRelationshipTo(mn, LinkTypes.Meta);
			tx.success();
		}
	}

	@Override
	public void connectMeta(InternalIdentifier id, InternalMetadata meta) {
		log.trace("Adding Metadata " +meta+ "to Identifier " +id);
		Node ln = ((Neo4JIdentifier) id).getNode();
		Node mn = ((Neo4JMetadata) meta).getNode();

		try (Transaction tx = beginTx()) {
			ln.createRelationshipTo(mn, LinkTypes.Meta);
			tx.success();
		}
	}

	public Transaction beginTx() {
		return mConnection.getConnection().beginTx();
	}

	@Override
	public InternalIdentifier getStartIdentifier() {
		log.trace("Fetching (random) root Identifier from the database");
		InternalIdentifier result = null;
		try (Transaction tx = beginTx()) {
			Iterator<Node> allNodes = GlobalGraphOperations.at(mConnection.getConnection()).getAllNodesWithLabel(Neo4JTypeLabels.IDENTIFIER).iterator();
			if(allNodes.hasNext()) {
				result =  new Neo4JIdentifier(allNodes.next(), this);
			}
			else {
				log.warn("Seems there is no Identifier in the database that could be used as a root");
				result =  null;
			}
			tx.success();
		}
		return result;
	}

	public List<InternalIdentifier> getAllIdentifier(){
		log.debug("Getting all the Identifier from the database");
		try (Transaction tx = beginTx()) {
			ArrayList<InternalIdentifier> allId = new ArrayList<>();

			Iterator<Node> allNodes = GlobalGraphOperations.at(mConnection.getConnection()).getAllNodesWithLabel(Neo4JTypeLabels.IDENTIFIER).iterator();
			while(allNodes.hasNext()) {
				allId.add(new Neo4JIdentifier(allNodes.next(), this));
			}

			tx.success();
			return allId;
		}
	}

	@Override
	public void disconnect(InternalIdentifier id1, InternalIdentifier id2) {
		log.trace("Disconnecting Identifier " +id1+ " and Identifier " +id2);
		InternalLink l = findCommonLink(id1, id2);
		remove(((Neo4JLink) l).getNode().getId());
	}

}
