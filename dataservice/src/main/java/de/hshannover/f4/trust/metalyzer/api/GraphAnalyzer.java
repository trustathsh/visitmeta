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
 * Interface fuer die GraphAnalyzerImpl Klasse. 
 * Bildet Schnittstelle zu den weiteren Metalyzer-Komponenten.
 * 
 * Copyright (c): Hochschule Hannover
 */
package de.hshannover.f4.trust.metalyzer.api;

/**
 * GraphAnalyzer has methods to analyze the given IF-MAP graph.
 * Analyze means in this case in a mathematical way and not in
 * a IF-MAP way.
 * @author Johannes Busch
 *
 */
public interface GraphAnalyzer {
	
	/**
	 * Returns the number of counted nodes in the graph.
	 * Nodes are in this case identifiers.
	 * @return
	 */
	public long countNodes();
	
	/**
	 * Returns the number of counted nodes in the graph at the given timestamp.
	 * Nodes are in this case identifiers.
	 * @return
	 */
	public long countNodesAt(long timestamp);
	
	/**
	 * Returns the number of counted nodes in the graph in the given delta.
	 * Nodes are in this case identifiers.
	 * @param from
	 * @param to
	 * @return
	 */
	public long countNodes(long from, long to);
	
	/**
	 * Returns the number of counted edges in the graph.
	 * Edges are in this case relationships between identifiers.
	 * @return
	 */
	public long countEdges();
	
	/**
	 * Returns the number of counted edges in the graph at the given timestamp.
	 * Edges are in this case relationships between identifiers.
	 * @return
	 */
	public long countEdgesAt(long timestamp);
	
	/**
	 * Returns the number of counted edges in the graph in the given delta.
	 * Edges are in this case relationships between identifiers. 
	 * @param from
	 * @param to
	 * @return
	 */
	public long countEdges(long from, long to);
	
	/**
	 * Returns the mean of edges in the graph.
	 * Beware: It does not count actual links. It counts metadata on a given link.
	 * 			If one link has several metadata, the link will be counted multiple times!
	 * @return
	 */
	public double getMeanOfEdges();
	
	/**
	 * Returns the mean of metadata in the graph at the given timestamp.
	 * Beware: It does not count actual links. It counts metadata on a given link.
	 * 			If one link has several metadata, the link will be counted multiple times!
	 * @return
	 */
	public double getMeanOfEdges(long timestamp);
}
