package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.gui.util.ConnectionTreeCellRenderer;
import de.hshannover.f4.trust.visitmeta.gui.util.RESTConnectionTree;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
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

	private JPanel mJpLog;

	private JPanel mJpLeft;

	private JPanel mJpRight;

	private JPanel mJpSouth;

	private JScrollPane mJspLeft;

	private JButton mJbSave;

	private JButton mJbClose;

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

		mJpSouth.add(mJbClose);
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

		switchParameterPanel();
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
		mJtConnections.setSelectionRow(1);

		ConnectionTreeCellRenderer treeRenderer = new ConnectionTreeCellRenderer();
		mJtConnections.setCellRenderer(treeRenderer);

		mJspLeft = new JScrollPane(mJtConnections);

		mJpLeft = new JPanel();
		mJpLeft.setLayout(new GridLayout());
		mJpLeft.add(mJspLeft);
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

	public void switchParameterPanel() {
		JPanel parameterPanel = null;

		Object selectedComponent = mJtConnections.getLastSelectedPathComponent();
		if (selectedComponent instanceof DataserviceConnection) {
			parameterPanel = new DataServiceParameterPanel((DataserviceConnection) selectedComponent);

		} else if (selectedComponent instanceof MapServerConnection) {
			parameterPanel = new MapServerParameterPanel((MapServerConnection) selectedComponent);

		} else if (selectedComponent instanceof Subscription) {
			parameterPanel = new SubscriptionParameterPanel((Subscription) selectedComponent);
		}

		if (parameterPanel != null) {
			mJpParameter.removeAll();

			LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 0.0, mJpParameter, parameterPanel, LayoutHelper.mLblInsets);
		}

		mJpParameter.updateUI();
		// super.pack();
	}

}
