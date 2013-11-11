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
 * This file is part of VisITMeta, version 0.0.2, implemented by the Trust@FHH 
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

import org.apache.log4j.Logger;



/**
 * Edge-class to represent the connection between two nodes (mStart and mEnd).<br>
 * The layout uses this class for its edges. The length of an edge is only necessary when using
 * LayoutSpring2D.
 */
public class Edge2D{

	// ///////////////////////////////////////////////////////////////////////////////////// MEMBERS

	private Node2D              mStart;
	private Node2D              mEnd;
	private int                 mLength;

	private static final Logger LOGGER = Logger.getLogger(Edge2D.class);

	// //////////////////////////////////////////////////////////////////////////////// CONSTRUCTORS

	public Edge2D(Node2D start, Node2D end){
		mStart = start;
		mEnd = end;
		mLength = 0;
	}


	public Edge2D(Node2D start, Node2D end, int length){
		mStart = start;
		mEnd = end;
		mLength = length;
	}

	// ////////////////////////////////////////////////////////////////////////////////////// PUBLIC

	/**
	 * <p>
	 * Calculates and sets the length of the edge.
	 * </p>
	 * <p>
	 * The length is not an absolute value. It represents the relative distance between the
	 * connected nodes (mStart and dEnd) depending on the content (String) of these nodes.
	 * The length is only necessary when using the spring-layout LayoutSpring2D.
	 * </p>
	 */
	public void calculateAndSetLength(){
		LOGGER.trace("Method calculateAndSetLength() called.");
		int startLength = MathLib.roundUp((mStart.getContentLength()) / 2.0);
		int endLength = MathLib.roundUp((mEnd.getContentLength()) / 2.0);
		mLength = startLength + endLength;
	}

	public Node2D getStart(){
		LOGGER.trace("Method getStart() called.");
		return mStart;
	}

	public Node2D getEnd(){
		LOGGER.trace("Method setEnd() called.");
		return mEnd;
	}

	@Deprecated
	public void setStart(Node2D node){
		LOGGER.trace("Method setStart(" + node + ") called.");
		mStart = node;
	}

	@Deprecated
	public void setEnd(Node2D node){
		LOGGER.trace("Method setEnd(" + node + ") called.");
		mEnd = node;
	}

	public int getLength(){
		LOGGER.trace("Method getLength() called.");
		return mLength;
	}

	public void setLength(int length){
		LOGGER.trace("Method getLength(" + length + ") called.");
		mLength = length;
	}

}
