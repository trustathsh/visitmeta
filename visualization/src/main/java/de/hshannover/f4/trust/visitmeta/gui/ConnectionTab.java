package de.hshannover.f4.trust.visitmeta.gui;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.log4j.Logger;

public class ConnectionTab extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ConnectionTab.class);
	
	private String mName;
	private PanelTimeLine mTimeLine = null;
	private JSplitPane mSplitPane = null;
	private JPanel mUpperPanel = null;
	private JPanel mLowerPanel = null;
	private JComponent mGraphPanel = null;

	public ConnectionTab(String pName, JComponent pGraphPanel) {
		super();
		this.mName = pName;
		this.setLayout(new GridLayout());
		logger.trace("init ConnectionTab");
		mGraphPanel = pGraphPanel;
		mTimeLine = new PanelTimeLine();

		mUpperPanel = new JPanel();
		mLowerPanel = new JPanel();

		mUpperPanel.setLayout(new GridLayout());
		mLowerPanel.setLayout(new GridLayout());

		mUpperPanel.add(mGraphPanel);
		mLowerPanel.add(mTimeLine);

		mSplitPane = new JSplitPane();
		mSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mSplitPane.setResizeWeight(0.99);
		mSplitPane.setEnabled(false);
		mSplitPane.setLeftComponent(mUpperPanel);
		mSplitPane.setRightComponent(mLowerPanel);
		this.add(mSplitPane);
	}
	
	@Override public String toString() {
		return this.mName;
	}
	
	@Override public int hashCode() {
		return this.mName.hashCode();
	}

	@Override public boolean equals(Object o) {
		if(o == null || !(o instanceof ConnectionTab)) {
			return false;
		}
		ConnectionTab other = (ConnectionTab) o;
		return this.mName.equals(other.mName);
	}
}
