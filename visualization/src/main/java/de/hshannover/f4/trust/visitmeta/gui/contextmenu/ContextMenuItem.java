package de.hshannover.f4.trust.visitmeta.gui.contextmenu;

import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;

public interface ContextMenuItem {

	public void actionPerformed(GraphicWrapper node);

	public String getItemTitle();

	public boolean canHandle(Propable node);

}
