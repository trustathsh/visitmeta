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
 * This file is part of visitmeta-visualization, version 0.3.0,
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.ExpandedLink;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeMetadata;
import de.hshannover.f4.trust.visitmeta.datawrapper.RichMetadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Pool class that contains a mapping of all Links to ExpandedLinks in the
 * current layout.
 */
public class PoolExpandedLink {
	private static final Logger LOGGER = Logger.getLogger(PoolExpandedLink.class);

	private PoolNodeIdentifier mIdentifierPool;
	private PoolNodeMetadata mMetadataPool;
	private HashMap<Link, ExpandedLink> mLinkPool;

	/**
	 * @param identifierPool
	 *            Identifier pool related to the Link pool.
	 * @param metadataPool
	 *            Metadata pool related to the Link pool.
	 */
	public PoolExpandedLink(PoolNodeIdentifier identifierPool, PoolNodeMetadata metadataPool) {
		mIdentifierPool = identifierPool;
		mMetadataPool = metadataPool;
		mLinkPool = new HashMap<Link, ExpandedLink>();
	}

	/**
	 * @param link
	 *            The Link for the ExpandedLink.
	 * @return The new ExpandedLink or null if the ExpandedLink does not exist.
	 */
	public ExpandedLink getLink(Link link) {
		return mLinkPool.get(link);
	}

	/**
	 * Removes an ExpandedLink.
	 * 
	 * @param link
	 *            The Link that references the ExpandedLink.
	 */
	public void release(Link link) {
		mLinkPool.remove(link);
	}

	/**
	 * Removes all ExpandedLinks.
	 */
	public void clear() {
		mLinkPool.clear();
	}

	/**
	 * @param link
	 *            The Link for the ExpandedLink.
	 * @return The new ExpandedLink or null if the ExpandedLink already existed.
	 */
	public ExpandedLink create(Link link) {
		if (mLinkPool.containsKey(link)) {
			return null;
		} else {
			LOGGER.debug("Create new link.");
			List<NodeMetadata> metaList = new ArrayList<NodeMetadata>();
			for (Metadata metadata : link.getMetadata()) {
				NodeMetadata nodeMetadata = mMetadataPool.createOrGet(new RichMetadata(metadata, link));
				metaList.add(nodeMetadata);
			}
			ExpandedLink expandedLink = new ExpandedLink(link, mIdentifierPool.getIdentifier(link.getIdentifiers()
					.getFirst()), mIdentifierPool.getIdentifier(link.getIdentifiers().getSecond()), metaList);
			mLinkPool.put(link, expandedLink);
			return expandedLink;
		}
	}
}
