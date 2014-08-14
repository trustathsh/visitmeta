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
 * This file is part of visitmeta visualization, version 0.1.2,
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

import java.io.IOException;
import java.io.StringReader;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;

/**
 * A class that implements {@link NodeInformationStrategy} and returns
 * a plain XML tree representation of {@link Identifier} and {@link Metadata}.
 * Plain XML means that the inner structure of the associated {@link Document}
 * object is directly converted to a {@link JTree} data model.
 * 
 * @author Bastian Hellmann
 *
 */
public class NodeInformationPlainXML extends NodeInformationStrategy {

	private DocumentBuilderFactory mFactory;
	private DocumentBuilder mBuilder;

	protected NodeInformationPlainXML() {
		mFactory = DocumentBuilderFactory.newInstance();
		try {
			mBuilder = mFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected DefaultMutableTreeNode fillMetadata(Metadata metadata) {
		return fill(metadata);
	}

	@Override
	protected DefaultMutableTreeNode fillIdentifier(Identifier identifier) {
		return fill(identifier);
	}

	/**
	 * Creates a plain XML tree structure for a given {@link Propable} instance.
	 * Handles both {@link Metadata} and {@link Identifier} the same way,
	 * traversing through the associated {@link Document} data.
	 * 
	 * @param propable a instance implementing the {@link Propable} interface
	 * @return a {@link DefaultMutableTreeNode} containing the whole tree
	 * structure for the given {@link Propable} instance
	 */
	private DefaultMutableTreeNode fill(Propable propable) {
		Document vDocument = null;
		DefaultMutableTreeNode vRoot = null;
		try {
			vDocument = mBuilder.parse(new InputSource(new StringReader(propable.getRawData())));
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		if (vDocument == null) {
			vRoot = new DefaultMutableTreeNode("No Data.");
		} else {
			Node vRootNode = vDocument.getFirstChild();
			vRoot = new DefaultMutableTreeNode(getNodeName(vRootNode));
			treeWalk(vRootNode, 0, vRoot);
		}
		return vRoot;
	}

	/**
	 * Walks throw the tree.
	 * 
	 * @param pNode
	 *            the Node.
	 * @param pLevel
	 *            the currentlevel of node in the tree.
	 * @param pParentNode
	 *            the ParrentNode of Node.
	 */
	private void treeWalk(Node pNode, int pLevel, DefaultMutableTreeNode pParentNode) {
		DefaultMutableTreeNode vChildNode = null;
		if (pNode.hasChildNodes()) {
			++pLevel;
			NodeList vList = pNode.getChildNodes();
			int vLength = vList.getLength();
			for (int i = 0; i < vLength; ++i) {
				Node vChild = vList.item(i);
				if (vChild.getNodeType() == Node.TEXT_NODE) {
					String vValue = vChild.getNodeValue();
					if (vValue != null && vValue.length() > 0) {
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
			if (vValue != null && vValue.length() > 0) {
				vChildNode = new DefaultMutableTreeNode(vValue);
				vChildNode.setAllowsChildren(false);
				pParentNode.add(vChildNode);
			}
		}
	}

	/**
	 * Returns the Label for the node.
	 * 
	 * @param pNode
	 *            the node.
	 * @return String with NodeName + attributes (NodeName + NodeValue).
	 */
	private StringBuffer getNodeName(Node pNode) {
		StringBuffer vResult = new StringBuffer();
		vResult.append(pNode.getNodeName());
		if (pNode.hasAttributes()) {
			int vLastIndex = pNode.getAttributes().getLength() - 1;
			vResult.append(" (");
			/* Attributes */
			for (int k = 0; k < vLastIndex; ++k) {
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
