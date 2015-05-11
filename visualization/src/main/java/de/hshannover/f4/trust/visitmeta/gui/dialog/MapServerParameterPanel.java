package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.hshannover.f4.trust.visitmeta.gui.util.HintTextField;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnectionData;

public class MapServerParameterPanel extends JPanel {

	private static final long serialVersionUID = -3686612903315798696L;

	private JLabel mJlName;
	private JLabel mJlUrl;
	private JLabel mJlBasicAuthentication;
	private JLabel mJlUsername;
	private JLabel mJlPassword;
	private JLabel mJlMaxPollResultSize;
	private JLabel mJlConnectingAtStartUp;

	private JTextField mJtfUrl;
	private JTextField mJtfName;
	private JTextField mJtfUsername;
	private JTextField mJtfMaxPollResultSize;

	private JPasswordField mJtfPassword;

	private JCheckBox mJcbBasicAuthentication;
	private JCheckBox mJcbConnectingAtStartUp;

	public MapServerParameterPanel() {
		createPanels();
	}

	public MapServerParameterPanel(MapServerConnectionData connection) {
		createPanels();

		updatePanel(connection);
	}

	private void createPanels() {
		setLayout(new GridBagLayout());

		mJlName = new JLabel("Name");
		mJlUrl = new JLabel("Map-Server Url");
		mJlBasicAuthentication = new JLabel("Basic Authentication");
		mJlBasicAuthentication.setEnabled(false);
		mJlUsername = new JLabel("Username");
		mJlPassword = new JLabel("Password");
		mJlMaxPollResultSize = new JLabel("max-poll-result-size");
		mJlConnectingAtStartUp = new JLabel("Connecting at start-up");

		mJtfName = new JTextField();
		mJtfName.setEditable(false);
		mJtfUrl = new JTextField();
		mJcbBasicAuthentication = new JCheckBox();
		mJcbBasicAuthentication.setEnabled(false);
		mJcbConnectingAtStartUp = new JCheckBox();
		mJtfUsername = new JTextField();
		mJtfPassword = new JPasswordField();
		mJtfMaxPollResultSize = new HintTextField("Optional");

		// x y w h wx wy
		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 1.0, this, mJlName, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJlUrl, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 2, 1, 1, 1.0, 1.0, this, mJlBasicAuthentication, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 3, 1, 1, 1.0, 1.0, this, mJlUsername, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 4, 1, 1, 1.0, 1.0, this, mJlPassword, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 5, 1, 1, 1.0, 1.0, this, mJlMaxPollResultSize, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 6, 1, 1, 1.0, 1.0, this, mJlConnectingAtStartUp, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 0, 1, 1, 1.0, 1.0, this, mJtfName, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 1, 1, 1, 1.0, 1.0, this, mJtfUrl, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 2, 1, 1, 1.0, 1.0, this, mJcbBasicAuthentication, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 3, 1, 1, 1.0, 1.0, this, mJtfUsername, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 4, 1, 1, 1.0, 1.0, this, mJtfPassword, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 5, 1, 1, 1.0, 1.0, this, mJtfMaxPollResultSize, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 6, 1, 1, 1.0, 1.0, this, mJcbConnectingAtStartUp, LayoutHelper.mLblInsets);
	}

	private void updatePanel(MapServerConnectionData connectionData) {
		mJtfName.setText(connectionData.getName());
		mJtfUrl.setText(connectionData.getUrl());
		mJtfUsername.setText(connectionData.getUserName());
		mJtfPassword.setText(connectionData.getUserPassword());
		mJcbBasicAuthentication.setSelected(connectionData.isAuthenticationBasic());
		// TODO TRUSTSTORE_PATH
		// TODO TRUSTSTORE_PASS
		mJcbConnectingAtStartUp.setSelected(connectionData.doesConnectOnStartup());
		mJtfMaxPollResultSize.setText(String.valueOf(connectionData.getMaxPollResultSize()));
	}
}
