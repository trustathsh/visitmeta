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
package de.hshannover.f4.trust.visitmeta.network;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.ExpandedLink;
import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeIdentifier;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeMetadata;
import de.hshannover.f4.trust.visitmeta.datawrapper.RichMetadata;
import de.hshannover.f4.trust.visitmeta.datawrapper.SettingManager;
import de.hshannover.f4.trust.visitmeta.datawrapper.TimeHolder;
import de.hshannover.f4.trust.visitmeta.datawrapper.UpdateContainer;
import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.util.Check;

/**
 * Class for managing a graph received from a dataservice. Contains methods to
 * update or load the graph.
 */
public class GraphNetworkConnection {
	private static final Logger LOGGER = Logger.getLogger(GraphNetworkConnection.class);

	protected UpdateContainer mUpdateContainer;

	private GraphService mGraphService = null;
	private TimeHolder mTimeHolder = null;
	private GraphPool mGraphPool = null;
	private SortedMap<Long, Long> mChangesMap = null;
	private SortedMap<Long, Long> mKnownChangesMap = null;

	private SettingManager mSettingManager = null;
	int mInterval = 0;

	/**
	 * Initializes a Connection-class instance.
	 * 
	 * @param graphService
	 *            The GraphService which matches the connection.
	 * @param container
	 *            Contains information about the Connection.
	 */
	public GraphNetworkConnection(GraphService graphService, GraphContainer container) {
		mUpdateContainer = new UpdateContainer();
		mGraphService = graphService;
		mTimeHolder = container.getTimeHolder();
		mGraphPool = container.getGraphPool();
		mSettingManager = container.getSettingManager();
		mInterval = mSettingManager.getNetworkInterval();
	}

	/**
	 * Returns the UpdateContainer which can be used freely.
	 * 
	 * @return A container class with the changes.
	 */
	public UpdateContainer getUpdate() {
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
		synchronized (this) {
			for (NodeIdentifier identifier : mUpdateContainer.getListDeleteIdentifier()) {
				mGraphPool.releaseIdentifier(identifier.getIdentifier());
			}
			for (ExpandedLink link : mUpdateContainer.getListDeleteLinks()) {
				mGraphPool.releaseLink(link.getLink());
			}
			for (List<NodeMetadata> listMetadata : mUpdateContainer.getListDeleteMetadataIdentifier().values()) {
				for (NodeMetadata metadata : listMetadata) {
					mGraphPool.releaseMetadata(metadata.getRichMetadata());
				}
			}
			for (List<NodeMetadata> listMetadata : mUpdateContainer.getListDeleteMetadataLinks().values()) {
				for (NodeMetadata metadata : listMetadata) {
					mGraphPool.releaseMetadata(metadata.getRichMetadata());
				}
			}
		}
	}

	/**
	 * Initializes the TimeHolder. Blocks till the TimeHolder is initialized!
	 * 
	 * @throws InterruptedException
	 *             If an interruption occurs during the timeout.
	 */
	private synchronized void initTimeHolder() throws InterruptedException {
		// This loop makes sure the TimeHolder is initialized correctly
		while (!mTimeHolder.isInitialized()) {
			loadChangesMap();
			mTimeHolder.notifyObservers();
			if (!mTimeHolder.isInitialized()) {
				LOGGER.info("No initial data found; retrying in " + mInterval + " milliseconds.");
				Thread.sleep(mInterval);
			}
		}
	}

	/**
	 * Loads the ChangesMap from the dataservice.
	 */
	public synchronized void loadChangesMap() {
		mKnownChangesMap = mChangesMap;
		mChangesMap = mGraphService.getChangesMap();

		if (mChangesMap != null && 0 < mChangesMap.size()) {
			LOGGER.debug("Server has " + mChangesMap.size() + " changes.");
			mTimeHolder.setChangesMap(mChangesMap);
		} else {
			LOGGER.info("Server has no map of changes.");
		}
	}

