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

import org.piccolo2d.nodes.PPath;
import org.piccolo2d.nodes.PText;

public class Piccolo2DGraphicWrapper implements GraphicWrapper {

	private PPath mNode;
	private PText mText;

	public Piccolo2DGraphicWrapper(PPath node, PText text) {
		mNode = node;
		mText = text;
	}

	@Override
	public void setPaint(Paint color) {
		this.mNode.setPaint(color);
	}

	@Override
	public Paint getStrokePaint() {
		return mNode.getStrokePaint();
	}

	@Override
	public void setTextPaint(Paint color) {
		mText.setTextPaint(color);
	}

	@Override
	public double getWidth() {
		return mNode.getWidth();
	}

	@Override
	public double getHeight() {
		return mNode.getHeight();
	}

	@Override
	public void setStrokePaint(Color color) {
		this.mNode.setStrokePaint(color);
	}

	@Override
	public void setTransparency(float f) {
		this.mNode.setTransparency(f);
	}

	@Override
	public Point2D getCenter2D() {
		return mNode.getBounds().getCenter2D();
	}

}
