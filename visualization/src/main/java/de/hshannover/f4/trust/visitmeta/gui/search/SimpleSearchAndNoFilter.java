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
 * This file is part of visitmeta-visualization, version 0.5.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatter;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;

/**
 * Implementation of the {@link SearchAndFilterStrategy}, that allows simple
 * search functionality and no filtering.
 *
 * Search terms support the following techniques:
 * <ul>
 * <li>multiple search terms, separated by a specified character; allows that a
 * search term matches different nodes. Example: <i>dev;ip<i>, matches on all
 * Identifiers and Metadata nodes that contain either <dev> or <ip>
 * <li>multiple arguments per search term, separated by a specified character;
 * allows that a single node has to match multiple strings to apply to a search
 * term. Example: <i>dev,switch</i>, matches on all Identifier and Metadata
 * nodes that contain both <i>dev</i> and <i>switch</i>
 * <li>check for exact matches by enclosing a string with a special character.
 * Example: <i>ip,"10.0.0.1"</i> matches only Identifier and Metadata nodes that
 * contain <dev> but also exactly contain the string <i>10.0.0.1</i>
 * </ul>
 *
 * @author Bastian Hellmann
 *
 */
public class SimpleSearchAndNoFilter implements SearchAndFilterStrategy {

	private static final String SEARCH_TERM_DELIMITER = ";";
	private static final String SUB_SEARCH_TERM_DELIMITER = ",";
	private static final String ENCLOSING_CHAR = "\"";

	private JPanel mSearchInputPanel;
	private Searchable mSearchableGraphPanel;
	private JTextField mInputTextfield;
	private JCheckBox mHideSearchMismatchCheckbox;

	/**
	 * Creates a new {@link SimpleSearchAndNoFilter} instance.
	 *
	 * @param searchableGraphPanel
	 *            a {@link Searchable} instance, used to visualize search terms.
	 */
	public SimpleSearchAndNoFilter(Searchable searchableGraphPanel) {
		mSearchableGraphPanel = searchableGraphPanel;

		initGUI();
	}

