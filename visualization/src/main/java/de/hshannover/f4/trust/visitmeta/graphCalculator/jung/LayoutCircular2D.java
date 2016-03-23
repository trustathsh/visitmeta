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
/**
 * 
 * This implementation uses the class StaticLayout of JUNG2 for consistency.
 * @author wuju
 * 
 */
package de.hshannover.f4.trust.visitmeta.graphCalculator.jung;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.graphCalculator.MetadataCollocation;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;

public class LayoutCircular2D extends Layout2D {

	protected StaticLayout<Node2D, Edge2D> mLayout;
	
	private double mRadius;	// TODO use the radius ...
	
	private static final Logger LOGGER = Logger.getLogger(LayoutCircular2D.class);

	public LayoutCircular2D(Graph2D graph) {
		super(graph, true);

		mDimension = new Dimension(1_000_000_000, 1_000_000_000);
		mRadius = 500_000_000;

		mLayout = new StaticLayout<>(mGraph.getGraph());
		mLayout.setSize(mDimension);

		mGraph.alterMetadataCollocationForEntireGraph(MetadataCollocation.FORK);
	}

	/**
	 * Set radius method
	 * 
	 * @param radius
	 */
	public void setRadius(double radius) {
		mRadius = radius;
	}

	/**
	 * Get radius method
	 * 
	 * @return
	 */
	public double getRadius() {
		return mRadius;
	}

	/**
	 * Draw identifier nodes
	 * 
	 * @param nodes
	 */
	private void drawNodeIdentifier(ArrayList<NodeIdentifier2D> nodes) {
		int i = 0;
		for (NodeIdentifier2D node2D : nodes) {
			double radius = getRadius();
			double angle = (2 * Math.PI * i) / nodes.size();
			double width = mDimension.getWidth();
			double height = mDimension.getHeight();
			setNodePosition(node2D, Math.cos(angle) * radius + width / 2, Math.sin(angle) * radius + height / 2);

			/*
			 * List<NodeMetadata2D> nodesMe = node2D.getNodesMetadata2D();
			 * for(NodeMetadata2D nodeMe:nodesMe) {
			 * drawNodeMetadata(nodeMe,angle); }
			 */

			i++;
		}

	}

	/**
	 * Draw metadata nodes
	 * 
	 * @param nodes
	 */
	private void drawNodeMetadata(ArrayList<NodeMetadata2D> nodes) {
		int i = 0;
		for (NodeMetadata2D node2D : nodes) {

			double angle = (2 * Math.PI * i) / nodes.size();
			double width = mDimension.getWidth();
			double height = mDimension.getHeight();
			double radius = getRadius();

			if (node2D.getExpandedLink2D() != null) {
				radius = getRadius() / 2;
				setNodePosition(node2D, Math.cos(angle) * radius + width / 2, Math.sin(angle) * radius + height / 2);
			} else {
				angle = Math.acos((node2D.getNodeIdentifier2D().getX() - width / 2) / getRadius());
				radius = getRadius() * 3 / 2;
				double x = Math.cos(angle) * radius + width / 2;
				double y = Math.sin(angle) * radius + height / 2;
				setNodePosition(node2D, x, y);

			}

			i++;
		}

	}

//	private void drawNodeMetadata(NodeMetadata2D nodeMe, double angle) {
//		if (nodeMe.getExpandedLink2D() != null) {
//			double radius = getRadius() / 2;
//			double width = mDimension.getWidth();
//			double height = mDimension.getHeight();
//			setNodePosition(nodeMe, Math.cos(angle) * radius + width / 2, Math.sin(angle) * radius + height / 2);
//		} else if (nodeMe.getNodeIdentifier2D() != null) {
//			double radius = getRadius() * 2;
//			double width = mDimension.getWidth();
//			double height = mDimension.getHeight();
//			setNodePosition(nodeMe, Math.cos(angle) * radius + width / 2, Math.sin(angle) * radius + height / 2);
//		}
//
//	}

	/**
	 * Adjust layout
	 * 
	 * @param iterations
	 *            unused for circle-layout
	 * 
	 */
	@Override
	public void adjust(int iterations) {
		LOGGER.trace("Method adjust(" + iterations + ") called.");
		Collection<Node2D> nodes = mGraph.getGraph().getVertices();
		ArrayList<NodeIdentifier2D> identifiers = new ArrayList<NodeIdentifier2D>();
		ArrayList<NodeMetadata2D> metadatas = new ArrayList<NodeMetadata2D>();
		for (Node2D node2D : nodes) {
			if (node2D instanceof NodeIdentifier2D) {
				identifiers.add((NodeIdentifier2D) node2D);
			} else {
				metadatas.add((NodeMetadata2D) node2D);
			}
		}
		drawNodeIdentifier(identifiers);
		drawNodeMetadata(metadatas);

	}

	/**
	 * This method does nothing for Circle-Layout. Defining an edge length is
	 * only necessary for Spring-Layout.
	 */
	@Override
	public void calculateUniformEdgeLength() {
		LOGGER.trace("Method calculateUniformEdgeLength() called.");
		// DO NOTHING!!!
		// Only necessary for SpringLayout.
	}

	/**
	 * Reset the graph after adding/removing elements.
	 */
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
		LOGGER.trace("Method getIterations() called.");
		return 0;
	}

	@Override
	public void setMaxIterations(int maxIterations) {
		LOGGER.trace("Method setIterations(" + maxIterations + ") called.");

	}

}
