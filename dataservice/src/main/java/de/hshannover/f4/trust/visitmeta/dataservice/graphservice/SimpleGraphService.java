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
package de.hshannover.f4.trust.visitmeta.dataservice.graphservice;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.SortedMap;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.DeltaImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifierGraph;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifierPair;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.dataservice.util.GraphHelper;
import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphFilter;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphType;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;
import de.hshannover.f4.trust.visitmeta.persistence.Executor;
import de.hshannover.f4.trust.visitmeta.persistence.Reader;
import de.hshannover.f4.trust.visitmeta.persistence.inmemory.InMemoryIdentifierGraph;

public class SimpleGraphService implements GraphService {

	private static final Logger log = Logger
			.getLogger(SimpleGraphService.class);

	private Reader mReader;

	private GraphCache mCache;

	private Executor mExecutor;

	private SimpleGraphService() {
		log.trace("new SimpleGraphService");
	}

	public SimpleGraphService(Reader r, Executor e, GraphCache cache) {
		this();
		mReader = r;
		mCache = cache;
		mExecutor = e;
	}

	@Override
	public List<IdentifierGraph> getInitialGraph() {
		log.trace("Method getInitialGraph() called.");
		List<IdentifierGraph> graph = new ArrayList<>();
		if (!getChangesMap().isEmpty()) {
			for (InternalIdentifierGraph binky : getInternalGraphAt(getChangesMap().firstKey())) {
				graph.add(GraphHelper.internalToExternalGraph(binky));
			}
		}
		return graph;
	}

	@Override
	public List<IdentifierGraph> getInitialGraph(GraphFilter filter) {
		log.trace("Method getInitialGraph(" + filter + ") called.");
		List<IdentifierGraph> graph = new ArrayList<>();
		if (!getChangesMap().isEmpty()) {
			graph.add(filterGraph(getInternalGraphAt(getChangesMap().firstKey()), filter));
		}
		return graph;
	}

	@Override
	public List<IdentifierGraph> getCurrentGraph() {
		log.trace("Method getCurrentGraph() called.");
		if (getChangesMap().isEmpty()) {
			return new ArrayList<>();
		} else {
			return getGraphAt(getChangesMap().lastKey());
		}
	}

	@Override
	public List<IdentifierGraph> getCurrentGraph(GraphFilter filter) {
		log.trace("Method getCurrentGraph(" + filter + ") called.");
		if (getChangesMap().isEmpty()) {
			return new ArrayList<>();
		}
		List<InternalIdentifierGraph> binky = getInternalGraphAt(getChangesMap().lastKey());
		List<IdentifierGraph> minky = new ArrayList<>();
		minky.add(filterGraph(binky, filter));
		return minky;
	}

	private List<InternalIdentifierGraph> getInternalGraphAt(Long timestamp) {
		SortedMap<Long, Long> changes = getChangesMap();
		long closestTimestamp = 0l;
		if (!changes.isEmpty()) {
			if (changes.containsKey(timestamp)) {
				closestTimestamp = timestamp;
			} else {
				SortedMap<Long, Long> head = changes.headMap(timestamp);
				if (!head.isEmpty()) {
					closestTimestamp = head.lastKey();
				} else {
					return new ArrayList<InternalIdentifierGraph>();
				}
			}
			if (mCache.lookup(closestTimestamp)) {
				return mCache.fetch(closestTimestamp);
			} else {
				List<InternalIdentifierGraph> graphAt = mReader.getGraphAt(closestTimestamp);
				mCache.put(closestTimestamp, graphAt);
				ArrayList<InternalIdentifierGraph> result = new ArrayList<>();
				for (InternalIdentifierGraph g : graphAt) {
					result.add(GraphHelper.cloneGraph(g));
				}
				return result;
			}
		}
		return new ArrayList<InternalIdentifierGraph>();

	}
	
	@Override
	public List<IdentifierGraph> getNotifiesAt(long timestamp) {
		log.trace("Method getNotifiesAt(" + timestamp + ") called.");
		List<IdentifierGraph> graph = new ArrayList<>();
		for(InternalIdentifierGraph internalGraph : mReader.getNotifiesAt(timestamp)) {
			graph.add(GraphHelper.internalToExternalGraph(internalGraph));
		}
		return graph;
	}

