package de.fhhannover.inform.trust.visitmeta.network;

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

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.visitmeta.datawrapper.ExpandedLink;
import de.fhhannover.inform.trust.visitmeta.datawrapper.NodeIdentifier;
import de.fhhannover.inform.trust.visitmeta.datawrapper.NodeMetadata;
import de.fhhannover.inform.trust.visitmeta.datawrapper.SettingManager;
import de.fhhannover.inform.trust.visitmeta.datawrapper.TimeHolder;
import de.fhhannover.inform.trust.visitmeta.datawrapper.TimeSelector;
import de.fhhannover.inform.trust.visitmeta.datawrapper.UpdateContainer;
import de.fhhannover.inform.trust.visitmeta.interfaces.Delta;
import de.fhhannover.inform.trust.visitmeta.interfaces.GraphService;
import de.fhhannover.inform.trust.visitmeta.interfaces.Identifier;
import de.fhhannover.inform.trust.visitmeta.interfaces.IdentifierGraph;
import de.fhhannover.inform.trust.visitmeta.interfaces.Link;
import de.fhhannover.inform.trust.visitmeta.interfaces.Metadata;

/**
 * Class for managing the connection to the dataservice.
 */
public class Connection {

	private static final Logger LOGGER = Logger.getLogger(Connection.class);

	protected UpdateContainer mUpdateContainer;

	GraphService 		   mGraphService = null;
	TimeHolder             mTimeHolder   = null;
	TimeSelector           mTimeSelector = null;
	SortedMap<Long, Long>  mChangesMap   = null;
	
	private SettingManager mSettingManager = null;
	int                    mInterval       = 0;
	
	public Connection(GraphService graphService) {
		mUpdateContainer = new UpdateContainer();
		mGraphService = graphService;
		mTimeSelector = TimeSelector.getInstance();
		mSettingManager = SettingManager.getInstance();
		mInterval       = mSettingManager.getNetworkInterval();
	}

	/**
	 * Returns the UpdateContainer which can be used freely.
	 * @return a Container-Class with the changes.
	 */
	public UpdateContainer getUpdate() {
		LOGGER.trace("Method getUpdate() called.");
		UpdateContainer result;
		synchronized (this) {
			result = mUpdateContainer;
			mUpdateContainer = new UpdateContainer();
		}
		return result;
	}

	/**
	 * Release the data in the delete lists of the UpdateContainer.
	 */
	protected void releaseData() {
		LOGGER.trace("Method releaseData() called.");
		synchronized (this) {
			for (NodeIdentifier vIdentifier : mUpdateContainer.getListDeleteIdentifier()) {
				PoolNodeIdentifier.release(vIdentifier.getIdentifier());
			}
			for (ExpandedLink vLink : mUpdateContainer.getListDeleteLinks()) {
				PoolExpandedLink.release(vLink.getLink());
			}
			for (List<NodeMetadata> vListMetadata : mUpdateContainer.getListDeleteMetadataIdentifier().values()) {
				for (NodeMetadata vMetadata : vListMetadata) {
					PoolNodeMetadata.release(vMetadata.getMetadata());
				}
			}
			for (List<NodeMetadata> vListMetadata : mUpdateContainer.getListDeleteMetadataLinks().values()) {
				for (NodeMetadata vMetadata : vListMetadata) {
					PoolNodeMetadata.release(vMetadata.getMetadata());
				}
			}
		}
	}

	/**
	 * Load the ChangesMap from the dataservice.
	 */
	public synchronized void loadChangesMap() {
		LOGGER.trace("Method loadChangesMap() called.");
		if (mTimeHolder != null) {
			mChangesMap = mGraphService.getChangesMap();
			if (mChangesMap != null && 0 < mChangesMap.size()) {
				LOGGER.debug("Server has " + mChangesMap.size() + " changes.");
				mTimeHolder.setChangesMap(mChangesMap);
			} else {
				LOGGER.warn("Server has no map of changes.");
			}
		}
	}

