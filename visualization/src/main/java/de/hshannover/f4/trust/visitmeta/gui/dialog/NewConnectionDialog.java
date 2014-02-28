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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerListModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hshannover.f4.trust.visitmeta.datawrapper.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.datawrapper.PropertiesManager;
import de.hshannover.f4.trust.visitmeta.graphCalculator.Calculator;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FacadeLogic;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FactoryCalculator;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FactoryCalculator.CalculatorType;
import de.hshannover.f4.trust.visitmeta.gui.GraphConnection;
import de.hshannover.f4.trust.visitmeta.gui.GuiController;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.gui.util.RESTConnection;
import de.hshannover.f4.trust.visitmeta.network.Connection;
import de.hshannover.f4.trust.visitmeta.network.FacadeNetwork;
import de.hshannover.f4.trust.visitmeta.network.FactoryConnection;
import de.hshannover.f4.trust.visitmeta.network.FactoryConnection.ConnectionType;

public class NewConnectionDialog extends JDialog{

	private static final long serialVersionUID = 3274298974215759835L;

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
		NewConnectionDialog temp = new NewConnectionDialog();
		temp.setVisible(true);
	}

	public NewConnectionDialog() {

		// Hilfsobjekte, damit alle Abstaende harmonisch sind.
		mNullInsets= new Insets(0,0,0,0); // keine Abstaende
		mLblInsets= new Insets(2,5,2,2); // Label-Abstaende
		mXinsets= new Insets(0,10,0,10); // Eingabkomponenten-Abstaende

		createDialog();
		createPanels();

	}

	public NewConnectionDialog(GuiController guiController) {
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

		mConnectionPanelDataService = new DataServicePanel();
		mConnectionPanelMapServer = new MapServerPanel(mConnectionPanelDataService.mListModelDataService);


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

	private abstract class TabPanel extends JPanel{

		private static final long serialVersionUID = -1419963248354632788L;

		protected JSplitPane mJspContent;
		private JPanel mJpConnectionList;
		protected JPanel mJpConnectionParameter;

		private JPanel mJpAddDeleteCopy;
		private JPanel mJpConnectDisconnect;
		private JPanel mJpSouth;

		protected JButton mJbAdd;
		protected JButton mJbDelete;
		protected JButton mJbCopy;
		protected JButton mJbSave;
		protected JButton mJbConnect;
		protected JButton mJbDisconnect;
		//		protected JButton mJbAbort;

		protected JLabel mJlNoConnectionsYet;

		private DefaultListModel<DataserviceConnection> mListModelDataService;

		private TabPanel(DefaultListModel<DataserviceConnection> listModelDataService){
			mListModelDataService = listModelDataService;
			createPanels();
			addConnectionList();
			addListeners();

			if (!((DefaultListModel<?>)getConnectionList().getModel()).isEmpty()) {
				getConnectionList().setSelectedIndex(0);
			}
		}

		public void createPanels() {
			setLayout(new GridBagLayout());

			mJpConnectionList= new JPanel();
			mJpConnectionList.setLayout(new GridBagLayout());
			mJpConnectionList.setBorder(BorderFactory.createTitledBorder("Connection List"));

			mJpConnectionParameter= new JPanel();
			mJpConnectionParameter.setLayout(new GridBagLayout());
			mJpConnectionParameter.setBorder(BorderFactory.createTitledBorder("Connection Parameter"));

			mJbAdd = new JButton("New");
			mJbDelete = new JButton("Delete");
			mJbCopy = new JButton("Duplicate");
			mJbConnect = new JButton("Connect");
			mJbDisconnect = new JButton("Disconnect");

			mJpAddDeleteCopy = new JPanel();
			mJpAddDeleteCopy.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

			mJpAddDeleteCopy.add(mJbAdd);
			mJpAddDeleteCopy.add(mJbDelete);
			mJpAddDeleteCopy.add(mJbCopy);

			mJpConnectDisconnect = new JPanel();
			mJpConnectDisconnect.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

			mJpConnectDisconnect.add(mJbConnect);
			mJpConnectDisconnect.add(mJbDisconnect);

			mJpSouth = new JPanel();
			mJpSouth.setLayout(new FlowLayout(FlowLayout.RIGHT));

			mJbSave = new JButton("Save");
			//			mJbAbort = new JButton("Abort");

			//			mJpSouth.add(mJbAbort);
			mJpSouth.add(mJbSave);

			mJlNoConnectionsYet = new JLabel("No connections yet.");
			mJlNoConnectionsYet.setHorizontalAlignment(SwingConstants.CENTER);

			mJspContent = new JSplitPane();
			mJspContent.setLeftComponent(mJpConnectionList);
			mJspContent.setRightComponent(mJlNoConnectionsYet);

			//			 x  y  w  h  wx   wy
			addComponent(0, 1, 1, 1, 0.0, 0.0, mJpConnectionList, mJpAddDeleteCopy, mNullInsets);
			addComponent(0, 2, 1, 1, 0.0, 0.0, mJpConnectionList, mJpConnectDisconnect, mNullInsets);
			addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJspContent, mLblInsets);
			addComponent(0, 1, 1, 1, 1.0, 0.0, mJpConnectionParameter, getParameterPanel(mListModelDataService), mLblInsets);
			addComponent(0, 2, 2, 1, 0.0, 0.0, mJpConnectionParameter, mJpSouth, mLblInsets);
		}

		private void addConnectionList() {


			//			 x  y  w  h  wx   wy
			addComponent(0, 0, 1, 1, 1.0, 1.0, mJpConnectionList, getConnectionList(), mLblInsets);
		}

		protected abstract void addListeners();
		protected abstract JPanel getParameterPanel(DefaultListModel<DataserviceConnection> mListModelDataService2);
		protected abstract JList<?> getConnectionList();

	}

	private class MapServerPanel extends TabPanel{

		private static final long serialVersionUID = -7620433767757449461L;

		private JList<RESTConnection> mJlMapServerConnections;
		private DefaultListModel<RESTConnection> mListModelMapServer;
		private DefaultListModel<DataserviceConnection> mListModelDataService;

		private MapServerParameterPanel mParameterPanel;

		private RESTConnection mPreviousConnection;

		//		private MapServerPanel(){
		//
		//		}

		public MapServerPanel(DefaultListModel<DataserviceConnection> listModelDataService) {
			super(listModelDataService);
			mListModelDataService = listModelDataService;

			for(int i=0; i<listModelDataService.getSize(); i++){
				DataserviceConnection tmpCon = listModelDataService.get(i);

				ClientConfig config = new DefaultClientConfig();
				Client client = Client.create(config);

				URI uri_connect = UriBuilder.fromUri(tmpCon.getUrl()).build();
				WebResource temp1 = client.resource(uri_connect);
				JSONObject jsonResponse = temp1.accept(MediaType.APPLICATION_JSON).get(JSONObject.class);
				System.out.println(jsonResponse);

				Iterator<String> ii = jsonResponse.keys();
				while(ii.hasNext()){
					String jKey = ii.next();
					JSONObject jsonConnection;
					try {
						jsonConnection = jsonResponse.getJSONObject(jKey);
						RESTConnection restConn = new RESTConnection(tmpCon, jKey, jsonConnection.getString("URL"), false);
						restConn.setBasicAuthentication(true);
						restConn.setUsername(jsonConnection.getString("Username"));
						restConn.setPassword(jsonConnection.getString("Password"));
						mListModelMapServer.addElement(restConn);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			if (!mListModelMapServer.isEmpty()) {
				mJlMapServerConnections.setSelectedIndex(0);
			}
		}

		@Override
		protected void addListeners() {
			mJbDelete.setEnabled(false);
			mJbCopy.setEnabled(false);

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
					mParameterPanel.mJsDataServiceConnection.setValue(param.getDataserviceConnection());

					mJspContent.setRightComponent(mJpConnectionParameter);
					mJspContent.updateUI();

					mPreviousConnection = param;
				}
			});

			mJbAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String name = "NewConnection" + (mListModelMapServer.getSize() + 1);
					String url = "https://localhost:8443";
					RESTConnection param = new RESTConnection(name, url, false);
					param.setBasicAuthentication(true);
					param.setDataserviceConnection(mListModelDataService.firstElement());

					mListModelMapServer.add(mListModelMapServer.getSize(), param);

					mJspContent.setRightComponent(mJpConnectionParameter);

					mJlMapServerConnections.setSelectedIndex(mListModelMapServer.getSize() - 1);

				}
			});
			//
			//			mJbDelete.addActionListener(new ActionListener() {
			//				@Override
			//				public void actionPerformed(ActionEvent arg0) {
			//					int index = mJlDataServiceConnections.getSelectedIndex();
			//					if (index >= 0) {
			//						mListModelDataService.remove(index);
			//						if (!mListModelDataService.isEmpty()) {
			//							index = (index == 0) ? 0 : index - 1;
			//							mJlDataServiceConnections.setSelectedIndex(index);
			//						} else {
			//							mJspContent.setRightComponent(mJlNoConnectionsYet);
			//							mJspContent.updateUI();
			//
			//						}
			//					}
			//				}
			//			});
			//
			//			mJbCopy.addActionListener(new ActionListener() {
			//				@Override
			//				public void actionPerformed(ActionEvent arg0) {
			//					int index = mJlDataServiceConnections.getSelectedIndex();
			//					if (index >= 0) {
			//						DataserviceConnection param =  mListModelDataService.getElementAt(index).clone();
			//						mListModelDataService.add(mListModelDataService.getSize(), param);
			//
			//						mJlDataServiceConnections.setSelectedIndex(mListModelDataService.getSize() - 1);
			//					}
			//				}
			//			});
			//
			mJbSave.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					DataserviceConnection tmpCon = mJlMapServerConnections.getSelectedValue().getDataserviceConnection();
					
					GraphContainer connection = new GraphContainer(tmpCon.getName(), tmpCon);
					mGuiController.addConnection(connection);

