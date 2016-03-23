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
 * This file is part of visitmeta-visualization, version 0.6.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2016 Trust@HsH
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

import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.binding.IfmapStrings;
import de.hshannover.f4.trust.ifmapj.identifier.IdentityType;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.graphDrawer.policy.PolicyNode;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.util.DocumentUtils;
import de.hshannover.f4.trust.visitmeta.util.IdentifierHelper;
import de.hshannover.f4.trust.visitmeta.util.IdentifierWrapper;

public class PolicySelectionNodePainter extends SelectionNodePainter {

	private static final Logger LOGGER = Logger.getLogger(PolicySelectionNodePainter.class);

	private static final String XMLNS_POLICY_URL_PREFIX = "@xmlns:policy";

	private static final String XMLNS_REV_META_URL_PREFIX = "@xmlns:rev-meta";

	private static final String XMLNS_POLICY_IDENTIFIER_URL_PREFIX = "xmlns";

	private static final String POLICY_METADATA_URL = "http://www.trust.f4.hs-hannover.de/2015/POLICY/METADATA/1";

	private static final String POLICY_IDENTIFIER_URL = "http://www.trust.f4.hs-hannover.de/2015/POLICY/IDENTIFIER/1";

	private static final String POLICY_REV_METADATA_URL = "http://www.trust.f4.hs-hannover.de/2015/REV/METADATA/1";

	private GraphPanel mPanel;

	private Propable mSelectedNode;

	private boolean mIsValid;

	private Set<GraphicWrapper> mSelectedPolicyParents;

	private Set<GraphicWrapper> mSelectedPolicyChilds;

	public PolicySelectionNodePainter(GraphPanel panel) {
		super(panel);
		mPanel = panel;
	}

	private boolean checkOfPolicyMetadata(Metadata metadata) {
		LOGGER.trace("check of policy metadata");

		for (String key : metadata.getProperties()) {
			if (key.contains(XMLNS_POLICY_URL_PREFIX) || key.contains(XMLNS_REV_META_URL_PREFIX)) {
				String url = metadata.valueFor(key);
				if (POLICY_METADATA_URL.equals(url) || POLICY_REV_METADATA_URL.equals(url)) {
					return true;
				}
				break;
			}
		}
		LOGGER.trace("wrong policy metadata url");
		return false;
	}

	private boolean checkIdentityIdentifier(IdentifierWrapper wrapper) {
		if (wrapper.getTypeName().equals(IfmapStrings.IDENTITY_EL_NAME)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean checkIdentityType(IdentifierWrapper wrapper) {
		String type = wrapper.getValueForXpathExpression("@" + IfmapStrings.IDENTITY_ATTR_TYPE);
		if (IdentityType.other.toString().equals(type)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean checkIdentityExtendetTypeDefinition(IdentifierWrapper wrapper) {
		String otherTypeDefinition = wrapper
				.getValueForXpathExpression("@" + IfmapStrings.IDENTITY_ATTR_OTHER_TYPE_DEF);
		if (IfmapStrings.OTHER_TYPE_EXTENDED_IDENTIFIER.equals(otherTypeDefinition)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean checkPolicyIdentity(IdentifierWrapper wrapper) {
		String name = wrapper.getValueForXpathExpression("@" + IfmapStrings.IDENTITY_ATTR_NAME);

		if (name != null) {
			Document document = DocumentUtils.parseEscapedXmlString(name);

			if (document != null) {
				Element extendetInformation = document.getDocumentElement();
				String url = extendetInformation.getAttribute(XMLNS_POLICY_IDENTIFIER_URL_PREFIX);

				if (POLICY_IDENTIFIER_URL.equals(url)) {
					return true;
				}
			}
		}

		LOGGER.trace("wrong policy idetifer url");
		return false;
	}

	private boolean checkOfPolicyIdentifier(Identifier identifier) {
		LOGGER.trace("check of esukom category identity");
		IdentifierWrapper wrapper = IdentifierHelper.identifier(identifier);

		if (!checkIdentityIdentifier(wrapper)) {
			LOGGER.trace("identifier is not a identity");
			return false;
		}

		if (!checkIdentityType(wrapper)) {
			LOGGER.trace("identity type is no " + IdentityType.other);
			return false;
		}

		if (!checkIdentityExtendetTypeDefinition(wrapper)) {
			LOGGER.trace("identity is not an extendet type");
			return false;
		}

		if (!checkPolicyIdentity(wrapper)) {
			LOGGER.trace("identity is not an policy identifier");
			return false;
		}

		return true;
	}

	private boolean isNewSelectedNode(GraphicWrapper selectedNode) {
		Propable selectedPropable = (Propable) selectedNode.getData();

		if (selectedPropable != null && mSelectedNode != selectedPropable) {
			return true;
		} else {
			return false;
		}
	}

	private void setNewNewSelectedNode(GraphicWrapper selectedNode) {
		Propable selectedPropable = (Propable) selectedNode.getData();

		mSelectedNode = selectedPropable;
		if (checkOfPolicyNode(mSelectedNode)) {
			mIsValid = true;
			mSelectedPolicyChilds = PolicyNode.getAllChilds(selectedNode);
			mSelectedPolicyParents = PolicyNode.getAllParents(selectedNode);
		} else {
			mIsValid = false;
		}
	}

	private boolean checkOfPolicyNode(Propable selectedNode) {
		if (selectedNode instanceof Identifier) {
			return checkOfPolicyIdentifier((Identifier) selectedNode);
		} else if (selectedNode instanceof Metadata) {
			return checkOfPolicyMetadata((Metadata) selectedNode);
		}
		return false;
	}

	@Override
	public void paintMetadataNode(Metadata metadata, GraphicWrapper graphic) {
		GraphicWrapper selectedNode = mPanel.getSelectedNode();

		if(selectedNode == null){
			return;
		}

		// only if selected node a POLICY Identifier or Metadata
		if (isNewSelectedNode(selectedNode)) {
			setNewNewSelectedNode(selectedNode);
		}

		if (!mIsValid) {
			return;
		}

		if (mSelectedPolicyParents.contains(graphic) || mSelectedPolicyChilds.contains(graphic)) {
			graphic.setPaint(mColorSelectedNode);
		}
	}

	@Override
	public void paintIdentifierNode(Identifier identifier, GraphicWrapper graphic) {
		GraphicWrapper selectedNode = mPanel.getSelectedNode();

		if (selectedNode == null) {
			return;
		}

		// only if selected node a POLICY Identifier or Metadata
		if (isNewSelectedNode(selectedNode)) {
			setNewNewSelectedNode(selectedNode);
		}

		if (!mIsValid) {
			return;
		}

		if (mSelectedPolicyParents.contains(graphic) || mSelectedPolicyChilds.contains(graphic)) {
			graphic.setPaint(mColorSelectedNode);
		}

	}

}
