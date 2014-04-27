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



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ifmapj.channel.SSRC;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.IfmapException;
import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.ifmapj.messages.Result;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeElement;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeUpdate;
import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.dataservice.util.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.dataservice.util.CryptoUtil;
import de.hshannover.f4.trust.visitmeta.ifmap.dumpData.IdentifierData;
import de.hshannover.f4.trust.visitmeta.ifmap.dumpData.SubscriptionRepository;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionException;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.IfmapConnectionException;
import de.hshannover.f4.trust.visitmeta.util.PropertiesReaderWriter;

public class DumpingService implements Runnable {


	private static final Logger log = Logger.getLogger(DumpingService.class);

	private static final PropertiesReaderWriter config = Application.getIFMAPConfig();

	private static Set<String> activeSubscriptions;

	private SSRC mSsrc;

	private Connection mConnection;

	static {
		Requests.registerRequestHandler(new DumpRequestHandler());
	}

	public DumpingService(Connection connection, SSRC ssrc){
		log.debug("new DumpingService...");

		activeSubscriptions = new HashSet<String>();
		mSsrc = ssrc;
		mConnection = connection;

		log.debug("... new DumpingService");
	}

	private List<IdentifierData> transformResult(Collection<Identifier> idents) {
		ArrayList<IdentifierData> l = new ArrayList<IdentifierData>();
		Iterator<Identifier> itr = idents.iterator();
		while (itr.hasNext()) {
			Identifier ident = itr.next();
			IdentifierData identData = new IdentifierData(ident);
			String uuid = CryptoUtil
					.generateMD5BySize(ident.toString(), 16);
			if (!SubscriptionRepository.getInstance().isAlreadySubscribed(
					mConnection, identData)) {
				identData.setSubscriptionName(uuid);
				l.add(identData);
			}

		}
		return l;
	}

	@Override
	public void run(){
		log.debug("run()...");

		long time = 0;
		activeSubscriptions.clear();

		while(!Thread.currentThread().isInterrupted()){

			DumpResult dump = null;
			long lastUpdate = 0;

			try {

				dump = dump(null);

			} catch (ConnectionException e) {
				break;
			}

			if(dump != null){
				lastUpdate = Long.parseLong(dump.getLastUpdate());

				if (lastUpdate > time) {
					time = lastUpdate;
					Collection<Identifier> identifier = dump.getIdentifiers();

					if (identifier != null && !identifier.isEmpty()) {
						// XXX bugfix
						List<IdentifierData> result = transformResult(identifier);
						if (!result.isEmpty()) {
							Iterator<IdentifierData> itr = result.iterator();
							while (itr.hasNext()) {
								IdentifierData id = itr.next();
								SubscriptionRepository.getInstance().addSubscription(mConnection, id);
							}
						}
						// XXX bugfix
						
						Set<String> newUuids;
						try {

							newUuids = subscribeUpdateForDumping(identifier, 1);

						} catch (ConnectionException e) {
							break;
						}
						activeSubscriptions.addAll(newUuids);
					}
				}
			}

			try {

				Thread.sleep(Integer.parseInt(config.getProperty(ConfigParameter.IFMAP_SUBSCRIPTION_DUMPING_SLEEPTIME)));

			} catch (InterruptedException e) {
				log.debug(e.getMessage());
				break;
			}
		}
		log.debug("...run()");
	}

	public DumpResult dump(String filter) throws ConnectionException {
		mConnection.checkIsConnectionEstablished();

		Result res = null;
		DumpRequest dreq = new DumpRequestImpl(filter);
		try {

			res = mSsrc.genericRequestWithSessionId(dreq);

		} catch (IfmapErrorResult e) {
			IfmapConnectionException ee = new IfmapConnectionException(e);
			log.error(ee.toString(), ee);
			throw ee;

		} catch (IfmapException e) {
			IfmapConnectionException ee = new IfmapConnectionException(e);
			log.error(ee.toString(), ee);
			throw ee;
		}

		if (!(res instanceof DumpResult)){
			IfmapConnectionException e = new IfmapConnectionException(new IfmapException("Can't cast to DumpResult", "If we don't get back a DumpResult instance"));
			log.error(e.toString(), e);
			throw e;
		}

		return (DumpResult) res;
	}

	private Set<String> subscribeUpdateForDumping(Collection<Identifier> idents, Integer depth) throws ConnectionException {
		log.debug("subscribeUpdateForDumping()...");

		Set<String> uuids = new HashSet<String>();

		if (idents != null && !idents.isEmpty()) {
			Iterator<Identifier> iter = idents.iterator();
			SubscribeRequest req = null;

			while (iter.hasNext()) {
				SubscribeUpdate update = Requests.createSubscribeUpdate();
				Identifier ident = iter.next();
				String uuid = CryptoUtil.generateMD5BySize(ident.toString(), 16);
				update.setName(uuid);
				update.setStartIdentifier(ident);

				update.setMaxSize(Integer.parseInt(config.getProperty(ConfigParameter.IFMAP_MAX_SIZE)));
				log.trace("Setting max_size for connection to: " + update.getMaxSize());

				if (depth != null && depth >= 0){
					update.setMaxDepth(depth);
				} else {
					update.setMaxDepth(Integer.parseInt(config.getProperty(ConfigParameter.IFMAP_MAX_DEPTH)));
				}
				log.trace("Setting max_depth for connection to: " + update.getMaxDepth());

				if(!activeSubscriptions.contains(uuid) && !uuids.contains(uuid)){
					if(req == null){
						req = Requests.createSubscribeReq();
					}

					req.addSubscribeElement(update);
					uuids.add(uuid);
				}
			}

			if(req != null){
				log.trace("send subscribe(req)...");
				subscribe(req);
				log.debug(uuids.size() + " new Identifier was subscribe");
			}
		}

		log.debug("...subscribeUpdateForDumping()");
		return uuids;
	}

	public void subscribe(SubscribeRequest request) throws ConnectionException {
		try {

			mSsrc.subscribe(request);

		} catch (IfmapErrorResult e) {
			IfmapConnectionException ee = new IfmapConnectionException(e);
			log.error(ee.toString(), ee);
			throw ee;

		} catch (IfmapException e) {
			IfmapConnectionException ee = new IfmapConnectionException(e);
			log.error(ee.toString(), ee);
			throw ee;
		}

		for(SubscribeElement r: request.getSubscribeElements()){
			activeSubscriptions.add(r.getName());
		}
	}
}