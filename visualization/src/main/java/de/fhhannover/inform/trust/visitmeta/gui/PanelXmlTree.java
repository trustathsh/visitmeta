package de.fhhannover.inform.trust.visitmeta.gui;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
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
 * This file is part of VisITMeta, version 0.0.2, implemented by the Trust@FHH 
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@FHH
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
/* Imports ********************************************************************/
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
/* Class **********************************************************************/
/**
 * A Panel that shows the a tree generated of XML data.
 */
public class PanelXmlTree extends JScrollPane {
/* Attributes *****************************************************************/
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(PanelXmlTree.class);
	private JTree                  mTree          = null;
	private DocumentBuilderFactory mFactory       = null;
	private DocumentBuilder        mBuilder       = null;
/* Constructors ***************************************************************/
	public PanelXmlTree() {
		super();
		mTree    = new JTree(new DefaultMutableTreeNode("No Data."));
		mFactory = DocumentBuilderFactory.newInstance();
		try {
			mBuilder = mFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setViewportView(mTree);
	}
/* Methods ********************************************************************/
	/**
	 * Generate a tree of XML data.
	 * @param pXml the XML data to show in a tree.
	 * @param pTimer the timer to hide the window.
	 */
	public void fill(String pXml, final Timer pTimer) {
		LOGGER.trace("Method initTreeRoot(" + pXml + ", " + pTimer + ") called.");
		Document vDocument = null;
		DefaultMutableTreeNode vRoot = null;
		try {
			vDocument = mBuilder.parse(new InputSource(new StringReader(pXml)));
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (vDocument == null) {
			vRoot = new DefaultMutableTreeNode("No Data.");
		} else {
			Node vRootNode = vDocument.getFirstChild();
			vRoot = new DefaultMutableTreeNode(getNodeName(vRootNode));
			treeWalk(vRootNode, 0, vRoot);
		}
		mTree.setModel(new DefaultTreeModel(vRoot, true));
		/* Add MouseListener to JTree */
		mTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent pE) {
				LOGGER.trace("MouseListener mouseExited(" + pE +") called.");
				LOGGER.debug("Start timer to hide WindowNodeProperties.");
				pTimer.start();
			}
			@Override
			public void mouseEntered(MouseEvent pE) {
				LOGGER.trace("MouseListener mouseEntered(" + pE +") called.");
				LOGGER.debug("Stop timer to hide WindowNodeProperties.");
				pTimer.stop();
			}
		});
		/* Redraw Panel */
		repaint();
	}
	/**
	 * Walks throw the tree.
	 * @param pNode the Node.
	 * @param pLevel the currentlevel of node in the tree.
	 * @param pParentNode the ParrentNode of Node.
	 */
	private void treeWalk(Node pNode, int pLevel, DefaultMutableTreeNode pParentNode) {
		LOGGER.trace("Method treeWalk(" + pNode + ", " + pLevel + ", " + pParentNode + ") called.");
		DefaultMutableTreeNode vChildNode = null;
		if (pNode.hasChildNodes()) {
			++pLevel;
			NodeList vList = pNode.getChildNodes();
			int vLength = vList.getLength();
			for (int i = 0; i < vLength; ++i) {
				Node vChild = vList.item(i);
				if(vChild.getNodeType() == Node.TEXT_NODE) {
					String vValue = vChild.getNodeValue();
					if(vValue != null && vValue.length() > 0) {
		    			vChildNode = new DefaultMutableTreeNode(vValue);
		    			vChildNode.setAllowsChildren(false);
		    			pParentNode.add(vChildNode);
					}
				} else {
    				vChildNode = new DefaultMutableTreeNode(getNodeName(vChild));
    				pParentNode.add(vChildNode);
    				treeWalk(vList.item(i), pLevel, vChildNode);
				}
			}
		} else {
			String vValue = pNode.getNodeValue();
			if(vValue != null && vValue.length() > 0) {
    			vChildNode = new DefaultMutableTreeNode(vValue);
    			vChildNode.setAllowsChildren(false);
    			pParentNode.add(vChildNode);
			}
		}
	}

	/**
	 * Returns the Label for the node.
	 * @param pNode the node.
	 * @return String with NodeName + attributes (NodeName + NodeValue).
	 */
	private StringBuffer getNodeName(Node pNode) {
		StringBuffer vResult = new StringBuffer();
		vResult.append(pNode.getNodeName());
		if(pNode.hasAttributes()) {
			int vLastIndex = pNode.getAttributes().getLength() - 1;
			vResult.append(" (");
			/* Attributes */
			for(int k = 0 ; k < vLastIndex; ++k) {
				vResult.append(pNode.getAttributes().item(k).getNodeName());
				vResult.append(": ");
				vResult.append(pNode.getAttributes().item(k).getNodeValue());
				vResult.append(", ");
			}
			/* Last attribute */
			vResult.append(pNode.getAttributes().item(vLastIndex).getNodeName());
			vResult.append(": ");
			vResult.append(pNode.getAttributes().item(vLastIndex).getNodeValue());
			vResult.append(")");
		}
		return vResult;
	}
}
