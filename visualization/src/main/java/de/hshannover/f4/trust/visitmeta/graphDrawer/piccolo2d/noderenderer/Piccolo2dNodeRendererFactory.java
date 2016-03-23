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
package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.noderenderer;

/**
 * Factory class that creates {@link Piccolo2dNodeRenderer} based on a given {@link Piccolo2dNodeRendererType}.
 *
 * @author Bastian Hellmann
 *
 */
public class Piccolo2dNodeRendererFactory {

	/**
	 * Creates a new {@link Piccolo2dNodeRenderer}.
	 *
	 * @param type
	 *            type of the {@link Piccolo2dNodeRenderer}
	 * @return a new {@link Piccolo2dNodeRenderer}
	 */
	public static Piccolo2dNodeRenderer create(Piccolo2dNodeRendererType type) {
		switch (type) {
			case RECTANGLES_WITH_ROUNDED_CORNERS:
				return new RectanglesWithRoundedCornersPiccolo2dNodeRenderer();
			case RECTANGLES_WITH_RECTANGULAR_CORNERS:
				return new RectanglesWithRectangularCornersPiccolo2dNodeRenderer();
			case ELLIPSE:
				return new EllipsePiccolo2dNodeRenderer();
			case EXAMPLE:
				return new ExamplePiccolo2dNodeRenderer();
			default:
				throw new IllegalArgumentException("No Piccolo2dRenderer found for type '"
						+ type + "'");
		}
	}

}