	/**
	 * Loads the graph at the beginning of a delta. Blocks till the TimeHolder
	 * is initialized!
	 * 
	 * @throws InterruptedException
	 *             If an interruption occurs during the timeout.
	 */
	public synchronized void loadGraphAtDeltaStart() throws InterruptedException {
		initTimeHolder();

		List<IdentifierGraph> graphs = mGraphService.getGraphAt(mTimeHolder.getDeltaTimeStart());
		if (graphs != null && graphs.size() > 0) {
			LOGGER.info("Load initial graph at: " + mTimeHolder.getDeltaTimeStart() + ".");
			debugGraphContent("initial", graphs);
			deltaUpdate(graphs);
			mTimeHolder.notifyObservers();
		}
	}

	/**
	 * Loads the current graph. Blocks till the TimeHolder is initialized!
	 * 
	 * @throws InterruptedException
	 *             If an interruption occurs during the timeout.
	 */
	public synchronized void loadCurrentGraph() throws InterruptedException {
		initTimeHolder();

		List<IdentifierGraph> graphs = mGraphService.getCurrentGraph();
		if (graphs != null && graphs.size() > 0) {
			LOGGER.info("Load current graph.");
			deltaUpdate(graphs);
			mTimeHolder.notifyObservers();
		}
	}

	/**
	 * Loads a delta.
	 * 
	 * @return Whether any updates and/or deletes occurred.
	 */
	public synchronized boolean loadDelta() {
		boolean update = false;
		boolean delete = false;
		if (mGraphService != null) {
			long timeStart = 0;
			long timeEnd = 0;
			synchronized (mTimeHolder) {
				timeStart = mTimeHolder.getDeltaTimeStart();
				timeEnd = mTimeHolder.getDeltaTimeEnd();
			}
			Delta delta = mGraphService.getDelta(timeStart, timeEnd);
			debugGraphContent(delta);
			if (delta.getUpdates().size() > 0) {
				update = deltaUpdate(delta.getUpdates());
			}
			if (delta.getDeletes().size() > 0) {
				delete = deltaDelete(delta.getDeletes());
			}
		}
		releaseData();
		return update || delete;
	}

	/**
	 * Updates the graph. Blocks till the TimeHolder is initialized!
	 * 
	 * @return Whether the graph has changed or not.
	 * @throws InterruptedException
	 *             If an interruption occurs during the timeout.
	 */
	public synchronized boolean updateGraph() throws InterruptedException {
		boolean update = false;
		boolean delete = false;

		initTimeHolder();

		long timeEnd = 0L;
		long newestTime = 0L;

		synchronized (mTimeHolder) {
			timeEnd = mTimeHolder.getDeltaTimeEnd();
			newestTime = mTimeHolder.getNewestTime();
			if (mKnownChangesMap != null && Check.chageMapHasChangeInThePast(timeEnd, mKnownChangesMap, mChangesMap)) {
				loadCurrentGraph();
				return true;
			}
		}

		if (timeEnd < newestTime) {
			Delta delta = mGraphService.getDelta(timeEnd, newestTime);
			debugGraphContent(delta);
			if (delta.getUpdates().size() > 0) {
				update = deltaUpdate(delta.getUpdates());
			}
			if (delta.getDeletes().size() > 0) {
				delete = deltaDelete(delta.getDeletes());
			}
			if (update || delete) {
				mTimeHolder.setDeltaTimeEnd(newestTime, false);
			}
		}
		releaseData();
		return update || delete;
	}

