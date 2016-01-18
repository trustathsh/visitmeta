package de.hshannover.f4.trust.visitmeta.gui.contextmenu;

import java.util.ArrayList;
import java.util.List;

public class ContextMenuItemFactory {

	private static List<ContextMenuItem> mList;

	public static List<ContextMenuItem> getAll() {
		if (mList == null) {
			mList = initList();
		}
		
		return mList;
	}

	
	private static List<ContextMenuItem> initList() {
		List<ContextMenuItem> list = new ArrayList<>();
		
		list.add(new ShowGraphFromHereContextMenuItem());
		list.add(new EditPolicyNodeContextMenuItem());
		list.add(new RunWhatIfAnalysisOnPolicyActionContextMenuItem());
		
		return list;
	}

}
