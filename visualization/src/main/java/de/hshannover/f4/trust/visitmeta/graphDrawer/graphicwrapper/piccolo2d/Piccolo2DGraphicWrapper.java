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
package de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.piccolo2d;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.piccolo2d.extras.nodes.PComposite;
import org.piccolo2d.nodes.PPath;
import org.piccolo2d.nodes.PText;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.visitmeta.IfmapStrings;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeType;
import de.hshannover.f4.trust.visitmeta.datawrapper.Position;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.util.DocumentUtils;

public class Piccolo2DGraphicWrapper implements GraphicWrapper {

	protected PPath mNode;

	protected PText mText;

	protected Propable mPropable;

	public Piccolo2DGraphicWrapper(PPath path, PText text) {
		mNode = path;
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

	@Override
	public NodeType getNodeType() {
		return (NodeType) mNode.getParent().getAttribute("type");
	}

	@Override
	public Position getPosition() {
		return (Position) mNode.getParent().getAttribute("position");
	}

	@Override
	public Propable getData() {
		return (Propable) mNode.getParent().getAttribute("data");
	}

	@Override
	public String getNodeTypeName() {
		return getData().getTypeName();
	}

	@Override
	public String getTimeStampDefaultFormat() {
		String timeStamp = getTimeStamp(new SimpleDateFormat("d.MM.y HH:mm:ss", Locale.GERMANY));
		String timeStampFraction = String.valueOf(getTimeStampFraction());
		StringBuffer sb = new StringBuffer();

		sb.append(timeStamp);
		sb.append(':');
		sb.append(timeStampFraction);

		return sb.toString();
	}

	@Override
	public String getTimeStamp(SimpleDateFormat dateFormat) {
		long timeStamp = getTimeStamp();
		if (dateFormat != null && timeStamp >= 0) {
			return dateFormat.format(timeStamp);
		}
		return null;
	}

	@Override
	public long getTimeStamp() {
		Object data = getData();
		if (data instanceof Metadata) {
			Metadata metadata = (Metadata) data;
			long timeStamp = metadata.getPublishTimestamp();
			return timeStamp;
		}
		return -1;
	}

	@Override
	public int getTimeStampFraction() {
		Element rootElement = getRootElement();
		if (rootElement.hasAttributes() && rootElement.hasAttribute(IfmapStrings.TIMESTAMP_FRACTION_ATTR)) {
			String timeStampFraction = rootElement.getAttribute(IfmapStrings.TIMESTAMP_FRACTION_ATTR);
			timeStampFraction = timeStampFraction.substring(2, timeStampFraction.length());
			return Integer.parseInt(timeStampFraction);
		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<PPath> getEdges() {
		return (ArrayList<PPath>) mNode.getParent().getAttribute("edges");
	}

	@Override
	public String getPublisher() {
		return (String) mNode.getParent().getAttribute("publisher");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GraphicWrapper> getNodes() {
		ArrayList<PComposite> nodes = (ArrayList<PComposite>) mNode.getAttribute("nodes");
		List<GraphicWrapper> edgesNodes = new ArrayList<GraphicWrapper>();
		if (nodes != null) {
			for (PComposite vCom : nodes) {
				PPath vNode = (PPath) vCom.getChild(0);
				PText vText = (PText) vCom.getChild(1);

				GraphicWrapper g = Piccolo2DGraphicWrapperFactory.create(vNode, vText);
				edgesNodes.add(g);
			}
		}
		return edgesNodes;
	}

	@Override
	public List<GraphicWrapper> getEdgesNodes() {
		List<GraphicWrapper> edgesNodes = new ArrayList<GraphicWrapper>();
		for (PComposite vCom : getEdgesCompositeNodes()) {
			PPath vNode = (PPath) vCom.getChild(0);
			PText vText = (PText) vCom.getChild(1);
			if (vNode != mNode) {
				GraphicWrapper g = Piccolo2DGraphicWrapperFactory.create(vNode, vText);
				edgesNodes.add(g);
			}
		}
		return edgesNodes;
	}

	@Override
	public List<PPath> getEdgesBetween(GraphicWrapper otherNode) {
		List<PPath> foundetedges = new ArrayList<PPath>();
		ArrayList<PPath> edges = getEdges();
		if (edges != null) {
			for (PPath edge : edges) {
				// find all edges between otherNode
				List<PPath> otherNodeEdges = otherNode.getEdges();
				if (otherNodeEdges.contains(edge)) {
					// edge contains in both nodes
					foundetedges.add(edge);
				}
			}
		}
		return foundetedges;
	}

	@SuppressWarnings("unchecked")
	private Set<PComposite> getEdgesCompositeNodes() {
		Set<PComposite> edgesNodes = new HashSet<PComposite>();
		ArrayList<PPath> edges = getEdges();
		if (edges != null) {
			for (PPath edge : edges) {
				ArrayList<PComposite> edgeNodes = (ArrayList<PComposite>) edge.getAttribute("nodes");
				edgesNodes.addAll(edgeNodes);
			}
		}
		return edgesNodes;
	}

	protected Document getDocument() {
		Object data = getData();

		if (data instanceof Propable) {
			Propable propable = (Propable) data;

			return DocumentUtils.parseEscapedXmlString(propable.getRawData());
		}
		return null;
	}

	protected Element getRootElement() {
		return getDocument().getDocumentElement();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}

		if (o == this) {
			return true;
		}

		if (!(o instanceof Piccolo2DGraphicWrapper)) {
			return false;
		}

		if (mNode != ((Piccolo2DGraphicWrapper) o).mNode) {
			return false;
		}

		if (mText != ((Piccolo2DGraphicWrapper) o).mText) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + mNode.hashCode();
		if (mText != null) {
			result = prime * result + mText.hashCode();
		}
		return result;
	}
}
