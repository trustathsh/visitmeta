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
 * This file is part of visitmeta-visualization, version 0.5.0,
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

/**
 * A 2D minimum bounding box.
 */
public class MinBoundingBox2D {

	// ///////////////////////////////////////////////////////////////////////////////////// MEMBERS

	private double minX;
	private double minY;
	private double maxX;
	private double maxY;
	private double width;
	private double height;

	private static final Logger LOGGER = Logger.getLogger(MinBoundingBox2D.class);

	// //////////////////////////////////////////////////////////////////////////////// CONSTRUCTORS

	public MinBoundingBox2D(double minX, double minY, double maxX, double maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		width = maxX
				- minX;
		height = maxY
				- minY;
	}

	public MinBoundingBox2D(Graph2D graph) {

		double minX = graph.getMaxDimension();
		double minY = graph.getMaxDimension();
		double maxX = 0.0;
		double maxY = 0.0;
		double tempX;
		double tempY;

		for (Node2D node : graph.getAllNodes2D()) {
			tempX = node.getX();
			tempY = node.getY();
			if (tempX < minX) {
				minX = tempX;
			}
			if (tempX > maxX) {
				maxX = tempX;
			}
			if (tempY < minY) {
				minY = tempY;
			}
			if (tempY > maxY) {
				maxY = tempY;
			}
		}

		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		width = maxX
				- minX;
		height = maxY
				- minY;
	}

	// ////////////////////////////////////////////////////////////////////////////////////// PUBLIC

	public double getMinX() {
		LOGGER.trace("Method getMinX() called.");
		return minX;
	}

	public double getMinY() {
		LOGGER.trace("Method getMinY() called.");
		return minY;
	}

	public double getMaxX() {
		LOGGER.trace("Method getMaxX() called.");
		return maxX;
	}

	public double getMaxY() {
		LOGGER.trace("Method getMaxY() called.");
		return maxY;
	}

	public double getWidth() {
		LOGGER.trace("Method getWidth() called.");
		return width;
	}

	public double getHeight() {
		LOGGER.trace("Method getHeight() called.");
		return height;
	}

	public double getCenterX() {
		LOGGER.trace("Method getCenterX() called.");
		return minX
				+ (width
						/ 2.0);
	}

	public double getCenterY() {
		LOGGER.trace("Method getCenterY() called.");
		return minY
				+ (height
						/ 2.0);
	}

	public void printInfo() {
		LOGGER.trace("Method printInfo() called.");
		LOGGER.error("MinBoundBox: ");
		LOGGER.error("min("
				+ minX + ", " + minY + ") ");
		LOGGER.error("max("
				+ maxX + ", " + maxY + ") ");
		LOGGER.error("dim("
				+ width + ", " + height + ") ");
		LOGGER.error("cen("
				+ getCenterX() + ", " + getCenterY() + ") ");
	}

}
