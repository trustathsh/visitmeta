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
package de.hshannover.f4.trust.metalyzer.api.impl;


import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.api.DeltaReturner;
import de.hshannover.f4.trust.metalyzer.api.ListReturner;
import de.hshannover.f4.trust.metalyzer.api.MetalyzerTime;
import de.hshannover.f4.trust.metalyzer.api.helper.MetalyzerAPIHelper;

/**
 * Represents the current time.
 * Part of the FilterDSL around SimpleFilter.
 * @author Johannes Busch
 *
 */
public class MetalyzerTimeImpl implements MetalyzerTime {
	
	private static final Logger log = Logger.getLogger(MetalyzerTimeImpl.class);
	
	private MetalyzerFilterImpl filter;
	
	private boolean current;
	
	private long from, to;
	
	public MetalyzerTimeImpl(MetalyzerFilterImpl filter) {
		if(null == filter) {
			log.error("Filter should not be null!");
			throw new RuntimeException("Filer should not be null");
		}
		this.filter = filter;
	}

	@Override
	public ListReturner atCurrent() {
		from = 0;
		to = 0;
		current = true;
		return filter;
	}
	
	public boolean isCurrent() {
		return (current == true && from == 0 && to == 0);
	}

	@Override
	public ListReturner at(long timestamp) {
		MetalyzerAPIHelper.isTimestampNegative(timestamp);
		
		current = false;
		to =  0;
		from = timestamp;
		return filter;
	}
	
	public boolean isAt() {
		return (current == false && from > 0 && to == 0);
	}

	public long getAtTimestampt() {
		return from;
	}

	@Override
	public DeltaReturner atDelta(long from, long to) {
		MetalyzerAPIHelper.isDeltaPossible(from, to);
		
		current = false;
		this.from = from;
		this.to = to;
		return filter;
	}

	public boolean isDelta() {
		return (current == false && from != 0 && to != 0);
	}
	
	public long[] getDelta() {
		long[] delta = {from, to};
		
		return delta;
	}
}
