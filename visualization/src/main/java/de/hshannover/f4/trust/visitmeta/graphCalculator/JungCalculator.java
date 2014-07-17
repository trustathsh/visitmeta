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
 * This file is part of visitmeta visualization, version 0.0.7,
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





import java.util.List;
import java.util.Observable;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.ExpandedLink;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeIdentifier;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeMetadata;
import de.hshannover.f4.trust.visitmeta.datawrapper.Position;
import de.hshannover.f4.trust.visitmeta.datawrapper.UpdateContainer;
import de.hshannover.f4.trust.visitmeta.graphCalculator.jung.Graph2D;
import de.hshannover.f4.trust.visitmeta.graphCalculator.jung.LayoutType;
import de.hshannover.f4.trust.visitmeta.graphCalculator.jung.MetadataCollocation;

public class JungCalculator implements Calculator {

	private static final Logger LOGGER = Logger.getLogger(JungCalculator.class);

	private Graph2D             mGraph2D;
	private LayoutType          mLayoutType;
	private MetadataCollocation mExpandedEdgeType;
	private MetadataCollocation mIdentifierEdgeType;

	public JungCalculator() {

		mLayoutType         = LayoutType.FORCE_DIRECTED;
		mExpandedEdgeType   = MetadataCollocation.FORK;
		mIdentifierEdgeType = MetadataCollocation.FORK;

		mGraph2D = new Graph2D(mExpandedEdgeType, mIdentifierEdgeType);
		mGraph2D.setLayout(mLayoutType);
	}

	/**
	 * Calculate the positions for the nodes of the graph.
	 */
	@Override
	public void calculatePositions() {
		LOGGER.trace("Method calculatePositions() called.");
		//Do nothing
	}

	/**
	 * Add and remove nodes and links to the graph.
	 * @param uc the container with the changes.
	 */
	@Override
	public void addRemoveNodesLinksMetadatas(UpdateContainer uc) {
		LOGGER.trace("Method updateGraph(" + uc + ") called.");
		/* Add observer */
		for(NodeIdentifier identifier : uc.getListAddIdentifier()){
			identifier.addObserver(this);
			for(NodeMetadata metadata : identifier.getMetadata()) {
				metadata.addObserver(this);
			}
		}
		for(ExpandedLink link : uc.getListAddLinks()){
			for(NodeMetadata metadata : link.getMetadata()){
				metadata.addObserver(this);
			}
		}
		for(List<NodeMetadata> listMetadata : uc.getListAddMetadataIdentifier().values()) {
			for(NodeMetadata metadata : listMetadata){
				metadata.addObserver(this);
			}
		}
		for(List<NodeMetadata> listMetadata : uc.getListAddMetadataLinks().values()) {
			for(NodeMetadata metadata : listMetadata){
				metadata.addObserver(this);
			}
		}
		/* Remove observer */
		for(NodeIdentifier identifer : uc.getListDeleteIdentifier()) {
			identifer.deleteObserver(this);
			for(NodeMetadata metadata : identifer.getMetadata()) {
				metadata.deleteObserver(this);
			}
		}
		for(ExpandedLink link : uc.getListDeleteLinks()) {
			for(NodeMetadata metadata : link.getMetadata()){
				metadata.deleteObserver(this);
			}
		}
		for(List<NodeMetadata> listMetadata : uc.getListDeleteMetadataIdentifier().values()) {
			for(NodeMetadata metadata : listMetadata){
				metadata.deleteObserver(this);
			}
		}
		for(List<NodeMetadata> listMetadata : uc.getListDeleteMetadataLinks().values()) {
			for(NodeMetadata metadata : listMetadata){
				metadata.deleteObserver(this);
			}
		}
		/* Add data to jung */
		mGraph2D.addRemoveNodesLinksMetadatas(uc);
		//mGraph2D.adjustNewNodes(100);
		LOGGER.debug("Resulting Graph2D (node maps): " + mGraph2D.getAllNodesCount() + " nodes, "
				+ mGraph2D.getAllEdges().size() + " edges");
		LOGGER.debug("Resulting Graph2D (JUNG graph): " + mGraph2D);
	}

