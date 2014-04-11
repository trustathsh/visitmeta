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
 * This file is part of visitmeta visualization, version 0.0.3,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d;




/* Imports ********************************************************************/
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.NodeIdentifier;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeMetadata;
import de.hshannover.f4.trust.visitmeta.datawrapper.Position;
import de.hshannover.f4.trust.visitmeta.graphDrawer.Piccolo2DPanel;
import de.hshannover.f4.trust.visitmeta.gui.GuiController;
import edu.umd.cs.piccolo.activities.PActivity;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.nodes.PComposite;
/* Class **********************************************************************/
/**
 * EventHandler for node in the graph.
 */
public class NodeEventHandler extends PDragEventHandler{
/* Attributes *****************************************************************/
	private static final Logger LOGGER = Logger.getLogger(NodeEventHandler.class);
	GuiController  mController = null;
	Piccolo2DPanel mPanel      = null;
/* Constructors ***************************************************************/
	public NodeEventHandler(GuiController pController, Piccolo2DPanel pPanel) {
		mController = pController;
		mPanel      = pPanel;
	}
/* Methods ********************************************************************/
	@Override
	protected void startDrag(PInputEvent e) {
		LOGGER.trace("Method startDrag(" + e + ") called.");
		super.startDrag(e);
		e.setHandled(true);
		e.getPickedNode().moveToFront();
		mController.hidePropertiesOfNodeNow();
		((Position) e.getPickedNode().getAttribute("position")).setInUse(true);
		((PActivity) e.getPickedNode().getAttribute("activitie")).terminate();
	}
	@Override
	@SuppressWarnings({ "unchecked" })
	protected void drag(PInputEvent e) {
		LOGGER.trace("Method drag(" + e + ") called.");
		super.drag(e);
		PComposite vNode = (PComposite) e.getPickedNode();
		/* Redraw edges */
		ArrayList<PPath> vEdges = (ArrayList<PPath>) vNode.getAttribute("edges");
		for(PPath vEdge : vEdges) {
			mPanel.updateEdge(vEdge);
		}
		/* TODO Redraw the shadow */
		PPath vShadow = (PPath) vNode.getAttribute("glow");
		if(vShadow != null) {
			vShadow.setOffset(vNode.getOffset());
		}
	}
	@Override
	protected void endDrag(PInputEvent e) {
		LOGGER.trace("Method endDrag(" + e + ") called.");
		super.endDrag(e);
		/* Set new position */
		Point2D vPoint = e.getPickedNode().getOffset();
		mController.updateNode(
				(Position) e.getPickedNode().getAttribute("position"),              // Position-Object
				(vPoint.getX() / mPanel.getAreaWidth())  + mPanel.getAreaOffsetX(), // x
				(vPoint.getY() / mPanel.getAreaHeight()) + mPanel.getAreaOffsetY(), // y
				0.0                                                                 // z
		);
		Point  vMouse = MouseInfo.getPointerInfo().getLocation();
		Position vNode  = (Position) e.getPickedNode().getAttribute("position");
		if(vNode instanceof NodeIdentifier) {
			mController.showPropertiesOfNode(((NodeIdentifier) vNode).getIdentifier(), vMouse.x, vMouse.y);
		} else if(vNode instanceof NodeMetadata) {
			mController.showPropertiesOfNode(((NodeMetadata) vNode).getMetadata(), vMouse.x, vMouse.y);
		}
		vNode.setInUse(false);
	}
	@Override
	public void mouseEntered(PInputEvent e) {
		LOGGER.trace("Method mouseEntered(" + e + ") called.");
		super.mouseEntered(e);
		Point  vMouse = MouseInfo.getPointerInfo().getLocation();
		Object vNode  = e.getPickedNode().getAttribute("position");
		if(vNode instanceof NodeIdentifier) {
			mController.showPropertiesOfNode(((NodeIdentifier) vNode).getIdentifier(), vMouse.x, vMouse.y);
		} else if(vNode instanceof NodeMetadata) {
			mController.showPropertiesOfNode(((NodeMetadata) vNode).getMetadata(), vMouse.x, vMouse.y);
		}
	}
	@Override
	public void mouseExited(PInputEvent e) {
		LOGGER.trace("Method mouseExited(" + e + ") called.");
		super.mouseExited(e);
		mController.hidePropertiesOfNode();
	}
}
