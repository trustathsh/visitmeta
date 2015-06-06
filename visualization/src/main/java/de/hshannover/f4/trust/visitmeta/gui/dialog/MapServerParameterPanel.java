package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.hshannover.f4.trust.visitmeta.gui.util.DocumentChangedListener;
import de.hshannover.f4.trust.visitmeta.gui.util.HintTextField;
import de.hshannover.f4.trust.visitmeta.gui.util.MapServerRestConnectionImpl;
import de.hshannover.f4.trust.visitmeta.gui.util.ParameterPanel;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnectionData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class MapServerParameterPanel extends ParameterPanel {

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

	private MapServerConnectionData mConnectionData;

	private DocumentChangedListener mDocumentChangedListener;

	private ItemListener mItemListener;

	public MapServerParameterPanel() {
		createPanels();
	}

	public MapServerParameterPanel(MapServerConnectionData connectionData) {
		mConnectionData = connectionData;

		createPanels();
		updatePanel();
		addChangeListeners();

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
		if (mConnectionData instanceof MapServerRestConnectionImpl) {
			mJtfName.setEditable(((MapServerRestConnectionImpl) mConnectionData).isNotPersised());
		} else {
			mJtfName.setEditable(false);
		}
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

	private void addChangeListeners() {
		mDocumentChangedListener = new DocumentChangedListener() {

			@Override
			protected void dataChanged() {
				fireParameterChanged();
			}
		};

		mItemListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent event) {
				fireParameterChanged();
			}
		};

		mJtfName.getDocument().addDocumentListener(mDocumentChangedListener);
		mJtfUrl.getDocument().addDocumentListener(mDocumentChangedListener);
		mJtfUsername.getDocument().addDocumentListener(mDocumentChangedListener);
		mJtfPassword.getDocument().addDocumentListener(mDocumentChangedListener);
		mJtfMaxPollResultSize.getDocument().addDocumentListener(mDocumentChangedListener);
		mJcbConnectingAtStartUp.addItemListener(mItemListener);
		mJcbBasicAuthentication.addItemListener(mItemListener);
	}

	private void updatePanel() {
		mJtfName.setText(mConnectionData.getName());
		mJtfUrl.setText(mConnectionData.getUrl());
		mJtfUsername.setText(mConnectionData.getUserName());
		mJtfPassword.setText(mConnectionData.getUserPassword());
		mJcbBasicAuthentication.setSelected(mConnectionData.isAuthenticationBasic());
		// TODO TRUSTSTORE_PATH
		// TODO TRUSTSTORE_PASS
		mJcbConnectingAtStartUp.setSelected(mConnectionData.doesConnectOnStartup());
		mJtfMaxPollResultSize.setText(String.valueOf(mConnectionData.getMaxPollResultSize()));
	}

	@Override
	public Data getData() {
		mConnectionData.setName(mJtfName.getText().trim());
		mConnectionData.setUrl(mJtfUrl.getText().trim());
		mConnectionData.setUserName(mJtfUsername.getText().trim());
		mConnectionData.setUserPassword(String.valueOf(mJtfPassword.getPassword()).trim());
		mConnectionData.setAuthenticationBasic(mJcbBasicAuthentication.isSelected());
		// TODO TRUSTSTORE_PATH
		// TODO TRUSTSTORE_PASS
		mConnectionData.setStartupConnect(mJcbConnectingAtStartUp.isSelected());
		mConnectionData.setMaxPollResultSize(Integer.parseInt(mJtfMaxPollResultSize.getText().trim()));
		return mConnectionData;
	}
}
