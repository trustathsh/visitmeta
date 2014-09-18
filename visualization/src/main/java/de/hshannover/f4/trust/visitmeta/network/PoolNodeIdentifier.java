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
 * This file is part of visitmeta-visualization, version 0.2.0,
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
package de.hshannover.f4.trust.visitmeta.network;

import java.util.HashMap;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.NodeIdentifier;
import de.hshannover.f4.trust.visitmeta.datawrapper.RichMetadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Pool class that contains a mapping of all Identifiers to NodeIdentifiers in
 * the current layout.
 */
public class PoolNodeIdentifier {
	private static final Logger LOGGER = Logger.getLogger(PoolNodeIdentifier.class);

	private PoolNodeMetadata mMetadataPool;
	private HashMap<Identifier, NodeIdentifier> mIdentifierPool = new HashMap<Identifier, NodeIdentifier>();

	/**
	 * @param metadataPool
	 *            Metadata pool related to the Identifier pool.
	 */
	public PoolNodeIdentifier(PoolNodeMetadata metadataPool) {
		mMetadataPool = metadataPool;
	}

	/**
	 * @param identifier
	 *            The Identifier for the NodeIdentifier.
	 * @return The NodeIdentifier or null if the NodeIdentifier does not exist.
	 */
	public NodeIdentifier getIdentifier(Identifier identifier) {
		return mIdentifierPool.get(identifier);
	}

	/**
	 * Removes a NodeIdentifier.
	 * 
	 * @param identifier
	 *            The Identifier that reference the ExpandedLink.
	 */
	public void release(Identifier identifier) {
		mIdentifierPool.remove(identifier);
	}

	/**
	 * Removes all NodeIdentifier.
	 */
	public void clear() {
		mIdentifierPool.clear();
	}

	/**
	 * @param identifier
	 *            The Identifier for the NodeIdentifier.
	 * @return The new NodeIdentifier or null if the NodeIdentifier already
	 *         existed.
	 */
	public NodeIdentifier create(Identifier identifier) {
		if (mIdentifierPool.containsKey(identifier)) {
			return null;
		} else {
			LOGGER.debug("Create new identifier.");
			NodeIdentifier nodeIdentifier = new NodeIdentifier(identifier);
			for (Metadata metadata : identifier.getMetadata()) {
				nodeIdentifier.addNodeMetadata(mMetadataPool.createOrGet(new RichMetadata(metadata, identifier)));
			}
			mIdentifierPool.put(identifier, nodeIdentifier);
			return nodeIdentifier;
		}
	}
}
