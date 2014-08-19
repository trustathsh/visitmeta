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

import java.util.ArrayList;

/**
 * @author hassannahle
 * @param <T> mean the resulttype of a query (for example: NodeResult)
 * The ResultObject includes information about a query in a given timeinterval.
 * It includes general statistical information about the timeinterval (mean, standardDeviation) and some informations about the 
 * timestamp*/

public class ResultObject<T> {

	private ArrayList<T> resultList;
	private double mean;
	private double standardDeviation;
	
	/**
	 * 
	 * @param mean describes the global mean over a timeinterval
	 * @param standardDeviation describes the global standardDeviation over a timeinterval
	 * @param evaluationResult a list of results for various timestamps in a timeinterval (for example: NodeResult)
	 */
	public ResultObject(double mean, double standardDeviation, ArrayList<T> evaluationResult){
		this.mean = mean;
		this.resultList = evaluationResult;
		this.standardDeviation = standardDeviation;
	}
	
	public ResultObject(){
		
	}
	
	/**
	 * @return returns the global mean value of a timeinterval
	 */
	public double getMean(){
		return mean;
	}
	
	/**
	 * @return returns the global standard deviation value of a timeinterval
	 */
	public double getStandardDeviation(){
		return standardDeviation;
	}
	
	/**
	 * @return returns a list of results for various timestamps in a timeinterval (for example: NodeResult)
	 */
	public ArrayList<T> getResultList(){
		return resultList;
	}
}
