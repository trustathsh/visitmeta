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
 * This file is part of visitmeta-visualization, version 0.4.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
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
import org.apache.commons.collections15.Transformer;
import org.apache.log4j.Logger;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;



/**
 * <p>A spring-layout.</p>
 * <p>Uses the SpringLayout of JUNG2.</p>
 * <p>
 * CAUTION: This class needs a revision. Use LayoutForceDirected2D (LayoutType.FORCE_DIRECTED)
 * until this is done.
 * </p>
 */
public class LayoutSpring2D extends Layout2D{

	// ///////////////////////////////////////////////////////////////////////////////////// MEMBERS


	protected SpringLayout<Node2D, Edge2D> mLayout;


	/**
	 * Defines how the layout gets the length of an edge.
	 */
	private final Transformer<Edge2D, Integer> mLengthFunction = new Transformer<Edge2D, Integer>(){
		@Override
		public Integer transform(Edge2D edge){
			return edge.getLength();
		}
	};

	/**
	 * <p>
	 * The tendency of an edge to change its length.<br>
	 * Specifies how much the grade of an edge influences its movement.
	 * </p>
	 * <p>
	 * Positive values < 1.0: Low-grade nodes rather tend to move then high-grade nodes.<br>
	 * Positive values > 1.0: High-grade nodes rather tend to move then low-grade nodes.<br>
	 * Negative values: Not provided.<br>
	 * Default-value: 0.7
	 * </p>
	 */
	private double mStretch;

	/**
	 * <p>
	 * How strongly an edge tries to maintain its length.<br>
	 * High values: Make sure that the node maintains its length.
	 * </p>
	 * <p>
	 * 0.0: Disables this functionality.<br>
	 * Negative values: Not provided.<br>
	 * Default-value: 1.0/3.0
	 * </p>
	 */
	private double mForceMultiplier;

	/**
	 * <p>
	 * Repulsion-radius.<br>
	 * Outside this radius, nodes do not repel each other.
	 * </p>
	 * <p>
	 * Negative values: not provided<br>
	 * Default-value: 100
	 * </p>
	 */
	private int mRepulsionRange;



	private Logger LOGGER = Logger.getLogger(LayoutSpring2D.class);

	// //////////////////////////////////////////////////////////////////////////////// CONSTRUCTORS

	public LayoutSpring2D(Graph2D graph){
		super(graph, false);

		mDimension = new Dimension(1000, 1000);
		mStretch = 0.7;
		mRepulsionRange = 100;
		mForceMultiplier = 1.0 / 3.0;

		mLayout = new SpringLayout<>(mGraph.getGraph(), mLengthFunction);
		mLayout.setStretch(mStretch);
		mLayout.setRepulsionRange(mRepulsionRange);
		mLayout.setForceMultiplier(mForceMultiplier);
		mLayout.setSize(mDimension);
	}


	public LayoutSpring2D(Graph2D graph, boolean useIndividualEdgeLength, int dimensionX,
			int dimensionY, double stretch, double forceMultiplier, int repulsionRange){
		super(graph, useIndividualEdgeLength);

		mDimension = new Dimension(dimensionX, dimensionY);
		mStretch = stretch;
		mForceMultiplier = forceMultiplier;
		mRepulsionRange = repulsionRange;

		mLayout = new SpringLayout<>(mGraph.getGraph(), mLengthFunction);
		mLayout.setStretch(mStretch);
		mLayout.setRepulsionRange(mRepulsionRange);
		mLayout.setForceMultiplier(mForceMultiplier);
		mLayout.setSize(mDimension);
	}

	// ////////////////////////////////////////////////////////////////////////////////////// PUBLIC



	public void update(){
		LOGGER.trace("Method update() called.");
		mLayout.setStretch(mStretch);
		mLayout.setRepulsionRange(mRepulsionRange);
		mLayout.setForceMultiplier(mForceMultiplier);
		mLayout.setSize(mDimension);
	}

	public void setStretch(double stretch){
		LOGGER.trace("Method setStretch(" + stretch + ") called.");
		mStretch = stretch;
		mLayout.setStretch(mStretch);
	}

	public double getStretch(){
		LOGGER.trace("Method getStretch() called.");
		return mStretch;
	}

	public void setRepulsionRange(int repulsionRange){
		LOGGER.trace("Method setRepulsionRange(" + repulsionRange + ") called.");
		mRepulsionRange = repulsionRange;
		mLayout.setRepulsionRange(mRepulsionRange);
	}

	public int getRepulsionRange(){
		LOGGER.trace("Method getRepulsionRange() called.");
		return mRepulsionRange;
	}

	public void setForceMultiplier(double forceMultiplier){
		LOGGER.trace("Method setForceMultiplier(" + forceMultiplier + ") called.");
		mForceMultiplier = forceMultiplier;
		mLayout.setForceMultiplier(mForceMultiplier);
	}

	public double getForceMultiplier(){
		LOGGER.trace("Method getForceMultiplier() called.");
		return mForceMultiplier;
	}



	// /////////////////////////////////////////////////////////////////////////////////////// SUPER

	/**
	 * Adjust (align) the graph.
	 * @param iterations Number of iterations the layout does to adjust the layout (nodes of the
	 *        graph).
	 */
	@Override
	public void adjust(int iterations){
		LOGGER.trace("Method adjust(" + iterations + ") called.");
		for(int i = 0; i < iterations; i++){
			mLayout.step();
		}
	}

	/**
	 * Calculate the uniform length for all edges.
	 */
	@Override
	public void calculateUniformEdgeLength(){
		LOGGER.trace("Method getUniformEdgeLength() called.");
		double nodesPerLine = Math.sqrt(mGraph.getAllNodesCount());
		mUniformEdgeLength = (int)((getDimensionX()/2.0)/nodesPerLine);
	}

	/**
	 * Reset the graph after adding/removing elements.
	 */
	@Override
    public void reset(){
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
    public void setMaxIterations(int maxIterations){
		// Not necessary for Spring-Layout, it never stops autonomously.
    }

	@Override
    public int getMaxIterations(){
		// Not necessary for Spring-Layout, it never stops autonomously.
	    return 0;
    }

	// ///////////////////////////////////////////////////////////////////////////////////// PRIVATE

	/**
	 * Determines the length of the edge with the maximum length.
	 *
	 * @return The length of the edge with the maximum length.
	 */
	@SuppressWarnings("unused")
	private int calculateMaxEdgeLength(){
		LOGGER.trace("Method calculateMaxEdgeLength() called.");
		int maxEdgeLength = 0;
		int curLength;
		//MetadataCollocation expandedEdgeType = mGraph.getMetadataCollocationLink();
		for(ExpandedLink2D ee : mGraph.getExpandedLinks2D()){
			curLength = ee.getTotalLength();
			if(curLength > maxEdgeLength){
				maxEdgeLength = curLength;
			}
		}
		return maxEdgeLength;
	}











}
