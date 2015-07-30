package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.renderer;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class RectanglesWithRounderCornersPiccolo2dRenderer implements Piccolo2dNodeRenderer {

	@Override
	public PPath createNode(PText text) {
		return PPath.createRoundRectangle(-5
				- 0.5F
						* (float) text.getWidth(), // x
				-5
						- 0.5F
								* (float) text.getHeight(), // y
				(float) text.getWidth()
						+ 10, // width TODO Add offset
				(float) text.getHeight()
						+ 10, // height TODO Add offset
				20.0f, // arcWidth TODO Design variable
				20.0f // arcHeight TODO Design variable
		);
	}
}