	@Override
	public void updateNode(Position p, double xNew, double yNew, double zNew){
		if(p instanceof NodeIdentifier){
			mGraph2D.updateNodePosition((NodeIdentifier)p, xNew, yNew);
			//mGraph2D.adjustAfterPickingNode((NodeIdentifier)o, 100);
		}
		else if(p instanceof NodeMetadata){
			mGraph2D.updateNodePosition((NodeMetadata)p, xNew, yNew);
			//mGraph2D.adjustAfterPickingNode((NodeMetadata)o, 100);
		}
	}

	/**
	 * A node was moved from its previous position to another in the graphical interface.
	 */
	@Override
	public synchronized void update(Observable o, Object arg) {
//		if(o instanceof NodeIdentifier){
//			mGraph2D.updateNodePosition((NodeIdentifier)o);
//			//mGraph2D.adjustAfterPickingNode((NodeIdentifier)o, 100);
//		}
//		else if(o instanceof NodeMetadata){
//			mGraph2D.updateNodePosition((NodeMetadata)o);
//			//mGraph2D.adjustAfterPickingNode((NodeMetadata)o, 100);
//		}
	}

	@Override
	public void clearGraph(){
		mGraph2D.clearGraph();
	}

	@Override
	public void deleteNode(Position node) {
		if(node instanceof NodeIdentifier){
			mGraph2D.deleteNode((NodeIdentifier)node);
		}
		else if(node instanceof NodeMetadata){
			mGraph2D.deleteNode((NodeMetadata)node);
		}
	}

	@Override
	public void deleteLink(ExpandedLink expandedLink){
		mGraph2D.deleteLink(expandedLink);
	}

	@Override
	public void adjustGraphAnew(int iterations){
		mGraph2D.adjustGraphAnew(iterations);
	}

	@Override
	public void adjustAllNodes(int iterations){
		mGraph2D.adjustAllNodes(iterations);
	}

	@Override
	public void adjustAllNodes(int iterations, boolean checkPermission, boolean checkPicking){
		mGraph2D.adjustAllNodes(iterations, checkPermission, checkPicking);
	}

	@Override
	public void adjustIdentifierNodes(int iterations){
		mGraph2D.adjustIdentifierNodes(iterations);
	}

	@Override
	public void adjustMetadataNodes(int iterations){
		mGraph2D.adjustMetadataNodes(iterations);
	}

	@Override
	public void adjustNewNodes(int iterations){
		mGraph2D.adjustNewNodes(iterations);
	}

	@Override
	public void adjustByStrategy(int iterations){
		mGraph2D.adjustByStrategy(iterations);
	}

	@Override
	public void alterLevelOfDetail(NodeIdentifier ni, MetadataCollocation mc){
		mGraph2D.alterMetadataCollocation(ni, mc);
	}

	@Override
	public void alterLevelOfDetail(ExpandedLink el, MetadataCollocation mc){
		mGraph2D.alterMetadataCollocation(el, mc);
	}

	@Override
	public void alterLevelOfDetailForAllIdentifiers(MetadataCollocation mc){
		mGraph2D.alterMetadataCollocationForAllIdentifiers(mc);
	}

	@Override
	public void alterLevelOfDetailForAllExpandedLinks(MetadataCollocation mc){
		mGraph2D.alterMetadataCollocationForAllExpandedLinks(mc);
	}

	@Override
	public void alterLevelOfDetailForEntireGraph(MetadataCollocation mc){
		mGraph2D.alterMetadataCollocationForEntireGraph(mc);
	}

	@Override
	public void setIterations(int iterations){
		mGraph2D.setIterations(iterations);
	}

	@Override
	public void setLayout(LayoutType layoutType){
		mGraph2D.setLayout(layoutType);
	}

	@Override
	public void setLayoutForceDirectd(double attractionMultiplier, double repulsionMultiplier){
		mGraph2D.setLayoutForceDirectd(attractionMultiplier, repulsionMultiplier);
	}

	@Override
	public void setLayoutSpring(boolean useIndividualEdgeLength, int dimensionX,
			int dimensionY, double stretch, double forceMultiplier, int repulsionRange){
		mGraph2D.setLayoutSpring(useIndividualEdgeLength, dimensionX, dimensionY, stretch,
			forceMultiplier, repulsionRange);
	}

}
