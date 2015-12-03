package de.hshannover.f4.trust.visitmeta.graphCalculator.jung;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import edu.uci.ics.jung.algorithms.layout.StaticLayout;

public class LayoutForceDirectedAlternative2D extends Layout2D {
	
	private class EnhancedNode2D {

		Node2D mNode;
		Vector2D mVelocity;
		Point2D mNextPosition;
		Vector2D mCurrentPosition;
		
		public EnhancedNode2D(Node2D node) {
			mNode = node;
			mVelocity = new Vector2D();
			mNextPosition = new Point2D();
			mCurrentPosition = calculateVectorFromPoint(new Point2D(mNode.getX(), mNode.getY()));
		}
		
	}
	
	private class Point2D {
		double mX;
		double mY;
		
		public Point2D(double x, double y) {
			mX = x;
			mY = y;
		}

		public Point2D() {
			mX = 0.0;
			mY = 0.0;
		}
		
		@Override
		public String toString() {
			return "Point2D(x: " + mX + ", y: " + mY + ")";
		}
	}
	
	private class Vector2D {
		double mMagnitude;
		double mAngle;
		
		public Vector2D(double magnitude, double angle) {
			mMagnitude = magnitude;
			mAngle = angle;
			
			if (mMagnitude < 0.0) {
				mMagnitude = -1.0 * mMagnitude;
				mAngle = (180.0 + mAngle) % 360;
			}
			
			if (mAngle < 0.0) {
				mAngle = (360.0 + mAngle);
			}
		}
		
		public Vector2D() {
			mMagnitude = 0.0;
			mAngle = 0.0;
		}

		public Vector2D add(Vector2D other) {
			double resultX = this.mMagnitude * Math.cos(Math.PI / 180.0) * this.mAngle;
			double resultY = this.mMagnitude * Math.sin(Math.PI / 180.0) * this.mAngle;;
			
			double otherX = other.mMagnitude * Math.cos(Math.PI / 180.0) * other.mAngle;;
			double otherY = other.mMagnitude * Math.cos(Math.PI / 180.0) * other.mAngle;;
			
			resultX += otherX;
			resultY += otherY;
			
			double magnitude = Math.sqrt(Math.pow(resultX, 2) + Math.pow(resultY, 2));
			
			double angle;
	        if (magnitude == 0.0) {
	        	angle = 0.0;
	        } else  {
	        	angle = (180.0 / Math.PI) * Math.atan2(resultY, resultX);
	        }
	        
	        return new Vector2D(magnitude, angle);
		}
		
		public Vector2D multiply(double multiplier) {
			return new Vector2D(this.mMagnitude * multiplier, this.mAngle);
		}
		
		public Point2D toPoint() {
			double x = this.mMagnitude * Math.cos((Math.PI / 180.0) * this.mAngle);
			double y = this.mMagnitude * Math.sin((Math.PI / 180.0) * this.mAngle);

			return new Point2D(x, y);
		}
		
		@Override
		public String toString() {
			return "Vector2D(magnitude: " + mMagnitude + ", angle: " + mAngle + ")";
		}
	}
	
	private static final Logger LOGGER = Logger.getLogger(LayoutForceDirectedAlternative2D.class);

	private static final int DISPLACEMENT_THRESHOLD = 10;
	private static final int MAX_STOP_COUNT = 15;
	private static final double ATTRACTION_CONSTANT = 0.1;
	private static final double REPULSION_CONSTANT = 10000;
	private static final double DAMPING = 0.5;
	private static final int SPRING_LENGTH = 100;

	protected StaticLayout<Node2D, Edge2D> mLayout; // JUNG layout class used for consistency
	
	private List<Node2D> mDrawnNodes; // list of nodes that have already been dra

	private int mMaxIterations;

	public LayoutForceDirectedAlternative2D(Graph2D graph) {
		super(graph, true);
		
		mDimension = new Dimension(1_000_000_000, 1_000_000_000);

		mLayout = new StaticLayout<Node2D, Edge2D>(mGraph.getGraph());
		mLayout.setSize(mDimension);
		
		mDrawnNodes = new ArrayList<Node2D>();
		
		// ensure that metadata collocation is set to FORK
//		mGraph.alterMetadataCollocationForEntireGraph(MetadataCollocation.FORK);
		
		init();
	}

