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
 * This file is part of visitmeta dataservice, version 0.1.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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
/**
 * Project: Metalyzer 
 * Author: Johannes Busch
 * Last Change: 
 * 		by: 	$Author: $
 * 		date: 	$Date: $ 
 * Copyright (c): Hochschule Hannover
 */
package de.hshannover.f4.trust.metalyzer.api.finder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.api.MetalyzerDelta;
import de.hshannover.f4.trust.metalyzer.api.finder.util.GenericElementFilter;
import de.hshannover.f4.trust.metalyzer.api.finder.util.IdentifierExtendedFilter;
import de.hshannover.f4.trust.metalyzer.api.finder.util.MetadataFinderHelper;
import de.hshannover.f4.trust.metalyzer.api.finder.util.NoIdentifierFilter;
import de.hshannover.f4.trust.metalyzer.api.finder.util.IdentifierFilter;
import de.hshannover.f4.trust.metalyzer.api.finder.util.NotIdentifierFilter;
import de.hshannover.f4.trust.metalyzer.api.helper.DeltaBuilder;
import de.hshannover.f4.trust.metalyzer.api.helper.FilterHelper;
import de.hshannover.f4.trust.metalyzer.api.impl.MetalyzerFilterImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Implementing the needed filter methods for the 
 * fluent-api filters.
 * 
 * @author Johannes Busch
 *
 */
public class GeneralFinderImpl {

	private static final Logger log = Logger.getLogger(GeneralFinderImpl.class);

	private GraphService graphServ;
	
	/**
	 * Initializes a new given filter with the specifier
	 * if-map connection.
	 * 
	 * @author Johannes Busch
	 * @param graphServ
	 */
	public GeneralFinderImpl(GraphService graphServ) {
		if (graphServ == null) {
			log.error("GraphServ must not be null!");
			throw new RuntimeException("GraphServ must not be null!");
		}
		this.graphServ = graphServ;
	}
	
	/**
	 * Returns a filtered list of Identifier.
	 * @author Johannes Busch
	 * @param filter	Specified fluent-APi filter.
	 * @return
	 */
	public ArrayList<Identifier> filterAsIdentifiers(MetalyzerFilterImpl filter) {
		FilterHelper.isFilterNull(filter, "filterAsIdentifiers");

		GenericElementFilter<Identifier> identFilter = getIdentifierHelper(filter);

		for (IdentifierGraph g : getGraphs(filter)) {
			identFilter.filter(g.getIdentifiers());
		}

		return (ArrayList<Identifier>) identFilter.getList();
	}
	
	/**
	 * Returns an identifier filter speficied by the filter-options.
	 * 
	 * @author Johannes Busch
	 * @param filter
	 * @return
	 */
	// TODO Special Case where no identifiers but metadata are filtered must be handled.
	private GenericElementFilter<Identifier> getIdentifierHelper(MetalyzerFilterImpl filter) {

		if (!filter.getIdentifiers().isEmpty() && !filter.getMetadata().isEmpty()) {
			return new IdentifierExtendedFilter(filter.getIdentifiers(),filter.getMetadata());
		} else if (!filter.getIdentifiers().isEmpty()) {
			return new IdentifierFilter(filter.getIdentifiers());
		} else if (!filter.getNotIdentifiers().isEmpty()) {
			return new NotIdentifierFilter(filter.getNotIdentifiers());
		} else {
			return new NoIdentifierFilter();
		}
	}
	
	/**
	 * Returns the "raw"-data from visitmeta specified
	 * by filter.
	 * 
	 * @author Johannes Busch
	 * @param filter
	 * @return
	 */
	private List<IdentifierGraph> getGraphs(MetalyzerFilterImpl filter) {
		if (filter.getTime().isCurrent()) {
			return graphServ.getCurrentGraph();
		} else if (filter.getTime().isAt()) {
			return graphServ.getGraphAt(filter.getTime().getAtTimestampt());
		}

		return new ArrayList<IdentifierGraph>();
	}
	
	/**
	 * Returns a filtered MetalzyerDelta.
	 * @author Johannes Busch
	 * @param filter
	 * @return
	 */
	// Filtering Metadata does not work!
	public MetalyzerDelta<Identifier> filterAsIdentifierDelta(
			MetalyzerFilterImpl filter) {
		FilterHelper.isFilterNull(filter, "filterAsIdentifierDelta");

		GenericElementFilter<Identifier> helper = getIdentifierHelper(filter);

		Delta delta = getDelta(filter);
		List<Identifier> preFromList = getIdentifierCollection(graphServ.getGraphAt(filter.getTime().getDelta()[0]), helper);
		List<Identifier> deletes = getIdentifierCollection(delta.getDeletes(),helper);
		List<Identifier> updates = getIdentifierCollection(delta.getUpdates(),helper);

		MetalyzerDelta<Identifier> mDelta = new MetalyzerDelta<>(deletes,
				updates, preFromList);

		return mDelta;
	}
	
