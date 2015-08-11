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

import java.awt.Color;
import java.awt.Paint;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.gui.search.SearchAndFilterStrategy;
import de.hshannover.f4.trust.visitmeta.gui.search.Searchable;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

/**
 * A {@link NodePainter} implementation that paints a node in a specific color if it was in the result of a search.
 * Also paints all non-matching nodes transparent, if this function is activated.
 *
 * @author Bastian Hellmann
 *
 */
public class SearchResultNodePainter implements NodePainter {

	private static final Properties mConfig = Main.getConfig();

	private Paint mColorContainsSearchTermNode = null;

	private float mHideSearchMismatchesTransparency;

	private Searchable mSearchable;

	public SearchResultNodePainter(GraphPanel panel) {
		if (panel instanceof Searchable) {
			mSearchable = (Searchable) panel;
		}

		String vColormContainsSearchTermNode = mConfig.getString(
				VisualizationConfig.KEY_COLOR_NODE_SEARCH, VisualizationConfig.DEFAULT_VALUE_COLOR_NODE_SEARCH);
		mColorContainsSearchTermNode = Color.decode(vColormContainsSearchTermNode);

		mHideSearchMismatchesTransparency = (float) mConfig.getDouble(
				"visualization.searchandfilter.transparency", 0.2);
	}

	@Override
	public void paintMetadataNode(Metadata metadata, GraphicWrapper graphic) {
		if (mSearchable != null) {
			SearchAndFilterStrategy strategy = mSearchable.getSearchAndFilterStrategy();
			boolean containsSearchTerm = false;
			if (strategy != null) {
				containsSearchTerm = strategy.containsSearchTerm(metadata, mSearchable.getSearchTerm());
			}

			boolean hideSearchMismatches = mSearchable.getHideSearchMismatches();

			/**
			 * If the metadata node matches the search term,
			 * paint it accordingly. Reduce the transparency
			 * of all non-matching nodes if the user set the
			 * checkbox.
			 */
			graphic.setTransparency(1.0f);
			if (containsSearchTerm) {
				graphic.setPaint(mColorContainsSearchTermNode);
			} else if (hideSearchMismatches) {
				graphic.setTransparency(mHideSearchMismatchesTransparency);
			}
		}
	}

	@Override
	public void paintIdentifierNode(Identifier identifier, GraphicWrapper graphic) {
		if (mSearchable != null) {
			SearchAndFilterStrategy strategy = mSearchable.getSearchAndFilterStrategy();
			boolean containsSearchTerm = false;
			if (strategy != null) {
				containsSearchTerm = strategy.containsSearchTerm(identifier, mSearchable.getSearchTerm());
			}

			boolean hideSearchMismatches = mSearchable.getHideSearchMismatches();

			/**
			 * If the identifier node matches the search term,
			 * paint it accordingly. Reduce the transparency
			 * of all non-matching nodes if the user set the
			 * checkbox.
			 */
			graphic.setTransparency(1.0f);
			if (containsSearchTerm) {
				graphic.setPaint(mColorContainsSearchTermNode);
			} else if (hideSearchMismatches) {
				graphic.setTransparency(mHideSearchMismatchesTransparency);
			}
		}
	}

}
