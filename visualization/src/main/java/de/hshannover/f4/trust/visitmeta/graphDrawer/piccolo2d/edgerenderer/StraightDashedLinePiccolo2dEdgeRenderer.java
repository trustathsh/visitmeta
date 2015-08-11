package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.edgerenderer;

import java.awt.BasicStroke;
import java.awt.geom.Point2D;

import org.piccolo2d.nodes.PPath;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * {@link Piccolo2dEdgeRenderer} that draws edges as straight dashed lines.
 *
 * @author Bastian Hellmann
 *
 */
public class StraightDashedLinePiccolo2dEdgeRenderer implements Piccolo2dEdgeRenderer {

	@Override
	public void drawEdge(PPath pEdge, Point2D vStart, Point2D vEnd, Metadata metadata,
			Identifier identifier) {
		drawStraightDashedLine(pEdge, vStart, vEnd);
	}

	/**
	 * Draws a straight dashed line.
	 *
	 * @param pEdge
	 *            the {@link PPath} object representing the edge
	 * @param vStart
	 *            the starting point of the edge
	 * @param vEnd
	 *            the end point of the edge
	 */
	public static void drawStraightDashedLine(PPath pEdge, Point2D vStart, Point2D vEnd) {
		float[] dash = {10.0f};
		pEdge.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
		pEdge.moveTo((float) vStart.getX(), (float) vStart.getY());
		pEdge.lineTo((float) vEnd.getX(), (float) vEnd.getY());
	}

}