	@Override
	public List<IdentifierGraph> getGraphAt(long timestamp) {
		log.trace("Method getGraphAt(" + timestamp + ") called.");
		List<IdentifierGraph> graph = new ArrayList<>();
		for (InternalIdentifierGraph internalGraph : getInternalGraphAt(timestamp)) {
			if (internalGraph.getStartIdentifier() != null) {
				graph.add(GraphHelper.internalToExternalGraph(internalGraph));
			}
		}
		return graph;
	}

	@Override
	public List<IdentifierGraph> getGraphAt(long timestamp, GraphFilter filter) {
		log.trace("Method getGraphAt(" + timestamp + ", " + filter + ") called.");
		List<IdentifierGraph> graph = new ArrayList<>();
		if (!getChangesMap().isEmpty()) {
			// FIXME check if graph at timestamp is empty
			graph.add(filterGraph(getInternalGraphAt(timestamp), filter));
		}
		return graph;
	}

	@Override
	public Delta getDelta(long t1, long t2) {
		log.trace("Method getDelta(" + t1 + "," + t2 + ") called.");
		if (t1 > t2) {
			return getDelta(t2, t1);
		}
		log.trace("preparing updates ...");
		List<IdentifierGraph> updates = createUpdateDelta(t1, t2);
		log.trace("preparing deletes ...");
		List<IdentifierGraph> deletes = createDeleteDelta(t1, t2);

		return new DeltaImpl(deletes, updates);
	}

	// delete = old - new
	private List<IdentifierGraph> createDeleteDelta(long from, long to) {
		List<InternalIdentifierGraph> newGraphs = getInternalGraphAt(to);
		List<InternalIdentifierGraph> oldGraphs = getInternalGraphAt(from);

		List<InternalIdentifier> allNewIdentifier = mergeToList(newGraphs);
		for (InternalIdentifierGraph oldGraph : oldGraphs) {
			removeIdentifiersFromGraph(allNewIdentifier, oldGraph);
		}

		List<IdentifierGraph> deleteResults = new ArrayList<>();
		for (InternalIdentifierGraph g : oldGraphs) {
			g.setTimestamp(to);
			if (!g.getIdentifiers().isEmpty()) {
				deleteResults.add(GraphHelper.internalToExternalGraph(g));
			}
		}

		return deleteResults;
	}

	// update = new - old
	private List<IdentifierGraph> createUpdateDelta(long from, long to) {
		List<InternalIdentifierGraph> newGraphs = getInternalGraphAt(to);
		List<InternalIdentifierGraph> oldGraphs = getInternalGraphAt(from);

		/**
		 * Warning!
		 * This may create separated subgraphs, which is against the overall assumption
		 * that IdentifierGraphs always contain a complete, non-separated graph.
		 */
		List<InternalIdentifier> allOldIdentifier = mergeToList(oldGraphs);
		for (InternalIdentifierGraph newGraph : newGraphs) {
			removeIdentifiersFromGraph(allOldIdentifier, newGraph);
		}

		/**
		 * This expands every possible subgraph to a single InteralIdentifierGraph,
		 * so that the assumption is fullfilled "again".
		 */
		List<InternalIdentifierGraph> expandedNewGraph = new ArrayList<InternalIdentifierGraph>();
		for (InternalIdentifierGraph g : newGraphs) {
			expandedNewGraph.addAll(expandGraph(g, to));
		}

		List<IdentifierGraph> updateResults = new ArrayList<>();
		for (InternalIdentifierGraph g : expandedNewGraph) {
			if (!g.getIdentifiers().isEmpty()) {
				updateResults.add(GraphHelper.internalToExternalGraph(g));
			}
		}

		return updateResults;
	}

	private List<InternalIdentifierGraph> expandGraph(InternalIdentifierGraph graph, long timestamp) {
		ArrayList<InternalIdentifierGraph> result = new ArrayList<InternalIdentifierGraph>();
		HashSet<InternalIdentifier> seen = new HashSet<InternalIdentifier>();
		while (seen.size() != graph.getIdentifiers().size()) {
			InternalIdentifier id = null;
			for (InternalIdentifier i : graph.getIdentifiers()) {
				id = i;
				if (!seen.contains(id)) {
					break;
				}
			}
			HashSet<InternalIdentifier> idsToVisit = new HashSet<InternalIdentifier>();
			InMemoryIdentifierGraph subgraph = new InMemoryIdentifierGraph(timestamp);
			idsToVisit.add(id);
			expandNode(idsToVisit, seen, subgraph);
			result.add(subgraph);
		}
		return result;
	}

