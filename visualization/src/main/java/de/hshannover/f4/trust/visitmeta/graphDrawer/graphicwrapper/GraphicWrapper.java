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
package de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.piccolo2d.nodes.PPath;

import de.hshannover.f4.trust.visitmeta.datawrapper.NodeIdentifier;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeMetadata;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeType;
import de.hshannover.f4.trust.visitmeta.datawrapper.Position;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;

/**
 * Interface to abstract objects from concrete graphical implementations like Piccolo2D.
 *
 * @author Bastian Hellmann
 * @author Marcel Reichenbach
 *
 */
public interface GraphicWrapper {

	/**
	 * Sets the color of a graphical object.
	 *
	 * @param color a {@link Paint} object representing the new color.
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
	 * @param color a {@link Paint} object representing the new stroke color.
	 */
	public void setStrokePaint(Color color);

	/**
	 * Sets the color of the text of a graphical object.
	 *
	 * @param color a {@link Paint} object representing the new text color.
	 */
	public void setTextPaint(Paint color);

	/**
	 * Sets the transparency of a graphical object
	 *
	 * @param f the transparency value (between 0.0 and 1.0)
	 */
	public void setTransparency(float f);

	/**
	 * Returns the width of a graphical object.
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

	/**
	 * Returns the {@link NodeType} of a graphical object.
	 * 
	 * @return if doesn't contain {@link NodeType} return null
	 */
	public NodeType getNodeType();

	/**
	 * Returns the {@link Position} of a graphical object.
	 * 
	 * @return e.g. {@link NodeMetadata} or {@link NodeIdentifier} if doesn't contain {@link Position} return null
	 */
	public Position getPosition();

	/**
	 * Returns the data of a graphical object.
	 * 
	 * @return e.g. {@link Metadata} or {@link Identifier} if doesn't contain {@link Propable} return null
	 */
	public Propable getData();

	/**
	 * Returns the {@link Propable}-typeName of a graphical object.
	 * 
	 * @return if doesn't contain {@link Propable} return null
	 */
	public String getNodeTypeName();

	/**
	 * Returns the Publisher-{@link String} of a graphical object.
	 * 
	 * @return Publisher-{@link String} if doesn't contain a Publisher-{@link String} return null
	 */
	public String getPublisher();

	/**
	 * Returns all nodes of this graphical object.
	 * 
	 * @return if doesn't contains nodes return null
	 */
	public List<GraphicWrapper> getNodes();

	/**
	 * Returns all edges of this graphical object.
	 * TODO remove reference to Piccolo2D PPath
	 * 
	 * @return {@link PPath}-edges if doesn't contains edges return null
	 */
	public ArrayList<PPath> getEdges();

	/**
	 * Returns all edges between this and the otherNode.
	 * TODO remove reference to Piccolo2D PPath
	 * 
	 * @param otherNode must contains edges from this node
	 * @return If doesn't contains edges return null.
	 */
	public List<PPath> getEdgesBetween(GraphicWrapper otherNode);

	/**
	 * Returns all graphical objects from all edges of this graphical object. Without himself and duplicate!
	 * 
	 * @return {@link GraphicWrapper}-List. If doesn't contains edges or other nodes return empty list.
	 */
	public List<GraphicWrapper> getEdgesNodes();

	public String getTimeStampDefaultFormat();

	public String getTimeStamp(SimpleDateFormat dateFormat);

	public long getTimeStamp();

	public int getTimeStampFraction();

}
