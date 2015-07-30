package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.renderer;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public interface Piccolo2dNodeRenderer {

	public PPath createNode(Identifier identifier, PText text);

	public PPath createNode(Metadata metadata, PText text);
}
