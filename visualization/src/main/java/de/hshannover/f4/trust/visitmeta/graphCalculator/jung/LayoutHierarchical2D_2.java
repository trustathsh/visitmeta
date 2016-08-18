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
 * This file is part of visitmeta-visualization, version 0.6.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2016 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.graphCalculator.jung;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.graphCalculator.MetadataCollocation;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;

/**
 * @author sunnihu
 */
public class LayoutHierarchical2D_2 extends Layout2D {

	// /////////////////////////////////////////////////////////////////////////////////////
	// MEMBERS

	private static final Logger LOGGER = Logger.getLogger(LayoutForceDirected2D.class);

	protected StaticLayout<Node2D, Edge2D> mLayout; // JUNG layout class used
													// for consistency

	private double mYOffset; // vertical offset between rows
	private ArrayList<Double> mXPositions; // horizontal positions of node
											// columns
	private ArrayList<Double> mYPositions; // vertical positions of next nodes
											// in columns
	private List<Node2D> mDrawnNodes; // list of nodes that have already been
										// drawn

	private int initLayer;
	private int maxSize;

	// ////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS

	public LayoutHierarchical2D_2(Graph2D graph) {
		super(graph, true);

		mDimension = new Dimension(1_000_000_000, 1_000_000_000);

		mLayout = new StaticLayout<Node2D, Edge2D>(mGraph.getGraph());
		mLayout.setSize(mDimension);

		mYOffset = 0.12;

		mXPositions = new ArrayList<>();

		mYPositions = new ArrayList<>();
		mDrawnNodes = new ArrayList<Node2D>();

		// ensure that metadata collocation is set to FORK
		mGraph.alterMetadataCollocationForEntireGraph(MetadataCollocation.FORK);
	}

	public boolean isRoot(NodeIdentifier2D nodeId) {
		boolean root = true;

		// find the node as root, that all its Links begin with it
		if (!nodeId.getExpandedLinks2D().isEmpty() && nodeId.getNodesMetadata2D().isEmpty()) {
			for (ExpandedLink2D link : nodeId.getExpandedLinks2D()) {
				root &= true;
				if (link.getStart().equals(nodeId)) {
					root = true;
				}
			}
		}

		if (root == true) {
			return true;
		} else
			return false;
	}
	

	// ///////////////////////////////////////////////////////////////////////////////////////
	// SUPER

	/**
	 * Adjust layout.
	 * 
	 * @param iterations
	 *            unused for hierarchical layout
	 */
	@Override
	public void adjust(int iterations) {
		LOGGER.trace("Method adjust(" + iterations + ") called.");

		mXPositions.clear();
		mYPositions.clear();

		mXPositions.add(0.6);
		mYPositions.add(mYOffset);
		initLayer = 1;
		maxSize=0;

		mDrawnNodes.clear();

		List<NodeMetadata2D> nodesMe = new ArrayList<>();
		Collection<Node2D> nodes = mGraph.getGraph().getVertices();
		for (Node2D node2D : nodes) {
			if (node2D instanceof NodeIdentifier2D) {
				NodeIdentifier2D nodeId=(NodeIdentifier2D) node2D;
				if (isRoot(nodeId)){
					drawNodeIdentifier(nodeId, initLayer);
				}
				initLayer = 1;
				mYPositions.set(0, mYPositions.get(0) + mYOffset);
			} else if (node2D instanceof NodeMetadata2D) {
				nodesMe.add((NodeMetadata2D) node2D);
			}
		}
		drawNodeMetadata(nodesMe);
	}

	@Override
	public void calculateUniformEdgeLength() {
		LOGGER.trace("Method calculateUniformEdgeLength() called.");
		// DO NOTHING!!!
		// Only necessary for SpringLayout.
	}

	@Override
	public void reset() {
		LOGGER.trace("Method reset() called.");
		mLayout.reset();
	}

	@Override
	public void setNodePosition(Node2D node2D, double x, double y) {
		LOGGER.trace("Method setNodePosition(" + node2D + ", " + x + ", " + y + ") called.");
		mLayout.setLocation(node2D, x, y);
	}

	@Override
	public double getNodePositionX(Node2D node2D) {
		LOGGER.trace("Method getNodePositionX(" + node2D + ") called.");
		return mLayout.getX(node2D);
	}

	@Override
	public double getNodePositionY(Node2D node2D) {
		LOGGER.trace("Method getNodePositionY(" + node2D + ") called.");
		return mLayout.getY(node2D);
	}

	@Override
	public void lockNode(Node2D node2D) {
		LOGGER.trace("Method lockNode(" + node2D + ") called.");
		mLayout.lock(node2D, true);
	}

	@Override
	public void unlockNode(Node2D node2D) {
		LOGGER.trace("Method unlockNode(" + node2D + ") called.");
		mLayout.lock(node2D, false);
	}

	@Override
	public void lockAllNodes() {
		LOGGER.trace("Method lockAllNodes() called.");
		mLayout.lock(true);
	}

	@Override
	public void unlockAllNodes() {
		LOGGER.trace("Method unlockAllNodes() called.");
		mLayout.lock(false);
	}

	@Override
	public int getMaxIterations() {
		LOGGER.trace("Method getMaxIterations() called.");
		// DO NOTHING!!!
		// Only necessary for iterative layouts.
		return 0;
	}

