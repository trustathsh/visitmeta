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
package de.hshannover.f4.trust.visitmeta.graphDrawer.noderenderer;

import java.awt.Color;
import java.awt.Paint;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeIdentifier;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeMetadata;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

public class SelectionNodeRenderer implements NodeRenderer {

	private static final Properties mConfig = Main.getConfig();
	private GraphPanel mPanel;

	private Paint mColorSelectedNode = null;

	public SelectionNodeRenderer(GraphPanel panel) {
		mPanel = panel;

		String vColorSelectedNode = mConfig.getString(VisualizationConfig.KEY_COLOR_NODE_SELECTED,
				VisualizationConfig.DEFAULT_VALUE_COLOR_NODE_SELECTED);

		mColorSelectedNode = Color.decode(vColorSelectedNode);
	}

	@Override
	public void paintMetadataNode(Metadata metadata, NodeMetadata m, GraphicWrapper graphic) {
		Propable selectedNode = mPanel.getSelectedNode();
		boolean isSelected = isSelected(selectedNode, metadata);
		if (isSelected) {
			graphic.setPaint(mColorSelectedNode);
		}

	}

	@Override
	public void paintIdentifierNode(Identifier identifier, NodeIdentifier i, GraphicWrapper graphic) {
		Propable selectedNode = mPanel.getSelectedNode();
		boolean isSelected = isSelected(selectedNode, identifier);
		if (isSelected) {
			graphic.setPaint(mColorSelectedNode);
		}
	}

	private boolean isSelected(Propable selectedNode, Propable toTest) {
		if (selectedNode == null) {
			return false;
		} else {
			if (selectedNode == toTest) {
				return true;
			} else {
				return false;
			}
		}
	}
}