	/**
	 * Returns a new build delta specified by the given request.
	 * This is necessary because the metalyzer delta is defined in a
	 * different way than the visitmeta delta.
	 * 
	 * @author Johannes Busch
	 * @param filter
	 * @return
	 */
	private Delta getDelta(MetalyzerFilterImpl filter) {
		long[] t = filter.getTime().getDelta();

		return DeltaBuilder.get(graphServ, t[0], t[1]);
	}
	
	/**
	 * Returns a collection of identifiers.
	 * 
	 * @author Johannes Busch
	 * @param graphs
	 * @param idFilter Determines if the collections if filtered or not.
	 * @return
	 */
	private List<Identifier> getIdentifierCollection(List<IdentifierGraph> graphs, GenericElementFilter<Identifier> idFilter) {
		if (graphs == null) {
			return idFilter.getList();
		}

		for (IdentifierGraph graph : graphs) {
			idFilter.filter(graph.getIdentifiers());
		}

		return idFilter.getListAndRefresh();
	}
	
//###################################### Metadata filter functions ##############################################
	
	/**
	 * Returns a list of filtered Metadata
	 * @param filter
	 * @return
	 */
	public ArrayList<Metadata> filterAsMetadata(MetalyzerFilterImpl filter) {
		FilterHelper.isFilterNull(filter, "filterAsMetadata");

		return (ArrayList<Metadata>) MetadataFinderHelper.getFiltered(getMetaCollection(filterAsIdentifiers(filter)), filter.getMetadata());
	}
	
	/**
	 * Extracts all Metadata from a given identifier list.
	 * 
	 * @author Johannes Busch
	 * @param identifiers
	 * @return
	 */
	private ArrayList<Metadata> getMetaCollection(List<Identifier> identifiers) {
		ArrayList<Metadata> metadata = new ArrayList<>();
		HashSet<Link> copiedLinks = new HashSet<>();

		for (Identifier ident : identifiers) {
			metadata.addAll(ident.getMetadata());

			for (Link l : ident.getLinks()) {
				if (!(copiedLinks.contains(l))) {
					metadata.addAll(l.getMetadata());
					copiedLinks.add(l);
				}
			}
		}

		return metadata;
	}

	/**
	 * Returns a filtered Metalyzer Delta
	 * 
	 * @author Johannes Busch
	 * @param filter
	 * @return
	 */
	public MetalyzerDelta<Metadata> filterAsMetadataDelta(
			MetalyzerFilterImpl filter) {
		FilterHelper.isFilterNull(filter, "filterAsMetadataDelta");

		GenericElementFilter<Identifier> helper = getIdentifierHelper(filter);
		
		// Grabs the delta from visitmeta and extracts the identifier lists.
		Delta delta = getDelta(filter);
		List<Identifier> preFromList = getIdentifierCollection(graphServ.getGraphAt(filter.getTime().getDelta()[0]), helper);
		List<Identifier> deletes = getIdentifierCollection(delta.getDeletes(),helper);
		List<Identifier> updates = getIdentifierCollection(delta.getUpdates(),helper);

		// Grabs the metadata from the identifiers.
		Collection<Metadata> mPreFromList = getMetaCollection(preFromList);
		Collection<Metadata> mDeletes = getMetaCollection(deletes);
		Collection<Metadata> mUpdates = getMetaCollection(updates);

		MetalyzerDelta<Metadata> mDelta = null;

		if (!filter.getMetadata().isEmpty()) {
			mDelta = new MetalyzerDelta<>(MetadataFinderHelper.getFiltered(mDeletes, filter.getMetadata()),
					MetadataFinderHelper.getFiltered(mUpdates,filter.getMetadata()),
					MetadataFinderHelper.getFiltered(mPreFromList,filter.getMetadata()));
		} else {
			mDelta = new MetalyzerDelta<>(MetadataFinderHelper.get(mDeletes),
					MetadataFinderHelper.get(mUpdates),
					MetadataFinderHelper.get(mPreFromList));
		}

		return mDelta;
	}
}
