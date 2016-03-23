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
