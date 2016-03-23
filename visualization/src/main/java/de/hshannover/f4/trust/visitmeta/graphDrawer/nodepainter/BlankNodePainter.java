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

import java.awt.Color;
import java.awt.Paint;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

public class BlankNodePainter implements NodePainter {

	private static final Properties mConfig = Main.getConfig();

	private Paint mColorBlankNode = null;
	private Paint mColorText;
	private Color mColorStroke;

	public BlankNodePainter(GraphPanel panel) {
		String vColorSelectedNode = mConfig.getString(VisualizationConfig.KEY_COLOR_NODE_BLANK,
				VisualizationConfig.DEFAULT_VALUE_COLOR_NODE_BLANK);
		String vColorText = VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_TEXT;
		String vColorStroke = VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_BORDER;
		
		mColorBlankNode = Color.decode(vColorSelectedNode);
		mColorText = Color.decode(vColorText);
		mColorStroke = Color.decode(vColorStroke);
	}

	@Override
	public void paintMetadataNode(Metadata metadata, GraphicWrapper graphic) {
		graphic.setPaint(mColorBlankNode);
		graphic.setTextPaint(mColorText);
		graphic.setStrokePaint(mColorStroke);
	}

	@Override
	public void paintIdentifierNode(Identifier identifier, GraphicWrapper graphic) {
		graphic.setPaint(mColorBlankNode);
		graphic.setTextPaint(mColorText);
		graphic.setStrokePaint(mColorStroke);
	}

}
