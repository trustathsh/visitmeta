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
 * Auhtor: Johannes Busch
 * 
 * Interface fuer die MetadataFinder Klasse. 
 * Bildet Schnittstelle zu den weiteren Metalyzer-Komponenten.
 * 
 * Copyright (c): Hochschule Hannover
 */
package de.hshannover.f4.trust.metalyzer.api;

import java.util.Collection;

import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * MetadataFinder Interface
 * 
 * Specifies all get / filter methods
 * for Metadata.
 * 
 * @author Johannes Busch
 *
 */
public interface MetadataFinder {
	/**
	 * Returns all current Metadata present in the IF-MAP graph of VisITMeta.
	 * @return {@link Collection}
	 * @throws {@link MetalyzerAPIException}
	 */
	public Collection<Metadata> getCurrent() throws MetalyzerAPIException;
	
	/**
	 * Filters the current Metadata with the given filter.
	 * @param filter
	 * @return {@link Collection}
	 * @throws {@link MetalyzerAPIException}
	 */
	public Collection<Metadata> getCurrent(String filter) throws MetalyzerAPIException;
	
	/**
	 * Returns all Metadata present in the IF-MAP graph of VisITMeta at the given timestamp.
	 * @param timestamp
	 * @return {@link Collection}
	 * @throws MetalyzerAPIException
	 */
	public Collection<Metadata> get(long timestamp) throws MetalyzerAPIException;

	/**
	 *  Filters the Metadata with the given filter at the given timestamp.
	 * @param filter
	 * @param timestamp
	 * @return {@link Collection}
	 * @throws {@link MetalyzerAPIException}
	 */
	public Collection<Metadata> get(String filter, long timestamp) throws MetalyzerAPIException;
	
	/**
	 * Returns a Delta with all deleted updated Metadata specified by the timestamps from and to.
	 * @param from
	 * @param to
	 * @return {@link MetalyzerDelta}
	 * @throws MetalyzerAPIException 
	 */
	public MetalyzerDelta<Metadata> get(long from, long to) throws MetalyzerAPIException;

	/**
	 * Returns a Delta with all deleted updated Metadata specified by the timestamps from and to.
	 * @param filter
	 * @param from
	 * @param to
	 * @return {@link MetalyzerDelta}
	 * @throws {@link MetalyzerAPIException}
	 */
	public MetalyzerDelta<Metadata> get(String filter, long from, long to) throws MetalyzerAPIException;
	
	/**
	 * Returns the number of all current metadata.
	 * @return
	 */
	public long count();
	
	/**
	 * Returns the number of all metadata at the given timestamp.
	 * @param timestamp
	 * @return
	 */
	public long count(long timestamp);
	
	/**
	 * Returns the number of all metadata in the given delta.
	 * @param from
	 * @param to
	 * @return
	 */
	public long count(long from, long to);
}
