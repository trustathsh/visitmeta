package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.edgerenderer;

import java.awt.geom.Point2D;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import edu.umd.cs.piccolo.nodes.PPath;

/**
 * {@link Piccolo2dEdgeRenderer} that draws edges as a curved line.
 *
 * @author Bastian Hellmann
 *
 */
public class CurvedLinePiccolo2dEdgeRenderer implements Piccolo2dEdgeRenderer {

	@Override
	public void drawEdge(PPath pEdge, Point2D vStart, Point2D vEnd, Metadata metadata,
			Identifier identifier) {
		float x1 = (float) vStart.getX();
		float y1 = (float) vStart.getY();
		float x3 = (float) vEnd.getX();
		float y3 = (float) vEnd.getY();
		float x2 = Math.abs(x3
				- x1);
		float y2 = Math.abs(y3
				- y1);

		pEdge.moveTo(x1, y1);
		pEdge.curveTo(x1, y1, x2, y2, x3, y3);
	}

}
