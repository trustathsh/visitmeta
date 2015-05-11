package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnectionData;

public class DataServiceParameterPanel extends JPanel {

	private static final long serialVersionUID = -4830135051242549298L;

	private JLabel mJlName;
	private JLabel mJlUrl;
	private JLabel mJlRawXml;

	private JTextField mJtfUrl;
	private JTextField mJtfName;

	private JCheckBox mJcbRawXML;

	public DataServiceParameterPanel() {
		createPanels();
	}

	public DataServiceParameterPanel(DataserviceConnectionData connection) {
		createPanels();

		updatePanel(connection);
	}

	private void createPanels() {
		setLayout(new GridBagLayout());

		mJlUrl = new JLabel("Url");
		mJlName = new JLabel("Name");
		mJlRawXml = new JLabel("RAW-XML");

		mJtfUrl = new JTextField();
		mJtfName = new JTextField();
		mJtfName.setEditable(false);

		mJcbRawXML = new JCheckBox();

		// x y w h wx wy
		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 1.0, this, mJlName, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 0, 1, 1, 1.0, 1.0, this, mJtfName, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJlUrl, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 1, 1, 1, 1.0, 1.0, this, mJtfUrl, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 2, 1, 1, 1.0, 1.0, this, mJlRawXml, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 2, 1, 1, 1.0, 1.0, this, mJcbRawXML, LayoutHelper.mLblInsets);
	}

	private void updatePanel(DataserviceConnectionData dataConnection) {
		mJtfName.setText(dataConnection.getName());
		mJtfUrl.setText(dataConnection.getUrl());
		mJcbRawXML.setSelected(dataConnection.isRawXml());
	}

}
