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
 * Auhtor: Johannes Busch
 * Last Change: 
 * 		by: 	$Author: $
 * 		date: 	$Date: $ 
 * Copyright (c): Hochschule Hannover
 */

package de.hshannover.f4.trust.metalyzer.api;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.api.finder.GeneralFinderImpl;
import de.hshannover.f4.trust.metalyzer.api.finder.IdentifierFinderImpl;
import de.hshannover.f4.trust.metalyzer.api.finder.MetadataFinderImpl;
import de.hshannover.f4.trust.metalyzer.api.helper.MetalyzerAPIHelper;
import de.hshannover.f4.trust.metalyzer.api.impl.GraphAnalyzerImpl;
import de.hshannover.f4.trust.metalyzer.api.impl.MetalyzerFilterImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;

/**
 * Entry point for all api methods.
 * Also handles the different if-map connections. For every connection
 * a new api object is created. Each object has its own if-map connection.
 * All objects are cached within the class.
 * 
 * @author Johannes Busch
 * @author Sören Grzanna
 *
 */
public class MetalyzerAPI {
	
	private static final Logger log = Logger.getLogger(MetalyzerAPI.class);
	
	/**
	 * Structures that caches all api connection objects.
	 */
	private static volatile HashMap<String,MetalyzerAPI> instances = new HashMap<>();
	
	private IdentifierFinderImpl identFinder;
	private MetadataFinderImpl metaFinder;
	private GeneralFinderImpl generalFinder;
	
	private GraphAnalyzer graphAnalyzer;
	
	/**
	 * Instance of an VisITMeta GraphService as an entry point to their api.
	 */
	private GraphService gs;
	
	private MetalyzerAPI(String connection) throws MetalyzerAPIException{
		gs = MetalyzerAPIHelper.getGraphService(connection);
		// Initializing the finder.
		identFinder = new IdentifierFinderImpl(gs);
		metaFinder = new MetadataFinderImpl(gs);
		generalFinder = new GeneralFinderImpl(gs);
		graphAnalyzer = new GraphAnalyzerImpl(gs);
	}

	/**
	 * Creates an Singleton instances of MetalyzerAPI
	 * @author Sören Grzanna
	 * @return {@link MetalyzerAPI}
	 * @throws MetalyzerAPIException 
	 */
	public static synchronized MetalyzerAPI getInstance() {
		return MetalyzerAPI.getInstance("default");
	}
	
	/**
	 * Creates an Singleton instances of MetalyzerAPI
	 * @author Sören Grzanna
	 * @param string connection
	 * @return {@link MetalyzerAPI}
	 * @throws MetalyzerAPIException
	 */
	public static synchronized MetalyzerAPI getInstance(String connection){
		if( !instances.containsKey(connection) ){
			try {
				instances.put(connection, new MetalyzerAPI(connection));
			} catch (MetalyzerAPIException e) {
				log.error("MetalyzerAPI konnte nicht erstellt werden: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return instances.get(connection);
	}
	
	/**
	 * Returns an instance of IdentifierFinder.
	 * @return {@link IdentifierFinder}
	 */
	public IdentifierFinder getIdentifierFinder() {
		return identFinder;
	}
	
	/**
	 * Returns an instance of MetadataFinder.
	 * @return {@link MetadataFinder}
	 */
	public MetadataFinder getMetadataFinder() {
		return metaFinder;
	}
	
	/**
	 * Returns an instance of GraphAnalyzer.
	 * @return {@link GraphAnalyzer}
	 */
	public GraphAnalyzer getGraphAnalyzer() {
		return graphAnalyzer;
	}
	
	/**
	 * Returns an instance of MetalyzerFilter.
	 * @return {@link MetalyzerFilter}
	 */
	public MetalyzerFilter getFilter() {
		return new MetalyzerFilterImpl(generalFinder);
	}
	
	/**
	 * Returns a ArrayList of all timestamps between timestamp from and to
	 * @param from Start timestamp   
	 * @param to End timestamp
	 * @return
	 * @author Sören Grzanna
	 */
	public ArrayList<Long> getTimestampsBetween(long from, long to){
		return MetalyzerAPIHelper.getTimestamps(gs, from, to);
	}
}