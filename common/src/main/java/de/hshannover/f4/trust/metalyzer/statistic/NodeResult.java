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
 * This file is part of visitmeta common, version 0.1.2,
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
package de.hshannover.f4.trust.metalyzer.statistic;

/**
 * @author hassannahle
 * a result object for node queries, which extends from the EvaluationResult class.
 * This result object is used to retrieve information about node queries.
 */
public class NodeResult extends EvaluationResult {
	
	private long numbersOfIdentifer;

	/**
	 * @param timestamp a timestamp from the timeinterval
	 * @param deviation deviation from the numbersOfIdentifer value (of timestamp) to the mean value (of the timeinterval)
	 * @param numbersOfIdentifier number of nodes in the timestamp
	 */
	public NodeResult(long timestamp, double deviation, long numbersOfIdentifier) {
		super(timestamp, deviation);
		this.numbersOfIdentifer = numbersOfIdentifier;
		
	}
	
	public NodeResult() {
		
	}
	
	/**
	 * @return returns the number of nodes in the timestamp
	 */
	public long getNumbersOfIdentifer(){
		return numbersOfIdentifer;
	}
}
