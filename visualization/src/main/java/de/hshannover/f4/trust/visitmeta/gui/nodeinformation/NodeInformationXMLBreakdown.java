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
package de.hshannover.f4.trust.visitmeta.gui.nodeinformation;

import javax.swing.tree.DefaultMutableTreeNode;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.hshannover.f4.trust.visitmeta.IfmapStrings;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.util.DocumentUtils;
import de.hshannover.f4.trust.visitmeta.util.IdentifierHelper;
import de.hshannover.f4.trust.visitmeta.util.IdentifierWrapper;

/**
 * A class that implements {@link NodeInformationStrategy} and returns a clearly
 * represented (non-strict) XML tree representation of {@link Identifier} and
 * {@link Metadata}.
 *
 * Nodes within the associated {@link Document} are not displayed as they appear
 * in the structure itself, i.e.
 * <ul>
 * <li>attributes are rendered as <i>italic</i> written nodes on the same level
 * as XML elements on that level
 * <li>the value of leaf elements is rendered in a single entry with the node
 * name of their parents, to reduce the amount of entries that need to be
 * expanded to see their contents
 * </ul>
 *
 * @author Bastian Hellmann
 *
 */
public class NodeInformationXMLBreakdown extends NodeInformationStrategy {

	@Override
	protected DefaultMutableTreeNode fillMetadata(Metadata metadata) {
		DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode(
				"<html>" + metadata.getTypeName() + " <i>(metadata)</i></html>");
		Document document = DocumentUtils.parseEscapedXmlString(metadata
				.getRawData());
		Node rootNode = document.getFirstChild();
		handleAttributes(rootNode, rootTreeNode);
		treeWalk(rootNode, 0, rootTreeNode);

		return rootTreeNode;
	}

	/**
	 * Handles the XML attributes of a given {@link Node}.
	 *
	 * All attributes are display with their <i>name</i> and <i>value</i>.
	 * IF-MAP operational attributes (ifmap-publisher-id, ifmap-cardinality and
	 * ifmap-timestamp) and XML namespace attributes are displayed in
	 * <i>italic</i> letters, whereas all other attributes are displayed
	 * <i><b>italic and bold</b></i>.
	 *
	 * @param node
	 *            {@link Node} instance, whose attributes (if existing) shall be
	 *            displayed
	 * @param treeNode
	 *            the {@link DefaultMutableTreeNode} where the attributes will
	 *            be attached to
	 */
	private void handleAttributes(Node node, DefaultMutableTreeNode treeNode) {
		if (node.hasAttributes()) {
			DefaultMutableTreeNode attributeNode;
			NamedNodeMap attributes = node.getAttributes();
			for (int i = 0; i < attributes.getLength(); i++) {
				Node item = attributes.item(i);
				String itemName = item.getNodeName();
				if (IfmapStrings.IFMAP_OPERATIONAL_ATTRIBUTES
						.contains(itemName) || itemName.contains("xmlns")) {
					attributeNode = new DefaultMutableTreeNode("<html><i>"
							+ item.getNodeName() + ":</i> "
							+ item.getNodeValue() + "</html>");
				} else {
					attributeNode = new DefaultMutableTreeNode("<html><b><i>"
							+ item.getNodeName() + ":</i></b> "
							+ item.getNodeValue() + "</html>");
				}
				attributeNode.setAllowsChildren(false);
				treeNode.add(attributeNode);
			}
		}
	}

