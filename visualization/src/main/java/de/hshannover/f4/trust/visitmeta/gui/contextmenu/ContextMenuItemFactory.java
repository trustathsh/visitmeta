package de.hshannover.f4.trust.visitmeta.gui.contextmenu;

import java.util.ArrayList;
import java.util.List;

public class ContextMenuItemFactory {

	private static List<ContextMenuItem> list = new ArrayList<>();

	static {
		list.add(new ShowGraphFromHereContextMenuItem());
		list.add(new EditPolicyNodeContextMenuItem());
	}

	public static List<ContextMenuItem> getAll() {
		return list;
	}

}
