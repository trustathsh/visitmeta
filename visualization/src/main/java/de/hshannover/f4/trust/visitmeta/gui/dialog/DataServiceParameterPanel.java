package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceRestConnectionImpl;
import de.hshannover.f4.trust.visitmeta.gui.util.DocumentChangedListener;
import de.hshannover.f4.trust.visitmeta.gui.util.ParameterPanel;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnectionData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class DataServiceParameterPanel extends ParameterPanel {

	private static final long serialVersionUID = -4830135051242549298L;

	private JLabel mJlName;
	private JLabel mJlUrl;
	private JLabel mJlRawXml;

	private JTextField mJtfUrl;
	private JTextField mJtfName;

	private JCheckBox mJcbRawXML;

	private DataserviceConnectionData mConnectionData;

	private DocumentChangedListener mDocumentChangedListener;

	private ItemListener mItemListener;

	public DataServiceParameterPanel() {
		createPanels();
	}

	public DataServiceParameterPanel(DataserviceConnectionData connectionData) {
		mConnectionData = connectionData;

		createPanels();
		updatePanel();
		addChangeListeners();
	}

	private void createPanels() {
		setLayout(new GridBagLayout());

		mJlUrl = new JLabel("Url");
		mJlName = new JLabel("Name");
		mJlRawXml = new JLabel("RAW-XML");

		mJtfUrl = new JTextField();
		mJtfName = new JTextField();
		if (mConnectionData instanceof DataserviceRestConnectionImpl) {
			mJtfName.setEditable(((DataserviceRestConnectionImpl) mConnectionData).isNotPersised());
		} else {
			mJtfName.setEditable(false);
		}

		mJcbRawXML = new JCheckBox();

		// x y w h wx wy
		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 1.0, this, mJlName, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 0, 1, 1, 1.0, 1.0, this, mJtfName, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJlUrl, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 1, 1, 1, 1.0, 1.0, this, mJtfUrl, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 2, 1, 1, 1.0, 1.0, this, mJlRawXml, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 2, 1, 1, 1.0, 1.0, this, mJcbRawXML, LayoutHelper.mLblInsets);
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
		mJcbRawXML.addItemListener(mItemListener);
	}


	private void updatePanel() {
		mJtfName.setText(mConnectionData.getName());
		mJtfUrl.setText(mConnectionData.getUrl());
		mJcbRawXML.setSelected(mConnectionData.isRawXml());
	}

	@Override
	public Data getData() {
		mConnectionData.setName(mJtfName.getText().trim());
		mConnectionData.setUrl(mJtfUrl.getText().trim());
		mConnectionData.setRawXml(mJcbRawXML.isSelected());
		return mConnectionData;
	}

}