//					ClientConfig config = new DefaultClientConfig();
//					Client client = Client.create(config);
//
//					URI uri_connect = UriBuilder.fromUri(tmpCon.getUrl()).build();
//					WebResource temp1 = client.resource(uri_connect);
//
//					JSONObject jObj = new JSONObject();
//					try {
//
//						jObj.put("url", mJlMapServerConnections.getSelectedValue().getUrl());
//						jObj.put("user", mJlMapServerConnections.getSelectedValue().getUsername());
//						jObj.put("userPass", mJlMapServerConnections.getSelectedValue().getPassword());
//
//					} catch (JSONException e1) {
//						e1.printStackTrace();
//					}
//
//					String response = temp1.path(mJlMapServerConnections.getSelectedValue().getName()).type(MediaType.APPLICATION_JSON).put(String.class, jObj);
//					System.out.println(response);
//
//					Connection vConnection = FactoryConnection.getConnection(ConnectionType.REST, mJlMapServerConnections.getSelectedValue());
//					Calculator vCalculator = FactoryCalculator.getCalculator(CalculatorType.JUNG);
//
//					FacadeNetwork vNetwork = new FacadeNetwork(vConnection);
//					FacadeLogic vLogic = new FacadeLogic(vNetwork, vCalculator);
//					GraphConnection connController = new GraphConnection(vLogic);
//
//					mGuiController.addConnection(mJlMapServerConnections.getSelectedValue().getName(), connController, mJlMapServerConnections.getSelectedValue());
//
//					Thread vThreadNetwork = new Thread(vNetwork);
//					Thread vThreadLogic = new Thread(vLogic);
//
//					vThreadNetwork.start();
//					vThreadLogic.start();
				}

			});

			mParameterPanel.mJcbDump.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (mParameterPanel.mJcbDump.isSelected()) {
						JOptionPane.showMessageDialog(mParameterPanel,
								"Dumping is NOT IF-MAP 2.0 compliant and can only be used with irond.",
								"Warning", JOptionPane.WARNING_MESSAGE);
						mJlMapServerConnections.getSelectedValue().startDump();
					}else{
						mJlMapServerConnections.getSelectedValue().stopDump();
					}
				}
			});

			mJbConnect.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					mJlMapServerConnections.getSelectedValue().connect();

				}
			});

			mJbDisconnect.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					mJlMapServerConnections.getSelectedValue().disconnect();

				}
			});
		}

		@Override
		protected JPanel getParameterPanel(DefaultListModel<DataserviceConnection> listModelDataService) {
			if(mParameterPanel == null){
				mParameterPanel = new MapServerParameterPanel(listModelDataService);
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

		private DataServicePanel(){
			super(null);
			readProperties();

			if (!mListModelDataService.isEmpty()) {
				mJlDataServiceConnections.setSelectedIndex(0);
			}
		}

		@Override
		protected void addListeners() {
			mJbConnect.setEnabled(false);
			mJbDisconnect.setEnabled(false);


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

					mJspContent.setRightComponent(mJpConnectionParameter);
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

					mJspContent.setRightComponent(mJpConnectionParameter);

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
					int index = mJlDataServiceConnections.getSelectedIndex();
					if (index >= 0) {
						DataserviceConnection param =  mListModelDataService.getElementAt(index).clone();
						mListModelDataService.add(mListModelDataService.getSize(), param);

						mJlDataServiceConnections.setSelectedIndex(mListModelDataService.getSize() - 1);
					}
				}
			});

			mJbSave.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					persistDataServiceConnections();
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
			int count = Integer.valueOf(PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT, "0"));
			for(int i=0; i<count; i++){
				String name = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_NAME(i), "localhost");
				String url = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_URL(i), "http://localhost:8000");
				boolean rawXml = Boolean.valueOf(PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_RAWXML(i), "true").toLowerCase());

				DataserviceConnection tmpConnection = new DataserviceConnection(name, url, rawXml);
				addDataserviceConnection(tmpConnection);
			}
		}

		private void addDataserviceConnection(DataserviceConnection con){
			mListModelDataService.add(mListModelDataService.getSize(), con);
		}

		@Override
		protected JPanel getParameterPanel(DefaultListModel<DataserviceConnection> listModelDataService) {
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
		private JLabel mJlDataServiceConnection;

		private JTextField mJtfUrl;
		private JTextField mJtfName;
		private JTextField mJtfUsername;
		private JPasswordField mJtfPassword;
		private JTextField mJtfMaxPollResultSize;

		public JCheckBox mJcbDump;
		public JCheckBox mJcbBasicAuthentication;

		private SpinnerListModel mDataService;
		private JSpinner mJsDataServiceConnection;

		private DefaultListModel<DataserviceConnection> mListModelDataService;

		private MapServerParameterPanel(){
			createPanels();
		}

		public MapServerParameterPanel(DefaultListModel<DataserviceConnection> listModelDataService) {
			mListModelDataService = listModelDataService;
			createPanels();
		}

		private void createPanels() {
			setLayout(new GridBagLayout());

			mJlName = new JLabel("Name");
			mJlUrl = new JLabel("Url");
			mJlBasicAuthentication = new JLabel("Basic Authentication");
			mJlBasicAuthentication.setEnabled(false);
			mJlUsername = new JLabel("Username");
			mJlPassword = new JLabel("Password");
			mJlMaxPollResultSize = new JLabel("max-poll-result-size");
			mJlDump = new JLabel("Dump");
			mJlDataServiceConnection = new JLabel("Data Service Connection");

			mJtfName = new JTextField();
			mJtfUrl = new JTextField();
			mJcbBasicAuthentication = new JCheckBox();
			mJcbBasicAuthentication.setEnabled(false);
			mJtfUsername = new JTextField();
			mJtfPassword = new JPasswordField();
			mJtfMaxPollResultSize = new JTextField();
			mJcbDump = new JCheckBox();

			List<DataserviceConnection> test = new ArrayList<DataserviceConnection>();
			for(int i=0; i<mListModelDataService.size(); i++){
				test.add(mListModelDataService.get(i));
			}

			mDataService = new SpinnerListModel(test);


			mJsDataServiceConnection = new JSpinner(mDataService);


			//			 x  y  w  h  wx   wy
			addComponent(0, 0, 1, 1, 1.0, 1.0, this, mJlName, mLblInsets);
			addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJlUrl, mLblInsets);
			addComponent(0, 2, 1, 1, 1.0, 1.0, this, mJlBasicAuthentication, mLblInsets);
			addComponent(0, 3, 1, 1, 1.0, 1.0, this, mJlUsername, mLblInsets);
			addComponent(0, 4, 1, 1, 1.0, 1.0, this, mJlPassword, mLblInsets);
			addComponent(0, 5, 1, 1, 1.0, 1.0, this, mJlMaxPollResultSize, mLblInsets);
			addComponent(0, 6, 1, 1, 1.0, 1.0, this, mJlDump, mLblInsets);
			addComponent(0, 7, 1, 1, 1.0, 1.0, this, mJlDataServiceConnection, mLblInsets);

			addComponent(1, 0, 1, 1, 1.0, 1.0, this, mJtfName, mLblInsets);
			addComponent(1, 1, 1, 1, 1.0, 1.0, this, mJtfUrl, mLblInsets);
			addComponent(1, 2, 1, 1, 1.0, 1.0, this, mJcbBasicAuthentication, mLblInsets);
			addComponent(1, 3, 1, 1, 1.0, 1.0, this, mJtfUsername, mLblInsets);
			addComponent(1, 4, 1, 1, 1.0, 1.0, this, mJtfPassword, mLblInsets);
			addComponent(1, 5, 1, 1, 1.0, 1.0, this, mJtfMaxPollResultSize, mLblInsets);
			addComponent(1, 6, 1, 1, 1.0, 1.0, this, mJcbDump, mLblInsets);
			addComponent(1, 7, 1, 1, 1.0, 1.0, this, mJsDataServiceConnection, mLblInsets);
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