	@Override
	public void setMaxIterations(int maxIterations) {
		LOGGER.trace("Method setMaxIterations(" + maxIterations + ") called.");
		// DO NOTHING!!!
		// Only necessary for iterative layouts.
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE

	/**
	 * Draw identifier node.
	 * 
	 * @param nodeId2D
	 *            identifier node to be drawn
	 * @param nodeMeOld
	 *            neighboring metadata node that has just been drawn (or null
	 *            for first identifier node)
	 */

	private void drawNodeIdentifier(NodeIdentifier2D nodeId2D, int layer) {

		// check if node has already been drawn
		if (mDrawnNodes.contains(nodeId2D)) {
			return;
		}

		if (layer > mXPositions.size()) {
			mXPositions.add(layer * 0.6);
			mYPositions.add(mYOffset);
		}

		// draw identifier node
		int colIdx = layer - 1;
		double y = mYPositions.get(colIdx);

		if (nodeId2D.hasAdjustPermission() && !nodeId2D.wasPicked()) {
			nodeId2D.setPositionTriggeredByJung(mXPositions.get(colIdx) * getDimensionX(), y * getDimensionY());
		}
		mYPositions.set(colIdx, y + mYOffset);

		mDrawnNodes.add(nodeId2D);
		layer++;

		List<NodeIdentifier2D> endIdNodes = new ArrayList<>();
		List<ExpandedLink2D> links = nodeId2D.getExpandedLinks2D();
		NodeIdentifier2D endNodeId = null;
		for (ExpandedLink2D link : links) {
			if (nodeId2D.equals(link.getStart())) {
				endNodeId = link.getEnd();
			} else if (nodeId2D.equals(link.getEnd())) {
				endNodeId = link.getStart();
			}

			drawNodeIdentifier(endNodeId, layer);
			endIdNodes.add(endNodeId);
		}

		// draw the Identifier on the other end of link

		for (NodeIdentifier2D endNode : endIdNodes) {
			drawLinks(endNode, layer);
		}

	}

	/**
	 * Draw metadata node.
	 * 
	 * @param nodeMe2D
	 *            metadata node to be drawn
	 * @param nodeIdOld
	 *            neighboring identifier node that has just been drawn
	 */
	private void drawNodeMetadata(List<NodeMetadata2D> nodesMe) {
		for (NodeMetadata2D nodeMe2D : nodesMe) {
			if (mDrawnNodes.contains(nodeMe2D)) {
				return;
			}
			assert nodeMe2D.getExpandedLink2D() != null || nodeMe2D.getNodeIdentifier2D() != null;
			if (nodeMe2D.getExpandedLink2D() != null) {
				NodeIdentifier2D start = nodeMe2D.getExpandedLink2D().getStart();
				NodeIdentifier2D end = nodeMe2D.getExpandedLink2D().getEnd();
				double incrX = getNodePositionX(end) - getNodePositionX(start);
				double incrY = getNodePositionY(end) - getNodePositionY(start);
				double x = getNodePositionX(start) + incrX / 2;
				double y = getNodePositionY(start) + incrY / 2;
				Collection<Node2D> nodes = mGraph.getGraph().getVertices();
				for (Node2D node2D : nodes) {
					if (node2D instanceof NodeIdentifier2D) {
						NodeIdentifier2D nodeId = (NodeIdentifier2D) node2D;
						if (x == getNodePositionX(nodeId) && y == getNodePositionY(nodeId)) {
							y = getNodePositionY(nodeId) + 0.03 * getDimensionY();
						}
					}					
				}
				List<Node2D> nodeDrawn=new ArrayList<>();
				nodeDrawn=mDrawnNodes;
				for(Node2D node2d:nodeDrawn){
					if(node2d instanceof NodeMetadata2D){
						NodeMetadata2D nodeMe = (NodeMetadata2D) node2d;
						if(x == getNodePositionX(nodeMe) && y == getNodePositionY(nodeMe)){
							y = getNodePositionY(nodeMe) + 0.03 * getDimensionY();
						}
					}
				}

				if (nodeMe2D.hasAdjustPermission() && !nodeMe2D.wasPicked()) {
					nodeMe2D.setPositionTriggeredByJung(x, y);
				}
				mDrawnNodes.add(nodeMe2D);
			} else if (nodeMe2D.getNodeIdentifier2D() != null) {
				NodeIdentifier2D nodeId = nodeMe2D.getNodeIdentifier2D();
				double x = getNodePositionX(nodeId) + 0.3 * getDimensionX();
				double y = getNodePositionY(nodeId);
				List<Node2D> nodeDrawn=new ArrayList<>();
				nodeDrawn=mDrawnNodes;
				for(Node2D node2d:nodeDrawn){
					if(node2d instanceof NodeMetadata2D){
						NodeMetadata2D nodeMe = (NodeMetadata2D) node2d;
						if(x == getNodePositionX(nodeMe) && y == getNodePositionY(nodeMe)){
							y = getNodePositionY(nodeMe) + 0.03 * getDimensionY();
						}
					}
				}
				if (nodeMe2D.hasAdjustPermission() && !nodeMe2D.wasPicked()) {
					nodeMe2D.setPositionTriggeredByJung(x, y);
				}
				mDrawnNodes.add(nodeMe2D);
			}
		}

	}

	/**
	 * draw second neighboring identifier node to the left of this metadata.
	 * -->M-->I
	 * 
	 * @param nodeMe2D
	 *            metadata node at the Link
	 */
	private void drawLinks(NodeIdentifier2D endNodeId, int layer) {

		// check if metadata node is attached to link
		if (!endNodeId.getExpandedLinks2D().isEmpty()) {

			drawNodeIdentifier(endNodeId, layer + 1);

		}

	}

}