	private void expandNode(HashSet<InternalIdentifier> idsToVisit, HashSet<InternalIdentifier> seen,
			InMemoryIdentifierGraph subgraph) {
		if (idsToVisit.size() > 0) {
			InternalIdentifier id = idsToVisit.iterator().next();
			InternalIdentifier insertedId = subgraph.findIdentifier(id);
			if (insertedId == null) {
				insertedId = subgraph.insert(id);

				for (InternalMetadata m : id.getMetadata()) {
					subgraph.connectMeta(insertedId, subgraph.insert(m));
				}
			}
			seen.add(id);
			for (InternalLink l : id.getLinks()) {
				InternalIdentifier other = l.getIdentifiers().getFirst().equals(id)
						? l.getIdentifiers().getSecond() : l.getIdentifiers().getFirst();
						if (!seen.contains(other)) {
							InternalIdentifier insertedOther = subgraph.findIdentifier(other);
							if (insertedOther == null) {
								insertedOther = subgraph.insert(other);

								for (InternalMetadata m : other.getMetadata()) {
									subgraph.connectMeta(insertedOther, subgraph.insert(m));
								}
							}
							InternalLink insertedLink = subgraph.connect(insertedId, insertedOther);
							for (InternalMetadata m : l.getMetadata()) {
								subgraph.connectMeta(insertedLink, subgraph.insert(m));
							}
							idsToVisit.add(other);
						}
			}
			idsToVisit.remove(id);
			expandNode(idsToVisit, seen, subgraph);
		}
	}

	private void removeIdentifiersFromGraph(
			List<InternalIdentifier> identifierToRemove,
			InternalIdentifierGraph graph) {
		for (InternalIdentifier graphIdentifier : graph.getIdentifiers()) {
			log.trace("looking for '" + graphIdentifier
					+ "' in identifiersToRemove");
			int indexOfOldEquivalent = identifierToRemove
					.indexOf(graphIdentifier);
			if (indexOfOldEquivalent != -1) {
				log.trace("found '" + graphIdentifier + "'");
				InternalIdentifier oldEquivalent = identifierToRemove
						.get(indexOfOldEquivalent);
				stripMetadataFromIdentifier(graphIdentifier,
						oldEquivalent.getMetadata());
				disconnectLinksFromIdentifier(graphIdentifier,
						oldEquivalent.getLinks());

				if (graphIdentifier.getMetadata().isEmpty()
						&& graphIdentifier.getLinks().isEmpty()) {
					log.trace("removing '" + graphIdentifier + "' from graph");
					graph.removeIdentifier(graphIdentifier);
				}
			}
		}

	}

	private void stripMetadataFromIdentifier(InternalIdentifier id,
			List<InternalMetadata> metadata) {
		for (InternalMetadata meta : metadata) {
			id.removeMetadata(meta, false);
		}
	}

	private void stripMetadataFromLink(InternalLink link,
			List<InternalMetadata> metadata) {
		for (InternalMetadata meta : metadata) {
			link.removeMetadata(meta, false);
		}
	}

	private void disconnectLinksFromIdentifier(InternalIdentifier id,
			List<InternalLink> links) {
		List<InternalLink> linksToDelete = new ArrayList<>();
		for (InternalLink l : links) {
			InternalIdentifierPair pair = l.getIdentifiers();
			InternalIdentifier opposite = (pair.getFirst().equals(id)) ? pair
					.getSecond() : pair.getFirst();
					for (InternalLink deleteCandidate : id.getLinks()) {
						InternalIdentifierPair candidatePair = deleteCandidate
								.getIdentifiers();
						if ((candidatePair.getFirst().equals(opposite) && candidatePair
								.getSecond().equals(id))
								|| (candidatePair.getFirst().equals(id) && candidatePair
										.getSecond().equals(opposite))) {
							stripMetadataFromLink(deleteCandidate, l.getMetadata());
							if (deleteCandidate.getMetadata().isEmpty()) {
								linksToDelete.add(l);
							}
						}
					}
		}
		for (InternalLink l : linksToDelete) {
			id.removeLink(l);
		}
	}

	private List<InternalIdentifier> mergeToList(
			List<InternalIdentifierGraph> graphs) {
		List<InternalIdentifier> ids = new ArrayList<>();
		for (InternalIdentifierGraph g : graphs) {
			ids.addAll(g.getIdentifiers());
		}
		return ids;
	}


