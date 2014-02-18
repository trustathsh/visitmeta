package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.hshannover.f4.trust.visitmeta.datawrapper.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.datawrapper.PropertiesManager;
import de.hshannover.f4.trust.visitmeta.gui.GuiController;
import de.hshannover.f4.trust.visitmeta.gui.util.RESTConnection;

public class ConnectionDialog extends JFrame {

	private static final long serialVersionUID = 2865708092367663841L;
	private JPanel mJpContent;
	private JPanel mJpNorth;
	private JPanel mJpSouth;
	private JPanel mJpRight;
	private JPanel mJpRightBlank;
	private JSplitPane mJsPcontent;
	private JTextField mJtFendpoint;
	private JTextField mJtFname;
	private JLabel mJlNoConnectionsYet;
	private JLabel mJlEndpoint ;
	private JLabel mJlName;
	private JLabel mJlDump;
	private JButton mJbAdd;
	private JButton mJbRemove;
	private JButton mJbCopy;
	private JButton mJbSave;
	private JButton mJbAbort;

	public JButton mJbTest;
	public JTextArea mJtATest;
	public JCheckBox mJcBdump;

	public DefaultListModel<RESTConnection> mListModel;
	public JList<RESTConnection> mConnectionList;
	public Set<Integer> indexesToRemove;

	private RESTConnection mPreviousConnection;

	private GuiController mContoller;

	private void newPanels(){
		mJpContent = new JPanel();
		mJpContent.setBorder(new EmptyBorder(5, 5, 5, 5));
		mJpContent.setLayout(new BorderLayout(0, 0));

		mJsPcontent = new JSplitPane();

		mJpRight = new JPanel();
		mJpRight.setLayout(null);

		mJpRightBlank = new JPanel();
		mJpRightBlank.setLayout(null);

		mJpNorth = new JPanel();
		mJpNorth.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		mJpSouth = new JPanel();
		mJpSouth.setLayout(new FlowLayout(FlowLayout.RIGHT));

		mJtATest = new JTextArea();
		mJtATest.setBorder(new LineBorder(new Color(0, 0, 0)));
		mJtATest.setLayout(null);
		//		mJtATest.setBounds(145, 397, 336, 166); // ori
		mJtATest.setBounds(145, 190, 336, 166);

	}

	private void addToContentPane(){
		setContentPane(mJpContent);
		mJpContent.add(mJsPcontent, BorderLayout.CENTER);
		mJpContent.add(mJpNorth, BorderLayout.NORTH);
		mJpContent.add(mJpSouth, BorderLayout.SOUTH);
	}

	private void newLabels(){
		mJlNoConnectionsYet = new JLabel("No connections yet.");
		mJlNoConnectionsYet.setBounds(0, 234, 497, 16);
		mJlNoConnectionsYet.setHorizontalAlignment(SwingConstants.CENTER);

		mJlEndpoint = new JLabel("Endpoint");
		mJlEndpoint.setHorizontalAlignment(SwingConstants.RIGHT);
		mJlEndpoint.setBounds(3, 38, 130, 16);

		mJlName = new JLabel("Name");
		mJlName.setHorizontalAlignment(SwingConstants.RIGHT);
		mJlName.setBounds(0, 9, 133, 16);

		mJlDump = new JLabel("Dump");
		mJlDump.setHorizontalAlignment(SwingConstants.RIGHT);
		mJlDump.setBounds(3, 105, 130, 16);
	}

	private void newTextFiels(){
		mJtFendpoint = new JTextField();
		mJtFendpoint.setColumns(10);
		mJtFendpoint.setBounds(145, 38, 336, 22);

		mJtFname = new JTextField();
		mJtFname.setColumns(10);
		mJtFname.setBounds(145, 9, 227, 22);

	}

	private void newButtons(){
		mJbTest = new JButton("Test connection");
		mJbTest.setBounds(145, 145, 123, 25);

		mJbAdd = new JButton("New");
		mJbRemove = new JButton("Delete");
		mJbCopy = new JButton("Duplicate");
		mJbSave = new JButton("Save");
		mJbAbort = new JButton("Abort");
	}

	private void newCheckBox(){
		mJcBdump = new JCheckBox("");
		mJcBdump.setBounds(145, 102, 25, 25);
	}

