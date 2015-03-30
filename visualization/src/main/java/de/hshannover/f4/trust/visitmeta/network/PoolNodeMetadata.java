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
 * This file is part of visitmeta-visualization, version 0.4.1,
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
package de.hshannover.f4.trust.visitmeta.network;

import java.util.HashMap;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.NodeMetadata;
import de.hshannover.f4.trust.visitmeta.datawrapper.RichMetadata;

/**
 * Pool class that contains a mapping of all Metadata to NodeMetadata in the
 * current layout.
 */
public class PoolNodeMetadata {
	private static final Logger LOGGER = Logger.getLogger(PoolNodeMetadata.class);

	private HashMap<RichMetadata, NodeMetadata> mPoolMetadataActive = new HashMap<RichMetadata, NodeMetadata>();

	/**
	 * @param metadata
	 *            The Metadata for the NodeMetadata.
	 * @return The NodeMetadata or null if the NodeMetadata does not exist.
	 */
	public NodeMetadata getMetadata(RichMetadata metadata) {
		return mPoolMetadataActive.get(metadata);
	}

	/**
	 * Removes a NodeMetadata.
	 * 
	 * @param metadata
	 *            The Metadata that references the NodeMetadata.
	 */
	public void release(RichMetadata metadata) {
		mPoolMetadataActive.remove(metadata);
	}

	/**
	 * Removes all NodeMetadata.
	 */
	public void clear() {
		LOGGER.trace("Method clear() called.");
		mPoolMetadataActive.clear();
	}

	/**
	 * @param metadata
	 *            The Metadata for the NodeMetadata.
	 * @return The new NodeMetadata or null if the NodeMetadata already existed.
	 */
	public NodeMetadata create(RichMetadata metadata) {
		if (mPoolMetadataActive.containsKey(metadata)) {
			LOGGER.debug("Found existing metadata in create(metadata).");
			return null;
		} else {
			LOGGER.debug("Create new metadata.");
			NodeMetadata nodeMetadata = new NodeMetadata(metadata);
			mPoolMetadataActive.put(metadata, nodeMetadata);
			return nodeMetadata;
		}
	}

	/**
	 * @param metadata
	 *            The Metadata for the NodeMetadata.
	 * @return The new NodeMetadata or the already existing NodeMetadata
	 */
	public NodeMetadata createOrGet(RichMetadata metadata) {
		if (mPoolMetadataActive.containsKey(metadata)) {
			LOGGER.debug("Found existing metadata in createOrGet(metadata).");
			return getMetadata(metadata);
		} else {
			LOGGER.debug("Create new metadata.");
			NodeMetadata nodeMetadata = new NodeMetadata(metadata);
			mPoolMetadataActive.put(metadata, nodeMetadata);
			return nodeMetadata;
		}
	}
}
