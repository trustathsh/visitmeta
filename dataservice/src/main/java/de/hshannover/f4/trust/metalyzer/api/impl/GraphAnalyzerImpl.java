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
 * Copyright (c): Hochschule Hannover
 */
package de.hshannover.f4.trust.metalyzer.api.impl;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.api.GraphAnalyzer;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphType;

/**
 * Impl class which implements all necessary methods of the
 * GraphAnalyzer interface. Has the connection to the database ( if-map graph )
 * through the GraphService.
 * 
 * @author Johannes Busch
 *
 */
public class GraphAnalyzerImpl implements GraphAnalyzer {
	
	private static final Logger log = Logger.getLogger(GraphAnalyzerImpl.class);
	
	private GraphService mService;
	
	/**
	 * service must not be null.
	 * @param service if-map connection
	 */
	public GraphAnalyzerImpl(GraphService service) {
		mService = service;
	}
	
	/**
	 * Counts all current nodes ( identifier ) in the graph.
	 */
	@Override
	public long countNodes() {
		return mService.count(GraphType.IDENTIFIER);
	}
	
	/**
	 * Counts all nodes ( identifier ) in the graph at the given timestamp.
	 * Timestamp must not be negative!
	 */
	@Override
	public long countNodesAt(long timestamp) {
		return mService.count(GraphType.IDENTIFIER, timestamp);
	}
	
	/**
	 * Counts all nodes ( identifier ) in the graph in the given time delta.
	 * Timestamps must not be negative!
	 */
	@Override
	public long countNodes(long from, long to) {
		return mService.count(GraphType.IDENTIFIER, from, to);
	}
	
	/**
	 * Counts all current edges ( relationships ) in the graph.
	 */
	@Override
	public long countEdges() {
		return mService.count(GraphType.LINK);
	}
	
	/**
	 * Counts all edges ( relationships ) in the graph at the given timestamp.
	 * Timestamp must not be negative!
	 */
	@Override
	public long countEdgesAt(long timestamp) {
		return mService.count(GraphType.LINK, timestamp);
	}
	
	/**
	 * Counts all edges ( relationships ) in the graph in the given time delta.
	 * Timestamps must not be negative!
	 */
	@Override
	public long countEdges(long from, long to) {
		return mService.count(GraphType.LINK, from, to);
	}
	
	/**
	 * Returns the current mean of edges for nodes. Every metadata has its own edge.
	 */
	@Override
	public double getMeanOfEdges() {
		return mService.meanOfEdges();
	}

	/**
	 * Returns the mean of edges for nodes at the given timestamp. Every metadata has its own edge.
	 * Timestamp must not be negative!
	 */
	@Override
	public double getMeanOfEdges(long timestamp) {
		return mService.meanOfEdges(timestamp);
	}
}
