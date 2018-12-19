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

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;
import edu.uci.ics.jung.algorithms.layout.KKLayout;

/**
 * <p>Uses the KKLayout of JUNG2, JUNG2 uses a Kamada-Kawai-Layout.</p>
 */
public class LayoutKamadaKawai2D extends Layout2D{

	// ///////////////////////////////////////////////////////////////////////////////////// MEMBERS

	protected KKLayout<Node2D, Edge2D> mLayout;

	private Properties mConfig = Main.getConfig();

	private boolean mAdjustForGravity;
	private double mDisconnectedDistanceMultiplier;
	private boolean mExchangeVertices;
	private double mLengthFactor;
	
	/**
	 * How many iterations this algorithm does before stopping.
	 */
	private int mMaxIterations;

	private static final Logger LOGGER = Logger.getLogger(LayoutKamadaKawai2D.class);

	// //////////////////////////////////////////////////////////////////////////////// CONSTRUCTORS

	public LayoutKamadaKawai2D(Graph2D graph){
		super(graph, true);
		
		mDimension = new Dimension(1_000_000_000, 1_000_000_000);
		mAdjustForGravity = mConfig.getBoolean(VisualizationConfig.KEY_CALCULATION_JUNG_KAMADAKAWAI_ADJUSTFORGRAVITY, VisualizationConfig.DEFAULT_VALUE_CALCULATION_JUNG_KAMADAKAWAI_ADJUSTFORGRAVITY);
		mDisconnectedDistanceMultiplier = mConfig.getDouble(VisualizationConfig.KEY_CALCULATION_JUNG_KAMADAKAWAI_DISCONNECTEDDISTANCEMULTIPLIER, VisualizationConfig.DEFAULT_VALUE_CALCULATION_JUNG_KAMADAKAWAI_DISCONNECTEDDISTANCEMULTIPLIER);
		mExchangeVertices = mConfig.getBoolean(VisualizationConfig.KEY_CALCULATION_JUNG_KAMADAKAWAI_EXCHANGEVERTICES, VisualizationConfig.DEFAULT_VALUE_CALCULATION_JUNG_KAMADAKAWAI_EXCHANGEVERTICES);
		mLengthFactor = mConfig.getDouble(VisualizationConfig.KEY_CALCULATION_JUNG_KAMADAKAWAI_LENGTHFACTOR, VisualizationConfig.DEFAULT_VALUE_CALCULATION_JUNG_KAMADAKAWAI_LENGTHFACTOR);
		mMaxIterations = mConfig.getInt(VisualizationConfig.KEY_CALCULATION_JUNG_KAMADAKAWAI_MAX_ITERATIONS, VisualizationConfig.DEFAULT_VALUE_CALCULATION_JUNG_KAMADAKAWAI_MAX_ITERATIONS);

		mLayout = new KKLayout<>(mGraph.getGraph());

		mLayout.setAdjustForGravity(mAdjustForGravity);
		mLayout.setDisconnectedDistanceMultiplier(mDisconnectedDistanceMultiplier);
		mLayout.setExchangeVertices(mExchangeVertices);
		mLayout.setLengthFactor(mLengthFactor);
		mLayout.setMaxIterations(mMaxIterations);
		mLayout.setSize(mDimension);
	}

	// ////////////////////////////////////////////////////////////////////////////////////// PUBLIC

	// /////////////////////////////////////////////////////////////////////////////////////// SUPER

	/**
	 * Adjust (align) the graph.
	 * @param iterations Number of iterations the layout does to adjust the layout (nodes of the
	 *        graph).
	 */
	@Override
    public void adjust(int iterations){
		LOGGER.trace("Method adjust(" + iterations + ") called.");
		if (mLayout.getGraph().getVertices().size() > 1) {			
			mLayout.initialize();
			mLayout.reset();
			for(int i = 0; i < iterations; i++){
				mLayout.step();
			}
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