	/**
	 * Load the initial graph to the timestamp in TimeSelector.
	 */
	public synchronized void loadInitialGraph() {
		LOGGER.trace("Method loadInitialGraph() called.");
		List<IdentifierGraph> vGraphs = mGraphService.getGraphAt(mTimeSelector.getTimeStart());
		if(vGraphs != null && vGraphs.size() > 0) {
			LOGGER.info("Load initial graph at: " + mTimeSelector.getTimeStart() + ".");
			debugGraphContent("initial", vGraphs);
			deltaUpdate(vGraphs);
			synchronized (mTimeHolder) {
				mTimeHolder.setTimeStart(vGraphs.get(0).getTimestamp(), false);
				mTimeHolder.setTimeEnd(vGraphs.get(0).getTimestamp(),   false);
			}
			mTimeHolder.notifyObservers();
		}
	}

	/**
	 * Load the delta to the timestamps in TimeSelector.
	 * @return true  = has updates and/or deletes.
	 *         false = no updates and deletes.
	 */
	public synchronized boolean loadDelta() {
		LOGGER.trace("Method loadDelta() called.");
		boolean vUpdate = false;
		boolean vDelete = false;
		if(mGraphService != null) {
			long vTimeStart = 0;
			long vTimeEnd   = 0;
			synchronized (mTimeSelector) {
				vTimeStart = mTimeSelector.getTimeStart();
				vTimeEnd   = mTimeSelector.getTimeEnd();
			}
			LOGGER.info("Update start time of graph.");
			LOGGER.info("From: " + vTimeStart);
			LOGGER.info("To: "   + vTimeEnd);
			Delta vDelta = mGraphService.getDelta(
					vTimeStart, // From
					vTimeEnd    // To
			);
			debugGraphContent(vDelta);
			if(vDelta.getUpdates().size() > 0) {
				vUpdate = deltaUpdate(vDelta.getUpdates());
			}
			if(vDelta.getDeletes().size() > 0) {
				vDelete = deltaDelete(vDelta.getDeletes());
			}
			/* Set end time */
			if(vUpdate || vDelete) {
				mTimeHolder.setTimeEnd(vTimeEnd);
			}
		}
		releaseData();
		return vUpdate || vDelete;
	}

	/**
	 * Load the delta for the live view.
	 * @return
	 * @throws InterruptedException 
	 */
	public synchronized boolean updateGraph() throws InterruptedException {
		LOGGER.trace("Method updateGraph() called.");
		boolean vUpdate = false;
		boolean vDelete = false;
		/* Init: Load current graph */
		while(mTimeHolder == null) {
			List<IdentifierGraph> vGraphs = mGraphService.getCurrentGraph();
			if(vGraphs != null && vGraphs.size() > 0) {
				LOGGER.info("Load initial graph.");
				debugGraphContent("initial", vGraphs);
				vUpdate     = deltaUpdate(vGraphs);
				mTimeHolder = TimeHolder.getInstance();
				loadChangesMap();
				synchronized (mTimeHolder) {
					mTimeHolder.setTimeStart(vGraphs.get(0).getTimestamp(), false);
					mTimeHolder.setTimeEnd(vGraphs.get(0).getTimestamp(),   false);
				}
				mTimeHolder.notifyObservers();
			} else {
				// FIXME refactoring needed; wait for network-interval				
				LOGGER.info("No initial data found; retrying in " + mInterval + " milliseconds.");
				Thread.sleep(mInterval);
			}
		}
		/* Update */
		long vTimeEnd    = 0;
		long vNewestTime = 0;
		synchronized (mTimeHolder) {
			vTimeEnd    = mTimeHolder.getTimeEnd();
			vNewestTime = mTimeHolder.getNewestTime();
		}
		if(vTimeEnd < vNewestTime) {
			LOGGER.info("Update graph.");
			LOGGER.info("From: " + vTimeEnd);
			LOGGER.info("To: "   + vNewestTime);
			Delta vDelta = mGraphService.getDelta(vTimeEnd, vNewestTime);
			debugGraphContent(vDelta);
			if (vDelta.getUpdates().size() > 0) {
				vUpdate = deltaUpdate(vDelta.getUpdates());
			}
			if (vDelta.getDeletes().size() > 0) {
				vDelete = deltaDelete(vDelta.getDeletes());
			}
			/* Set end time */
			if(vUpdate || vDelete) {
				mTimeHolder.setTimeEnd(vNewestTime);
			}
		}
		releaseData();
		return vUpdate || vDelete;
	}
	
