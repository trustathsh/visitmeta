/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 *
 * =====================================================
 *
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 *
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 *
 * This file is part of visitmeta-visualization, version 0.5.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;

import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.exceptions.RESTException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.gui.MainWindow;
import de.hshannover.f4.trust.visitmeta.gui.util.ConnectionTreeCellRenderer;
import de.hshannover.f4.trust.visitmeta.gui.util.ConnectionTreePopupMenu;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceRestConnectionImpl;
import de.hshannover.f4.trust.visitmeta.gui.util.Dataservices;
import de.hshannover.f4.trust.visitmeta.gui.util.MapServerRestConnectionImpl;
import de.hshannover.f4.trust.visitmeta.gui.util.ParameterListener;
import de.hshannover.f4.trust.visitmeta.gui.util.ParameterPanel;
import de.hshannover.f4.trust.visitmeta.gui.util.RESTConnectionTree;
import de.hshannover.f4.trust.visitmeta.gui.util.RestHelper;
import de.hshannover.f4.trust.visitmeta.gui.util.RestSubscriptionImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.data.DataserviceData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.MapServerData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.util.StringHelper;
import de.hshannover.f4.trust.visitmeta.util.yaml.DataservicePersister;

public class ConnectionDialog extends JDialog {

	private static final long serialVersionUID = -8052562697583611679L;

	private static final Logger LOGGER = Logger.getLogger(ConnectionDialog.class);

	private static DataservicePersister mDataservicePersister;

	private MainWindow mMainWindow;

	private List<DataserviceConnection> mDataserviceList;

	private JTextArea mJtaLogWindows;

	private JScrollPane mJspLogWindows;

	private RESTConnectionTree mJtConnections;

	private JSplitPane mJspMain;

	private JPanel mJpParameter;

	private ParameterPanel mParameterValues;

	private JPanel mJpLog;

	private JPanel mJpLeft;

	private JPanel mJpRight;

	private JPanel mJpSouth;

	private JScrollPane mJspLeft;

	private JButton mJbSave;

	private JButton mJbClose;

	private JButton mJbReset;

	static {
		LOGGER.addAppender(new JTextAreaAppander());
	}

	public ConnectionDialog(MainWindow mainWindow, List<DataserviceConnection> dataserviceList) {
		mMainWindow = mainWindow;
		mDataserviceList = dataserviceList;
		mDataservicePersister = Main.getDataservicePersister();

		createDialog();
		createPanels();

		super.pack();
	}

