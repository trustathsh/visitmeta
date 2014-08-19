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
 * Author: Sören Grzanna
 * Author: Johannes Busch
 * Last Change: 
 * 		by: 	$Author: $
 * 		date: 	$Date: $ 
 * Copyright (c): Hochschule Hannover
 */
package de.hshannover.f4.trust.metalyzer.api.finder;

import java.util.Collection;
import java.util.List;

import de.hshannover.f4.trust.metalyzer.api.IdentifierFinder;
import de.hshannover.f4.trust.metalyzer.api.MetalyzerDelta;
import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.api.finder.util.GenericElementFilter;
import de.hshannover.f4.trust.metalyzer.api.finder.util.NoIdentifierFilter;
import de.hshannover.f4.trust.metalyzer.api.finder.util.IdentifierFilter;
import de.hshannover.f4.trust.metalyzer.api.helper.DeltaBuilder;
import de.hshannover.f4.trust.metalyzer.api.helper.FilterHelper;
import de.hshannover.f4.trust.metalyzer.api.helper.MetalyzerAPIHelper;
import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphType;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

/**
 * Implements the {@link IdentifierFinder} interface.
 * 
 * @author Sören Grzanna
 * @author Johannes Busch
 *
 */
public class IdentifierFinderImpl implements IdentifierFinder {

	private GraphService graphServ;
	
	/**
	 * Initializes the IdentifierFinder class.
	 * @author Johannes Busch
	 * @param mockService
	 */
	public IdentifierFinderImpl(GraphService graphService) {
		graphServ = graphService;
	}
	
	/**
	 * Returns a Collection of all current Identifiers in the IF-MAP graph of VisITMeta.
	 * @author Johannes Busch
	 * @return {@link Collection}
	 * @throws {@link MetalyzerAPIException} Exception thrown when the filer is null
	 */
	@Override
	public Collection<Identifier> getCurrent() throws MetalyzerAPIException {
		return getCurrent(StandardIdentifierType.ALL);
	}
	
	/**
	 * Returns a Collection of all current Identifiers in the IF-MAP graph of VisITMeta.
	 * @author Johannes Busch
	 * @param filter {@link StandardIdentifierType} Filter contains a filter of IF-MAP identifier-Types.
	 * @return {@link Collection}
	 * @throws {@link MetalyzerAPIException} Exception thrown when the filer is null
	 */
	@Override
	public Collection<Identifier> getCurrent(StandardIdentifierType filter) throws MetalyzerAPIException {
		FilterHelper.isFilterNull(filter);
		
		if( StandardIdentifierType.ALL == filter) {
			return getIdentifierCollection(graphServ.getCurrentGraph(), new NoIdentifierFilter());
		}
		
		IdentifierFilter idFilter = new IdentifierFilter(FilterHelper.convertSTItoString(filter));
		return getIdentifierCollection(graphServ.getCurrentGraph(), idFilter);
	}
	
	/**
	 * Returns a Collection of all Identifiers in the IF-MAP graph of VisITMeta at the given timestamp.
	 * @author Johannes Busch
	 * @param timestamp 
	 * @return
	 * @throws {@link MetalyzerAPIException} Exception thrown when the filer is null, timestamp is negative.
	 */
	@Override
	public Collection<Identifier> get(long timestamp) throws MetalyzerAPIException {
		return get(StandardIdentifierType.ALL,timestamp);
	}
	
	/**
	 * Returns a Collection of all Identifiers in the IF-MAP graph of VisITMeta at the given timestamp.
	 * @author Johannes Busch
	 * @param filter {@link StandardIdentifierType} Filter contains a filter of IF-MAP identifier-Types.
	 * @param timestamp 
	 * @return
	 * @throws {@link MetalyzerAPIException} Exception thrown when the filer is null, timestamp is negative.
	 */
	@Override
	public Collection<Identifier> get(StandardIdentifierType filter, long timestamp) throws MetalyzerAPIException {
		FilterHelper.isFilterNull(filter);
		MetalyzerAPIHelper.isTimestampNegative(timestamp);
		
		if( StandardIdentifierType.ALL == filter) {
			return getIdentifierCollection(graphServ.getGraphAt(timestamp), new NoIdentifierFilter());
		}
		
		IdentifierFilter idFilter = new IdentifierFilter(FilterHelper.convertSTItoString(filter));
		return getIdentifierCollection(graphServ.getGraphAt(timestamp), idFilter);
	}
	
