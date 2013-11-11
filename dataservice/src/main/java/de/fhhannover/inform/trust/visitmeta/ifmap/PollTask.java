package de.fhhannover.inform.trust.visitmeta.ifmap;

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
import java.util.concurrent.Callable;

import javax.xml.parsers.DocumentBuilder;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import de.fhhannover.inform.trust.ifmapj.channel.ARC;
import de.fhhannover.inform.trust.ifmapj.exception.EndSessionException;
import de.fhhannover.inform.trust.ifmapj.exception.IfmapErrorResult;
import de.fhhannover.inform.trust.ifmapj.exception.IfmapException;
import de.fhhannover.inform.trust.ifmapj.messages.SearchResult;
import de.fhhannover.inform.trust.visitmeta.dataservice.factories.InternalMetadataFactory;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.fhhannover.inform.trust.visitmeta.persistence.Writer;

/**
 * A <tt>PollTask</tt> executes one IF-MAP poll request.
 *
 * @author Ralf Steuerwald
 *
 */
class PollTask implements Callable<PollResult> {

	private static final Logger log = Logger.getLogger(PollTask.class);

	private ARC mArc;

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
	public PollTask(ARC arc, InternalMetadataFactory metadataFactory, IfmapJHelper helper) {
		if (arc == null) {
			throw new IllegalArgumentException("ARC cannot be null");
		}
		if (metadataFactory == null) {
			throw new IllegalArgumentException("metadataFactory cannot be null");
		}
		mArc = arc;
		mMetadataFactory = metadataFactory;
		mIfmapJHelper = helper;
	}

	/**
	 * Executes one single poll request and returns the received data.
	 *
	 * @throws PollException if something goes wrong while polling for new results
	 */
	@Override
	public PollResult call() {
		log.debug("starting poll request ...");
		try {
			de.fhhannover.inform.trust.ifmapj.messages.PollResult pollResult = mArc.poll();

			List<ResultItem> updates = new ArrayList<>();
			List<ResultItem> deletes = new ArrayList<>();
			for (SearchResult searchResult : pollResult.getResults()) {
				switch (searchResult.getType()) {
				case updateResult:
					log.debug("processing search list ...");
					updates.addAll(transformResultItems(searchResult.getResultItems()));
					break;
				case searchResult:
					log.debug("processing update list ...");
					updates.addAll(transformResultItems(searchResult.getResultItems()));
					break;
				case deleteResult:
					log.debug("processing delete list ...");
					deletes.addAll(transformResultItems(searchResult.getResultItems()));
					break;
				default:
					// TODO ignore notify updates?
					log.info(searchResult.getType() + " result skipped");
					break;
				}
			}
			log.debug("finish poll request.");
			return new PollResult(updates, deletes);
		} catch (IfmapErrorResult | EndSessionException | IfmapException e) {
			throw new PollException(e);
		}
	}

	/**
	 * Maps a list of ifmapj {@link de.fhhannover.inform.trust.ifmapj.messages.ResultItem}s items
	 * to a list of internal {@link ResultItem}s.
	 */
	List<ResultItem> transformResultItems(
			List<de.fhhannover.inform.trust.ifmapj.messages.ResultItem> ifmapjResultItems) {
		List<ResultItem> items = new ArrayList<>();
		for (de.fhhannover.inform.trust.ifmapj.messages.ResultItem item : ifmapjResultItems) {
			log.debug("processing result item '"+item+"'");

			ResultItem resultItem = transformResultItem(item);
			if (resultItem.getMetadata().size() > 0) {
				items.add(resultItem);
			}
		}
		return items;
	}

	/**
	 * Transforms a ifmapj {@link de.fhhannover.inform.trust.ifmapj.messages.ResultItem}
	 * into a internal {@link ResultItem}.
	 */
	ResultItem transformResultItem(
			de.fhhannover.inform.trust.ifmapj.messages.ResultItem ifmapjResultItem) {
		List<Document> metadataDocuments = ifmapjResultItem.getMetadata();

		List<InternalMetadata> metadata = new ArrayList<>(metadataDocuments.size());
		for (Document d : metadataDocuments) {
			InternalMetadata m = mMetadataFactory.createMetadata(d);
			metadata.add(m);
		}

		if (ifmapjResultItem.holdsLink()) {
			InternalIdentifier id1 = mIfmapJHelper.ifmapjIdentifierToInternalIdentifier(
					ifmapjResultItem.getIdentifier1());
			InternalIdentifier id2 = mIfmapJHelper.ifmapjIdentifierToInternalIdentifier(
					ifmapjResultItem.getIdentifier2());
			return new ResultItem(id1, id2, metadata);
		} else {
			InternalIdentifier id = mIfmapJHelper.extractSingleIdentifier(ifmapjResultItem);
			return new ResultItem(id, null, metadata);
		}
	}
}
