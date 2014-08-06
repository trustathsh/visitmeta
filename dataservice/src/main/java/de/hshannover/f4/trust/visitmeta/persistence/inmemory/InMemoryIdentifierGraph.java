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
package de.hshannover.f4.trust.visitmeta.persistence.inmemory;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import scala.actors.threadpool.Arrays;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifierGraph;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.persistence.Repository;


/**
 * Implementation of IdentifierGraph that holds only one startidentifier in
 * direct access -> getIdentifiers is O(n)!
 *
 * @author rosso
 *
 */
public class InMemoryIdentifierGraph implements InternalIdentifierGraph, Repository {
	private long mTimestamp;
	private static Logger log = Logger.getLogger(InMemoryIdentifierGraph.class);
	private Map<Integer, InMemoryIdentifier> mIdentifiers;

	private InMemoryIdentifierGraph() {
		mIdentifiers = new HashMap<>();
	}

	public InMemoryIdentifierGraph(long timestamp) {
		this();
		mTimestamp = timestamp;
	}

	@Override
	public InternalIdentifier getStartIdentifier() {
		if (mIdentifiers.size() > 0)
			return (InternalIdentifier) mIdentifiers.values().toArray()[0];
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InternalIdentifier> getAllIdentifier(){
		return Arrays.asList(mIdentifiers.values().toArray());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InternalIdentifier> getIdentifiers() {
		return Arrays.asList(mIdentifiers.values().toArray());
	}

	@Override
	public long getTimestamp() {
		return mTimestamp;
	}

	@Override
	public InternalIdentifier findIdentifier(InternalIdentifier id) {
		InMemoryIdentifier idM = mIdentifiers.get(id.hashCode());
		if (idM == null) {
			log.trace("Failed to find identifier in graph");
		}
		return idM;
	}

	@Override
	public InternalIdentifier insert(InternalIdentifier id) {
		if (id == null)
			throw new RuntimeException("Cant insert an Identifier that is null");
		InMemoryIdentifier fresh = new InMemoryIdentifier(id);
		mIdentifiers.put(fresh.hashCode(), fresh);
		return fresh;
	}

	@Override
	public InternalLink findCommonLink(InternalIdentifier id1, InternalIdentifier id2) {
		InternalIdentifier idGraph1 = findIdentifier(id1);
		for (InternalLink l : idGraph1.getLinks()) {
			if (l.getIdentifiers().getFirst().equals(id2)) {
				return (InternalLink) l;
			} else if (l.getIdentifiers().getSecond().equals(id2)) {
				return (InternalLink) l;
			}
		}
		return null;
	}

	@Override
	public InternalLink getLink(long id) {
		throw new UnsupportedOperationException("This is not the method you are looking for");
	}

	@Override
	public InternalMetadata getMetadata(long id) {
		throw new UnsupportedOperationException("This is not the method you are looking for");
	}

	@Override
	public InternalIdentifier getIdentifier(long id) {
		throw new UnsupportedOperationException("This is not the method you are looking for");
	}

	@Override
	public InternalMetadata insert(InternalMetadata meta) {
		return new InMemoryMetadata(meta);
	}

	@Override
	public InternalLink connect(InternalIdentifier id1,
			InternalIdentifier id2) {
		InMemoryIdentifier idGraph1 = (InMemoryIdentifier) id1;
		InMemoryIdentifier idGraph2 = (InMemoryIdentifier) id2;

		if (mIdentifiers.containsKey(id1.hashCode()) && mIdentifiers.containsKey(id2.hashCode())) {

		if (idGraph1 == null || idGraph2 == null)
			return null;
		for (InternalLink l : idGraph1.getLinks()) {
			if (l.getIdentifiers().getFirst() == idGraph2)
				return (InternalLink) l;
			else if (l.getIdentifiers().getSecond() == idGraph2)
				return (InternalLink) l;
		}
		InMemoryLink l = new InMemoryLink(idGraph1, idGraph2);
		idGraph1.addLink(l);
		idGraph2.addLink(l);
		return l;
		}
		log.error("Someone is trying to connect Identifiers that are not in the graph");
		return null;
	}

	@Override
	public void connectMeta(InternalLink link, InternalMetadata meta) {
		if (link == null || meta == null)
			return;
		link.addMetadata(meta);
	}

	@Override
	public void connectMeta(InternalIdentifier id, InternalMetadata meta) {
		if (id == null || meta == null)
			return;
		id.addMetadata(meta);
	}

	@Override
	public void disconnect(InternalIdentifier id1, InternalIdentifier id2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeIdentifier(InternalIdentifier i) {
		i.clearLinks();
		i.clearMetadata();
		this.mIdentifiers.remove(i.hashCode());
		}

	@Override
	public void setTimestamp(long timestamp) {
		mTimestamp = timestamp;
	}
}
