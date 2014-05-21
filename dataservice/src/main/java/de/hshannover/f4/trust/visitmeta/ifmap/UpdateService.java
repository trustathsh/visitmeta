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



import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ifmapj.channel.ARC;
import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InternalIdentifierFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InternalMetadataFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.util.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionCloseException;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionException;
import de.hshannover.f4.trust.visitmeta.persistence.Writer;
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
				mWriter.submitPollResult(pollResult);

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
}
