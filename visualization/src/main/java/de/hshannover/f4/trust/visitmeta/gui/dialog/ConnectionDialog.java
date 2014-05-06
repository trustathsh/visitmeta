package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.datawrapper.PropertiesManager;
import de.hshannover.f4.trust.visitmeta.gui.GuiController;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.gui.util.RestConnection;
import de.hshannover.f4.trust.visitmeta.network.DataserviceConnectionFactory;

public class ConnectionDialog extends JDialog{

	private static final long serialVersionUID = 3274298974215759835L;

	private static final Logger log = Logger.getLogger(ConnectionDialog.class);

	private JTextAreaAppander mJTextAreaAppander;

	// Auxiliary objects so that all distances are harmonious.
	private static final Insets nullInsets= new Insets(0,0,0,0); // no free space
	private static final Insets lblInsets= new Insets(2,5,2,2); // label distances
	private static final Insets xinsets= new Insets(0,10,0,10); // Input components Clearances

	private JTabbedPane mJtpMain;

	private JPanel mJpSouth;


	private JButton mJbClose;

	private JButton mJbSave;


	private MapServerPanel mConnectionPanelMapServer;

	private DataServicePanel mConnectionPanelDataService;


	private GuiController mGuiController;


	public static void main(String[] args) {
		ConnectionDialog temp = new ConnectionDialog();
		temp.setVisible(true);
	}

	public ConnectionDialog() {
		mJTextAreaAppander = new JTextAreaAppander();
		log.addAppender(mJTextAreaAppander);

		createDialog();
		createPanels();

		pack();

		//		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2, (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);
	}

	public ConnectionDialog(GuiController guiController) {
		this();
		mGuiController = guiController;
	}

	public void createDialog() {
		setTitle("Manage REST connections");
		setModal(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowListener() {
			@Override public void windowClosing(WindowEvent arg0) {
				dispose();
			}
			@Override public void windowClosed(WindowEvent arg0) {}
			@Override public void windowActivated(WindowEvent arg0) {}
			@Override public void windowDeactivated(WindowEvent arg0) {}
			@Override public void windowDeiconified(WindowEvent arg0) {}
			@Override public void windowIconified(WindowEvent arg0) {}
			@Override public void windowOpened(WindowEvent arg0) {}
		});
	}

	public void createPanels() {
		getContentPane().setLayout(new GridBagLayout());

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

		mJpSouth.add(mJbClose);
		mJpSouth.add(mJbSave);

		mConnectionPanelMapServer = new MapServerPanel();
		mConnectionPanelDataService = new DataServicePanel();

		mJtpMain = new JTabbedPane();
		mJtpMain.add("Map Server Connections", mConnectionPanelMapServer);
		mJtpMain.add("Dataservice Connections", mConnectionPanelDataService);

		//			 x  y  w  h  wx   wy
		addComponent(0, 0, 1, 1, 1.0, 1.0, getContentPane(), mJtpMain, lblInsets);
		addComponent(0, 1, 1, 1, 0.0, 0.0, getContentPane(), mJpSouth, lblInsets);
	}

