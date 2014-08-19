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
 * This file is part of visitmeta dataservice, version 0.1.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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
/** Project: Metalyzer
 * Author: Michael Felchner
 * Author: Mihaela Stein
 * Author: Sven Steinbach
 * Last Change:
 * 	by: $Author: $
 * 	date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

package de.hshannover.f4.trust.metalyzer.semantics.helper;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.semantics.entities.User;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierPair;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Helper-Class which provides User-Helper-Methods to extract information out of the IF-MAP-Graph.
 *
 */
public abstract class UserHelper {
	
	// Logging:
	private static final Logger log = Logger.getLogger(UserHelper.class);
	
	/**
	 * Helper-Method to find user-identifiers from other identifiers (mainly used with access-request-identifiers).
	 * @param accessRequest
	 * Identifier to start from (should be an access-request).
	 * @param users
	 * ArrayList where the users will be saved in.
	 */
	public static void findUsersFromIdentifier(Identifier accessRequest, ArrayList<User> users) {
		log.info("Call of the helper-method 'findUsersFromIdentifier'");
		
		//Check all links.
		for (Link link : accessRequest.getLinks()) {
			IdentifierPair identifierPair = link.getIdentifiers();
			Identifier first = identifierPair.getFirst();
			Identifier second = identifierPair.getSecond();

			//Searching for the publish-timestamp and the role of the user.
			long timestamp = 0;
			String role ="";
			for (Metadata meta : link.getMetadata()) {
				//Searching for the corresponding publish-timestamp:
				if (meta.getTypeName().equals("authenticated-as")) {
					timestamp = meta.getPublishTimestamp();
				}
			}

			//First identifier of the link is the user.
			if (first.getTypeName().equals("identity")) {
				for (Link userLink : first.getLinks()) {
					for (Metadata linkMeta : userLink.getMetadata()) {
						if (linkMeta.getTypeName().equals("role")) {
							role = linkMeta.valueFor("/meta:role/name");
						}
					}
				}
				
				User user = new User(timestamp, first.valueFor("/identity[@name]"), role);
				
				//Check for duplicates.
				if (!users.contains(user) && user.getTimestamp() != 0) {
					users.add(user);
				}

				//Second identifier of the link is the user.
			} else if(second.getTypeName().equals("identity")) {
				for (Link userLink : second.getLinks()) {
					for (Metadata linkMeta : userLink.getMetadata()) {
						if (linkMeta.getTypeName().equals("role")) {
							role = linkMeta.valueFor("/meta:role/name");
						}
					}
				}
				
				User user = new User(timestamp, second.valueFor("/identity[@name]"), role);
				
				//Check for duplicates.
				if (!users.contains(user) && user.getTimestamp() != 0) {
					users.add(user);
				}
			}
		}
	}
	
	/**
	 * Helper-Method to find user-identifiers from any other identifier.
	 * @param identifier
	 * Identifier to start from (should be an IP-Address or a MAC-Address).
	 * @param devices
	 * ArrayList where the users will be saved in.
	 */
	public static void findUsersOverIdentifier(Identifier identifier, ArrayList<User> users) {
		log.info("Call of the helper-method 'findUsersOverIdentifier'");
		
		//Check all links.
		for (Link link : identifier.getLinks()) {
			IdentifierPair identifierPair = link.getIdentifiers();
			Identifier first = identifierPair.getFirst();
			Identifier second = identifierPair.getSecond();
			
			//First identifier of the link is the access-request.
			if (first.getTypeName().equals("access-request")) {
				findUsersFromIdentifier(first, users);
				
				//Second identifier of the link is the access-request.
			} else if (second.getTypeName().equals("access-request")) {
				findUsersFromIdentifier(second, users);
			}
		}
	}
}
