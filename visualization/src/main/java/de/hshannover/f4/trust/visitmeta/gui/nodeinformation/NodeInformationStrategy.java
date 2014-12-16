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
package de.hshannover.f4.trust.visitmeta.gui.nodeinformation;

import javax.swing.tree.DefaultMutableTreeNode;

import de.hshannover.f4.trust.visitmeta.gui.PanelXmlTree;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;

/**
 * Abstract superclass for classes that create the content for
 * a {@link PanelXmlTree}, representing {@link Identifier} and
 * {@link Metadata} within a tree-like structure.
 * 
 * @author Bastian Hellmann
 *
 */
public abstract class NodeInformationStrategy {

	/**
	 * Creates the tree structure rendered in a {@link PanelXmlTree},
	 * based on the type (identifier or metadata) and the information of the
	 * object itself.
	 * 
	 * Uses abstract methods to generate the tree structure for
	 * {@link Identifier} or {@link Metadata} instances.
	 *
	 * @param propable the {@link Propable} for creating the tree structure
	 * @return a {@link DefaultMutableTreeNode} instance with the complete
	 * generated tree structure
	 */
	public DefaultMutableTreeNode createNodeInformation(Propable propable) {
		DefaultMutableTreeNode result = null;
		if (propable instanceof Identifier) {
			result = fillIdentifier((Identifier) propable);
		} else if (propable instanceof Metadata) {
			result = fillMetadata((Metadata) propable);
		}

		if (result != null) {
			return result;
		} else {
			return new DefaultMutableTreeNode("No Data.");
		}
	}

	/**
	 * Creates the tree structure for a given {@link Metadata} instance.
	 * 
	 * @param metadata a {@link Metadata} instance
	 * @return a {@link DefaultMutableTreeNode} representing the content of the {@link Metadata} instance
	 */
	protected abstract DefaultMutableTreeNode fillMetadata(Metadata metadata);

	/**
	 * Creates the tree structure for a given {@link Identifier} instance.
	 * 
	 * @param identifier a {@link Identifier} instance
	 * @return a {@link DefaultMutableTreeNode} representing the content of the {@link Identifier} instance
	 */
	protected abstract DefaultMutableTreeNode fillIdentifier(Identifier identifier);
}