	/**
	 * Convert the updates from the dataservice to internal format.
	 * 
	 * @param graphs
	 *            the graph with the updates.
	 * @return Whether any updates occurred.
	 */
	// TODO bad method name!!! not only used for delta updates !!!
	protected boolean deltaUpdate(List<IdentifierGraph> graphs) {
		boolean result = false;
		for (IdentifierGraph graph : graphs) {
			for (Identifier identifier : graph.getIdentifiers()) {
				NodeIdentifier tmpIdentifier = mGraphPool.createIdentifier(identifier);
				if (tmpIdentifier == null) {
					tmpIdentifier = mGraphPool.getIdentifier(identifier);
					List<NodeMetadata> listMetadata = mUpdateContainer.getListAddMetadataIdentifier()
							.get(tmpIdentifier);
					if (listMetadata == null) {
						listMetadata = new ArrayList<>();
						mUpdateContainer.getListAddMetadataIdentifier().put(tmpIdentifier, listMetadata);
					}
					for (Metadata metadata : identifier.getMetadata()) {
						NodeMetadata tmpMetadata = mGraphPool.createMetadata(new RichMetadata(metadata, identifier));
						if (tmpMetadata != null) {
							LOGGER.debug("New metadata of an identifier.");
							listMetadata.add(tmpMetadata);
							mGraphPool.getIdentifier(identifier).addNodeMetadata(tmpMetadata);
							result = true;
						}
					}
				} else {
					LOGGER.debug("New identifier.");
					mUpdateContainer.getListAddIdentifier().add(tmpIdentifier);
					result = true;
				}
			}
			for (Identifier identifier : graph.getIdentifiers()) {
				for (Link link : identifier.getLinks()) {
					ExpandedLink tmpLink = mGraphPool.createLink(link);
					if (tmpLink == null) {
						tmpLink = mGraphPool.getLink(link);
						List<NodeMetadata> listMetadata = mUpdateContainer.getListAddMetadataLinks().get(tmpLink);
						if (listMetadata == null) {
							listMetadata = new ArrayList<>();
							mUpdateContainer.getListAddMetadataLinks().put(tmpLink, listMetadata);
						}
						for (Metadata metadata : link.getMetadata()) {
							NodeMetadata tmpMetadata = mGraphPool.createMetadata(new RichMetadata(metadata, link));
							if (tmpMetadata != null) {
								LOGGER.debug("New metadata of an link.");
								listMetadata.add(tmpMetadata);
								mGraphPool.getLink(link).addMetadata(tmpMetadata);
								result = true;
							}
						}
					} else {
						LOGGER.debug("New link.");
						mUpdateContainer.getListAddLinks().add(tmpLink);
						result = true;
					}
				}
			}
		}
		LOGGER.debug("New data found: " + result);
		return result;
	}

