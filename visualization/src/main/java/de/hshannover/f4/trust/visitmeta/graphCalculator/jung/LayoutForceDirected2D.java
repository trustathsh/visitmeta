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
 * This file is part of visitmeta visualization, version 0.0.5,
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
import edu.uci.ics.jung.algorithms.layout.FRLayout;



/**
 * <p>A force-directed-layout.</p>
 * <p>Uses the FRLayout of JUNG2, JUNG2 uses a Fruchterman-Reingold-Layout.</p>
 */
public class LayoutForceDirected2D extends Layout2D{

	// ///////////////////////////////////////////////////////////////////////////////////// MEMBERS

	protected FRLayout<Node2D, Edge2D> mLayout;

	/**
	 * <p>How much edges try to keep their nodes together.</p>
	 * <p>Default: 0.25</p>
	 */
	private double mAttractionMultiplier;

	/**
	 * <p>How much nodes try to push each other apart.</p>
	 * <p>Default: 0.75</p>
	 */
	private double mRepulsionMultiplier;

	/**
	 * How many iterations this algorithm does before stopping.
	 */
	private int mMaxIterations;

	private static final Logger LOGGER = Logger.getLogger(LayoutForceDirected2D.class);

	// //////////////////////////////////////////////////////////////////////////////// CONSTRUCTORS

	public LayoutForceDirected2D(Graph2D graph){
		super(graph, true);

		mAttractionMultiplier = 0.25;
		mRepulsionMultiplier = 0.75;
		mDimension = new Dimension(1_000_000_000, 1_000_000_000);
		mMaxIterations = 500;

		mLayout = new FRLayout<>(mGraph.getGraph());

		mLayout.setAttractionMultiplier(mAttractionMultiplier);
		mLayout.setRepulsionMultiplier(mRepulsionMultiplier);
		mLayout.setMaxIterations(mMaxIterations);
		mLayout.setSize(mDimension);
	}


	public LayoutForceDirected2D(Graph2D graph, double attractionMultiplier,
			double repulsionMultiplier){
		super(graph, true);

		mAttractionMultiplier = attractionMultiplier;
		mRepulsionMultiplier = repulsionMultiplier;
		mDimension = new Dimension(1_000_000_000, 1_000_000_000);
		mMaxIterations = 500;

		mLayout = new FRLayout<>(mGraph.getGraph());

		mLayout.setAttractionMultiplier(mAttractionMultiplier);
		mLayout.setRepulsionMultiplier(mRepulsionMultiplier);
		mLayout.setMaxIterations(mMaxIterations);
		mLayout.setSize(mDimension);

	}

	// ////////////////////////////////////////////////////////////////////////////////////// PUBLIC

	public double getAttractionMultiplier(){
		LOGGER.trace("Method getAttractionMultiplier() called.");
		return mAttractionMultiplier;
	}

	public void setAttractionMultiplier(double attractionMultiplier){
		LOGGER.trace("Method setAttractionMultiplier(" + attractionMultiplier + ") called.");
		mAttractionMultiplier = attractionMultiplier;
		mLayout.setAttractionMultiplier(mAttractionMultiplier);
	}

	public double getRepulsionMultiplier(){
		LOGGER.trace("Method getRepulsionMultiplier() called.");
		return mRepulsionMultiplier;
	}

	public void setRepulsionMultiplier(double repulsionMultiplier){
		LOGGER.trace("Method setRepulsionMultiplier(" + repulsionMultiplier + ") called.");
		mRepulsionMultiplier = repulsionMultiplier;
		mLayout.setRepulsionMultiplier(mRepulsionMultiplier);
	}

	/**
	 * Set repulsion of nodes by percentage.
	 * Attraction an repulsion multiplier are set by calling this method.
	 * @param percent How much nodes repel (1 to 99 percent)
	 */
	public void setNodeRepulsion(int percent){
		LOGGER.trace("Method setNodeRepulsion(" + percent + ") called.");
		if(percent <= 0){
			percent = 1;
		}
		else if(percent >= 100){
			percent = 99;
		}

		double repulsion = percent / 100.0;
		double attraction = 0.99 - repulsion;

		setAttractionMultiplier(attraction);
		setRepulsionMultiplier(repulsion);

		reset();
	}

	/**
	 * Set repulsion of nodes by percentage temporary.
	 * Members attraction- and repulsion-multiplier will not be overwritten.
	 * Old state can be set by calling setOldAttractionRepulsion().
	 * Attraction- an repulsion-multiplier are set by calling this method.
	 * @param percent How much nodes repel (1 to 99 percent)
	 */
	public void setNodeRepulsionTemporary(int percent){
		LOGGER.trace("Method setNodeRepulsionTemporary(" + percent + ") called.");
		if(percent <= 0){
			percent = 1;
		}
		else if(percent >= 100){
			percent = 99;
		}

		double repulsion = percent / 100.0;
		double attraction = 0.99 - repulsion;

		mLayout.setAttractionMultiplier(attraction);
		mLayout.setRepulsionMultiplier(repulsion);

		reset();
	}

	/**
	 * Set default attraction- and repulsion-multiplier.
	 * Attraction-Multiplier: 0.25
	 * Repulsion-Multiplier:  0.75
	 */
	public void setDefaultAttractionRepulsion(){
		LOGGER.trace("Method setDefaultAttractionRepulsion() called.");
		setAttractionMultiplier(0.25);
		setRepulsionMultiplier(0.75);
		reset();
	}

	/**
	 * Can be called after setNodeRepulsionTemporary(percent) was called.
	 * Will retrieve old attraction- and repulsion state before
	 * calling setNodeRepulsionTemporary(percent).
	 */
	public void setOldAttractionRepulsion(){
		LOGGER.trace("Method setOldAttractionRepulsion() called.");
		setAttractionMultiplier(mAttractionMultiplier);
		setRepulsionMultiplier(mRepulsionMultiplier);
		reset();
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
	 * This method does nothing for Force-Directed-Layout.
	 * Defining an edge length is only necessary for Spring-Layout.
	 */
	@Override
    public void calculateUniformEdgeLength(){
		LOGGER.trace("Method calculateUniformEdgeLength() called.");
		// DO NOTHING!!!
		// Only necessary for SpringLayout.
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
    public int getMaxIterations(){
		LOGGER.trace("Method getIterations() called.");
		return mMaxIterations;
	}

	@Override
    public void setMaxIterations(int maxIterations){
		LOGGER.trace("Method setIterations(" + maxIterations + ") called.");
		mMaxIterations = maxIterations;
		mLayout.setMaxIterations(maxIterations);
	}

}
