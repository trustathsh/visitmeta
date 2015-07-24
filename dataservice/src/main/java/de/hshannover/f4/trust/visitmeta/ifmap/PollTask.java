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
 * This file is part of visitmeta-dataservice, version 0.5.0,
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
package de.hshannover.f4.trust.visitmeta.ifmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import de.hshannover.f4.trust.ifmapj.channel.ARC;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.messages.SearchResult;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InternalMetadataFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;

/**
 * A <tt>PollTask</tt> executes one IF-MAP poll request.
 *
 * @author Ralf Steuerwald
 *
 */
class PollTask implements Callable<PollResult> {

	private static final Logger log = Logger.getLogger(PollTask.class);

	private MapServerConnection mConnection;

	private InternalMetadataFactory mMetadataFactory;

	private IfmapJHelper mIfmapJHelper;

	/**
	 * Create a new <tt>PollTask</tt> which uses the given {@link ARC} for
	 * polling. The received data gets returned as a {@link PollResult}.
	 *
	 * @param arc
	 *            the {@link ARC} used for polling
	 * @param metadataFactory
	 *            the factory which is used to build internal metadata objects
	 */
	public PollTask(MapServerConnection connection, InternalMetadataFactory metadataFactory, IfmapJHelper helper) {
		if (connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		if (metadataFactory == null) {
			throw new IllegalArgumentException("metadataFactory cannot be null");
		}
		mConnection = connection;
		mMetadataFactory = metadataFactory;
		mIfmapJHelper = helper;
	}

	/**
	 * Executes one single poll request and returns the received data.
	 * 
	 * @throws ConnectionException
	 * @throws PollException
	 *             if something goes wrong while polling for new results
	 */
	@Override
	public PollResult call() throws ConnectionException {
		log.debug("starting poll request ...");

		de.hshannover.f4.trust.ifmapj.messages.PollResult pollResult = mConnection.poll();

		printErrors(pollResult.getErrorResults());

		Map<String, List<ResultItem>> searchResults = new HashMap<String, List<ResultItem>>();
		for (SearchResult searchResult : pollResult.getResults()) {
			switch (searchResult.getType()) {
				case updateResult:
					log.debug("processing update list ...");
					List<ResultItem> updateResultItems = transformResultItems(searchResult.getResultItems(),
							ResultItemTypeEnum.UPDATE);
					addResultItems(searchResults, searchResult.getName(), updateResultItems);
					break;
				case searchResult:
					log.debug("processing search list ...");
					List<ResultItem> searchResultItems = transformResultItems(searchResult.getResultItems(),
							ResultItemTypeEnum.SEARCH);
					addResultItems(searchResults, searchResult.getName(), searchResultItems);
					break;
				case deleteResult:
					log.debug("processing delete list ...");
					List<ResultItem> deleteResultItems = transformResultItems(searchResult.getResultItems(),
							ResultItemTypeEnum.DELETE);
					addResultItems(searchResults, searchResult.getName(), deleteResultItems);
					break;
				case notifyResult:
					log.debug("processing notify list ...");
					List<ResultItem> notifyResultItems = transformResultItems(searchResult.getResultItems(),
							ResultItemTypeEnum.NOTIFY);
					addResultItems(searchResults, searchResult.getName(), notifyResultItems);
					break;
				default:
					log.info(searchResult.getType() + " result skipped");
					break;
			}
		}

		List<ResultItem> results = filterDeleteResultItems(searchResults);

		log.debug("finish poll request.");
		return new PollResult(results);

	}

	private void printErrors(Collection<IfmapErrorResult> errorResults) {
		if (errorResults.size() > 0) {
			for (IfmapErrorResult error : errorResults) {
				log.error(error.toString());
			}
		}
	}

	private List<ResultItem> filterDeleteResultItems(Map<String, List<ResultItem>> searchResults) {
		// find linked subscriptions
		List<Set<String>> linkedSubscriptionList = findLinkedSubscriptions(searchResults);

		// filter DELETE-ResultItems
		int filteredCount = 0;
		List<ResultItem> results = new ArrayList<>();
		for (Entry<String, List<ResultItem>> entry : searchResults.entrySet()) {
			for (ResultItem item : entry.getValue()) {
				if (item.getType() == ResultItemTypeEnum.DELETE) {
					// only for DELETE-ResultItems
					Set<String> linkedSubSet = findLinkedSubscriptionSet(entry.getKey(), linkedSubscriptionList);

					if (linkedSubSet == null || containsOtherSubscrptions(searchResults, item, linkedSubSet)) {
						// check DELETE-ResultItems that are included in all linked subscriptions or is null
						results.add(item);
					} else {
						filteredCount++;
					}

				} else {
					results.add(item);
				}
			}
		}

		if (filteredCount > 0) {
			log.debug("removed " + filteredCount
					+ " DELETE-ResultItem(s) because not included in the other SearchResults");
		}

		return results;
	}

	private Set<String> findLinkedSubscriptionSet(String subscription, List<Set<String>> linkedSubscriptions) {
		for (Set<String> set : linkedSubscriptions) {
			if (set.contains(subscription)) {
				return set;
			}
		}
		return null;
	}

	private List<Set<String>> findLinkedSubscriptions(Map<String, List<ResultItem>> searchResults) {
		List<Set<String>> linkedSubscriptions = new ArrayList<Set<String>>();

		for (Entry<String, List<ResultItem>> entry : searchResults.entrySet()) {
			for (ResultItem item : entry.getValue()) {
				Set<String> subscriptions = findLinkedSubscriptions(searchResults, item);
				if (subscriptions.size() > 1) {
					addSubscriptionSet(linkedSubscriptions, subscriptions);
				}
			}
		}
		return linkedSubscriptions;
	}

	private void addSubscriptionSet(List<Set<String>> linkedSubscriptions, Set<String> subscriptions) {
		for (Set<String> set : linkedSubscriptions) {
			for (String s : subscriptions) {
				if (set.contains(s)) {
					set.addAll(subscriptions);
					return;
				}
			}
		}
		linkedSubscriptions.add(subscriptions);
	}

	private Set<String> findLinkedSubscriptions(Map<String, List<ResultItem>> searchResults, ResultItem resultItem) {
		Set<String> subscriptions = new HashSet<String>();

		for (Entry<String, List<ResultItem>> entry : searchResults.entrySet()) {
			if (entry.getValue().contains(resultItem)) {
				subscriptions.add(entry.getKey());
			}
		}

		return subscriptions;
	}

	private boolean containsOtherSubscrptions(Map<String, List<ResultItem>> searchResults, ResultItem item,
			Set<String> subscriptions) {
		for (String key : searchResults.keySet()) {
			if (subscriptions.contains(key)) {
				// only for linked subscriptions
				if (!containsSubscription(searchResults, item, key)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean containsSubscription(Map<String, List<ResultItem>> searchResults, ResultItem item,
			String subscriptionName) {
		List<ResultItem> results = searchResults.get(subscriptionName);
		return results.contains(item);
	}

	private void addResultItems(Map<String, List<ResultItem>> searchResults, String subscriptionName,
			List<ResultItem> results) {
		if (subscriptionName == null) {
			log.warn("SearchResult name is null! Nothing stored!");
			return;
		}

		if (searchResults.containsKey(subscriptionName)) {
			searchResults.get(subscriptionName).addAll(results);
		} else {
			searchResults.put(subscriptionName, results);
		}
	}

	/**
	 * Maps a list of ifmapj
	 * {@link de.hshannover.f4.trust.ifmapj.messages.ResultItem}s items to a
	 * list of internal {@link ResultItem}s.
	 */
	List<ResultItem> transformResultItems(List<de.hshannover.f4.trust.ifmapj.messages.ResultItem> ifmapjResultItems,
			ResultItemTypeEnum type) {
		List<ResultItem> items = new ArrayList<>();
		for (de.hshannover.f4.trust.ifmapj.messages.ResultItem item : ifmapjResultItems) {
			log.debug("processing result item '" + item + "'");

			ResultItem resultItem = transformResultItem(item, type);
			if (resultItem.getMetadata().size() > 0) {
				items.add(resultItem);
			}
		}
		return items;
	}

	/**
	 * Transforms a ifmapj
	 * {@link de.hshannover.f4.trust.ifmapj.messages.ResultItem} into a internal
	 * {@link ResultItem}.
	 */
	ResultItem transformResultItem(de.hshannover.f4.trust.ifmapj.messages.ResultItem ifmapjResultItem,
			ResultItemTypeEnum type) {
		List<Document> metadataDocuments = ifmapjResultItem.getMetadata();

		List<InternalMetadata> metadata = new ArrayList<>(metadataDocuments.size());
		for (Document d : metadataDocuments) {
			InternalMetadata m = mMetadataFactory.createMetadata(d);
			if(type == ResultItemTypeEnum.NOTIFY) {
				m.switchToNotify();
			}
			metadata.add(m);
		}

		if (ifmapjResultItem.holdsLink()) {
			InternalIdentifier id1 = mIfmapJHelper.ifmapjIdentifierToInternalIdentifier(ifmapjResultItem
					.getIdentifier1());
			InternalIdentifier id2 = mIfmapJHelper.ifmapjIdentifierToInternalIdentifier(ifmapjResultItem
					.getIdentifier2());
			return new ResultItem(id1, id2, metadata, type);
		} else {
			InternalIdentifier id = mIfmapJHelper.extractSingleIdentifier(ifmapjResultItem);
			return new ResultItem(id, null, metadata, type);
		}
	}
}
