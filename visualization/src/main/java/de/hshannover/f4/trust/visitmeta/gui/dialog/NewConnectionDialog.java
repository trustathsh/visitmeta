package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.exceptions.RESTException;
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
import de.hshannover.f4.trust.visitmeta.interfaces.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnectionData;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnectionData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.util.yaml.DataservicePersister;

public class NewConnectionDialog extends JDialog{

	private static final long serialVersionUID = -8052562697583611679L;

	private static final Logger LOGGER = Logger.getLogger(NewConnectionDialog.class);

	private static Properties mConfig;

	private static DataservicePersister mDataservicePersister;

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

	public static void main(String[] args) {
		Main.initComponents();
		mConfig = Main.getConfig();
		mDataservicePersister = Main.getDataservicePersister();

		NewConnectionDialog temp = new NewConnectionDialog();
		temp.setVisible(true);
	}

	public NewConnectionDialog() {
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
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - getWidth() / 2, (Toolkit
				.getDefaultToolkit().getScreenSize().height) / 2 - getHeight() / 2);
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

	private void initSouthPanel() {
		mJpSouth = new JPanel();
		mJpSouth.setLayout(new FlowLayout(FlowLayout.RIGHT));

		mJbClose = new JButton("Close");
		mJbClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});

		mJbSave = new JButton("Save");
		mJbSave.setEnabled(false);
		mJbSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				savePropertyChanges();
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

		try {
			mJtConnections = new RESTConnectionTree(Main.getDataservicePersister().loadDataserviceConnections());
		} catch (PropertyException e1) {
			e1.printStackTrace();
		}
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
			if(!dataserviceConnection.isNotPersised()){
				dataserviceConnection.setOldData(((DataserviceConnectionData)dataserviceConnection).copy());
			}
			dataserviceConnection.changeData((DataserviceConnectionData) changedData);
			dataserviceConnection.setNotPersised(true);

		} else if (selectedComponent instanceof MapServerRestConnectionImpl) {
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) selectedComponent;
			if(!mapServerConnection.isNotPersised()){
				mapServerConnection.setOldData(((MapServerConnectionData)mapServerConnection).copy());
			}
			mapServerConnection.changeData((MapServerConnectionData) changedData);
			mapServerConnection.setNotPersised(true);

		} else if (selectedComponent instanceof RestSubscriptionImpl) {
			RestSubscriptionImpl subscription = (RestSubscriptionImpl) selectedComponent;
			if(!subscription.isNotPersised()){
				subscription.setOldData(((SubscriptionData)subscription).copy());
			}
			subscription.changeData((SubscriptionData) changedData);
			subscription.setNotPersised(true);
		}

		mJspLeft.updateUI();
	}

	private int confirmDeleteRequest() {
		return JOptionPane.showConfirmDialog(this, "Would you really want to delete?", "Confirm delete request!",
				JOptionPane.YES_NO_OPTION);
	}

	public void eventDeleteData() throws PropertyException, RESTException {
		int requestResult = confirmDeleteRequest();

		if (requestResult == JOptionPane.NO_OPTION || requestResult == JOptionPane.CLOSED_OPTION) {
			return;
		}

		Object selectedComponent = mJtConnections.getSelectionPath().getLastPathComponent();
		TreePath parentPath = mJtConnections.getSelectionPath().getParentPath();
		Object parentData = parentPath.getLastPathComponent();

		if (selectedComponent instanceof DataserviceRestConnectionImpl && parentData instanceof Dataservices) {
			DataserviceRestConnectionImpl dataserviceConnection = (DataserviceRestConnectionImpl) selectedComponent;
			Dataservices dataservices = (Dataservices) parentData;

			if (!dataserviceConnection.isNotPersised()) {
				mDataservicePersister.removeDataserviceConnection(dataserviceConnection.getConnectionName());
			}

			dataservices.removeDataserviceConnection(dataserviceConnection);

			mJtConnections.updateModel();
		} else if (selectedComponent instanceof MapServerConnection && parentData instanceof DataserviceConnection) {
			MapServerConnection mapServerConnection = (MapServerConnection) selectedComponent;
			DataserviceConnection dataserviceConnection = (DataserviceConnection) parentData;

			RestHelper.deleteMapServerConnection(dataserviceConnection, mapServerConnection.getConnectionName());

			dataserviceConnection.removeMapServerData(mapServerConnection);

			mJtConnections.updateModel();

		} else if (selectedComponent instanceof Subscription && parentData instanceof MapServerRestConnectionImpl) {
			Subscription subscription = (Subscription) selectedComponent;
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) parentData;

			RestHelper.deleteSubscription(mapServerConnection.getDataserviceConnection(),
					mapServerConnection.getConnectionName(), subscription.getName());

			mapServerConnection.deleteSubscription(subscription.getName());

			mJtConnections.updateModel();
		}

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
					"New Dataservice-Connection " + (dataservices.getSubDataCount() + 1), "", false);
			newDataserviceConnection.setNotPersised(true);

			addNewData(selectionPath, newDataserviceConnection);

		} else if (selectedComponent instanceof DataserviceConnection) {
			DataserviceConnection dataserviceConnection = (DataserviceConnection) selectedComponent;
			MapServerRestConnectionImpl newMapServerConnection = new MapServerRestConnectionImpl(
					dataserviceConnection, "New Map-Server-Connection " + (dataserviceConnection.getSubDataCount() + 1));
			newMapServerConnection.setNotPersised(true);

			addNewData(selectionPath, newMapServerConnection);

		} else if (selectedComponent instanceof MapServerConnection) {
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) selectedComponent;
			RestSubscriptionImpl newSubscription = new RestSubscriptionImpl(mapServerConnection,
					"New Subscription " + (mapServerConnection.getSubDataCount() + 1));
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
			dataserviceConnection.addMapServerData((MapServerConnectionData) newData);

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
				LOGGER.error(e.toString(), e);
			}

		} else if (selectedComponent instanceof MapServerRestConnectionImpl) {
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) selectedComponent;

			try {
				RestHelper.saveMapServerConnection(mapServerConnection.getDataserviceConnection(), mapServerConnection);
				mapServerConnection.setNotPersised(false);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | JSONException e) {
				LOGGER.error(e.toString(), e);
			}

		} else if (selectedComponent instanceof RestSubscriptionImpl) {
			RestSubscriptionImpl subscription = (RestSubscriptionImpl) selectedComponent;
			Data parentData = mJtConnections.getSelectedParentData();
			if (parentData instanceof MapServerRestConnectionImpl) {
				MapServerRestConnectionImpl connectionData = (MapServerRestConnectionImpl) parentData;
				try {
					RestHelper.saveSubscription(connectionData.getDataserviceConnection(), connectionData.getName(),
							subscription);
					subscription.setNotPersised(false);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | JSONException e) {
					LOGGER.error(e.toString(), e);
				}
			}
		}
		changeParameterPanel();
	}

	public void selectPath(TreePath newPath) {
		mJtConnections.setSelectionPath(newPath);
		changeParameterPanel();
	}

}
