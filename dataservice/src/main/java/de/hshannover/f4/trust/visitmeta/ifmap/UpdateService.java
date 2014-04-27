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
 * This file is part of visitmeta dataservice, version 0.0.3,
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
package de.hshannover.f4.trust.visitmeta.ifmap;



import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ifmapj.channel.ARC;
import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InternalIdentifierFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InternalMetadataFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.dataservice.util.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.dataservice.xml.DomHelper;
import de.hshannover.f4.trust.visitmeta.ifmap.dumpData.IdentifierData;
import de.hshannover.f4.trust.visitmeta.ifmap.dumpData.IfmapDataType;
import de.hshannover.f4.trust.visitmeta.ifmap.dumpData.IfmapMarshaller;
import de.hshannover.f4.trust.visitmeta.ifmap.dumpData.Link;
import de.hshannover.f4.trust.visitmeta.ifmap.dumpData.LinkConstructionException;
import de.hshannover.f4.trust.visitmeta.ifmap.dumpData.Metadata;
import de.hshannover.f4.trust.visitmeta.ifmap.dumpData.MetadataEntry;
import de.hshannover.f4.trust.visitmeta.ifmap.dumpData.NoValidIdentifierTypeException;
import de.hshannover.f4.trust.visitmeta.ifmap.dumpData.PollResultContainer;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionCloseException;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionException;
import de.hshannover.f4.trust.visitmeta.persistence.Writer;
import de.hshannover.f4.trust.visitmeta.persistence.inmemory.InMemoryMetadata;
import de.hshannover.f4.trust.visitmeta.util.PropertiesReaderWriter;

/**
 * When a <tt>UpdateService</tt> is started, it will subscribe for the
 * configured start identifier and after that continuously poll for
 * new information on that subscription.
 *
 * @author Ralf Steuerwald
 *
 */
public class UpdateService implements Runnable {

	private static final Logger log = Logger.getLogger(UpdateService.class);

	protected final static int DEFAULT_MAX_RETRY = 10;
	protected final static int DEFAULT_RETRY_INTERVAL = 10;

	protected PropertiesReaderWriter config = Application.getIFMAPConfig();

	protected final int MAX_DEPTH = Integer.valueOf(config.getProperty(ConfigParameter.IFMAP_MAX_DEPTH));
	protected final int MAX_SIZE = Integer.valueOf(config.getProperty(ConfigParameter.IFMAP_MAX_SIZE));
	protected final int MAX_RETRY;
	protected final int RETRY_INTERVAL;

	private Connection mConnection;

	protected Writer mWriter;

	protected InternalIdentifierFactory mIdentifierFactory;

	protected InternalMetadataFactory mMetadataFactory;

	protected de.hshannover.f4.trust.visitmeta.ifmap.IfmapJHelper mIfmapJHelper;


	/**
	 * Create a new {@link UpdateService} which uses the given writer to submit new
	 * {@link PollResult}s to the application.
	 *
	 * @param writer
	 * @param identifierFactory
	 * @param metadataFactory
	 */
	public UpdateService(Connection connection, Writer writer, InternalIdentifierFactory identifierFactory, InternalMetadataFactory metadataFactory) {
		log.trace("new UpdateService() ...");

		if (writer == null) {
			throw new IllegalArgumentException("writer cannot be null");
		}
		if (identifierFactory == null) {
			throw new IllegalArgumentException("identifierFactory cannot be null");
		}
		if (metadataFactory == null) {
			throw new IllegalArgumentException("metadataFactory cannot be null");
		}

		mConnection = connection;
		mWriter = writer;
		mIdentifierFactory = identifierFactory;
		mMetadataFactory = metadataFactory;

		mIfmapJHelper = new de.hshannover.f4.trust.visitmeta.ifmap.IfmapJHelper(mIdentifierFactory);

		int tmp = 0;
		try {
			tmp = Integer.parseInt(config.getProperty(ConfigParameter.IFMAP_MAX_RETRY));
		} catch (NumberFormatException e) {
			tmp = DEFAULT_MAX_RETRY;
		}
		MAX_RETRY = tmp;
		try {
			tmp = Integer.parseInt(config.getProperty(ConfigParameter.IFMAP_RETRY_INTERVAL));
		} catch (NumberFormatException e) {
			tmp = DEFAULT_RETRY_INTERVAL;
		}
		RETRY_INTERVAL = tmp;

		log.trace("... new UpdateService() OK");
	}