	private void createDialog() {
		setTitle("Manage VisITMeta connections");
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new ConnectionDialogWindowListener(this));
		setMinimumSize(new Dimension(600, 500));
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)
				/ 2
				- getWidth()
						/ 2,
				(Toolkit
						.getDefaultToolkit().getScreenSize().height)
						/ 2
						- getHeight()
								/ 2);
	}

	private void createPanels() {
		getContentPane().setLayout(new GridBagLayout());

		initMainPanel();
		initSouthPanel();

		mJtConnections.setSelectionRow(1);
		changeParameterPanel();

		// x y w h wx wy
		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 1.0, getContentPane(), mJspMain, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 1, 1, 1, 0.0, 0.0, getContentPane(), mJpSouth, LayoutHelper.mLblInsets);
	}

	private int confirmCloseRequest() {
		return JOptionPane.showConfirmDialog(this, "Do you really want to close without saving?",
				"Confirm close window!", JOptionPane.YES_NO_OPTION);
	}

	private boolean existNotPersistedData() {
		Object root = mJtConnections.getModel().getRoot();
		if (root instanceof Dataservices) {
			Dataservices dataservices = (Dataservices) root;
			for (Data dataserviceConnectionData : dataservices.getSubData()) {
				if (dataserviceConnectionData instanceof DataserviceRestConnectionImpl) {
					DataserviceRestConnectionImpl dataserviceConnection =
							(DataserviceRestConnectionImpl) dataserviceConnectionData;

					if (dataserviceConnection.isNotPersised()) {
						return true;
					}

					for (Data mapServerConnectionData : dataserviceConnection.getSubData()) {
						if (mapServerConnectionData instanceof MapServerRestConnectionImpl) {
							MapServerRestConnectionImpl mapServerConnection =
									(MapServerRestConnectionImpl) mapServerConnectionData;

							if (mapServerConnection.isNotPersised()) {
								return true;
							}

							for (Data subscriptionData : mapServerConnection.getSubData()) {
								if (subscriptionData instanceof RestSubscriptionImpl) {
									RestSubscriptionImpl subscription = (RestSubscriptionImpl) subscriptionData;
									if (subscription.isNotPersised()) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private void initSouthPanel() {
		mJpSouth = new JPanel();
		mJpSouth.setLayout(new FlowLayout(FlowLayout.RIGHT));

		mJbClose = new JButton("Close");
		mJbClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (existNotPersistedData()) {
					int requestResult = confirmCloseRequest();
					if (requestResult != JOptionPane.YES_OPTION) {
						return;
					}
				}
				setVisible(false);
			}
		});

		mJbSave = new JButton("Save");
		mJbSave.setEnabled(false);
		mJbSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				savePropertyChanges();
				updateMainWindowTree();
			}

		});

		mJbReset = new JButton("Reset");
		mJbReset.setEnabled(false);
		mJbReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				resetPropertyChanges();
			}
		});

		mJpSouth.add(mJbClose);
		mJpSouth.add(mJbReset);
		mJpSouth.add(mJbSave);
	}

	private void initMainPanel() {
		mJspMain = new JSplitPane();
		mJspMain.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		mJspMain.setResizeWeight(0.25);

		initLeftHandSide();
		initRightHandSide();

		mJspMain.setLeftComponent(mJpLeft);
		mJspMain.setRightComponent(mJpRight);

	}

	/**
	 * Initializes the left hand side of the JSplitPane
	 */
	private void initLeftHandSide() {
		mJtConnections = new RESTConnectionTree(mDataserviceList);

		mJtConnections.expandAllNodes();
		mJtConnections.addMouseListener(new ConnectionTreeDialogListener(this));

		ConnectionTreeCellRenderer treeRenderer = new ConnectionTreeCellRenderer();
		mJtConnections.setCellRenderer(treeRenderer);

		mJspLeft = new JScrollPane(mJtConnections);

		mJpLeft = new JPanel();
		mJpLeft.setLayout(new GridBagLayout());

		// x y w h wx wy
		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 1.0, mJpLeft, mJspLeft, LayoutHelper.mLblInsets);

	}

	/**
	 * Initializes the right hand side of the JSplitPane
	 */
	private void initRightHandSide() {

		mJpParameter = new JPanel();
		mJpParameter.setLayout(new GridBagLayout());
		mJpParameter.setBorder(BorderFactory.createTitledBorder("Connection Parameter"));

		// MapServerParameterPanel connectionParameter = new MapServerParameterPanel();

		mJpLog = new JPanel();
		mJpLog.setLayout(new GridBagLayout());
		mJpLog.setBorder(BorderFactory.createTitledBorder("Connection Log"));

		mJtaLogWindows = new JTextArea(5, 40);
		mJtaLogWindows.setEditable(false);
		// for append logging messages
		JTextAreaAppander.addJTextArea(mJtaLogWindows);

		mJspLogWindows = new JScrollPane(mJtaLogWindows);

		mJpRight = new JPanel();
		mJpRight.setLayout(new GridBagLayout());

		// x y w h wx wy
		// LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 0.0, mJpParameter, connectionParameter, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 1.0, mJpLog, mJspLogWindows, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 1.0, mJpRight, mJpParameter, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 1, 1, 1, 0.0, 0.0, mJpRight, mJpLog, LayoutHelper.mLblInsets);
	}

	public void changeParameterPanel() {
		Object selectedComponent = mJtConnections.getLastSelectedPathComponent();

		if (selectedComponent instanceof DataserviceConnection) {
			mParameterValues = new DataServiceParameterPanel(((DataserviceConnection) selectedComponent).copy());
			mJbSave.setEnabled(((DataserviceRestConnectionImpl) selectedComponent).isNotPersised());
			mJbReset.setEnabled(((DataserviceRestConnectionImpl) selectedComponent).isNotPersised());
			if (((DataserviceRestConnectionImpl) selectedComponent).isNotPersised()
					&& ((DataserviceRestConnectionImpl) selectedComponent).getOldData() == null) {
				mParameterValues.setNameTextFieldEditable();
			}

		} else if (selectedComponent instanceof MapServerConnection) {
			mParameterValues = new MapServerParameterPanel(((MapServerConnection) selectedComponent).copy());
			mJbSave.setEnabled(((MapServerRestConnectionImpl) selectedComponent).isNotPersised());
			mJbReset.setEnabled(((MapServerRestConnectionImpl) selectedComponent).isNotPersised());
			if (((MapServerRestConnectionImpl) selectedComponent).isNotPersised()
					&& ((MapServerRestConnectionImpl) selectedComponent).getOldData() == null) {
				mParameterValues.setNameTextFieldEditable();
			}

		} else if (selectedComponent instanceof Subscription) {
			mParameterValues = new SubscriptionParameterPanel(((Subscription) selectedComponent).copy());
			mJbSave.setEnabled(((RestSubscriptionImpl) selectedComponent).isNotPersised());
			mJbReset.setEnabled(((RestSubscriptionImpl) selectedComponent).isNotPersised());
			if (((RestSubscriptionImpl) selectedComponent).isNotPersised()
					&& ((RestSubscriptionImpl) selectedComponent).getOldData() == null) {
				mParameterValues.setNameTextFieldEditable();
			}
		}

		if (mParameterValues != null) {

			mParameterValues.addParameterListener(new ParameterListener() {
				@Override
				public void parameterChanged() {
					Data changedData = mParameterValues.getData();
					propertiesDataChanged(changedData);
				}
			});
			mJpParameter.removeAll();

			LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 0.0, mJpParameter, mParameterValues, LayoutHelper.mLblInsets);

			mJpParameter.updateUI();
		}
	}

	public void showConnectionTreePopupMenu(int x, int y) {
		TreePath treePath = mJtConnections.getClosestPathForLocation(x, y);

		selectPath(treePath);

		Object selectedComponent = mJtConnections.getLastSelectedPathComponent();

		ConnectionTreePopupMenu popUp = new ConnectionTreePopupMenu(mJtConnections, this, (Data) selectedComponent);
		popUp.show(mJtConnections, x, y);
	}

	public void propertiesDataChanged(Data changedData) {
		mJbSave.setEnabled(true);
		mJbReset.setEnabled(true);

		Object selectedComponent = mJtConnections.getLastSelectedPathComponent();
		if (selectedComponent instanceof DataserviceRestConnectionImpl) {
			DataserviceRestConnectionImpl dataserviceConnection = (DataserviceRestConnectionImpl) selectedComponent;
			if (!dataserviceConnection.isNotPersised()) {
				dataserviceConnection.setOldData(((DataserviceData) dataserviceConnection).copy());
			}
			dataserviceConnection.changeData((DataserviceData) changedData);
			dataserviceConnection.setNotPersised(true);

		} else if (selectedComponent instanceof MapServerRestConnectionImpl) {
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) selectedComponent;
			if (!mapServerConnection.isNotPersised()) {
				mapServerConnection.setOldData(((MapServerData) mapServerConnection).copy());
			}
			mapServerConnection.changeData((MapServerData) changedData);
			mapServerConnection.setNotPersised(true);

		} else if (selectedComponent instanceof RestSubscriptionImpl) {
			RestSubscriptionImpl subscription = (RestSubscriptionImpl) selectedComponent;
			if (!subscription.isNotPersised()) {
				subscription.setOldData(((SubscriptionData) subscription).copy());
			}
			subscription.changeData((SubscriptionData) changedData);
			subscription.setNotPersised(true);
		}

		mJspLeft.updateUI();
	}

	private int confirmDeleteRequest() {
		return JOptionPane.showConfirmDialog(this, "Do you really want to delete?", "Confirm delete request!",
				JOptionPane.YES_NO_OPTION);
	}

	public void eventDeleteData() throws PropertyException, RESTException {
		int requestResult = confirmDeleteRequest();

		if (requestResult == JOptionPane.NO_OPTION
				|| requestResult == JOptionPane.CLOSED_OPTION) {
			return;
		}

		Object selectedComponent = mJtConnections.getSelectionPath().getLastPathComponent();
		TreePath parentPath = mJtConnections.getSelectionPath().getParentPath();
		Object parentData = parentPath.getLastPathComponent();

		if (selectedComponent instanceof DataserviceRestConnectionImpl
				&& parentData instanceof Dataservices) {
			DataserviceRestConnectionImpl dataserviceConnection = (DataserviceRestConnectionImpl) selectedComponent;
			Dataservices dataservices = (Dataservices) parentData;

			if (!dataserviceConnection.isNotPersised()) {
				mDataservicePersister.removeDataserviceConnection(dataserviceConnection.getConnectionName());
			}

			dataservices.removeDataserviceConnection(dataserviceConnection);

			mJtConnections.updateModel();
		} else if (selectedComponent instanceof MapServerRestConnectionImpl
				&& parentData instanceof DataserviceConnection) {
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) selectedComponent;
			DataserviceConnection dataserviceConnection = (DataserviceConnection) parentData;

			if (!mapServerConnection.isNotPersised()) {
				RestHelper.deleteMapServerConnection(dataserviceConnection, mapServerConnection.getConnectionName());
			}

			dataserviceConnection.removeMapServerData(mapServerConnection);

			mJtConnections.updateModel();

		} else if (selectedComponent instanceof RestSubscriptionImpl
				&& parentData instanceof MapServerRestConnectionImpl) {
			RestSubscriptionImpl subscription = (RestSubscriptionImpl) selectedComponent;
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) parentData;

			if (!subscription.isNotPersised()) {
				RestHelper.deleteSubscription(mapServerConnection.getDataserviceConnection(),
						mapServerConnection.getConnectionName(), subscription.getName());
			}

			mapServerConnection.deleteSubscription(subscription.getName());

			mJtConnections.updateModel();
		}

		updateMainWindowTree();
	}

	public void eventCloneData() {
		Object selectedComponent = mJtConnections.getSelectionPath().getLastPathComponent();
		TreePath parentPath = mJtConnections.getSelectionPath().getParentPath();

		if (selectedComponent instanceof Data) {
			Data newData = ((Data) selectedComponent).clone();

			addNewData(parentPath, newData);
		}
	}

	public void eventNewData() {
		Object selectedComponent = mJtConnections.getSelectionPath().getLastPathComponent();
		TreePath selectionPath = mJtConnections.getSelectionPath();

		if (selectedComponent instanceof Dataservices) {
			Dataservices dataservices = (Dataservices) selectedComponent;
			DataserviceRestConnectionImpl newDataserviceConnection = new DataserviceRestConnectionImpl(
					"New Dataservice-Connection "
							+ (dataservices.getSubDataCount()
									+ 1),
					"", false);
			newDataserviceConnection.setNotPersised(true);

			addNewData(selectionPath, newDataserviceConnection);

		} else if (selectedComponent instanceof DataserviceConnection) {
			DataserviceConnection dataserviceConnection = (DataserviceConnection) selectedComponent;
			MapServerRestConnectionImpl newMapServerConnection = new MapServerRestConnectionImpl(dataserviceConnection,
					"New Map-Server-Connection "
							+ (dataserviceConnection.getSubDataCount()
									+ 1));
			newMapServerConnection.setNotPersised(true);

			addNewData(selectionPath, newMapServerConnection);

		} else if (selectedComponent instanceof MapServerConnection) {
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) selectedComponent;
			RestSubscriptionImpl newSubscription = new RestSubscriptionImpl("New_Subscription_"
					+ (mapServerConnection.getSubDataCount()
							+ 1),
					mapServerConnection);
			newSubscription.setNotPersised(true);

			addNewData(selectionPath, newSubscription);
		}
	}

	public void addNewData(TreePath pathToAdd, Data newData) {
		Object lastPathComponent = pathToAdd.getLastPathComponent();
		TreePath newPath = pathToAdd.pathByAddingChild(newData);

		if (lastPathComponent instanceof Dataservices) {
			Dataservices dataservices = (Dataservices) lastPathComponent;
			dataservices.addDataserviceConnection((DataserviceConnection) newData);

			mJtConnections.updateModel();
			selectPath(newPath);
			mParameterValues.setNameTextFieldEditable();

		} else if (lastPathComponent instanceof DataserviceConnection) {
			DataserviceConnection dataserviceConnection = (DataserviceConnection) lastPathComponent;
			dataserviceConnection.addMapServerData((MapServerData) newData);

			mJtConnections.updateModel();
			selectPath(newPath);
			mParameterValues.setNameTextFieldEditable();

		} else if (lastPathComponent instanceof MapServerConnection) {
			MapServerConnection mapServerConnection = (MapServerConnection) lastPathComponent;
			mapServerConnection.addSubscription((Subscription) newData);

			mJtConnections.updateModel();
			selectPath(newPath);
			mParameterValues.setNameTextFieldEditable();
		}
	}

	public void resetPropertyChanges() {
		Object selectedComponent = mJtConnections.getLastSelectedPathComponent();

		if (selectedComponent instanceof DataserviceRestConnectionImpl) {
			DataserviceRestConnectionImpl dataserviceConnection = (DataserviceRestConnectionImpl) selectedComponent;
			dataserviceConnection.resetData();
			dataserviceConnection.setNotPersised(false);

		} else if (selectedComponent instanceof MapServerRestConnectionImpl) {
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) selectedComponent;
			mapServerConnection.resetData();
			mapServerConnection.setNotPersised(false);

		} else if (selectedComponent instanceof RestSubscriptionImpl) {
			RestSubscriptionImpl subscription = (RestSubscriptionImpl) selectedComponent;
			subscription.resetData();
			subscription.setNotPersised(false);

		}
		changeParameterPanel();
	}

	public void savePropertyChanges() {
		Object selectedComponent = mJtConnections.getLastSelectedPathComponent();

		if (selectedComponent instanceof DataserviceRestConnectionImpl) {
			DataserviceRestConnectionImpl dataserviceConnection = (DataserviceRestConnectionImpl) selectedComponent;
			try {
				Main.getDataservicePersister().persist(dataserviceConnection);
				dataserviceConnection.setNotPersised(false);
			} catch (PropertyException e) {
				LOGGER.error(e.toString());
				JOptionPane.showMessageDialog(null, StringHelper.breakLongString(e.toString(), 80), e.getClass()
						.getSimpleName(), JOptionPane.ERROR_MESSAGE);
			}

		} else if (selectedComponent instanceof MapServerRestConnectionImpl) {
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) selectedComponent;

			try {
				RestHelper.saveMapServerConnection(mapServerConnection.getDataserviceConnection(), mapServerConnection);
				mapServerConnection.setNotPersised(false);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | JSONException e) {
				LOGGER.error(e.toString());
				JOptionPane.showMessageDialog(null, StringHelper.breakLongString(e.toString(), 80), e.getClass()
						.getSimpleName(), JOptionPane.ERROR_MESSAGE);
			}

		} else if (selectedComponent instanceof RestSubscriptionImpl) {
			RestSubscriptionImpl subscription = (RestSubscriptionImpl) selectedComponent;
			Data parentData = mJtConnections.getSelectedParentData();
			if (parentData instanceof MapServerRestConnectionImpl) {
				MapServerRestConnectionImpl connectionData = (MapServerRestConnectionImpl) parentData;

				if (valideSubscriptionData(subscription)) {
					try {
						RestHelper.saveSubscription(connectionData.getDataserviceConnection(),
								connectionData.getName(), subscription);
						subscription.setNotPersised(false);
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | JSONException
							| ConnectionException e) {
						LOGGER.error(e.toString());
						JOptionPane.showMessageDialog(null, StringHelper.breakLongString(e.toString(), 80), e
								.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Subscriptions required \"Start Identifier\" and \"Start "
							+ "Identifier Type\" \n Valid Identifier Types are: 'device' 'access-request' "
							+ "'ip-address' 'mac-address' \n On identifier type: ip-address: \"[type],[value]\" e.g. "
							+ "\"IPv4,10.1.1.1\"", "Subscription-Data not Valid!", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		changeParameterPanel();
	}

	private boolean valideSubscriptionData(RestSubscriptionImpl subscription) {
		if (subscription.getStartIdentifier() == null) {
			return false;
		}
		if (subscription.getIdentifierType() == null) {
			return false;
		}
		if (!valideIdentifierType(subscription.getIdentifierType(), subscription.getStartIdentifier())) {
			return false;
		}
		return true;
	}

	private boolean valideIdentifierType(String identifierType, String identifier) {
		switch (identifierType) {
			case "device":
				return true;
			case "access-request":
				return true;
			case "ip-address":
				String[] split = identifier.split(",");
				switch (split[0]) {
					case "IPv4":
						return true;
					case "IPv6":
						return true;
					default:
						return false;
				}
			case "mac-address":
				return true;
			default:
				return false;
		}
	}

	public void selectPath(TreePath newPath) {
		mJtConnections.setSelectionPath(newPath);
		changeParameterPanel();
	}

	@SuppressWarnings("unchecked")
	private void updateMainWindowTree() {
		List<Data> dataserviceList = ((Dataservices) mJtConnections.getModel().getRoot()).getSubData();
		mMainWindow.getConnectionTree().updateConnections((List<DataserviceConnection>) (List<?>) dataserviceList);
		mMainWindow.getConnectionTree().expandAllNodes();
		mMainWindow.reopenConnectionTabs();
	}

}
