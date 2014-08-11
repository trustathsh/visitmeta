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
 * This file is part of visitmeta visualization, version 0.1.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2014 Trust@HsH
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
 * <p>A Bipartite-Layout adapted to the specific structure of MAP graphs
 * (with identifier nodes connected via metadata nodes).</p>
 * <p>This implementation uses the class StaticLayout of JUNG2 for consistency.
 * The layout algorithm itself has been developed by Trust@HsH group.</p>
 * 
 * <p>Layout approach: Extended bipartite layout with 5 columns (numbered 0 to 4 from left to right) with</p>
 * <ul>
 *   <li>identifier nodes in columns 1 and 3,</li>
 * 	 <li>metadata nodes attached to links in column 2 (center), and</li>
 *   <li>metadata nodes attached to identifiers in columns 0 and 4.</li>
 * </ul>
 * <p>The graph is traversed using depth-first or breadth-first traversal.</p>
 * 
 * <p>Note: This approach only works for MetadataCollocation.FORK.
 * The metadata collocation is therefore altered to FORK at construction.</p>
 * 
 * @author vahlers
 */
public class LayoutBipartite2D extends Layout2D {

	// ///////////////////////////////////////////////////////////////////////////////////// MEMBERS

	private static final Logger LOGGER = Logger.getLogger(LayoutForceDirected2D.class);

	protected StaticLayout<Node2D, Edge2D> mLayout;		// JUNG layout class used for consistency

	private boolean mUseDFS;			// true: depth-first traversal (default), false: breadth-first traversal
	
	private int mXDir;					// current horizontal layout direction (-1: left, +1: right)
	private double mYOffset;			// vertical offset between rows
	private double[] mXPositions;		// horizontal positions of node columns
	private double[] mYPositions;		// vertical positions of next nodes in columns
	private List<Node2D> mDrawnNodes;	// list of nodes that have already been drawn

	// //////////////////////////////////////////////////////////////////////////////// CONSTRUCTORS

	public LayoutBipartite2D(Graph2D graph){
		super(graph, true);

		mDimension = new Dimension(1_000_000_000, 1_000_000_000);
		
		mLayout = new StaticLayout<Node2D, Edge2D>(mGraph.getGraph());
		mLayout.setSize(mDimension);
		
		mUseDFS = true;
		
		mYOffset = 0.06;
		mXPositions = new double[] { 0.2, 0.4, 0.8, 1.2, 1.4 };
		mYPositions = new double[5];
		mDrawnNodes = new ArrayList<Node2D>();

		// ensure that metadata collocation is set to FORK
		mGraph.alterMetadataCollocationForEntireGraph(MetadataCollocation.FORK);
	}

	// ////////////////////////////////////////////////////////////////////////////////////// PUBLIC

	/**
	 * Get graph traversal method.
	 * @return true: depth-first, false: breadth-first
	 */
	public boolean useDepthFirstTraversal() {
		return mUseDFS;
	}
	
	/**
	 * Set graph traversal method.
	 * @param useDFS true: depth-first, false: breadth-first
	 */
	public void useDepthFirstTraversal(boolean useDFS) {
		mUseDFS = useDFS;
	}
	
	// /////////////////////////////////////////////////////////////////////////////////////// SUPER

