package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.renderer;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public interface Piccolo2dNodeRenderer {

	public PPath createNode(PText text);
}
