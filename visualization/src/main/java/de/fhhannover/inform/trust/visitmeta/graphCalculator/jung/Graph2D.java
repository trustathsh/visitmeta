package de.fhhannover.inform.trust.visitmeta.graphCalculator.jung;

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
 * This file is part of VisITMeta, version 0.0.1, implemented by the Trust@FHH 
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

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.log4j.Logger;
import de.fhhannover.inform.trust.visitmeta.datawrapper.ExpandedLink;
import de.fhhannover.inform.trust.visitmeta.datawrapper.NodeIdentifier;
import de.fhhannover.inform.trust.visitmeta.datawrapper.NodeMetadata;
import de.fhhannover.inform.trust.visitmeta.datawrapper.UpdateContainer;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;



/**
 * <p>Main-class to initialize the graph-calculation. It also operates as a facade to
 * the graph-calculation.</p>
 */
public class Graph2D{

	// ///////////////////////////////////////////////////////////////////////////////////// MEMBERS

	private Graph<Node2D, Edge2D> mGraph;
	private Layout2D mLayout2D;

	private Map<NodeIdentifier, NodeIdentifier2D> mNodeIdentifierMap;
	private Map<ExpandedLink, ExpandedLink2D>     mExpandedLinkMap;
	private Map<NodeMetadata, NodeMetadata2D>     mNodeMetadataMap;

	private List<Node2D> mNewNodes2D;

	private List<NodeIdentifier> mNodeIdentifiersToRemove;
	private List<NodeMetadata>   mNodeMetadatasToRemove;
	private List<ExpandedLink>   mExpandedLinksToRemove;

	private MetadataCollocation mMetaCollocationLink;
	private MetadataCollocation mMetaCollocationIdentifier;

	private LayoutType mLayoutType;

	private static final Logger LOGGER = Logger.getLogger(Graph2D.class);

	private int mRedrawCounter;
	private int mStrategyCounter;
	private boolean mNewNodesAdjusted;

	private int mIterations;



	// //////////////////////////////////////////////////////////////////////////////// CONSTRUCTORS

	public Graph2D(MetadataCollocation metaCollocationLink,
			MetadataCollocation metaCollocationIdentifier){

		mGraph = new UndirectedSparseGraph<>();

		mNodeIdentifierMap = new HashMap<>();
		mExpandedLinkMap   = new HashMap<>();
		mNodeMetadataMap   = new HashMap<>();

		mNewNodes2D = new ArrayList<>();

		mNodeIdentifiersToRemove = new ArrayList<>();
		mNodeMetadatasToRemove   = new ArrayList<>();
		mExpandedLinksToRemove   = new ArrayList<>();

		mMetaCollocationLink = metaCollocationLink;
		mMetaCollocationIdentifier = metaCollocationIdentifier;

		mRedrawCounter = 75;
		mStrategyCounter = 0;
		mNewNodesAdjusted = true;

		mIterations = 100;
	}



	// ///////////////////////////////////////////////////////////// PUBLIC - MAIN MODIFYING METHODS


	/**
	 * Main update method.
	 * Add and remove Elements (Identifier, Metadatas and Links)
	 * Actually this method only adds Elements to the graph and MARKS Elements to be removed.
	 * If you decide to remove these Elements, call removeElementsFinally().
	 * @param uc UpdateContainer containing all Elements to be added and to be removed.
	 */
	public void addRemoveNodesLinksMetadatas(UpdateContainer uc){
		LOGGER.trace("Method addRemoveNodesLinksMetadatas(" + uc + ") called.");

		mNewNodes2D.clear();
		mNewNodesAdjusted = false;

		for(NodeIdentifier id : uc.getListAddIdentifier()){
			addNodeIdentifier(id);
		}

		for(ExpandedLink link : uc.getListAddLinks()){
			addExpandedLink(link);
		}

		Set<Entry<NodeIdentifier, List<NodeMetadata>>> idMetas =
			uc.getListAddMetadataIdentifier().entrySet();
		for(Entry<NodeIdentifier, List<NodeMetadata>> entry : idMetas){
			for(NodeMetadata meta : entry.getValue()){
				addNodeMetadata(meta, entry.getKey());
			}
		}

		Set<Entry<ExpandedLink, List<NodeMetadata>>> linkMetas =
			uc.getListAddMetadataLinks().entrySet();
		for(Entry<ExpandedLink, List<NodeMetadata>> entry : linkMetas){
			for(NodeMetadata meta : entry.getValue()){
				addNodeMetadata(meta, entry.getKey());
			}
		}

		collectElementsToRemove(uc);

		if(!mLayout2D.useIndividualEdgeLength()){
			mLayout2D.calculateUniformEdgeLength();
			for(Edge2D edge : mGraph.getEdges()){
				edge.setLength(mLayout2D.getUniformEdgeLength());
			}
		}

		mLayout2D.reset();

		adjustAllNodes(mIterations, false, true);
	}


