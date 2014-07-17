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
 * This file is part of visitmeta visualization, version 0.1.0,
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
package de.hshannover.f4.trust.visitmeta.graphCalculator.jung;





import java.awt.Dimension;
import org.apache.log4j.Logger;




/**
 * A super-class to represent a layout.
 */
public abstract class Layout2D{

	// ///////////////////////////////////////////////////////////////////////////////////// MEMBERS

	protected Graph2D                  mGraph;
	protected Dimension                mDimension;
	protected MinBoundingBox2D         mMinBoundingBox;

	protected boolean mUseIndividualEdgeLength;
	protected int mUniformEdgeLength;

	private final double GRAPH_SIZE_FACTOR = 2.0 / 3.0;
	private static final Logger LOGGER = Logger.getLogger(Layout2D.class);

	// //////////////////////////////////////////////////////////////////////////////// CONSTRUCTORS

	public Layout2D(Graph2D graph, boolean useIndividualEdgeLength){
		mGraph = graph;
		mUseIndividualEdgeLength = useIndividualEdgeLength;
		mUniformEdgeLength = 0;
	}

	// ////////////////////////////////////////////////////////////////////// PUBLIC - GETTER SETTER


	public Dimension getDimension(){
		LOGGER.trace("Method getGraphDimension() called.");
		return mDimension;
	}

	public double getDimensionX(){
		LOGGER.trace("Method getDimensionX() called.");
		return mDimension.getWidth();
	}

	public double getDimensionY(){
		LOGGER.trace("Method getDimensionY() called.");
		return mDimension.getHeight();
	}

//	public int getIterations(){
//		LOGGER.trace("Method getIterations() called.");
//		return mIterations;
//	}
//
//	public void setIterations(int iterations){
//		LOGGER.trace("Method setIterations(" + iterations + ") called.");
//		mIterations = iterations;
//	}

	/**
	 * Get the information if edges have an individual length.
	 * @return true if all edges has an individual length, otherwise false
	 */
	public boolean useIndividualEdgeLength(){
		LOGGER.trace("Method useIndividualEdgeLength() called.");
		return mUseIndividualEdgeLength;
	}

	/**
	 * Set the state that all edges use an individual edge length.
	 * @param individualEdgeLength
	 */
	public void useIndividualEdgeLength(boolean individualEdgeLength){
		LOGGER.trace("Method useIndividualEdgeLength(" + individualEdgeLength + ") called.");
		mUseIndividualEdgeLength = individualEdgeLength;
	}

	public int getUniformEdgeLength(){
		LOGGER.trace("Method getUniformEdgeLength() called.");
		return mUniformEdgeLength;
	}

	public void setUniformEdgeLength(int uniformEdgeLEngth){
		LOGGER.trace("Method setUniformEdgeLength(" + uniformEdgeLEngth + ") called.");
		mUniformEdgeLength = uniformEdgeLEngth;
	}


	// /////////////////////////////////////////////////////////////////////// PROTECTED - DIMENSION

	/**
	 * Calculates the maximum extent (space) the graph needs.
	 *
	 * @return The maximum extent (space) the graph needs.
	 */
	protected int calculateMaxGraphExtent(){
		LOGGER.trace("Method calculateMaxGraphExtent() called.");

		int maxGraphExtent = 0;
		int curLength;
		//MetadataCollocation expandedEdgeType = mGraph.getMetadataCollocationLink();

		for(ExpandedLink2D ee : mGraph.getExpandedLinks2D()){
			curLength = ee.getTotalLength();
			maxGraphExtent = maxGraphExtent + curLength;
		}
		return maxGraphExtent;
	}

	/**
	 * <p>Sets the graph-dimension.</p>
	 * <p>This is the space, provided for the layout to situate the nodes of the graph.</p>
	 *
	 * @param maxExtend The maximum extent the layout gets to generate its dimension.
	 */
	protected void setGraphDimension(int maxExtent){
		LOGGER.trace("Method setGraphDimension(" + maxExtent + ") called.");
		int x_and_y = MathLib.roundUp(maxExtent * GRAPH_SIZE_FACTOR);
		mDimension.setSize(x_and_y, x_and_y);
	}

	/**
	 * <p>Sets the graph-dimension.</p>
	 * <p>This is the space, provided for the layout to situate the nodes of the graph.</p>
	 *
	 * @param sizeX Size in x-direction for the dimension.
	 * @param sizeY Size in y-direction for the dimension.
	 */
	protected void setGraphDimension(int sizeX, int sizeY){
		LOGGER.trace("Method setGraphDimension(" + sizeX + ", " + sizeY + ") called.");
		mDimension.setSize(sizeX, sizeY);
	}


	// //////////////////////////////////////////////////////////////////////////////////// ABSTRACT

	public abstract void adjust(int iterations);
	public abstract void calculateUniformEdgeLength();
	public abstract void reset();
	public abstract void setNodePosition(Node2D node2D, double x, double y);
	public abstract double getNodePositionX(Node2D node2D);
	public abstract double getNodePositionY(Node2D node2D);
	public abstract void lockNode(Node2D node2D);
	public abstract void unlockNode(Node2D node2D);
	public abstract void lockAllNodes();
	public abstract void unlockAllNodes();
	public abstract void setMaxIterations(int maxIterations);
	public abstract int getMaxIterations();

}
