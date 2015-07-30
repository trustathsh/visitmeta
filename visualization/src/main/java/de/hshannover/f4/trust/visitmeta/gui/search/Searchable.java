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

/**
 * Interface for GUI classes that display IF-MAP graphs and provide
 * functionality to highlight nodes based on a search term. Handling of the
 * search-mechanism itself is done be a {@link SearchAndFilterStrategy}.
 *
 * @author Bastian Hellmann
 *
 */
public interface Searchable {

	/**
	 * This method is called by a {@link SearchAndFilterStrategy} instance to
	 * signal the {@link Searchable} instance that a new search term was entered
	 * by the user and nodes may have to be rendered again with a
	 * {@link Searchable} specific highlighting.
	 *
	 * @param searchTerm
	 *            a {@link String} that defines a search term
	 */
	public void search(String searchTerm);

	public String getSearchTerm();

	/**
	 * Setter for a {@link SearchAndFilterStrategy}. Can then be used to
	 * delegate the decision, if a given node matches the given search term back
	 * to the {@link SearchAndFilterStrategy}
	 *
	 * @param strategy
	 *            implementation of the {@link SearchAndFilterStrategy}
	 *            interface
	 */
	public void setSearchAndFilterStrategy(SearchAndFilterStrategy strategy);

	public SearchAndFilterStrategy getSearchAndFilterStrategy();

	/**
	 * Sets a flag if mismatches of a given search term shall be "hidden" or
	 * otherwise visually rendered differently in comparison to nodes that do
	 * match the search term.
	 *
	 * @param b
	 *            boolean flag if mismatches shall be rendered as "hidden" to
	 *            emphasize the matches of a search
	 */
	public void setHideSearchMismatches(boolean b);

	public boolean getHideSearchMismatches();
}
