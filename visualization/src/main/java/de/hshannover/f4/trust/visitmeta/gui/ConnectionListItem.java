package de.hshannover.f4.trust.visitmeta.gui;

import javax.swing.JComponent;

public class ConnectionListItem {
	private String mName;
	private JComponent mConnectionPanel;
	private int mIndex;
	
	public ConnectionListItem(String pName, JComponent pConnectionPanel) {
		mName = pName;
		mConnectionPanel = pConnectionPanel;
	}
	
	public JComponent getConnectionPanel() {
		return this.mConnectionPanel;
	}
	
	public int getIndex() {
		return this.mIndex;
	}
	
	public void setIndex(int pIndex) {
		this.mIndex = pIndex;
	}
	
	@Override public String toString() {
		return this.mName;
	}
}
