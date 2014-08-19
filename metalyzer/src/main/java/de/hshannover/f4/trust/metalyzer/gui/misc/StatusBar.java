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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */


public class StatusBar extends JPanel {
	private JComponent leadComponent;
	private List<JComponent> trailingComponents;

	public StatusBar(JComponent lead) {
		this(lead, null);
	}

	public StatusBar(JComponent lead, List<JComponent> trailing) {
		this.leadComponent = lead;
		this.trailingComponents = trailing;

		this.setLayout(new BorderLayout());
		this.add(new JSeparator(), BorderLayout.NORTH);
		
		JPanel leadingPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		leadingPanel.add(createComponent(leadComponent));
		//Workaround: needs a zero size seperator to display the first component correctly
		leadingPanel.add(createVerticalSeperator(0));
		this.add(leadingPanel);
		
		if (trailingComponents != null && trailingComponents.size() > 0) {
			JPanel trailinPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
			for (JComponent c : trailingComponents) {
				trailinPanel.add(createVerticalSeperator(1));
				trailinPanel.add(createComponent(c));
			}
			this.add(trailinPanel, BorderLayout.EAST);
		}
	}

	/**
	 * Returns the lead component of the status bar
	 * @return the first {@link JComponent}
	 * */
	public JComponent getLeadComponent() {
		return leadComponent;
	}

	/**
	 * Returns a trailing component of the status bar
	 * @param index of a trailing component
	 * @return the first {@link JComponent}
	 * */
	public JComponent getTrailingComponentAt(int index) {
		if (trailingComponents == null || trailingComponents.isEmpty()) {
			return null;
		}
		return trailingComponents.get(index);
	}

	private JSeparator createVerticalSeperator(int width) {
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		separator.setPreferredSize(new Dimension(width, 15));
		return separator;
	}

	private JPanel createComponent(JComponent component) {
		JPanel panelComponent = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		panelComponent.add(component);
		return panelComponent;
	}

}
