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
 * This file is part of visitmeta-visualization, version 0.6.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2016 Trust@HsH
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

import de.hshannover.f4.trust.visitmeta.datawrapper.ExpandedLink;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeIdentifier;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeMetadata;
import de.hshannover.f4.trust.visitmeta.datawrapper.RichMetadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;

/**
 * This is a class containing all pools which are necessary for a connection.
 */
public class GraphPool {
	private PoolNodeIdentifier mIdentifierPool;
	private PoolExpandedLink mLinkPool;
	private PoolNodeMetadata mMetadataPool;

	/**
	 * Initializes the pool classes and connects them correctly.
	 */
	public GraphPool() {
		mMetadataPool = new PoolNodeMetadata();
		mIdentifierPool = new PoolNodeIdentifier(mMetadataPool);
		mLinkPool = new PoolExpandedLink(mIdentifierPool, mMetadataPool);
	}

	public PoolNodeIdentifier getIdentifierPool() {
		return mIdentifierPool;
	}

	public PoolExpandedLink getLinkPool() {
		return mLinkPool;
	}

	public PoolNodeMetadata getMetadataPool() {
		return mMetadataPool;
	}

	/**
	 * Clears each pool.
	 */
	public void clearGraph() {
		mIdentifierPool.clear();
		mMetadataPool.clear();
		mLinkPool.clear();
	}

	/**
	 * @see PoolNodeIdentifier#createIdentifier(Identifier)
	 * @param identifier
	 * @return NodeIdentifier or null.
	 */
	public NodeIdentifier createIdentifier(Identifier identifier) {
		return mIdentifierPool.create(identifier);
	}

	/**
	 * @see PoolNodeIdentifier#getIdentifier(Identifier)
	 * @param identifier
	 * @return NodeIdetifier or null.
	 */
	public NodeIdentifier getIdentifier(Identifier identifier) {
		return mIdentifierPool.getIdentifier(identifier);
	}

	/**
	 * @see PoolNodeIdentifier#release(Identifier)
	 * @param identifier
	 */
	public void releaseIdentifier(Identifier identifier) {
		mIdentifierPool.release(identifier);
	}

	/**
	 * @see PoolExpandedLink#create(Link)
	 * @param link
	 * @return ExpandedLink or null.
	 */
	public ExpandedLink createLink(Link link) {
		return mLinkPool.create(link);
	}

	/**
	 * @see PoolExpandedLink#getLink(Link)
	 * @param link
	 * @return ExpanedLink or null.
	 */
	public ExpandedLink getLink(Link link) {
		return mLinkPool.getLink(link);
	}

	/**
	 * @see PoolExpandedLink#release(Link)
	 * @param link
	 */
	public void releaseLink(Link link) {
		mLinkPool.release(link);
	}

	/**
	 * @see PoolNodeMetadata#create(RichMetadata)
	 * @param metadata
	 * @return NodeMetadata or null.
	 */
	public NodeMetadata createMetadata(RichMetadata metadata) {
		return mMetadataPool.create(metadata);
	}

	/**
	 * @see PoolNodeMetadata#getMetadata(RichMetadata)
	 * @param metadata
	 * @return NodeMetadata or null.
	 */
	public NodeMetadata getMetadata(RichMetadata metadata) {
		return mMetadataPool.getMetadata(metadata);
	}

	/**
	 * @see PoolNodeMetadata#release(RichMetadata)
	 * @param metadata
	 */
	public void releaseMetadata(RichMetadata metadata) {
		mMetadataPool.release(metadata);
	}
}