	/**
	 * Adjust layout.
	 * @param iterations unused for bipartite layout
	 */
	@Override
	public void adjust(int iterations) {
    	LOGGER.trace("Method adjust(" + iterations + ") called.");
    	mXDir = -1;
    	Arrays.fill(mYPositions, mYOffset);
		mDrawnNodes.clear();
		
		Collection<Node2D> nodes = mGraph.getGraph().getVertices();
		for (Node2D node2D : nodes) {
    		if (node2D instanceof NodeIdentifier2D) {
    			drawNodeIdentifier((NodeIdentifier2D) node2D, null);
    			
    			// start next graph component in column 1 (left)
    			mXDir = -1;
    			mYPositions[1] += mYOffset;
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
    public void setNodePosition(Node2D node2D, double x, double y){
		LOGGER.trace("Method setNodePosition(" + node2D + ", " + x + ", " + y + ") called.");
		mLayout.setLocation(node2D, x, y);
	}

	@Override
    public double getNodePositionX(Node2D node2D){
		LOGGER.trace("Method getNodePositionX("+node2D+") called.");
		return mLayout.getX(node2D);
	}

	@Override
    public double getNodePositionY(Node2D node2D){
		LOGGER.trace("Method getNodePositionY("+node2D+") called.");
		return mLayout.getY(node2D);
	}

	@Override
    public void lockNode(Node2D node2D){
		LOGGER.trace("Method lockNode("+node2D+") called.");
		mLayout.lock(node2D, true);
	}

	@Override
    public void unlockNode(Node2D node2D){
		LOGGER.trace("Method unlockNode("+node2D+") called.");
		mLayout.lock(node2D, false);
	}

	@Override
    public void lockAllNodes(){
		LOGGER.trace("Method lockAllNodes() called.");
		mLayout.lock(true);
	}

	@Override
    public void unlockAllNodes(){
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

	// ////////////////////////////////////////////////////////////////////////////////////// PRIVATE

	/**
	 * Draw identifier node.
	 * @param nodeId2D identifier node to be drawn
	 * @param nodeMeOld neighboring metadata node that has just been drawn (or null for first identifier node)
	 */
	private void drawNodeIdentifier(NodeIdentifier2D nodeId2D, NodeMetadata2D nodeMeOld) {
		if (mUseDFS) {	// depth-first traversal
			drawNodeIdentifierDFS(nodeId2D, nodeMeOld);
		}
		else {			// breadth-first-traversal
			drawNodeIdentifierBFS(nodeId2D, nodeMeOld);			
		}
	}

	/**
	 * Draw identifier node with depth-first traversal of attached metadata nodes (recursive implementation).
	 * @param nodeId2D identifier node to be drawn
	 * @param nodeMeOld neighboring metadata node that has just been drawn (or null for first identifier node)
	 */
	private void drawNodeIdentifierDFS(NodeIdentifier2D nodeId2D, NodeMetadata2D nodeMeOld) {
		
		// check if node has already been drawn
    	if (mDrawnNodes.contains(nodeId2D)) {
    		return;
    	}
    	
    	// draw identifier node
    	int colIdx = 2 + mXDir; 	// column 1 (left) or column 3 (right)
    	if (nodeMeOld != null) {
    		mYPositions[colIdx] = Math.max(getNodePositionY(nodeMeOld) / getDimensionY(), mYPositions[colIdx]);
    	}
    	if (nodeId2D.hasAdjustPermission() && !nodeId2D.wasPicked()) {
			nodeId2D.setPositionTriggeredByJung(mXPositions[colIdx] * getDimensionX(), mYPositions[colIdx] * getDimensionY());
    	}
		mYPositions[colIdx] += mYOffset;
		mDrawnNodes.add(nodeId2D);
		
		// first draw metadata nodes which are directly attached to identifier
		List<NodeMetadata2D> nodesMe = nodeId2D.getNodesMetadata2D();
    	for (NodeMetadata2D nodeMe2D : nodesMe) {
			drawNodeMetadata(nodeMe2D, nodeId2D);	
    	}
		
    	// draw all neighbors, incl. metadata nodes attached to links
    	Collection<Node2D> neighbors = mGraph.getGraph().getNeighbors(nodeId2D);
    	for (Node2D neighbor2D : neighbors) {
    		assert neighbor2D instanceof NodeMetadata2D;	// neighbor of identifier must be metadata
			drawNodeMetadata((NodeMetadata2D) neighbor2D, nodeId2D);	
			traverseNodeMetadata((NodeMetadata2D) neighbor2D);	
    	}
		
	}

	/**
	 * Draw identifier node with breadth-first traversal of attached metadata nodes (recursive implementation).
	 * TODO: Improve layout results of breadth-first traversal. <VA> 2014-08-05
	 * @param nodeId2D identifier node to be drawn
	 * @param nodeMeOld neighboring metadata node that has just been drawn (or null for first identifier node)
	 */
	private void drawNodeIdentifierBFS(NodeIdentifier2D nodeId2D, NodeMetadata2D nodeMeOld) {
		
		// check if node has already been drawn
    	if (mDrawnNodes.contains(nodeId2D)) {
    		return;
    	}

    	// draw identifier node
    	int colIdx = 2 + mXDir; 	// column 1 (left) or column 3  (right)
    	if (nodeMeOld != null) {
    		mYPositions[colIdx] = Math.max(getNodePositionY(nodeMeOld) / getDimensionY(), mYPositions[colIdx]);
    	}
    	if (nodeId2D.hasAdjustPermission() && !nodeId2D.wasPicked()) {
			nodeId2D.setPositionTriggeredByJung(mXPositions[colIdx] * getDimensionX(), mYPositions[colIdx] * getDimensionY());
    	}
		mYPositions[colIdx] += mYOffset;
		mDrawnNodes.add(nodeId2D);
		
		// first draw metadata nodes which are directly attached to identifier
		List<NodeMetadata2D> nodesMe = nodeId2D.getNodesMetadata2D();
    	for (NodeMetadata2D nodeMe2D : nodesMe) {
			drawNodeMetadata(nodeMe2D, nodeId2D);	
    	}
		
    	// draw all neighbors, incl. metadata nodes attached to links
    	Collection<Node2D> neighbors = mGraph.getGraph().getNeighbors(nodeId2D);
    	List<NodeMetadata2D> nodesToTraverse = new ArrayList<NodeMetadata2D>();
    	for (Node2D neighbor2D : neighbors) {
    		assert neighbor2D instanceof NodeMetadata2D;	// neighbor of identifier must be metadata
			NodeMetadata2D nodeMe2D = (NodeMetadata2D) neighbor2D;
			drawNodeMetadata(nodeMe2D, nodeId2D);
			nodesToTraverse.add(nodeMe2D);
    	}
    	
    	// traverse those neighbors that are metadata nodes attached to links
    	for (NodeMetadata2D nodeMe2D : nodesToTraverse) {
   			traverseNodeMetadata(nodeMe2D);	
    	}
		
	}

	/**
	 * Draw metadata node.
	 * @param nodeMe2D metadata node to be drawn
	 * @param nodeIdOld neighboring identifier node that has just been drawn
	 */
	private void drawNodeMetadata(NodeMetadata2D nodeMe2D, NodeIdentifier2D nodeIdOld) {
    	if (mDrawnNodes.contains(nodeMe2D)) {
    		return;
    	}
    	assert nodeMe2D.getExpandedLink2D() != null || nodeMe2D.getNodeIdentifier2D() != null;	// metadata must be attached to either link or identifier
		if (nodeMe2D.getExpandedLink2D() != null) {			// metadata node attached to link -> column 2 (center)
	    	int colIdx = 2; 	// column 2 (center)
			mYPositions[colIdx] = Math.max(getNodePositionY(nodeIdOld) / getDimensionY(), mYPositions[colIdx]);
	    	if (nodeMe2D.hasAdjustPermission() && !nodeMe2D.wasPicked()) {
	    		nodeMe2D.setPositionTriggeredByJung(mXPositions[colIdx] * getDimensionX(),	mYPositions[colIdx] * getDimensionY());
	    	}
	    	mYPositions[colIdx] += mYOffset;
			mDrawnNodes.add(nodeMe2D);
		}
		else if (nodeMe2D.getNodeIdentifier2D() != null) {	// metadata node attached to identifier -> column 0 or 4
	    	int colIdx = 2 + 2 * mXDir; 	// column 0 (left) or column 4  (right)
			mYPositions[colIdx] = Math.max(getNodePositionY(nodeIdOld) / getDimensionY(), mYPositions[colIdx]);
	    	if (nodeMe2D.hasAdjustPermission() && !nodeMe2D.wasPicked()) {
	    		nodeMe2D.setPositionTriggeredByJung(mXPositions[colIdx] * getDimensionX(),	mYPositions[colIdx] * getDimensionY());
	    	}
	    	mYPositions[colIdx] += mYOffset;
			mDrawnNodes.add(nodeMe2D);
		}
	}

	/**
	 * Traverse metadata node attached to link, i.e., draw second neighboring identifier node.
	 * @param nodeMe2D metadata node to be traversed
	 */
	private void traverseNodeMetadata(NodeMetadata2D nodeMe2D) {
		
		// check if metadata node is attached to link (metadata nodes attached to identifiers cannot be traversed)
		if (nodeMe2D.getExpandedLink2D() != null) {
	    	// draw all neighbors
			mXDir *= -1;
	    	Collection<Node2D> neighbors = mGraph.getGraph().getNeighbors(nodeMe2D);
	    	for (Node2D neighbor2D : neighbors) {
	    		assert neighbor2D instanceof NodeIdentifier2D;	// neighbor of metadata must be identifier
    			drawNodeIdentifier((NodeIdentifier2D) neighbor2D, nodeMe2D);	
	    	}
			mXDir *= -1;
		}
	}

}
