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
package de.hshannover.f4.trust.visitmeta.persistence;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifierGraph;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifierPair;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.persistence.inmemory.InMemoryIdentifierGraph;

public abstract class AbstractReader implements Reader {

	private static final Logger log = Logger.getLogger(AbstractReader.class);

	protected Repository mRepo;

	@Override
	public List<InternalIdentifierGraph> getCurrentState() {
		log.debug("reading current graph state from repository ...");
		return getGraphAt(Long.MAX_VALUE);
	}

	/**
	 * @param available
	 * @param start
	 * @param timestamp
	 * @return
	 */
	private InternalIdentifierGraph buildGraph(
			List<InternalIdentifier> available,
			InternalIdentifier start,
			long timestamp
			) {

		InMemoryIdentifierGraph graph = new InMemoryIdentifierGraph(timestamp);
		HashSet<InternalIdentifier> seen = new HashSet<>();
		detachIdentifierSingleGraph(start, seen, graph, timestamp);
		available.removeAll(seen);
		return graph;
	}

	/**
	 * Detach the identifier <tt>current</tt> and recursively all its neighbors
	 * from the neo4j database graph. The link between two identifiers is created
	 * by a backtracking-like approach:
	 * <ul>
	 *   <li>first detach the current identifier and store it into the graph</li>
	 *   <li>iterate over all links of the current identifier</li>
	 *   <ul>
	 *     <li>if the other identifier on a link is already in the graph, detach/create
	 *         the link between the current and the other identifier</li>
	 *     <li>if the other identifier is not in the graph, start the recursion for the
	 *         other identifier</li>
	 *   </ul>
	 * </ul>
	 *
	 * @param current
	 * @param seen
	 * @param graph
	 * @param timestamp
	 */
	private void detachIdentifierSingleGraph(
			InternalIdentifier current,
			HashSet<InternalIdentifier> seen,
			InMemoryIdentifierGraph graph,
			long timestamp
			) {

		InternalIdentifier detachedCurrent = null;
		if (!seen.contains(current)) {
			if (current.isValidAt(timestamp)) {
				detachedCurrent = graph.insert(current);
				detachIdentifierMetadata(current, detachedCurrent, graph, timestamp);

				seen.add(current);

				for (InternalLink l : current.getLinks()) {
					if (l.isValidAt(timestamp)) {
						InternalIdentifierPair pair = l.getIdentifiers();
						InternalIdentifier other = (pair.getFirst().equals(current)) ? pair.getSecond() : pair.getFirst();

						if (seen.contains(other)) {
							if (other.isValidAt(timestamp)) {
								InternalIdentifier detachedOther = graph.findIdentifier(other);
								InternalLink link = graph.connect(detachedCurrent, detachedOther);
								detachLinkMetadata(l, link, graph, timestamp);
							}
						} else {
							detachIdentifierSingleGraph(other, seen, graph, timestamp);
						}
					}
				}
			}
		}
	}

	/**
	 * Detaches the Metadata of Identifier <tt>from</tt> by copying it to Identifier <tt>to</tt>.
	 * @param from
	 * @param to
	 * @param graph
	 * @param timestamp
	 */
	private void detachIdentifierMetadata(
			InternalIdentifier from,
			InternalIdentifier to,
			InMemoryIdentifierGraph graph,
			long timestamp
			) {

		for (InternalMetadata m : from.getMetadata()) {
			if (m.isValidAt(timestamp)) {
				InternalMetadata loadedMeta = graph.insert(m);
				graph.connectMeta(to, loadedMeta);
			}
		}
	}

	/**
	 * Detaches the Metadata of Link <tt>from</tt> by copying it to Link <tt>to</tt>
	 * @param from
	 * @param to
	 * @param graph
	 * @param timestamp
	 */
	private void detachLinkMetadata(
			InternalLink from,
			InternalLink to,
			InMemoryIdentifierGraph graph,
			long timestamp
			) {

		for (InternalMetadata m : from.getMetadata()) {
			if (m.isValidAt(timestamp)) {
				InternalMetadata loadedMeta = graph.insert(m);
				graph.connectMeta(to, loadedMeta);
			}
		}
	}

	@Override
	public List<InternalIdentifierGraph> getGraphAt(long timestamp) {
		log.debug("Get the graph at "+timestamp);
		ArrayList<InternalIdentifier> validIds = new ArrayList<>();
		ArrayList<InternalIdentifierGraph> graph = new ArrayList<>();

		for (InternalIdentifier current : mRepo.getAllIdentifier()) {
			if(current.isValidAt(timestamp)){
				validIds.add(current);
			}
		}

		if (validIds.isEmpty()) {
			graph.add(new InMemoryIdentifierGraph(timestamp));
			return graph;
		}
		while (!validIds.isEmpty()) {
			InternalIdentifier start = validIds.get(0);
			graph.add(buildGraph(validIds, start, timestamp));
		}
		return graph;
	}
	
	@Override
	public List<InternalIdentifierGraph> getNotifiesAt(long timestamp) {
		log.debug("Get the notifies at "+timestamp);
		ArrayList<InternalIdentifierGraph> notifies = new ArrayList<>();
		for(InternalIdentifier current : mRepo.getAllIdentifier()) {
			for(InternalMetadata meta : current.getMetadata()) {
				if(meta.isNotify() && meta.getPublishTimestamp() == timestamp) {
					InMemoryIdentifierGraph temp = new InMemoryIdentifierGraph(timestamp);
					InternalIdentifier first = temp.insert(current);
					InternalMetadata newMeta = temp.insert(meta);
					temp.connectMeta(first, newMeta);
					notifies.add(temp);
				}
			}
			
			for(InternalLink link : current.getLinks()) {
				if(checkIfListContainsLink(notifies, link)) {
					continue;
				}
				for(InternalMetadata meta : link.getMetadata()) {
					if(meta.isNotify() && meta.getPublishTimestamp() == timestamp) {
						InMemoryIdentifierGraph temp = new InMemoryIdentifierGraph(timestamp);
						InternalIdentifierPair ids = link.getIdentifiers();
						InternalIdentifier first = temp.insert(ids.getFirst());
						InternalIdentifier second = temp.insert(ids.getSecond());
						InternalMetadata newMeta = temp.insert(meta);
						InternalLink newLink = temp.connect(first, second);
						temp.connectMeta(newLink, newMeta);
						notifies.add(temp);
					}
				}
			}
		}
		
		return notifies;
	}
	
	private boolean checkIfListContainsLink(List<InternalIdentifierGraph> list, InternalLink link) {
		for(InternalIdentifierGraph graph : list) {
			for(InternalIdentifier current : graph.getIdentifiers()) {
				for(InternalLink tmpLink : current.getLinks())
					if(link.equals(tmpLink)) {
						return true;
					}
			}
		}
		return false;
	}

	@Override
	public abstract long getTimeOfLastUpdate();

}