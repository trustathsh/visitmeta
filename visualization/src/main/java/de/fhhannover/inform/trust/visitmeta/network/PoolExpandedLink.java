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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.visitmeta.datawrapper.ExpandedLink;
import de.fhhannover.inform.trust.visitmeta.datawrapper.NodeMetadata;
import de.fhhannover.inform.trust.visitmeta.interfaces.Link;
import de.fhhannover.inform.trust.visitmeta.interfaces.Metadata;

/**
 * Pool class that contains a mapping of all Links to ExpandedLinks in the current layout.
 * TODO Pool must manage the count of entities.
 */
public class PoolExpandedLink {

	private static final Logger LOGGER = Logger.getLogger(PoolExpandedLink.class);

	private static HashMap<Link,ExpandedLink> mPoolLinksActive  = new HashMap<Link,ExpandedLink>();
//	private static HashMap<Link,ExpandedLink> mPoolLinksSuspend = new HashMap<Link,ExpandedLink>();

	/**
	 * Get a ExpandedLink.
	 * @param link the Link for the ExpandedLink.
	 * @return the new ExpandedLink (active) or
	 * Null if the ExpandedLink doesn't exist.
	 */
	public static ExpandedLink getActive(Link link) {
		LOGGER.trace("Method getActive(" + link + ") called.");
		return mPoolLinksActive.get(link);
	}

//	/**
//	 * Get a ExpandedLink.
//	 * @param link the Link for the ExpandedLink.
//	 * @return the new ExpandedLink (active or suspend) or
//	 * Null if the ExpandedLink doesn't exist.
//	 */
//	public static ExpandedLink getActiveOrSuspend(Link link) {
//		LOGGER.trace("Method getActiveOrSuspend(" + link + ") called.");
//		ExpandedLink result = mPoolLinksActive.get(link);
//		if(result == null) {
//			result = mPoolLinksSuspend.get(link);
//		}
//		return result;
//	}

	public static HashMap<Link,ExpandedLink> get() {
		LOGGER.trace("Method get() called.");
		return mPoolLinksActive;
	}

	/**
	 * Suspend a NodeIdentifier.
	 * @param link the Identifier that reference the NodeIdentifier.
	 */
	public static void release(Link link) {
		LOGGER.trace("Method release(" + link + ") called.");
//		mPoolLinksSuspend.put(link, mPoolLinksActive.get(link));
		mPoolLinksActive.remove(link);
	}

	/**
	 * Suspend all NodeIdentifier.
	 */
	public static void clear() {
		LOGGER.trace("Method clear() called.");
//		mPoolLinksSuspend.putAll(mPoolLinksActive);
		mPoolLinksActive.clear();
	}

	/**
	 * Create a new ExpandedLink.
	 * @param link the Link for the ExpandedLink.
	 * @return the new ExpandedLink or null if the ExpandedLink already existed.
	 */
	public static ExpandedLink create(Link link) {
		LOGGER.trace("Method create(" + link + ") called.");
		if(mPoolLinksActive.containsKey(link)) {
			return null;
		} else {
			LOGGER.debug("Create new link.");
			List<NodeMetadata> metaList = new ArrayList<NodeMetadata>();
			for(Metadata metadata : link.getMetadata()) {
				NodeMetadata nodeMetadata = PoolNodeMetadata.createOrGet(metadata);
				metaList.add(nodeMetadata);
			}
			ExpandedLink expandedLink = new ExpandedLink(
					link,
					PoolNodeIdentifier.getActive(link.getIdentifiers().getFirst()),
					PoolNodeIdentifier.getActive(link.getIdentifiers().getSecond()),
					metaList
			);
			mPoolLinksActive.put(link, expandedLink);
			return expandedLink;
		}
	}

//	/**
//	 * Create or get a ExpandedLink and activate the ExpandedLink if suspend.
//	 * @param link the Link for the ExpandedLink.
//	 * @return the new ExpandedLink or the already existing ExpandedLink (active and suspend).
//	 */
//	public static ExpandedLink createOrActivate(Link link) {
//		LOGGER.trace("Method create(" + link + ") called.");
//		if(mPoolLinksActive.containsKey(link)) {
//			/* ExpandedLink is active*/
//			return null;
//		} else if(mPoolLinksSuspend.containsKey(link)) {
//			/* Activate ExpandedLink */
//			ExpandedLink expandedLink = mPoolLinksSuspend.get(link);
//			mPoolLinksActive.put(link, expandedLink);
//			mPoolLinksSuspend.remove(link);
//			return expandedLink;
//		} else {
//			/* Create new ExpandedLink */
//			LOGGER.debug("Create new link.");
//			List<NodeMetadata> metaList = new ArrayList<NodeMetadata>();
//			for(Metadata metadata : link.getMetadata()) {
//				NodeMetadata nodeMetadata = PoolNodeMetadata.createOrGet(metadata);
//				metaList.add(nodeMetadata);
//			}
//			ExpandedLink expandedLink = new ExpandedLink(
//					link,
//					PoolNodeIdentifier.getActive(link.getIdentifiers().getFirst()),
//					PoolNodeIdentifier.getActive(link.getIdentifiers().getSecond()),
//					metaList
//			);
//			mPoolLinksActive.put(link, expandedLink);
//			return expandedLink;
//		}
//	}

	/**
	 * Create or get a ExpandedLink.
	 * @param link the Link for the ExpandedLink.
	 * @return the new ExpandedLink or the already existing ExpandedLink (active).
	 * Null if the ExpandedLink is suspend.
	 */
	public static ExpandedLink createOrGet(Link link) {
		LOGGER.trace("Method createOrGet(" + link + ") called.");
		if(mPoolLinksActive.containsKey(link)) {
			LOGGER.debug("Found existing link.");
			return getActive(link);
		} else {
			LOGGER.debug("Create new link.");
			List<NodeMetadata> metaList = new ArrayList<NodeMetadata>();
			for(Metadata metadata : link.getMetadata()) {
				NodeMetadata nodeMetadata = PoolNodeMetadata.createOrGet(metadata);
				metaList.add(nodeMetadata);
			}
			ExpandedLink expandedLink = new ExpandedLink(
					link,
					PoolNodeIdentifier.getActive(link.getIdentifiers().getFirst()),
					PoolNodeIdentifier.getActive(link.getIdentifiers().getSecond()),
					metaList
			);
			mPoolLinksActive.put(link, expandedLink);
			return expandedLink;
		}
	}

}
