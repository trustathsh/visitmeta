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
 * This file is part of visitmeta dataservice, version 0.0.3,
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
package de.fhhannover.inform.trust.visitmeta.persistence.neo4j;




import static de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifierPair;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;

public class Neo4JLink extends InternalLink {

	private Neo4JRepository mRepo;
	private Node mMe;
/**
 * Create a link node in the database.
 * @param n The node in memory.
 * @param graph The graph where to create this node.
 */
	public Neo4JLink(Node n, Neo4JRepository graph) {
		super();
		if (!n.getProperty(NODE_TYPE_KEY).equals(VALUE_TYPE_NAME_LINK)) {
			String msg = "Trying to construct a Link with a node of type "
					+n.getProperty(NODE_TYPE_KEY)+
					". We clearly disapprove and will die ungracefully now";
					throw new RuntimeException(msg);
		}
		mMe = n;
		mRepo = graph;
	}

	@Override
	public InternalIdentifierPair getIdentifiers() {
		Iterator<Relationship> it = mMe.getRelationships(LinkTypes.Link).iterator();

		InternalIdentifier id1 = mRepo.getIdentifier(it.next().getStartNode().getId());
		InternalIdentifier id2 = mRepo.getIdentifier(it.next().getStartNode().getId());

		if (id1.hashCode() > id2.hashCode())
			return new InternalIdentifierPair(id1, id2);
		else
			return new InternalIdentifierPair(id2, id1);
	}

	public Node getNode() {
		return mMe;
	}

	@Override
	public List<InternalMetadata> getMetadata() {
		Iterator<Relationship> i = 	mMe.getRelationships(LinkTypes.Meta).iterator();
		List<InternalMetadata> lm = new ArrayList<>();

		while(i.hasNext()){
			Relationship r = i.next();
			InternalMetadata metadata = mRepo.getMetadata(r.getEndNode().getId());
			lm.add(metadata);
		}
		return lm;
	}

	public String getHash(){
		return (String)mMe.getProperty(KEY_HASH);
	}

	@Override
	public void addMetadata(InternalMetadata m) {
		Neo4JMetadata metadata = (Neo4JMetadata) mRepo.insert(m);
		mRepo.connectMeta(this, metadata);
	}

	@Override
	public void clearMetadata() {
		for (Relationship r : mMe.getRelationships(LinkTypes.Meta)) {
			mRepo.remove(r.getEndNode().getId());
		}
	}

	@Override
	public void removeMetadata(InternalMetadata meta) {
		for (Relationship r : mMe.getRelationships(LinkTypes.Meta)) {
			Neo4JMetadata neo4jMetadata = (Neo4JMetadata) mRepo.getMetadata(r.getEndNode().getId());
			if (meta.equals(neo4jMetadata)) {
				mRepo.remove(neo4jMetadata.getNode().getId());
				break;
			}
		}
	}
/**
 * Check if this link has metadata connected to it.
 */
	@Override
	public boolean hasMetadata(InternalMetadata meta) {
		for (Relationship r : mMe.getRelationships(LinkTypes.Meta)) {
			Neo4JMetadata neo4jMetadata = (Neo4JMetadata) mRepo.getMetadata(r.getEndNode().getId());
			if (meta.equals(neo4jMetadata)) {
				return true;
			}
		}
		return false;
	}

}