	/**
	 * Initializes the graphical user interface for this search implementation.
	 * Consists of a textfield for the search term and a checkbox, that is used
	 * to hide all nodes that don't match a search string.
	 */
	private void initGUI() {
		mSearchInputPanel = new JPanel();
		mSearchInputPanel.setLayout(new BoxLayout(mSearchInputPanel,
				BoxLayout.X_AXIS));

		DefaultFormatter formatter = new DefaultFormatter();
		formatter.setOverwriteMode(false);
		formatter.setCommitsOnValidEdit(true);
		mInputTextfield = new JFormattedTextField(formatter);
		mInputTextfield.setToolTipText("<html>Search terms separator: <b>"
				+ SEARCH_TERM_DELIMITER + "</b><br>"
				+ "Inner search term separator: <b>"
				+ SUB_SEARCH_TERM_DELIMITER + "</b><br>"
				+ "Exact match enclosing character: <b>" + ENCLOSING_CHAR
				+ "</b><br>"
				+ "Example: ip,10.0.0;mac,\"aa:bb:cc:dd:ee:ff\"<br>" 
				+ " detects and highlights all nodes that <b>contain</b> the strings "
				+ "<i>ip</i> and <i>10.0.0</i>,<br>"
				+ "as well as all nodes that <b>contain</b> the string mac and <b>exactly contain</b> "
				+ "the value <i>aa:bb:cc:dd:ee:ff</i>" + "</html>");

		mInputTextfield.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				mSearchableGraphPanel.search(mInputTextfield.getText());
			}
		});

		mHideSearchMismatchCheckbox = new JCheckBox();
		mHideSearchMismatchCheckbox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				mSearchableGraphPanel
						.setHideSearchMismatches(mHideSearchMismatchCheckbox
								.isSelected());
			}
		});

		mSearchInputPanel.add(new JLabel("Search: "));
		mSearchInputPanel.add(mInputTextfield);
		mSearchInputPanel.add(new JLabel("Hide search mismatches: "));
		mSearchInputPanel.add(mHideSearchMismatchCheckbox);
	}

	@Override
	public JPanel getJPanel() {
		return mSearchInputPanel;
	}

	/**
	 * Checks if a {@link Propable} object matches a given search term. This
	 * method separates the search term in sub-search terms when applicable, by
	 * splitting the search term with the specified character. Furthermore
	 * splits each sub-search term into single search terms and checks if all
	 * single search terms are applicable to the {@link Propable} object.
	 * Delegates to methods that search for equality or containedness of a
	 * search term.
	 *
	 * @param propable
	 *            either a {@link Identifier} or a {@link Metadata} object
	 * @param searchTerm
	 *            a search term
	 * @return true, if the {@link Propable} object matches the search term
	 */
	private boolean containsSearchTerm(Propable propable, String searchTerm) {
		if (!searchTerm.equals("")) {
			boolean foundSomething = false;
			String[] terms = searchTerm.split(SEARCH_TERM_DELIMITER);

			for (String term : terms) {
				String[] subTerms = term.split(SUB_SEARCH_TERM_DELIMITER);

				boolean allSubTermsFound = true;
				for (String subTerm : subTerms) {
					if (subTerms.length == 1) {
						if (isEnclosedSearchString(subTerm)) {
							allSubTermsFound = searchForEqualString(propable,
									subTerm.replaceAll(ENCLOSING_CHAR, ""));
						} else {
							allSubTermsFound = searchForContainingString(
									propable, subTerm);
						}
					} else {
						if (isEnclosedSearchString(subTerm)) {
							if (!searchForEqualString(propable,
									subTerm.replaceAll(ENCLOSING_CHAR, ""))) {
								allSubTermsFound = false;
							}
						} else {
							if (!searchForContainingString(propable, subTerm)) {
								allSubTermsFound = false;
							}
						}
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

	/**
	 * Checks if a search term is begins and ends ("is enclosed") with the
	 * specified character.
	 *
	 * @param searchTerm
	 *            a search term
	 * @return true, if the search term is enclosed by the specified character
	 */
	private boolean isEnclosedSearchString(String searchTerm) {
		return (searchTerm.startsWith(ENCLOSING_CHAR) && searchTerm
				.endsWith(ENCLOSING_CHAR));
	}

	/**
	 * Checks if the content of a {@link Propable} object matches a search
	 * string by means of containing it. All values (typename and properties)
	 * are checked with the contains-method of {@link String}.
	 *
	 * @param propable
	 *            either a {@link Identifier} or a {@link Metadata} object
	 * @param searchTerm
	 *            a search term
	 * @return true, if the any of the values of the {@link Propable} contains
	 *         the search string
	 */
	private boolean searchForContainingString(Propable propable,
			String searchTerm) {
		String typeName = propable.getTypeName();
		List<String> properties = propable.getProperties();

		if (typeName.contains(searchTerm)) {
			return true;
		}

		for (String property : properties) {
			if (propable.valueFor(property).contains(searchTerm)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if the content of a {@link Propable} object exactly matches a
	 * search string by means of complete equality. All values (typename and
	 * properties) are checked with the equals-method of {@link String} class.
	 *
	 * @param propable
	 *            either a {@link Identifier} or a {@link Metadata} object
	 * @param searchTerm
	 *            a search term
	 * @return true, if the any of the values of the {@link Propable} equals the
	 *         search string
	 */
	private boolean searchForEqualString(Propable propable, String searchTerm) {
		String typeName = propable.getTypeName();
		List<String> properties = propable.getProperties();

		if (typeName.equals(searchTerm)) {
			return true;
		}

		for (String property : properties) {
			if (propable.valueFor(property).equals(searchTerm)) {
				return true;
			}
		}

		return false;
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
