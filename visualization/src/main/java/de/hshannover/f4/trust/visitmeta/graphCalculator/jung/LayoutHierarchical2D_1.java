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
public class LayoutHierarchical2D_1 extends Layout2D {

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

	public LayoutHierarchical2D_1(Graph2D graph) {
		super(graph, true);

		mDimension = new Dimension(1_000_000_000, 1_000_000_000);

		mLayout = new StaticLayout<Node2D, Edge2D>(mGraph.getGraph());
		mLayout.setSize(mDimension);

		mYOffset = 0.06;
		

		mXPositions = new ArrayList<>();

		mYPositions = new ArrayList<>();
		mDrawnNodes = new ArrayList<Node2D>();

		// ensure that metadata collocation is set to FORK
		mGraph.alterMetadataCollocationForEntireGraph(MetadataCollocation.FORK);
	}

	public void maxSize(List<NodeIdentifier2D> nodesId){
		for(NodeIdentifier2D nodeId:nodesId){
			if(nodeId.getExpandedLinks2D().size()>maxSize){
				maxSize=nodeId.getExpandedLinks2D().size();
			}
		}
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

		mXPositions.add(0.3);
		mYPositions.add(mYOffset);
		initLayer = 1;
		maxSize=0;
		mDrawnNodes.clear();

		Collection<Node2D> nodes = mGraph.getGraph().getVertices();
		List<NodeIdentifier2D> nodesId=new ArrayList<>();
		for(Node2D node : nodes){
			if (node instanceof NodeIdentifier2D) {
				nodesId.add((NodeIdentifier2D) node);
			}
		}
		maxSize(nodesId);
		for (Node2D node2D : nodes) {
			if (node2D instanceof NodeIdentifier2D) {
				NodeIdentifier2D nodeId=(NodeIdentifier2D) node2D;
				//find the root with maximum number of links
				if (nodeId.getExpandedLinks2D().size()==maxSize){
					drawNodeIdentifier(nodeId, null, initLayer);
				}
				initLayer = 1;
				mYPositions.set(0, mYPositions.get(0) + mYOffset);
			}
		}

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

	private void drawNodeIdentifier(NodeIdentifier2D nodeId2D, NodeMetadata2D nodeMeOld, int layer) {

		// check if node has already been drawn
		if (mDrawnNodes.contains(nodeId2D)) {
			return;
		}

		if (layer > mXPositions.size()) {
			mXPositions.add(layer * 0.3);
			mYPositions.add(mYOffset);
		}

		// draw identifier node
		int colIdx = layer - 1;
		double y = mYPositions.get(colIdx);
		if (nodeMeOld != null) {
			y = Math.max(getNodePositionY(nodeMeOld) / getDimensionY(), y);
		}
		if (nodeId2D.hasAdjustPermission() && !nodeId2D.wasPicked()) {
			nodeId2D.setPositionTriggeredByJung(mXPositions.get(colIdx) * getDimensionX(), y * getDimensionY());
		}
		mYPositions.set(colIdx, y + mYOffset);

		mDrawnNodes.add(nodeId2D);
		layer++;
		// first draw metadata nodes which are directly attached to identifier
		List<NodeMetadata2D> nodesMe = nodeId2D.getNodesMetadata2D();
		for (NodeMetadata2D nodeMe2D : nodesMe) {
			drawNodeMetadata(nodeMe2D, nodeId2D, layer);
		}

		// draw all neighbors, incl. metadata nodes attached to links
		Collection<Node2D> neighbors = mGraph.getGraph().getNeighbors(nodeId2D);
		List<NodeMetadata2D> nodeMeInLink = new ArrayList<NodeMetadata2D>();
		// for (Node2D neighbor2D : neighbors) {
		// assert neighbor2D instanceof NodeMetadata2D; // neighbor of
		// // identifier must
		// // be metadata
		// NodeMetadata2D nodeMe2D = (NodeMetadata2D) neighbor2D;
		// drawNodeMetadata(nodeMe2D, nodeId2D, layer);
		// nodeMeInLink.add(nodeMe2D);
		// }

		for (Node2D neighbor2D : neighbors) {
			assert neighbor2D instanceof NodeMetadata2D; // neighbor of
															// identifier must
															// be metadata
			NodeMetadata2D nodeMe2D = (NodeMetadata2D) neighbor2D;
			NodeIdentifier2D end = null;
			if (nodeId2D.equals(nodeMe2D.getExpandedLink2D().getStart())) {
				end = nodeMe2D.getExpandedLink2D().getEnd();
			} else if (nodeId2D.equals(nodeMe2D.getExpandedLink2D().getEnd())) {
				end = nodeMe2D.getExpandedLink2D().getStart();
			}
			drawNodeMetadata(nodeMe2D, nodeId2D, layer);
			nodeMeInLink.add(nodeMe2D);

			for (Node2D neighbor : neighbors) {
				assert neighbor instanceof NodeMetadata2D;
				NodeMetadata2D nodeMe = (NodeMetadata2D) neighbor2D;
				if (!nodeMeInLink.contains(nodeMe)) {
					NodeIdentifier2D end2 = null;
					// if (end.equals(nodeMe.getExpandedLink2D().getStart())
					// || end.equals(nodeMe.getExpandedLink2D().getEnd())) {
					// drawNodeMetadata(nodeMe, nodeId2D, layer);
					// }
					if (nodeId2D.equals(nodeMe.getExpandedLink2D().getStart())) {
						end2 = nodeMe.getExpandedLink2D().getEnd();
					} else if (nodeId2D.equals(nodeMe.getExpandedLink2D().getEnd())) {
						end2 = nodeMe.getExpandedLink2D().getStart();
					}

					if (end2.equals(end)) {
						drawNodeMetadata(nodeMe, nodeId2D, layer);
					}
				}
			}
		}
		for (NodeMetadata2D nodeInLink : nodeMeInLink) {
			drawLinks(nodeInLink, layer);
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
	private void drawNodeMetadata(NodeMetadata2D nodeMe2D, NodeIdentifier2D nodeIdOld, int layer) {
		if (mDrawnNodes.contains(nodeMe2D)) {
			return;
		}
		if (layer > mXPositions.size()) {
			mXPositions.add(layer * 0.3);
			mYPositions.add(mYOffset);
		}
		assert nodeMe2D.getExpandedLink2D() != null || nodeMe2D.getNodeIdentifier2D() != null;
		if (nodeMe2D.getExpandedLink2D() != null) {
			int colIdx = layer - 1;
			double y = mYPositions.get(colIdx);
			y = Math.max(getNodePositionY(nodeIdOld) / getDimensionY(), y);

			if (nodeMe2D.hasAdjustPermission() && !nodeMe2D.wasPicked()) {
				nodeMe2D.setPositionTriggeredByJung(mXPositions.get(colIdx) * getDimensionX(), y * getDimensionY());
			}
			mYPositions.set(colIdx, y + mYOffset);
			mDrawnNodes.add(nodeMe2D);
		} else if (nodeMe2D.getNodeIdentifier2D() != null) {
			int colIdx = layer - 1;
			double y = mYPositions.get(colIdx);
			y = Math.max(getNodePositionY(nodeIdOld) / getDimensionY(), y);

			if (nodeMe2D.hasAdjustPermission() && !nodeMe2D.wasPicked()) {
				nodeMe2D.setPositionTriggeredByJung(mXPositions.get(colIdx) * getDimensionX(),
						mYPositions.get(colIdx) * getDimensionY());
			}
			mYPositions.set(colIdx, y + mYOffset);
			mDrawnNodes.add(nodeMe2D);
		}
	}

	/**
	 * draw second neighboring identifier node to the left of this metadata.
	 * -->M-->I
	 * 
	 * @param nodeMe2D
	 *            metadata node at the Link
	 */
	private void drawLinks(NodeMetadata2D nodeMe2D, int layer) {

		// check if metadata node is attached to link
		if (nodeMe2D.getExpandedLink2D() != null) {
			// draw all neighbors
			ExpandedLink2D link = nodeMe2D.getExpandedLink2D();
			
			Collection<Node2D> neighbors = mGraph.getGraph().getNeighbors(nodeMe2D);
			for (Node2D neighbor2D : neighbors) {
				assert neighbor2D instanceof NodeIdentifier2D;
				if (neighbor2D.equals(link.getEnd()) || neighbor2D.equals(link.getStart())) {
					drawNodeIdentifier((NodeIdentifier2D) neighbor2D, nodeMe2D, layer + 1);
				}

			}
		}

	}

}