	/**
	 * Establish a new {@link ARC} to the MAPS and continuously poll for new data. The
	 * poll results get forwarded to the application.
	 */
	@Override
	public void run() {
		log.debug("run() ...");

		while (!Thread.interrupted()) {
			PollTask task = new PollTask(mConnection, mMetadataFactory, mIfmapJHelper);
			try {

				PollResult pollResult = task.call();

				PollResultContainer poll = null;
				try {
					poll = IfmapMarshaller.marshallPollResult(pollResult.getIfmapJPollResult());
				} catch (RemoteException e) {
					log.error("error while marshallPollResult", e);
				} catch (NoValidIdentifierTypeException e) {
					log.error("error while marshallPollResult", e);
				} catch (LinkConstructionException e) {
					log.error("error while marshallPollResult", e);
				}
				poll = IfmapMarshaller.filterPollResult(mConnection, poll);


				PollResult newTransformPollResult = transformPollResultContainer(poll);

				mWriter.submitPollResult(newTransformPollResult);

			} catch (ConnectionCloseException e) {
				log.debug("Stop polling while: " + e.toString());
				break;

			} catch (PollException  e) {
				log.error(e.toString(), e);
				break;

			} catch (ConnectionException e) {
				log.error(e.toString(), e);
				break;
			}
		}
		log.debug("... run()");
	}

	private PollResult transformPollResultContainer(PollResultContainer poll) {
		List<ResultItem> updates = new ArrayList<>();
		List<ResultItem> deletes = new ArrayList<>();
		for (IfmapDataType ifmapDataType : poll.getNew()) {
			ResultItem resultItem = transformResultItem(ifmapDataType);
			if (resultItem.getMetadata().size() > 0) {
				updates.add(resultItem);
			}
		}
		for (IfmapDataType ifmapDataType : poll.getUpdate()) {
			ResultItem resultItem = transformResultItem(ifmapDataType);
			if (resultItem.getMetadata().size() > 0) {
				updates.add(resultItem);
			}
		}
		for (IfmapDataType ifmapDataType : poll.getDelete()) {
			ResultItem resultItem = transformResultItem(ifmapDataType);
			if (resultItem.getMetadata().size() > 0) {
				deletes.add(resultItem);
			}
		}
		return new PollResult(updates, deletes, null);
	}

	/**
	 * Transforms a ifmapj {@link de.hshannover.f4.trust.ifmapj.messages.ResultItem}
	 * into a internal {@link ResultItem}.
	 */
	ResultItem transformResultItem(IfmapDataType ifmapDataType) {
		List<Metadata> metadataDocuments = ifmapDataType.getMetadata();

		List<InternalMetadata> metadata = new ArrayList<>(metadataDocuments.size());
		for (Metadata d : metadataDocuments) {
			InternalMetadata m = createMetadata(d);
			metadata.add(m);
		}

		if (ifmapDataType instanceof Link) {
			InternalIdentifier id1 = mIfmapJHelper.ifmapjIdentifierToInternalIdentifier(((Link)ifmapDataType).getIdentifier1().getRequestObject());
			InternalIdentifier id2 = mIfmapJHelper.ifmapjIdentifierToInternalIdentifier(((Link)ifmapDataType).getIdentifier2().getRequestObject());
			return new ResultItem(id1, id2, metadata);
		} else {
			InternalIdentifier id = mIfmapJHelper.ifmapjIdentifierToInternalIdentifier(((IdentifierData)ifmapDataType).getRequestObject());
			return new ResultItem(id, null, metadata);
		}
	}

	public InternalMetadata createMetadata(Metadata document) {
		log.trace("creating metadata from XML");

		// extract metadata object attributes
		String typename = document.getDocument().getDocumentElement().getLocalName();
		boolean isSingleValueMetadata = document.getCardinality().equals("singleValue");
		Calendar cal = DatatypeConverter.parseDateTime(document.getTimestamp());
		long timestamp = cal.getTime().getTime();

		// recursively collect all information under the top-level element
		Map<String, String> properties = document.getAttributes();

		// create a internal metadata object
		InMemoryMetadata metadata = new InMemoryMetadata(typename, isSingleValueMetadata, timestamp);
		metadata.setRawData(DomHelper.documentToString(document.getDocument()));

		metadata.addProperty("/" + document.getName() + "[@ifmap-publisher-id]", document.getPublisher());
		metadata.addProperty("/" + document.getName() + "[@ifmap-timestamp]", document.getTimestamp());
		metadata.addProperty("/" + document.getName() + "[@ifmap-cardinality]", document.getCardinality());

		for (Entry<String, String> e : properties.entrySet()) {
			metadata.addProperty("/" + document.getName() + "[@" + e.getKey() + "]", e.getValue());
		}

		for(MetadataEntry metaEntry: document.getChilds()){
			metadata.addProperty("/" + document.getName() + "[@" + metaEntry.getName() + "]", metaEntry.getValue());
			for (Entry<String, String> ee : metaEntry.getAttributes().entrySet()) {
				metadata.addProperty("/" + document.getName() + "/" + metaEntry.getName() + "[@" + ee.getKey() + "]", ee.getValue());
			}
		}

		return metadata;
	}
}
