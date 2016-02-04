package de.hshannover.f4.trust.visitmeta.gui.contextmenu;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;

public class ShowGraphFromHereContextMenuItem implements ContextMenuItem {

	private Logger mLogger = Logger.getLogger(ShowGraphFromHereContextMenuItem.class);

	@Override
	public void actionPerformed(GraphicWrapper wrapper) {
		startTreeWalk(wrapper.getData());
	}

	@Override
	public String getItemTitle() {
		return "Show Graph from here";
	}

	private void startTreeWalk(Propable node) {
		if (node instanceof Identifier) {
			mLogger.debug("Identifier: " + node.getTypeName());
		} else if (node instanceof Metadata) {
			mLogger.debug("Metadata: " + node.getTypeName());
		}
	}

	@Override
	public boolean canHandle(Propable node) {
		return true;
	}

}
