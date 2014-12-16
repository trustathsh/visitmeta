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
package de.hshannover.f4.trust.visitmeta.datawrapper;





import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Container class for any graph updates.
 */
public class UpdateContainer {

	private static final Logger LOGGER = Logger.getLogger(UpdateContainer.class);

	private List<NodeIdentifier>                        mListAddIdentifier;
	private List<ExpandedLink>                          mListAddLinks;
	private HashMap<NodeIdentifier, List<NodeMetadata>> mListAddMetadataIdentifier;
	private HashMap<ExpandedLink,   List<NodeMetadata>> mListAddMetadataLinks;
	private HashMap<NodeIdentifier, List<NodeMetadata>> mListDeleteMetadataIdentifier;
	private HashMap<ExpandedLink,   List<NodeMetadata>> mListDeleteMetadataLinks;
	private List<NodeIdentifier>                        mListDeleteIdentifier;
	private List<ExpandedLink>                          mListDeleteLinks;

	public UpdateContainer() {
		mListAddIdentifier            = new ArrayList<>();
		mListAddLinks                 = new ArrayList<>();
		mListAddMetadataIdentifier    = new HashMap<>();
		mListAddMetadataLinks         = new HashMap<>();
		mListDeleteMetadataIdentifier = new HashMap<>();
		mListDeleteMetadataLinks      = new HashMap<>();
		mListDeleteIdentifier         = new ArrayList<>();
		mListDeleteLinks              = new ArrayList<>();

	}
	private UpdateContainer(UpdateContainer update) {
		mListAddIdentifier            = new ArrayList<>(update.getListAddIdentifier());
		mListAddLinks                 = new ArrayList<>(update.getListAddLinks());
		mListAddMetadataIdentifier    = new HashMap<>(update.getListAddMetadataIdentifier());
		mListAddMetadataLinks         = new HashMap<>(update.getListAddMetadataLinks());
		mListDeleteMetadataIdentifier = new HashMap<>(update.getListDeleteMetadataIdentifier());
		mListDeleteMetadataLinks      = new HashMap<>(update.getListDeleteMetadataLinks());
		mListDeleteIdentifier         = new ArrayList<>(update.getListDeleteIdentifier());
		mListDeleteLinks              = new ArrayList<>(update.getListDeleteLinks());
	}
	public synchronized List<NodeIdentifier> getListAddIdentifier() {
		LOGGER.trace("Method getListAddIdentifier() called.");
		return mListAddIdentifier;
	}
	public synchronized List<ExpandedLink> getListAddLinks() {
		LOGGER.trace("Method getListAddLinks() called.");
		return mListAddLinks;
	}
	public synchronized HashMap<NodeIdentifier, List<NodeMetadata>> getListAddMetadataIdentifier() {
		LOGGER.trace("Method getListAddMetadataIdentifier() called.");
		return mListAddMetadataIdentifier;
	}
	public synchronized HashMap<ExpandedLink, List<NodeMetadata>> getListAddMetadataLinks() {
		LOGGER.trace("Method getListAddMetadataLinks() called.");
		return mListAddMetadataLinks;
	}
	public synchronized HashMap<NodeIdentifier, List<NodeMetadata>> getListDeleteMetadataIdentifier() {
		LOGGER.trace("Method getListDeleteMetadataIdentifier() called.");
		return mListDeleteMetadataIdentifier;
	}
	public synchronized HashMap<ExpandedLink, List<NodeMetadata>> getListDeleteMetadataLinks() {
		LOGGER.trace("Method getListDeleteMetadataLinks() called.");
		return mListDeleteMetadataLinks;
	}
	public synchronized List<NodeIdentifier> getListDeleteIdentifier() {
		LOGGER.trace("Method getListDeleteIdentifier() called.");
		return mListDeleteIdentifier;
	}
	public synchronized List<ExpandedLink> getListDeleteLinks() {
		LOGGER.trace("Method getListDeleteLinks() called.");
		return mListDeleteLinks;
	}
	/**
	 * Create a flat copy of the UpdateContainer.
	 * @return the flat copy.
	 */
	public synchronized UpdateContainer getCopyFlat() {
		LOGGER.trace("Method getCopyFlat() called.");
		return new UpdateContainer(this);
	}
}