	/**
	 * Walks the {@link Document} tree and creates
	 * {@link DefaultMutableTreeNode} instances. At each level, the attributes
	 * are handled as well as possible children.
	 *
	 * @param node
	 *            starting {@link Node} for the current level
	 * @param level
	 *            the current level of the tree
	 * @param treeNode
	 *            the parent tree node of the starting node
	 */
	private void treeWalk(Node node, int level, DefaultMutableTreeNode treeNode) {
		Node childNode = null;
		DefaultMutableTreeNode childTreeNode;
		if (node.hasChildNodes()) {
			level++;
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				childNode = childNodes.item(i);

				if (childNode.getNodeType() == Node.TEXT_NODE) {
					String value = childNode.getNodeValue();
					if (value != null && value.length() > 0) {
						String parentTreeNodeText = (String) treeNode
								.getUserObject();
						treeNode.setUserObject("<html><b>" + parentTreeNodeText
								+ "</b>: " + value + "</html>");
						treeNode.setAllowsChildren(false);
					}
				} else {
					childTreeNode = new DefaultMutableTreeNode(
							childNode.getNodeName());
					handleAttributes(childNode, childTreeNode);
					treeNode.add(childTreeNode);
					treeWalk(childNode, level, childTreeNode);
				}
			}
		}
	}

	@Override
	protected DefaultMutableTreeNode fillIdentifier(Identifier identifier) {
		IdentifierWrapper wrapper = IdentifierHelper.identifier(identifier);
		String typeName = wrapper.getTypeName();

		switch (typeName) {
			case IfmapStrings.ACCESS_REQUEST_EL_NAME:
				return createAccessRequestNode(wrapper);
			case IfmapStrings.DEVICE_EL_NAME:
				return createDeviceNode(wrapper);
			case IfmapStrings.IDENTITY_EL_NAME:
				return createIdentityNode(wrapper);
			case IfmapStrings.MAC_ADDRESS_EL_NAME:
				return createMacAddressNode(wrapper);
			case IfmapStrings.IP_ADDRESS_EL_NAME:
				return createIpAddressNode(wrapper);
			default:
				return new DefaultMutableTreeNode("-");
		}
	}

	/**
	 * Returns a {@link DefaultMutableTreeNode} representation of a IF-MAP
	 * <i>access-request</i> {@link Identifier}.
	 *
	 * @param identifier
	 *            {@link IdentifierWrapper} instance (encapsulating a
	 *            {@link Identifier})
	 * @return {@link DefaultMutableTreeNode} representing the
	 *         <i>access-request</i> {@link Identifier}
	 */
	private DefaultMutableTreeNode createAccessRequestNode(
			IdentifierWrapper wrapper) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("<html>"
				+ IfmapStrings.ACCESS_REQUEST_EL_NAME
				+ " <i>(identifier)</i></html>");
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
				"<html><b>name:</b> "
						+ wrapper.getValueForXpathExpressionOrElse("@"
								+ IfmapStrings.ACCESS_REQUEST_ATTR_NAME, "-")
						+ "</html>");
		childNode.setAllowsChildren(false);
		root.add(childNode);
		return root;
	}

	/**
	 * Returns a {@link DefaultMutableTreeNode} representation of a IF-MAP
	 * <i>device</i> {@link Identifier}.
	 *
	 * @param identifier
	 *            {@link IdentifierWrapper} instance (encapsulating a
	 *            {@link Identifier})
	 * @return {@link DefaultMutableTreeNode} representing the <i>device</i>
	 *         {@link Identifier}
	 */
	private DefaultMutableTreeNode createDeviceNode(IdentifierWrapper wrapper) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("<html>"
				+ IfmapStrings.DEVICE_EL_NAME + " <i>(identifier)</i></html>");
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
				"<html><b>name:</b> "
						+ wrapper.getValueForXpathExpressionOrElse(
								IfmapStrings.DEVICE_NAME_EL_NAME, "-")
						+ "</html>");
		childNode.setAllowsChildren(false);
		root.add(childNode);
		return root;
	}

	/**
	 * Returns a {@link DefaultMutableTreeNode} representation of a IF-MAP
	 * <i>identity</i> {@link Identifier}. <i>extended identifiers</i> are
	 * treated in a special way.
	 *
	 * @param identifier
	 *            {@link IdentifierWrapper} instance (encapsulating a
	 *            {@link Identifier})
	 * @return {@link DefaultMutableTreeNode} representing the <i>identity</i>
	 *         {@link Identifier}
	 */
	private DefaultMutableTreeNode createIdentityNode(IdentifierWrapper wrapper) {
		String type = wrapper.getValueForXpathExpressionOrElse("@"
				+ IfmapStrings.IDENTITY_ATTR_TYPE, "-");
		String name = wrapper.getValueForXpathExpressionOrElse("@"
				+ IfmapStrings.IDENTITY_ATTR_NAME, "-");
		String otherTypeDefinition = wrapper.getValueForXpathExpressionOrElse(
				"@" + IfmapStrings.IDENTITY_ATTR_OTHER_TYPE_DEF, "-");

		DefaultMutableTreeNode root;
		DefaultMutableTreeNode childNode;

		if (type.equals("other")) {
			if (otherTypeDefinition.equals("extended")) {
				root = new DefaultMutableTreeNode("<html>"
						+ name.substring(name.indexOf(";") + 1,
								name.indexOf(" "))
						+ " <i>(extended-identifier)</i></html>");

				childNode = handleExtendedIdentifierName(name);
				root.add(childNode);
			} else {
				root = new DefaultMutableTreeNode("<html>" + name
						+ " <i>(extended-identifier)</i></html>");
			}
			childNode = new DefaultMutableTreeNode("<html><b>type:</b> " + type
					+ "</html>");
			childNode.setAllowsChildren(false);
			root.add(childNode);

			childNode = new DefaultMutableTreeNode(
					"<html><b>other-type-definition:</b> "
							+ otherTypeDefinition + "</html>");
			childNode.setAllowsChildren(false);
			root.add(childNode);
		} else {
			root = new DefaultMutableTreeNode("<html>"
					+ IfmapStrings.IDENTITY_EL_NAME
					+ " <i>(identifier)</i></html>");

			childNode = new DefaultMutableTreeNode("<html><b>name:</b> " + name
					+ "</html>");
			childNode.setAllowsChildren(false);
			root.add(childNode);

			childNode = new DefaultMutableTreeNode("<html><b>type:</b> " + type
					+ "</html>");
			childNode.setAllowsChildren(false);
			root.add(childNode);
		}

		return root;
	}

	/**
	 * Handles the content of the <i>name</i> element of extended identifiers.
	 * The encapsulated XML (stored within the <i>name</i> element of the
	 * <i>identity</i> object) is first de-escaped and then parsed as a separate
	 * {@link Document}. It is then rendered as a additional sub-tree within the
	 * overal representation.
	 *
	 * @param extendedIdentifierXML
	 *            the escaped XML representation of the <i>extended
	 *            identifier</i>
	 * @return a {@link DefaultMutableTreeNode} instance, representing the
	 *         {@link Document} structure of the encapsulated XML representation
	 */
	private DefaultMutableTreeNode handleExtendedIdentifierName(
			String extendedIdentifierXML) {
		DefaultMutableTreeNode extendedIdentifierRoot = null;

		Document document = DocumentUtils
				.parseEscapedXmlString(extendedIdentifierXML);
		Node extendedIdentifierNode = document.getFirstChild();

		extendedIdentifierRoot = new DefaultMutableTreeNode(
				"<html><b>extended information</b></html>");

		handleAttributes(extendedIdentifierNode, extendedIdentifierRoot);
		treeWalk(extendedIdentifierNode, 0, extendedIdentifierRoot);

		return extendedIdentifierRoot;
	}

	/**
	 * Returns a {@link DefaultMutableTreeNode} representation of a IF-MAP
	 * <i>mac-address</i> {@link Identifier}.
	 *
	 * @param identifier
	 *            {@link IdentifierWrapper} instance (encapsulating a
	 *            {@link Identifier})
	 * @return {@link DefaultMutableTreeNode} representing the
	 *         <i>mac-address</i> {@link Identifier}
	 */
	private DefaultMutableTreeNode createMacAddressNode(
			IdentifierWrapper wrapper) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("<html>"
				+ IfmapStrings.MAC_ADDRESS_EL_NAME
				+ " <i>(identifier)</i></html>");
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
				"<html><b>value:</b> "
						+ wrapper.getValueForXpathExpressionOrElse("@"
								+ IfmapStrings.MAC_ADDRESS_ATTR_VALUE, "-")
						+ "</html>");
		childNode.setAllowsChildren(false);
		root.add(childNode);
		return root;
	}

	/**
	 * Returns a {@link DefaultMutableTreeNode} representation of a IF-MAP
	 * <i>ip-address</i> {@link Identifier}.
	 *
	 * @param identifier
	 *            {@link IdentifierWrapper} instance (encapsulating a
	 *            {@link Identifier})
	 * @return {@link DefaultMutableTreeNode} representing the <i>ip-address</i>
	 *         {@link Identifier}
	 */
	private DefaultMutableTreeNode createIpAddressNode(IdentifierWrapper wrapper) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("<html>"
				+ IfmapStrings.IP_ADDRESS_EL_NAME
				+ " <i>(identifier)</i></html>");
		DefaultMutableTreeNode childNode;

		childNode = new DefaultMutableTreeNode("<html><b>value:</b> "
				+ wrapper.getValueForXpathExpressionOrElse("@"
						+ IfmapStrings.IP_ADDRESS_ATTR_VALUE, "-") + "</html>");
		childNode.setAllowsChildren(false);
		root.add(childNode);

		childNode = new DefaultMutableTreeNode("<html><b>type:</b> "
				+ wrapper.getValueForXpathExpressionOrElse("@"
						+ IfmapStrings.IP_ADDRESS_ATTR_TYPE, "-") + "</html>");
		childNode.setAllowsChildren(false);
		root.add(childNode);
		return root;
	}
}