	public Vector2D calculateVectorFromPoint(Point2D point2d) {
		return new Vector2D(calcDistance(point2d),
				getBearingAngle(point2d));
	}

	@Override
	public void adjust(int iterations) {
		LOGGER.trace("Method adjust(" + iterations + ") called.");
		
		mDrawnNodes.clear();
		
		int stopCount = 0;
		
		Collection<Node2D> nodes = mGraph.getGraph().getVertices();
		List<EnhancedNode2D> enhancedNodes = new ArrayList<>();
		
		for (Node2D currentNode : nodes) {
			enhancedNodes.add(new EnhancedNode2D(currentNode));
		}
		
		for (int currentIteration = 0; currentIteration < iterations; currentIteration++) {
			double totalDisplacement = 0;
			
			int n = 0;
			for (EnhancedNode2D currentNode : enhancedNodes) {
				LOGGER.info("Enhanced Node #" + n + "/" + enhancedNodes.size());
				n++;
				
				Vector2D netforce = new Vector2D();
				
				for (Node2D otherNode : nodes) {
					if (otherNode != currentNode.mNode) {
						netforce = netforce.add(calculateRepulsionForce(currentNode.mNode, otherNode));
					}
				}
				
				for (Node2D neighborNode : mGraph.getGraph().getNeighbors(currentNode.mNode)) {
					netforce = netforce.add(calculateAttractionForce(currentNode.mNode, neighborNode));
				}
				
				LOGGER.info("netforce: " + netforce);
				
				currentNode.mVelocity = currentNode.mVelocity.add(netforce).multiply(DAMPING);
				
				currentNode.mNextPosition = currentNode.mCurrentPosition.add(currentNode.mVelocity).toPoint();
			}
			
			for (EnhancedNode2D currentNode : enhancedNodes) {
				totalDisplacement += calcDistance(currentNode.mCurrentPosition.toPoint(), currentNode.mNextPosition);
				currentNode.mCurrentPosition = calculateVectorFromPoint(currentNode.mNextPosition);
			}
			
			LOGGER.info("iteration: " + currentIteration + ", totalDisplacement: " + totalDisplacement + ", stopCount: " + stopCount);
			
			if (totalDisplacement < DISPLACEMENT_THRESHOLD) {
				stopCount++;
			}
			
			if (stopCount > MAX_STOP_COUNT) {
				break;
			}
		}
		
		centerAndMoveNodes(enhancedNodes);
	}

	private void centerAndMoveNodes(List<EnhancedNode2D> enhancedNodes) {
		LOGGER.trace("Method centerAndMoveNodes() called.");
		
		double x = 0.0;
		double y = 0.0;
		
		for (EnhancedNode2D enhancedNode : enhancedNodes) {
			Point2D midPoint = new Point2D(0 + (mDimension.getWidth() / 2), 0 + (mDimension.getHeight() / 2));
			
			x = enhancedNode.mNextPosition.mX - midPoint.mX;
			y = enhancedNode.mNextPosition.mY - midPoint.mY;
			if (enhancedNode.mNode.hasAdjustPermission()
					&& !enhancedNode.mNode.wasPicked()) {
				
				enhancedNode.mNode.setPositionTriggeredByJung(x, y);
			}
		}		
	}

	private Point2D nodeToPoint(Node2D node) {
		return new Point2D(node.getX(), node.getY());
	}
	
	private Vector2D calculateAttractionForce(Node2D node, Node2D other) {
		LOGGER.trace("Method calculateAttractionForce() called.");
		
		int proximity = Math.max(calcDistance(node, other), 1);

		// Hooke's Law: F = -kx
		double force = ATTRACTION_CONSTANT * Math.max(proximity - SPRING_LENGTH, 0);
		double angle = getBearingAngle(node, other);

		return new Vector2D(force, angle);
	}

	private double getBearingAngle(Node2D node, Node2D other) {
		LOGGER.trace("Method getBearingAngle(Node2D node, Node2D other) called.");
		
		Point2D start;
		if (node == null) {
			start = new Point2D(0.0, 0.0);
		} else {
			start = nodeToPoint(node);
		}
		Point2D end = nodeToPoint(other);
		
		return getBearingAngle(start, end);
	}
		
