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
 * Copyright (C) 2012 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.interfaces;





import java.util.List;
import java.util.SortedMap;

/**
 * @author rosso
 * Client interface to the application offering high-level services.
 */
public interface GraphService {
	/**
	 * @return
	 * A {@ SortedMap} that maps timestamps to the number of changes that occured at that time
	 * in acscending order. <i>Note: Due to the nature of the IF-MAP protocol, this call is not
	 * idempotent! Called at different points of time may yield varying results.</i>
	 */
	public SortedMap<Long, Long> getChangesMap();
	/**
	 * @return
	 * The oldest graph known in the application. I.e. the first update the application received
	 * from the MAP server.
	 */
	public List<IdentifierGraph> getInitialGraph();
	/**
	 * @param timestamp
	 * @return
	 * State of the IF-MAP graph structure are time <tt>timestamp</tt>. <i>Note: Due to the nature
	 * of the IF-MAP protocol, this call is not idempotent! Called at different points of time may
	 * yield varying results.</i>
	 */
	public List<IdentifierGraph> getGraphAt(long timestamp);
	/**
	 * @return
	 * The graph at the current time, i.e. the graph that represents the latest changes in the
	 * IF-MAP graph structure.
	 */
	public List<IdentifierGraph> getCurrentGraph();
	/**
	 * @param filter
	 * @return
	 * The oldest graph known in the application. I.e. the first update the application received
	 * from the MAP server. Filtered according to the filter rules defined by the filter object.
	 */
	public List<IdentifierGraph> getInitialGraph(GraphFilter filter);
	/**
	 * @param timestamp
	 * @param filter
	 * @return
	 * State of the IF-MAP graph structure are time <tt>timestamp</tt>. <i>Note: Due to the nature
	 * of the IF-MAP protocol, this call is not idempotent! Called at different points of, it can
	 * yield varying results.</i>
	 * Filtered according to the filter object.
	 */
	public List<IdentifierGraph> getGraphAt(long timestamp, GraphFilter filter);
	/**
	 * @param filter
	 * @return
	 * The graph at the current time, i.e. the graph that represents the latest changes in the
	 * IF-MAP graph structure.
	 * Filtered according to the filter object.
	 */
	public List<IdentifierGraph> getCurrentGraph(GraphFilter filter);
	/**
	 * Calculates the changes in the IF-MAP graph structure that have occured between <tt>t1</tt>
	 * and <tt>t2</tt>.
	 * @param t1 Point of time one (unix epoch)
	 * @param t2 Point of time two (unix epoch)
	 * @return
	 * {@link Delta} object containing updates and deletes in seperate data structures.
	 */
	public Delta getDelta(long t1, long t2);
	
	/**
	 * Counts all current entities of the given type in the graph.
	 * @param type
	 * @return
	 */
	public long count(GraphType type);
	
	/**
	 * Counts all entities of the given type in the graph at the given timestamp.
	 * @param type
	 * @return
	 */
	public long count(GraphType type, long timestamp);
	
	/**
	 * Counts all entities of the given type in the graph in the given delta.
	 * @param type
	 * @param from
	 * @param to
	 * @return
	 */
	public long count(GraphType type, long from, long to);
	
	/**
	 * Returns the mean of edges in the graph.
	 * Beware: It does not count actual links. It counts metadata on a given link.
	 * 			If one link has several metadata, the link will be counted multiple times!
	 * @return
	 */
	public double meanOfEdges();
	
	/**
	 * Returns the mean of edges in the graph.
	* Beware: It does not count actual links. It counts metadata on a given link.
	 * 			If one link has several metadata, the link will be counted multiple times!
	 * @return
	 */
	public double meanOfEdges(long timestamp);
}
