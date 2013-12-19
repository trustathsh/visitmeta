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
package de.fhhannover.inform.trust.visitmeta.dataservice.util;



import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.visitmeta.dataservice.graphservice.IdentifierGraphImpl;
import de.fhhannover.inform.trust.visitmeta.dataservice.graphservice.IdentifierImpl;
import de.fhhannover.inform.trust.visitmeta.dataservice.graphservice.LinkImpl;
import de.fhhannover.inform.trust.visitmeta.dataservice.graphservice.MetadataImpl;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifierGraph;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifierPair;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.fhhannover.inform.trust.visitmeta.interfaces.IdentifierGraph;
import de.fhhannover.inform.trust.visitmeta.persistence.inmemory.InMemoryIdentifier;
import de.fhhannover.inform.trust.visitmeta.persistence.inmemory.InMemoryIdentifierGraph;
import de.fhhannover.inform.trust.visitmeta.persistence.inmemory.InMemoryMetadata;

/**
 * Encapsulates graph utility methods, like copying, transforming and more.
 */
public class GraphHelper {

	private static final Logger log = Logger.getLogger(GraphHelper.class);

	/**
	 * Creates a deep copy of an {@link InternalIdentifierGraph}
	 * @param original The graph to copy
	 * @return A deep copy of the original graph
	 */
	public static InternalIdentifierGraph cloneGraph(InternalIdentifierGraph original) {
		InternalIdentifier start = original.getStartIdentifier();
		InMemoryIdentifierGraph copy = new InMemoryIdentifierGraph(original.getTimestamp());

		if (start == null) {
			log.debug("returning empty graph");
			return new InMemoryIdentifierGraph(original.getTimestamp());
		}

		recurse(start, copy, new HashSet<InternalIdentifier>());
		return copy;
	}

	/**
	 * Transforms an internal representation of a graph ({@link InternalIdentifierGraph}) into the external
	 * external format ({@link IdentifierGraph}) using the ({@link IdentifierGraphImpl}) implementation
	 * @param original The graph in the internal data structure
	 * @return The graph in the external data structure
	 */
	public static IdentifierGraph internalToExternalGraph(
			InternalIdentifierGraph original) {
		InternalIdentifier start = original.getStartIdentifier();
		IdentifierGraphImpl copy = new IdentifierGraphImpl(
				original.getTimestamp());
		recurse(start, copy, new HashSet<InternalIdentifier>());
		return copy;
	}
	
	private static void recurse(InternalIdentifier current, IdentifierGraphImpl graph,
			Set<InternalIdentifier> seen) {
		IdentifierImpl detachedCurrent = null;
		if (!seen.contains(current)) {
			detachedCurrent = new IdentifierImpl(current);
			graph.insert(detachedCurrent);
			for (InternalMetadata m : current.getMetadata()) {
				detachedCurrent.addMetadata(new MetadataImpl(m));
			}
			seen.add(current);
			for (InternalLink l : current.getLinks()) {
				InternalIdentifierPair pair = l.getIdentifiers();
				InternalIdentifier other = (InternalIdentifier) ((pair
						.getFirst().equals(current)) ? pair.getSecond() : pair
						.getFirst());
				if (seen.contains(other)) {
					IdentifierImpl detachedOther = graph.findIdentifier(other);
					LinkImpl link = graph.connect(detachedCurrent,
							detachedOther);
					for (InternalMetadata m : l.getMetadata()) {
						link.addMetadata(new MetadataImpl(m));
					}
				} else {
					recurse(other, graph, seen);
				}
			}
		}
	}
	
	private static void recurse(InternalIdentifier current, InMemoryIdentifierGraph graph,
			Set<InternalIdentifier> seen) {
		InternalIdentifier detachedCurrent = null;
		if (!seen.contains(current)) {
			detachedCurrent = graph.insert(current);
			for (InternalMetadata m : current.getMetadata()) {
				graph.connectMeta(detachedCurrent, graph.insert(m));
			}
			seen.add(current);
			for (InternalLink l : current.getLinks()) {
				InternalIdentifierPair pair = l.getIdentifiers();
				InternalIdentifier other = (InternalIdentifier) ((pair
						.getFirst().equals(current)) ? pair.getSecond() : pair
								.getFirst());
				if (seen.contains(other)) {
					InternalIdentifier detachedOther = graph.findIdentifier(other);
					InternalLink link = graph.connect(detachedCurrent,
							detachedOther);
					for (InternalMetadata m : l.getMetadata()) {
						graph.connectMeta(link, graph.insert(m));
					}
				} else {
					recurse(other, graph, seen);
				}
			}
		}

	}
}
