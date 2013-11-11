package de.fhhannover.inform.trust.visitmeta.gui;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
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
 * This file is part of VisITMeta, version 0.0.2, implemented by the Trust@FHH 
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@FHH
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
/* Imports ********************************************************************/
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Timer;

import org.apache.commons.collections15.map.HashedMap;
import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.visitmeta.datawrapper.ExpandedLink;
import de.fhhannover.inform.trust.visitmeta.datawrapper.NodeIdentifier;
import de.fhhannover.inform.trust.visitmeta.datawrapper.NodeMetadata;
import de.fhhannover.inform.trust.visitmeta.datawrapper.Position;
import de.fhhannover.inform.trust.visitmeta.datawrapper.SettingManager;
import de.fhhannover.inform.trust.visitmeta.datawrapper.TimeManagerCreation;
import de.fhhannover.inform.trust.visitmeta.datawrapper.TimeManagerDeletion;
import de.fhhannover.inform.trust.visitmeta.datawrapper.TimeSelector;
import de.fhhannover.inform.trust.visitmeta.datawrapper.UpdateContainer;
import de.fhhannover.inform.trust.visitmeta.graphCalculator.FacadeLogic;
import de.fhhannover.inform.trust.visitmeta.graphDrawer.GraphPanel;
import de.fhhannover.inform.trust.visitmeta.graphDrawer.GraphPanelFactory;
import de.fhhannover.inform.trust.visitmeta.interfaces.Propable;
/* Class **********************************************************************/
/**
 * Controller for interaction with user.
 */
public class GuiController implements Observer {
/* Attributes *****************************************************************/
	private static final Logger LOGGER = Logger.getLogger(GuiController.class);
	private FacadeLogic          mFacadeLogic           = null;
	private GraphPanel           mGraphPanel            = null;
	private VisITMetaWindow      mMainWindow            = null;
	private WindowNodeProperties mWindowNodeProperties  = null;
	private WindowColorSettings  mWindowColorSettings   = null;
	private WindowSettings       mWindowSettings        = null;
	private SettingManager       mSettingManager        = null;
	private TimeSelector         mTimeSelector          = null;
	private Timer                mTimerPropertiesShow   = null;
	private Timer                mTimerPropertiesHide   = null;
	private TimeManagerCreation  mTimerCreation         = null;
	private TimeManagerDeletion  mTimerDeletion         = null;
	private boolean              mAddHighlights         = true;
	private HashedMap<Observable, Observable> mObservables = new HashedMap<>();
/* Constructors ***************************************************************/
	public GuiController(FacadeLogic pLogic) {
		mFacadeLogic    = pLogic;
		mTimeSelector   = TimeSelector.getInstance();
		mSettingManager = SettingManager.getInstance();
		/* Main-Window for VisITMeta */
		mGraphPanel          = GraphPanelFactory.getGraphPanel("Piccolo2D", this);
		mMainWindow          = new VisITMetaWindow(this, mGraphPanel.getPanel());
		/* Windows for Settings */
		mWindowSettings = new WindowSettings(this);
		mWindowSettings.setAlwaysOnTop(true);
		mWindowSettings.setVisible(false);
		mWindowColorSettings = new WindowColorSettings(this);
		mWindowColorSettings.setAlwaysOnTop(true);
		mWindowColorSettings.setVisible(false);
		/* Window for Properties of the Nodes */
		mWindowNodeProperties = new WindowNodeProperties(mMainWindow, this);
		mTimerPropertiesShow  = new Timer(750, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				LOGGER.trace("ActionListener actionPerformed(" + pE +") called.");
				LOGGER.debug("Stop timer and hide WindowNodeProperties.");
				mTimerPropertiesShow.stop();
				mWindowNodeProperties.setVisible(true);
			}
		});
		mTimerPropertiesHide = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				LOGGER.trace("ActionListener actionPerformed(" + pE +") called.");
				LOGGER.debug("Stop timer and hide WindowNodeProperties.");
				mTimerPropertiesHide.stop();
				mWindowNodeProperties.setVisible(false);
			}
		});
		/* Time manager for the nodes in the graph */
		mTimerCreation = TimeManagerCreation.getInstance();
		mTimerDeletion = TimeManagerDeletion.getInstance();
		mTimerCreation.setController(this);
		mTimerDeletion.setController(this);
		/* Add as observer */
		mSettingManager.addObserver(this);
		mFacadeLogic.addObserver(this);
		mTimeSelector.addObserver(this);
	}
