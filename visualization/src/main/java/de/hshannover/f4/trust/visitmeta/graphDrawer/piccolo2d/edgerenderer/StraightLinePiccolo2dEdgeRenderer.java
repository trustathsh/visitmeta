package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.edgerenderer;

import java.awt.geom.Point2D;

import org.piccolo2d.nodes.PPath;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * {@link Piccolo2dEdgeRenderer} that draws edges as straight lines.
 *
 * @author Bastian Hellmann
 *
 */
public class StraightLinePiccolo2dEdgeRenderer implements Piccolo2dEdgeRenderer {

	@Override
	public void drawEdge(PPath pEdge, Point2D vStart, Point2D vEnd, Metadata metadata,
			Identifier identifier) {
		drawStraightLine(pEdge, vStart, vEnd);
	}

	/**
	 * Draws a straight line.
	 *
	 * @param pEdge
	 *            the {@link PPath} object representing the edge
	 * @param vStart
	 *            the starting point of the edge
	 * @param vEnd
	 *            the end point of the edge
	 */
	public static void drawStraightLine(PPath pEdge, Point2D vStart, Point2D vEnd) {
		pEdge.moveTo((float) vStart.getX(), (float) vStart.getY());
		pEdge.lineTo((float) vEnd.getX(), (float) vEnd.getY());
	}

}
