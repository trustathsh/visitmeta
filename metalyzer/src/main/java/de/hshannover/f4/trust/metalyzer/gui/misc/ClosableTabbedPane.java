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
package de.hshannover.f4.trust.metalyzer.gui.misc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */


public class ClosableTabbedPane extends JTabbedPane {

	private CloseButtonTab mButtonTab;
	
	/**
	 * Adds a tab with close button to the ClosableTabbedPane
	 * @param title of the new tab  
	 * @param comp child component of the tab
	 * */
	@Override
	public void addTab(String title, Component component) {
		addTab(title,component,null);
	}
	
	/**
	 * Adds a tab with close button to the ClosableTabbedPane
	 * @param title of the new tab  
	 * @param comp child component of the tab
	 * @param tabCloseListener 
	 * */

	public void addTab(String title, Component component, ActionListener tabCloseListener) {
		super.addTab(title, component);
		int index = this.getTabCount() - 1;

		mButtonTab = new CloseButtonTab(this, title, component);
		if(tabCloseListener != null) {
			mButtonTab.addCloseButtonListener(tabCloseListener);
		}

		this.setTabComponentAt(index, mButtonTab);
		this.setSelectedIndex(index);
	}
	
	/**
	 * Adds a {@link ChangeListener} to the ClosableTabbedPane
	 * @param tabChangeListener
	 * */
	public void addTabChangeListener(ChangeListener tabChangeListener) {
		this.addChangeListener(tabChangeListener);
	}
	
	private class CloseButtonTab extends JPanel {
		private JLabel mTitleLabel;
		private JButton mCloseButton;

		public CloseButtonTab(final ClosableTabbedPane parent,
				final String title, final Component tab) {

			this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 3));
			this.setOpaque(false);

			mTitleLabel = new JLabel(title);
			this.add(mTitleLabel);

			mCloseButton = new CloseButton();
			mCloseButton.setMargin(new Insets(0, 0, 0, 0));
			mCloseButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					parent.remove(tab);
				}

			});
			this.add(mCloseButton);
		}

		public void addCloseButtonListener(ActionListener listener) {
			mCloseButton.addActionListener(listener);
		}
	}

	private class CloseButton extends JButton {
		
		private int mWidth;
		private int mHeight;
		
		public CloseButton() {
			this(16,16);
		}
		
		public CloseButton(int width, int height) {
			this.mWidth = width;
			this.mHeight = height;
			this.setPreferredSize(new Dimension(mWidth, mHeight));
		}
		
		/**
		 * Paints the cross of the close button
		 * @param g
		 */
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			int width = mWidth / 2;
			int height = mHeight / 2;
			int x = mWidth / 4;
			int y = mHeight / 4;

			g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_SQUARE,
					BasicStroke.CAP_ROUND));
			g2.setColor(Color.BLACK);
			g2.drawLine(x, y, x + width, y + height);
			g2.drawLine(x + width, y, x, y + height);
			if (getModel().isRollover()) {
				g2.setColor(Color.RED);
				g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_SQUARE,
						BasicStroke.CAP_ROUND));
				g2.drawLine(x, y, x + width, y + height);
				g2.drawLine(x + width, y, x, y + height);

			}
		}
	}

}
