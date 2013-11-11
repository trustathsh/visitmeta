package de.fhhannover.inform.trust.visitmeta.graphCalculator;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
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
 * This file is part of VisITMeta, version 0.0.2, implemented by the Trust@FHH 
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@FHH
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

import java.util.Observer;

import de.fhhannover.inform.trust.visitmeta.datawrapper.ExpandedLink;
import de.fhhannover.inform.trust.visitmeta.datawrapper.NodeIdentifier;
import de.fhhannover.inform.trust.visitmeta.datawrapper.Position;
import de.fhhannover.inform.trust.visitmeta.datawrapper.UpdateContainer;
import de.fhhannover.inform.trust.visitmeta.graphCalculator.jung.LayoutType;
import de.fhhannover.inform.trust.visitmeta.graphCalculator.jung.MetadataCollocation;

/**
 * Interface for a graph calculator.
 */
public interface Calculator extends Observer {

	/**
	 * Calculate the positions for the nodes of the graph.
	 */
	public void calculatePositions();

	/**
	 * Add nodes and links to the graph.
	 * Mark nodes and links which should be removed.
	 * @param update the container with the changes.
	 */
	public void addRemoveNodesLinksMetadatas(UpdateContainer uc);

	/**
	 * Remove a node from the graph.
	 * @param node the node.
	 */
	public void deleteNode(Position node);

	/**
	 * Remove an ExpandedLink from the graph.
	 * @param expandedLink ExpandedLink to remove
	 */
	public void deleteLink(ExpandedLink expandedLink);

	/**
	 * Remove all Elements from graph.
	 */
	public void clearGraph();

	/**
	 * Set the position of a node.
	 * @param p the node.
	 * @param x the position in x.
	 * @param y the position in y.
	 * @param z the position in z.
	 */
	public void updateNode(Position p, double x, double y, double z);

	/**
	 * Adjusts the graph anew.
	 * @param iterations The number of iterations the graph adjust its nodes.
	 */
	public void adjustGraphAnew(int iterations);

	/**
	 * Adjusts the whole graph.
	 * @param iterations The number of iterations the graph adjust its nodes.
	 */
	public void adjustAllNodes(int iterations);

	/**
	 * Adjust/align all nodes (Identifiers and Metadatas) anew.
	 * @param iterations Number of iterations
	 * @param checkPermission Check if a node to be adjusted has permission to adjust.
	 * @param checkPicking Check if a node to be adjusted is picked (removed) by user in
	 * 			graphical interface. If so, do not adjust this specific node.
	 */
	public void adjustAllNodes(int iterations, boolean checkPermission, boolean checkPicking);

	/**
	 * Adjusts all Identifier-Nodes (only the Identifier-Nodes).
	 * @param iterations The number of iterations the graph adjust its Identifier-Nodes.
	 */
	public void adjustIdentifierNodes(int iterations);

	/**
	 * Adjusts all Metadata-Nodes (only the Metadata-Nodes).
	 * @param iterations The number of iterations the graph adjust its Metadata-Nodes.
	 */
	public void adjustMetadataNodes(int iterations);

	/**
	 * Adjusts all new Nodes (only the new Nodes).
	 * @param iterations The number of iterations the graph adjust its new added Nodes.
	 */
	public void adjustNewNodes(int iterations);

	/**
	 * Adjusts the graph by a strategy implemented in Graph2D.
	 * @param iterations The number of iterations the graph will be adjusted.
	 */
	public void adjustByStrategy(int iterations);

	/**
	 * Alters the MetadataCollocation of the given NodeIdentifier.
	 * @param ni NodeIdentifier which MetadataCollocation should be altered.
	 * @param mc How the Metadata-Nodes should be arranged.
	 */
	public void alterLevelOfDetail(NodeIdentifier ni, MetadataCollocation mc);

	/**
	 * Alters the MetadataCollocation of the given ExpandedLink.
	 * @param el ExpandedLink which MetadataCollocation should be altered.
	 * @param mc How the Metadata-Nodes should be arranged.
	 */
	public void alterLevelOfDetail(ExpandedLink el, MetadataCollocation mc);

	/**
	 * Alters the MetadataCollocation for all NodeIdentifiers.
	 * @param mc How the Metadata-Nodes should be arranged.
	 */
	public void alterLevelOfDetailForAllIdentifiers(MetadataCollocation mc);

	/**
	 * Alters the MetadataCollocation for all ExpandedLinks.
	 * @param mc How the Metadata-Nodes should be arranged.
	 */
	public void alterLevelOfDetailForAllExpandedLinks(MetadataCollocation mc);

	/**
	 * Alters the MetadataCollocation for all NodeIdentifiers and all ExpandedLinks.
	 * @param mc How the Metadata-Nodes should be arranged.
	 */
	public void alterLevelOfDetailForEntireGraph(MetadataCollocation mc);

	/**
	 * Set the number of iterations the graph does in one step.
	 * @param iterations Numer of iterations.
	 */
	public void setIterations(int iterations);

	/**
	 * Set layout (force-directed- or spring-layout) with default parameters.
	 * @param layoutType
	 */
	public void setLayout(LayoutType layoutType);

	/**
	 * Set layout (force-directed) with user selected parameters.
	 * @param attractionMultiplier How much edges try to keep their nodes together.
	 * @param repulsionMultiplier How much nodes try to push each other apart.
	 */
	public void setLayoutForceDirectd(double attractionMultiplier, double repulsionMultiplier);

	/**
	 * Set layout (spring).
	 * @param useIndividualEdgeLength true if every edge has to have its individual length
	 * @param dimensionX Dimension of provided space for graph in x-dimension
	 * @param dimensionY Dimension of provided space for graph in y-dimension
	 * @param stretch The tendency of an edge to change its length
	 * @param forceMultiplier How strongly an edge tries to maintain its length
	 * @param repulsionRange Outside this radius, nodes do not repel each other
	 */
	public void setLayoutSpring(boolean useIndividualEdgeLength, int dimensionX,
			int dimensionY, double stretch, double forceMultiplier, int repulsionRange);


}
