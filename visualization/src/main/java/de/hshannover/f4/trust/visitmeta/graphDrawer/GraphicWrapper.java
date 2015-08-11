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
package de.hshannover.f4.trust.visitmeta.graphDrawer;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.Point2D;

/**
 * Interface to abstract objects from concrete graphical implementations like Piccolo2D.
 *
 * @author Bastian Hellmann
 *
 */
public interface GraphicWrapper {

	/**
	 * Sets the color of a graphical object.
	 *
	 * @param color
	 *            a {@link Paint} object representing the new color.
	 */
	public void setPaint(Paint color);

	/**
	 * Returns the color of the stroke o f a graphical object.
	 *
	 * @return a {@link Paint} object representing the stroke color.
	 */
	public Paint getStrokePaint();

	/**
	 * Sets the color of the stroke of a graphical object.
	 *
	 * @param color
	 *            a {@link Paint} object representing the new stroke color.
	 */
	public void setStrokePaint(Color color);

	/**
	 * Sets the color of the text of a graphical object.
	 *
	 * @param color
	 *            a {@link Paint} object representing the new text color.
	 */
	public void setTextPaint(Paint color);

	/**
	 * Sets the transparency of a graphical object
	 *
	 * @param f
	 *            the transparency value (between 0.0 and 1.0)
	 */
	public void setTransparency(float f);

	/**
	 * Returns the width of a graphical objec.t
	 *
	 * @return the width
	 */
	public double getWidth();

	/**
	 * Returns the height of a graphical object.
	 *
	 * @return the height
	 */
	public double getHeight();

	/**
	 * Returns the {@link Point2D} that marks the center of a graphcial object.
	 *
	 * @return the center of the object.
	 */
	public Point2D getCenter2D();
}
