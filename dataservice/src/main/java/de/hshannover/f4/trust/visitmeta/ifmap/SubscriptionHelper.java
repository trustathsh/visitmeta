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
 * This file is part of visitmeta-dataservice, version 0.4.2,
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

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeDelete;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeUpdate;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.util.SubscriptionKey;
import de.hshannover.f4.trust.visitmeta.util.yaml.ConnectionsProperties;

public class SubscriptionHelper {

	private static final Logger log = Logger.getLogger(SubscriptionHelper.class);

	public static SubscribeRequest buildUpdateRequest(SubscriptionData subscription) {
		SubscribeRequest request = Requests.createSubscribeReq();
		SubscribeUpdate subscribe = Requests.createSubscribeUpdate();

		subscribe.setName(subscription.getName());
		subscribe.setMaxDepth(subscription.getMaxDepth());
		subscribe.setMaxSize(subscription.getMaxSize());
		subscribe.setMatchLinksFilter(subscription.getMatchLinksFilter());
		subscribe.setResultFilter(subscription.getResultFilter());
		subscribe.setTerminalIdentifierTypes(subscription.getTerminalIdentifierTypes());
		subscribe.setStartIdentifier(createStartIdentifier(subscription.getIdentifierType(), subscription.getStartIdentifier()));

		request.addSubscribeElement(subscribe);

		return request;
	}

	public static SubscribeRequest buildDeleteRequest(SubscriptionData subscription) {
		SubscribeRequest request = Requests.createSubscribeReq();
		SubscribeDelete subscribe = Requests.createSubscribeDelete();

		subscribe.setName(subscription.getName());

		request.addSubscribeElement(subscribe);

		return request;
	}

	public static SubscribeRequest buildRequest(JSONObject jObj) {
		return buildUpdateRequest(buildSubscribtion(jObj));
	}

	public static Subscription buildSubscribtion(JSONObject jObj) {
		Subscription subscription = new SubscriptionImpl();

		subscription.setMaxDepth(ConnectionsProperties.DEFAULT_SUBSCRIPTION_MAX_DEPTH);
		subscription.setMaxSize(ConnectionsProperties.DEFAULT_MAX_POLL_RESULT_SIZE);

		Iterator<String> i = jObj.keys();
		while (i.hasNext()) {
			String jKey = i.next();

			switch (jKey) {
			case SubscriptionKey.SUBSCRIPTION_NAME: subscription.setName(jObj.optString(jKey)); break;
			case SubscriptionKey.IDENTIFIER_TYPE: subscription.setIdentifierType(jObj.optString(jKey)); break;
			case SubscriptionKey.START_IDENTIFIER: subscription.setStartIdentifier(jObj.optString(jKey)); break;
			case SubscriptionKey.MAX_DEPTH: subscription.setMaxDepth(jObj.optInt(jKey)); break;
			case SubscriptionKey.MAX_SIZE: subscription.setMaxSize(jObj.optInt(jKey)); break;
			case SubscriptionKey.MATCH_LINKS_FILTER: subscription.setMatchLinksFilter(jObj.optString(jKey)); break;
			case SubscriptionKey.RESULT_FILTER: subscription.setResultFilter(jObj.optString(jKey)); break;
			case SubscriptionKey.TERMINAL_IDENTIFIER_TYPES: subscription.setTerminalIdentifierTypes(jObj.optString(jKey)); break;
			case SubscriptionKey.USE_SUBSCRIPTION_AS_STARTUP: subscription.setStartupSubscribe(jObj.optBoolean(jKey)); break;
			default: log.warn("The key: \"" + jKey + "\" is not a valide JSON-Key for subscriptions."); break;
			}
		}

		return subscription;
	}

	private static Identifier createStartIdentifier(String sIdentifierType, String sIdentifier) {
		switch (sIdentifierType) {
		case "device":
			return Identifiers.createDev(sIdentifier);
		case "access-request":
			return Identifiers.createAr(sIdentifier);
		case "ip-address":
			String[] split = sIdentifier.split(",");
			switch (split[0]) {
			case "IPv4":
				return Identifiers.createIp4(split[1]);
			case "IPv6":
				return Identifiers.createIp6(split[1]);
			default:
				throw new RuntimeException("unknown IP address type '"+split[0]+"'");
			}
		case "mac-address":
			return Identifiers.createMac(sIdentifier);

			// TODO identity and extended identifiers

		default:
			throw new RuntimeException("unknown identifier type '"+sIdentifierType+"'");
		}
	}

}
