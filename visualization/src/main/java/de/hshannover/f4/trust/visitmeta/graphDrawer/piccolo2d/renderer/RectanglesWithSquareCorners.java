package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.renderer;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class RectanglesWithSquareCorners implements Piccolo2dNodeRenderer {

	private float mOffsetWidth = 10.0f;
	private float mOffsetHeight = 10.0f;

	public PPath createNode(PText text) {
		return PPath.createRectangle(
				-5
				- 0.5F
				* (float) text.getWidth(), // x
				-5
				- 0.5F
				* (float) text.getHeight(), // y
				(float) text.getWidth()
				+ mOffsetWidth, // width + offset
				(float) text.getHeight()
				+ mOffsetHeight // height + offset
				);
	}

	@Override
	public PPath createNode(Identifier identifier, PText text) {
		return createNode(text);
	}

	@Override
	public PPath createNode(Metadata metadata, PText text) {
		return createNode(text);
	}
}