	/**
	 * Convert the updates from the dataservice to internal format.
	 * @param pGraphs the graph with the changes.
	 * @return true  = has updates.
	 *         false = no updates.
	 */
	protected boolean deltaUpdate(List<IdentifierGraph> pGraphs) {
		LOGGER.trace("Method deltaUpdate(" + pGraphs + ") called.");
		boolean vResult = false;
		/* Updates from delta */
		for(IdentifierGraph graph : pGraphs) {
			/* identifier */
			for(Identifier identifier : graph.getIdentifiers()) {
				NodeIdentifier tmpIdentifier = PoolNodeIdentifier.create(identifier);
				if(tmpIdentifier == null) {
					/* Identifier already exists */
					tmpIdentifier = PoolNodeIdentifier.getActive(identifier);
					/* Metadata */
					List<NodeMetadata> listMetadata = mUpdateContainer.getListAddMetadataIdentifier().get(tmpIdentifier);
					if(listMetadata == null) {
						listMetadata = new ArrayList<>();
						mUpdateContainer.getListAddMetadataIdentifier().put(tmpIdentifier, listMetadata);
					}
					/* Add new Metadata */
					for(Metadata metadata : identifier.getMetadata()) {
						NodeMetadata tmpMetadata = PoolNodeMetadata.create(metadata);
						if(tmpMetadata != null) {
							LOGGER.debug("New metadata of an identifier.");
							listMetadata.add(tmpMetadata);
							PoolNodeIdentifier.getActive(identifier).addNodeMetadata(tmpMetadata); // Add Metadata to Identifier
							vResult = true;
						}
					}
				} else {
					/* Identifier is new */
					LOGGER.debug("New identifier.");
					mUpdateContainer.getListAddIdentifier().add(tmpIdentifier);
					vResult = true;
				}
			}
			/* links */
			for(Identifier identifier : graph.getIdentifiers()) {
				for(Link link : identifier.getLinks()) {
					ExpandedLink tmpLink = PoolExpandedLink.create(link);
					if(tmpLink == null) {
						/* link already exists */
						tmpLink = PoolExpandedLink.getActive(link);
						/* Metadata */
						List<NodeMetadata> listMetadata = mUpdateContainer.getListAddMetadataLinks().get(tmpLink);
						if(listMetadata == null) {
							listMetadata = new ArrayList<>();
							mUpdateContainer.getListAddMetadataLinks().put(tmpLink, listMetadata);
						}
						/* Add new Metadata */
						for(Metadata metadata : link.getMetadata()) {
							NodeMetadata tmpMetadata = PoolNodeMetadata.create(metadata);
							if(tmpMetadata != null) {
								LOGGER.debug("New metadata of an link.");
								listMetadata.add(tmpMetadata);
								PoolExpandedLink.getActive(link).addMetadata(tmpMetadata); // Add Metadata to Link
								vResult = true;
							}
						}
					} else {
						/* Identifier is new */
						LOGGER.debug("New link.");
						mUpdateContainer.getListAddLinks().add(tmpLink);
						vResult = true;
					}
				}
			}
		}
		LOGGER.debug("New data found: " + vResult);
		return vResult;
	}

