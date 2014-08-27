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
package de.hshannover.f4.trust.metalyzer.api.helper;

import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.api.exception.NegativeTimestampException;
import de.hshannover.f4.trust.metalyzer.api.exception.TimestampException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.ifmap.ConnectionManager;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;

/**
 * Class contains several helper methods which are used
 * in several places in the api.
 * 
 * @author Sören Grzanna
 * @author Johannes Busch
 *
 */
public class MetalyzerAPIHelper {
	
	private static final Logger log = Logger.getLogger(MetalyzerAPIHelper.class);
	
	/**
	 * Tests if the timestamp is negative.
	 * Throws an {@link NegativeTimestampException} when timestamp is negative.
	 * @author Johannes Busch
	 * @throws {@link NegativeTimestampException}
	 */
	public static void isTimestampNegative(long timestamp) throws NegativeTimestampException{
		if( timestamp < 0 ) {
			log.error("Timestamp was negative: " + timestamp);
			throw new NegativeTimestampException(timestamp);
		}
	}
	
	/**
	 * Tests if the delta given by the timstamps is possible.
	 * Throws an {@link TimestampException} when one of the timestamps is negative or from greater than to.
	 * @author Johannes Busch
	 * @throws {@link NegativeTimestampException}
	 */
	public static void isDeltaPossible(long from, long to) throws TimestampException{
		isTimestampNegative(from);
		isTimestampNegative(to);
		if( from > to ) {
			log.error("Delta ist not possible: " + from + " - " + to);
			throw new TimestampException("From must not be later than to! (from: " + from + " to: " + to + ")");
		}
	}
	
	/**
	 * Returns the default Graphservice used in MetalyzerAPI.
	 * @param connectionName specifies an connection from the VisITMeta-connectionpool.
	 * @return
	 * @author Johannes Busch
	 * @throws MetalyzerAPIException Accours when no GraphService is available.
	 */
	public static GraphService getGraphService(String connectionName) throws MetalyzerAPIException {
		
		try {
			return ConnectionManager.getGraphServiceFromConnection(connectionName);
		} catch (ConnectionException e) {
			log.error("Error with: " + connectionName + " " + e.getMessage());
			throw new MetalyzerAPIException("Error while retrieving " + connectionName + "-Connection found! -> " + e.getMessage());
		}
	}
	
	/**
	 * Returns a ArrayList of all Timestamps between Timestamp from and to
	 * @param from Start timestamp   
	 * @param to End timestamp
	 * @return
	 * @author Sören Grzanna
	 */
	public static ArrayList<Long> getTimestamps(GraphService gs, long from, long to){
		Set<Long> allTimes = gs.getChangesMap().keySet();
		ArrayList<Long> times = new ArrayList<>();
		
		for (Long time : allTimes) {
			if( time > to ){
				break;
			}			
			
			if( time >= from ){
				times.add(time);
			}
		}
		
		return times;
	}
}
