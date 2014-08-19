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
package de.hshannover.f4.trust.metalyzer.api.helper;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.api.exception.FilterException;
import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.api.exception.NoFilterException;
import de.hshannover.f4.trust.metalyzer.api.impl.MetalyzerFilterImpl;

/**
 * Contains several helper methods for the api filters.
 * 
 * @author Johannes Busch
 *
 */
public class FilterHelper {
	
	private static final Logger log = Logger.getLogger(FilterHelper.class);
	
	/**
	 * Converts the enum {@link StandardIdentifierType} into a String.
	 * @param identType {@link StandardIdentifierType}
	 * @return
	 * @throws MetalyzerAPIException
	 */
	public static String convertSTItoString(StandardIdentifierType identType) throws MetalyzerAPIException{
		if( StandardIdentifierType.ALL == identType ) {
			log.error("Type.All can not be converted!");
			throw new FilterException("Can not be converted: StandardIdentifierType.all");
		}
		if( StandardIdentifierType.ACCESS_REQUEST == identType ) {
			return "access-request";
		}
		if( StandardIdentifierType.DEVICE == identType ) {
			return "device";
		}
		if( StandardIdentifierType.IP_ADDRESS == identType ) {
			return "ip-address";
		}
		if( StandardIdentifierType.MAC_ADDRESS == identType ) {
			return "mac-address";
		}
		if( StandardIdentifierType.IDENTITY == identType ) {
			return "identity";
		}
		
		log.error("Unknown Identifier.");
		throw new NoFilterException("Unkown IdentifierType");
	}
	
	/**
	 * Tests if the given filter-Object is NULL
	 * @param filter
	 * @throws MetalyzerAPIException Throws an Exception if the filter is null.
	 */
	public static void isFilterNull(StandardIdentifierType filter) {
		if(filter == null) {
			log.error("Type-Filter was null");
			throw new NoFilterException("Filter must not be null!");
		}
	}
	
	/**
	 * Tests if the given filter-Object is NULL or equals(" ").
	 * @param filter
	 * @throws MetalyzerAPIException Throws an Exception if the filter is null.
	 */
	public static void isFilterNull(String filter) {
		if(filter == null || filter.equals("") || filter.equals(" ")) {
			log.error("String-Filter was null");
			throw new NoFilterException("Filter must not be null!");
		}
	}
	
	/**
	 * Tests if the given filter-Object is NULL and throws a RuntimeException.
	 * @param filter
	 * @param calledFunctionName
	 * @throws RuntimeException
	 */
	public static void isFilterNull(MetalyzerFilterImpl filter, String calledFunctionName) {
		if(filter == null) {
			log.error("SimpleFilterImpl in " + calledFunctionName + " should not be null!");
			throw new RuntimeException("SimpleFilterImpl should not be null!");
		}
	}
}
