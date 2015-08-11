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

import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeDelete;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeUpdate;
import de.hshannover.f4.trust.visitmeta.interfaces.data.SubscriptionData;

public class SubscriptionHelper {

	public static SubscribeRequest buildUpdateRequest(SubscriptionData subscription) {
		SubscribeRequest request = Requests.createSubscribeReq();
		SubscribeUpdate subscribe = Requests.createSubscribeUpdate();

		subscribe.setName(subscription.getName());
		subscribe.setMaxDepth(subscription.getMaxDepth());
		subscribe.setMaxSize(subscription.getMaxSize());
		subscribe.setMatchLinksFilter(subscription.getMatchLinksFilter());
		subscribe.setResultFilter(subscription.getResultFilter());
		subscribe.setTerminalIdentifierTypes(subscription.getTerminalIdentifierTypes());
		subscribe.setStartIdentifier(
				createStartIdentifier(subscription.getIdentifierType(), subscription.getStartIdentifier()));

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
						throw new RuntimeException("unknown IP address type '"
								+ split[0] + "'");
				}
			case "mac-address":
				return Identifiers.createMac(sIdentifier);

			// TODO identity and extended identifiers

			default:
				throw new RuntimeException("unknown identifier type '"
						+ sIdentifierType + "'");
		}
	}

}