	private void addListeners(){
		mConnectionList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				RESTConnection param = mConnectionList.getSelectedValue();
				if (param == null) {
					return;
				}

				updatePreviousRESTConnection(mJtFname.getText().trim(), mJtFendpoint.getText().trim(), mJcBdump.isSelected());

				mJtFname.setText(param.getName());
				mJtFendpoint.setText(param.getUrl());
				mJcBdump.setSelected(param.isDumping());

				mJsPcontent.setRightComponent(mJpRight);
				mJsPcontent.updateUI();

				mPreviousConnection = param;
			}
		});

		mJcBdump.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (mJcBdump.isSelected()) {
					JOptionPane
					.showMessageDialog(
							mJpContent,
							"Dumping is NOT IF-MAP 2.0 compliant and can only be used with irond.",
							"Warning", JOptionPane.WARNING_MESSAGE);
				}
				mConnectionList.getSelectedValue().setDumping(mJcBdump.isSelected());
			}
		});

		mJbAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = "New Connection (" + (mListModel.getSize() + 1) + ")";
				String url = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_DEFAULT_URL, "http://localhost:8000/default");
				RESTConnection param = new RESTConnection(name, url, false);
				param.setName(name);
				mListModel.add(mListModel.getSize(), param);

				mJsPcontent.setRightComponent(mJpRight);
				mJsPcontent.updateUI();

				mConnectionList.setSelectedIndex(mListModel.getSize() - 1);

			}
		});

		mJbRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index = mConnectionList.getSelectedIndex();
				if (index >= 0) {
					mListModel.remove(index);
					indexesToRemove.add(index);
					if (!mListModel.isEmpty()) {
						index = (index == 0) ? 0 : index - 1;
						mConnectionList.setSelectedIndex(index);
					} else {
						mJsPcontent.setRightComponent(mJpRightBlank);
						mJsPcontent.updateUI();

					}
				}
			}
		});

		mJbCopy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index = mConnectionList.getSelectedIndex();
				if (index >= 0) {
					RESTConnection param =  mListModel.getElementAt(index).clone();
					mListModel.add(mListModel.getSize(), param);

					mJsPcontent.setRightComponent(mJpRight);
					mJsPcontent.updateUI();

					mConnectionList.setSelectedIndex(mListModel.getSize() - 1);
				}
			}
		});

		mJbSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				persistRESTConnections();
				setVisible(false);
			}
		});

		mJbAbort.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
	}

	private void addToPanels(){

		mJsPcontent.setLeftComponent(mConnectionList);
		mJsPcontent.setRightComponent(mJpRightBlank);
		mJsPcontent.setDividerLocation(150);
		mJpRightBlank.add(mJlNoConnectionsYet);
		mJpRight.add(mJlEndpoint);
		mJpRight.add(mJtFendpoint);
		mJpRight.add(mJlName);
		mJpRight.add(mJtFname);
		mJpRight.add(mJbTest);
		mJpRight.add(mJtATest);
		mJpRight.add(mJlDump);
		mJpRight.add(mJcBdump);
		mJpNorth.add(mJbAdd);
		mJpNorth.add(mJbRemove);
		mJpNorth.add(mJbCopy);
		mJpSouth.add(mJbAbort);
		mJpSouth.add(mJbSave);
	}

	public ConnectionDialog(RESTConnection[] cons) {
		//		mContoller = guiController;

		setResizable(false);
		setTitle("Manage REST connections");
		setBounds(100, 100, 671, 500);

		indexesToRemove = new HashSet<Integer>();

		addData(cons);

		newPanels();
		addToContentPane();
		newLabels();
		newTextFiels();
		newButtons();
		newCheckBox();
		addToPanels();
		addListeners();

		readProperties();

		if (!mListModel.isEmpty()) {
			mConnectionList.setSelectedIndex(0);
		}

	}

	private void addData(RESTConnection[] cons) {
		mConnectionList = new JList<RESTConnection>();
		mConnectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mConnectionList.setBorder(new LineBorder(SystemColor.activeCaptionBorder));

		mListModel = new DefaultListModel<RESTConnection>();
		if (cons != null) {
			for (RESTConnection c : cons) {
				mListModel.add(mListModel.getSize(), c);
			}
		}

		mConnectionList.setModel(mListModel);
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

	private void persistRESTConnections(){
		int propertyCount = Integer.valueOf(PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_COUNT, "0"));
		for(Integer i: indexesToRemove){
			if(i < propertyCount){
				removeRESTConnection(i);
			}
		}

		updatePreviousRESTConnection(mJtFname.getText().trim(), mJtFendpoint.getText().trim(), mJcBdump.isSelected());

		int count = mListModel.size();
		PropertiesManager.storeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_COUNT, String.valueOf(count));

		for(int i=0; i<count; i++){
			PropertiesManager.storeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_COUNT_NAME(i), mListModel.get(i).getName());
			PropertiesManager.storeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_COUNT_URL(i), mListModel.get(i).getUrl());
			PropertiesManager.storeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_COUNT_DUMPING(i), String.valueOf(mListModel.get(i).isDumping()));
		}
	}

	private void updatePreviousRESTConnection(String name, String url, boolean dumping){
		if(mPreviousConnection != null){
			mPreviousConnection.update(name, url, dumping);
		}
	}

	private void addRESTConnection(RESTConnection con){
		mListModel.add(mListModel.getSize(), con);
	}

	private void removeRESTConnection(int index){
		PropertiesManager.removeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_COUNT_NAME(index));
		PropertiesManager.removeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_COUNT_URL(index));
		PropertiesManager.removeProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_COUNT_DUMPING(index));

	}

}
