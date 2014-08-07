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
 * This file is part of visitmeta visualization, version 0.1.1,
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
package de.hshannover.f4.trust.visitmeta.graphCalculator;





/**
 * Represents different types for the collocation (in other words: alignment or orientation) of the
 * metadata-nodes.
 */
public enum MetadataCollocation{

	/**
	 * NodeIdentifier2D or ExpandedLink2D do not show their metadata-nodes.
	 */
	SIMPLE,

	/**
	 * <p>For metadata-nodes of an NodeIdentifier2D: Every metadata-node has its own edge to his
	 * Identifier.</p>
	 * <p>For metadata-nodes of an ExpandedLink2D: Every metadata-node has two edges. One edge to
	 * his first Identifier and one edge to his second Identifier.</p>
	 */
	CHAIN,

	/**
	 * <p>
	 * For metadata-nodes of an NodeIdentifier2D: All metadata-nodes are arranged like pearls on a
	 * necklace. The first metadata-node is connected (via an edge) to the Identifier, the second
	 * metadata-node is conneced to the first metadata-node, the third metadata-node is connected to
	 * the second metadata-node etc.
	 * </p>
	 * <p>
	 * For metadata-nodes of a ExpandedLink2D: All metadata-nodes are arranged like pearls on a
	 * necklace. The first metadata-node is connected (via an edge) to the first Identifier, the
	 * second metadata-node is conneced to the first metadata-node, the third metadata-node is
	 * connected to the second metadata-node etc. The last metadata-node is connected to the second
	 * Identifier.
	 * </p>
	 */
	FORK,

	/**
	 * <p>For metadata-nodes of an NodeIdentifier2D: Like FORK but there are also edges between the
	 * metadata-nodes to hold them together.</p>
	 * <p>For metadata-nodes of a ExpandedLink2D: Like FORK but there are also edges between the
	 * metadata-nodes to hold them together.</p>
	 */
	FORK_LINKED
}
