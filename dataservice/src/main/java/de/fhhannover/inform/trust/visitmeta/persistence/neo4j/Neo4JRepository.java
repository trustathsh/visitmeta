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

import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_HASH;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_META_CARDINALITY;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TIMESTAMP_DELETE;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TIMESTAMP_PUBLISH;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TYPE_NAME;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.NODE_TYPE_KEY;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_META_CARDINALITY_MULTI;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_META_CARDINALITY_SINGLE;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_TYPE_NAME_IDENTIFIER;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_TYPE_NAME_LINK;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_TYPE_NAME_METADATA;
import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_RAW_DATA;

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

import scala.collection.mutable.ArrayBuilder.ofBoolean;

import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.fhhannover.inform.trust.visitmeta.interfaces.Propable;
import de.fhhannover.inform.trust.visitmeta.persistence.Repository;

public class Neo4JRepository implements Repository {

	private static final Logger log = Logger.getLogger(Neo4JRepository.class);
	private Neo4JConnection mConnection;
	private MessageDigest mMessageDigest;

	private Neo4JRepository() {
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
			return (InternalIdentifier) neoId;
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
		Transaction tx = beginTx();

		try {
			Node n = mConnection.getConnection().createNode();
			n.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_IDENTIFIER);
			n.setProperty(KEY_TYPE_NAME, id.getTypeName());
			n.setProperty(KEY_HASH, calcHash(id));
			n.setProperty(KEY_RAW_DATA, id.getRawData());

			mConnection.getConnection().
				getReferenceNode().createRelationshipTo(n, LinkTypes.Creation);

			Iterator<String> i = id.getProperties().iterator();
			String keyProp;
			while (i.hasNext()) {
				keyProp = i.next();
				n.setProperty(keyProp, id.valueFor(keyProp));
			}
			neoI = new Neo4JIdentifier(n, this);
			tx.success();
		} finally {
			tx.finish();
		}

		return neoI;
	}

	@Override
	public InternalMetadata insert(InternalMetadata meta) {
		log.trace("Adding Metadata " +meta+ " to the database");
		Neo4JMetadata m = null;
		Transaction tx = beginTx();

		try {
			Node metaNode = mConnection.getConnection().createNode();
			metaNode.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_METADATA);
			metaNode.setProperty(KEY_TYPE_NAME, meta.getTypeName());
			metaNode.setProperty(KEY_META_CARDINALITY, meta.isSingleValue()?
					VALUE_META_CARDINALITY_SINGLE : VALUE_META_CARDINALITY_MULTI);
			metaNode.setProperty(KEY_TIMESTAMP_PUBLISH, meta.getPublishTimestamp());
			metaNode.setProperty(KEY_TIMESTAMP_DELETE, InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP);
			metaNode.setProperty(KEY_RAW_DATA, meta.getRawData());
			m = new Neo4JMetadata(metaNode, this);

			for (String key : meta.getProperties()) {
				m.addProperty(key, meta.valueFor(key));
			}

			metaNode.setProperty(KEY_HASH, calcHash(meta));

			tx.success();
		} finally {
			tx.finish();
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
		Transaction tx = beginTx();
		try {

			Node idGraph1 = ((Neo4JIdentifier) id1).getNode();
			Node idGraph2 = ((Neo4JIdentifier) id2).getNode();

			linkNode = mConnection.getConnection().createNode();
			linkNode.setProperty(NODE_TYPE_KEY, VALUE_TYPE_NAME_LINK);

			idGraph1.createRelationshipTo(linkNode, LinkTypes.Link);
			idGraph2.createRelationshipTo(linkNode, LinkTypes.Link);
			tx.success();
		} finally {
			tx.finish();
		}

		return (getLink(linkNode.getId()));
	}

	public void remove(long id) {
		log.trace("Removing Node with the id " +id+ " from the database");
		Node n = getNodeById(id);
		String type = (String) n.getProperty(NODE_TYPE_KEY);

		switch (type) {
		case VALUE_TYPE_NAME_METADATA:
			removeMetadata(n);
			break;
		case VALUE_TYPE_NAME_IDENTIFIER:
			removeIdentifier(n);
			break;
		case VALUE_TYPE_NAME_LINK:
			removeLink(n);
			break;
		default:
			log.warn("Unexpected Node type (" + type + ")  - not removing this node");
			break;
		}
	}

	public void removeMetadata(Node n) {
		Transaction tx = beginTx();
		try {
			long deleteTimestamp = mConnection.getTimestampManager().getLastTimestamp();

			//Remove the Metadata node itself
			n.setProperty(KEY_TIMESTAMP_DELETE, deleteTimestamp);
			tx.success();
		} finally {
			tx.finish();
		}
	}

	public void removeIdentifier(Node n) {
		Transaction tx = beginTx();
		try {
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
			Iterator<Relationship> creations = n.getRelationships(LinkTypes.Creation)
					.iterator();
			while (creations.hasNext()) {
				Relationship r = creations.next();
				r.delete();
			}
			//Remove the Identifier node itself
			n.delete();
			tx.success();
		} finally {
			tx.finish();
		}
	}

	public void removeLink(Node n) {
		Transaction tx = beginTx();
		try {
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
		} finally {
			tx.finish();
		}
	}

	@Override
	public void connectMeta(InternalLink link, InternalMetadata meta) {
		log.trace("Adding Metadata " +meta+ "to Link " +link);
		Transaction tx = beginTx();

		try {
			Node ln = ((Neo4JLink) link).getNode();
			Node mn = ((Neo4JMetadata) meta).getNode();

			ln.createRelationshipTo(mn, LinkTypes.Meta);
			tx.success();
		} finally {
			tx.finish();
		}
	}

	@Override
	public void connectMeta(InternalIdentifier id, InternalMetadata meta) {
		log.trace("Adding Metadata " +meta+ "to Identifier " +id);
		Node ln = ((Neo4JIdentifier) id).getNode();
		Node mn = ((Neo4JMetadata) meta).getNode();

		Transaction tx = beginTx();

		try {
			ln.createRelationshipTo(mn, LinkTypes.Meta);
			tx.success();
		} finally {
			tx.finish();
		}
	}

	public Transaction beginTx() {
		return mConnection.getConnection().beginTx();
	}

	@Override
	public InternalIdentifier getStartIdentifier() {
		log.trace("Fetching (random) root Identifier from the database");
		Node ref = mConnection.getConnection().getReferenceNode();
		for (Relationship r : ref.getRelationships(LinkTypes.Creation)) {
			return new Neo4JIdentifier(r.getEndNode(), this);
		}
		log.warn("Seems there is no Identifier in the database that could be used as a root");
		return null;
	}

	public List<InternalIdentifier> getAllIdentifier(){
		log.debug("Getting all the Identifier from the database");
		ArrayList<InternalIdentifier> allId = new ArrayList<>();

		for (Relationship r : getRoot().getRelationships(LinkTypes.Creation)) {
			allId.add(new Neo4JIdentifier(r.getEndNode(), this));
		}

		return allId;
	}

	protected Node getRoot() {
		return mConnection.getConnection().getReferenceNode();
	}

	@Override
	public void disconnect(InternalIdentifier id1, InternalIdentifier id2) {
		log.trace("Disconnecting Identifier " +id1+ " and Identifier " +id2);
		InternalLink l = findCommonLink(id1, id2);
		remove(((Neo4JLink) l).getNode().getId());
	}
}