	/**
	 * Convert the deletes from the dataservice to internal format.
	 * @param pGraphs the graph with the changes.
	 * @return true  = has deletes.
	 *         false = no deletes.
	 */
	protected boolean deltaDelete(List<IdentifierGraph> pGraphs) {
		LOGGER.trace("Method deltaDelete(" + pGraphs + ") called.");
		boolean vResult = false;
		/* Deletes from delta */
		for(IdentifierGraph graph : pGraphs) {
			/* identifier */
			for(Identifier identifier : graph.getIdentifiers()) {
				NodeIdentifier tmpIdentifier = PoolNodeIdentifier.getActive(identifier);
				if(tmpIdentifier != null) {
					/* Metadata */
					List<NodeMetadata> listMetadata = mUpdateContainer.getListDeleteMetadataIdentifier().get(tmpIdentifier);
					if(listMetadata == null) {
						listMetadata = new ArrayList<>();
						mUpdateContainer.getListDeleteMetadataIdentifier().put(tmpIdentifier, listMetadata);
					}
					/* Add old Metadata */
					for(Metadata metadata : identifier.getMetadata()) {
						NodeMetadata tmpMetadata = PoolNodeMetadata.getActive(metadata);
						if(tmpMetadata != null) {
							LOGGER.debug("Delete metadata of an identifer.");
							listMetadata.add(tmpMetadata);
							PoolNodeIdentifier.getActive(identifier).deleteNodeMetadata(tmpMetadata); // Delete Metadata to Identifier
							vResult = true;
						}
					}
					/* Add empty identifier to delete list */
					if(!tmpIdentifier.hasMetadata() && !tmpIdentifier.hasLinks()) {
						LOGGER.debug("Delete identifer.");
						mUpdateContainer.getListDeleteIdentifier().add(tmpIdentifier);
						vResult = true;
					}
				}
			}
			/* links */
			for(Identifier identifier : graph.getIdentifiers()) {
				for(Link link : identifier.getLinks()) {
					ExpandedLink tmpLink = PoolExpandedLink.getActive(link);
					if(tmpLink != null) {
						/* Metadata */
						List<NodeMetadata> listMetadata = mUpdateContainer.getListDeleteMetadataLinks().get(tmpLink);
						if(listMetadata == null) {
							listMetadata = new ArrayList<>();
							mUpdateContainer.getListDeleteMetadataLinks().put(tmpLink, listMetadata);
						}
						/* Add old Metadata */
						for(Metadata metadata : link.getMetadata()) {
							NodeMetadata tmpMetadata = PoolNodeMetadata.getActive(metadata);
							if(tmpMetadata != null && !listMetadata.contains(tmpMetadata)) {
								LOGGER.debug("Delete metadata of a link.");
								listMetadata.add(tmpMetadata);
								PoolExpandedLink.getActive(link).deleteMetadata(tmpMetadata); // Delete Metadata to Link
								vResult = true;
							}
						}
						/* Add empty link to delete list */
						if(!tmpLink.hasMetadata()) {
							mUpdateContainer.getListDeleteLinks().add(tmpLink);
							NodeIdentifier first = tmpLink.getFirst();
							NodeIdentifier second = tmpLink.getSecond();
							/* Add empty identifier to delete list */
							if(first != null) {
								tmpLink.deleteIdentifier(first);
								if(!first.hasLinks() && !first.hasMetadata()) {
									LOGGER.debug("Delete identifier.");
									mUpdateContainer.getListDeleteIdentifier().add(first);
									vResult = true;
								}
							}
							if(second != null) {
								tmpLink.deleteIdentifier(second);
								if(!second.hasLinks() && !second.hasMetadata()) {
									LOGGER.debug("Delete identifier.");
									mUpdateContainer.getListDeleteIdentifier().add(second);
									vResult = true;
								}
							}
						}
					}
				}
			}
		}
		LOGGER.debug("Data to delete: " + vResult);
		return vResult;
	}
	
	protected void debugGraphContent(String pGraphType, List<IdentifierGraph> pGraphs) {
		if(Logger.getRootLogger().getLevel().toInt() < Level.INFO.toInt()) {
			int vNumberOfIdentifier = 0;
			int vNumberOfMetadata   = 0;
			int vNumberOfLinks      = 0;
			LOGGER.debug("Count content of " + pGraphType +" graph:");
			/* Count Content */
			for(IdentifierGraph vGraph : pGraphs) {
				for(Identifier vIdentifier : vGraph.getIdentifiers()) {
					++vNumberOfIdentifier;
					vNumberOfMetadata += vIdentifier.getMetadata().size();
					for(Link vLink : vIdentifier.getLinks()) {
						++vNumberOfLinks;
						vNumberOfMetadata += vLink.getMetadata().size();
					}
				}
			}
			/* Logging */
			LOGGER.debug("Graph has " + vNumberOfIdentifier + " references to identifier.");
			LOGGER.debug("Graph has " + vNumberOfMetadata + " references to metadata.");
			LOGGER.debug("Graph has " + vNumberOfLinks + " references to links.");
		}
	}
	
	protected void debugGraphContent(Delta pGraph) {
		/* Update graph */
		debugGraphContent("update", pGraph.getUpdates());
		/* Delete graph */
		debugGraphContent("delete", pGraph.getDeletes());
	}
}
