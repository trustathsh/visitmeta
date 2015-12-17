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
 * This file is part of visitmeta-visualization, version 0.5.2,
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
package de.hshannover.f4.trust.visitmeta.graphDrawer.nodepainter;

import java.util.List;

import org.piccolo2d.nodes.PPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.visitmeta.datawrapper.NodeMetadata;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeType;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.util.DocumentUtils;

public class PolicyFeatureMetadataReference extends MouseOverNodePainter {

	public static final String POLICY_FEATURE_EL_NAME = "policy-feature";

	public static final String ESUKOM_FEATURE_EL_NAME = "feature";

	private GraphicWrapper mMouseOverNode;

	private Element mMouseOverMetadataElement;

	private Element mMouseOverIdentifierElement;

	private boolean mValidMouseOver;

	public PolicyFeatureMetadataReference(GraphPanel panel) {
		super(panel);
	}

	private void setNewMouseOverNode(GraphicWrapper newMouseOverNode) {
		mMouseOverNode = newMouseOverNode;
	}

	private void resetOldMouseOverNode() {
		// only reset valid old MouseOverNodes
		if (mMouseOverNode != null && mValidMouseOver) {
			for (GraphicWrapper edgeNode : mMouseOverNode.getEdgesNodes()) {
				if (edgeNode.getNodeType() == NodeType.METADATA) {
					List<PPath> bothNodeEdges = mMouseOverNode.getEdgesBetween(edgeNode);

					// delete all edges
					for (PPath edge : bothNodeEdges) {
						super.mGraphPanel.deleteEdge(edgeNode.getPosition(), edge);
					}
				}
			}
		}
	}

	private boolean isNewMouseOverNode(GraphicWrapper mouseOverNode) {
		Object mouseOverNodeData = mouseOverNode.getData();

		if (mouseOverNodeData instanceof Propable) {
			if (mMouseOverNode == null) {
				return true;
			} else if (mMouseOverNode.getData() != mouseOverNodeData) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private void setIsValideMouseOver(Propable mouseOverNode) {
		if (mouseOverNode instanceof Metadata && mouseOverNode.getTypeName().equals(POLICY_FEATURE_EL_NAME)) {
			setMouseOverElements(mouseOverNode);
			mValidMouseOver = true;
		} else {
			mValidMouseOver = false;
		}
	}

	private void setMouseOverElements(Propable mouseOverNode) {
		Document mouseOverNodeDocument = DocumentUtils.parseEscapedXmlString(mouseOverNode.getRawData());
		mMouseOverMetadataElement = (Element) mouseOverNodeDocument.getDocumentElement().getFirstChild()
				.getFirstChild();
		mMouseOverIdentifierElement = (Element) mMouseOverMetadataElement.getNextSibling().getFirstChild();
	}

	@Override
	public void paintMetadataNode(Metadata metadata, GraphicWrapper graphic) {
		GraphicWrapper mouseOverNode = mGraphPanel.getMouseOverNode();

		if (mouseOverNode == null) {
			resetOldMouseOverNode();
			setNewMouseOverNode(mouseOverNode);
			return;
		}

		// only if mouse over node a POLICY FEATURE
		if (isNewMouseOverNode(mouseOverNode)) {
			resetOldMouseOverNode();
			setNewMouseOverNode(mouseOverNode);
			setIsValideMouseOver((Propable) mouseOverNode.getData());
		}

		if (!mValidMouseOver) {
			return;
		}

		// only for ESUKOM FEATURE metadata
		if (!metadata.getTypeName().equals(ESUKOM_FEATURE_EL_NAME)) {
			return;
		}

		Document paintMetadataDocument = DocumentUtils.parseEscapedXmlString(metadata.getRawData());
		Element revMetadataElement = paintMetadataDocument.getDocumentElement();

		if (revMetadataElement.isEqualNode(mMouseOverMetadataElement)) {
			List<GraphicWrapper> edges = graphic.getEdgesNodes();
			// ESUKOM_FEATURE
			for (GraphicWrapper edge : edges) {
				Object data = edge.getData();
				if (data instanceof Identifier) {
					Identifier identifier = (Identifier) data;
					Document revIdentifierDocumen = DocumentUtils.parseEscapedXmlString(identifier.getRawData());
					Element revIdentifierElement = revIdentifierDocumen.getDocumentElement();

					if (revIdentifierElement.isEqualNode(mMouseOverIdentifierElement)) {
						// set mouseOverColor
						graphic.setPaint(mColorMouseOverNode);

						// add edge
						super.mGraphPanel.addEdge((NodeMetadata) graphic.getPosition(), graphic.getPosition(),
								mouseOverNode.getPosition());
					}
				}
			}
		}
	}

	@Override
	public void paintIdentifierNode(Identifier identifier, GraphicWrapper graphic) {
		// nothing ToDo only for metadata
	}

}
