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
 * This file is part of visitmeta-visualization, version 0.4.1,
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





import org.apache.log4j.Logger;
import de.hshannover.f4.trust.visitmeta.datawrapper.Position;



/**
 * <p>Super-node-class to represent a node.</p>
 * <p>The layout uses this class for its nodes.</p>
 */
public abstract class Node2D{

	// ///////////////////////////////////////////////////////////////////////////////////// MEMBERS


	protected static int        idCount = 0;
	protected int               mId;

	private Position            mPiccoloPosition;
	protected Layout2D          mLayout2D;

	private Edge2D              mEdge2DIn;
	private Edge2D              mEdge2DOut;
	private Edge2D              mEdge2DForkIn;
	private Edge2D              mEdge2DForkOut;

	private boolean mAdjustPermission;
	private boolean mWasPicked;

	private static final Logger LOGGER = Logger.getLogger(Node2D.class);

	// //////////////////////////////////////////////////////////////////////////////// CONSTRUCTORS

	public Node2D(Position piccoloPosition, Layout2D layout2D){
		mId = ++idCount;
		mPiccoloPosition = piccoloPosition;
		mLayout2D        = layout2D;
		mEdge2DIn        = null;
		mEdge2DOut       = null;
		mEdge2DForkIn    = null;
		mEdge2DForkOut   = null;

		mAdjustPermission = true;
	}

	// /////////////////////////////////////////////////////////////////////// PUBLIC - SET POSITION

	public void setPositionTriggeredByJung(Node2D nodePositionToGet){
		LOGGER.trace("Method setPositionTriggeredByJung(" +nodePositionToGet+ ") called.");
		double x = nodePositionToGet.getX();
		double y = nodePositionToGet.getY();
		this.setPositionTriggeredByJung(x, y);
	}


	public void setPositionTriggeredByJung(double x, double y){
		LOGGER.trace("Method setPositionTriggeredByJung(" + x + ", " + y + ") called.");
		mLayout2D.unlockNode(this);
		mLayout2D.setNodePosition(this, x, y);
		mLayout2D.lockNode(this);
		double piccoloX = x / mLayout2D.getDimensionX();
		double piccoloY = y / mLayout2D.getDimensionY();
		mPiccoloPosition.setPosition(piccoloX, piccoloY, 0.0);
	}


	public void setNodePositionInPiccolo(){
		LOGGER.trace("Method setPositionAfterAdjust() called.");
		double x = mLayout2D.getNodePositionX(this) / mLayout2D.getDimensionX();
		double y = mLayout2D.getNodePositionY(this) / mLayout2D.getDimensionY();
		mPiccoloPosition.setPosition(x, y, 0.0);
	}


	/**
	 * <p>Sets the position of a node in the calculation-layer (JUNG2).</p>
	 * <p>Should be called after a node was picked (moved) in the graphical-layer (piccolo).</p>
	 *
	 * @param x The x-position of the node in the graphical-layer (piccolo).
	 * @param y The y-position of the node in the graphical-layer (piccolo).
	 */
	public void setPositionTriggeredByPiccolo(double x, double y){
		LOGGER.trace("Method setPositionTriggeredByPiccolo(" + x + ", " + y + ") called.");
		double jungX = x * mLayout2D.getDimensionX();
		double jungY = y * mLayout2D.getDimensionY();
		mLayout2D.unlockNode(this);
		mLayout2D.setNodePosition(this, jungX, jungY);
		mLayout2D.lockNode(this);
	}





	// //////////////////////////////////////////////////////////////////////////////////// ABSTRACT

	/**
	 * @return The content-String of the node.
	 */
	public abstract String getContent();

	/**
	 * @return The length (number of characters) of the content-String of the node.
	 */
	public abstract int getContentLength();



	// ////////////////////////////////////////////////////////////////////// PUBLIC - GETTER SETTER

	/**
	 * @return The x-position of the node in the calculation-layer (JUNG2).
	 */
	public double getX(){
		LOGGER.trace("Method getX() called.");
		return mLayout2D.getNodePositionX(this);
	}

	/**
	 * @return The y-position of the node in the calculation-layer (JUNG2).
	 */
	public double getY(){
		LOGGER.trace("Method getY() called.");
		return mLayout2D.getNodePositionY(this);
	}

	public boolean hasAdjustPermission(){
		LOGGER.trace("Method hasAdjustPermission() called.");
		return mAdjustPermission;
	}

	public void setAdjustPermission(boolean adjustPermission){
		LOGGER.trace("Method adjustPermission(" + adjustPermission + ") called.");
		mAdjustPermission = adjustPermission;
	}

	public boolean wasPicked(){
		LOGGER.trace("Method wasPicked() called.");
		return mWasPicked;
	}

	public void setPicked(boolean picked){
		LOGGER.trace("Method setPicked(" + picked + ") called.");
		mWasPicked = picked;
	}

	public Edge2D getEdgeIn(){
		LOGGER.trace("Method getEdgeIn() called.");
    	return mEdge2DIn;
    }

	public void setEdgeIn(Edge2D edge){
		LOGGER.trace("Method setEdgeIn(" + edge + ") called.");
    	mEdge2DIn = edge;
    }

	public Edge2D getEdgeOut(){
		LOGGER.trace("Method getEdgeOut() called.");
    	return mEdge2DOut;
    }

	public void setEdgeOut(Edge2D edge){
		LOGGER.trace("Method setEdgeOut(" + edge + ") called.");
    	mEdge2DOut = edge;
    }

	public Edge2D getForkEdgeIn(){
		LOGGER.trace("Method getForkEdgeIn() called.");
    	return mEdge2DForkIn;
    }

	public void setForkEdgeIn(Edge2D edge){
		LOGGER.trace("Method setForkEdgeIn(" + edge + ") called.");
    	mEdge2DForkIn = edge;
    }

	public Edge2D getForkEdgeOut(){
		LOGGER.trace("Method getForkEdgeOut() called.");
    	return mEdge2DForkOut;
    }

	public void setForkEdgeOut(Edge2D edge){
		LOGGER.trace("Method setForkEdgeOut(" + edge + ") called.");
    	mEdge2DForkOut = edge;
    }

	public Layout2D getLayout() {
		return mLayout2D;
	}
	
	public void setLayout(Layout2D layout2D) {
		mLayout2D = layout2D;
	}

}
