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
package de.hshannover.f4.trust.metalyzer.interfaces;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.statistic.EdgeResult;
import de.hshannover.f4.trust.metalyzer.statistic.FrequencySelection;
import de.hshannover.f4.trust.metalyzer.statistic.FrequencyType;
import de.hshannover.f4.trust.metalyzer.statistic.MeanOfEdgeResult;
import de.hshannover.f4.trust.metalyzer.statistic.MeanType;
import de.hshannover.f4.trust.metalyzer.statistic.NodeResult;
import de.hshannover.f4.trust.metalyzer.statistic.ResultObject;

public interface StatisticService {
	
	/**
	 * Returns a mean of links with the given StandardIdentifierType to a current timestamp
	 * @param from
	 * @param to
	 * @param {@link StandardIdentifierType}
	 * @return mean
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public double getMeanCurrent(StandardIdentifierType filter, MeanType type) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Returns a mean of links with the given StandardIdentifierType at given timestamp
	 * @param from
	 * @param to
	 * @param {@link StandardIdentifierType}
	 * @return mean
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public double getMeanTimestamp(long timestamp, StandardIdentifierType filter, MeanType type) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Returns a mean of links with the given StandardIdentifierType at given timestamp interval from and to
	 * @param from
	 * @param to
	 * @param {@link StandardIdentifierType}
	 * @return mean
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public double getMeanFromTo(long from, long to, StandardIdentifierType filter, MeanType type) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Returns a Map with frequencies of metadatas to a current timestamp
	 * @param type
	 * @param selection
	 * @return {@link HashMap}
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public Map<String, Double> getFrequencyCurrent(FrequencyType type, FrequencySelection selection) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Returns a Map with frequencies of metadatas at given timestamp
	 * @param timestamp
	 * @param type
	 * @param selection
	 * @return {@link HashMap}
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public Map<String, Double> getFrequencyTimestamp(long timestamp, FrequencyType type, FrequencySelection selection) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Returns a Map with frequencies of metadatas at given timestamp interval from and to
	 * @param from
	 * @param to
	 * @param type
	 * @return {@link HashMap}
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public Map<String, Double> getFrequencyFromTo(long from, long to, FrequencyType type, FrequencySelection selection) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Returns the count of identifer at given timestamp interval from and to
	 * @param from
	 * @param to
	 * @return identiferCount
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public int getIdentifierCount(long from, long to) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Returns the count of identifer at given timestamp
	 * @param timestamp
	 * @return identiferCount
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public int getIdentifierCount(long timestamp) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Returns the count of identifer to a current timestamp
	 * @return identiferCount
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public int getIdentifierCount() throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Returns the count of metadata at given timestamp interval from and to
	 * @param from
	 * @param to
	 * @return metadataCount
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public int getMetadataCount(long from, long to) throws InterruptedException, ExecutionException, TimeoutException;
	
	
	/**
	 * Returns the count of metadata at given timestamp
	 * @param from
	 * @param to
	 * @return metadataCount
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public int getMetadataCount(long timestamp) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Returns the count of metadata to a current timestamp
	 * @param from
	 * @param to
	 * @return metadataCount
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public int getMetadataCount() throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Returns an {@link ResultObject} that holds information of nodes in this graph 
	 * @param from
	 * @param to
	 * @return ResultObject
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	 public ResultObject<NodeResult> getNodeCharecteristic(long from, long to)  throws InterruptedException, ExecutionException, TimeoutException;
	 
		/**
		 * Returns an {@link ResultObject} that holds information of edges in this graph 
		 * @param from
		 * @param to
		 * @return ResultObject
		 * @throws TimeoutException 
		 * @throws ExecutionException 
		 * @throws InterruptedException 
		 */
	 public ResultObject<EdgeResult> getEdgeCharecteristic(long from, long to)  throws InterruptedException, ExecutionException, TimeoutException;
	 
		/**
		 * Returns an {@link ResultObject} that holds information of the mean of edges in this graph 
		 * @param from
		 * @param to
		 * @return ResultObject
		 * @throws TimeoutException 
		 * @throws ExecutionException 
		 * @throws InterruptedException 
		 */
	 public ResultObject<MeanOfEdgeResult> getMeanOfEdgeCharecteristic(long from, long to) throws InterruptedException, ExecutionException, TimeoutException;

}
