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
 * Last Change: 
 * 		by: 	$Author: $
 * 		date: 	$Date: $ 
 * Copyright (c): Hochschule Hannover
 */
package de.hshannover.f4.trust.metalyzer.api.helper;

import java.util.ArrayList;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.DeltaImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

/**
 * Builds a new delta for the given time-delta.
 * Class is needed because visitmeta delta and
 * metalyzer delta are defined in diferrend ways.
 * 
 * Visitmeta delta just does a graph substraction of the two graphs
 * specified by the timestamps.
 * 
 * Metalyzer delta also contains all changes within the given time
 * delta.
 * 
 * @author Sören Grzanna
 *
 */
public class DeltaBuilder {
	
	/**
	 * Returns a new Delta with all the changes within the given delta.
	 * 
	 * @param gs if-map connection against the request will be processed.
	 * @param from start timestamp
	 * @param to end timestamp
	 * @return
	 */
	public static Delta get(GraphService gs, long from, long to){
		ArrayList<Long> times = MetalyzerAPIHelper.getTimestamps(gs, from, to);
		ArrayList<IdentifierGraph> deletes = new ArrayList<>();
		ArrayList<IdentifierGraph> updates = new ArrayList<>();
		
		if( times.size() <= 1 ){
			return new DeltaImpl(deletes, updates);
		}
		
		int index = 0;
		do{
			Delta delta = gs.getDelta( times.get(index) , times.get(index+1) );
			deletes.addAll( delta.getDeletes() );
			updates.addAll( delta.getUpdates() );
			index++;
		}while( index+1 < times.size() );

		return new DeltaImpl(deletes, updates);
	}
}
