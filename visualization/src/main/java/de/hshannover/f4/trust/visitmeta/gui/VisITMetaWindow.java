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
 * This file is part of visitmeta visualization, version 0.0.3,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.gui;




/* Imports ********************************************************************/
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.PropertiesManager;
/* Class **********************************************************************/
/**
 * Window-Class
 */
public class VisITMetaWindow extends JFrame {
/* Attributes *****************************************************************/
	private static final long   serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(VisITMetaWindow.class);
	private JSplitPane mSplitPane = null;
/* Constructors ***************************************************************/
	public VisITMetaWindow(GuiController pController, JComponent pGraphPanel) {
		super();
		setLookAndFeel();
		setMinimumSize(new Dimension(400, 0));
		/* Components */
		mSplitPane = new JSplitPane();
		MenuBar       vMenuBar        = new MenuBar(pController);
		JPanel        vRightComponent = new JPanel();
		SpringLayout  vSpringLayout   = new SpringLayout();
		PanelTimeLine vTimeLine       = new PanelTimeLine();
		Log4jAppender vLog4jAppender  = new Log4jAppender();
		JComponent    vLogPane        = vLog4jAppender.getComponent();
		Logger.getRootLogger().addAppender(vLog4jAppender);
		/* vSpringLayout */
		vSpringLayout.putConstraint(SpringLayout.NORTH, vTimeLine, 0, SpringLayout.NORTH, vRightComponent);
		vSpringLayout.putConstraint(SpringLayout.EAST,  vTimeLine, 0, SpringLayout.EAST,  vRightComponent);
		vSpringLayout.putConstraint(SpringLayout.WEST,  vTimeLine, 0, SpringLayout.WEST,  vRightComponent);
		vSpringLayout.putConstraint(SpringLayout.NORTH, vLogPane, 0, SpringLayout.SOUTH, vTimeLine);
		vSpringLayout.putConstraint(SpringLayout.EAST,  vLogPane, 0, SpringLayout.EAST,  vRightComponent);
		vSpringLayout.putConstraint(SpringLayout.WEST,  vLogPane, 0, SpringLayout.WEST,  vRightComponent);
		vSpringLayout.putConstraint(SpringLayout.SOUTH, vLogPane, 0, SpringLayout.SOUTH, vRightComponent);
		/* vRightComponent*/
		vRightComponent.setLayout(vSpringLayout);
		vRightComponent.add(vTimeLine);
		vRightComponent.add(vLogPane);
		/* vSplitPane */
		mSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mSplitPane.setLeftComponent(pGraphPanel);
		mSplitPane.setRightComponent(vRightComponent);
		/* mMainWindow */
		setJMenuBar(vMenuBar);
		getContentPane().add(mSplitPane);
		/* Load Properties */
		setTitle(PropertiesManager.getProperty("window", "title", "VisITMeta"));
		setLocation(
				Integer.parseInt(PropertiesManager.getProperty("window", "position.x", "0")), // x
				Integer.parseInt(PropertiesManager.getProperty("window", "position.y", "0"))  // y
		);
		setPreferredSize(new Dimension(
				Integer.parseInt(PropertiesManager.getProperty("window", "width",  "700")), // width
				Integer.parseInt(PropertiesManager.getProperty("window", "height", "700"))  // height
		));
		mSplitPane.setResizeWeight(Double.parseDouble(PropertiesManager.getProperty(
				"window",
				"splitpercent",
				"0.8"
		)));
		/* Close Listener */
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				PropertiesManager.storeProperty("window", "position.x",   String.valueOf((int) getLocationOnScreen().getX())); // x
				PropertiesManager.storeProperty("window", "position.y",   String.valueOf((int) getLocationOnScreen().getY())); // y
				PropertiesManager.storeProperty("window", "width",        String.valueOf(getWidth()));  // width
				PropertiesManager.storeProperty("window", "height",       String.valueOf(getHeight())); // height
//				PropertiesManager.storeProperty("window", "splitpercent", String.valueOf(mSplitPane.getResizeWeight()));
				System.exit(0);
			}
		});
	}
/* Methods ********************************************************************/
	/**
	 * Set the look and feel of java like the operating system.
	 */
	private void setLookAndFeel() {
		LOGGER.trace("Method setLookAndFeel() called.");
		try {
			switch(System.getProperty("os.name").toLowerCase()) {
				case "win": // Windows
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				break;
				case "linux": // Linux
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
				break;
//				case "nix": // Unix
//				break;
//				case "mac": // Mac
//				break;
//				case "sunos": // Solaris
//					UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
//				break;
				default:
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				break;
			}
		} catch (ClassNotFoundException | InstantiationException |
				IllegalAccessException | UnsupportedLookAndFeelException e
		) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
