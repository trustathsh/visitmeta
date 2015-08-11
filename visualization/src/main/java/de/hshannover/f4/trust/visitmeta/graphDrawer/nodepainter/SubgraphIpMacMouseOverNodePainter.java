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
package de.hshannover.f4.trust.visitmeta.graphDrawer.nodepainter;

import java.util.Arrays;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.IfmapStrings;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;

/**
 * A {@link NodePainter} implementation that paints all node that are hovered over by the user with the mouse pointer,
 * and all nodes that included in a list of typenames.
 * As an example, this class paints IP address and MAC address identifier and ip-mac medadata objects, when one of them
 * is hovered over.
 *
 * @author Bastian Hellmann
 *
 */
public class SubgraphIpMacMouseOverNodePainter extends MouseOverNodePainter {

	private List<String> mConnectedNodeTypeNames =
			Arrays.asList(new String[] {IfmapStrings.IP_ADDRESS_EL_NAME, IfmapStrings.MAC_ADDRESS_EL_NAME, "ip-mac"});

	public SubgraphIpMacMouseOverNodePainter(GraphPanel panel) {
		super(panel);
	}

	@Override
	public void paintMetadataNode(Metadata metadata, GraphicWrapper graphic) {
		Propable mouseOverNode = mGraphPanel.getMouseOverNode();
		if (mouseOverNode != null) {
			boolean isMouseOver = isMouseOver(mouseOverNode, metadata);
			boolean isConnected = isConnected(mouseOverNode, metadata);
			if (isMouseOver
					|| isConnected) {
				graphic.setPaint(mColorMouseOverNode);
			}
		}
	}

	@Override
	public void paintIdentifierNode(Identifier identifier, GraphicWrapper graphic) {
		Propable mouseOverNode = mGraphPanel.getMouseOverNode();
		if (mouseOverNode != null) {
			boolean isMouseOver = isMouseOver(mouseOverNode, identifier);
			boolean isConnected = isConnected(mouseOverNode, identifier);
			if (isMouseOver
					|| isConnected) {
				graphic.setPaint(mColorMouseOverNode);
			}
		}
	}

	private boolean isConnected(Propable mouseOverNode, Propable toTest) {
		String mouseOverTypeName = mouseOverNode.getTypeName();
		String toTestTypeName = toTest.getTypeName();

		if (mConnectedNodeTypeNames.contains(toTestTypeName)
				&& mConnectedNodeTypeNames.contains(mouseOverTypeName)) {
			return true;
		} else {
			return false;
		}
	}

}
