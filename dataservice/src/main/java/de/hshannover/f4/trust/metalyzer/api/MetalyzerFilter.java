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
 * Last Change: 
 * 		by: 	$Author: $
 * 		date: 	$Date: $ 
 * Copyright (c): Hochschule Hannover
 */
package de.hshannover.f4.trust.metalyzer.api;

import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;

/**
 * Entry Point of the fluent-filter-api.
 * Has several different filter methods.
 * 
 * @author Johannes Busch
 *
 */
public interface MetalyzerFilter {
	
	/**
	 * Adds an identifier to list of filtered identifier.
	 * @param identifier
	 * @return
	 * @throws MetalyzerAPIException
	 */
	public MetalyzerFilter or(StandardIdentifierType identifier);
	
	/**
	 * Adds an metadata to the list of filtered metadata.
	 * @param metadata
	 * @return
	 */
	public MetalyzerFilter or(String metadata);
	
	/**
	 * Adds an identifier to the list of excluded identifiers.
	 * @param identifier must not be null.
	 * @return
	 * @throws MetalyzerAPIException
	 */
	public MetalyzerFilter not(StandardIdentifierType identifier);
	
	/**
	 * Adds an metadata to the list of excluded metadata.
	 * @param metadata must not be null.
	 * @return
	 */
	//public SimpleFilter not(String metadata); deactived because it is not supported yet
		
	/**
	 * Returns an Time object which shall be used to set the filter time.
	 * @return
	 */
	public MetalyzerTime getMetalyzerTime();
}
