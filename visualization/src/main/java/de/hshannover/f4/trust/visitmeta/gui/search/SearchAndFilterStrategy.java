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
 * This file is part of visitmeta-visualization, version 0.5.1,
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

import javax.swing.JPanel;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Interface for implementations of search and filtering functionality. Defines
 * both the GUI components (text input fields, ...) and the mechanisms to check
 * if a search term applies to {@link Metadata} or {@link Identifier} nodes.
 *
 * @author Bastian Hellmann
 *
 */
public interface SearchAndFilterStrategy {

	/**
	 * Returns a {@link JPanel} component that contains all needed GUI
	 * components for the specific search and filter methods.
	 *
	 * @return the associated {@link JPanel} of a
	 *         {@link SearchAndFilterStrategy}.
	 */
	public JPanel getJPanel();

	/**
	 * Checks if the values of a {@link Identifier} matches a given search term.
	 * The meaning of <i>matching</i> is dependent on the implementation of this
	 * interface.
	 *
	 * @param identifier
	 *            a {@link Identifier} node
	 * @param searchTerm
	 *            a {@link String} that defines a search term; the syntax of the
	 *            search term depends on the implementation of this interface
	 * @return true, if the search term matches the given {@link Identifier}
	 *         node
	 */
	public boolean containsSearchTerm(Identifier identifier, String searchTerm);

	/**
	 * Checks if the values of a {@link Metadata} matches a given search term.
	 * The meaning of <i>matching</i> is dependent on the implementation of this
	 * interface.
	 *
	 * @param metadata
	 *            a {@link Metadata} node
	 * @param searchTerm
	 *            a {@link String} that defines a search term; the syntax of the
	 *            search term depends on the implementation of this interface
	 * @return true, if the search term matches the given {@link Metadata} node
	 */
	public boolean containsSearchTerm(Metadata metadata, String searchTerm);

}