	/**
	 * Returns an Metalyzer Delta build by the given two timestamps with all Identifier that were present before from-Timestamp.
	 * @author Johannes Busch
	 * @param from Timestamps where the Delta should start.
	 * @param to Timestamps where the Delta should end.
	 * @return MetalyzerDelta {@link MetalyzerDelta} The availables are from the graph at the from timestamp
	 * @throws {@link MetalyzerAPIException} Exception thrown when filter is null, timestamps are negative or from is higher then to
	 */
	@Override
	public MetalyzerDelta<Identifier> get(long from, long to) throws MetalyzerAPIException {
		return get(StandardIdentifierType.ALL,from,to);
	}
	
	/**
	 * Returns an Metalyzer Delta build by the given two timestamps with all Identifier that were present before from-Timestamp.
	 * @author Johannes Busch
	 * @param filter {@link StandardIdentifierType} Filter contains a filter of IF-MAP identifier-Types.
	 * @param from Timestamps where the Delta should start.
	 * @param to Timestamps where the Delta should end.
	 * @return MetalyzerDelta {@link MetalyzerDelta} The availables are from the graph at the from timestamp
	 * @throws {@link MetalyzerAPIException} Exception thrown when filter is null, timestamps are negative or from is higher then to
	 */
	public MetalyzerDelta<Identifier> get(StandardIdentifierType filter, long from, long to) throws MetalyzerAPIException {
		FilterHelper.isFilterNull(filter);
		MetalyzerAPIHelper.isDeltaPossible(from, to);
		
		Delta delta = DeltaBuilder.get(graphServ, from, to);
		Collection<Identifier> preFromList = get(filter,from);
		Collection<Identifier> deletes = null;
		Collection<Identifier> updates = null;
		GenericElementFilter<Identifier> idFilter = null;
		
		if(StandardIdentifierType.ALL == filter) {
			idFilter = new NoIdentifierFilter();
		}
		else {
			idFilter = new IdentifierFilter(FilterHelper.convertSTItoString(filter));
		}
		
		deletes = getIdentifierCollection(delta.getDeletes(),idFilter);
		updates = getIdentifierCollection(delta.getUpdates(),idFilter);
		
		return new MetalyzerDelta<Identifier>(deletes, updates, preFromList);
	}
	
	/**
	 * Returns a collection of identifiers.
	 * @author Johannes Busch
	 * @param graphs
	 * @param idFilter Determines if the collections if filtered or not.
	 * @return
	 */
	private Collection<Identifier> getIdentifierCollection(List<IdentifierGraph> graphs, GenericElementFilter<Identifier> idFilter) {
		if(graphs == null) {
			return idFilter.getList();
		}
		
		for(IdentifierGraph graph : graphs) {
			idFilter.filter(graph.getIdentifiers());
		}
		
		return idFilter.getListAndRefresh();
	}
	
	/**
	 * Returns the number of all current identifiers.
	 * @author Johannes Busch
	 * @return
	 */
	@Override
	public long count() {
		return graphServ.count(GraphType.IDENTIFIER);
	}
	
	/**
	 * Returns the number of all identifiers at the given timestamp.
	 * @author Johannes Busch
	 * @param timestamp
	 * @return
	 */
	@Override
	public long count(long timestamp) {
		return graphServ.count(GraphType.IDENTIFIER, timestamp);
	}
	
	/**
	 * Returns the number of all identifiers in the given delta.
	 * @author Johannes Busch
	 * @param from
	 * @param to
	 * @return
	 */
	@Override
	public long count(long from, long to) {
		return graphServ.count(GraphType.IDENTIFIER, from, to);
	}
}