	/**
	 * Hilfsroutine beim Hinzufuegen einer Komponente zu einem
	 * Container im GridBagLayout.
	 * Die Parameter sind Constraints beim Hinzufuegen.
	 * @param x x-Position
	 * @param y y-Position
	 * @param width Breite in Zellen
	 * @param height Hoehe in Zellen
	 * @param weightx Gewicht
	 * @param weighty Gewicht
	 * @param cont Container
	 * @param comp Hinzuzufuegende Komponente
	 * @param insets Abstaende rund um die Komponente
	 */
	private static void addComponent(int x, int y, int width, int height, double weightx, double weighty, Container cont, Component comp, Insets insets) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x; gbc.gridy = y;
		gbc.gridwidth = width; gbc.gridheight = height;
		gbc.weightx = weightx; gbc.weighty = weighty;
		gbc.insets= insets;
		cont.add(comp, gbc);
	}

	private static void switchJPanel(int x, int y, int width, int height, double weightx, double weighty, JPanel cont, Component compToAdd, Component compToRemove, Insets insets) {
		cont.remove(compToRemove);
		addComponent(x, y, width, height, weightx, weighty, cont, compToAdd, insets);
		cont.updateUI();
	}

	private abstract class TabPanel extends JPanel{

		private static final long serialVersionUID = -1419963248354632788L;

		protected JSplitPane mJspContent;
		protected JPanel mJpLeftSplitPane;
		protected JPanel mJpRightSplitPane;
		protected JPanel mJpConnectionParameter;
		protected JPanel mJpLog;

		private JPanel mJpAddDeleteCopy;
		//		private JPanel mJpSouth;

		private JTextArea mJtaLogWindows;
		private JScrollPane mJspLogWindows;

		protected JButton mJbAdd;
		protected JButton mJbDelete;
		protected JButton mJbCopy;

		protected JLabel mJlNoConnectionsYet;

		private DefaultListModel<DataserviceConnection> mListModelDataService;

		private TabPanel(){
			createPanels();
			addConnectionList();

			if (!((DefaultListModel<?>)getConnectionList().getModel()).isEmpty()) {
				getConnectionList().setSelectedIndex(0);
			}
		}

		private void createPanels() {
			setLayout(new GridBagLayout());

			mJpLeftSplitPane= new JPanel();
			mJpLeftSplitPane.setLayout(new GridBagLayout());
			mJpLeftSplitPane.setBorder(BorderFactory.createTitledBorder("Connection List"));

			mJpConnectionParameter= new JPanel();
			mJpConnectionParameter.setLayout(new GridBagLayout());
			mJpConnectionParameter.setBorder(BorderFactory.createTitledBorder("Connection Parameter"));

			mJpLog= new JPanel();
			mJpLog.setLayout(new GridBagLayout());
			mJpLog.setBorder(BorderFactory.createTitledBorder("Connection Log"));

			mJpRightSplitPane= new JPanel();
			mJpRightSplitPane.setLayout(new GridBagLayout());

			mJbAdd = new JButton("New");
			mJbDelete = new JButton("Delete");
			mJbCopy = new JButton("Duplicate");

			mJpAddDeleteCopy = new JPanel();
			mJpAddDeleteCopy.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

			mJpAddDeleteCopy.add(mJbAdd);
			mJpAddDeleteCopy.add(mJbDelete);
			mJpAddDeleteCopy.add(mJbCopy);

			//			mJpSouth = new JPanel();
			//			mJpSouth.setLayout(new FlowLayout(FlowLayout.LEFT));
			//


			mJlNoConnectionsYet = new JLabel("No connections yet.");
			mJlNoConnectionsYet.setHorizontalAlignment(SwingConstants.CENTER);

			mJspContent = new JSplitPane();
			mJspContent.setLeftComponent(mJpLeftSplitPane);
			mJspContent.setRightComponent(mJpRightSplitPane);

			mJtaLogWindows = new JTextArea(5, 70);
			mJtaLogWindows.setEditable(false);
			// for append logging messages
			mJTextAreaAppander.addJTextArea(mJtaLogWindows);

			mJspLogWindows = new JScrollPane(mJtaLogWindows);

			//			 x  y  w  h  wx   wy
			addComponent(0, 2, 1, 1, 0.0, 0.0, mJpLeftSplitPane, mJpAddDeleteCopy, nullInsets);
			addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJspContent, lblInsets);
			addComponent(0, 0, 1, 1, 1.0, 0.0, mJpConnectionParameter, mJlNoConnectionsYet, lblInsets);
			addComponent(0, 0, 1, 1, 1.0, 1.0, mJpRightSplitPane, mJpConnectionParameter, lblInsets);
			addComponent(0, 0, 1, 1, 1.0, 0.0, mJpLog, mJspLogWindows, lblInsets);
			addComponent(0, 1, 1, 1, 0.0, 0.0, mJpRightSplitPane, mJpLog, lblInsets);
			//			addComponent(0, 3, 1, 1, 0.0, 0.0, mJpLeftSplitPane, mJpSouth, mNullInsets);
		}

		private void addConnectionList() {
			//			 x  y  w  h  wx   wy
			addComponent(0, 1, 1, 1, 1.0, 1.0, mJpLeftSplitPane, getConnectionList(), lblInsets);
		}

		protected abstract JPanel getParameterPanel();
		protected abstract JList<?> getConnectionList();

	}

	private class MapServerPanel extends TabPanel{

		private static final long serialVersionUID = -7620433767757449461L;

		private JList<RestConnection> mJlMapServerConnections;
		private DefaultListModel<RestConnection> mListModelMapServer;

		private MapServerParameterPanel mParameterPanel;

		private RestConnection mPreviousConnection;

		private JComboBox<DataserviceConnection> mJcbDataServiceConnection;
		private JLabel mJlDataservice;
		private JPanel mJpDataserviceComboBox;

		public void updateDataserviceComboBox(){
			mJcbDataServiceConnection.removeAllItems();

			List<DataserviceConnection> dataserviceList = DataserviceConnectionFactory.getDataserviceConnectionsFromProperties();

			for(DataserviceConnection dc: dataserviceList){
				mJcbDataServiceConnection.addItem(dc);
			}
		}

		public MapServerPanel() {
			mParameterPanel = new MapServerParameterPanel();

			super.mJbDelete.setEnabled(false); // TODO Rest connection delete functionality

			mJcbDataServiceConnection = new JComboBox<DataserviceConnection>();

			updateDataserviceComboBox();

			mJpDataserviceComboBox = new JPanel();
			mJpDataserviceComboBox.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

			mJlDataservice = new JLabel("Dataservice:");

			mJpDataserviceComboBox.add(mJlDataservice);
			mJpDataserviceComboBox.add(mJcbDataServiceConnection);

			addComponent(0, 0, 1, 1, 0.0, 0.0, mJpLeftSplitPane, mJpDataserviceComboBox, nullInsets);

			addListeners();

			DataserviceConnection dConnection = (DataserviceConnection) mJcbDataServiceConnection.getSelectedItem();
			if(dConnection != null){
				updateRestConnectionsList(dConnection);
			}

			if (!mListModelMapServer.isEmpty()) {
				mJlMapServerConnections.setSelectedIndex(0);
			}
		}

		private void addListeners() {
			mJlMapServerConnections.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent arg0) {
					RestConnection param = mJlMapServerConnections.getSelectedValue();
					if (param != null) {
						if(mPreviousConnection != null){
							updateRestConnection(mPreviousConnection);
						}

						mParameterPanel.updatePanel(param);
						mPreviousConnection = param;

						mParameterPanel.mJtfName.setEditable(false);

						switchJPanel(0, 0, 1, 1, 1.0, 0.0, mJpConnectionParameter, getParameterPanel(), mJlNoConnectionsYet, lblInsets);
					}
				}
			});

			mJbAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String name = "NewConnection" + (mListModelMapServer.getSize() + 1);

					RestConnection param = new RestConnection((DataserviceConnection) mJcbDataServiceConnection.getSelectedItem(), name);
					param.setAuthenticationBasic(true);

					mListModelMapServer.add(mListModelMapServer.getSize(), param);

					switchJPanel(0, 0, 1, 1, 1.0, 0.0, mJpConnectionParameter, getParameterPanel(), mJlNoConnectionsYet, lblInsets);

					mJlMapServerConnections.setSelectedIndex(mListModelMapServer.getSize() - 1);

					mParameterPanel.mJtfName.setEditable(true);

				}
			});

			mJbDelete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int index = mJlMapServerConnections.getSelectedIndex();
					if (index >= 0) {
						mListModelMapServer.remove(index);
						if (!mListModelMapServer.isEmpty()) {
							index = (index == 0) ? 0 : index - 1;
							mJlMapServerConnections.setSelectedIndex(index);
						} else {
							switchJPanel(0, 0, 1, 1, 1.0, 0.0, mJpConnectionParameter, mJlNoConnectionsYet, getParameterPanel(), lblInsets);
						}
					}
				}
			});

			mJbCopy.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					RestConnection param =  mJlMapServerConnections.getSelectedValue().clone();
					mListModelMapServer.add(mListModelMapServer.getSize(), param);
					mJlMapServerConnections.setSelectedIndex(mListModelMapServer.getSize() - 1);
				}
			});

			mJbSave.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(mJtpMain.getSelectedComponent() == mConnectionPanelMapServer){
						RestConnection tmpCon = mJlMapServerConnections.getSelectedValue();

						updateRestConnection(tmpCon);

						tmpCon.saveInDataservice();

						GraphContainer connection = new GraphContainer(tmpCon);
						mGuiController.addConnection(connection);

						mParameterPanel.mJtfName.setEditable(false);

						log.info("new Map-Server connection is stored");
					}
				}

			});

			mJcbDataServiceConnection.addItemListener(new ItemListener(){

				@Override
				public void itemStateChanged(ItemEvent event) {
					System.out.println("itemStateChanged " + event);
					if(event.getStateChange() == ItemEvent.SELECTED){
						System.out.println("itemStateChanged = SELECTED" + event);
						DataserviceConnection dConnection = (DataserviceConnection) event.getItem();

						updateRestConnectionsList(dConnection);

						if (!mListModelMapServer.isEmpty()) {
							mJlMapServerConnections.setSelectedIndex(0);
						}
					}
				}});
		}

		private void updateRestConnectionsList(DataserviceConnection dConnection){
			log.info("Update connection list from Dataservice(" + dConnection.getName() + ")");
			mListModelMapServer.removeAllElements();

			List<RestConnection> connectionList = dConnection.loadRestConnections();

			for(RestConnection rConnection: connectionList){
				mListModelMapServer.addElement(rConnection);
			}
		}

		private void updateRestConnection(RestConnection restConnection){
			restConnection.setConnectionName(mParameterPanel.mJtfName.getText().trim());
			restConnection.setUrl(mParameterPanel.mJtfUrl.getText().trim());
			restConnection.setUsername(mParameterPanel.mJtfUsername.getText().trim());
			restConnection.setPassword(new String(mParameterPanel.mJtfPassword.getPassword()).trim());
			restConnection.setAuthenticationBasic(mParameterPanel.mJcbBasicAuthentication.isSelected());
			// TODO TRUSTSTORE_PATH
			// TODO TRUSTSTORE_PASS
			restConnection.setStartupConnect(mParameterPanel.mJcbConnectingAtStartUp.isSelected());
			restConnection.setStartupDump(mParameterPanel.mJcbDump.isSelected());
			restConnection.setMaxPollResultSize(mParameterPanel.mJtfMaxPollResultSize.getText().trim());
		}

		@Override
		protected JPanel getParameterPanel() {
			if(mParameterPanel == null){
				mParameterPanel = new MapServerParameterPanel();
			}
			return mParameterPanel;
		}

		@Override
		protected JList<?> getConnectionList() {
			if(mJlMapServerConnections == null){
				mListModelMapServer = new DefaultListModel<RestConnection>();

				mJlMapServerConnections = new JList<RestConnection>();
				mJlMapServerConnections.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				mJlMapServerConnections.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
				mJlMapServerConnections.setModel(mListModelMapServer);
			}
			return mJlMapServerConnections;
		}

	}

	private class DataServicePanel extends TabPanel{

		private static final long serialVersionUID = 6943047877185872808L;

		private JList<DataserviceConnection> mJlDataServiceConnections;
		private DefaultListModel<DataserviceConnection> mListModelDataService;

		private DataServiceParameterPanel mParameterPanel;

		private DataserviceConnection mPreviousConnection;

		private DataServicePanel(){
			mParameterPanel = new DataServiceParameterPanel();

			readProperties();
			addListeners();

			if (!mListModelDataService.isEmpty()) {
				mJlDataServiceConnections.setSelectedIndex(0);
			}
		}

		private void addListeners() {
			mJlDataServiceConnections.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent arg0) {
					DataserviceConnection param = mJlDataServiceConnections.getSelectedValue();
					if (param != null) {
						if(mPreviousConnection != null){
							updateDataserviceConnection(mPreviousConnection);
						}

						mParameterPanel.updatePanel(param);
						mPreviousConnection = param;

						switchJPanel(0, 0, 1, 1, 1.0, 0.0, mJpConnectionParameter, getParameterPanel(), mJlNoConnectionsYet, lblInsets);
					}
				}
			});

			mJbAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String name = "New Connection (" + (mListModelDataService.getSize() + 1) + ")";
					String url = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_DEFAULT_URL, "http://localhost:8000");
					DataserviceConnection param = new DataserviceConnection(name, url, true);
					param.saveInProperty();

					mListModelDataService.add(mListModelDataService.getSize(), param);
					mJlDataServiceConnections.setSelectedIndex(mListModelDataService.getSize() - 1);
					switchJPanel(0, 0, 1, 1, 1.0, 0.0, mJpConnectionParameter, getParameterPanel(), mJlNoConnectionsYet, lblInsets);



				}
			});

			mJbDelete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int index = mJlDataServiceConnections.getSelectedIndex();
					if (index >= 0) {
						mJlDataServiceConnections.getSelectedValue().deleteFromProperty();
						mListModelDataService.remove(index);
						if (!mListModelDataService.isEmpty()) {
							index = (index == 0) ? 0 : index - 1;
							mJlDataServiceConnections.setSelectedIndex(index);
						} else {
							switchJPanel(0, 0, 1, 1, 1.0, 0.0, mJpConnectionParameter, mJlNoConnectionsYet, getParameterPanel(), lblInsets);
						}
					}
				}
			});

			mJbCopy.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					DataserviceConnection param =  mJlDataServiceConnections.getSelectedValue().clone();
					param.saveInProperty();
					mListModelDataService.add(mListModelDataService.getSize(), param);
					mJlDataServiceConnections.setSelectedIndex(mListModelDataService.getSize() - 1);
				}
			});

			mJbSave.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(mJtpMain.getSelectedComponent() == mConnectionPanelDataService){
						if(mPreviousConnection != null){
							updateDataserviceConnection(mPreviousConnection);
						}
						mJlDataServiceConnections.getSelectedValue().saveInProperty();
						mConnectionPanelMapServer.updateDataserviceComboBox();

						log.info("DataService Connections was persist");
					}
				}
			});
		}

		private void readProperties(){
			List<DataserviceConnection> dataserviceList = DataserviceConnectionFactory.getDataserviceConnectionsFromProperties();
			for(DataserviceConnection dc: dataserviceList){
				addDataserviceConnection(dc);
			}
		}

		private void addDataserviceConnection(DataserviceConnection con){
			mListModelDataService.add(mListModelDataService.getSize(), con);
		}

		private void updateDataserviceConnection(DataserviceConnection dataConnection){
			dataConnection.setName(mParameterPanel.mJtfName.getText().trim());
			dataConnection.setUrl(mParameterPanel.mJtfUrl.getText().trim());
			dataConnection.setRawXml(mParameterPanel.mJcbRawXML.isSelected());
		}

		@Override
		protected JPanel getParameterPanel() {
			if(mParameterPanel == null){
				mParameterPanel = new DataServiceParameterPanel();
			}
			return mParameterPanel;
		}

		@Override
		protected JList<?> getConnectionList() {
			if(mJlDataServiceConnections == null){
				mListModelDataService = new DefaultListModel<DataserviceConnection>();

				mJlDataServiceConnections = new JList<DataserviceConnection>();
				mJlDataServiceConnections.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				mJlDataServiceConnections.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
				mJlDataServiceConnections.setModel(mListModelDataService);
			}
			return mJlDataServiceConnections;
		}

	}

	private class MapServerParameterPanel extends JPanel{

		private static final long serialVersionUID = -3686612903315798696L;

		private JLabel mJlName;
		private JLabel mJlUrl ;
		private JLabel mJlDump;
		private JLabel mJlDumpDescription;
		private JLabel mJlDumpDescription2;
		private JLabel mJlDumpDescription3;
		private JLabel mJlBasicAuthentication;
		private JLabel mJlUsername ;
		private JLabel mJlPassword;
		private JLabel mJlMaxPollResultSize;
		private JLabel mJlConnectingAtStartUp;

		private JTextField mJtfUrl;
		private JTextField mJtfName;
		private JTextField mJtfUsername;
		private JTextField mJtfMaxPollResultSize;

		private JPasswordField mJtfPassword;

		private JCheckBox mJcbDump;
		private JCheckBox mJcbBasicAuthentication;
		private JCheckBox mJcbConnectingAtStartUp;


		private MapServerParameterPanel(){
			createPanels();
			addListeners();
		}

		private void addListeners(){
			final Component test = this;
			mJcbDump.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (mJcbDump.isSelected()) {
						JOptionPane.showMessageDialog(test,
								"Dumping is NOT IF-MAP 2.0 compliant and can only be used with irond.",
								"Warning", JOptionPane.WARNING_MESSAGE);
					}
				}
			});
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
			mJlDump = new JLabel("Dump");
			mJlDumpDescription = new JLabel("Dumping is NOT IF-MAP 2.0 compliant!");
			mJlDumpDescription.setHorizontalAlignment(SwingConstants.CENTER);
			mJlDumpDescription.setEnabled(false);
			mJlDumpDescription2 = new JLabel("That is currently only supported by irond.");
			mJlDumpDescription2.setHorizontalAlignment(SwingConstants.CENTER);
			mJlDumpDescription2.setEnabled(false);
			mJlDumpDescription3 = new JLabel("The dump is used to bootstrap the visualization process.");
			mJlDumpDescription3.setHorizontalAlignment(SwingConstants.CENTER);
			mJlDumpDescription3.setEnabled(false);

			mJtfName = new JTextField();
			mJtfName.setEditable(false);
			mJtfUrl = new JTextField();
			mJcbBasicAuthentication = new JCheckBox();
			mJcbBasicAuthentication.setEnabled(false);
			mJcbConnectingAtStartUp = new JCheckBox();
			mJtfUsername = new JTextField();
			mJtfPassword = new JPasswordField();
			mJtfMaxPollResultSize = new HintTextField("Optional");
			mJcbDump = new JCheckBox();


			//			 x  y  w  h  wx   wy
			addComponent(0, 0, 1, 1, 1.0, 1.0, this, mJlName, lblInsets);
			addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJlUrl, lblInsets);
			addComponent(0, 2, 1, 1, 1.0, 1.0, this, mJlBasicAuthentication, lblInsets);
			addComponent(0, 3, 1, 1, 1.0, 1.0, this, mJlUsername, lblInsets);
			addComponent(0, 4, 1, 1, 1.0, 1.0, this, mJlPassword, lblInsets);
			addComponent(0, 5, 1, 1, 1.0, 1.0, this, mJlMaxPollResultSize, lblInsets);
			addComponent(0, 6, 1, 1, 1.0, 1.0, this, mJlConnectingAtStartUp, lblInsets);
			addComponent(0, 7, 1, 1, 1.0, 1.0, this, mJlDump, lblInsets);
			addComponent(0, 8, 2, 1, 1.0, 1.0, this, mJlDumpDescription, nullInsets);
			addComponent(0, 9, 2, 1, 1.0, 1.0, this, mJlDumpDescription2, nullInsets);
			addComponent(0, 10, 2, 1, 1.0, 1.0, this, mJlDumpDescription3, nullInsets);

			addComponent(1, 0, 1, 1, 1.0, 1.0, this, mJtfName, lblInsets);
			addComponent(1, 1, 1, 1, 1.0, 1.0, this, mJtfUrl, lblInsets);
			addComponent(1, 2, 1, 1, 1.0, 1.0, this, mJcbBasicAuthentication, lblInsets);
			addComponent(1, 3, 1, 1, 1.0, 1.0, this, mJtfUsername, lblInsets);
			addComponent(1, 4, 1, 1, 1.0, 1.0, this, mJtfPassword, lblInsets);
			addComponent(1, 5, 1, 1, 1.0, 1.0, this, mJtfMaxPollResultSize, lblInsets);
			addComponent(1, 6, 1, 1, 1.0, 1.0, this, mJcbConnectingAtStartUp, lblInsets);
			addComponent(1, 7, 1, 1, 1.0, 1.0, this, mJcbDump, lblInsets);
		}

		private void updatePanel(RestConnection restConnection){
			mJtfName.setText(restConnection.getConnectionName());
			mJtfUrl.setText(restConnection.getUrl());
			mJtfUsername.setText(restConnection.getUsername());
			mJtfPassword.setText(restConnection.getPassword());
			mJcbBasicAuthentication.setSelected(restConnection.isAuthenticationBasic());
			// TODO TRUSTSTORE_PATH
			// TODO TRUSTSTORE_PASS
			mJcbConnectingAtStartUp.setSelected(restConnection.isStartupConnect());
			mJcbDump.setSelected(restConnection.isStartupDump());
			mJtfMaxPollResultSize.setText(restConnection.getMaxPollResultSize());
		}
	}

	private class DataServiceParameterPanel extends JPanel{

		private static final long serialVersionUID = -4830135051242549298L;

		private JLabel mJlName;
		private JLabel mJlUrl ;
		private JLabel mJlRawXml;

		private JTextField mJtfUrl;
		private JTextField mJtfName;

		public JCheckBox mJcbRawXML;


		private DataServiceParameterPanel(){
			createPanels();
		}

		private void createPanels() {
			setLayout(new GridBagLayout());

			mJlUrl = new JLabel("Url");
			mJlName = new JLabel("Name");
			mJlRawXml = new JLabel("RAW-XML");

			mJtfUrl = new JTextField();
			mJtfName = new JTextField();

			mJcbRawXML = new JCheckBox();

			//			 x  y  w  h  wx   wy
			addComponent(0, 0, 1, 1, 1.0, 1.0, this, mJlName, lblInsets);
			addComponent(1, 0, 1, 1, 1.0, 1.0, this, mJtfName, lblInsets);
			addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJlUrl, lblInsets);
			addComponent(1, 1, 1, 1, 1.0, 1.0, this, mJtfUrl, lblInsets);
			addComponent(0, 2, 1, 1, 1.0, 1.0, this, mJlRawXml, lblInsets);
			addComponent(1, 2, 1, 1, 1.0, 1.0, this, mJcbRawXML, lblInsets);
		}

		private void updatePanel(DataserviceConnection dataConnection){
			mJtfName.setText(dataConnection.getName());
			mJtfUrl.setText(dataConnection.getUrl());
			mJcbRawXML.setSelected(dataConnection.isRawXml());
		}
	}
}