/* Methods ********************************************************************/
	/**
	 * Pack and set visible.
	 */
	public void show() {
		LOGGER.trace("Method show() called.");
		mMainWindow.pack();
		mMainWindow.setVisible(true);
	}
	/**
	 * Add the identifier in the list to the graph.
	 * @param pIdentifier the list with identifier.
	 */
	private synchronized void addIdentifier(List<NodeIdentifier> pIdentifier) {
		LOGGER.trace("Method addIdentifier(" + pIdentifier + ") called.");
		for(NodeIdentifier vIdentifier : pIdentifier) {
			mGraphPanel.addIdentifier(vIdentifier); // Add identifier to panel
			if(mAddHighlights) {
				mGraphPanel.markAsNew(vIdentifier);
				mTimerCreation.addNode(vIdentifier);
			}
			vIdentifier.addObserver(this);          // Set GuiController as Observer
			mObservables.put(vIdentifier, vIdentifier);
		}
	}
	/**
	 * Add the metadata of the list and create the edges to the identifier in the graph.
	 * @param pIdentifier the identifier.
	 * @param pMetadata the list with metadata.
	 */
	private synchronized void addMetadata(NodeIdentifier pIdentifier, List<NodeMetadata> pMetadata) {
		LOGGER.trace("Method addMetadata(" + pIdentifier + ", " + pMetadata + ") called.");
		for(NodeMetadata vMetadata : pMetadata) {
			mGraphPanel.addMetadata(pIdentifier, vMetadata);
			if(mAddHighlights) {
				mGraphPanel.markAsNew(vMetadata);
				mTimerCreation.addNode(vMetadata);
			}
			vMetadata.addObserver(this);
			mObservables.put(vMetadata, vMetadata);
		}
	}
	/**
	 * Add the metadata of the list and create the edges of the link in the graph.
	 * @param pLink the link.
	 * @param pMetadata the list with metadata.
	 */
	private synchronized void addMetadata(ExpandedLink pLink, List<NodeMetadata> pMetadata) {
		LOGGER.trace("Method addMetadata(" + pLink + ", " + pMetadata + ") called.");
		for(NodeMetadata vMetadata : pMetadata) {
			mGraphPanel.addMetadata(pLink, vMetadata);
			if(mAddHighlights) {
				mGraphPanel.markAsNew(vMetadata);
				mTimerCreation.addNode(vMetadata);
			}
			vMetadata.addObserver(this);
			mObservables.put(vMetadata, vMetadata);
		}
	}
	/**
	 * Remove the IdentifierNode.
	 * @param pIdentifier the NodeIdentifier to identify the node in the graph.
	 */
	private synchronized void deleteIdentifier(List<NodeIdentifier> pIdentifier) {
		LOGGER.trace("Method deleteIdentifier(" + pIdentifier + ") called.");
		if(mAddHighlights) {
			for (NodeIdentifier vIdentifier : pIdentifier) {
				mGraphPanel.markAsDelete(vIdentifier);
				mTimerDeletion.addNode(vIdentifier);
			}
		}
	}
	/**
	 * Delete the MetadataNode and the edges to the node from the graph.
	 * @param pMetadata the NodeMetadata to identify the node and edges in the graph.
	 */
	private synchronized void deleteMetadata(List<NodeMetadata> pMetadata) {
		LOGGER.trace("Method deleteMetadata(" + pMetadata + ") called.");
		if (mAddHighlights) {
			for (NodeMetadata vMetadata : pMetadata) {
				mGraphPanel.markAsDelete(vMetadata);
				mTimerDeletion.addNode(vMetadata);
			}
		}
	}
	public synchronized void deleteNode(Position pNode) {
		LOGGER.trace("Method deleteNode(" + pNode + ") called.");
		mGraphPanel.deleteNode(pNode);
		pNode.deleteObserver(this);
		mObservables.remove(pNode);
	}
	public synchronized void removeHighlight(Position pNode) {
		LOGGER.trace("Method removeHighlight(" + pNode + ") called.");
		mGraphPanel.clearHighlight(pNode);
	}
	/**
	 * Set the new position for a Position-Object.
	 * @param pNode the Object.
	 * @param pNewX the new x coordinate.
	 * @param pNewY the new y coordinate.
	 * @param pNewZ the new z coordinate.
	 */
	public void updateNode(Position pNode, double pNewX, double pNewY, double pNewZ) {
		LOGGER.trace(
				"Method updateNode(" +
				pNode + ", " +
				pNewX + ", " +
				pNewY + ", " +
				pNewZ + ") called."
		);
		mFacadeLogic.updateNode(pNode, pNewX, pNewY, pNewZ);
	}
	/**
	 * Set the color settings window visible.
	 */
	public void showColorSettings() {
		LOGGER.trace("Method showColorSettings() called.");
		mWindowColorSettings.updateWindow();
		mWindowColorSettings.setLocationRelativeTo(mMainWindow);
		mWindowColorSettings.setVisible(true);
	}
	/**
	 * Set the settings window visible.
	 */
	public void showSettings() {
		LOGGER.trace("Method showSettings() called.");
		mWindowSettings.setLocationRelativeTo(mMainWindow);
		mWindowSettings.setVisible(true);
	}
	/**
	 * Get a list of all known publisher.
	 */
	public List<String> getPublisher() {
		LOGGER.trace("Method getPublisher() called.");
		return mGraphPanel.getPublisher();
	}
	/**
	 * Repaint nodes of a specific type and publisher.
	 * @param pPublisher the id of the current publisher, empty if the default color is changed.
	 * @param pType the type of the node,
	 */
	public void repaintNodes(String pType, String pPublisher) {
		LOGGER.trace("Method repaintMetadata(" + pPublisher + ") called.");
		mGraphPanel.repaintNodes(pType, pPublisher);
	}
	/**
	 * Open a window with the properties of the node
	 * @param pData the properties of the node.
	 * @param pX the x coordinate of the window.
	 * @param pY the y coordinate of the window.
	 */
	public void showPropertiesOfNode(final Propable pData, final int pX, final int pY) {
		LOGGER.trace("Method showPropertiesOfNode(" + pData + ", " + pX + ", " + pY + ") called.");
		mTimerPropertiesHide.stop();
		mWindowNodeProperties.fill(pData.getRawData(), mTimerPropertiesHide);
		mWindowNodeProperties.repaint();
		mWindowNodeProperties.setLocation(pX + 1, pY + 1);
		mTimerPropertiesShow.start();
	}
	/**
	 * Hide the property window after a period of time.
	 */
	public void hidePropertiesOfNode() {
		LOGGER.trace("Method hidePropertiesOfNode() called.");
		mTimerPropertiesShow.stop();
		mTimerPropertiesHide.start();
	}
	/**
	 * Hide the property window.
	 */
	public void hidePropertiesOfNodeNow() {
		LOGGER.trace("Method hidePropertiesOfNodeNow() called.");
		mTimerPropertiesShow.stop();
		mTimerPropertiesHide.stop();
		mWindowNodeProperties.setVisible(false);
	}
	/**
	 * Stop the calculation of node positions.
	 */
	public void stopGraphMotion() {
		LOGGER.trace("Method stopGraphMotion() called.");
		mFacadeLogic.stopCalculation();
	}
	/**
	 * Resume the calculation of node positions.
	 */
	public void startGraphMotion() {
		LOGGER.trace("Method startGraphMotion() called.");
		mFacadeLogic.startCalculation();
	}
	/**
	 * Checks whether the positions of nodes are currently calculated.
	 * @return False = Motion of graph is Off.
	 *         True  = Motion of graph is On.
	 */
	public boolean isGraphMotion() {
		LOGGER.trace("Method isGraphMotion() called.");
		return mFacadeLogic.isCalculationRunning();
	}
	/**
	 * Reposition all nodes.
	 */
	public void redrawGraph() {
		LOGGER.trace("Method redrawGraph() called.");
		mFacadeLogic.recalculateGraph();
	}
	/**
	 * Remove all nodes and edges from the graph.
	 */
	public void clearGraph() {
		LOGGER.trace("Method clearGraph() called.");
		/* Delete observables */
		for(Observable vObserbale : mObservables.keySet()) {
			vObserbale.deleteObserver(this);
		}
		mObservables.clear();
		mGraphPanel.clearGraph();
	}
	/**
	 * Load the initial graph to the timestamp in TimeSelector.
	 * @see FacadeLogic#loadInitialGraph()
	 */
	private void loadInitialGraph() {
		LOGGER.trace("Method loadInitialGraph() called.");
		/* Remove all data */
		mTimerCreation.removeAll();
		mTimerDeletion.removeAll();
		clearGraph();
		/* Call for initial graph */
		mFacadeLogic.loadInitialGraph();
	}
	/**
	 * Load the delta to the timestamps in TimeSelector.
	 * @see FacadeLogic#loadDelta()
	 */
	private  void loadDelta() {
		LOGGER.trace("Method loadDelta() called.");
		/* Call for delta */
		mFacadeLogic.loadDelta();
	}
	/**
	 * Load the initial graph and delta to the timestamp in TimeSelector.
	 * @see GuiController#loadInitialGraph()
	 * @see GuiController#loadDelta()
	 */
	private void delta() {
		LOGGER.trace("Method delta() called.");
		/* Load initial graph */
		mGraphPanel.setNodeTranslationDuration(0);
		mAddHighlights = false;
		loadInitialGraph();
		mTimerCreation.removeAll();
		mTimerDeletion.removeAll();
		mAddHighlights = true;
		/* Load delta */
		loadDelta();
		mGraphPanel.setNodeTranslationDuration(mSettingManager.getNodeTranslationDuration());
	}
	/**
	 * Fill the GraphPanel with the data.
	 * @param pData the data.
	 */
	public void fillGraph(UpdateContainer pData) {
		/* Add nodes to graph. */
		addIdentifier(pData.getListAddIdentifier());
		for(NodeIdentifier vIdentifier : pData.getListAddIdentifier()) {
			addMetadata(vIdentifier, vIdentifier.getMetadata());
		}
		for(ExpandedLink vLink : pData.getListAddLinks()) {
			addMetadata(vLink, vLink.getMetadata());
		}
		for(Entry<NodeIdentifier, List<NodeMetadata>> vSet : pData.getListAddMetadataIdentifier().entrySet()) {
			addMetadata(vSet.getKey(), vSet.getValue());
		}
		for(Entry<ExpandedLink, List<NodeMetadata>> vSet : pData.getListAddMetadataLinks().entrySet()) {
			addMetadata(vSet.getKey(), vSet.getValue());
		}
		/* Remove nodes from graph */
		for(List<NodeMetadata> vMetadata : pData.getListDeleteMetadataLinks().values()) {
			deleteMetadata(vMetadata);
		}
		for(List<NodeMetadata> vMetadata : pData.getListDeleteMetadataIdentifier().values()) {
			deleteMetadata(vMetadata);
		}
		deleteIdentifier(pData.getListDeleteIdentifier());
		/* Adjust panel size */
		mGraphPanel.adjustPanelSize();
	}
	@Override
	public void update(Observable o, Object arg) {
		LOGGER.trace("Method update(" + o + ", " + arg + ") called.");
		if(o instanceof FacadeLogic) {
			fillGraph(mFacadeLogic.getUpdate());
			mGraphPanel.repaint();
		} else if(o instanceof NodeIdentifier) {
			mGraphPanel.updateIdentifier((NodeIdentifier) o);
			mGraphPanel.repaint();
		} else if(o instanceof NodeMetadata) {
			mGraphPanel.updateMetadata((NodeMetadata) o);
			mGraphPanel.repaint();
		} else if(o instanceof TimeSelector) {
			if(!mTimeSelector.isLiveView()) {
				delta();
			}
			mGraphPanel.repaint();
		} else if(o instanceof SettingManager) {
			mGraphPanel.setNodeTranslationDuration(mSettingManager.getNodeTranslationDuration());
		}
	}
}
