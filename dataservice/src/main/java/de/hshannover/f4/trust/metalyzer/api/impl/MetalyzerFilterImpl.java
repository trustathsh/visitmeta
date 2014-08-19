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
package de.hshannover.f4.trust.metalyzer.api.impl;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.api.DeltaReturner;
import de.hshannover.f4.trust.metalyzer.api.ListReturner;
import de.hshannover.f4.trust.metalyzer.api.MetalyzerDelta;
import de.hshannover.f4.trust.metalyzer.api.MetalyzerFilter;
import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.api.MetalyzerTime;
import de.hshannover.f4.trust.metalyzer.api.exception.FilterTimeException;
import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.api.finder.GeneralFinderImpl;
import de.hshannover.f4.trust.metalyzer.api.helper.FilterHelper;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

public class MetalyzerFilterImpl implements MetalyzerFilter, ListReturner, DeltaReturner {
	
	private static final Logger log = Logger.getLogger(MetalyzerFilterImpl.class);
	
	private GeneralFinderImpl filterFinder;
	private MetalyzerTimeImpl time;
	private HashSet<String> ident;
	private HashSet<String> notIdent;
	private HashSet<String> meta;
	private HashSet<String> notMeta;
	
	public MetalyzerFilterImpl(GeneralFinderImpl filterFinder) {
		this.filterFinder = filterFinder;
		ident = new HashSet<>();
		meta = new HashSet<>();
		notIdent = new HashSet<>();
		notMeta = new HashSet<>();
	}
	
	/**
	 * Adds an identifier to list of filtered identifier.
	 * @param identifier must not be null.
	 * @return
	 * @throws MetalyzerAPIException
	 */
	@Override
	public MetalyzerFilter or(StandardIdentifierType identifier) {
		FilterHelper.isFilterNull(identifier);
		
		ident.add(FilterHelper.convertSTItoString(identifier));
		
		return this;
	}
	
	/**
	 * Adds an metadata to the list of filtered metadata.
	 * @param metadata must not be null.
	 * @return
	 * @throws MetalyzerAPIException 
	 */
	@Override
	public MetalyzerFilter or(String metadata) {
		FilterHelper.isFilterNull(metadata);
		
		meta.add(metadata);
		
		return this;
	}
	
	/**
	 * Adds an identifier to the list of exluded identifers.
	 * @param identifier must not be null.
	 * @return
	 * @throws MetalyzerAPIException
	 */
	@Override
	public MetalyzerFilter not(StandardIdentifierType identifier) {
		FilterHelper.isFilterNull(identifier);
		
		notIdent.add(FilterHelper.convertSTItoString(identifier));
		
		return this;
	}
	
	/**
	 * Adds an metadata to the list of exluded metadata.
	 * @param metadata must not be null.
	 * @return
	 */
//	@Override Deactivated because it is not supported yet!
//	public SimpleFilterImpl not(String metadata) {
//		notMeta.add(metadata);
//		
//		return this;
//	}
	
	/**
	 * Returns a new MetalyzerTime object to set the time for this filter.
	 */
	@Override
	public MetalyzerTime getMetalyzerTime() {
		time = new MetalyzerTimeImpl(this);
		return time;
	}

	@Override
	public ArrayList<Identifier> asIdentifiers() {
		if(time == null || !(time.isCurrent() || time.isAt())) {
			log.error("MetalyzerTime was not correctly set! ( Maybe time was not set or an delta ? )");
			throw new FilterTimeException("MetalyzerTime was not correctly set!");
		}
		return filterFinder.filterAsIdentifiers(this);
	}

	@Override
	public ArrayList<Metadata> asMetadata() {
		if(time == null || !(time.isCurrent() || time.isAt())) {
			log.error("MetalyzerTime was not correctly set! ( Maybe time was not set or an delta ? )");
			throw new FilterTimeException("MetalyzerTime was not correctly set!");
		}
		return filterFinder.filterAsMetadata(this);
	}

	@Override
	public MetalyzerDelta<Identifier> asIdentifierDelta() {
		if(time == null || !time.isDelta()) {
			log.error("MetalyzerTime was not correctly set! ( Maybe time was not set or it was not an delta ? )");
			throw new FilterTimeException("MetalyzerTime was not correctly set!");
		}
		return filterFinder.filterAsIdentifierDelta(this);
	}

	@Override
	public MetalyzerDelta<Metadata> asMetadataDelta() {
		if(time == null || !time.isDelta()) {
			log.error("MetalyzerTime was not correctly set! ( Maybe time was not set or it was not an delta ? )");
			throw new FilterTimeException("MetalyzerTime was not correctly set!");
		}
		return filterFinder.filterAsMetadataDelta(this);
	}
	
	/**
	 * Returns an set of positice identifiers.
	 * @return
	 */
	public HashSet<String> getIdentifiers() {
		return ident;
	}
	
	/**
	 * Returns an set of positive metadata.
	 * @return
	 */
	public HashSet<String> getMetadata() {
		return meta;
	}
	
	/**
	 * Returns a set of identifiers which should be excluded in result.
	 * @return
	 */
	public HashSet<String> getNotIdentifiers() {
		return notIdent;
	}
	
	/**
	 * Returns a set of metadata which should be exluded in result.
	 * @return
	 */
	public HashSet<String> getNotMetadata() {
		return notMeta;
	}
	
	/**
	 * Returns as MetalyzerTimeImpl.
	 * @return
	 */
	// TODO Should be merged with getMetalyzerTime()
	public MetalyzerTimeImpl getTime() {
		return time;
	}
}