	/**
	 * Convert the deletes from the dataservice to internal format.
	 * 
	 * @param graphs
	 *            the graph with the deletes.
	 * @return Whether any deletes occured.
	 */
	protected boolean deltaDelete(List<IdentifierGraph> graphs) {
		LOGGER.trace("Method deltaDelete(" + graphs + ") called.");
		boolean result = false;
		for (IdentifierGraph graph : graphs) {
			for (Identifier identifier : graph.getIdentifiers()) {
				NodeIdentifier tmpIdentifier = mGraphPool.getIdentifier(identifier);
				if (tmpIdentifier != null) {
					List<NodeMetadata> listMetadata = mUpdateContainer.getListDeleteMetadataIdentifier().get(
							tmpIdentifier);
					if (listMetadata == null) {
						listMetadata = new ArrayList<>();
						mUpdateContainer.getListDeleteMetadataIdentifier().put(tmpIdentifier, listMetadata);
					}
					for (Metadata metadata : identifier.getMetadata()) {
						NodeMetadata tmpMetadata = mGraphPool.getMetadata(new RichMetadata(metadata, identifier));
						if (tmpMetadata != null) {
							LOGGER.debug("Delete metadata of an identifer.");
							listMetadata.add(tmpMetadata);
							mGraphPool.getIdentifier(identifier).deleteNodeMetadata(tmpMetadata);
							result = true;
						}
					}
					if (!tmpIdentifier.hasMetadata() && !tmpIdentifier.hasLinks()) {
						LOGGER.debug("Delete identifer.");
						mUpdateContainer.getListDeleteIdentifier().add(tmpIdentifier);
						result = true;
					}
				}
			}
			for (Identifier identifier : graph.getIdentifiers()) {
				for (Link link : identifier.getLinks()) {
					ExpandedLink tmpLink = mGraphPool.getLink(link);
					if (tmpLink != null) {
						List<NodeMetadata> listMetadata = mUpdateContainer.getListDeleteMetadataLinks().get(tmpLink);
						if (listMetadata == null) {
							listMetadata = new ArrayList<>();
							mUpdateContainer.getListDeleteMetadataLinks().put(tmpLink, listMetadata);
						}
						for (Metadata metadata : link.getMetadata()) {
							NodeMetadata tmpMetadata = mGraphPool.getMetadata(new RichMetadata(metadata, link));
							if (tmpMetadata != null && !listMetadata.contains(tmpMetadata)) {
								LOGGER.debug("Delete metadata of a link.");
								listMetadata.add(tmpMetadata);
								mGraphPool.getLink(link).deleteMetadata(tmpMetadata);
								result = true;
							}
						}
						if (!tmpLink.hasMetadata()) {
							LOGGER.debug("Delete link.");
							mUpdateContainer.getListDeleteLinks().add(tmpLink);
							NodeIdentifier first = tmpLink.getFirst();
							NodeIdentifier second = tmpLink.getSecond();
							if (first != null) {
								tmpLink.deleteIdentifier(first);
								if (!first.hasLinks() && !first.hasMetadata()) {
									LOGGER.debug("Delete first identifier of a link.");
									mUpdateContainer.getListDeleteIdentifier().add(first);
									result = true;
								}
							}
							if (second != null) {
								tmpLink.deleteIdentifier(second);
								if (!second.hasLinks() && !second.hasMetadata()) {
									LOGGER.debug("Delete second identifier of a link.");
									mUpdateContainer.getListDeleteIdentifier().add(second);
									result = true;
								}
							}
						}
					}
				}
			}
		}
		LOGGER.debug("Data to delete: " + result);
		return result;
	}

	/**
	 * Shows the amounts of Identifier, Links and Metadata of the given Graph
	 * with some more details.
	 * 
	 * @param graphType
	 *            Type of the graph.
	 * @param graphs
	 *            Graph to debug.
	 */
	protected void debugGraphContent(String graphType, List<IdentifierGraph> graphs) {
		if (Logger.getRootLogger().getLevel().toInt() < Level.INFO.toInt()) {
			int numberOfGraph = 0;
			int numberOfIdentifier = 0;
			int numberOfMetadata = 0;
			int numberOfLinks = 0;
			LOGGER.debug("Count content of " + graphType + " graph:");
			for (IdentifierGraph graph : graphs) {
				++numberOfGraph;
				String logResult = "Graph " + numberOfGraph;
				for (Identifier vIdentifier : graph.getIdentifiers()) {
					++numberOfIdentifier;
					logResult += "\n* " + vIdentifier.getTypeName() + " |";
					numberOfMetadata += vIdentifier.getMetadata().size();
					for (Metadata vMetadata : vIdentifier.getMetadata()) {
						logResult += " " + vMetadata.getTypeName();
					}
					for (Link vLink : vIdentifier.getLinks()) {
						++numberOfLinks;
						logResult += "\n    link |";
						numberOfMetadata += vLink.getMetadata().size();
						for (Metadata vMetadata : vLink.getMetadata()) {
							logResult += " " + vMetadata.getTypeName();
						}
					}
				}
				LOGGER.debug(logResult);
			}
			LOGGER.debug("Graph has " + numberOfIdentifier + " references to identifier.");
			LOGGER.debug("Graph has " + numberOfMetadata + " references to metadata.");
			LOGGER.debug("Graph has " + numberOfLinks + " references to links.");
		}
	}

	/**
	 * @param graph
	 *            A Delta containing updates and/or deletes.
	 */
	protected void debugGraphContent(Delta graph) {
		debugGraphContent("update", graph.getUpdates());
		debugGraphContent("delete", graph.getDeletes());
	}
}
