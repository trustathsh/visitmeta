package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import de.hshannover.f4.trust.visitmeta.datawrapper.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.datawrapper.PropertiesManager;
import de.hshannover.f4.trust.visitmeta.gui.util.RESTConnection;

public class NewConnectionDialog extends JDialog{

	private static final long serialVersionUID = 3274298974215759835L;

	private Insets mXinsets;
	private Insets mLblInsets;
	private Insets mNullInsets;

	private JTabbedPane mJtpMain;

	private JPanel mJpSouth;

	private JButton mJbClose;

	private ConnectionPanel mConnectionPanelMapServer;
	private ConnectionPanel mConnectionPanelDataService;

	private DefaultListModel<RESTConnection> mListModelMapServer;
	private DefaultListModel<RESTConnection> mListModelDataService;

	public static void main(String[] args) {
		NewConnectionDialog temp = new NewConnectionDialog();
		temp.setVisible(true);
	}

	public NewConnectionDialog() {

		// Hilfsobjekte, damit alle Abstaende harmonisch sind.
		mNullInsets= new Insets(0,0,0,0); // keine Abstaende
		mLblInsets= new Insets(2,5,2,2); // Label-Abstaende
		mXinsets= new Insets(0,10,0,10); // Eingabkomponenten-Abstaende

		mListModelMapServer = new DefaultListModel<RESTConnection>();
		mListModelDataService = new DefaultListModel<RESTConnection>();

		readProperties();

		createDialog();
		createPanels();
		addListeners();

		pack();
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

		mConnectionPanelMapServer = new ConnectionPanel(new MapServerPanel(), mListModelMapServer);
		mConnectionPanelDataService = new ConnectionPanel(new DataServicePanel(), mListModelDataService);

		mJtpMain = new JTabbedPane();
		mJtpMain.add("Map Server Connections", mConnectionPanelMapServer);
		mJtpMain.add("Dataservice Connections", mConnectionPanelDataService);

		mJpSouth = new JPanel();
		mJpSouth.setLayout(new FlowLayout(FlowLayout.RIGHT));

		mJbClose = new JButton("Close");

		mJpSouth.add(mJbClose);

		//			 x  y  w  h  wx   wy
		addComponent(0, 0, 1, 1, 1.0, 1.0, getContentPane(), mJtpMain, mLblInsets);
		addComponent(0, 1, 1, 1, 0.0, 0.0, getContentPane(), mJpSouth, mLblInsets);
	}

	private void addListeners(){

	}

	private void readProperties(){
		int count = Integer.valueOf(PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_COUNT, "0"));

		for(int i=0; i<count; i++){
			String name = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_COUNT_NAME(i), "default");
			String url = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_COUNT_URL(i), "http://localhost:8000/default");
			boolean dumping = Boolean.valueOf(PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_COUNT_DUMPING(i), "false").toLowerCase());

			RESTConnection tmpConnection = new RESTConnection(name, url, dumping);
			addRESTConnection(tmpConnection);
		}
	}

	private void addRESTConnection(RESTConnection con){
		mListModelDataService.add(mListModelDataService.getSize(), con);
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

	private class ConnectionPanel extends JPanel{

		private static final long serialVersionUID = -1419963248354632788L;

		private JSplitPane mJspContent;
		private JPanel mJpConnectionList;
		private JPanel mJpConnectionParameter;

		private JPanel mJpNorth;
		private JPanel mJpSouth;

		private JButton mJbAdd;
		private JButton mJbRemove;
		private JButton mJbCopy;
		private JButton mJbSave;
		private JButton mJbAbort;

		public JList<RESTConnection> mJlConnection;
		public DefaultListModel<RESTConnection> mListModel;

		private JLabel mJlNoConnectionsYet;

		private JPanel mPanelToAdd;

		private ConnectionPanel(JPanel panelToAdd, DefaultListModel<RESTConnection> listModel){
			mPanelToAdd = panelToAdd;
			mListModel = listModel;

			createPanels();
			createLabels();
			addData();

			if (!mListModel.isEmpty()) {
				mJlConnection.setSelectedIndex(0);
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
			mJbRemove = new JButton("Delete");
			mJbCopy = new JButton("Duplicate");

			mJpNorth = new JPanel();
			mJpNorth.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

			mJpNorth.add(mJbAdd);
			mJpNorth.add(mJbRemove);
			mJpNorth.add(mJbCopy);

			mJpSouth = new JPanel();
			mJpSouth.setLayout(new FlowLayout(FlowLayout.RIGHT));

			mJbSave = new JButton("Save");
			mJbAbort = new JButton("Abort");

			mJpSouth.add(mJbAbort);
			mJpSouth.add(mJbSave);

			mJspContent = new JSplitPane();
			mJspContent.setLeftComponent(mJpConnectionList);
			mJspContent.setRightComponent(mJpConnectionParameter);
			mJspContent.setDividerLocation(150);

			//			 x  y  w  h  wx   wy
			addComponent(0, 0, 1, 1, 0.0, 0.0, this, mJpNorth, mNullInsets);
			addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJspContent, mLblInsets);
			addComponent(0, 1, 1, 1, 1.0, 0.0, mJpConnectionParameter, mPanelToAdd, mLblInsets);
			addComponent(0, 2, 2, 1, 0.0, 0.0, mJpConnectionParameter, mJpSouth, mLblInsets);
		}

		public void createLabels() {
			mJlNoConnectionsYet = new JLabel("No connections yet.");
			mJlNoConnectionsYet.setHorizontalAlignment(SwingConstants.CENTER);

			//			 x  y  w  h  wx   wy
			//			addComponent(0, 0, 1, 1, 0.0, 0.0, mJpConnectionParameter, mJlNoConnectionsYet, mLblInsets);
		}

		private void addData() {
			mJlConnection = new JList<RESTConnection>();
			mJlConnection.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			mJlConnection.setBorder(new LineBorder(SystemColor.activeCaptionBorder));

			mJlConnection.setModel(mListModel);

			//			 x  y  w  h  wx   wy
			addComponent(0, 0, 1, 1, 1.0, 1.0, mJpConnectionList, mJlConnection, mLblInsets);
		}

	}

	private class MapServerPanel extends JPanel{

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
		private JPasswordField mJtfPassword;
		private JTextField mJtfMaxPollResultSize;

		public JCheckBox mJcbDump;
		public JCheckBox mJcbBasicAuthentication;


		private MapServerPanel(){
			createPanels();
		}

		private void createPanels() {
			setLayout(new GridBagLayout());

			mJlName = new JLabel("Name");
			mJlUrl = new JLabel("Url");
			mJlBasicAuthentication = new JLabel("Basic Authentication");
			mJlUsername = new JLabel("Username");
			mJlPassword = new JLabel("Password");
			mJlMaxPollResultSize = new JLabel("max-poll-result-size");
			mJlDump = new JLabel("Dump");

			mJtfName = new JTextField();
			mJtfUrl = new JTextField();
			mJcbBasicAuthentication = new JCheckBox();
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

	private class DataServicePanel extends JPanel{

		private static final long serialVersionUID = -4830135051242549298L;

		private JLabel mJlName;
		private JLabel mJlUrl ;
		private JLabel mJlRawXml;

		private JTextField mJtfUrl;
		private JTextField mJtfName;

		public JCheckBox mJcbRawXML;


		private DataServicePanel(){
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
