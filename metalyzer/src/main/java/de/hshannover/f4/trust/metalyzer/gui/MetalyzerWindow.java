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
 * This file is part of visitmeta metalyzer, version 0.0.1,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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
package de.hshannover.f4.trust.metalyzer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import net.java.dev.designgridlayout.DesignGridLayout;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.MetalyzerStarter;
import de.hshannover.f4.trust.metalyzer.gui.exporter.PdfExporter;
import de.hshannover.f4.trust.metalyzer.gui.labels.InfoLabels;
import de.hshannover.f4.trust.metalyzer.gui.misc.MessageBox;
import de.hshannover.f4.trust.metalyzer.gui.misc.StatusBar;
import de.hshannover.f4.trust.metalyzer.gui.network.AnalyseConnection;
import de.hshannover.f4.trust.metalyzer.gui.network.FactoryAnalyseConnection;
import de.hshannover.f4.trust.metalyzer.gui.network.TimePollService;
import de.hshannover.f4.trust.visitmeta.util.PropertiesReaderWriter;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class MetalyzerWindow extends JFrame {
	
	private static final Logger log = Logger.getLogger(MetalyzerStarter.class);
	
	private TimePollService mPollService; 
	private AnalyseConnection mConnection;
	private MetalyzerGuiController mGuiController;
	private MetalyzerPanel mMetalyzerView;
	private PropertiesReaderWriter mProperties;

	private JMenu mFileMenu;
	private JMenu mPrefMenu;
	private JMenu mHelpMenu;
	
	private JMenuItem mExitItem;
	private JMenuItem mSettingsItem;
	private JMenuItem mAboutItem;
	private JMenuItem mExportAll;
	private JMenuItem mExportSingle;

	private JSpinner mWidthSpinner;
	private JSpinner mHeightSpinner;
	private JSpinner mDelaySpinner;
	private JSpinner mTimesoutSpinner;

	private JTextField mExportPath;

	private StatusBar mStatusBar;

	public MetalyzerWindow() {
		loadProperties();
		initConnection();
		initWindow();
	}
	
	private void loadProperties() {
		try {
			String metalyzerConfig = MetalyzerWindow.class.getClassLoader().getResource("metalyzerConfig.properties").getPath();
			log.info("Config file loaded: "+metalyzerConfig);
			mProperties = new PropertiesReaderWriter(metalyzerConfig, false);
		} catch (Exception e) {
			String msg = "Error while reading the config files";
			log.fatal(msg);
			MessageBox.showErrorOutputDialog("Config Error", msg, e.toString());
			System.exit(1);
		}
	}
	
	private void initConnection() {
		mConnection = FactoryAnalyseConnection.getConnection(mProperties.getProperty("metalyzer.rest.url"), mProperties.getProperty("metalyzer.rest.conn"));
		log.debug("AnalyseConnection initialized");
		
		mPollService = new TimePollService(mProperties.getProperty("metalyzer.rest.url") ,mProperties.getProperty("metalyzer.rest.conn"));
		mPollService.setPollingDelay(Long.parseLong(mProperties.getProperty("metalyzer.poll.delay")));
		mPollService.setRequestTimeout(Long.parseLong(mProperties.getProperty("metalyzer.poll.timeout")));
	}

	private void initWindow() {
		mGuiController = new MetalyzerGuiController(mConnection, mPollService);
		log.debug("GuiController initialized");
		
		String title = mProperties.getProperty("metalyzer.title");
		int wndWidth = Integer.parseInt(mProperties.getProperty("metalyzer.wnd.width"));
		int wndHeight = Integer.parseInt(mProperties.getProperty("metalyzer.wnd.height"));
		mGuiController.setViewSize(wndWidth, wndHeight);
		
		this.setTitle(title);
		ImageIcon icon = new ImageIcon(MetalyzerWindow.class.getClassLoader().getResource("logo2.png").getPath());
		this.setIconImage(icon.getImage());
		this.setLocation(getScreenCenter(wndWidth, wndHeight));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setJMenuBar(createMenu());
		
		mMetalyzerView = mGuiController.getMetalyzerView();	
		this.add(mMetalyzerView, BorderLayout.CENTER);
		
		JLabel mainElement = new JLabel("");
		List<JComponent> trailingElements = new ArrayList<JComponent>();
		trailingElements.add(new JLabel("Connection URL: "+mProperties.getProperty("metalyzer.rest.url")));
		trailingElements.add(new JLabel("Connection Type: "+mProperties.getProperty("metalyzer.rest.conn")));
		mStatusBar = new StatusBar(mainElement,trailingElements);
		this.add(mStatusBar, BorderLayout.SOUTH);
		
		this.pack();
		this.setVisible(true);
	}
	
	private Point getScreenCenter(int wndWidth, int wndHeight) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point screenCenter = new Point(screenSize.width/2 - wndWidth/2, screenSize.height/2 - wndHeight/2);
		return screenCenter;
	}

	private JMenuBar createMenu() {
		JMenuBar mMenuBar = new JMenuBar();
		
		mFileMenu = new JMenu("File");
		mMenuBar.add(mFileMenu);
		
		mExportSingle = new JMenuItem("Export");
		mExportSingle.setAction(new ExportAction("Export",""));
		mExportAll = new JMenuItem("Export All");
		mExportAll.setAction(new ExportAction("Export All",""));
		mFileMenu.add(mExportSingle);
		mFileMenu.add(mExportAll);
		
		mFileMenu.addSeparator();
		mExitItem = new JMenuItem("Exit");
		mExitItem.setAction(new ExitAction("Exit","Closes the application"));
		mFileMenu.add(mExitItem);
		
		mPrefMenu = new JMenu("Preferences");
		mMenuBar.add(mPrefMenu);
		
		mSettingsItem = new JMenuItem("Settings");
		mSettingsItem.setAction(new SettingsAction("Settings","Opens the settings dialog"));
		mPrefMenu.add(mSettingsItem);
		
		mHelpMenu = new JMenu("Help");
		mMenuBar.add(mHelpMenu);
		
		mAboutItem = new JMenuItem("About");
		mAboutItem.setAction(new AboutAction("About","Opens the about dialog"));
		mHelpMenu.add(mAboutItem);
		
		return mMenuBar;
	}
	
	private JPanel createSettingsPanel() {
		JPanel settingsPanel = new JPanel();
		
		DesignGridLayout layout = new DesignGridLayout(settingsPanel);

		int width = Integer.parseInt(mProperties.getProperty("metalyzer.wnd.width"));
		int height = Integer.parseInt(mProperties.getProperty("metalyzer.wnd.height"));
		mWidthSpinner = new JSpinner(new SpinnerNumberModel(width, 640, 1920, 32));
		mHeightSpinner = new JSpinner(new SpinnerNumberModel(height, 480, 1080, 24));
		
		layout.row().grid().add(new JLabel("Note: You must restart the program for the changes to take effect."));
		
		JLabel wndSettings = new JLabel("Window Settings");
		wndSettings.setForeground(Color.BLUE);
		layout.row().left().add(wndSettings, new JSeparator()).fill();
		layout.row().grid(new JLabel("Width:")).empty(2).add(mWidthSpinner);
		layout.row().grid(new JLabel("Height:")).empty(2).add(mHeightSpinner);
		
		int delay = Integer.parseInt(mProperties.getProperty("metalyzer.poll.delay"));
		int timeout = Integer.parseInt(mProperties.getProperty("metalyzer.poll.timeout"));
		mDelaySpinner = new JSpinner(new SpinnerNumberModel(delay, 1, 20, 1));
		mTimesoutSpinner = new JSpinner(new SpinnerNumberModel(timeout, 1, 20, 1));
		
		JLabel pollSettings = new JLabel("Polling Settings");
		pollSettings.setForeground(Color.BLUE);
		layout.row().left().add(pollSettings, new JSeparator()).fill();
		layout.row().grid(new JLabel("Delay:")).empty(2).add(mDelaySpinner);
		layout.row().grid(new JLabel("Timeout:")).empty(2).add(mTimesoutSpinner);
		
		mExportPath = new JTextField();
		mExportPath.setText(mProperties.getProperty("metalyzer.export.path",""));
		mExportPath.setEditable(false);
		mExportPath.setBackground(Color.WHITE);
		
		JButton browseButton = new JButton("Browse");
		browseButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser pathChooser = new JFileChooser();
				pathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int ret = pathChooser.showOpenDialog(null);
				if(ret == JFileChooser.APPROVE_OPTION) {
					mExportPath.setText(pathChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		JLabel exportSettings = new JLabel("Export Output");
		exportSettings.setForeground(Color.BLUE);
		layout.row().left().add(exportSettings, new JSeparator()).fill();
		layout.row().right().add(mExportPath).fill().add(browseButton);
		
		
		return settingsPanel;
	}
	
	/**
	 * This class handels the closing of the application via menu
	 * */
	class ExitAction extends AbstractAction {
	    public ExitAction(String text, String desc) {
	        super(text, null);
	        putValue(SHORT_DESCRIPTION, desc);
	    }

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	/**
	 * This class handels the export of the cart and tables via menu
	 * */
	class ExportAction extends AbstractAction {
		private PdfExporter pdfExporter;
		
	    public ExportAction(String text, String desc) {
	        super(text, null);
	        putValue(SHORT_DESCRIPTION, desc);
	        pdfExporter = new PdfExporter(mGuiController, mProperties.getProperty("metalyzer.export.path"));
	    }

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Export")) {
				CategoryPanel category = mGuiController.getCurrentSelectionPanel();
				if(category == null) {
					MessageBox.showInfomationDialog("Information","You have to select at least one tab to export");
				} else {
					File exportFile = pdfExporter.showExportDialog("Export");
					pdfExporter.export(exportFile, category);
				}
			}
			if(e.getActionCommand().equals("Export All")) {
				List<CategoryPanel> categories = mGuiController.getMetalyzerView().getAllTabs();
				if(categories.size() == 0) {
					MessageBox.showInfomationDialog("Information","You have to select at least one tab to export");
				} else {
					File exportFile = pdfExporter.showExportDialog("Export All");
					pdfExporter.exportAll(exportFile, categories);
				}
			}
		}
	}
	

	/**
	 * This class handels the preferences of the application via menu
	 * */
	class SettingsAction extends AbstractAction {
	    public SettingsAction(String text, String desc) {
	        super(text, null);
	        putValue(SHORT_DESCRIPTION, desc);
	    }

		@Override
		public void actionPerformed(ActionEvent e) {
			JPanel settingsPanel = createSettingsPanel();
			boolean save = MessageBox.showCustomDialog(null, settingsPanel, "Settings", JOptionPane.PLAIN_MESSAGE, null, new Object[]{"Save", "Cancel"});
			if(save) {
				int width = (int) mWidthSpinner.getValue();
				int height = (int) mHeightSpinner.getValue();
				int delay = (int) mDelaySpinner.getValue();
				int timeout = (int) mTimesoutSpinner.getValue();
				String path = mExportPath.getText();
				try {
					mProperties.storeProperty("metalyzer.wnd.width", Integer.toString(width));
					mProperties.storeProperty("metalyzer.wnd.height", Integer.toString(height));
					mProperties.storeProperty("metalyzer.poll.delay", Integer.toString(delay));
					mProperties.storeProperty("metalyzer.poll.timeout", Integer.toString(timeout));
					mProperties.storeProperty("metalyzer.export.path", path);
					log.debug("Settings saved.");
				} catch (IOException e1) {
					MessageBox.showErrorDialog("Settings Error", "Cannot save the new values.");
				}
			}
		}
	}
	
	/**
	 * This class shows a about panel via menu
	 * */
	class AboutAction extends AbstractAction {
		public AboutAction(String text, String desc) {
	        super(text, null);
	        putValue(SHORT_DESCRIPTION, desc);
	    }

		@Override
		public void actionPerformed(ActionEvent e) {
			final JTextArea aboutText = new JTextArea(10, 25);
			aboutText.setEditable(false);
			aboutText.setOpaque(false);
			aboutText.setText(InfoLabels.ABOUT_INFO);
			ImageIcon icon = new ImageIcon(MetalyzerWindow.class.getClassLoader().getResource("logo.png"));
			MessageBox.showCustomDialog(null, aboutText, "About", JOptionPane.INFORMATION_MESSAGE, icon);
		}
	}
}
