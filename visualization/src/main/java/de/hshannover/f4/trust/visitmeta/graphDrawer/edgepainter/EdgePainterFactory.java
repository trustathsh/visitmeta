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
package de.hshannover.f4.trust.visitmeta.graphDrawer.edgepainter;

import java.util.ArrayList;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;

/**
 * A factory class to get all registered {@link EdgePainter} implementations in their correct order.
 *
 * @author Bastian Hellmann
 *
 */
public class EdgePainterFactory {

	/**
	 * Returns a list of {@link EdgePainter} in the order they are going to be executed.
	 *
	 * @param panel
	 *            a {@link GraphPanel} instance
	 * @return a {@link List} of {@link EdgePainter} in execution order
	 */
	public static List<EdgePainter> getEdgePainter(GraphPanel panel) {
		List<EdgePainter> result = new ArrayList<>();

		result.add(new DefaultEdgePainter(panel));
		result.add(new SearchResultEdgePainter(panel));

		return result;
	}
}
