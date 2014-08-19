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

import de.hshannover.f4.trust.metalyzer.api.MetadataFinder;
import de.hshannover.f4.trust.metalyzer.api.MetalyzerDelta;
import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.api.finder.util.MetadataFinderHelper;
import de.hshannover.f4.trust.metalyzer.api.helper.DeltaBuilder;
import de.hshannover.f4.trust.metalyzer.api.helper.MetalyzerAPIHelper;
import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphType;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Implements the {@link MetadataFinder} interface.
 * 
 * @author Sören Grzanna
 * @author Johannes Busch
 *
 */
public class MetadataFinderImpl implements MetadataFinder {

	private GraphService graphServ;
	
	/**
	 * Initializes the MetadataFinder class.
	 * @author Johannes Busch
	 * @param graphService Used to get data from VisITMeta.
	 */
	public MetadataFinderImpl(GraphService graphService) {
		graphServ = graphService;
	}

	/**
	 * Returns a collection of all known current Metadata in the IF-MAP graph.
	 * @author Sören Grzanna
	 * @param filter
	 * @return
	 * @throws MetalyzerAPIException
	 */
	@Override
	public Collection<Metadata> getCurrent() throws MetalyzerAPIException {
		return getCurrent(null);
	}
	
	/**
	 * Returns a filtered collection of known current Metadata in the IF-MAP graph.
	 * @author Sören Grzanna
	 * @param filter
	 * @return
	 * @throws MetalyzerAPIException
	 */
	@Override
	public Collection<Metadata> getCurrent(String filter) throws MetalyzerAPIException {
		Collection<Metadata> metacollection = MetadataFinderHelper.getMetaCollection(graphServ.getCurrentGraph());
		if(filter == null) {
			return metacollection;
		}
		return MetadataFinderHelper.getFiltered(metacollection, filter);
	}
	

	/**
	 * Returns a collection of all known Metadata in the IF-MAP graph at the given timestamp.
	 * @author Sören Grzanna
	 * @param filter
	 * @param timestamp
	 * @return
	 * @throws MetalyzerAPIException
	 */
	@Override
	public Collection<Metadata> get(long timestamp) throws MetalyzerAPIException {
		return get(null,timestamp);
	}

	/**
	 * Returns a filtered collection of all known Metadata in the IF-MAP graph at the given timestamp.
	 * @author Sören Grzanna
	 * @param filter
	 * @param timestamp
	 * @return
	 * @throws MetalyzerAPIException
	 */
	@Override
	public Collection<Metadata> get(String filter, long timestamp) throws MetalyzerAPIException {
		MetalyzerAPIHelper.isTimestampNegative(timestamp);
		Collection<Metadata> metacollection = MetadataFinderHelper.getMetaCollection(graphServ.getGraphAt(timestamp));
		if(filter == null) {
			return metacollection;
		}
		return MetadataFinderHelper.getFiltered(metacollection, filter);
	}
	

	/**
	 * Returns an Metalyzer Delta build by the given two timestamps with all Metadata that were present before from-Timestamp.
	 * @author Sören Grzanna
	 * @param from Timestamps where the Delta should start.
	 * @param to Timestamps where the Delta should end.
	 * @return MetalyzerDelta {@link MetalyzerDelta} The availables are from the graph at the from timestamp
	 * @throws {@link MetalyzerAPIException} Exception thrown when filter is null, timestamps are negative or from is higher then to
	 */
	@Override
	public MetalyzerDelta<Metadata> get(long from, long to) throws MetalyzerAPIException {
		return get(null,from,to);
	}

	/**
	 * Returns an Metalyzer Delta build by the given two timestamps with all filtered Metadata that were present before from-Timestamp.
	 * @author Sören Grzanna
	 * @param filter Filter contains a filter of IF-MAP metadata.
	 * @param from Timestamps where the Delta should start.
	 * @param to Timestamps where the Delta should end.
	 * @return MetalyzerDelta {@link MetalyzerDelta} The availables are from the graph at the from timestamp
	 * @throws {@link MetalyzerAPIException} Exception thrown when filter is null, timestamps are negative or from is higher then to
	 */
	@Override
	public MetalyzerDelta<Metadata> get(String filter, long from, long to) throws MetalyzerAPIException {
		MetalyzerAPIHelper.isDeltaPossible(from, to);
		
		Delta delta = DeltaBuilder.get(graphServ, from, to);
		Collection<Metadata> preFrom = get(filter, from);
		
		if(filter == null){
			return new MetalyzerDelta<Metadata>(
					MetadataFinderHelper.getMetaCollection(delta.getDeletes()), 
					MetadataFinderHelper.getMetaCollection(delta.getUpdates()), 
					preFrom
			);
		}
		
		Collection<Metadata> deletes = MetadataFinderHelper.getMetaCollection(delta.getDeletes());
		Collection<Metadata> updates = MetadataFinderHelper.getMetaCollection(delta.getUpdates());
		
		return new MetalyzerDelta<Metadata>(
				MetadataFinderHelper.getFiltered(deletes, filter),
				MetadataFinderHelper.getFiltered(updates, filter),
				MetadataFinderHelper.getFiltered(preFrom, filter)
				);
	}
	
	/**
	 * Returns the number of all current metadata.
	 * @author Johannes Busch
	 * @return
	 */
	@Override
	public long count() {
		return graphServ.count(GraphType.METADATA);
	}
	
	/**
	 * Returns the number of all metadata at the given timestamp.
	 * @author Johannes Busch
	 * @param timestamp
	 * @return
	 */
	@Override
	public long count(long timestamp) {
		return graphServ.count(GraphType.METADATA, timestamp);
	}

	/**
	 * Returns the number of all metadata in the given delta.
	 * @author Johannes Busch
	 * @param from
	 * @param to
	 * @return
	 */
	@Override
	public long count(long from, long to) {
		return graphServ.count(GraphType.METADATA, from, to);
	}
}
