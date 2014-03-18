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
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.HierarchyEventListener;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hshannover.f4.trust.visitmeta.datawrapper.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.datawrapper.PropertiesManager;
import de.hshannover.f4.trust.visitmeta.gui.GuiController;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.gui.util.RESTConnection;

public class ConnectionDialog extends JDialog{

	private static final long serialVersionUID = 3274298974215759835L;

	private static int gg = 0;

	private Insets mXinsets;
	private Insets mLblInsets;
	private Insets mNullInsets;

	private JTabbedPane mJtpMain;

	private JPanel mJpSouth;

	private JButton mJbClose;

	private MapServerPanel mConnectionPanelMapServer;
	private DataServicePanel mConnectionPanelDataService;

	private GuiController mGuiController;

	public static void main(String[] args) {
		ConnectionDialog temp = new ConnectionDialog();
		temp.setVisible(true);
	}

	public ConnectionDialog() {

		// Hilfsobjekte, damit alle Abstaende harmonisch sind.
		mNullInsets= new Insets(0,0,0,0); // keine Abstaende
		mLblInsets= new Insets(2,5,2,2); // Label-Abstaende
		mXinsets= new Insets(0,10,0,10); // Eingabkomponenten-Abstaende

		createDialog();
		createPanels();

	}

	public ConnectionDialog(GuiController guiController) {
		this();
		mGuiController = guiController;
	}

