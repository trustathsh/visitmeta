package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.edgerenderer;

import java.awt.geom.Point2D;

import de.hshannover.f4.trust.visitmeta.IfmapStrings;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import edu.umd.cs.piccolo.nodes.PPath;

public class ExamplePiccolo2dEdgeRenderer implements Piccolo2dEdgeRenderer {

	@Override
	public void drawEdge(PPath pEdge, Point2D vStart, Point2D vEnd, Metadata metadata,
			Identifier identifier) {
		if ((identifier.getTypeName().equals(IfmapStrings.IP_ADDRESS_EL_NAME)
				||
				identifier.getTypeName().equals(IfmapStrings.MAC_ADDRESS_EL_NAME))
				&& metadata.getTypeName().equals("ip-mac")) {
			StraightDashedLinePiccolo2dEdgeRenderer.drawStraightDashedLine(pEdge, vStart, vEnd);
		} else {
			StraightLinePiccolo2dEdgeRenderer.drawStraightLine(pEdge, vStart, vEnd);
		}
	}

}