	@Override
	public SortedMap<Long, Long> getChangesMap() {
		log.trace("Method getChangesMap() called.");
		return mReader.getChangesMap();
	}


	public IdentifierGraph filterGraph(List<InternalIdentifierGraph> graphList,
			GraphFilter filter) {
		Identifier startId = filter.getStartIdentifier();
		InternalIdentifier internalStartId = null;
		InternalIdentifierGraph graph = null;
		for(InternalIdentifierGraph g : graphList) {
			for (InternalIdentifier id : g.getIdentifiers()) {
				if (id.sameAs(startId)) {
					graph = g;
					internalStartId = id;
				}
			}
		}
		List<String> matchLinks = filter.getMatchLinks();
		List<String> resultFilter = filter.getResultFilter();
		int maxDepth = filter.getMaxDepth();
		// String terminalIndentifierType = filter.get
		InMemoryIdentifierGraph result = new InMemoryIdentifierGraph(
				graph.getTimestamp());
		searchRoutine(graph, internalStartId, result, 0, matchLinks, maxDepth);
		if(resultFilter != null) {
			filterResult(result, resultFilter);
		}
		return GraphHelper.internalToExternalGraph(result);
	}

	private void filterResult(InMemoryIdentifierGraph result, List<String> resultFilter) {
		if(resultFilter.isEmpty()) {
			for (InternalIdentifier id : result.getAllIdentifier()) {
				id.clearMetadata();
				for (InternalLink l : id.getLinks()) {
					l.clearMetadata();
				}
			}
		} else {
			for(InternalIdentifier id : result.getAllIdentifier()) {
				ArrayList<InternalMetadata> toRemove = new ArrayList<>();
				for(InternalMetadata meta : id.getMetadata()) {
					if(!resultFilter.contains(meta.getTypeName())) {
						toRemove.add(meta);
						id.removeMetadata(meta);
					}
				}
				for (InternalMetadata meta : toRemove) {
					id.removeMetadata(meta);
				}
				toRemove.clear();
				for(InternalLink link : id.getLinks()) {
					for (InternalMetadata linkMeta : link.getMetadata()) {
						if(!resultFilter.contains(linkMeta.getTypeName())) {
							toRemove.add(linkMeta);
						}
					}
					for (InternalMetadata meta : toRemove) {
						link.removeMetadata(meta);
					}
				}
			}
		}
	}

	private void searchRoutine(InternalIdentifierGraph graph,
			InternalIdentifier currentId, InMemoryIdentifierGraph result,
			int currentDepth, List<String> matchLinks, int maxDepth) {
		if (currentDepth <= maxDepth) {
			InternalIdentifier insertedId = result.insert(currentId);
			for (InternalMetadata currentMeta : currentId.getMetadata()) {
				result.insert(currentMeta);
				result.connectMeta(insertedId, currentMeta);
			}
			for (InternalLink currentLink : currentId.getLinks()) {
				for (InternalMetadata currentLinkMeta : currentLink
						.getMetadata()) {
					if (matchLinks.contains(currentLinkMeta.getTypeName())) {
						result.insert(currentLinkMeta);
						InternalIdentifierPair ip = currentLink
								.getIdentifiers();
						InternalIdentifier other = (ip.getFirst().equals(
								currentId) ? ip.getSecond() : ip.getFirst());

						if (!result.getIdentifiers().contains(other)) {
							other = result.insert(other);
						} else {
							for (InternalIdentifier tmp : result.getIdentifiers())  {
								if(tmp.equals(other)) {
									other = tmp;
								}
							}
						}
						InternalLink insertedLink = result.connect(insertedId, other);
						result.connectMeta(insertedLink, currentLinkMeta);
						if (!result.getIdentifiers().contains(other)) {
							searchRoutine(graph, ip.getSecond(), result,
									++currentDepth, matchLinks, maxDepth);
						}
					}
				}
			}
		}
	}

	@Override
	public long count(GraphType type) {
		return mExecutor.count(type);
	}

	@Override
	public long count(GraphType type, long timestamp) {
		return mExecutor.count(type, timestamp);
	}

	@Override
	public long count(GraphType type, long from, long to) {
		return mExecutor.count(type, from, to);
	}

	@Override
	public double meanOfEdges() {
		return mExecutor.meanOfEdges();
	}

	@Override
	public double meanOfEdges(long timestamp) {
		return mExecutor.meanOfEdges(timestamp);
	}
}
