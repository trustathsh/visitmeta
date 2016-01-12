package de.hshannover.f4.trust.visitmeta.gui.contextmenu;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.util.ExtendedIdentifierHelper;

public class EditPolicyNodeContextMenuItem implements ContextMenuItem {

	private Logger mLogger = Logger.getLogger(EditPolicyNodeContextMenuItem.class);

	private List<String> policyTypeNames = Arrays.asList("signature", "anomaly", "rule", "condition");

	@Override
	public void actionPerformed(Propable propable) {
		// TODO get content of node
		propable.getRawData();

		// TODO create new GUI element to show and edit node content (using detail information or new popup menu?)
		JWindow editWindow = new JWindow();

		// TODO use PanelXMWTree to display content

		// TODO get changed content from GUI element (save-button?)


		// TODO get metadata for link to parent (from policy-viewpoint) and associated parent identifier


		// TODO create IFMAP publish request with ifmapj


		// TODO send publish request (lifetime forever) via ifmapj as (new or existing?) user; new connection with username policy-editor?


		// TODO close IFMAP connection


		// TODO close GUI element

	}

	@Override
	public String getItemTitle() {
		return "Edit Policy item";
	}

	@Override
	public boolean canHandle(Propable node) {
		if (node instanceof Identifier) {
			Identifier i = (Identifier) node;
			if (ExtendedIdentifierHelper.isExtendedIdentifier(i) &&
					policyTypeNames.contains(ExtendedIdentifierHelper.getExtendedIdentifierInnerTypeName(i).toLowerCase())) {
				return true;
			} else if (policyTypeNames.contains(i.getTypeName())) {
				return true;
			} else {
				return false;
			}
		} else if (node instanceof Metadata) {
			Metadata m = (Metadata) node;
			if (policyTypeNames.contains(m.getTypeName().toLowerCase())) {
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}
	}

}