	public void createDialog() {
		setTitle("Manage REST connections");
		setModal(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 671, 500);

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

		mConnectionPanelDataService = new DataServicePanel(this);
		mConnectionPanelMapServer = new MapServerPanel(this);

		mJtpMain = new JTabbedPane();
		mJtpMain.add("Map Server Connections", mConnectionPanelMapServer);
		mJtpMain.add("Dataservice Connections", mConnectionPanelDataService);

		mJpSouth = new JPanel();
		mJpSouth.setLayout(new FlowLayout(FlowLayout.RIGHT));

		mJbClose = new JButton("Close");
		mJbClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});

		mJpSouth.add(mJbClose);

		//			 x  y  w  h  wx   wy
		addComponent(0, 0, 1, 1, 1.0, 1.0, getContentPane(), mJtpMain, mLblInsets);
		addComponent(0, 1, 1, 1, 0.0, 0.0, getContentPane(), mJpSouth, mLblInsets);
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

	public static List<DataserviceConnection> getDataserviceConnectionsFromProperties(){
		ArrayList<DataserviceConnection> dataserviceList = new ArrayList<DataserviceConnection>();
		int connectionCount = Integer.valueOf(PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT, "0"));

		for(int i=0; i<connectionCount; i++){
			String name = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_NAME(i), "localhost");
			String url = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_URL(i), "http://localhost:8000");
			boolean rawXml = Boolean.valueOf(PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_RAWXML(i), "true").toLowerCase());

			dataserviceList.add(new DataserviceConnection(name, url, rawXml));
		}

		return dataserviceList;
	}

	private abstract class TabPanel extends JPanel{

		private static final long serialVersionUID = -1419963248354632788L;

		private final static String newline = "\n";


		protected ConnectionDialog mContext;

		protected JSplitPane mJspContent;
		protected JPanel mJpLeftSplitPane;
		protected JPanel mJpRightSplitPane;
		protected JPanel mJpConnectionParameter;
		protected JPanel mJpLog;

		private JPanel mJpAddDeleteCopy;
		private JPanel mJpSouth;

		private JTextArea mJtaLogWindows;
		private JScrollPane mJspLogWindows;

		protected JButton mJbAdd;
		protected JButton mJbDelete;
		protected JButton mJbCopy;
		protected JButton mJbSave;

		protected JLabel mJlNoConnectionsYet;

		private DefaultListModel<DataserviceConnection> mListModelDataService;

		private TabPanel(ConnectionDialog context){
			mContext = context;
			createPanels();
			addConnectionList();

			if (!((DefaultListModel<?>)getConnectionList().getModel()).isEmpty()) {
				getConnectionList().setSelectedIndex(0);
			}
		}

		public void createPanels() {
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

			mJpSouth = new JPanel();
			mJpSouth.setLayout(new FlowLayout(FlowLayout.LEFT));

			mJbSave = new JButton("Save");
			mJpSouth.add(mJbSave);

			mJlNoConnectionsYet = new JLabel("No connections yet.");
			mJlNoConnectionsYet.setHorizontalAlignment(SwingConstants.CENTER);

			mJspContent = new JSplitPane();
			mJspContent.setLeftComponent(mJpLeftSplitPane);
			mJspContent.setRightComponent(mJlNoConnectionsYet);

			LoggerRepository test = Logger.getDefaultHierarchy();
			test.addHierarchyEventListener(new HierarchyEventListener() {

				@Override
				public void removeAppenderEvent(Category arg0, Appender arg1) {
					System.out.println("removeAppenderEvent");

				}

				@Override
				public void addAppenderEvent(Category arg0, Appender arg1) {
					System.out.println("addAppenderEvent" + arg1.getName());

				}
			});

			mJtaLogWindows = new JTextArea(5, 20);
			mJspLogWindows = new JScrollPane(mJtaLogWindows);
			mJtaLogWindows.setEditable(false);
			//			mJtaLogWindows.setCaretPosition(mJtaLogWindows.getDocument().getLength());


			Logger hh = Logger.getLogger(ConnectionDialog.class);
			hh.addAppender(new Appender() {

				@Override
				public void setName(String arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setLayout(Layout arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setErrorHandler(ErrorHandler arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public boolean requiresLayout() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public String getName() {
					return "Connection Log Appander";
				}

				@Override
				public Layout getLayout() {
					return new SimpleLayout();
				}

				@Override
				public Filter getFilter() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public ErrorHandler getErrorHandler() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void doAppend(LoggingEvent lEvent) {
					mJtaLogWindows.append(lEvent.getMessage() + newline);

					mJtaLogWindows.setCaretPosition(mJtaLogWindows.getDocument().getLength());

				}

				@Override
				public void close() {
					// TODO Auto-generated method stub

				}

				@Override
				public void clearFilters() {
					// TODO Auto-generated method stub

				}

				@Override
				public void addFilter(Filter arg0) {
					// TODO Auto-generated method stub

				}
			});

			hh.info(gg + "Hallo test");
			gg++;
			hh.info(gg + "Hallo test2");
			gg++;
			hh.info(gg + "Hallo test3");
			gg++;

			//			 x  y  w  h  wx   wy
			addComponent(0, 2, 1, 1, 0.0, 0.0, mJpLeftSplitPane, mJpAddDeleteCopy, mNullInsets);
			addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJspContent, mLblInsets);
			addComponent(0, 0, 1, 1, 1.0, 0.0, mJpConnectionParameter, getParameterPanel(), mLblInsets);
			addComponent(0, 0, 1, 1, 1.0, 1.0, mJpRightSplitPane, mJpConnectionParameter, mLblInsets);
			addComponent(0, 0, 1, 1, 0.0, 0.0, mJpLog, mJspLogWindows, mLblInsets);
			addComponent(0, 1, 1, 1, 0.0, 0.0, mJpRightSplitPane, mJpLog, mLblInsets);
			addComponent(0, 3, 1, 1, 0.0, 0.0, mJpLeftSplitPane, mJpSouth, mNullInsets);
		}

		private void addConnectionList() {
			//			 x  y  w  h  wx   wy
			addComponent(0, 1, 1, 1, 1.0, 1.0, mJpLeftSplitPane, getConnectionList(), mLblInsets);
		}

		protected abstract JPanel getParameterPanel();
		protected abstract JList<?> getConnectionList();

	}

	private class MapServerPanel extends TabPanel{

		private static final long serialVersionUID = -7620433767757449461L;

		private JList<RESTConnection> mJlMapServerConnections;
		private DefaultListModel<RESTConnection> mListModelMapServer;

		private MapServerParameterPanel mParameterPanel;

		private RESTConnection mPreviousConnection;

		private JComboBox<DataserviceConnection> mJcbDataServiceConnection;
		private JLabel mJlDataservice;
		private JPanel mJpDataserviceComboBox;

		public void updateDataserviceComboBox(){
			mJcbDataServiceConnection.removeAllItems();

			for(DataserviceConnection dc: getDataserviceConnectionsFromProperties()){
				mJcbDataServiceConnection.addItem(dc);
			}
		}

		public MapServerPanel(ConnectionDialog context) {
			super(context);

			mJcbDataServiceConnection = new JComboBox<DataserviceConnection>();

			updateDataserviceComboBox();

			mJpDataserviceComboBox = new JPanel();
			mJpDataserviceComboBox.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

			mJlDataservice = new JLabel("Dataservice:");

			mJpDataserviceComboBox.add(mJlDataservice);
			mJpDataserviceComboBox.add(mJcbDataServiceConnection);

			addComponent(0, 0, 1, 1, 0.0, 0.0, mJpLeftSplitPane, mJpDataserviceComboBox, mNullInsets);

			addListeners();

			DataserviceConnection dConnection = (DataserviceConnection) mJcbDataServiceConnection.getSelectedItem();
			updateRestConnectionsList(dConnection);

			if (!mListModelMapServer.isEmpty()) {
				mJlMapServerConnections.setSelectedIndex(0);
			}
		}

		private void addListeners() {
			mJlMapServerConnections.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent arg0) {
					RESTConnection param = mJlMapServerConnections.getSelectedValue();
					if (param == null) {
						return;
					}

					if(mPreviousConnection != null){
						mPreviousConnection.update(mParameterPanel.mJtfName.getText().trim(), mParameterPanel.mJtfUrl.getText().trim(), mParameterPanel.mJcbDump.isSelected());
						mPreviousConnection.setUsername(mParameterPanel.mJtfUsername.getText().trim());
						mPreviousConnection.setPassword(new String(mParameterPanel.mJtfPassword.getPassword()).trim());
					}

					mParameterPanel.mJtfName.setText(param.getName());
					mParameterPanel.mJtfUrl.setText(param.getUrl());
					mParameterPanel.mJcbDump.setSelected(param.isDumping());
					mParameterPanel.mJtfUsername.setText(param.getUsername());
					mParameterPanel.mJtfPassword.setText(param.getPassword());
					mParameterPanel.mJcbBasicAuthentication.setSelected(param.isBasicAuthentication());
					mJspContent.setRightComponent(mJpRightSplitPane);
					mJspContent.updateUI();

					mPreviousConnection = param;
				}
			});

			mJbAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String name = "NewConnection" + (mListModelMapServer.getSize() + 1);
					String url = "https://localhost:8443";
					RESTConnection param = new RESTConnection((DataserviceConnection) mJcbDataServiceConnection.getSelectedItem(), name, url, false);
					param.setBasicAuthentication(true);

					mListModelMapServer.add(mListModelMapServer.getSize(), param);
					mJspContent.setRightComponent(mJpRightSplitPane);
					mJlMapServerConnections.setSelectedIndex(mListModelMapServer.getSize() - 1);

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
							mJspContent.setRightComponent(mJlNoConnectionsYet);
							mJspContent.updateUI();

						}
					}
				}
			});

			mJbCopy.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					RESTConnection param =  mJlMapServerConnections.getSelectedValue().clone();
					mListModelMapServer.add(mListModelMapServer.getSize(), param);
					mJlMapServerConnections.setSelectedIndex(mListModelMapServer.getSize() - 1);
				}
			});


			mJbSave.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					DataserviceConnection tmpCon = mJlMapServerConnections.getSelectedValue().getDataserviceConnection();

					ClientConfig config = new DefaultClientConfig();
					Client client = Client.create(config);

					URI uri_connect = UriBuilder.fromUri(tmpCon.getUrl()).build();
					WebResource temp1 = client.resource(uri_connect);

					JSONObject jObj = new JSONObject();
					try {

						jObj.put("url", mParameterPanel.mJtfUrl.getText().trim());
						jObj.put("user", mParameterPanel.mJtfUsername.getText().trim());
						jObj.put("userPass", new String(mParameterPanel.mJtfPassword.getPassword()).trim());

					} catch (JSONException e1) {
						e1.printStackTrace();
					}

					String response = temp1.path(mJlMapServerConnections.getSelectedValue().getName()).type(MediaType.APPLICATION_JSON).put(String.class, jObj);
					System.out.println(response);

					GraphContainer connection = new GraphContainer(mJlMapServerConnections.getSelectedValue().getName(), mJlMapServerConnections.getSelectedValue());
					mGuiController.addConnection(connection);

					//										Connection vConnection = FactoryConnection.getConnection(ConnectionType.REST, mJlMapServerConnections.getSelectedValue());
					//										Calculator vCalculator = FactoryCalculator.getCalculator(CalculatorType.JUNG);
					//
					//										FacadeNetwork vNetwork = new FacadeNetwork(vConnection);
					//										FacadeLogic vLogic = new FacadeLogic(vNetwork, vCalculator);
					//										GraphConnection connController = new GraphConnection(vLogic);
					//
					//										mGuiController.addConnection(mJlMapServerConnections.getSelectedValue().getName(), connController, mJlMapServerConnections.getSelectedValue());
					//
					//										Thread vThreadNetwork = new Thread(vNetwork);
					//										Thread vThreadLogic = new Thread(vLogic);
					//
					//										vThreadNetwork.start();
					//										vThreadLogic.start();
				}

			});

			//			mJcbDataServiceConnection.addPropertyChangeListener(new PropertyChangeListener() {
			//
			//				@Override
			//				public void propertyChange(PropertyChangeEvent arg0) {
			//					System.out.println("propertyChange " + arg0);
			//					if(arg0.getOldValue() == null && arg0.getNewValue() == ){
			//						System.out.println("propertyChange != null " + arg0);
			//						DataserviceConnection dConnection = (DataserviceConnection) mJcbDataServiceConnection.getSelectedItem();
			//						updateRestConnectionsList(dConnection);
			//
			//						if (!mListModelMapServer.isEmpty()) {
			//							mJlMapServerConnections.setSelectedIndex(0);
			//						}
			//					}
			//				}
			//			});

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
			mListModelMapServer.removeAllElements();

			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);

			URI uri_connect = UriBuilder.fromUri(dConnection.getUrl()).build();
			WebResource temp1 = client.resource(uri_connect);
			JSONObject jsonResponse = temp1.accept(MediaType.APPLICATION_JSON).get(JSONObject.class);
			System.out.println(jsonResponse);

			Iterator<String> ii = jsonResponse.keys();
			while(ii.hasNext()){
				String jKey = ii.next();
				JSONObject jsonConnection;
				try {
					jsonConnection = jsonResponse.getJSONObject(jKey);
					RESTConnection restConn = new RESTConnection(dConnection, jKey, jsonConnection.getString("URL"), false);
					restConn.setBasicAuthentication(true);
					restConn.setUsername(jsonConnection.getString("Username"));
					restConn.setPassword(jsonConnection.getString("Password"));
					mListModelMapServer.addElement(restConn);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
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
				mListModelMapServer = new DefaultListModel<RESTConnection>();

				mJlMapServerConnections = new JList<RESTConnection>();
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

		private DataServicePanel(ConnectionDialog context){
			super(context);
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
					if (param == null) {
						return;
					}

					if(mPreviousConnection != null){
						mPreviousConnection.update(mParameterPanel.mJtfName.getText().trim(), mParameterPanel.mJtfUrl.getText().trim(), mParameterPanel.mJcbRawXML.isSelected());
					}

					mParameterPanel.mJtfName.setText(param.getName());
					mParameterPanel.mJtfUrl.setText(param.getUrl());
					mParameterPanel.mJcbRawXML.setSelected(param.isRawXml());

					mJspContent.setRightComponent(mJpRightSplitPane);
					mJspContent.updateUI();

					mPreviousConnection = param;
				}
			});

			mJbAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String name = "New Connection (" + (mListModelDataService.getSize() + 1) + ")";
					String url = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_DEFAULT_URL, "http://localhost:8000");
					DataserviceConnection param = new DataserviceConnection(name, url, true);

					mListModelDataService.add(mListModelDataService.getSize(), param);

					mJspContent.setRightComponent(mJpRightSplitPane);

					mJlDataServiceConnections.setSelectedIndex(mListModelDataService.getSize() - 1);

				}
			});

			mJbDelete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int index = mJlDataServiceConnections.getSelectedIndex();
					if (index >= 0) {
						mListModelDataService.remove(index);
						if (!mListModelDataService.isEmpty()) {
							index = (index == 0) ? 0 : index - 1;
							mJlDataServiceConnections.setSelectedIndex(index);
						} else {
							mJspContent.setRightComponent(mJlNoConnectionsYet);
							mJspContent.updateUI();

						}
					}
				}
			});

			mJbCopy.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					DataserviceConnection param =  mJlDataServiceConnections.getSelectedValue().clone();
					mListModelDataService.add(mListModelDataService.getSize(), param);
					mJlDataServiceConnections.setSelectedIndex(mListModelDataService.getSize() - 1);
				}
			});

			mJbSave.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					persistDataServiceConnections();
					mContext.mConnectionPanelMapServer.updateDataserviceComboBox();
				}

			});

		}

		private void persistDataServiceConnections(){
			int propertyCount = Integer.valueOf(PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT, "0"));
			for(int i=0; i<propertyCount; i++){
				removeDataServiceConnection(i);
			}

			if(mPreviousConnection != null){
				mPreviousConnection.update(mParameterPanel.mJtfName.getText().trim(), mParameterPanel.mJtfUrl.getText().trim(), mParameterPanel.mJcbRawXML.isSelected());
			}

			int count = mListModelDataService.size();
			PropertiesManager.storeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT, String.valueOf(count));

			for(int i=0; i<count; i++){
				PropertiesManager.storeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_NAME(i), mListModelDataService.get(i).getName());
				PropertiesManager.storeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_URL(i), mListModelDataService.get(i).getUrl());
				PropertiesManager.storeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_RAWXML(i), String.valueOf(mListModelDataService.get(i).isRawXml()));
			}
		}

		private void removeDataServiceConnection(int index){
			PropertiesManager.removeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_NAME(index));
			PropertiesManager.removeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_URL(index));
			PropertiesManager.removeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_RAWXML(index));

		}

		private void readProperties(){
			for(DataserviceConnection dc: getDataserviceConnectionsFromProperties()){
				addDataserviceConnection(dc);
			}
		}

		private void addDataserviceConnection(DataserviceConnection con){
			mListModelDataService.add(mListModelDataService.getSize(), con);
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
		private JLabel mJlBasicAuthentication;
		private JLabel mJlUsername ;
		private JLabel mJlPassword;
		private JLabel mJlMaxPollResultSize;

		private JTextField mJtfUrl;
		private JTextField mJtfName;
		private JTextField mJtfUsername;
		private JTextField mJtfMaxPollResultSize;

		private JPasswordField mJtfPassword;

		private JCheckBox mJcbDump;
		private JCheckBox mJcbBasicAuthentication;


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
			mJlDump = new JLabel("Dump");

			mJtfName = new JTextField();
			mJtfUrl = new JTextField();
			mJcbBasicAuthentication = new JCheckBox();
			mJcbBasicAuthentication.setEnabled(false);
			mJtfUsername = new JTextField();
			mJtfPassword = new JPasswordField();
			mJtfMaxPollResultSize = new JTextField();
			mJcbDump = new JCheckBox();


			//			 x  y  w  h  wx   wy
			addComponent(0, 0, 1, 1, 1.0, 1.0, this, mJlName, mLblInsets);
			addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJlUrl, mLblInsets);
			addComponent(0, 2, 1, 1, 1.0, 1.0, this, mJlBasicAuthentication, mLblInsets);
			addComponent(0, 3, 1, 1, 1.0, 1.0, this, mJlUsername, mLblInsets);
			addComponent(0, 4, 1, 1, 1.0, 1.0, this, mJlPassword, mLblInsets);
			addComponent(0, 5, 1, 1, 1.0, 1.0, this, mJlMaxPollResultSize, mLblInsets);
			addComponent(0, 6, 1, 1, 1.0, 1.0, this, mJlDump, mLblInsets);

			addComponent(1, 0, 1, 1, 1.0, 1.0, this, mJtfName, mLblInsets);
			addComponent(1, 1, 1, 1, 1.0, 1.0, this, mJtfUrl, mLblInsets);
			addComponent(1, 2, 1, 1, 1.0, 1.0, this, mJcbBasicAuthentication, mLblInsets);
			addComponent(1, 3, 1, 1, 1.0, 1.0, this, mJtfUsername, mLblInsets);
			addComponent(1, 4, 1, 1, 1.0, 1.0, this, mJtfPassword, mLblInsets);
			addComponent(1, 5, 1, 1, 1.0, 1.0, this, mJtfMaxPollResultSize, mLblInsets);
			addComponent(1, 6, 1, 1, 1.0, 1.0, this, mJcbDump, mLblInsets);
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
			addComponent(0, 0, 1, 1, 1.0, 1.0, this, mJlName, mLblInsets);
			addComponent(1, 0, 1, 1, 1.0, 1.0, this, mJtfName, mLblInsets);
			addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJlUrl, mLblInsets);
			addComponent(1, 1, 1, 1, 1.0, 1.0, this, mJtfUrl, mLblInsets);
			addComponent(0, 2, 1, 1, 1.0, 1.0, this, mJlRawXml, mLblInsets);
			addComponent(1, 2, 1, 1, 1.0, 1.0, this, mJcbRawXML, mLblInsets);
		}
	}
}