	public void deleteNode(NodeIdentifier nodeIdentifier){
		removeNodeIdentifier(nodeIdentifier);
	}

	public void deleteNode(NodeMetadata nodeMetadata){
		removeNodeMetadata(nodeMetadata);
	}

	public void deleteLink(ExpandedLink expandedLink){
		removeExpandedLink(expandedLink);
	}


	/**
	 * Set layout (force-directed- or spring-layout) with default parameters.
	 * @param layoutType
	 */
	public void setLayout(LayoutType layoutType){
		LOGGER.trace("Method setLayout(" + layoutType + ") called.");
		mLayoutType = layoutType;
		if(layoutType == LayoutType.FORCE_DIRECTED){
			mLayout2D = new LayoutForceDirected2D(this);
		}
		else if(layoutType == LayoutType.SPRING){
			mLayout2D = new LayoutSpring2D(this);
		}
	}


	/**
	 * Set layout (force-directed).
	 * @param attractionMultiplier How much edges try to keep their nodes together.
	 * @param repulsionMultiplier How much nodes try to push each other apart.
	 */
	public void setLayoutForceDirectd(double attractionMultiplier, double repulsionMultiplier){
		mLayoutType = LayoutType.FORCE_DIRECTED;
		mLayout2D = new LayoutForceDirected2D(this, attractionMultiplier, repulsionMultiplier);
	}


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
			int dimensionY, double stretch, double forceMultiplier, int repulsionRange){
		mLayoutType = LayoutType.SPRING;
		mLayout2D = new LayoutSpring2D(this, useIndividualEdgeLength, dimensionX, dimensionY,
				stretch, forceMultiplier, repulsionRange);
	}


	/**
	 * Remove all Elements from graph.
	 * Set graph null.
	 */
	public void clearGraph(){
		LOGGER.trace("Method clearGraph() called.");

		removeAllElements();

		mNodeIdentifierMap.clear();
		mExpandedLinkMap.clear();
		mNodeMetadataMap.clear();
		mNewNodes2D.clear();
		setLayout(mLayoutType);
	}


	/**
	 * Remove all tagged Elements (Identifier, Metadatas and Links).
	 * Call this method after calling addRemoveNodesLinksMetadatas(UpdateContainer uc)
	 */
	@Deprecated
	public void removeTaggedElementsFinally(){
		LOGGER.trace("Method removeElementsFinally() called.");
		for(NodeMetadata meta : mNodeMetadatasToRemove){
			removeNodeMetadata(meta);
		}

		for(ExpandedLink link : mExpandedLinksToRemove){
			removeExpandedLink(link);
		}

		for(NodeIdentifier id : mNodeIdentifiersToRemove){
			removeNodeIdentifier(id);
		}

		mNodeIdentifiersToRemove.clear();
		mNodeMetadatasToRemove.clear();
		mExpandedLinksToRemove.clear();

		mLayout2D.reset();
	}


	public void setIterations(int iterations){
		mIterations = iterations;
	}



	// /////////////////////////////////////////////////////////////// PUBLIC - ADJUST GRAPH METHODS


	/**
	 * Adjust/align graph anew.
	 * @param iterations Number of iterations
	 */
	public void adjustGraphAnew(int iterations){
		LOGGER.trace("Method adjustGraphAnew(" + iterations + ") called.");

		mRedrawCounter = 0;

		MinBoundingBox2D minBox = new MinBoundingBox2D(this);
		double xCenter = minBox.getCenterX();
		double yCenter = minBox.getCenterY();

		for(Entry<NodeIdentifier, NodeIdentifier2D> id : mNodeIdentifierMap.entrySet()){
			id.getValue().setAdjustPermission(true);
			id.getValue().setPicked(false);
			id.getValue().setPositionTriggeredByPiccolo(xCenter, yCenter);
		}
		for(Entry<NodeMetadata, NodeMetadata2D> meta : mNodeMetadataMap.entrySet()){
			meta.getValue().setAdjustPermission(true);
			meta.getValue().setPicked(false);
			meta.getValue().setPositionTriggeredByPiccolo(xCenter, yCenter);
		}

		adjustAllNodes(iterations, false, true);

		mLayout2D.reset();
	}


	/**
	 * Adjust/align all nodes (Identifiers and Metadatas) anew.
	 * @param iterations Number of iterations
	 */
	public void adjustAllNodes(int iterations){
		LOGGER.trace("Method adjustAllNodes(" + iterations + ") called.");

		if(mLayout2D instanceof LayoutForceDirected2D && mRedrawCounter < 75){
			((LayoutForceDirected2D)mLayout2D).setNodeRepulsionTemporary(mRedrawCounter);
			mRedrawCounter = mRedrawCounter + 5;
		}
		else if(mLayout2D instanceof LayoutForceDirected2D && mRedrawCounter == 75){
			((LayoutForceDirected2D)mLayout2D).setOldAttractionRepulsion();
			mRedrawCounter = mRedrawCounter + 5;
		}

		mLayout2D.unlockAllNodes();

		for(Entry<NodeIdentifier, NodeIdentifier2D> id : mNodeIdentifierMap.entrySet()){
			if(!id.getValue().hasAdjustPermission()){
				mLayout2D.lockNode(id.getValue());
			}
		}
		for(Entry<NodeMetadata, NodeMetadata2D> meta : mNodeMetadataMap.entrySet()){
			if(!meta.getValue().hasAdjustPermission()){
				mLayout2D.lockNode(meta.getValue());
			}
		}

		mLayout2D.adjust(iterations);
		mLayout2D.lockAllNodes();

		for(Entry<NodeIdentifier, NodeIdentifier2D> id : mNodeIdentifierMap.entrySet()){
			id.getValue().setNodePositionInPiccolo();
		}
		for(Entry<NodeMetadata, NodeMetadata2D> meta : mNodeMetadataMap.entrySet()){
			meta.getValue().setNodePositionInPiccolo();
		}

		mLayout2D.reset();
	}



	/**
	 * Adjust/align all nodes (Identifiers and Metadatas) anew.
	 * @param iterations Number of iterations
	 * @param checkPermission Check if a node to be adjusted has permission to adjust.
	 * @param pinPickedNodes Check if a node to be adjusted is picked (removed) by user in
	 * 			graphical interface. If so, do not adjust this specific node.
	 */
	public void adjustAllNodes(int iterations, boolean checkPermission, boolean pinPickedNodes){
		LOGGER.trace("Method adjustAllNodes(" + iterations + ", " + checkPermission + ", " +
				pinPickedNodes + ") called.");

		if(mLayout2D instanceof LayoutForceDirected2D && mRedrawCounter < 75){
			((LayoutForceDirected2D)mLayout2D).setNodeRepulsionTemporary(mRedrawCounter);
			mRedrawCounter = mRedrawCounter + 5;
		}
		else if(mLayout2D instanceof LayoutForceDirected2D && mRedrawCounter == 75){
			((LayoutForceDirected2D)mLayout2D).setOldAttractionRepulsion();
			mRedrawCounter = mRedrawCounter + 5;
		}

		mLayout2D.unlockAllNodes();

		for(Entry<NodeIdentifier, NodeIdentifier2D> id : mNodeIdentifierMap.entrySet()){
			if(checkPermission && !id.getValue().hasAdjustPermission()){
				mLayout2D.lockNode(id.getValue());
			}
			else if(pinPickedNodes && id.getValue().wasPicked()){
				mLayout2D.lockNode(id.getValue());
			}
		}
		for(Entry<NodeMetadata, NodeMetadata2D> meta : mNodeMetadataMap.entrySet()){
			if(checkPermission && !meta.getValue().hasAdjustPermission()){
				mLayout2D.lockNode(meta.getValue());
			}
			else if(pinPickedNodes && meta.getValue().wasPicked()){
				mLayout2D.lockNode(meta.getValue());
			}
		}

		mLayout2D.adjust(iterations);
		mLayout2D.lockAllNodes();

		for(Entry<NodeIdentifier, NodeIdentifier2D> id : mNodeIdentifierMap.entrySet()){
			id.getValue().setNodePositionInPiccolo();
		}
		for(Entry<NodeMetadata, NodeMetadata2D> meta : mNodeMetadataMap.entrySet()){
			meta.getValue().setNodePositionInPiccolo();
		}

		mLayout2D.reset();
	}


	/**
	 * Adjust/align only IdentifierNodes anew.
	 * @param iterations Number of iterations
	 */
	public void adjustIdentifierNodes(int iterations){
		LOGGER.trace("Method adjustIdentifierNodes(" + iterations + ") called.");
		mLayout2D.lockAllNodes();
		for(Entry<NodeIdentifier, NodeIdentifier2D> id : mNodeIdentifierMap.entrySet()){
			mLayout2D.unlockNode(id.getValue());
		}
		mLayout2D.adjust(iterations);
		mLayout2D.lockAllNodes();

		for(Entry<NodeIdentifier, NodeIdentifier2D> id : mNodeIdentifierMap.entrySet()){
			id.getValue().setNodePositionInPiccolo();
		}

		mLayout2D.reset();
	}


	/**
	 * Adjust/align only MetadataNodes anew.
	 * @param iterations Number of iterations
	 */
	public void adjustMetadataNodes(int iterations){
		LOGGER.trace("Method adjustMetadataNodes(" + iterations + ") called.");
		mLayout2D.lockAllNodes();

		for(Entry<NodeMetadata, NodeMetadata2D> meta : mNodeMetadataMap.entrySet()){
			mLayout2D.unlockNode(meta.getValue());
		}
		mLayout2D.adjust(iterations);
		mLayout2D.lockAllNodes();

		for(Entry<NodeMetadata, NodeMetadata2D> meta : mNodeMetadataMap.entrySet()){
			meta.getValue().setNodePositionInPiccolo();
		}

		mLayout2D.reset();
	}


	/**
	 * Adjust/align only new nodes anew.
	 * @param iterations Number of iterations
	 */
	public void adjustNewNodes(int iterations){
		LOGGER.trace("Method adjustNewNodes(" + iterations + ") called.");
		mLayout2D.lockAllNodes();
		for(Node2D node : mNewNodes2D){
			mLayout2D.unlockNode(node);
		}
		mLayout2D.adjust(iterations);
		mLayout2D.lockAllNodes();
		for(Node2D node2D : mNewNodes2D){
			node2D.setNodePositionInPiccolo();
		}
		mNewNodesAdjusted = true;

		mLayout2D.reset();
	}






	/**
	 * Adjust/align graph by a defined strategy.
	 * @param iterations Number of iterations
	 */
	public void adjustByStrategy(int iterations){
		LOGGER.trace("Method adjustByStrategy(" + iterations + ") called.");
		mStrategyCounter++;

		if(!mNewNodesAdjusted){
			adjustNewNodes(iterations);
		}
		if(mStrategyCounter < 3){
			adjustIdentifierNodes(iterations*100);
		}
		else{
			adjustAllNodes(iterations*100);
		}


		if(mStrategyCounter < 20){
			adjustAllNodes(iterations*100);
		}
		else{
			adjustMetadataNodes(iterations*100);
		}

		mLayout2D.reset();
	}


	/**
	 * Adjust/align an Identifiers and its Metadatas after the Identifier was picked/moved
	 * by the user in the graphical interface.
	 * @param iterations Number of iterations
	 */
	public void adjustAfterPickingNode(NodeIdentifier nodeId, int iterations){
		LOGGER.trace("Method adjustAfterPickingNode(" + iterations + "," + iterations + ") called.");
		if(!mNodeIdentifierMap.containsKey(nodeId)){
			return;
		}

		mLayout2D.lockAllNodes();
		NodeIdentifier2D node2D = mNodeIdentifierMap.get(nodeId);
		for(NodeMetadata2D nm : node2D.getNodesMetadata2D()){
			if(nm.hasAdjustPermission()){
				mLayout2D.unlockNode(nm);
			}
		}
		for(ExpandedLink2D el : node2D.getExpandedLinks2D()){
			for(NodeMetadata2D nm : el.getNodesMetadata2D()){
				if(nm.hasAdjustPermission()){
					mLayout2D.unlockNode(nm);
				}
			}
		}
		mLayout2D.adjust(iterations);
		mLayout2D.lockAllNodes();
		for(NodeMetadata2D nm : node2D.getNodesMetadata2D()){
			nm.setNodePositionInPiccolo();
		}
		for(ExpandedLink2D el : node2D.getExpandedLinks2D()){
			for(NodeMetadata2D nm : el.getNodesMetadata2D()){
				nm.setNodePositionInPiccolo();
			}
		}

		mLayout2D.reset();
	}


	/**
	 * Adjust/align an Identifiers and its Metadatas after the Identifier was picked/moved
	 * by the user in the graphical interface.
	 * @param iterations Number of iterations
	 */
	public void adjustAfterPickingNode(NodeMetadata nodeMeta, int iterations){
		LOGGER.trace("Method adjustAfterPickingNode(" + nodeMeta + "," + iterations + ") called.");
		if(!mNodeIdentifierMap.containsKey(nodeMeta)){
			return;
		}

		mLayout2D.lockAllNodes();

		NodeMetadata2D nodeMeta2D = mNodeMetadataMap.get(nodeMeta);
		NodeIdentifier2D nodeId2D = nodeMeta2D.getNodeIdentifier2D();
		ExpandedLink2D exLink2D   = nodeMeta2D.getExpandedLink2D();

		if(nodeId2D != null){
			for(NodeMetadata2D nm : nodeId2D.getNodesMetadata2D()){
				mLayout2D.unlockNode(nm);
			}
		}
		else if(exLink2D != null){
			for(NodeMetadata2D nm : exLink2D.getNodesMetadata2D()){
				mLayout2D.unlockNode(nm);
			}
		}

		mLayout2D.lockNode(nodeMeta2D);
		mLayout2D.adjust(iterations);
		mLayout2D.lockAllNodes();

		if(nodeId2D != null){
			for(NodeMetadata2D nm : nodeId2D.getNodesMetadata2D()){
				nm.setNodePositionInPiccolo();
			}
		}
		else if(exLink2D != null){
			for(NodeMetadata2D nm : exLink2D.getNodesMetadata2D()){
				nm.setNodePositionInPiccolo();
			}
		}

		mLayout2D.reset();
	}





	// ////////////////////////////////////////////////////////////////////////////////////// PUBLIC

	/**
	 * Alter the collocation/alignmant of the MetadataNodes of an specific Identifier
	 * @param ni Identifier whose Metadatas should be aligned
	 * @param mc How should the MetadataNodes be aligned
	 */
	public void alterMetadataCollocation(NodeIdentifier ni, MetadataCollocation mc){
		LOGGER.trace("Method alterMetadataCollocation(" + ni + "," + mc + ") called.");
		if(mNodeIdentifierMap.containsKey(ni)){
			mNodeIdentifierMap.get(ni).changeMetadataCollocation(mc);
		}
	}

	/**
	 * Alter the collocation/alignmant of the MetadataNodes of an specific Link
	 * @param el Link whose Metadatas should be aligned
	 * @param mc How should the MetadataNodes be aligned
	 */
	public void alterMetadataCollocation(ExpandedLink el, MetadataCollocation mc){
		LOGGER.trace("Method alterMetadataCollocation(" + el + "," + mc + ") called.");
		if(mExpandedLinkMap.containsKey(el)){
			mExpandedLinkMap.get(el).changeMetadataCollocation(mc);
		}
	}

	/**
	 * Alter the collocation/alignmant of the MetadataNodes of all Identifiers
	 * @param mc How should the MetadataNodes be aligned
	 */
	public void alterMetadataCollocationForAllIdentifiers(MetadataCollocation mc){
		LOGGER.trace("Method alterMetadataCollocationForAllIdentifiers(" + mc + ") called.");
		for(Entry<NodeIdentifier, NodeIdentifier2D> id : mNodeIdentifierMap.entrySet()){
			id.getValue().changeMetadataCollocation(mc);
		}
	}

	/**
	 * Alter the collocation/alignmant of the MetadataNodes of all Links
	 * @param mc How should the MetadataNodes be aligned
	 */
	public void alterMetadataCollocationForAllExpandedLinks(MetadataCollocation mc){
		LOGGER.trace("Method alterMetadataCollocationForAllExpandedLinks(" + mc + ") called.");
		for(Entry<ExpandedLink, ExpandedLink2D> link : mExpandedLinkMap.entrySet()){
			link.getValue().changeMetadataCollocation(mc);
		}
	}

	/**
	 * Alter the collocation/alignmant of the MetadataNodes of all Links and Identifiers
	 * @param mc How should the MetadataNodes be aligned
	 */
	public void alterMetadataCollocationForEntireGraph(MetadataCollocation mc){
		LOGGER.trace("Method alterMetadataCollocationForEntireGraph(" + mc + ") called.");
		alterMetadataCollocationForAllIdentifiers(mc);
		alterMetadataCollocationForAllExpandedLinks(mc);
	}


	// ////////////////////////////////////////////////////////////////////////////////////// PUBLIC


	/**
	 * Called after a node was picked (moved) in the graphical interface.
	 * This method updates the position of a node (here: NodeIdentifier)
	 * in the graph-calculation-layer (JUNG2).
	 *
	 * @param nodeIdentifier Node which position should be updated in the graph-calculation-layer.
	 */
	public void updateNodePosition(NodeIdentifier nodeIdentifier, double xNew, double yNew){
		LOGGER.debug("Method updateNodePosition("+nodeIdentifier+", "+xNew+", "+xNew+") called.");
		if(mNodeIdentifierMap.containsKey(nodeIdentifier)){
			NodeIdentifier2D nodeId2D = mNodeIdentifierMap.get(nodeIdentifier);
			nodeId2D.setPicked(true);
			//mLayout2D.lockNode(nodeId2D);
			double xJungPrevious = nodeId2D.getX();
			double yJungPrevious = nodeId2D.getY();
			nodeId2D.setPositionTriggeredByPiccolo(xNew, yNew);
			double xJungNow = nodeId2D.getX();
			double yJungNow = nodeId2D.getY();
			double xOffset = xJungNow - xJungPrevious;
			double yOffset = yJungNow - yJungPrevious;
			for(NodeMetadata2D nm : nodeId2D.getNodesMetadata2D()){
				nm.setPositionTriggeredByJung(
					nm.getX() + xOffset,
					nm.getY() + yOffset
				);
			}
		}
	}

	/**
	 * Called after a node was picked (moved) in the graphical interface.
	 * This method updates the position of a node (here: NodeMetadata)
	 * in the graph-calculation-layer (JUNG2).
	 *
	 * @param nodeIdentifier Node which position should be updated in the graph-calculation-layer.
	 */
	public void updateNodePosition(NodeMetadata nodeMetadata, double xNew, double yNew){
		LOGGER.debug("Method updateNodePosition("+nodeMetadata+", "+xNew+", "+xNew+") called.");
		if(mNodeMetadataMap.containsKey(nodeMetadata)){
			NodeMetadata2D node2D = mNodeMetadataMap.get(nodeMetadata);
			node2D.setPicked(true);
			//mLayout2D.lockNode(node2D);
			node2D.setPositionTriggeredByPiccolo(xNew, yNew);
		}
	}






	// ///////////////////////////////////////////////////////// PRIVATE - ELEMENT MODIFYING METHODS



	/**
	 * Called after by addRemoveNodesLinksMetadatas(UpdateContainer uc) to tag all
	 * Elements (Identifier, Metadatas and Links) to be removed
	 * @param uc UpdateContainer containing all Elements to be added and to be removed.
	 */
	private void collectElementsToRemove(UpdateContainer uc){
		LOGGER.trace("Method collectElementsToRemove(" + uc + ") called.");
		Set<Entry<NodeIdentifier, List<NodeMetadata>>> idMetas =
			uc.getListDeleteMetadataIdentifier().entrySet();
		for(Entry<NodeIdentifier, List<NodeMetadata>> entry : idMetas){
			mNodeMetadatasToRemove.addAll(entry.getValue());
		}

		Set<Entry<ExpandedLink, List<NodeMetadata>>> linkMetas =
			uc.getListDeleteMetadataLinks().entrySet();
		for(Entry<ExpandedLink, List<NodeMetadata>> entry : linkMetas){
			mNodeMetadatasToRemove.addAll(entry.getValue());
		}

		mExpandedLinksToRemove.addAll(uc.getListDeleteLinks());

		mNodeIdentifiersToRemove.addAll(uc.getListDeleteIdentifier());
	}


	/**
	 * Remove all elements (Identifier, Metadatas and Links) from graph.
	 * Not necessary to tag elements before calling this method.
	 * This method deletes them all at once.
	 */
	public void removeAllElements(){

		for(NodeMetadata metas: new ArrayList<NodeMetadata>(mNodeMetadataMap.keySet())){
			removeNodeMetadata(metas);
		}
		for(ExpandedLink links : new ArrayList<ExpandedLink>(mExpandedLinkMap.keySet())){
			removeExpandedLink(links);
		}
		for(NodeIdentifier id : new ArrayList<NodeIdentifier>(mNodeIdentifierMap.keySet())){
			removeNodeIdentifier(id);
		}
	}


	/**
	 * Add a new NodeIdentifier to the graph.
	 * @param nodeIdentifier A new NodeIdentifier to be added to the graph.
	 */
	private void addNodeIdentifier(NodeIdentifier nodeIdentifier){
		LOGGER.trace("Method addNodeIdentifier(" + nodeIdentifier + ") called.");

		if(!mNodeIdentifierMap.containsKey(nodeIdentifier)){
    		NodeIdentifier2D nodeID2D = new NodeIdentifier2D(nodeIdentifier, mGraph, mLayout2D,
    		        mMetaCollocationIdentifier);
    		addToLists(nodeIdentifier, nodeID2D);
    		mGraph.addVertex(nodeID2D);
		}
	}


	/**
	 * Remove an existing Identifier from the graph.
	 * @param nodeIdentifier The Identifier to be removed from the graph.
	 */
	private void removeNodeIdentifier(NodeIdentifier nodeIdentifier){
		LOGGER.trace("Method removeNodeIdentifier(" + nodeIdentifier + ") called.");

		if(mNodeIdentifierMap.containsKey(nodeIdentifier)){
			NodeIdentifier2D nodeIdentifier2D = mNodeIdentifierMap.get(nodeIdentifier);
			removeFromLists(nodeIdentifier, nodeIdentifier2D);
			nodeIdentifier2D.removeYourself();
		}
	}


	/**
	 * Add a new ExpandedLink to the graph.
	 * @param expandedLink A new ExpandedLinks to be added to the graph.
	 */
	private void addExpandedLink(ExpandedLink expandedLink){
		LOGGER.trace("Method addExpandedLink(" + expandedLink + ") called.");

		if(mExpandedLinkMap.containsKey(expandedLink)){
			return;
		}

		NodeIdentifier2D start = mNodeIdentifierMap.get(expandedLink.getFirst());
		NodeIdentifier2D end = mNodeIdentifierMap.get(expandedLink.getSecond());

		if(start == null){
			addNodeIdentifier(expandedLink.getFirst());
			start = mNodeIdentifierMap.get(expandedLink.getFirst());
		}
		if(end == null){
			addNodeIdentifier(expandedLink.getSecond());
			end = mNodeIdentifierMap.get(expandedLink.getSecond());
		}

		ExpandedLink2D exLink2D = new ExpandedLink2D(expandedLink, start, end, mGraph, mLayout2D,
				mMetaCollocationLink);

		addToLists(expandedLink, exLink2D);
	}


	/**
	 * Remove an existing Link from the graph.
	 * @param expandedLink Link to be removed from the graph.
	 */
	private void removeExpandedLink(ExpandedLink expandedLink){
		LOGGER.trace("Method removeExpandedLink(" + expandedLink + ") called.");

		if(mExpandedLinkMap.containsKey(expandedLink)){
    		ExpandedLink2D exLink2D = mExpandedLinkMap.get(expandedLink);
    		removeFromLists(expandedLink, exLink2D);
    		exLink2D.removeYourself();
		}
	}


	/**
	 * Add a Metadata to an Identifier.
	 * @param meta Metadata to be added to the Identifier.
	 * @param id Identifier the Metadata should be added to.
	 */
	private void addNodeMetadata(NodeMetadata meta, NodeIdentifier id){
		LOGGER.trace("Method addMetadata(" + meta + ", " + id + ") called.");
		if(mNodeIdentifierMap.containsKey(id)){
			NodeIdentifier2D id2D = mNodeIdentifierMap.get(id);
			if(id2D != null){
				id2D.addNodeMetadata(meta);
				NodeMetadata2D meta2D = id2D.getNodeMetadata2D(meta);
				addToLists(meta, meta2D);
			}
		}
	}


	/**
	 * Add a Metadata to a Link.
	 * @param meta Metadata to be added to the Link.
	 * @param link Link the Metadata should be added to.
	 */
	private void addNodeMetadata(NodeMetadata meta, ExpandedLink link){
		LOGGER.trace("Method addMetadata(" + meta + ", " + link + ") called.");
		if(mExpandedLinkMap.containsKey(link)){
			ExpandedLink2D link2D = mExpandedLinkMap.get(link);
			if(link2D != null){
				link2D.addNodeMetadata(meta);
				NodeMetadata2D meta2D = link2D.getNodeMetadata2D(meta);
				addToLists(meta, meta2D);
			}
		}
	}


	/**
	 * Remove a Metadata from the graph.
	 * @param meta Metadata be be removed from the graph.
	 */
	private void removeNodeMetadata(NodeMetadata meta){
		LOGGER.trace("Method removeMetadata(" + meta + ") called.");
		if(mNodeMetadataMap.containsKey(meta)){
			NodeMetadata2D meta2D = mNodeMetadataMap.get(meta);
			if(meta2D != null && meta2D.getNodeIdentifier2D() != null){
				meta2D.getNodeIdentifier2D().removeNodeMetadata(meta);
			}
			else if(meta2D != null && meta2D.getExpandedLink2D() != null){
				meta2D.getExpandedLink2D().removeNodeMetadata(meta);
			}
			removeFromLists(meta);
		}
	}




	// ///////////////////////////////////////////////////////////// PRIVATE/PUBLIC - HELPER METHODS


	private void addToLists(NodeIdentifier nodeIdentifier, NodeIdentifier2D nodeIdentifier2D){
		LOGGER.trace("Method addToLists(" + nodeIdentifier + ", " + nodeIdentifier2D + ") called.");
		mNodeIdentifierMap.put(nodeIdentifier, nodeIdentifier2D);
		mNodeMetadataMap.putAll(nodeIdentifier2D.getNodeMetadataMap());

		if(!mNewNodes2D.contains(nodeIdentifier2D)){
			mNewNodes2D.add(nodeIdentifier2D);
		}
		for(NodeMetadata2D nm : nodeIdentifier2D.getNodesMetadata2D()){
			if(!mNewNodes2D.contains(nm)){
				mNewNodes2D.add(nm);
			}
		}
	}

	private void removeFromLists(NodeIdentifier nodeIdentifier, NodeIdentifier2D nodeIdentifier2D){
		LOGGER.trace("Method removeFromLists(" + nodeIdentifier + ", " + nodeIdentifier2D + ") called.");
		for(NodeMetadata nm : nodeIdentifier2D.getNodesMetadata()){
			mNodeMetadataMap.remove(nm);
		}

		mNodeIdentifierMap.remove(nodeIdentifier);
	}

	private void addToLists(ExpandedLink expandedLink, ExpandedLink2D expandedLink2D){
		LOGGER.trace("Method addToLists(" + expandedLink + ", " + expandedLink2D + ") called.");
		mExpandedLinkMap.put(expandedLink, expandedLink2D);

		mNodeMetadataMap.putAll(expandedLink2D.getNodeMetadataMap());
		for(NodeMetadata2D nm : expandedLink2D.getNodesMetadata2D()){
			if(!mNewNodes2D.contains(nm)){
				mNewNodes2D.add(nm);
			}
		}
	}

	private void removeFromLists(ExpandedLink expandedLink, ExpandedLink2D expandedLink2D){
		LOGGER.trace("Method removeFromLists(" + expandedLink + ", " + expandedLink2D + ") called.");
		for(NodeMetadata nm : expandedLink2D.getNodesMetadata()){
			mNodeMetadataMap.remove(nm);
		}

		mExpandedLinkMap.remove(expandedLink);
	}

	private void addToLists(NodeMetadata nodeMetadata, NodeMetadata2D nodeMetadata2D){
		LOGGER.trace("Method ExpandedLink2D(" + nodeMetadata + ", " + nodeMetadata + ") called.");
		mNodeMetadataMap.put(nodeMetadata, nodeMetadata2D);
		if(!mNewNodes2D.contains(nodeMetadata2D)){
			mNewNodes2D.add(nodeMetadata2D);
		}
	}

	private void removeFromLists(NodeMetadata nodeMetadata){
		LOGGER.trace("Method nodeMetadata(" + nodeMetadata + ") called.");
		mNodeMetadataMap.remove(nodeMetadata);
	}

	@SuppressWarnings("unused")
	private void printElementCount(){
		int idCount = mNodeIdentifierMap.size();
		int metaCount = mNodeMetadataMap.size();
		int nodeCount = idCount + metaCount;
		int linkCount = mExpandedLinkMap.size();

		int vertexCount = mGraph.getVertexCount();
		int edgeCount = mGraph.getEdgeCount();

		System.err.print("ElementCount: ");
		System.err.print("node("+nodeCount+"/"+vertexCount+"), ");
		System.err.print("edge("+linkCount+"/"+edgeCount+"), ");
		System.err.print("id("+idCount+"), ");
		System.err.println("meta("+metaCount+")");
	}


	// ///////////////////////////////////////////////////////////////////////////// PUBLIC - GETTER


	public Graph<Node2D, Edge2D> getGraph(){
		LOGGER.trace("Method getGraph() called.");
		return mGraph;
	}

	public List<Node2D> getAllNodes2D(){
		LOGGER.trace("Method getAllNodes2D() called.");
		List<Node2D> nodes = new ArrayList<>();

		nodes.addAll(mNodeIdentifierMap.values());
		nodes.addAll(mNodeMetadataMap.values());

		return nodes;
	}

	public int getAllNodesCount(){
		return (mNodeIdentifierMap.size() + mNodeMetadataMap.size());
	}

	public Dimension getDimension(){
		return mLayout2D.getDimension();
	}

	public double getMaxDimension(){
		if(mLayout2D.getDimensionX() > mLayout2D.getDimensionY()){
			return mLayout2D.getDimensionX();
		}
		else{
			return mLayout2D.getDimensionY();
		}
	}

	public List<ExpandedLink2D> getExpandedLinks2D(){
		LOGGER.trace("Method getExpandedLinks2D() called.");
		List<ExpandedLink2D> links2D = new ArrayList<>();
		links2D.addAll(mExpandedLinkMap.values());
		return links2D;
	}

	public MetadataCollocation getMetadataCollocationLink(){
		LOGGER.trace("Method getMetadataCollocationLink() called.");
		return mMetaCollocationLink;
	}

	public MetadataCollocation getMetadataCollocationIdentifier(){
		LOGGER.trace("Method getMetadataCollocationIdentifier() called.");
		return mMetaCollocationIdentifier;
	}

	public List<Edge2D> getAllEdges(){
		LOGGER.trace("Method getAllEdges() called.");
		List<Edge2D> edges = new ArrayList<>();

		for(Entry<NodeIdentifier, NodeIdentifier2D> id : mNodeIdentifierMap.entrySet()){
			edges.addAll(id.getValue().getEdges());
		}
		for(Entry<ExpandedLink, ExpandedLink2D> links : mExpandedLinkMap.entrySet()){
			edges.addAll(links.getValue().getEdges2D());
		}

		return edges;
	}


    // /////////////////////////////////////////////////////////////////////////////////////////////
}



























