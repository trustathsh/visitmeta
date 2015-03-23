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
package de.hshannover.f4.trust.visitmeta.gui;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.gui.nodeinformation.NodeInformationStrategy;
import de.hshannover.f4.trust.visitmeta.gui.nodeinformation.NodeInformationStrategyFactory;
import de.hshannover.f4.trust.visitmeta.gui.nodeinformation.NodeInformationStrategyType;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;

/**
 * A Panel that shows a tree generated of XML data.
 */
public class PanelXmlTree extends JScrollPane {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(PanelXmlTree.class);
	private JTree mTree = null;
	private NodeInformationStrategy mNodeInformationStrategy;

	private Properties mConfig = Main.getConfig();

	public PanelXmlTree() {
		super();
		mTree = new JTree(new DefaultMutableTreeNode("No Data."));

		String nodeInformationStrategyType = mConfig.getString(
				"visualization.node.information.style",
				NodeInformationStrategyType.XML_BREAKDOWN.name());
		mNodeInformationStrategy = NodeInformationStrategyFactory
				.create(NodeInformationStrategyType
						.valueOf(nodeInformationStrategyType));
		setViewportView(mTree);
	}

	/**
	 * Generate a tree of XML data.
	 *
	 * @param pData
	 *            the XML data to show in a tree.
	 */
	public void fill(Propable pData) {
		LOGGER.trace("Method fill(" + pData + ") called.");
		DefaultMutableTreeNode vRoot = mNodeInformationStrategy
				.createNodeInformation(pData);
		mTree.setModel(new DefaultTreeModel(vRoot, true));
		repaint();
	}
}
