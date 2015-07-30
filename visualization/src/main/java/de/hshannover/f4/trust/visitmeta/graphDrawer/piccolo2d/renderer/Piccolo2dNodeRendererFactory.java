package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.renderer;

public class Piccolo2dNodeRendererFactory {

	public static Piccolo2dNodeRenderer create(Piccolo2dNodeRendererType type) {
		switch (type) {
			case RECTANGLES_WITH_ROUNDED_CORNERS:
				return new RectanglesWithRounderCornersPiccolo2dRenderer();
			case RECTANGLES_WITH_SQUARE_CORNERS:
				return new RectanglesWithSquareCorners();
			case ELLIPSE:
				return null;
			default:
				throw new IllegalArgumentException("No Piccolo2dRenderer found for type '"
						+ type + "'");
		}
	}

}
