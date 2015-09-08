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
package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.edgerenderer;

import java.awt.BasicStroke;
import java.awt.geom.Point2D;

import org.piccolo2d.nodes.PPath;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * {@link Piccolo2dEdgeRenderer} that draws edges as straight dashed lines.
 *
 * @author Bastian Hellmann
 *
 */
public class StraightDashedLinePiccolo2dEdgeRenderer implements Piccolo2dEdgeRenderer {

	@Override
	public void drawEdge(PPath pEdge, Point2D vStart, Point2D vEnd, Metadata metadata,
			Identifier identifier) {
		drawStraightDashedLine(pEdge, vStart, vEnd);
	}

	/**
	 * Draws a straight dashed line.
	 *
	 * @param pEdge
	 *            the {@link PPath} object representing the edge
	 * @param vStart
	 *            the starting point of the edge
	 * @param vEnd
	 *            the end point of the edge
	 */
	public static void drawStraightDashedLine(PPath pEdge, Point2D vStart, Point2D vEnd) {
		float[] dash = {10.0f};
		pEdge.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
		pEdge.moveTo((float) vStart.getX(), (float) vStart.getY());
		pEdge.lineTo((float) vEnd.getX(), (float) vEnd.getY());
	}

}
