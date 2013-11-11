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

import java.util.HashMap;

import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.visitmeta.datawrapper.NodeIdentifier;
import de.fhhannover.inform.trust.visitmeta.interfaces.Identifier;
import de.fhhannover.inform.trust.visitmeta.interfaces.Metadata;
/**
 * Pool class that contains a mapping of all Identifiers to NodeIdentifiers in the current layout.
 * TODO Pool must manage the count of entities.
 */
public class PoolNodeIdentifier {

	private static final Logger LOGGER = Logger.getLogger(PoolNodeIdentifier.class);

	private static HashMap<Identifier,NodeIdentifier> mPoolIdentifierActive  = new HashMap<Identifier,NodeIdentifier>();
//	private static HashMap<Identifier,NodeIdentifier> mPoolIdentifierSuspend = new HashMap<Identifier,NodeIdentifier>();

	/**
	 * Get a NodeIdentifier.
	 * @param identifier the Identifier for the NodeIdentifier.
	 * @return the NodeIdentifier (active) or
	 *         Null if the NodeIdentifier doesn't exist.
	 */
	public static NodeIdentifier getActive(Identifier identifier) {
		LOGGER.trace("Method getActive(" + identifier + ") called.");
		return mPoolIdentifierActive.get(identifier);
	}

//	/**
//	 * Get a NodeIdentifier.
//	 * @param identifier the Identifier for the NodeIdentifier.
//	 * @return the NodeIdentifier (active or suspend) or
//	 *         Null if the NodeIdentifier doesn't exist.
//	 */
//	public static NodeIdentifier getActiveOrSuspend(Identifier identifier) {
//		LOGGER.trace("Method getActiveOrSuspend(" + identifier + ") called.");
//		NodeIdentifier result = mPoolIdentifierActive.get(identifier);
//		if(result == null) {
//			result = mPoolIdentifierSuspend.get(identifier);
//		}
//		return result;
//	}

	public static HashMap<Identifier,NodeIdentifier> get() {
		LOGGER.trace("Method get() called.");
		return mPoolIdentifierActive;
	}

	/**
	 * Suspend a NodeIdentifier.
	 * @param identifier the Identifier that reference the ExpandedLink.
	 */
	public static void release(Identifier identifier) {
		LOGGER.trace("Method release(" + identifier + ") called.");
//		mPoolIdentifierSuspend.put(identifier, mPoolIdentifierActive.get(identifier));
		mPoolIdentifierActive.remove(identifier);
	}

	/**
	 * Suspend all NodeIdentifier.
	 */
	public static void clear() {
		LOGGER.trace("Method clear() called.");
//		mPoolIdentifierSuspend.putAll(mPoolIdentifierActive);
		mPoolIdentifierActive.clear();
	}

	/**
	 * Create a new NodeIdentifier.
	 * @param identifier the Identifier for the NodeIdentifier.
	 * @return the new NodeIdentifier or null if the NodeIdentifier already existed.
	 */
	public static NodeIdentifier create(Identifier identifier) {
		LOGGER.trace("Method create(" + identifier + ") called.");
		if(mPoolIdentifierActive.containsKey(identifier)) {
			return null;
		} else {
			LOGGER.debug("Create new identifier.");
			NodeIdentifier nodeIdentifier = new NodeIdentifier(identifier);
			for(Metadata metadata : identifier.getMetadata()) {
				nodeIdentifier.addNodeMetadata(PoolNodeMetadata.createOrGet(metadata));
			}
			mPoolIdentifierActive.put(identifier, nodeIdentifier);
			return nodeIdentifier;
		}
	}

//	/**
//	 * Create or get a NodeIdentifier and activate the NodeIdentifier if suspend.
//	 * @param identifier the Identifier for the NodeIdentifier.
//	 * @return the new NodeIdentifier or the already existing NodeIdentifier (active and suspend).
//	 */
//	public static NodeIdentifier createOrActivate(Identifier identifier) {
//		LOGGER.trace("Method createOrActive(" + identifier + ") called.");
//		if(mPoolIdentifierActive.containsKey(identifier)) {
//			/* NodeIdentifier is active */
//			return null;
//		} else if(mPoolIdentifierSuspend.containsKey(identifier)) {
//			/* Activate NodeIdentifier */
//			NodeIdentifier nodeIdentifier = mPoolIdentifierSuspend.get(identifier);
//			mPoolIdentifierActive.put(identifier, nodeIdentifier);
//			mPoolIdentifierSuspend.remove(identifier);
//			return nodeIdentifier;
//		} else {
//			/* Create new NodeIdentifier */
//			LOGGER.debug("Create new identifier.");
//			NodeIdentifier nodeIdentifier = new NodeIdentifier(identifier);
//			for(Metadata metadata : identifier.getMetadata()) {
//				nodeIdentifier.addNodeMetadata(PoolNodeMetadata.createOrGet(metadata));
//			}
//			mPoolIdentifierActive.put(identifier, nodeIdentifier);
//			return nodeIdentifier;
//		}
//	}

	/**
	 * Create or get a NodeIdentifier.
	 * @param identifier the Identifier for the NodeIdentifier.
	 * @return the new NodeIdentifier or the already existing NodeIdentifier (active).
	 *         Null if the NodeIdentifier is suspend.
	 */
	public static NodeIdentifier createOrGet(Identifier identifier) {
		LOGGER.trace("Method createOrGet(" + identifier + ") called.");
		if(mPoolIdentifierActive.containsKey(identifier)) {
			LOGGER.debug("Found existing identifier.");
			return getActive(identifier);
		} else {
			LOGGER.debug("Create new identifier.");
			NodeIdentifier nodeIdentifier = new NodeIdentifier(identifier);
			for(Metadata metadata : identifier.getMetadata()) {
				nodeIdentifier.addNodeMetadata(PoolNodeMetadata.createOrGet(metadata));
			}
			mPoolIdentifierActive.put(identifier, nodeIdentifier);
			return nodeIdentifier;
		}
	}

}