	private double getBearingAngle(Point2D end) {
		return getBearingAngle(new Point2D(), end);
	}
	
	private double getBearingAngle(Point2D start, Point2D end) {
		LOGGER.trace("Method getBearingAngle() called.");
		
		Point2D half = new Point2D(start.mX + ((end.mX - start.mX) / 2), start.mY + ((end.mY - start.mY) / 2));

		double diffX = (double)(half.mX - start.mX);
		double diffY = (double)(half.mY - start.mY);

		if (diffX == 0) diffX = 0.001;
		if (diffY == 0) diffY = 0.001;

		double angle;
		if (Math.abs(diffX) > Math.abs(diffY)) {
			angle = Math.tanh(diffY / diffX) * (180.0 / Math.PI);
			if (((diffX < 0) && (diffY > 0)) || ((diffX < 0) && (diffY < 0))) {
				angle += 180;
			}
		} else {
			angle = Math.tanh(diffX / diffY) * (180.0 / Math.PI);
			if (((diffY < 0) && (diffX > 0)) || ((diffY < 0) && (diffX < 0))) {
				angle += 180;
			}
			angle = (180 - (angle + 90));
		}

		return angle;
	}

	private Vector2D calculateRepulsionForce(Node2D node, Node2D other) {
		LOGGER.trace("Method calculateRepulsionForce() called.");
		
		int proximity = Math.max(calcDistance(node, other), 1);

		// Coulomb's Law: F = k(Qq/r^2)
		double force = -(REPULSION_CONSTANT / Math.pow(proximity, 2));
		double angle = getBearingAngle(node, other);

		return new Vector2D(force, angle);
	}
	
	private int calcDistance(Node2D node, Node2D other) {
		LOGGER.trace("Method calcDistance(Node2D node, Node2D other) called.");
		
		Point2D a;
		if (node == null) {
			a = new Point2D(0.0, 0.0);
		} else {
			a = nodeToPoint(node);
		}
		Point2D b = nodeToPoint(other);

		return calcDistance(a, b);
	}
	
	private int calcDistance(Point2D p2) {
		return calcDistance(new Point2D(), p2);
	}
	
	private int calcDistance(Point2D p1, Point2D p2) {
		LOGGER.trace("Method calcDistance(Point2D p1, Point2D p2) called.");
		
		double xDist = (p1.mX - p2.mX);
		double yDist = (p1.mY - p2.mY);
		
		return (int) Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
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
	public void setNodePosition(Node2D node2D, double x, double y) {
		LOGGER.trace("Method setNodePosition("
				+ node2D + ", " + x + ", " + y + ") called.");
		mLayout.setLocation(node2D, x, y);
	}

	@Override
	public double getNodePositionX(Node2D node2D) {
		LOGGER.trace("Method getNodePositionX("
				+ node2D + ") called.");
		return mLayout.getX(node2D);
	}

	@Override
	public double getNodePositionY(Node2D node2D) {
		LOGGER.trace("Method getNodePositionY("
				+ node2D + ") called.");
		return mLayout.getY(node2D);
	}

	@Override
	public void lockNode(Node2D node2D) {
		LOGGER.trace("Method lockNode("
				+ node2D + ") called.");
		mLayout.lock(node2D, true);
	}

	@Override
	public void unlockNode(Node2D node2D) {
		LOGGER.trace("Method unlockNode("
				+ node2D + ") called.");
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
	public void setMaxIterations(int maxIterations) {
		LOGGER.trace("Method setMaxIterations(" + maxIterations + ") called.");
		mMaxIterations = maxIterations;
	}

	@Override
	public int getMaxIterations() {
		LOGGER.trace("Method getMaxIterations() called.");
		return mMaxIterations;
	}

	private void init() {
		Collection<Node2D> nodes = mGraph.getGraph().getVertices();
		Random random = new Random();
		
		for (Node2D node : nodes) {
			double x = random.nextDouble() * 1000;
			double y = random.nextDouble() * 1000;
			LOGGER.info("x: " + x + ", y: " + y);
			
			node.setPositionTriggeredByJung(x, y);
		}
	}
}
