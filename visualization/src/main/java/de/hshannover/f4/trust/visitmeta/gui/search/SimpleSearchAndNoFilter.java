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
 * This file is part of visitmeta-visualization, version 0.3.0,
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
package de.hshannover.f4.trust.visitmeta.gui.search;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatter;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;

/**
 * @author Bastian Hellmann
 *
 */
public class SimpleSearchAndNoFilter implements SearchAndFilterStrategy {

	private JPanel mSearchInputPanel;
	private Searchable mSearchableGraphPanel;
	private JTextField mInputTextfield;

	public SimpleSearchAndNoFilter(Searchable searchableGraphPanel) {
		mSearchableGraphPanel = searchableGraphPanel;

		initGUI();
	}

	private void initGUI() {
		mSearchInputPanel = new JPanel();
		mSearchInputPanel.setLayout(new BoxLayout(mSearchInputPanel,
				BoxLayout.X_AXIS));

		DefaultFormatter formatter = new DefaultFormatter();
		formatter.setCommitsOnValidEdit(true);
		mInputTextfield = new JFormattedTextField(formatter);

		mInputTextfield.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				mSearchableGraphPanel.search(mInputTextfield.getText());
			}
		});

		mSearchInputPanel.add(new JLabel("Search: "));
		mSearchInputPanel.add(mInputTextfield);
	}

	@Override
	public JPanel getJPanel() {
		return mSearchInputPanel;
	}

	/**
	 * @param propable
	 * @param searchTerm
	 * @return
	 */
	private boolean containsSearchTerm(Propable propable, String searchTerm) {
		if (!searchTerm.equals("")) {
			String rawData = propable.getRawData();

			boolean foundSomething = false;
			String[] terms = searchTerm.split(";");

			for (String term : terms) {
				String[] subTerms = term.split(",");
				boolean allSubTermsFound = true;

				String subTerm = "";

				for (int i = 0; i < subTerms.length; i++) {
					subTerm = subTerms[i];
					if (subTerms.length == 1) {
						allSubTermsFound = rawData.contains(subTerm);
					} else if (!rawData.contains(subTerm)) {
						allSubTermsFound = false;
					}
				}

				if (allSubTermsFound) {
					foundSomething = true;
				}
			}
			return foundSomething;
		} else {
			return false;
		}
	}

	@Override
	public boolean containsSearchTerm(Identifier identifier, String searchTerm) {
		return containsSearchTerm((Propable) identifier, searchTerm);
	}

	@Override
	public boolean containsSearchTerm(Metadata metadata, String searchTerm) {
		return containsSearchTerm((Propable) metadata, searchTerm);
	}

}
